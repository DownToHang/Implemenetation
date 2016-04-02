package io.evolution.downtohang;

/**
 * Created by michael on 4/2/2016.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;


/**
 * Created by michael on 4/2/2016.
 */
public class SettingsActivity extends Activity implements View.OnClickListener{

    private static final int PICK_PROFILE_ICON_IMAGE = 100;
    public ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Button browseButton = (Button) findViewById(R.id.browsebutton);
        CheckBox notificationCheckBox = (CheckBox) findViewById(R.id.notificationCheckBox);
        CheckBox gpsCheckBox = (CheckBox) findViewById(R.id.gpsCheckBox);
        //profilePic = (ImageView) findViewById(R.id.profilePic);


        browseButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToImageSelect();
            }
        });
        //on button click, create intent
    }


    @Override
    public void onClick(View v) {
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
            switch(requestCode) {
                case PICK_PROFILE_ICON_IMAGE:
                    Uri newProfileIconImage = data.getData();
                    ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
                    profilePic.setImageURI(newProfileIconImage);
                    break;
            }
        }
    }


