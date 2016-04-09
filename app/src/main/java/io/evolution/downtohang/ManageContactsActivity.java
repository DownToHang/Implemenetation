package io.evolution.downtohang;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 4/2/2016.
 */
public class ManageContactsActivity extends Activity {

    private TextView manageContactsSearchUserLabel;
    private EditText manageContactsSearchUserEditText;
    private ImageView manageContactsSearchIconImageView;
    private ListView manageContactsSearchedUsersListView;
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_contacts);

        //Get references to widgets
        manageContactsSearchUserLabel = (TextView) findViewById(R.id.manageContactsSearchUserLabel);
        manageContactsSearchUserEditText = (EditText) findViewById(R.id.manageContactsSearchUserEditText);
        manageContactsSearchIconImageView = (ImageView) findViewById(R.id.manageContactsSearchIconImageView);
        manageContactsSearchedUsersListView = (ListView) findViewById(R.id.manageContactsSearchedUsersListView);

        populateList();
        populateListView();
    }

    @Override
    public void onResume(){
        super.onResume();
        //do stuff
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
        manageContactsSearchedUsersListView = (ListView) findViewById(R.id.manageContactsSearchedUsersListView);
        manageContactsSearchedUsersListView.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<User>{

        public MyListAdapter() {
            super(ManageContactsActivity.this, R.layout.manage_contacts_adapter, users);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //makes sure we have a view to work with , if not we create one
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.manage_contacts_adapter, parent, false);
            }

            //get the user from list
            User currentUser = users.get(position);
            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.manageContactsAdapterUserNameLabel);
            usernameView.setText(currentUser.getUsername());
            //status
            Button button = (Button) itemView.findViewById(R.id.manageContactsAdapterActionButton);
            if(!currentUser.getHangStatus().equals("0")) {
                button.setText("ADD");
            }else{
                button.setText("REMOVE");
            }
            return itemView;
        }

    }

}
