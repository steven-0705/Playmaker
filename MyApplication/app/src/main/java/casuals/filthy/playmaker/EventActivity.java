package casuals.filthy.playmaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;

/**
 * Created by Steven on 4/25/2015.
 */
public class EventActivity extends BaseActivity implements AsyncResponse {
    private long eventId;
    private ProgressDialog progress;
    private EventBean event;
    public static EventActivity instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            eventId = extras.getLong("EVENT_ID");
        }
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Retrieving your data...");
        progress.show();


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.events_page);

        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getEvent(GroupActivity.getGroupId(), eventId);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void response(Object o) {
        if(!(o instanceof EventBean)) {
            return;
        }
        event = (EventBean) o;
        TextView event_name = (TextView) findViewById(R.id.event_name);
        TextView event_type = (TextView) findViewById(R.id.event_type);
        event_name.setText(event.getName());
        event_type.setText(event.getType());
        TextView poll_message = (TextView) findViewById(R.id.poll_message);
        LinearLayout pollView = (LinearLayout) findViewById(R.id.poll);
        TextView event_date = (TextView) findViewById(R.id.event_date);
        TextView event_time = (TextView) findViewById(R.id.event_time);
        Date date = new Date(event.getDate());
        SimpleDateFormat dateformat = new SimpleDateFormat("M/dd/yy");
        SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");
        String dateString = dateformat.format(date);
        String timeString = timeformat.format(date);
        if(timeString.charAt(0) == '0') { timeString.substring(1); }
        event_date.setText("Date: " + dateString);
        event_time.setText("Time: " + timeString);

        if(event.isClosed()) {
            pollView.setVisibility(View.GONE);
            poll_message.setVisibility(View.GONE);
        }
        else {
            event_date.setVisibility(View.GONE);
            event_time.setVisibility(View.GONE);

            pollView.setVisibility(View.VISIBLE);
            pollView.removeAllViews();
            SimpleDateFormat format = new SimpleDateFormat("h:mm a M/dd/yy");

            // compile votes
            int[] votes = new int[event.getPollMeaning().size()];
            for (int vote: event.getDatePoll().getVotes().values())
                    votes[vote]++;

            // generate the options
            String[] opts = new String[event.getPollMeaning().size()];
            for (int i = 0; i < opts.length; i++) {
                opts[i] = votes[i] + " |   " + format.format(new Date(event.getPollMeaning().get(i)));
            }

            // create the poll
            ListView options = new ListView(this);
            options.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice,
                    android.R.id.text1, opts));
            options.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            options.setLayoutParams(lp);

            // check if they have already voted
            if (event.getDatePoll().getVotes().containsKey(GroupActivity.getUserId())) {
                int pos = event.getDatePoll().getVotes().get(GroupActivity.getUserId());
                options.setItemChecked(pos, true);
            }

            options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new DatastoreAdapter(EventActivity.this).voteOnDate(GroupActivity.getUserId(), GroupActivity.getGroupId(), event.getId(), position);
                }
            });

            pollView.addView(options);
        }

        if (!event.isReported() && GroupActivity.isAdmin() && event.getAttending() != null && event.getAttending().size() > 0)
            findViewById(R.id.report_scores).setVisibility(View.VISIBLE);

        // populate the items
        LinearLayout items = (LinearLayout) findViewById(R.id.items_list);
        items.removeAllViews();
        for (String item: event.getItems()) {
            TextView entry = new TextView(EventActivity.this);
            entry.setText(" - " + item);
            entry.setTextSize(24);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            entry.setLayoutParams(lp);
            items.addView(entry);
        }

        // populate attendees
        if (!event.getAttending().keySet().contains(GroupActivity.getUserId()))
            ((Button) findViewById(R.id.join_event_button)).setVisibility(View.VISIBLE);

        LinearLayout attendees = (LinearLayout) findViewById(R.id.attendees_list);
        attendees.removeAllViews();
        if (event.getNumTeams() > 0) {
            int t = 0;
            for (EventBean.EventTeam team: event.getTeams()) {
                t++;
                TextView entry = new TextView(EventActivity.this);
                entry.setText("    Team " + t);
                entry.setTextSize(32);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                entry.setLayoutParams(lp);
                attendees.addView(entry);

                // add members
                for (String memberId: team.getMembers()) {
                    TextView user = new TextView(EventActivity.this);
                    user.setText(event.getAttending().get(memberId));
                    user.setTextSize(24);
                    lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    user.setLayoutParams(lp);
                    attendees.addView(user);
                }

            }
        }
        else {
            for (String memberId: event.getAttending().keySet()) {
                TextView user = new TextView(EventActivity.this);
                user.setText(event.getAttending().get(memberId));
                user.setTextSize(24);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                user.setLayoutParams(lp);
                attendees.addView(user);
            }
        }

        progress.dismiss();
    }

    public void joinEvent(View v) {
        ((Button) findViewById(R.id.join_event_button)).setVisibility(View.GONE);
        DatastoreAdapter da = new DatastoreAdapter(this);
        da.joinEvent(GroupActivity.getGroupId(), event.getId(), GroupActivity.getUserId());
        Toast.makeText(this, "Joined event", Toast.LENGTH_SHORT).show();
    }

    public void reportScores(View v) {
        // TODO make intent
        instance = this;
        Intent i = new Intent(EventActivity.this.getApplicationContext(), StatsActivity.class);
        i.putExtra("EVENT_ID", event.getId());
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        new DatastoreAdapter(this).getEvent(GroupActivity.getGroupId(), eventId);
    }
}












