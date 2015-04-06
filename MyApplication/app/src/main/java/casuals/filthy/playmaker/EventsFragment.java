package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import android.content.Context;
import android.widget.Toast;

public class EventsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.events, container, false);
        // TODO Auto-generated method stub
        Spinner dropdown = (Spinner) eventView.findViewById(R.id.spinner1);
        String[] items = new String[]{"Basketball", "Baseball", "LAN Party", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        //EditText editText = (EditText) eventView.findViewById(R.id.edittext1);
        //final EditText askLoc = (EditText) eventView.findViewById(R.id.locationView);
        //EditText getLoc = (EditText) eventView.findViewById(R.id.edittext2);
        //final EditText editTime = (EditText) eventView.findViewById(R.id.edittime1);
        //editTime.setEnabled(false);
        return eventView;
    }

    @Override

        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getView());
        setHasOptionsMenu(true);
            final Button button1 = (Button) view.findViewById(R.id.button1);
            final Button button2 = (Button) view.findViewById(R.id.button2);
            final EditText getLoc = (EditText) view.findViewById(R.id.edittext2);
            final EditText getOther = (EditText) view.findViewById(R.id.edittext1);
            final Spinner getOption = (Spinner) view.findViewById(R.id.spinner1);
            button1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar currentDate = Calendar.getInstance();
                    int mYear = currentDate.get(Calendar.YEAR);
                    int mMonth = currentDate.get(Calendar.MONTH);
                    int mDay = currentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog DatePicker;
                    DatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                            // TODO Auto-generated method stub
                            month = month + 1;
                            //editTime.append("" + month + "/" + day + "/" + year + "\n");
                            button1.setText("" + month + "/" + day + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
                    DatePicker.setTitle("Select Date");
                    DatePicker.show();
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loc = getLoc.getText().toString();
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
                    startActivity(mapIntent);
                }
            });
    }
}