package casuals.filthy.playmaker;

import android.app.Activity;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.beans.GroupBean;
import casuals.filthy.playmaker.data.beans.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 3/27/2015.
 */
public class UserActivity extends BaseActivity implements AsyncResponse{

    private String userId;
    private String userName;
    private String userEmail;
    private List<Long> groupIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            userId = extras.getString("ID");
            userEmail = extras.getString("EMAIL");
            userName = extras.getString("DISPLAY_NAME");
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.user);
        final TextView user_name = (TextView) findViewById(R.id.user_display);
        final TextView user_email = (TextView) findViewById(R.id.user_user);
        final ImageView user_image = (ImageView) findViewById(R.id.user_image);
        final TextView user_group = (TextView) findViewById(R.id.user_groups);
        final Button createGroup = (Button) findViewById(R.id.groupCreate);
        final ListView groupList = (ListView) findViewById(R.id.user_group_list);
        user_group.setEnabled(false);
        user_name.setText(userName);
        user_email.setText(userEmail);
        createGroup.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(getApplicationContext(), "You did not enter anything", Toast.LENGTH_SHORT).show();
                        } else if (input.getText().toString().length() < 3) {
                            Toast.makeText(getApplicationContext(), "Group name must be at least 3 letters long", Toast.LENGTH_SHORT).show();
                        } else {
                            String temp = input.getText().toString();
                            DatastoreAdapter adapter = new DatastoreAdapter(UserActivity.this);
                            adapter.createGroup(temp, MainActivity.getId());

                        }
                    }
                });
                alert.show();
            }
        });
        Button eventCreate = (Button) findViewById(R.id.eventCreate);
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

                Intent i = new Intent(getApplicationContext(), EventCreate.class);

                startActivity(i);
                //finish();
            }
        });
        DatastoreAdapter adapter = new DatastoreAdapter(this);
        adapter.getUser(userId, userName, userEmail);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        BaseActivity.login=0;
    }

    @Override
    public void signOut() {
        super.signOut();
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View userView = inflater.inflate(R.layout.user, container,false);

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
*/
       @Override
       public void response(Object o) {
           if(!(o instanceof UserBean)) {
               return;
           }
           UserBean user = (UserBean) o;
           List<UserBean.UserGroupBean> groupList = user.getGroups();
           ListView listView = (ListView) findViewById(R.id.user_group_list);
           List<String> list = new ArrayList<String>();
           List<Long> idList = new ArrayList<Long>();
           for(UserBean.UserGroupBean group: groupList) {
               list.add(group.getName());
               idList.add(group.getId());
           }
           MainActivity.setGroupIds(idList);
           ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
           listView.setAdapter(arrayAdapter);
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Long groupId = MainActivity.getGroupIds().get(position);
               }
           });
       }

    public String getName(){
        return userName;
    }
    public String getEmail(){
        return userEmail;
    }
    public String getId(){
        return userId;
    }
    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> list) {
        groupIds = list;
    }
}
