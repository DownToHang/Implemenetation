package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by eliakah on 4/2/2016.
 */
public class HangoutActivity extends AppCompatActivity  {
    private Button leave_Button;
    private ListView hangout_ListView;
    private List<User> users = new ArrayList<User>();
    private LocalDB db;
    private Context context;
    private User you;
    private SharedPreferences savedValues;
    private OkHttpClient client;
    String resp;
    private List<User> usersFound = new ArrayList<>();


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
        setContentView(R.layout.hangout);
        context = this;
        db = new LocalDB(context);


        savedValues = getSharedPreferences("Saved Values",MODE_PRIVATE);
        if(savedValues.getString("yourName",null) == null) {
            // end main, need to create an account first.
            goToActivity(CreateAccountActivity.class);
            finish();
            return;
        }
        generateYou();

        leave_Button = (Button) findViewById(R.id.leave_Button);
        populateList();
        populateListView();

    }

    public void goToActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    public void generateYou() {
        String uuid = savedValues.getString("yourUUID",null);
        String username = savedValues.getString("yourName",null);
        int status = savedValues.getInt("yourStatus",-1);
        String hangoutStatus = savedValues.getString("yourHangoutStatus",null);
        String latitude = savedValues.getString("yourLat",null);
        String longitude = savedValues.getString("yourLong",null);
        you = new User(uuid,username,hangoutStatus,status);
    }

    private void populateList() {
        users.add(new User("0", "user 1", "0"));
        users.add(new User("1", "user 2", "0"));
        users.add(new User("2", "user 3", "1"));
        users.add(new User("3", "user 4", "0"));
        users.add(new User("4", "user 5", "0"));
        users.add(new User("5", "user 6", "1"));
    }

    public void populateListView(){
        //pull from database -- TBA
        ArrayAdapter<User> adapter = new MyListAdapter();
        hangout_ListView = (ListView) findViewById(R.id.hangout_ListView);
        hangout_ListView.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<User>{

        public MyListAdapter() {
            super(HangoutActivity.this, R.layout.hangout_adapter, users);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //makes sure we have a view to work with , if not we create one
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.hangout_adapter, parent, false);
            }

            //get the user from list
            User currentUser = users.get(position);
            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.username_TextView);
            usernameView.setText(currentUser.getUsername());
            //status
            ImageView statusView = (ImageView) itemView.findViewById(R.id.status_ImageView);
            if(!currentUser.getHangStatus().equals("0")) {
                statusView.setImageResource(R.mipmap.android_check);
            }else{
                statusView.setImageResource(R.mipmap.android_block);
            }



            return itemView;
        }
    }



    // ----- Asynchronous Task Classes -----
    class getUsersFromDB extends AsyncTask<Void, Void, String> {

        /**
         * Task to perform in the background
         * @param params a list of void parameters
         * @return Three possible types of strings:
         *          "200" if the request went through.
         *          The message of the response if the HTTP code was not 200.
         *          "failed" if the request failed.
         */
        Response response;

        @Override
        protected String doInBackground(Void... params ) {
            // params must be in a particular order.
            try {
                Request request = new Request.Builder()
                        .url("http://www.3volution.io:4001/api/Users?filter={\"where\":{\"hangoutStatus\":\""+0+"\"}}")
                        .get()
                        .addHeader("x-ibm-client-id", "default")
                        .addHeader("x-ibm-client-secret", "SECRET")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();

                this.response = client.newCall(request).execute();
                if(response.code() == 200) {
                    resp = response.body().string();
                    return "200";
                }
                else {
                    return response.message();
                }
            }
            catch (IOException e) {
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
                try {
                    JSONArray userJSONArray = new JSONArray(resp);
                    for (int i = 0; i < userJSONArray.length(); i++) {
                        JSONObject o = userJSONArray.getJSONObject(i);
                        usersFound.add(new User(o.getString("uuid"),
                                o.getString("userName"),
                                o.getInt("status"),
                                o.getString("hangoutStatus"),
                                o.getDouble("latitude"),
                                o.getDouble("longitude")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }
            else if(message.equals("failed")) {

            }
            else {
                // HTTP Error Message

            }
            System.out.println("done");
        }
    }



}
