package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;
import casuals.filthy.playmaker.data.beans.UserBean;

/**
 * Created by Shane on 4/21/2015.
 */
public class StatsActivity extends Activity implements AsyncResponse{

    private EventBean event;
    private long EventId;

    public static String[] places = {"1st", "2nd", "3rd", "4th" , "5th",
            "6th", "7th", "8th", "9th", "10th", "11th", "14th", "15th",
            "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd",
            "24th", "25th", "26th", "27th", "28th", "29th", "30th" };

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
        List<EventBean.EventTeam> teamlist = event.getTeams();
        ListView lv = (ListView) findViewById(R.id.team_list);

        String[] names = new String[teamlist.size()];
        for (int i = 0; i < teamlist.size(); i++) {
            names[i] = "Team " + (i + 1);
        }

        ListAdapter adapter = new RankAdapter(this,R.layout.rank_view,
                R.id.rank_team_name, names, teamlist.size());

        lv.setAdapter(adapter);

        ((TextView) findViewById(R.id.stats_title)).setText(event.getName() + " Stats");
    }

    public void submit(View v) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_agenda)
                .setTitle("Submit Scores")
                .setMessage("Are you sure these scored are correct?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lets submit some scores

                        ListView teamsView = (ListView) findViewById(R.id.team_list);
                        List<Integer> score = new ArrayList<Integer>();
                        List<Integer> down = new ArrayList<Integer>();
                        for (int i = 0; i < event.getTeams().size(); i++) {
                            NumberPicker np = (NumberPicker) ((View) teamsView.getAdapter().getView(0, null, null)).findViewById(R.id.rankPicker);

                            score.add(event.getTeams().size() + 1 - np.getValue());
                            down.add(0);
                        }

                        new DatastoreAdapter(null).reportEventStats(GroupActivity.getUserId(),
                                GroupActivity.getGroupId(), event.getId(), score, down);

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}

