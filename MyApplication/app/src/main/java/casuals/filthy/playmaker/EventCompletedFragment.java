package casuals.filthy.playmaker;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Steven on 4/25/2015.
 */
public class EventCompletedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.events_page, container,false);
        return eventView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button participants = (Button) getView().findViewById(R.id.user_button);
        participants.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.participant_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();

                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
        Button items = (Button) getView().findViewById(R.id.item_button);
        items.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.items_popout, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button dismissButton = (Button)popupView.findViewById(R.id.dismiss);
                dismissButton.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();

                    }});
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }
}
