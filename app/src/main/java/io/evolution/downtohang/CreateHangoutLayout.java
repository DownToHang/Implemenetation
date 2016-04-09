package io.evolution.downtohang;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateHangoutLayout extends AppCompatActivity{

    List<User> users = new ArrayList<User>();
    private ListView lv;
    private Context context;
    private Button hangoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hangout_layout);

        lv = (ListView) findViewById(R.id.createHangoutListView);
        context = this;
        hangoutButton = (Button) findViewById(R.id.hangoutButton);

        //populates list of users
        populateUsers();
        //populates the listView with items
        populateListView();
        //sets the onClickListerner for the "Lets Hang!" button
        setOnClickListener();
    }

    private void setOnClickListener() {
        hangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> selectedUsers = new ArrayList<User>();
                for(User u: users){
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
        users.add(new User("Yoonix"));
        users.add(new User("SuperPieGuy"));
        users.add(new User("EZ"));
        users.add(new User("SombreroCat"));
        users.add(new User("Deaganthrope"));
        users.add(new User("RedundantWeedle"));
        users.add(new User("Megaladon"));
    }

    private void populateListView() {

        //Build Adapter
        ArrayAdapter<User> adapter = new MyArrayAdapter();

        //Configure the ListView
        lv.setAdapter(adapter);
    }

    private class MyArrayAdapter extends ArrayAdapter<User> {

        private List<User> userList;
        private Context context;

        public MyArrayAdapter() {
            super(CreateHangoutLayout.this, R.layout.activity_item_layout, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // username to be displayed
            User current = users.get(position);

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
