package casuals.filthy.playmaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;

import java.text.SimpleDateFormat;
import java.util.Date;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;
import casuals.filthy.playmaker.data.beans.PollBean;

/**
 * Created by Steven on 4/25/2015.
 */
public class EventActivity extends BaseActivity implements AsyncResponse {
    private long eventId;
    private ProgressDialog progress;
    private EventBean event;

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


        /*final TextView event_date = (TextView) findViewById(R.id.event_date);
        final TextView event_time = (TextView) findViewById(R.id.event_time);
        final TextView poll_message = (TextView) findViewById(R.id.poll_message);
        //final Button participants = (Button) findViewById(R.id.user_button);
        //final Button items = (Button) findViewById(R.id.item_button);
        final CheckBox poll_option1 = (CheckBox) findViewById(R.id.poll_option1);
        final CheckBox poll_option2 = (CheckBox) findViewById(R.id.poll_option2);
        final CheckBox poll_option3 = (CheckBox) findViewById(R.id.poll_option3);
        if() {
            poll_message.setVisibility(View.INVISIBLE);
            poll_option1.setVisibility(View.INVISIBLE);
            poll_option2.setVisibility(View.INVISIBLE);
            poll_option3.setVisibility(View.INVISIBLE);
        }
        else {
            event_date.setVisibility(View.INVISIBLE);
            event_time.setVisibility(View.INVISIBLE);
        }*/
        /*participants.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GroupActivity.viewpager.setPagingEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.participant_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        GroupActivity.viewpager.setPagingEnabled(true);
                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
        items.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GroupActivity.viewpager.setPagingEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.items_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        GroupActivity.viewpager.setPagingEnabled(true);
                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });*/
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
        //final Button participants = (Button) findViewById(R.id.user_button);
        //final Button items = (Button) findViewById(R.id.item_button);
        CheckBox poll_option1 = (CheckBox) findViewById(R.id.poll_option1);
        CheckBox poll_option2 = (CheckBox) findViewById(R.id.poll_option2);
        CheckBox poll_option3 = (CheckBox) findViewById(R.id.poll_option3);
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
            poll_message.setVisibility(View.GONE);
            poll_option1.setVisibility(View.GONE);
            poll_option2.setVisibility(View.GONE);
            poll_option3.setVisibility(View.GONE);
        }
        else {
            event_date.setVisibility(View.GONE);
            event_time.setVisibility(View.GONE);
        }

        if (!event.isReported())
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
                entry.setText("Team " + t);
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
    }
}












