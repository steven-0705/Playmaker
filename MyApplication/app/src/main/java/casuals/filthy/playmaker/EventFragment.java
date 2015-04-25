package casuals.filthy.playmaker;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Steven on 4/25/2015.
 */
public class EventFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.events_page, container,false);
        return eventView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView pollMessage = (TextView) getView().findViewById(R.id.poll_message);
        TextView date = (TextView) getView().findViewById(R.id.event_date);
        TextView time = (TextView) getView().findViewById(R.id.event_time);
        CheckBox option1 = (CheckBox) getView().findViewById(R.id.poll_option1);
        CheckBox option2 = (CheckBox) getView().findViewById(R.id.poll_option2);
        CheckBox option3 = (CheckBox) getView().findViewById(R.id.poll_option3);
        boolean eventPending = true;
        if(eventPending) {
            date.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);

        }
        else {
            pollMessage.setVisibility(View.INVISIBLE);
            option1.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.INVISIBLE);
            option3.setVisibility(View.INVISIBLE);
        }
        Button participants = (Button) getView().findViewById(R.id.user_button);
        participants.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.viewpager.setPagingEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.participant_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        MainActivity.viewpager.setPagingEnabled(true);
                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
        Button items = (Button) getView().findViewById(R.id.item_button);
        items.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.viewpager.setPagingEnabled(false);
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.items_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        MainActivity.viewpager.setPagingEnabled(true);
                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }
}
