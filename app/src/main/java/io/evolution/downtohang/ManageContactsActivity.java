package io.evolution.downtohang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
 * Created by Patrick on 4/2/2016.
 */
public class ManageContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private OkHttpClient client;
    private TextView manageContactsSearchUserLabel;
    private EditText manageContactsSearchUserEditText;
    private ImageView manageContactsSearchIconImageView;
    private ListView manageContactsSearchedUsersListView;
    private CheckBox selectCheckBox;
    private Button manageContactsSearchButton;
    private List<User> usersFound = new ArrayList<>();
    private String userToSearch;
    String resp;
    private Button manageContactsAdapterActionButton;

    private User you;
    private String uuidLeader;
    private String selectedUuid;
    List<User> onlineUsers = new ArrayList<User>();
    private LocalDB db;
    private SharedPreferences savedValues;
    private Button hangoutButton;
    private List<User> participants = new ArrayList<User>();
    User currentUser;



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
        setContentView(R.layout.manage_contacts);
        resp = "";

        //Get references to widgets
        manageContactsSearchUserLabel = (TextView) findViewById(R.id.manageContactsSearchUserLabel);
        manageContactsSearchUserEditText = (EditText) findViewById(R.id.manageContactsSearchUserEditText);
        manageContactsSearchIconImageView = (ImageView) findViewById(R.id.manageContactsSearchIconImageView);
        manageContactsSearchedUsersListView = (ListView) findViewById(R.id.manageContactsSearchedUsersListView);
        manageContactsSearchButton = (Button) findViewById(R.id.manageContactsSearchButton);
        selectCheckBox = (CheckBox) findViewById(R.id.selectCheckBox);
        manageContactsAdapterActionButton = (Button) findViewById(R.id.manageContactsAdapterActionButton);
        manageContactsSearchButton.setOnClickListener(this);




        client = new OkHttpClient();
        usersFound = new ArrayList<>();



        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);
        generateYou();
        uuidLeader = you.getUUID();
        db = new LocalDB(this);
        hangoutButton = (Button) findViewById(R.id.hangoutButton);
        hangoutButton.setOnClickListener(this);

        populateList();
        populateListView();
    }

    @Override
    public void onResume(){
        super.onResume();
        //do stuff
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.manageContactsSearchButton:
                Toast.makeText(this, "Search Button", Toast.LENGTH_SHORT).show();
                populateList();
                break;
            case R.id.manageContactsAdapterActionButton:
                participants.add(currentUser);
                String allParts = "Users Invited: ";
                for(User user : participants){
                    allParts += user.getUsername() + ", ";
                }
                Toast.makeText(this, allParts, Toast.LENGTH_SHORT).show();
                break;
            case R.id.hangoutButton:
                currentUser.setStatus(0);
                currentUser.setHangoutStatus(currentUser.getUUID());

                selectedUuid = currentUser.getUUID();
                new UpdateUserHangStatus().execute();

                break;
        }
    }

    private void populateList() {
        userToSearch = manageContactsSearchUserEditText.getText().toString();
        new getUsersFromDB().execute();
        System.out.println("done");
        //populateListView();
    }

    public void populateListView(){
        //pull from database -- TBA
        ArrayAdapter<User> adapter = new MyListAdapter();
        manageContactsSearchedUsersListView = (ListView) findViewById(R.id.manageContactsSearchedUsersListView);
        manageContactsSearchedUsersListView.setAdapter(adapter);
    }

    public void generateYou() {
        String uuid = savedValues.getString("yourUUID",null);
        String username = savedValues.getString("yourName",null);
        int status = savedValues.getInt("yourStatus", -1);
        String hangoutStatus = savedValues.getString("yourHangoutStatus",null);
        String latitude = savedValues.getString("yourLat",null);
        String longitude = savedValues.getString("yourLong",null);
        you = new User(uuid,username,status,hangoutStatus,Double.parseDouble(latitude),
                Double.parseDouble(longitude));
    }

    private class MyListAdapter extends ArrayAdapter<User> implements View.OnClickListener{

        public MyListAdapter() {
            super(ManageContactsActivity.this, R.layout.manage_contacts_adapter, usersFound);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //makes sure we have a view to work with , if not we create one
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.manage_contacts_adapter, parent, false);
            }

            //get the user from list
            currentUser = usersFound.get(position);
            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.manageContactsAdapterUserNameLabel);
            usernameView.setText(currentUser.getUsername());

            Button manageContactsAdapterActionButton = (Button) itemView.findViewById(R.id.manageContactsAdapterActionButton);
            manageContactsAdapterActionButton.setOnClickListener(this);



            /*Button checks user status and displays cooresponding button text:
            User is a friend:       Button text = "Remove"
            User is not a friend:   Button text = "Add"
            User has received invite but has not acted: Button text = "Pending"
             */

            /*Button button = (Button) itemView.findViewById(R.id.manageContactsAdapterActionButton);
            if(!currentUser.getHangoutStatus().equals("0")) {
                button.setText("ADD");
            }else{
                button.setText("REMOVE");
            }*/
            return itemView;
        }

        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.manageContactsAdapterActionButton:
                    participants.add(currentUser);
                    String allParts = "Users Invited: ";
                    for(User user : participants){
                        allParts += user.getUsername() + ", ";
                    }
                    Toast.makeText(getContext(), allParts, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * Go to the main activity.
     */
    public void goToMainActivity() {
        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("yourStatus", 0);
        editor.putString("yourHangoutStatus", uuidLeader);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
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
                        .url("http://www.3volution.io:4001/api/Users?filter={\"where\":{\"userName\":\""+userToSearch+"\"}}")
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


    /*-----------------------------Asynchronus Task-----------------------------*/
    class UpdateUserHangStatus extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            try {
                String responseCode = "";
                for(User user: participants) {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{" +
                            "\"hangoutStatus\":" + "\"" + uuidLeader + "\"" +
                            ",\"status\":0}");
                    Request request = new Request.Builder()
                            .url("http://www.3volution.io:4001/api/Users/update?where={\"uuid\": \"" + user.getUUID() + "\"}")
                            .post(body)
                            .addHeader("x-ibm-client-id", "default")
                            .addHeader("x-ibm-client-secret", "SECRET")
                            .addHeader("content-type", "application/json")
                            .addHeader("accept", "application/json")
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.code() == 200) {
                        responseCode = ""+ response.code()+"";
                    } else {
                        responseCode = response.message();
                    }
                }
                if (responseCode.equals("200")) {
                    return "200";
                } else {
                    return responseCode;
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
                System.out.println("Success");
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

}
