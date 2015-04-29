package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;

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
        List<EventBean.EventTeam> teamlist = event.getTeams();
        ListView lv = (ListView) findViewById(R.id.team_list);

        String[] names = new String[teamlist.size()];
        Log.i("Num Teams: ", "" + teamlist.size());
        if(event.getNumTeams() != 0) {
            for (int i = 0; i < teamlist.size(); i++) {
                names[i] = "Team " + (i + 1);
            }
        }
        else {
            Map<String,String> attending = event.getAttending();
            for(int i = 0; i < teamlist.size(); i++) {
                List<String> members = teamlist.get(i).getMembers();
                String name = attending.get(members.get(0));
                names[i] = name;
            }
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
                        List<Double> up = new ArrayList<Double>();
                        List<Double> down = new ArrayList<Double>();

                        for (int i = 0; i < event.getTeams().size(); i++) {
                            NumberPicker np = (NumberPicker) getViewByPosition(i, teamsView).findViewById(R.id.rankPicker);

                            up.add((double) (event.getTeams().size() + 1 - np.getValue()) / event.getTeams().size());
                            down.add(0.0);
                        }

                        new DatastoreAdapter(EventActivity.instance).reportEventStats(GroupActivity.getUserId(),
                                GroupActivity.getGroupId(), event.getId(), up, down);

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}

