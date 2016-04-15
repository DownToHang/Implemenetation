package io.evolution.downtohang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private User you;
    private ArrayList<User> users;
    private ImageButton changeStatusImageButton;
    private ListView usersListView;
    private Button mainHangoutButton;
    private LocalDB db;
    private OkHttpClient client;

    private SharedPreferences savedValues;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                goToActivity(ManageContactsActivity.class);
                return true;
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
        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);
        if (savedValues.getString("yourName", null) == null) {
            // end main, need to create an account first.
            goToActivity(CreateAccountActivity.class);
            finish();
            return;
        }
        generateYou();
        setContentView(R.layout.main_layout);
        client = new OkHttpClient();
        changeStatusImageButton = (ImageButton) findViewById(R.id.changeStatusImageButton);
        changeStatusImageButton.setOnClickListener(this);
        if(you.getStatus() == 0) {
            changeStatusImageButton.setImageResource(R.mipmap.red_circle_icone_5751_128);
        }

        usersListView = (ListView) findViewById(R.id.usersListView);
        mainHangoutButton = (Button) findViewById(R.id.mainHangoutButton);

        mainHangoutButton.setOnClickListener(this);
        db = new LocalDB(this);

        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        generateYou();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateYou(you.getStatus(),you.getHangStatus(),you.getLat(),you.getLong());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateYou(0,you.getHangStatus(),you.getLat(),you.getLong());
    }

    public void generateYou() {
        String uuid = savedValues.getString("yourUUID", null);
        String username = savedValues.getString("yourName", null);
        int status = savedValues.getInt("yourStatus", -1);
        String hangoutStatus = savedValues.getString("yourHangoutStatus", null);
        String latitude = savedValues.getString("yourLat", null);
        String longitude = savedValues.getString("yourLong", null);
        you = new User(uuid, username, status, hangoutStatus, Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    /**
     * Populate the list view with users who are friends.
     */
    private void populateListView() {
        users = db.getAllUsers();
        ArrayAdapter<User> adapter = new MainListAdapter();
        usersListView.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeStatusImageButton:
                changeStatus();
                break;
            case R.id.mainHangoutButton:
                goToActivity(CreateHangoutLayout.class);
                break;
            default:
                break;
        }
    }

    public void goToActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    private void changeStatus() {
        int status = you.getStatus();
        int newStatus;
        if (status == 1) { //go offline
            changeStatusImageButton.setImageResource(R.mipmap.red_circle_icone_5751_128);
            newStatus = 0;
        } else { // go online
            changeStatusImageButton.setImageResource(R.mipmap.green_circle_icone_4156_128);
            newStatus = 1;
        }
        updateYou(newStatus,you.getHangStatus(),you.getLat(),you.getLong());
    }

    private void updateYou(int newStatus, String newHangoutStatus, double latitude, double longitude) {
        // you object
        you.setStatus(newStatus);
        you.setHangStatus(newHangoutStatus);
        you.setLatitude(latitude);
        you.setLongitude(longitude);

        // shared preferences
        String latString = Double.toString(you.getLat());
        String longString = Double.toString(you.getLong());
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("yourStatus", newStatus);
        editor.putString("yourHangoutStatus", newHangoutStatus);
        editor.putString("yourLat", latString);
        editor.putString("yourLong", longString);
        editor.commit();

        // server database
        Toast.makeText(getApplicationContext(),you.getUsername() + " " + you.getUUID() + " " + you.getStatus(),Toast.LENGTH_SHORT).show();
        new UpdateDB().execute(you.getUUID(), you.getUsername(), Integer.toString(newStatus),
                newHangoutStatus, latString, longString);
    }

    private class MainListAdapter extends ArrayAdapter<User> {

        public int lastExpanded;

        public MainListAdapter() {
            super(MainActivity.this, R.layout.main_list_layout, users);
            this.lastExpanded = -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get the user from list
            User currentUser = users.get(position);
            MainListItemLayout mainListItemLayout = null;
            if (convertView == null) {
                mainListItemLayout = new MainListItemLayout(getContext(), currentUser, false, you);
            } else {
                mainListItemLayout = (MainListItemLayout) convertView;
            }
            return mainListItemLayout;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }

    // ----- Asynchronous Calls -----
    class UpdateDB extends AsyncTask<String, Void, String> {

        /**
         * Task to perform in the background
         *
         * @param params a list of void parameters
         * @return Three possible types of strings:
         * "200" if the request went through.
         * The message of the response if the HTTP code was not 200.
         * "failed" if the request failed.
         */
        @Override
        protected String doInBackground(String... params) {

            // params must be in a particular order.
            String uuid = params[0];
            String username = params[1];
            int status = Integer.parseInt(params[2]);
            String hangoutStatus = params[3];
            double latitude = Double.parseDouble(params[4]);
            double longitude = Double.parseDouble(params[5]);

            String esc_quote = "\"";

            StringBuilder requestBody = new StringBuilder();
            requestBody.append("{").append(esc_quote).append("userName").append(esc_quote).append(":").append(esc_quote).append(username).append(esc_quote).append(",")
                    .append(esc_quote).append("status").append(esc_quote).append(":").append(status).append(",")
                    .append(esc_quote).append("hangoutStatus").append(esc_quote).append(":").append(esc_quote).append(hangoutStatus).append(esc_quote).append(",")
                    .append(esc_quote).append("latitude").append(esc_quote).append(":").append(latitude).append(",")
                    .append(esc_quote).append("longitude").append(esc_quote).append(":").append(longitude).append("}");

            StringBuilder url = new StringBuilder();
            url.append("http://www.3volution.io:4001/api/Users/update?where={")
                    .append(esc_quote).append("uuid").append(esc_quote).append(":")
                    .append(esc_quote).append(uuid).append(esc_quote).append("}");

            try {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, requestBody.toString());
                Request request = new Request.Builder()
                        .url(url.toString())
                        .post(body)
                        .addHeader("x-ibm-client-id", "default")
                        .addHeader("x-ibm-client-secret", "SECRET")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    return "200";
                } else {
                    return response.message();
                }
            } catch (IOException e) {
                System.err.println(e);
                return "failed";
            }
        }

        /**
         * Actions to perform after the asynchronous request
         *
         * @param message the message returned by the request
         */
        @Override
        protected void onPostExecute(String message) {
            if (message.equals("200")) {
                // success, do what you need to.
                System.out.println("Updated!");
            }
        }
    }
}
