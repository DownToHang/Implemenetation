package io.evolution.downtohang;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by eliakah on 4/2/2016.
 */
public class HangoutActivity extends Activity {
    private Button leave_Button;
    private ListView hangout_ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hangout);

        leave_Button = (Button) findViewById(R.id.leave_Button);

    }


}
