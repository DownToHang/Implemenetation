package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Bill Ezekiel on 3/7/2016.
 */
public class MainListItemLayout extends RelativeLayout implements OnClickListener {


    private User you;

    private final String NO_HANGOUT = "0";
    private final int AVAILABLE = 1;
    private final int BUSY = 0;
    private final int PENDING_FRIEND_REQUEST = 2;
    private final int OFFLINE = -1;

    // Collapsed View
    private RelativeLayout mainListItemExpandedView;
    private RelativeLayout mainListItemCollapsedView;
    private ImageView userStatusImageView;
    private TextView usernameLabel;

    // Expanded View
    private ImageView userIconImageView;
    private TextView expandedUsernameLabel;
    private TextView expandedLocationLabel;


    private Context context;
    private boolean expanded;
    private User user;
    private boolean changing;

    public MainListItemLayout(Context context, User user, boolean expanded, User appUser) {
        super(context);
        this.user = user;
        this.expanded = expanded;
        changing = false;

        this.you = appUser;

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_list_layout,this,true);

        // Collapsed
        mainListItemCollapsedView = (RelativeLayout) findViewById(R.id.mainListItemCollapsedView);
        mainListItemCollapsedView.setOnClickListener(this);

        usernameLabel = (TextView) findViewById(R.id.usernameLabel);
        usernameLabel.setText(user.getUsername());
        usernameLabel.setOnClickListener(this);

        userStatusImageView = (ImageView) findViewById(R.id.userStatusImageView);
        if(user.getStatus() < -1 || user.getStatus() > 2) {
            userStatusImageView.setImageResource(R.mipmap.gray_circle_question_icone_6920_128);
        }
        else if(user.getStatus() == 2) {
            userStatusImageView.setImageResource(R.mipmap.blue_circle_icone_5480_128);
        }
        else if(!user.getHangoutStatus().equals(NO_HANGOUT)) {
            userStatusImageView.setImageResource(R.mipmap.orange_circle_icone_6032_128);
        }
        else if(user.getStatus().equals(AVAILABLE)) {
            userStatusImageView.setImageResource(R.mipmap.green_circle_icone_4156_128);
        }
        else if(user.getStatus().equals(BUSY)) {
            userStatusImageView.setImageResource(R.mipmap.red_circle_icone_5751_128);
        }
        else{
            userStatusImageView.setImageResource(R.mipmap.gray_circle_icone_6920_128);
        }

        // Expanded View
        mainListItemExpandedView = (RelativeLayout) findViewById(R.id.mainListItemExpandedView);
        mainListItemExpandedView.setOnClickListener(this);

        expandedUsernameLabel = (TextView) findViewById(R.id.expandedUsernameLabel);
        expandedUsernameLabel.setText(user.getUsername());
        expandedUsernameLabel.setOnClickListener(this);

        userIconImageView = (ImageView) findViewById(R.id.userIconImageView);
        if(user.getStatus() < -1 || user.getStatus() > 2) {
            userIconImageView.setImageResource(R.mipmap.gray_circle_question_trans);
        }
        else if(user.getStatus() == 2) {
            userIconImageView.setImageResource(R.mipmap.blue_trans);
        }
        else if(!user.getHangoutStatus().equals(NO_HANGOUT)) {
            userIconImageView.setImageResource(R.mipmap.orange_trans);
        }
        else if(user.getStatus().equals(AVAILABLE)) {
            userIconImageView.setImageResource(R.mipmap.green_trans);
        }
        else if(user.getStatus().equals(BUSY)) {
            userIconImageView.setImageResource(R.mipmap.red_trans);
        }
        else {
            userIconImageView.setImageResource(R.mipmap.grey_trans);
        }
        userIconImageView.setBackgroundResource(R.mipmap.default_profile_icon);

        expandedLocationLabel = (TextView) findViewById(R.id.expandedLocationLabel);

        StringBuilder locationStringBuilder = new StringBuilder();

        Location youLocation = new Location("");

        Location userLocation = user.getLocation();

        float[] resultArray = new float[1];

        youLocation.distanceBetween(you.getLatitude(), you.getLongitude(), user.getLatitude(), user.getLongitude(), resultArray);

        double distanceFeet = resultArray[0] * 3.28084;

        DecimalFormat df = new DecimalFormat("#.##");


        //displays how far away your contacts are from you in feet/miles
        if (distanceFeet < 5280) {
            locationStringBuilder.append("Is ")
                    .append(df.format(distanceFeet))
                    .append(" feet away.")
                    .append("\n");
        }
        else {
            locationStringBuilder.append("Is ")
                    .append(df.format(distanceFeet/5280))
                    .append(" miles away.")
                    .append("\n");
        }


        //Location userLocation = user.getLocation();



//        locationStringBuilder.append("Lat: ").append(userLocation.getLatitude()).append("\n")
//                .append("Long: ").append(userLocation.getLongitude());
        expandedLocationLabel.setText(locationStringBuilder.toString());

        if (expanded) {
            expand();
        }
        else {
            collapse();
        }
    }


    public void expand() {
        expanded = true;
        changing = true;
        mainListItemCollapsedView.setVisibility(View.GONE);
        mainListItemExpandedView.setVisibility(VISIBLE);
    }

    public void collapse() {
        expanded = false;
        changing = true;
        mainListItemCollapsedView.setVisibility(VISIBLE);
        mainListItemExpandedView.setVisibility(View.GONE);
    }

    public void collapseWithAnimation() {
        collapse();
        // animation
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.usernameLabel:
                expand();
                break;
            case R.id.expandedUsernameLabel:
                collapse();
                break;
            case R.id.mainListItemExpandedView:
                collapseWithAnimation();
                break;
            case R.id.mainListItemCollapsedView:
                expand();
                break;
            default:
                break;
        }
    }

    // refresh after a delete
    private void refresh() {
        Activity activity = (Activity) context;
        activity.finish();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
