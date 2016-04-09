package io.evolution.downtohang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

    private int status = 0;
    private ImageButton changeStatusImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        changeStatusImageButton = (ImageButton) findViewById(R.id.changeStatusImageButton);
        changeStatusImageButton.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.changeStatusImageButton:
                changeStatus();
        }
    }

    private void changeStatus() {
        if(status == 1) {
            changeStatusImageButton.setImageResource(R.mipmap.green_circle_icone_4156_128);
            status = 0;
        }
        else {
            changeStatusImageButton.setImageResource(R.mipmap.red_circle_icone_5751_128);
            status = 1;
        }


    }
}
