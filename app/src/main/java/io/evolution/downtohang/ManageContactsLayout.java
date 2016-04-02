/*

package io.evolution.downtohang;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


*/
/**
 * Created by Patrick on 4/2/2016.
 *//*


public class ManageContactsLayout extends RelativeLayout implements View.OnClickListener{

    //Define variables for widgets
    private ImageView manageContactsAdapterUserNameImageView;
    private TextView manageContactsAdapterUserNameLabel;
    private Button manageContactsAdapterActionButton;

    public Context context;

    public ManageContactsLayout(Context context){
        super(context);
    }

    public ManageContactsLayout(Context context, User user){  //Or Pass database?
        super(context);

        //Set context
        this.context = context;

        //Inflate the layout
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.manage_contacts_adapter, this, true);

        //Get references to widgets
        manageContactsAdapterUserNameImageView = (ImageView)
                findViewById(R.id.manageContactsAdapterUserNameImageView);
        manageContactsAdapterUserNameLabel = (TextView)
                findViewById(R.id.manageContactsAdapterUserNameLabel);
        manageContactsAdapterActionButton = (Button)
                findViewById(R.id.manageContactsAdapterActionButton);

        //Set the listeners
        manageContactsAdapterActionButton.setOnClickListener(this);
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.manageContactsAdapterActionButton:
                Log.d("ManageContacts Button: ", "PRESSED!");
                break;
        }
    }



}

*/
