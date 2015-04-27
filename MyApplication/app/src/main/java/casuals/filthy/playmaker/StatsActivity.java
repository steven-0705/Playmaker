package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;
import casuals.filthy.playmaker.data.beans.GroupBean;

/**
 * Created by Shane on 4/21/2015.
 */
public class StatsActivity extends Activity implements AsyncResponse{

    private EventBean event;
    private long EventId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EventId = extras.getLong("EVENT_ID");
        }
        DatastoreAdapter dsa = new DatastoreAdapter(this);
        dsa.getEvent(GroupActivity.getGroupId(), EventId);
        int numteams = 0;
        /*numteams = event.getNumTeams();
        Log.w("NumTeams: ", String.valueOf(numteams));*/
        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayout1);
        /*for (int i = 0; i < 30; i++) {
            TextView tv = new TextView(StatsActivity.this);
            tv.setText("Dynamic layouts ftw!");
            tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            ll.addView(tv);;
        }*/
    }

    @Override
    public void response(Object o) {
        if (!(o instanceof EventBean)) {
            return;
        }
        event = (EventBean) o;
        int i=1;
        List<EventBean.EventTeam> listTeams = event.getTeams();
        Log.w("Tag: ", String.valueOf(event.getNumTeams()));
        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayout1);
        Log.w("Teams: ", String.valueOf(listTeams.size()));
        for (EventBean.EventTeam team: listTeams) {
            TextView tv = new TextView(StatsActivity.this);
            tv.setText("Team " + String.valueOf(i));
            ll.addView(tv);
            i++;
        }
    }
}

