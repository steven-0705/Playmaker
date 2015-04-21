package casuals.filthy.playmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;

import java.util.Calendar;

/**
 * Created by Shane on 4/20/2015.
 */
public class EventCreate extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.events);
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Basketball", "Baseball", "LAN Party", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.additem);
        final EditText getLoc = (EditText) findViewById(R.id.edittext2);
        final EditText getOther = (EditText) findViewById(R.id.edittext1);
        final EditText getTime = (EditText) findViewById(R.id.EditTime);
        final EditText itemList = (EditText) findViewById(R.id.itemlist);
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
                alert.setTitle("Add an item!");
                alert.setView(input);

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = input.getText().toString();
                        itemList.append(temp + ", ");
                    }
                });

                alert.show();
            }
        });
    }
    }



