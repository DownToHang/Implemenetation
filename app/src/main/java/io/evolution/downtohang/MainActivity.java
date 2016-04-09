package io.evolution.downtohang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {

    private int status = 0;
    private ArrayList<User> users;
    private ImageButton changeStatusImageButton;
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        changeStatusImageButton = (ImageButton) findViewById(R.id.changeStatusImageButton);
        changeStatusImageButton.setOnClickListener(this);

        usersListView = (ListView) findViewById(R.id.usersListView);

        populateList();
        populateListView();
    }


    private void populateList() {
        users = new ArrayList();
        users.add(new User("0","SuperPieGuy","0","1",null));
        users.add(new User("1","Josh","0","0",null));
        users.add(new User("2","Bill","0","0",null));
        users.add(new User("3","SuperPieGuy2","Will","0",null));
        users.add(new User("4","reundantWeEdle420","Will","0",null));
        users.add(new User("5","Yoonix","0","1",null));
        users.add(new User("6","SuperPieGuy3","Will","0",null));
        users.add(new User("7","JoshAgain","0","0",null));

    }

    private void populateListView() {
        ArrayAdapter<User> adapter = new MainListAdapter();
        usersListView.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.changeStatusImageButton:
                changeStatus();
        }
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

        public MainListAdapter() {
            super(MainActivity.this, R.layout.main_list_layout, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //makes sure we have a view to work with , if not we create one
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.main_list_layout, parent, false);
            }

           //get the user from list
            User currentUser = users.get(position);

            // status image
            ImageView userStatusImageView = (ImageView) itemView.findViewById(R.id.userStatusImageView);
            if(!currentUser.getHangStatus().equals("0")) {
                userStatusImageView.setImageResource(R.mipmap.orange_circle_icone_6032_128);
            }
            else if(currentUser.getAvailablity().equals("1")) {
                userStatusImageView.setImageResource(R.mipmap.green_circle_icone_4156_128);
            }
            else if(currentUser.getAvailablity().equals("0")) {
                userStatusImageView.setImageResource(R.mipmap.red_circle_icone_5751_128);
            }


            // username
            TextView usernameLabel = (TextView) itemView.findViewById(R.id.usernameLabel);
            usernameLabel.setText(currentUser.getUsername());

            ImageButton acceptImageButton = (ImageButton) itemView.findViewById(R.id.acceptImageButton);
            ImageButton rejectImageButton = (ImageButton) itemView.findViewById(R.id.rejectImageButton);

            return itemView;
        }

    }

}
