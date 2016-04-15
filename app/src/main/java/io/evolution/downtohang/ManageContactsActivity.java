package io.evolution.downtohang;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Patrick on 4/2/2016.
 */
public class ManageContactsActivity extends AppCompatActivity {

    private OkHttpClient client;
    private TextView manageContactsSearchUserLabel;
    private EditText manageContactsSearchUserEditText;
    private ImageView manageContactsSearchIconImageView;
    private ListView manageContactsSearchedUsersListView;
    private List<User> usersFound = new ArrayList<>();
    private String userToSearch;
    String resp;
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
                populateList();
                return true;

            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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
        client = new OkHttpClient();
        usersFound = new ArrayList<>();
        populateList();
        populateListView();
    }

    @Override
    public void onResume(){
        super.onResume();
        //do stuff
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

    private class MyListAdapter extends ArrayAdapter<User>{

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
            User currentUser = usersFound.get(position);
            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.manageContactsAdapterUserNameLabel);
            usernameView.setText(currentUser.getUsername());

            /*Button checks user status and displays cooresponding button text:
            User is a friend:       Button text = "Remove"
            User is not a friend:   Button text = "Add"
            User has received invite but has not acted: Button text = "Pending"
             */

            Button button = (Button) itemView.findViewById(R.id.manageContactsAdapterActionButton);
            if(!currentUser.getHangStatus().equals("0")) {
                button.setText("ADD");
            }else{
                button.setText("REMOVE");
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
                        .url("http://www.3volution.io:4001/api/Users?filter={\"where\":{\"userName\":\"bob\"}}")
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
