package io.evolution.downtohang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateAccountActivity extends Activity
    implements View.OnClickListener {

    private TextView usernameLabel;
    private EditText editUsername;
    private TextView errorLabel;
    private TextView profileIconLabel;
    private ImageView profileIcon;
    private Button selectImageButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

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
                setErrorMessage(null);
                break;
            case R.id.createAccountCreateAccountButton:
                setErrorMessage(getResources().getString(R.string.error_message_db_failed));
        }
    }

    private void setErrorMessage(String message) {
        StringBuilder sb = new StringBuilder();
        if(message != null) {
            sb.append("Error! ").append(message);
        }
        errorLabel.setText(sb.toString());
    }

    /**
     * Creates an account by adding it to the server database.
     * @param username the username.
     * @return true if the creation was successful.
     */
    public boolean createAccount(String username) {
        if(validateUsername(username)) {
            // attempt to connect to database.
        }
        return true;
    }

    public boolean validateUsername(String username) {
        String trimmedUsername = username.trim();
        return trimmedUsername.length() > 0;
    }
}
