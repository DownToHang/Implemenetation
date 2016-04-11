package io.evolution.downtohang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.*;

public class CreateAccountActivity extends Activity
    implements View.OnClickListener {

    private static final int PICK_PROFILE_ICON_IMAGE = 100;
    private OkHttpClient client;
    private TextView usernameLabel;
    private EditText editUsername;
    private TextView errorLabel;
    private TextView profileIconLabel;
    private ImageView profileIcon;
    private Button selectImageButton;
    private Button createAccountButton;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        client = new OkHttpClient();
        // get references to each widget
        usernameLabel = (TextView) findViewById(R.id.createAccountUsernameLabel);
        editUsername = (EditText) findViewById(R.id.createAccountEditUsername);
        errorLabel = (TextView) findViewById(R.id.createAccountErrorLabel);
        profileIconLabel = (TextView) findViewById(R.id.createAccountProfileIconLabel);
        profileIcon = (ImageView) findViewById(R.id.createAccountProfileIcon);
        selectImageButton = (Button) findViewById(R.id.createAccountSelectImageButton);
        createAccountButton = (Button) findViewById(R.id.createAccountCreateAccountButton);

        // set click listeners
        selectImageButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createAccountSelectImageButton:
                goToImageSelect();
                break;
            case R.id.createAccountCreateAccountButton:
                if(createAccount(editUsername.getText().toString())) {

                    savedValues = getSharedPreferences("Saved Values",MODE_PRIVATE);
                    SharedPreferences.Editor editor = savedValues.edit();
                    editor.putString("youUser",editUsername.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    try {
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\"uuid\":\""+ UUID.randomUUID().toString()+"\",\"userName\":\""+editUsername.getText().toString()+"\",\"status\":0,\"hangoutStatus\":0,\"latitude\":39.7104,\"longitude\":-75.1202}");
                        Request request = new Request.Builder()
                                .url("http://www.3volution.io:4001/api/Users")
                                .post(body)
                                .addHeader("x-ibm-client-id", "default")
                                .addHeader("x-ibm-client-secret", "SECRET")
                                .addHeader("content-type", "application/json")
                                .addHeader("accept", "application/json")
                                .build();

                        Response response = client.newCall(request).execute();
                    }catch (IOException e){}
                }
        }
    }

    /**
     * Set the text of the error label.
     * @param message the error message.
     */
    private void setErrorMessage(String message) {
        errorLabel.setText(message != null ? message:"");
    }

    /**
     * Creates an account by adding it to the server database.
     * @param username the username.
     * @return true if the creation was successful.
     */
    public boolean createAccount(String username) {
        return validateUsername(username);
        /**
        if(validateUsername(username)) {
            // attempt to connect to database.
            //if(!db.connected()) return false;
        }
        return true;
         */
    }

    /**
     * Check if a username is valid. A username is valid if it contains...
     * @param username the username
     * @return true if the username is valid.
     */
    public boolean validateUsername(String username) {
        if(username.length() <= 0) {
            setErrorMessage("Enter a username!");
            return false;
        }
        String regExp = "^[A-Z,a-z][ A-Z,a-z,0-9,_]*$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(username);
        if(matcher.find()) {
            return true;
        }
        else {
            setErrorMessage("Invalid username!");
            return false;
        }
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
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newProfileIconImage);
                        profileIcon.setImageBitmap(bitmap);
                    }
                    catch(IOException ie) {
                        System.out.println("ISSUE!");
                    }
                    break;
            }
        }
    }
}