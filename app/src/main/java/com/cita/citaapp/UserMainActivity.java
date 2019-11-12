package com.cita.citaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cita.citaapp.app.AppController;
import com.cita.citaapp.ui.child.ChildActivity;
import com.cita.citaapp.ui.child.ChildFragment;
import com.cita.citaapp.ui.home.HomeFragment;
import com.cita.citaapp.ui.nutrient.NutrientFragment;
import com.cita.citaapp.ui.profile.ProfileFragment;
import com.cita.citaapp.utils.CircleTransform;
import com.cita.citaapp.utils.Server;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserMainActivity extends AppCompatActivity {

    private static int userId;
    private static String lvlUser;

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;

    private TextView tvFullName, tvEmailAddress;
    private ImageView imgProfile, imgNavHeaderBackground;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private static final String TAG_JSON_OBJ = "json_obj_req";
    private static final String TAG = UserMainActivity.class.getSimpleName();
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_NUTRIENT = "nutrient";
    private static final String TAG_CHILD = "child";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_GUIDE = "guide";
    private static final String TAF_ABOUT = "about";
    private static final String TAG_POLICY = "policy";
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_EMAIL_ADDRESS = "email_address";

    private static String currentTag = TAG_HOME;

    private String[] activityTitles;

    private Handler mHandler;

    private static String urlProfileImg = Server.URL + "public/images/default.png";
    private static String urlNavHeaderBg = Server.URL + "public/backgrounds/default.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getIntExtra(LoginActivity.TAG_USER_ID, 0);
        lvlUser = getIntent().getStringExtra(LoginActivity.TAG_LVL_USER);

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        activityTitles = getResources().getStringArray(R.array.nav_item_user_activity_titles);

//        sharedPreferences = getSharedPreferences(LoginActivity.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        View headerView = navigationView.getHeaderView(0);
        tvFullName = headerView.findViewById(R.id.tv_full_name);
        tvEmailAddress = headerView.findViewById(R.id.tv_email_address);
        imgNavHeaderBackground = headerView.findViewById(R.id.img_header_bg);
        imgProfile = headerView.findViewById(R.id.img_profile);

        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            currentTag = TAG_HOME;
            loadFragment();
        }

    }

    private void loadNavHeader() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "read_profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvFullName.setText(jsonObject.getString(TAG_FULL_NAME));
                            tvEmailAddress.setText(jsonObject.getString(TAG_EMAIL_ADDRESS));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserMainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(LoginActivity.TAG_LVL_USER, lvlUser);
                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJ, getApplicationContext());

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBackground);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragment() {
        // selecting appropriate nav menu item
//        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(currentTag) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, currentTag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

//        Fragment fragment = getFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment, currentTag);
//        fragmentTransaction.commitAllowingStateLoss();

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable);

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 1:
                // Fragment Profil
                Bundle bundleProfile = new Bundle();
                bundleProfile.putInt(LoginActivity.TAG_USER_ID, userId);
                bundleProfile.putString(LoginActivity.TAG_LVL_USER, lvlUser);
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundleProfile);
                return profileFragment;
            case 2:
                // Fragment Data Kandungan Gizi
                Bundle bundleNutrient = new Bundle();
                bundleNutrient.putInt(LoginActivity.TAG_USER_ID, userId);
                NutrientFragment nutrientFragment = new NutrientFragment();
                nutrientFragment.setArguments(bundleNutrient);
                return nutrientFragment;
            case 3:
                // Fragment Data Anak
                Bundle bundleChild = new Bundle();
                bundleChild.putInt(LoginActivity.TAG_USER_ID, userId);
                bundleChild.putString(LoginActivity.TAG_LVL_USER, lvlUser);
                ChildFragment childFragment = new ChildFragment();
                childFragment.setArguments(bundleChild);
                return childFragment;
            default:
                // Fragment Beranda
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        currentTag = TAG_HOME;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 1;
                        currentTag = TAG_PROFILE;
                        break;
                    case R.id.nav_nutrient:
                        navItemIndex = 2;
                        currentTag = TAG_NUTRIENT;
                        break;
                    case R.id.nav_child:
                        navItemIndex = 3;
                        currentTag = TAG_CHILD;
//                        Intent intent = new Intent(UserMainActivity.this, ChildActivity.class);
//                        intent.putExtra(LoginActivity.TAG_USER_ID, userId);
//                        intent.putExtra(LoginActivity.TAG_LVL_USER, lvlUser);
//                        startActivity(intent);

                        break;
                    case R.id.nav_guide:
                    case R.id.nav_about_us:
                    case R.id.nav_policy:
                        // launch new intent instead of loading fragment
                        Toast.makeText(UserMainActivity.this, "Lagi otw...", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                loadFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        boolean shouldLoadHomeFragOnBackPress = true;

        // checking if user is on other navigation menu
        // rather than home
        if (navItemIndex != 0) {
            navItemIndex = 0;
            currentTag = TAG_HOME;
            loadFragment();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void logout() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(LoginActivity.SESSION_STATUS, false);
//        editor.putString(LoginActivity.TAG_LVL_USER, null);
//        editor.putInt(LoginActivity.TAG_USER_ID, 0);
//        editor.putString(LoginActivity.TAG_FULL_NAME, null);
//        editor.putString(LoginActivity.TAG_EMAIL_ADDRESS, null);
//        editor.apply();

        Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action_settings:
                Toast.makeText(UserMainActivity.this, "Lagi otw...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                logout();
                break;
        }


    }

}
