package casuals.filthy.playmaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.EventBean;

/**
 * Created by Steven on 4/25/2015.
 */
public class EventActivity extends BaseActivity implements AsyncResponse {
    private boolean eventCompleted;
    private long eventId;
    private ProgressDialog progress;

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

        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getEvent(GroupActivity.getGroupId(), eventId);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.events_page);
        final TextView event_date = (TextView) findViewById(R.id.event_date);
        final TextView event_time = (TextView) findViewById(R.id.event_time);
        final TextView event_name = (TextView) findViewById(R.id.event_name);
        final TextView event_type = (TextView) findViewById(R.id.event_type);
        final TextView poll_message = (TextView) findViewById(R.id.poll_message);
        final Button participants = (Button) findViewById(R.id.user_button);
        final Button items = (Button) findViewById(R.id.item_button);
        final CheckBox poll_option1 = (CheckBox) findViewById(R.id.poll_option1);
        final CheckBox poll_option2 = (CheckBox) findViewById(R.id.poll_option2);
        final CheckBox poll_option3 = (CheckBox) findViewById(R.id.poll_option3);
        if(eventCompleted) {
            poll_message.setVisibility(View.INVISIBLE);
            poll_option1.setVisibility(View.INVISIBLE);
            poll_option2.setVisibility(View.INVISIBLE);
            poll_option3.setVisibility(View.INVISIBLE);
        }
        else {
            event_date.setVisibility(View.INVISIBLE);
            event_time.setVisibility(View.INVISIBLE);
        }
        participants.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GroupActivity.viewpager.setPagingEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.participant_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
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
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        GroupActivity.viewpager.setPagingEnabled(true);
                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
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
        EventBean event = (EventBean) o;
        TextView event_name = (TextView) findViewById(R.id.event_name);
        TextView event_type = (TextView) findViewById(R.id.event_type);
        event_name.setText(event.getName());
        event_type.setText(event.getType());

        progress.dismiss();
    }
}
