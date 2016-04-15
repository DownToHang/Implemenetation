package io.evolution.downtohang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private int status = 0;
    private User you;
    private ArrayList<User> users;
    private ImageButton changeStatusImageButton;
    private ListView usersListView;
    private Button mainHangoutButton;
    private LocalDB db;

    private SharedPreferences savedValues;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
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
        savedValues = getSharedPreferences("Saved Values",MODE_PRIVATE);
        if(savedValues.getString("yourName",null) == null) {
            // end main, need to create an account first.
            goToActivity(CreateAccountActivity.class);
            finish();
            return;
        }
        generateYou();
        setContentView(R.layout.main_layout);
        changeStatusImageButton = (ImageButton) findViewById(R.id.changeStatusImageButton);
        changeStatusImageButton.setOnClickListener(this);

        usersListView = (ListView) findViewById(R.id.usersListView);
        mainHangoutButton = (Button) findViewById(R.id.mainHangoutButton);

        mainHangoutButton.setOnClickListener(this);
        db = new LocalDB(this);

        populateListView();
    }

    public void generateYou() {
        String uuid = savedValues.getString("yourUUID",null);
        String username = savedValues.getString("yourName", null);
        int status = savedValues.getInt("yourStatus", -1);
        String hangoutStatus = savedValues.getString("yourHangoutStatus", null);
        String latitude = savedValues.getString("yourLat",null);
        String longitude = savedValues.getString("yourLong",null);

//        Location location  =  new Location("");
//        location.setLatitude(Double.parseDouble(latitude));
//        location.setLongitude(Double.parseDouble(longitude));

        you = new User(uuid,username,status,hangoutStatus, Double.parseDouble(latitude), Double.parseDouble(longitude));
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
        switch(v.getId()) {
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
        if(status == 1) {
            changeStatusImageButton.setImageResource(R.mipmap.green_circle_icone_4156_128);
            status = 0;
        }
        else {
            changeStatusImageButton.setImageResource(R.mipmap.red_circle_icone_5751_128);
            status = 1;
        }


    }

    private class MainListAdapter extends ArrayAdapter<User> {

        public int lastExpanded;

        public MainListAdapter() {
            super(MainActivity.this, R.layout.main_list_layout, users);
            this.lastExpanded = -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //get the user from list
            User currentUser = users.get(position);
            MainListItemLayout mainListItemLayout = null;
            if(convertView == null) {
                mainListItemLayout = new MainListItemLayout(getContext(),currentUser,false, you);
            }
            else {
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

}
