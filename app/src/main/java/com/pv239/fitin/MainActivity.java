package com.pv239.fitin;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.fragments.filter.FilterFragment;
import com.pv239.fitin.fragments.filter.MyFiltersFragment;
import com.pv239.fitin.fragments.results.ResultsFragment;
import com.pv239.fitin.utils.Provider;
import com.pv239.fitin.Entities.User;
import com.pv239.fitin.fragments.home.HomeFragment;
import com.pv239.fitin.fragments.login.LoginFragment;
import com.pv239.fitin.utils.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, FragmentManager.OnBackStackChangedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;
    private User user;

    private LoginFragment loginFragment;

    private static final String INIT_TAG = "INIT_TAG";
    private Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        this.ref = new Firebase(Constants.FIREBASE_REF);

//        loginFragment = new LoginFragment();

//        setFullScreenDisplay(loginFragment);
        initActivity();
    }

    public void initActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.isChecked()) {
                    return true;
                }

                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {

                    case R.id.navigation_item_home:
                        getSupportFragmentManager().popBackStack(INIT_TAG, 0);
                        break;
                    case R.id.navigation_item_favourites:
                        updateDisplay(new AttachmentFragment());
                        break;
                    case R.id.navigation_item_my_filters:
                        MyFiltersFragment myFiltersFragment = new MyFiltersFragment();
//                        Log.i(Constants.TAG, "MyFiltersFragment");
                        myFiltersFragment.setRef(ref.child("equipments"));
                        updateDisplay(myFiltersFragment);
                        break;

                    case R.id.navigation_item_open_results:
                        Filter filter = new Filter();
                        filter.setName("Near by");
                        ResultsFragment resultsFragment = new ResultsFragment();
                        resultsFragment.setFilter(filter);
                        updateDisplay(resultsFragment);
                        break;
                    case R.id.navigation_item_open_filter:
                        FilterFragment filterFragment = new FilterFragment();
                        updateDisplay(filterFragment);
                        break;
                }
                return true;
            }
        });


        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        updateDisplay(new HomeFragment(), INIT_TAG);

    }

    @Override
    public void onBackStackChanged() {
        Log.i(Constants.TAG, "onBackStackChanged");
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>1;
        Log.i(Constants.TAG, "canback " + canback + " backstact entry count " + getSupportFragmentManager().getBackStackEntryCount());
        if(canback) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_white_36dp);
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        Log.i(Constants.TAG, "onBackPressed. Entrycount " + getSupportFragmentManager().getBackStackEntryCount());
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>1;
        if(canback) {
            handleBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void handleBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(fragment.getTag()).commit();
    }
    private void updateDisplay(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(tag).commit();
    }


    private void setFullScreenDisplay(Fragment fragment) {
        Log.i(Constants.TAG, "Setting full screen display");
//        findViewById(R.id.navigation_view).setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }

    private void removeFullScreenDisplay(Fragment fragment) {
//        findViewById(R.id.navigation_view).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()>1) {
                    handleBackPressed();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                // Pri logout sa vráti úplne na 0 - bez fragmentov v back stacku
                getSupportFragmentManager().popBackStack(INIT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                loginFragment.logout(user.getProvider(), new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        Log.i(Constants.TAG, "Logged out");
                        setFullScreenDisplay(loginFragment);
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGoogleSignUp(GoogleSignInAccount acct, String coverPhotoUrl) {
        Log.i(Constants.TAG, acct.getDisplayName());

        Log.i(Constants.TAG, "on googleSignUp");

        user = new User(Provider.Google);
        user.setName(acct.getDisplayName(), (TextView) findViewById(R.id.user_name));
        user.setEmail(acct.getEmail(), (TextView) findViewById(R.id.user_email));
        user.setProfileImage(acct.getPhotoUrl().toString(), (CircleImageView) findViewById(R.id.user_profile_image), this);
        user.setCoverImage(coverPhotoUrl, (View) findViewById(R.id.user_cover_image), this);

        initActivity();
        removeFullScreenDisplay(loginFragment);
    }

//    public void setActionBarTitle(String title){
//        actionBar.setTitle(title);
//    }
}
