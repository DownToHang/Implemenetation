package io.evolution.downtohang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CreateHangoutLayout extends AppCompatActivity {

    String [] usernames = {"Yoonix", "SuperPieGuy", "EZ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hangout_layout);

        //LayoutInflater inflate = (LayoutInflater)context.get

        populateListView();
    }

    private void populateListView() {

        //Build Adapter
        ArrayAdapter<String> adapter = new MyArrayAdapter();

        //Configure the ListView
        ListView listView = (ListView) findViewById(R.id.createHangoutListView);
        listView.setAdapter(adapter);
    }


    private class MyArrayAdapter extends ArrayAdapter<String> {
        public MyArrayAdapter() {
            super(CreateHangoutLayout.this, R.layout.activity_item_layout, usernames);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            //Makes sure view is created and worked on
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.activity_item_layout, parent, false);
            }

            // username to be displayed
            String current = usernames[position];

            // fill the listView
            TextView usernameDisp = (TextView) findViewById(R.id.usernameDisplay);
            usernameDisp.setText(current);

            return itemView;
        }//end of getView
    }//end of MyArrayListAdapter class

}
