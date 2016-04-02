package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliakah on 4/2/2016.
@@ -21,7 +11,6 @@ import java.util.List;
public class HangoutActivity extends Activity {
    private Button leave_Button;
    private ListView hangout_ListView;
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
@@ -29,115 +18,8 @@ public class HangoutActivity extends Activity {
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

            //fill the viewList
            //ImageView imageView = (ImageView) itemView.findViewById(R.id.profilePicture_ImageView);
            //imageView.setImageResource(currentUser.getImagePath());

            //username
            TextView usernameView = (TextView) itemView.findViewById(R.id.username_TextView);
            usernameView.setText(currentUser.getUsername());
            //status
            ImageView statusView = (ImageView) itemView.findViewById(R.id.status_ImageView);
            if(!currentUser.getHangoutStatus().equals("0")) {
                statusView.setImageResource(R.mipmap.android_check);
            }else{
                statusView.setImageResource(R.mipmap.android_check);
            }



            return itemView;
        }
    }











    //inner class
    public class User{
        String id;
        String username;
        String hangoutStatus;
        public User (String id, String username, String hangoutStatus){
            this.id = id;
            this.username = username;
            this.hangoutStatus = hangoutStatus;
        }
        public User (){

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHangoutStatus() {
            return hangoutStatus;
        }

        public void setHangoutStatus(String hangoutStatus) {
            this.hangoutStatus = hangoutStatus;
        }

        }
}
