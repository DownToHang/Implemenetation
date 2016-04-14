package io.evolution.downtohang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import io.evolution.downtohang.MainActivity;
import io.evolution.downtohang.R;
import io.evolution.downtohang.User;

/**
 * Created by Bill Ezekiel on 3/7/2016.
 */
public class MainListItemLayout extends RelativeLayout implements OnClickListener {

    private static final String BUSY = "0",NO_HANGOUT = "0";
    private static final String AVAILABLE = "1";

    // Collapsed View
    private RelativeLayout mainListItemExpandedView;
    private RelativeLayout mainListItemCollapsedView;
    private ImageView userStatusImageView;
    private TextView usernameLabel;
    private ImageButton acceptImageButton;
    private ImageButton rejectImageButton;

    // Expanded View
    private ImageView userIconImageView;
    private TextView expandedUsernameLabel;
    private TextView expandedLocationLabel;
    private ImageButton expandedAcceptImageButton;
    private ImageButton expandedRejectImageButton;


    private Context context;
    private boolean expanded;
    private User user;
    private boolean changing;

    public MainListItemLayout(Context context, User user, boolean expanded) {
        super(context);
        this.user = user;
        this.expanded = expanded;
        changing = false;

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
        if(!user.getHangStatus().equals(NO_HANGOUT)) {
            userStatusImageView.setImageResource(R.mipmap.orange_circle_icone_6032_128);
        }
        else if(user.getAvailability().equals(AVAILABLE)) {
            userStatusImageView.setImageResource(R.mipmap.green_circle_icone_4156_128);
        }
        else if(user.getAvailability().equals(BUSY)) {
            userStatusImageView.setImageResource(R.mipmap.red_circle_icone_5751_128);
        }
        /*else if(pending friend use blue) {

        }*/
        else {
            userStatusImageView.setImageResource(R.mipmap.gray_circle_icone_6920_128);
        }

        acceptImageButton = (ImageButton) findViewById(R.id.acceptImageButton);
        rejectImageButton = (ImageButton) findViewById(R.id.rejectImageButton);
        acceptImageButton.setOnClickListener(this);
        rejectImageButton.setOnClickListener(this);


        // Expanded View
        mainListItemExpandedView = (RelativeLayout) findViewById(R.id.mainListItemExpandedView);
        mainListItemExpandedView.setOnClickListener(this);

        expandedUsernameLabel = (TextView) findViewById(R.id.expandedUsernameLabel);
        expandedUsernameLabel.setText(user.getUsername());
        expandedUsernameLabel.setOnClickListener(this);

        userIconImageView = (ImageView) findViewById(R.id.userIconImageView);
        if(!user.getHangStatus().equals(NO_HANGOUT)) {
            userIconImageView.setImageResource(R.mipmap.orange_trans);
        }
        else if(user.getAvailability().equals(AVAILABLE)) {
            userIconImageView.setImageResource(R.mipmap.green_trans);
        }
        else if(user.getAvailability().equals(BUSY)) {
            userIconImageView.setImageResource(R.mipmap.red_trans);
        }
        /*else if(pending friend use blue) {

        }*/
        else {
            userIconImageView.setImageResource(R.mipmap.gray_circle_question_icone_6920_128);
        }
        userIconImageView.setBackgroundResource(R.mipmap.default_profile_icon);

        expandedLocationLabel = (TextView) findViewById(R.id.expandedLocationLabel);
        expandedAcceptImageButton = (ImageButton) findViewById(R.id.expandedAcceptImageButton);
        expandedAcceptImageButton.setBackgroundColor(0);
        expandedAcceptImageButton.setOnClickListener(this);

        expandedRejectImageButton = (ImageButton) findViewById(R.id.expandedRejectImageButton);
        expandedRejectImageButton.setBackgroundColor(0);
        expandedRejectImageButton.setOnClickListener(this);

        if (expanded) {
            expand();
        }
        else {
            collapse();
        }
    }

    public MainListItemLayout(Context context,User user) {
        this(context, user, false);
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
            case R.id.acceptImageButton:
                System.out.println("Clicked acceptImageButton");
                break;
            case R.id.rejectImageButton:
                System.out.println("Clicked rejectImageButton");
                break;
            case R.id.expandedAcceptImageButton:
                System.out.println("Clicked expandedAcceptImageButton");
                break;
            case R.id.expandedRejectImageButton:
                System.out.println("Clicked expandedRejectImageButton");
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
