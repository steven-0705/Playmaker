package casuals.filthy.playmaker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import casuals.filthy.playmaker.data.DatastoreAdapter;
import casuals.filthy.playmaker.data.AsyncResponse;
import casuals.filthy.playmaker.data.beans.UserBean;


import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.SignInButton;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.plus.Plus;

import com.google.android.gms.plus.model.people.Person;



public class MainActivity extends BaseActivity implements ActionBar.TabListener, AsyncResponse{
    ActionBar actionbar;
    static ViewPager viewpager;
    FragmentPageAdapter ft;
    static String id;
    static String email;
    static String name;
    DatastoreAdapter test = new DatastoreAdapter(this);
    //call getUser

    private static CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            id = extras.getString("ID");
            email = extras.getString("EMAIL");
            name = extras.getString("DISPLAY_NAME");
            if(id != null && email != null && name != null)
            {
               //test.getUser(id,name, email);
            }
        }

        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.pager);
        ft = new FragmentPageAdapter(getSupportFragmentManager());
        actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowHomeEnabled(false);
        viewpager.setAdapter(ft);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionbar.addTab(actionbar.newTab().setText("My Profile").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setIcon(R.drawable.group_icon).setText("Group").setTabListener(this));
       // actionbar.addTab(actionbar.newTab().setIcon(R.drawable.event_icon).setText("Events").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setIcon(R.drawable.leaderboard_icon).setText("Leader Board").setTabListener(this));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                actionbar.setSelectedNavigationItem(arg0);
                checkforSignOut();
                if(arg0 == 0)
                {
                    test.getUser(id,name, email);
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                checkforSignOut();

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                checkforSignOut();
            }
        });

    }



    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        checkforSignOut();

    }
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewpager.setCurrentItem(tab.getPosition());
        checkforSignOut();

    }
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        checkforSignOut();
    }
    public static Button mSignOut;
    public void checkforSignOut(){
        if(mSignOut == null)
        {
            try{

                mSignOut = (Button) findViewById(R.id.signOut);
                mSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.login=3;
                        mSignOut = null;
                        signOut();
                        Intent i = new Intent(getApplicationContext(), BaseActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            mSignOut=null;
            }
            catch(Exception e)
            {
                return;
            }
        }
    }


    @Override
    public void signOut() {
        super.signOut();
    }

    @Override
    public void response(Object o) {
        //where we call getUser
        UserBean obj = (UserBean) o;

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        BaseActivity.login=0;

    }


    static public String getName(){
        return name;
    }
    static public String getEmail(){
        return email;
    }
    static public String getId(){
        return id;
    }


}









