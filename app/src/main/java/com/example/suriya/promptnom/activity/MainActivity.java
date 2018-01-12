package com.example.suriya.promptnom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.fragment.AllTransitionFragment;
import com.example.suriya.promptnom.fragment.DeviceFragment;
import com.example.suriya.promptnom.fragment.TransitonFragment;
import com.example.suriya.promptnom.adapter.ViewPagerAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.service.CountItem;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MAIN = 92;
    private DrawerLayout drawerLayout;
    private ImageView imgProfile;
    private TextView tvDisplayName, tvDisplayEmail;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("Employee");
        initInstace();
        loadUserProfile();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName == null) {
                Toast.makeText(this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, UserInfoActivity.class);
                startActivityForResult(intent, REQUEST_MAIN);
                //startActivity(new Intent(this, UserInfoActivity.class));
            } else {
                Toast.makeText(this, "Welcome " + displayName, Toast.LENGTH_LONG).show();
            }

        }
        Intent service = new Intent(MainActivity.this, CountItem.class);
        startService(service);
    }

    private void initInstace() {

        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, R.string.open_menu, R.string.close_menu);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navLogout) {
                    logoutFirebase();
                    return true;
                }
                if (id == R.id.navUpdatePro) {
                    Intent intent = new Intent(MainActivity.this, UserinfoUpdateActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
                return false;
            }
        });
        imgProfile = (ImageView) navView.getHeaderView(0)
                .findViewById(R.id.imgProfile);
        tvDisplayName = (TextView) navView.getHeaderView(0)
                .findViewById(R.id.tvDisplayName);
        tvDisplayEmail = (TextView) navView.getHeaderView(0)
                .findViewById(R.id.tvDisplayEmail);

    }

    private void loadUserProfile() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgProfile);
            }
            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                tvDisplayName.setText(displayName);
            }
            if (user.getEmail() != null) {
                String displayEmail = user.getEmail();
                tvDisplayEmail.setText(displayEmail);
            }
        }
    }

    private void logoutFirebase() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginandSignupActivity.class));
                finish();
                Toast.makeText(MainActivity.this, "Log out", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setupViewPager(final ViewPager viewPager) {

        String ruleID = EmployeeManager.getInstance().getRuleID();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (ruleID.equals("Admin")) {
            adapter.addFragment(DeviceFragment.newInstance(), "อุปกรณ์");
            adapter.addFragment(TransitonFragment.newInstance(), "รายการยืม");
            adapter.addFragment(AllTransitionFragment.newInstance(), "ประวัติการยืมอุปกรณ์");
            viewPager.setAdapter(adapter);
        } else {
            adapter.addFragment(DeviceFragment.newInstance(), "ยืมอุปกรณ์");
            adapter.addFragment(TransitonFragment.newInstance(), "รายการยืม");
            adapter.addFragment(AllTransitionFragment.newInstance(), "ประวัติการยืมอุปกรณ์");
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAIN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uName = data.getStringExtra("uName");
                String urlImg = user.getPhotoUrl().toString();
                String disName = user.getDisplayName();
                if (user != null) {
                    if (user.getPhotoUrl() != null) {
                        Glide.with(this).load(urlImg)
                                .apply(RequestOptions.circleCropTransform())
                                .into(imgProfile);
                    }
                    if (disName!=null){
                        tvDisplayName.setText(disName);
                    }
                    if (user.getEmail() != null) {
                        String displayEmail = user.getEmail();
                        tvDisplayEmail.setText(displayEmail);
                    }
                    viewPager = (ViewPager) findViewById(R.id.viewpager);
                    setupViewPager(viewPager);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(MainActivity.this, CountItem.class);
        startService(service);
        String userID = mAuth.getCurrentUser().getUid();
        
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}