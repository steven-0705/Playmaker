package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shane on 4/20/2015.
 */
public class EventCreate extends Activity {
    double latitude;
    double longitude;
    List<Address> geocodeMatches = null;
    private MapView mapView;
    private GoogleMap gMap;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.events);
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Basketball", "Baseball", "LAN Party", "Other"};
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
        final TextView getTime = (TextView) findViewById(R.id.EditTime);
        final TextView itemList = (TextView) findViewById(R.id.itemlist);
        final String[] time = new String[3];
        final Spinner getOption = (Spinner) findViewById(R.id.spinner1);
        getTime.setEnabled(false);
        itemList.setEnabled(false);
        getOther.setEnabled(false);
        for (int i=0; i<time.length; i++) {
            time[i] = "";
        }

        getOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView av, View v, int pos, long id) {
                if (getOption.getSelectedItem().toString().matches("Other")) {
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
                int i = 0;

                DatePickerDialog DatePicker;
                DatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(android.widget.DatePicker datepicker, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        month = month + 1;
                        String temp = "" + month + "/" + day + "/" + year;
                        for (int i=0; i<time.length; i++){ // This loop and these checks are necessary because Android is dumb and detects a single Click twice
                            if (time[i].matches("")) {
                                time[i] = (temp);
                                break;
                            }
                            if (time[i].matches(temp)) {
                                break;
                            }
                        }
                        temp = time[0] + "\n" + time[1] + "\n" + time[2] + "\n";
                        getTime.setText(temp); // sets the Text field to a max of 3 dates
                    }
                }, mYear, mMonth, mDay);
                DatePicker.setTitle("Select Date");
                DatePicker.show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                  /*String loc = getLoc.getText().toString();
                    String other = getOther.getText().toString();
                    String option = getOption.getSelectedItem().toString();
                    if (loc.isEmpty()) {
                        Context context = v.getContext();
                        CharSequence text = "Please provide an address";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }
                    if (option=="Other" && other == "") {
                        Context context = v.getContext();
                        CharSequence text = "Please provide an event name";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + loc);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);*/

                    /*EditTextPreference Input;
                    Input = new EditTextPreference(v.getContext(), new EditTextPreference.OnPreferenceClickListener() {
                        public void onPreferenceClick(EditTextPreference Input) {
                            Log.w("Tag:", Input.getText());
                        }

                    });*/

                Log.w("Date 1:", time[0]);
                Log.w("Date 2:", time[1]);
                Log.w("Date 3:", time[2]);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText input= new EditText(v.getContext());
                input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                input.setSingleLine();
                alert.setTitle("Add an item!");
                alert.setView(input);
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(getApplicationContext(), "You did not enter anything", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            String temp = input.getText().toString();
                            itemList.append(temp + ", ");
                        }
                    }
                });

                alert.show();
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

    }



