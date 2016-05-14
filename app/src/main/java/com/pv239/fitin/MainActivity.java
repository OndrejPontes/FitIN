package com.pv239.fitin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pv239.fitin.home.HomeFragment;
import com.pv239.fitin.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private final String TAG = "FITIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//                    case R.id.navigation_item_attachment:
//                        updateDisplay(new AttachmentFragment());
//                        break;
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

        setFullScreenDisplay(new LoginFragment());
    }

    private void updateDisplay(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }


    private void  setFullScreenDisplay(Fragment fragment) {
        Log.i(TAG, "Setting full screen display");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
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
        }

        return super.onOptionsItemSelected(item);
    }
}
