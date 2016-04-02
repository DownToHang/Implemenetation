package io.evolution.downtohang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateHangoutLayout extends AppCompatActivity {

    List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hangout_layout);

        //LayoutInflater inflate = (LayoutInflater)context.get

        populateUsers();
        populateListView();
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
        ListView listView = (ListView) findViewById(R.id.createHangoutListView);
        listView.setAdapter(adapter);
    }


    private class MyArrayAdapter extends ArrayAdapter<User> {
        public MyArrayAdapter() {
            super(CreateHangoutLayout.this, R.layout.activity_item_layout, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            //Makes sure view is created and worked on
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.activity_item_layout, parent, false);
            }

            // username to be displayed
            User current = users.get(position);

            // fill the listView
            TextView usernameDisp = (TextView) findViewById(R.id.usernameDisplay);
            usernameDisp.setText(current.getUsername());

            return itemView;
        }//end of getView
    }//end of MyArrayListAdapter class

}
