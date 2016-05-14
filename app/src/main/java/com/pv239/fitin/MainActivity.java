package com.pv239.fitin;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.pv239.fitin.utils.Provider;
import com.pv239.fitin.Entities.User;
import com.pv239.fitin.home.HomeFragment;
import com.pv239.fitin.login.LoginFragment;
import com.pv239.fitin.utils.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private User user;

    private LoginFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment = new LoginFragment();

        setFullScreenDisplay(loginFragment);
    }

    public void initActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.navigation_item_favourites:
                        updateDisplay(new AttachmentFragment());
                        break;
//
//                    case R.id.navigation_item_images:
//                        updateDisplay(new ImageFragment());
//                        break;
//
//                    case R.id.navigation_item_location:
//                        updateDisplay(new MyLocationFragment());
//                        break;
//
//                    case R.id.navigation_sub_item_01:
//                        Toast.makeText(MainActivity.this, "Navigation Item Location Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//
//
//                    case R.id.navigation_sub_item_02:
//                        Toast.makeText(MainActivity.this, "Navigation Item Location Clicked", Toast.LENGTH_SHORT).show();
//                        break;
                }
                return true;
            }
        });

        updateDisplay(new HomeFragment());
    }

    private void updateDisplay(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }


    private void setFullScreenDisplay(Fragment fragment) {
        Log.i(Constants.TAG, "Setting full screen display");
//        findViewById(R.id.navigation_view).setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }

    private void removeFullScreenDisplay(Fragment fragment) {
//        findViewById(R.id.navigation_view).setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getFragmentManager();
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
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
        user.setProfileImage(acct.getPhotoUrl().toString(), (CircleImageView) findViewById(R.id.user_profile_image));
        user.setCoverImage(coverPhotoUrl, (View) findViewById(R.id.user_cover_image));

        initActivity();
        removeFullScreenDisplay(loginFragment);


    }
}
