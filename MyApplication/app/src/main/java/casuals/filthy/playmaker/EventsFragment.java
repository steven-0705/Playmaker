package casuals.filthy.playmaker;

/**
 * Created by Shane on 3/19/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;



public class EventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.events, container,false);
        Bundle args = getArguments();
        ((TextView) eventView.findViewById(R.id.eventTextView)).setText("i wonder if this works");
        // TODO Auto-generated method stub
        return eventView;
    }


}