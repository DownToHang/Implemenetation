package io.evolution.downtohang;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class CreateHangoutLayout extends AppCompatActivity{

    List<User> onlineUsers = new ArrayList<User>();
    private LocalDB db;
    private ListView lv;
    private Context context;
    private Button hangoutButton;


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

        //populates list of users
        populateUsers();
        //populates the listView with items
        populateListView();
        //sets the onClickListener for the "Lets Hang!" button
        setOnClickListener();
    }

    private void setOnClickListener() {
        hangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must update local database and yourself, first thing

                // change leader hangoutStatus to leader's own uuid

                // change members' hangoutStatus to leader's uuid

                ArrayList<User> selectedUsers = new ArrayList<User>();
                for(User u: onlineUsers){
                    boolean selected = u.isSelected();
                    if(selected){
                        selectedUsers.add(u);
                    }//end if
                }//end for

                //this is to test if selected users are correctly selected
                //this code will be replaced by a new intent passing in a list of user id's
                String toastMsg = "You have Selected: \n";
                for (User x: selectedUsers){
                    toastMsg = toastMsg + x.getUsername()+"\n";

                }

                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
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
}//end of CreateHangoutActivity
