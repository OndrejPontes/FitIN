package com.pv239.fitin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.favourite.FavouriteFragment;
import com.pv239.fitin.fragments.filter.FilterFragment;
import com.pv239.fitin.fragments.filter.MyFiltersFragment;
import com.pv239.fitin.fragments.gym.GymFragment;
import com.pv239.fitin.fragments.home.HomeFragment;
import com.pv239.fitin.fragments.login.LoginFragment;
import com.pv239.fitin.fragments.login.RegisterFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;
import com.pv239.fitin.utils.services.GymNotificationListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBar actionBar;

    private LoginFragment loginFragment;

    private Firebase ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        final Intent intent = getIntent();


        this.ref = new Firebase(Constants.FIREBASE_REF);

        final MainActivity self = this;

        loginFragment = new LoginFragment();
        loginFragment.setRef(ref);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(final AuthData authData) {
                if(authData == null || isExpired(authData)) {
                    Log.i(Constants.TAG, "Not logged in, listener");
                    setFullScreenDisplay(loginFragment);
                } else {
//                    ref.unauth();
                    Log.i(Constants.TAG, "Logged in, listener");
//                    User user = new User(authData.getProvider());
//                    user.setEmail(authData.getProviderData().get("email").toString());
//                    user.setId(authData.getUid());
//                    user.setName("John Doe");
//                    user.setProfileImageUrl(authData.getProviderData().get("profileImageURL").toString());
//                    DataManager.getInstance().putObject(Constants.USER, user);
//                    if(authData.getProvider().equals("password"))
                        ref.child("users").child(authData.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User u = new User();
                                if(dataSnapshot.exists()) {
                                    u = dataSnapshot.getValue(User.class);
                                }
                                switch (authData.getProvider()) {
                                    case "password":
                                        u.setId(authData.getUid());
                                        u.setProvider(authData.getProvider());
                                        u.setEmail(authData.getProviderData().get("email").toString());
                                        u.setName(dataSnapshot.child("name").getValue().toString());
                                        u.setProfileImageUrl(authData.getProviderData().get("profileImageURL").toString());
                                        break;
                                    case "google":
                                        u.setId(authData.getUid());
                                        u.setProvider(authData.getProvider());
                                        u.setEmail(authData.getProviderData().get("email").toString());
                                        u.setName(authData.getProviderData().get("displayName").toString());
                                        u.setProfileImageUrl(authData.getProviderData().get("profileImageURL").toString());
                                }
//                                if(u == null)
//                                    return;
                                DataManager.getInstance().putObject(Constants.USER, u);
                                startService(new Intent(self, GymNotificationListener.class));
                                updateUser();


                                if(intent.getExtras() != null) {
                                    Object gymIdObject = intent.getExtras().get(Constants.GYM_ID);
                                    if (gymIdObject != null) {
                                        String gymId = (String) gymIdObject;

                                        GymFragment fragment = new GymFragment();
                                        fragment.setId(gymId);
                                        fragment.setRef(ref.child("gyms").child(gymId));

                                        FragmentHelper.replaceFragment(getSupportFragmentManager(), fragment, Constants.GYM_VIEW_TAG);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
//                    else if(authData.getProvider().equals("google")) {

//                    }
                    removeFullScreenDisplay(loginFragment);
                    initActivity();

//                    updateUser();
                }
            }
        });
//        initActivity();
    }

    private void initActivity() {
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
                        getSupportFragmentManager().popBackStack(Constants.HOME_TAG, 0);
                        break;
                    case R.id.navigation_item_favourites:
                        FragmentHelper.replaceFragment(getSupportFragmentManager(), new FavouriteFragment(), Constants.FAVOURITES_TAG);
                        break;
                    case R.id.navigation_item_my_filters:
                        MyFiltersFragment myFiltersFragment = new MyFiltersFragment();
//                        Log.i(Constants.TAG, "MyFiltersFragment");
                        myFiltersFragment.setRef(ref.child("equipments"));
                        FragmentHelper.replaceFragment(getSupportFragmentManager(), myFiltersFragment, Constants.FILTERS_LIST_TAG);
                        break;
                    case R.id.navigation_item_open_filter:
                        FilterFragment filterFragment = new FilterFragment();
                        //reset to load new filter
                        filterFragment.invalidateTempValues();
                        FragmentHelper.replaceFragment(getSupportFragmentManager(), filterFragment, Constants.FILTER_VIEW_TAG);
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
        homeFragment.setRef(ref);

        FragmentHelper.replaceFragment(getSupportFragmentManager(), homeFragment, Constants.HOME_TAG);

        updateUser();
    }

    @Override
    public void onBackStackChanged() {
        Log.i(Constants.TAG, "onBackStackChanged");
        shouldDisplayHomeUp();
    }

    private void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount()>1;
        Log.i(Constants.TAG, "canBack " + canBack + " backStack entry count " + getSupportFragmentManager().getBackStackEntryCount());
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
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
        handleBackPressed();
    }

    private void handleBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
//        navigationView.setCheckedItem(0);
    }

    private void setFullScreenDisplay(Fragment fragment) {
        Log.i(Constants.TAG, "Setting full screen display");
//        findViewById(R.id.navigation_view).setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }

    private void removeFullScreenDisplay(Fragment fragment) {
        findViewById(R.id.navigation_view).setVisibility(View.VISIBLE);

        // Register fragment was opened, need to remove it
        if(getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack(Constants.HOME_TAG, 0);
        }

        FragmentHelper.removeFragment(getSupportFragmentManager(), fragment);

        Fragment fr = FragmentHelper.findFragment(getSupportFragmentManager(), R.id.drawer_layout);
        if(fr != null) {
            FragmentHelper.removeFragment(getSupportFragmentManager(), R.id.drawer_layout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_settings:
//                return true;
            case R.id.action_logout:
                // Pri logout sa vráti úplne na 0 - bez fragmentov v back stacku
                getSupportFragmentManager().popBackStack(Constants.HOME_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    private void updateUser() {
        User user = (User) DataManager.getInstance().getObject(Constants.USER);
        if(user != null) {
            TextView email = (TextView) findViewById(R.id.user_email);
            if (email != null && !email.getText().equals(user.getEmail())) {
                email.setText(user.getEmail());
            }

            TextView name = (TextView) findViewById(R.id.user_name);
            if (name != null && !name.getText().equals(user.getName())) {
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

    public void logOutButtonClicked(View view) {
        getSupportFragmentManager().popBackStack(Constants.HOME_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loginFragment.logout();
    }

}
