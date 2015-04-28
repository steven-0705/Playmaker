package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
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
    }

    @Override
    public void response(Object o) {
        if (!(o instanceof EventBean)) {
            return;
        }
        event = (EventBean) o;
        List<String> teamlist = new ArrayList<String>();
        int i=1;
        List<EventBean.EventTeam> listTeams = event.getTeams();
        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayout1);
        ListView lv = (ListView) findViewById(R.id.team_list);
        for (EventBean.EventTeam team: listTeams) {
            //TextView tv = new TextView(StatsActivity.this);
            //tv.setText("Team " + String.valueOf(i));
            //lv.addView(tv);
            teamlist.add("Team " + String.valueOf(i));
            i++;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, teamlist);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        /*ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("Position: ", String.valueOf(position));
            }
        });*/
    }
}

