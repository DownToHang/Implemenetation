package io.evolution.downtohang;

/**
 * Created by michael on 4/2/2016.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by michael on 4/2/2016.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener{

    private static final int PICK_PROFILE_ICON_IMAGE = 100;
    public ImageView profilePic;
    private EditText usernameEdit;

    private SharedPreferences savedValues;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.zero_menu, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Button browseButton = (Button) findViewById(R.id.browsebutton);
        CheckBox notificationCheckBox = (CheckBox) findViewById(R.id.notificationCheckBox);
        CheckBox gpsCheckBox = (CheckBox) findViewById(R.id.gpsCheckBox);
        //profilePic = (ImageView) findViewById(R.id.profilePic);

        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        savedValues = getSharedPreferences("Saved Values",MODE_PRIVATE);
        usernameEdit.setText(savedValues.getString("youUser", ""));


        browseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToImageSelect();
            }
        });
        usernameEdit.setOnEditorActionListener(this);
        //on button click, create intent
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
        System.out.println("Here");
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            SharedPreferences.Editor editor = savedValues.edit();
            editor.putString("youUser",usernameEdit.getText().toString());
            editor.commit();
        }
        return true;
    }


    /**
     * Go to gallery via an intent.
     */
    public void goToImageSelect() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_PROFILE_ICON_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PROFILE_ICON_IMAGE:
                    Uri newProfileIconImage = data.getData();
                    ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
                    profilePic.setImageURI(newProfileIconImage);
                    break;
            }
        }
    }

}


