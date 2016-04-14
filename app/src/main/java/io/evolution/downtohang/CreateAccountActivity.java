package io.evolution.downtohang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.*;

public class CreateAccountActivity extends AppCompatActivity
    implements View.OnClickListener {

    private final int REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION = 1;

    private static final int PICK_PROFILE_ICON_IMAGE = 100;
    private OkHttpClient client;
    private TextView usernameLabel;
    private EditText editUsername;
    private TextView errorLabel;
    private TextView profileIconLabel;
    private ImageView profileIcon;
    private Button selectImageButton;
    private Button createAccountButton;

    private AppLocationListener locationListener;
    private LocationManager locationManager;

    private String uuid;
    private String username;
    private String status;
    private String hangoutStatus;
    private String latitude;
    private String longitude;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);

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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION);
        }
        else {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            locationListener = new AppLocationListener(lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude());
            String toastMsg = "Lat: " + locationListener.getLatitude() + " Long: " +
                    locationListener.getLongitude();
            Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_LONG).show();
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 200, 1,
                    locationListener);
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createAccountSelectImageButton:
                goToImageSelect();
                break;
            case R.id.createAccountCreateAccountButton:
               createAccount();
        }
    }

    /**
     */
    public void goToMainActivity() {
        savedValues = getSharedPreferences("Saved Values",MODE_PRIVATE);
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("yourUUID",uuid);
        editor.putString("yourName",username);
        editor.putString("yourStatus",status);
        editor.putString("yourHangoutStatus",hangoutStatus);
        editor.putString("yourLat",latitude);
        editor.putString("yourLong",longitude);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
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
     */
    public void createAccount() {
        username = editUsername.getText().toString();
        if(validateUsername()) {
            if (!isNetworkAvailable()) {
                setErrorMessage("ERROR - No internet connection.");
            }
            uuid = UUID.randomUUID().toString();
            status = "0";
            hangoutStatus = "0";
            latitude = Double.toString(locationListener.getLatitude());
            longitude =  Double.toString(locationListener.getLongitude());
            new AddUserToServerDB().execute();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                        locationListener = new AppLocationListener(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude());
                        String toastMsg = "Lat: " + locationListener.getLatitude() + " Long: " +
                                locationListener.getLongitude();
                        Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    locationListener = new AppLocationListener(0,0);
                    String toastMsg = "Lat: " + locationListener.getLatitude() + " Long: " +
                            locationListener.getLongitude();
                    Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * Check if a username is valid. A username is valid if it contains...
     * @return true if the username is valid.
     */
    public boolean validateUsername() {
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
     * @return true if this device has some form of internet connectivity.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                newProfileIconImage);
                        profileIcon.setImageBitmap(bitmap);
                    }
                    catch(IOException ie) {
                        System.out.println("ISSUE!");
                    }
                    break;
            }
        }
    }

    // ----- Asynchronous Task Classes -----
    class AddUserToServerDB extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params ) {
            // params must be in a particular order.
            try {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{" +
                        "\"uuid\":\"" + uuid + "\"," +
                        "\"userName\":\"" + username + "\"," +
                        "\"status\":\"" + status + "\"," +
                        "\"hangoutStatus\":\"" + hangoutStatus + "\"," +
                        "\"latitude\":\"" + latitude + "\"," +
                        "\"longitude\":\"" + longitude + "\"" +
                        "}");
                Request request = new Request.Builder()
                        .url("http://www.3volution.io:4001/api/Users")
                        .post(body)
                        .addHeader("x-ibm-client-id", "default")
                        .addHeader("x-ibm-client-secret", "SECRET")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();
                Response response = client.newCall(request).execute();
                if(response.code() == 200) {
                    return "200";
                }
                else {
                    return response.message();
                }
            }
            catch (IOException e) {
                System.err.println(e);
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if(message.equals("200")) {
                // success, do what you need to.
                goToMainActivity();
            }
            else if(message.equals("failed")) {
                setErrorMessage("Error Occurred.");
            }
            else {
                // HTTP Error Message
                setErrorMessage(message);
            }
        }
    }
}

