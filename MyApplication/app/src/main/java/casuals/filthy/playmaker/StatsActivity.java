package casuals.filthy.playmaker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by Shane on 4/21/2015.
 */
public class StatsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayout1);
        for (int i = 0; i < 30; i++) {
            TextView tv = new TextView(StatsActivity.this);
            tv.setText("Dynamic layouts ftw!");
            tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            ll.addView(tv);;
        }
    }
}

