package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateHangoutListItemLayout extends RelativeLayout implements android.widget.CompoundButton.OnCheckedChangeListener{

    private CheckBox cb;
    private TextView username;
    private Context context;
    private User user;

    public CreateHangoutListItemLayout(Context context, User user) {
        super(context);
        this.user = user;
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_item_layout, this, true);


        cb = (CheckBox) findViewById(R.id.selectCheckBox);
        username = (TextView) findViewById(R.id.usernameDisplayx);
        username.setText(user.getUsername());

        cb.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        user.setIsSelected(isChecked);
        //Toast to test if it registers being checked
//        Toast.makeText(context,"Clicked on User :" + user.getUsername()
//                + ". State is :" + isChecked, Toast.LENGTH_SHORT).show();
    }
}
