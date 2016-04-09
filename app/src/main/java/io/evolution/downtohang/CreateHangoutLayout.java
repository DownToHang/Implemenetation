package io.evolution.downtohang;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CreateHangoutLayout extends AppCompatActivity{

    List<User> users = new ArrayList<User>();
//    private ListView listView;
    private ListView lv;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hangout_layout);

        lv = (ListView) findViewById(R.id.createHangoutListView);
        context = this;

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
//        listView = (ListView) findViewById(R.id.createHangoutListView);
//        assert listView != null;
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
