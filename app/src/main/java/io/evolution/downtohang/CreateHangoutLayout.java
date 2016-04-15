package io.evolution.downtohang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateHangoutLayout extends AppCompatActivity{

    List<User> onlineUsers = new ArrayList<User>();
    private LocalDB db;
    private ListView lv;
    private Context context;
    private Button hangoutButton;

    private OkHttpClient client;

    private String uuidLeader;
    private String selectedUuid;

    private SharedPreferences savedValues;
    private User you;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.limited_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_refresh:
                Toast.makeText(this, "Refresh Button", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hangout_layout);

        lv = (ListView) findViewById(R.id.createHangoutListView);
        context = this;
        db = new LocalDB(context);
        hangoutButton = (Button) findViewById(R.id.hangoutButton);
        client = new OkHttpClient();
        uuidLeader = you.getHangStatus();

        //populates list of users
        populateUsers();
        //populates the listView with items
        populateListView();
        //sets the onClickListener for the "Lets Hang!" button
        setOnClickListener();
    }

    public void generateYou() {
        String uuid = savedValues.getString("yourUUID",null);
        String username = savedValues.getString("yourName",null);
        String status = savedValues.getString("yourStatus",null);
        String hangoutStatus = savedValues.getString("yourHangoutStatus",null);
        String latitude = savedValues.getString("yourLat",null);
        String longitude = savedValues.getString("yourLong",null);
        assert status != null;
        you = new User(uuid,username,hangoutStatus,Integer.parseInt(status));
    }

    private void setOnClickListener() {
        hangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* must refresh local database and yourself, first thing */
                db.updateFriends(onlineUsers); //updates friends to current

                /* change leader hangoutStatus to leader's own uuid */
                you.setHangStatus(uuidLeader);
                you.setStatus(0);
                /* update you (leader) in online database */
                selectedUuid = you.getUUID();
                new UpdateUserHangStatus().execute();

                ArrayList<User> selectedUsers = new ArrayList<User>();
                for(User u: onlineUsers){
                    boolean selected = u.isSelected();
                    if(selected){
                        selectedUsers.add(u);
                    }//end if
                }//end for

                /* change members' hangoutStatus to leader's uuid and set status to 0 */
                for (User x: selectedUsers){
                    selectedUuid = x.getUUID();
                    //updates local database
                    x.setStatus(0);
                    x.setHangStatus(uuidLeader);
                    //updates online database
                    new  UpdateUserHangStatus().execute();
                }
            }//end onClick
        });
    }

    private void populateUsers() {
        ArrayList<User> users = db.getAllUsers();
        for(User u: users){
            if(u.getStatus() == 1){
                onlineUsers.add(u);
            }//end if
        }//end for
    }

    private void populateListView() {
        //Build Adapter
        ArrayAdapter<User> adapter = new MyArrayAdapter();

        //Configure the ListView
        lv.setAdapter(adapter);
    }

    private class MyArrayAdapter extends ArrayAdapter<User> {

        public MyArrayAdapter() {
            super(CreateHangoutLayout.this, R.layout.activity_item_layout, onlineUsers);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // username to be displayed
            User current = onlineUsers.get(position);

            CreateHangoutListItemLayout item = null;
            if (convertView == null){
                item = new CreateHangoutListItemLayout(getContext(), current);
            }else{
                item = (CreateHangoutListItemLayout) convertView;
            }
            return item;
        }//end of getView




    }//end of MyArrayListAdapter class

    /**
     * Go to the main activity.
     */
    public void goToMainActivity() {
        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("yourStatus", "0");
        editor.putString("yourHangoutStatus", uuidLeader);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    /*-----------------------------Asynchronus Task-----------------------------*/
    class UpdateUserHangStatus extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            try {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{"+
                                    "\"hangoutStatus\":" + uuidLeader +
                                    "\"status\":0}");
                Request request = new Request.Builder()
                        .url("http://www.3volution.io:4001/api/Users/update?where={\"uuid\": \"" + selectedUuid + "\"}")
                        .post(body)
                        .addHeader("x-ibm-client-id", "default")
                        .addHeader("x-ibm-client-secret", "SECRET")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                if(response.code() == 200) {
                    return "200";
                }
                else {
                    return response.message();
                }
            }catch (IOException e) {
                System.err.println(e);
                return "failed";
            }

        }

        /**
         * Actions to perform after the asynchronous request
         * @param message the message returned by the request
         */
        @Override
        protected void onPostExecute(String message) {
            if(message.equals("200")) {
                // success, do what you need to.
                goToMainActivity();
            }
            else if(message.equals("failed")) {
                //error
                System.err.println("error");
            }
            else {
                // HTTP Error Message
                System.err.println("HTTP Error Message");
            }
        }
    }

}//end of CreateHangoutActivity
