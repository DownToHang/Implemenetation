package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private int status = 0;
    private ArrayList<User> users;
    private ImageButton changeStatusImageButton;
    private ListView usersListView;
    private Button mainHangoutButton;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_add:
                Toast.makeText(this, "Add Button", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_refresh:
                Toast.makeText(this, "Refresh Button", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.main_layout);
        changeStatusImageButton = (ImageButton) findViewById(R.id.changeStatusImageButton);
        changeStatusImageButton.setOnClickListener(this);

        usersListView = (ListView) findViewById(R.id.usersListView);
        mainHangoutButton = (Button) findViewById(R.id.mainHangoutButton);

        mainHangoutButton.setOnClickListener(this);

        populateList();
        populateListView();
    }


    private void populateList() {
        users = new ArrayList();
        users.add(new User("0", "SuperPieGuy", "0", "1", null));
        users.add(new User("1","Josh","0","0",null));
        users.add(new User("2", "Bill", "0", "0", null));
        users.add(new User("3","SuperPieGuy2","Will","0",null));
        users.add(new User("4","reundantWeEdle420","Will","0",null));
        users.add(new User("5", "Yoonix", "0", "1", null));
        users.add(new User("6", "SuperPieGuy3", "Will", "0", null));
        users.add(new User("7", "JoshAgain", "0", "0", null));

    }

    private void populateListView() {
        ArrayAdapter<User> adapter = new MainListAdapter();
        usersListView.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.changeStatusImageButton:
                changeStatus();
                break;
            case R.id.mainHangoutButton:
                goToHangoutLayout();
                break;
            default:
                break;
        }
    }

    public void goToHangoutLayout() {
        Intent intent = new Intent(getApplicationContext(), HangoutActivity.class);
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
                mainListItemLayout = new MainListItemLayout(getContext(),currentUser,false);
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
