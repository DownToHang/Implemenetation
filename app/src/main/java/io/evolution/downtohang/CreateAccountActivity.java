package io.evolution.downtohang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
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

/**
 * The create account activity. Users create an account and are added to
 * the database.
 */
public class CreateAccountActivity extends AppCompatActivity
    implements View.OnClickListener {

    private final int REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION = 1;

    private static final int PICK_PROFILE_ICON_IMAGE = 100;

    private TextView usernameLabel;
    private EditText editUsername;
    private TextView errorLabel;
    private TextView profileIconLabel;
    private ImageView profileIcon;
    private Button selectImageButton;
    private Button createAccountButton;

    private AppLocationListener locationListener;
    private LocationManager locationManager;
    private OkHttpClient client;

    private String uuid;
    private String username;
    private String status;
    private String hangoutStatus;
    private String latitude;
    private String longitude;

    private SharedPreferences savedValues;

    /**
     * Create the Activity
     * @param savedInstanceState the applications current saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new AppLocationListener();
        String bestProvider = locationManager.getBestProvider(new Criteria(),true);
        System.out.println(bestProvider);

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
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION);
        }
        else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 200, 1,
                    locationListener);
            //testLocationListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(locationListener);
        }
        catch(SecurityException se) {
            System.err.print("No permissions to remove!");
        }

    }

    /**
     * Testing for location listener set up
     */
    public void testLocationListener() {
        String lat = "No location available";
        String lon = "No location available";
        Location current = locationListener.getLocation();
        if(current != null) {
            lat = Double.toString(current.getLatitude());
            lon = Double.toString(current.getLongitude());
        }
        String toastMsg = "Lat: " + lat+ " Long: " + lon;
        Toast.makeText(getApplicationContext(),toastMsg,Toast.LENGTH_LONG).show();
    }

    /**
     * Handle click events for a view.
     * @param v a view
     */
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
     * Go to the main activity.
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
        finish();
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
            Location currentLocation = locationListener.getLocation();
            if(currentLocation != null) {
                latitude = Double.toString(currentLocation.getLatitude());
                longitude =  Double.toString(currentLocation.getLongitude());
            }
            else {
                latitude = longitude = "N/A";
            }
            new AddUserToServerDB().execute();
        }
    }


    /**
     * Perform some action after the user has denied or accepted
     * a request to use a permission.
     * @param requestCode the code of the request
     * @param permissions the list of permissions being to be granted
     * @param grantResults array corresponding to permissions determining whether
     *                     the permission was granted or denied.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_UPDATE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 200, 1,
                                locationListener);
                    }
                }
                //testLocationListener();
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
        testLocationListener();
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_PROFILE_ICON_IMAGE);*/
    }

    /**
     * Handle data sent back from another intent
     * @param requestCode the request code, specified by user.
     * @param resultCode the result code, determines if everything went okay.
     * @param data the data from the intent.
     */
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

        /**
         * Task to perform in the background
         * @param params a list of void parameters
         * @return Three possible types of strings:
         *          "200" if the request went through.
         *          The message of the response if the HTTP code was not 200.
         *          "failed" if the request failed.
         */
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

        /**
         * Actions to perform after the asynchronous request
         * @param message the message returned by the request
         */
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