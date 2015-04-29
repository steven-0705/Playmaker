package casuals.filthy.playmaker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;

/**
 * Created by Shane on 4/20/2015.
 */
public class EventCreate extends Activity implements AsyncResponse{
    DatastoreAdapter test = new DatastoreAdapter(this);
    double latitude;
    double longitude;
    static int numTeam = 0;
    static boolean teamAuto = false;
    List<Address> geocodeMatches = null;
    private MapView mapView;
    private GoogleMap gMap;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle extras = getIntent().getExtras();
        String[] items = new String[extras == null ? 1 : extras.size()+1];
        if(extras != null) {
            for(int i = 0; i<extras.size(); i++)
            {
                String item = extras.getString("EVENT_TYPE_"+i);
                item = ((char) (item.charAt(0) - 32)) + item.substring(1);
                items[i] = item;
            }
        }
        items[extras == null ? 0 : extras.size()] = "Add New Event Type";
        setContentView(R.layout.events);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        mapView = new MapView(this.getApplicationContext());
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
        gMap.setBuildingsEnabled(true);
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.additem);
        final EditText getLoc = (EditText) findViewById(R.id.edittext2);
        final EditText getOther = (EditText) findViewById(R.id.edittext1);
        final EditText nameOfEvent = (EditText) findViewById(R.id.TitleNameofEvent);
        final TextView getTime = (TextView) findViewById(R.id.EditTime);
        final TextView itemList = (TextView) findViewById(R.id.itemlist);
        final String[] date = new String[3];
        final TextView getTeam = (TextView) findViewById(R.id.event_team);
        final String[] time = new String[3];
        final String[] miltime = new String[3];
        final String[] hourAndMin = new String[2];
        final Spinner getOption = (Spinner) findViewById(R.id.spinner1);
        itemList.setEnabled(false);
        getOther.setEnabled(false);
        getTeam.setEnabled(false);

        final Switch teamEnabled = (Switch) findViewById(R.id.switch2);

        for (int i=0; i<date.length; i++) {
            date[i] = "";
            for (int j = 0; j < time.length; j++) {
                time[j] = "";
                miltime[j] = "";
            }
        }


            getOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView av, View v, int pos, long id) {
                    if (getOption.getSelectedItem().toString().matches("Add New Event Type")) {
                        getOther.setEnabled(true);
                    } else {
                        getOther.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView av) {
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar currentDate = Calendar.getInstance();
                    int mYear = currentDate.get(Calendar.YEAR);
                    int mMonth = currentDate.get(Calendar.MONTH);
                    int mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                    final int mHour = currentDate.get(Calendar.HOUR);
                    final int mMin = currentDate.get(Calendar.MINUTE);
                    int i = 0;
                    boolean timeReady = false;
                    DatePickerDialog DatePicker;
                    final TimePickerDialog TimePicker;
                    TimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Do something with the time chosen by the user
                            String temp = "";
                            if (minute / 10 == 0) {
                                if (hourOfDay == 12) {
                                    temp = "" + "12" + ":0" + minute + " PM";
                                }

                                if (hourOfDay > 12) {
                                    temp = "" + (hourOfDay - 12) + ":0" + minute + " PM";
                                }
                                if (hourOfDay < 12) {
                                    temp = "" + hourOfDay + ":0" + minute + " AM";
                                }
                            } else {
                                if (hourOfDay == 12) {
                                    temp = "" + "12" + ":" + minute + " PM";
                                }
                                if (hourOfDay > 12) {
                                    temp = "" + (hourOfDay - 12) + ":" + minute + " PM";
                                }
                                if (hourOfDay < 12) {
                                    temp = "" + hourOfDay + ":" + minute + " AM";
                                }
                            }
                            for (int i = 0; i < time.length; i++) { // This loop and these checks are necessary because Android is dumb and detects a single Click twice
                                if (!date[i].matches("") && time[i].matches("")) {
                                    time[i] = (temp);
                                    miltime[i] = hourOfDay + ":" + minute;
                                    break;
                                }
                            }
                            temp = date[0] + " " + time[0] + "\n" + date[1] + " " + time[1] + "\n" + date[2] + " " + time[2] + "\n";
                            getTime.setText(temp); // sets the Text field to a max of 3 dates
                        }
                    }, mHour, mMin, false);
                    TimePicker.setTitle("Select Time");
                    DatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                            // TODO Auto-generated method stub
                            if(datepicker.isShown()) {
                                month = month + 1;
                                String temp = "" + month + "/" + day + "/" + year;
                                for (int i = 0; i < date.length; i++) { // This loop and these checks are necessary because Android is dumb and detects a single Click twice
                                    if (date[i].matches("")) {
                                        date[i] = (temp);
                                        TimePicker.show();
                                        break;
                                    }
                                    /*
                                    if (date[i].matches(temp)) {
                                        break;
                                    }
                                    */
                                }
                            }
                        }
                    }, mYear, mMonth, mDay);
                    DatePicker.setTitle("Select Date");
                    DatePicker.show();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Date> EventDates = new ArrayList<Date>();
                    List<Long> EventTimes = new ArrayList<Long>();
                    List<String> getItems = new ArrayList<String>();
                    TextView itemList = (TextView) findViewById(R.id.itemlist);
                    String [] items = itemList.getText().toString().split(", ");
                    EditText edittext = (EditText) findViewById(R.id.edittext2);
                    String location = edittext.getText().toString();
                    if(location.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter a Location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(nameOfEvent.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter Title of Event", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int numdates = 0;

                    for (int k = 0; k < items.length; k++) {
                        getItems.add(items[k]);
                    }

                    for (int j = 0; j < date.length; j++) {
                        if (!date[j].matches("")) {
                            numdates++;
                        }
                    }

                    for (int i = 0; i < numdates; i++) {
                        String[] tempdate = date[i].split("/");
                        String[] temptime = miltime[i].split(":");
                        Date eventdate = new Date(Integer.parseInt(tempdate[2]), Integer.parseInt(tempdate[0]) - 1, Integer.parseInt(tempdate[1]), Integer.parseInt(temptime[0]), Integer.parseInt(temptime[1]));
                        EventDates.add(eventdate);
                    }
                    if(EventDates.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter Dates", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Collections.sort(EventDates);
                    for(int i = 0; i<EventDates.size(); i++)
                    {
                        EventTimes.add(i, EventDates.get(i).getTime());
                    }
                    Log.w("ListLen: ", Integer.toString(EventDates.size()));
                    DatastoreAdapter dsa = new DatastoreAdapter(EventCreate.this);



                    if (getOption.getSelectedItem().toString() == "Add New Event Type") {
                        if(getOther.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter New Event Type", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dsa.createEvent(GroupActivity.getUserId(), GroupActivity.getGroupId(), nameOfEvent.getText().toString(),  getOther.getText().toString(), (EventTimes.get(0)-(24*60*60*1000)), location,true /*teamAuto*/,numTeam,EventTimes, getItems);

                    } else {
                        dsa.createEvent(GroupActivity.getUserId(), GroupActivity.getGroupId(), nameOfEvent.getText().toString(), getOption.getSelectedItem().toString(), (EventTimes.get(0) - (24 * 60 * 60 * 1000)), location, true /*teamAuto*/, numTeam, EventTimes, getItems);
                    }
                    finish();
                }
//tring userId, long groupId, String eventName, String eventType, long closeDate,
//String address, boolean autogen, int numTeams, List<Long> eventDates, List<String> items)
                //user id , long group id, name event of string, event Type (group contains this),  String of address, boolean for teams,int # teams, list of date;

            });



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText input = new EditText(v.getContext());
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                input.setSingleLine();
                alert.setTitle("Add an item!");
                alert.setView(input);
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(getApplicationContext(), "You did not enter anything", Toast.LENGTH_SHORT).show();
                        } else {
                            String temp = input.getText().toString();
                            itemList.append(temp + ", ");
                        }
                    }
                });

                alert.show();
            }
        });
    teamEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.team_select, null);
                NumberPicker np = (NumberPicker) popupView.findViewById(R.id.numberPicker1);
                np.setMinValue(2);
                np.setMaxValue(10);
                np.setEnabled(true);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        Log.w("Numteam1: ", String.valueOf(newVal));
                        numTeam=newVal;
                    }
                });
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(false);
                Button btnCancel = (Button) popupView.findViewById(R.id.back);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numTeam=0;
                        teamEnabled.setChecked(false);
                        popupWindow.dismiss();
                    }
            });
                Button btnDismiss = (Button)popupView.findViewById(R.id.confirm);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        if (numTeam == 0) {
                            numTeam = 2;
                        }
                    }});
                popupWindow.showAtLocation(buttonView.getRootView(), Gravity.CENTER, 0, 0);
            }

            else
            {
                numTeam=0;
                teamAuto = false;
            }
        }
    });



    }

    public void getLocation(View view) {
        EditText edittext = (EditText) findViewById(R.id.edittext2);
        String place = edittext.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

        try {
            geocodeMatches = new Geocoder(this).getFromLocationName( place, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!geocodeMatches.isEmpty()) {
            latitude = geocodeMatches.get(0).getLatitude();
            longitude = geocodeMatches.get(0).getLongitude();
            Toast.makeText(this, geocodeMatches.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            edittext.setText(geocodeMatches.get(0).getAddressLine(0));
        }

        LatLng LatLong = new LatLng(latitude, longitude);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLong, 15));

    }


    @Override
    public void response(Object o) {

    }
}


