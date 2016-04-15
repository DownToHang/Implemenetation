package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliakah on 4/2/2016.
 */
public class HangoutActivity extends AppCompatActivity  {
    private Button leave_Button;
    private ListView hangout_ListView;
    private List<User> users = new ArrayList<User>();


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
        setContentView(R.layout.hangout);

        leave_Button = (Button) findViewById(R.id.leave_Button);
        populateList();
        populateListView();
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
        hangout_ListView = (ListView) findViewById(R.id.hangout_ListView);
        hangout_ListView.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<User>{

        public MyListAdapter() {
            super(HangoutActivity.this, R.layout.hangout_adapter, users);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //makes sure we have a view to work with , if not we create one
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.hangout_adapter, parent, false);
            }

            //get the user from list
            User currentUser = users.get(position);
            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.username_TextView);
            usernameView.setText(currentUser.getUsername());
            //status
            ImageView statusView = (ImageView) itemView.findViewById(R.id.status_ImageView);
            if(!currentUser.getHangStatus().equals("0")) {
                statusView.setImageResource(R.mipmap.android_check);
            }else{
                statusView.setImageResource(R.mipmap.android_block);
            }



            return itemView;
        }
    }



}
