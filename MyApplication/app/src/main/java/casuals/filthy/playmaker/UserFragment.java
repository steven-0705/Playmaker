package casuals.filthy.playmaker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.UserBean;

import java.util.List;

/**
 * Created by Shane on 3/27/2015.
 */
public class UserFragment extends Fragment implements AsyncResponse{
private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View userView = inflater.inflate(R.layout.user, container,false);
            Bundle args = getArguments();

            // TODO Auto-generated method stub
            return userView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState)
       {
            super.onViewCreated(view, savedInstanceState);
//           TextView display = (TextView) getView().findViewById(R.id.user_display);
//           display.setText(MainActivity.getName());
//           TextView name = (TextView) getView().findViewById(R.id.user_favoriteText);
//           name.setText(MainActivity.getEmail());
//           TextView id = (TextView) getView().findViewById(R.id.user_user);
//           id.setText(MainActivity.getId());
           DatastoreAdapter adapter = new DatastoreAdapter(this);
           adapter.getUser(MainActivity.getId(), MainActivity.getName(), MainActivity.getEmail());
           Button groupCreate = (Button) getView().findViewById(R.id.groupCreate);
           groupCreate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                   final EditText input= new EditText(v.getContext());
                   input.setImeOptions(EditorInfo.IME_ACTION_DONE);
                   input.setSingleLine();
                   alert.setTitle("Enter a name for your group.");
                   alert.setView(input);
                   alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           if (input.getText().toString().matches("")) {
                               Toast.makeText(getActivity().getApplicationContext(), "You did not enter anything", Toast.LENGTH_SHORT).show();
                           } else if (input.getText().toString().length() < 3) {
                               Toast.makeText(getActivity().getApplicationContext(), "Group name must be at least 3 letters long", Toast.LENGTH_SHORT).show();
                           } else {
                               String temp = input.getText().toString();
                               DatastoreAdapter adapter = new DatastoreAdapter(UserFragment.this);
                               adapter.createGroup(temp, MainActivity.getId());
                           }
                       }
                   });
                   alert.show();
               }
           });
           Button eventCreate = (Button) getView().findViewById(R.id.eventCreate);
           eventCreate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
//                   LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                   View popupView = inflater.inflate(R.layout.events, null);
//                   final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//
//                   Button btnDismiss = (Button)popupView.findViewById(R.id.button2);
//                   btnDismiss.setOnClickListener(new Button.OnClickListener(){
//                       @Override
//                       public void onClick(View v) {
//                           // TODO Auto-generated method stub
//                           popupWindow.dismiss();
//                       }});
//                   popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                   Intent i = new Intent(getActivity().getApplicationContext(), EventCreate.class);

                   startActivity(i);
                   //finish();
               }
           });

           Button stats = (Button) getView().findViewById(R.id.statsButton);
           stats.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(getActivity().getApplicationContext(), StatsActivity.class);
                   startActivity(i);
                   //finish();
               }
           });
//            Button test = (Button) getView().findViewById(R.id.button);
//            test.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    BaseActivity.login=3;
//
//                    Intent i = new Intent(getActivity().getApplicationContext(), BaseActivity.class);
//                    startActivity(i);
//
//                    getActivity().finish();
//
//
//                }
//            });
       }

       @Override
       public void response(Object o) {
           if(!(o instanceof UserBean)) {
               return;
           }
           UserBean user = (UserBean) o;
           List<UserBean.UserGroupBean> groupList = user.getGroups();
           for(UserBean.UserGroupBean group: groupList) {
               Log.i("Name:",group.name);
           }
       }
}
