package com.pv239.fitin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.fragments.filter.FilterFragment;
import com.pv239.fitin.fragments.filter.MyFiltersFragment;
import com.pv239.fitin.fragments.login.RegisterFragment;
import com.pv239.fitin.fragments.results.ResultsFragment;
import com.pv239.fitin.utils.DataManager;
import com.pv239.fitin.Entities.User;
import com.pv239.fitin.fragments.home.HomeFragment;
import com.pv239.fitin.fragments.login.LoginFragment;
import com.pv239.fitin.utils.Constants;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements /*LoginFragment.OnLoginListener, */FragmentManager.OnBackStackChangedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;

    private LoginFragment loginFragment;

    private static final String INIT_TAG = "INIT_TAG";
    private Firebase ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        this.ref = new Firebase(Constants.FIREBASE_REF);

        loginFragment = new LoginFragment();
        loginFragment.setRef(ref);

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData == null || isExpired(authData)) {
                    Log.i(Constants.TAG, "Not logged in, listener");
                    setFullScreenDisplay(loginFragment);
                } else {
                    Log.i(Constants.TAG, "Logged in, listener");
                    User user = new User(authData.getProvider());
                    user.setEmail(authData.getProviderData().get("email").toString());
                    user.setId(authData.getUid());
                    user.setName("John Doe");
                    user.setProfileImageUrl(authData.getProviderData().get("profileImageURL").toString());
                    DataManager.getInstance().putObject(Constants.USER, user);
                    removeFullScreenDisplay(loginFragment);
                    initActivity();

//                    updateUser();
                }
            }
        });
//        initActivity();
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
                        Filter filter = new Filter("Near by");
                        ResultsFragment resultsFragment = new ResultsFragment();
                        resultsFragment.setFilter(filter);
                        resultsFragment.setRef(ref);
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

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setRef(ref.child("gyms"));

        updateDisplay(homeFragment, INIT_TAG);

        updateUser();
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
        if(getSupportFragmentManager().findFragmentById(R.id.drawer_layout) != null && getSupportFragmentManager().findFragmentById(R.id.drawer_layout).getClass().equals(RegisterFragment.class)) {
            Log.i(Constants.TAG, "Register fragment back pressed called");
            setFullScreenDisplay(loginFragment);
            return;
        }


        if(mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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

//        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
//        // Register fragment was opened, need to remove it
//        if(getSupportFragmentManager().getBackStackEntryCount() >= 1) {
//            getSupportFragmentManager().popBackStack(INIT_TAG, 0);
//        }
//        backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().remove(fragment).commit();
        Fragment fr = fragmentManager.findFragmentById(R.id.drawer_layout);
        if(fr == null) {
            return;
        }
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.drawer_layout)).commit();
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
                loginFragment.logout();
//                loginFragment.logout(user.getProvider(), new ResultCallback() {
//                    @Override
//                    public void onResult(@NonNull Result result) {
//                        Log.i(Constants.TAG, "Logged out");
//                        setFullScreenDisplay(loginFragment);
//                    }
//                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUser() {
        User user = (User) DataManager.getInstance().getObject(Constants.USER);
        if(user != null) {
            TextView email = (TextView) findViewById(R.id.user_email);
            if (email != null) {
                email.setText(user.getEmail());
            }

            TextView name = (TextView) findViewById(R.id.user_name);
            if (name != null) {
                name.setText(user.getName());
            }

            CircleImageView profilePic = (CircleImageView) findViewById(R.id.user_profile_image);
            if(profilePic != null) {
                Picasso.with(this)
                        .load(user.getProfileImageUrl())
                        .into(profilePic);
            }
        }
    }

//    @Override
//    public void onGoogleSignUp(GoogleSignInAccount acct, String coverPhotoUrl) {
//        Log.i(Constants.TAG, acct.getDisplayName());
//
//        Log.i(Constants.TAG, "on googleSignUp");
//
//        user = new User(Provider.Google);
//        user.setName(acct.getDisplayName(), (TextView) findViewById(R.id.user_name));
//        user.setEmail(acct.getEmail(), (TextView) findViewById(R.id.user_email));
//        user.setProfileImage(acct.getPhotoUrl().toString(), (CircleImageView) findViewById(R.id.user_profile_image), this);
//        user.setCoverImage(coverPhotoUrl, (View) findViewById(R.id.user_cover_image), this);
//
//        initActivity();
//        removeFullScreenDisplay(loginFragment);
//    }

//    public void setActionBarTitle(String title){
//        actionBar.setTitle(title);
//    }

    private boolean isExpired(AuthData authData) {
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }
}
