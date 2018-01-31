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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.suriya.promptnom.Base.Utility;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.fragment.AllTransitionFragment;
import com.example.suriya.promptnom.fragment.DeviceFragment;
import com.example.suriya.promptnom.fragment.TransitonFragment;
import com.example.suriya.promptnom.adapter.ViewPagerAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
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
    private DatabaseReference mDataRefTran;
    private DatabaseReference mDataRefUserTran;
    private DatabaseReference mDataRefUser;
    private List<Transition> tranList = new ArrayList<>();
    private int firstLogin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("Employee");
        mDataRefUserTran = FirebaseDatabase.getInstance().getReference("User-Transition");
        mDataRefTran = FirebaseDatabase.getInstance().getReference("Transition");
        mDataRefUser = FirebaseDatabase.getInstance().getReference("Employee");

        /**if (EmployeeManager.getInstance().getRuleID() == null){
         String userId = mAuth.getCurrentUser().getUid();
         mDataRefUser.child(userId).addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Employee employee = dataSnapshot.getValue(Employee.class);
        EmployeeManager.getInstance().setRuleID(employee.getRuleID());
        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
        });
         }*/

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            finish();
            Intent loginIntent = new Intent(MainActivity.this, LoginandSignupActivity.class);
            startActivity(loginIntent);
        } else {
            String displayName = user.getDisplayName();
            if (displayName == null) {
                Toast.makeText(this, "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(this, UserInfoActivity.class));
            } else {
                Toast.makeText(this, "Welcome " + displayName, Toast.LENGTH_SHORT).show();
            }
            initInstace();
            loadUserProfile();
        }
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
                Toast.makeText(MainActivity.this, "ออกจากระบบ", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setupViewPager(final ViewPager viewPager) {

        String ruleID = EmployeeManager.getInstance().getRuleID();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (ruleID != null) {
            if (ruleID.equals("Admin")) {
                adapter.addFragment(DeviceFragment.newInstance(), getResources().getString(R.string.device));
                adapter.addFragment(TransitonFragment.newInstance(), getResources().getString(R.string.lendandreturn));
                adapter.addFragment(AllTransitionFragment.newInstance(), "ประวัติการยืมอุปกรณ์");
                viewPager.setAdapter(adapter);
            } else {
                adapter.addFragment(DeviceFragment.newInstance(), getResources().getString(R.string.device));
                adapter.addFragment(TransitonFragment.newInstance(), getResources().getString(R.string.lendandreturn));
                adapter.addFragment(AllTransitionFragment.newInstance(), "ประวัติการยืมอุปกรณ์");
                viewPager.setAdapter(adapter);
            }
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
                    if (disName != null) {
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
        String userID = mAuth.getCurrentUser().getUid();
        String ruleId = EmployeeManager.getInstance().getRuleID();
        if (firstLogin == 0) {
            if (ruleId != null) {
                if (ruleId.equals("Employee")) {
                    mDataRefUserTran.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tranList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Transition tran = ds.getValue(Transition.class);
                                if (tran.isLendState() == true) {
                                    tranList.add(tran);
                                }
                            }
                            String countItem = String.valueOf(tranList.size());
                            Toast.makeText(MainActivity.this, "มีอุปกรณ์ที่คุณยืมทั้งหมด " + countItem, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    firstLogin = getIntent().getIntExtra("first", 0);
                } else {
                    mDataRefTran.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tranList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Transition tran = ds.getValue(Transition.class);
                                if (tran.isLendState() == true) {
                                    tranList.add(tran);
                                }
                            }
                            String countItem = String.valueOf(tranList.size());
                            Toast.makeText(MainActivity.this, "มีอุปกนณ์ที่ถูกยืม " + countItem, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    firstLogin = getIntent().getIntExtra("first", 0);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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