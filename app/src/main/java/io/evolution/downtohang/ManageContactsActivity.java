package io.evolution.downtohang;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Patrick on 4/2/2016.
 */
public class ManageContactsActivity extends Activity {

    private TextView manageContactsSearchUserLabel;
    private EditText manageContactsSearchUserEditText;
    private ImageView manageContactsSearchIconImageView;
    private ListView manageContactsSearchedUsersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_contacts);

        //Get references to widgets
        manageContactsSearchUserLabel = (TextView) findViewById(R.id.manageContactsSearchUserLabel);
        manageContactsSearchUserEditText = (EditText) findViewById(R.id.manageContactsSearchUserEditText);
        manageContactsSearchIconImageView = (ImageView) findViewById(R.id.manageContactsSearchIconImageView);
        manageContactsSearchedUsersListView = (ListView) findViewById(R.id.manageContactsSearchedUsersListView);

    }

    @Override
    public void onResume(){
        super.onResume();
        //do stuff
    }
}
