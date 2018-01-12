package com.example.suriya.promptnom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.TransitionDetailActivity;
import com.example.suriya.promptnom.adapter.TransitionAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Device;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReDevice;
import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TransitonFragment extends Fragment {

    private DatabaseReference mDataRefDevice, mDataRefItem, mDataRefTransition,
            mDataRefUserTran, mDataRefUser, mRootRef;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView tvNoDev;
    private ListView listItem;
    private TransitionAdapter adapter;
    private ArrayList<ReTransition> transitionsList = new ArrayList<>();
    private ValueEventListener adminValueEventListener;
    private ValueEventListener employeeValueEventListener;

    public TransitonFragment() {
        super();
    }

    public static TransitonFragment newInstance() {
        TransitonFragment fragment = new TransitonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDataRefDevice = mRootRef.child("Device");
        mDataRefItem = mRootRef.child("Item");
        mDataRefTransition = mRootRef.child("Transition");
        mDataRefUserTran = mRootRef.child("User-Transition");
        mDataRefUser = mRootRef.child("Employee");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transition, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        String ruleIDEmp = EmployeeManager.getInstance().getRuleID();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        listItem = (ListView) rootView.findViewById(R.id.listViewTran);

        adminValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transitionsList.clear();
                for (DataSnapshot dataTran : dataSnapshot.getChildren()) {
                    final Transition tran = dataTran.getValue(Transition.class);
                    mDataRefDevice.child(tran.getDeviceID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final ReDevice device = dataSnapshot.getValue(ReDevice.class);
                            mDataRefItem.child(device.getDeviceID()).child(tran.getItemID())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            transitionsList.clear();
                                            final ItemDevice item = dataSnapshot.getValue(ItemDevice.class);
                                            mDataRefUser.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Employee emp = dataSnapshot.getValue(Employee.class);
                                                    ReTransition reTransition = new ReTransition();
                                                    reTransition.setBrand(device.getBrand());
                                                    reTransition.setName(device.getDeviceName());
                                                    reTransition.setNumber(item.getItemNumber());
                                                    reTransition.setStatus(item.getItemStatus());
                                                    reTransition.setUrlDevice(device.getUrlPhotoDevice());
                                                    reTransition.setEmpID(tran.getEmpID());
                                                    reTransition.setEmpName(emp.getEmpName());
                                                    reTransition.setDateLend(tran.getDateLand());
                                                    reTransition.setItemID(item.getItemId());
                                                    reTransition.setTranID(tran.getTranID());
                                                    reTransition.setDeviceID(device.getDeviceID());
                                                    if (tran.isLendState() == true) {
                                                        transitionsList.add(0, reTransition);
                                                    }

                                                    /**if (reTransition.getStatus().equals("Lend")){
                                                     transitionsList.add(reTransition);
                                                     }*/
                                                    adapter = new TransitionAdapter(getActivity(), transitionsList);
                                                    listItem.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        employeeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transitionsList.clear();
                for (DataSnapshot dataTran : dataSnapshot.getChildren()) {
                    final Transition tran = dataTran.getValue(Transition.class);
                    transitionsList.clear();
                    mDataRefDevice.child(tran.getDeviceID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final ReDevice device = dataSnapshot.getValue(ReDevice.class);
                            mDataRefItem.child(device.getDeviceID()).child(tran.getItemID())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            transitionsList.clear();
                                            final ItemDevice item = dataSnapshot.getValue(ItemDevice.class);
                                            mDataRefUser.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Employee emp = dataSnapshot.getValue(Employee.class);
                                                    ReTransition reTransition = new ReTransition();
                                                    reTransition.setBrand(device.getBrand());
                                                    reTransition.setName(device.getDeviceName());
                                                    reTransition.setNumber(item.getItemNumber());
                                                    reTransition.setStatus(item.getItemStatus());
                                                    reTransition.setUrlDevice(device.getUrlPhotoDevice());
                                                    reTransition.setEmpID(tran.getEmpID());
                                                    reTransition.setEmpName(emp.getEmpName());
                                                    reTransition.setDateLend(tran.getDateLand());
                                                    reTransition.setItemID(item.getItemId());
                                                    reTransition.setTranID(tran.getTranID());
                                                    reTransition.setDeviceID(device.getDeviceID());
                                                    if (tran.isLendState() == true) {
                                                        transitionsList.add(0, reTransition);
                                                    }
                                                    /**if (reTransition.getStatus().equals("Lend")){
                                                     transitionsList.add(reTransition);
                                                     }*/
                                                    adapter = new TransitionAdapter(getActivity(), transitionsList);
                                                    listItem.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Get Database
        if (ruleIDEmp.equals("Admin")) {
            mDataRefTransition.addValueEventListener(adminValueEventListener);
            listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), TransitionDetailActivity.class);
                    intent.putExtra("ReTransition", transitionsList.get(i));
                    startActivity(intent);
                }
            });
        } else if (ruleIDEmp.equals("Employee")) {
            mDataRefUserTran.child(userID).addValueEventListener(employeeValueEventListener);
            listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), TransitionDetailActivity.class);
                    intent.putExtra("ReTransition", transitionsList.get(i));
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        String ruleID = EmployeeManager.getInstance().getRuleID();
        if (ruleID.equals("Admin")){
            if (adminValueEventListener != null){
                mRootRef.removeEventListener(adminValueEventListener);
            }
        }else {
            if (employeeValueEventListener != null){
                mRootRef.removeEventListener(employeeValueEventListener);
            }
        }
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }
}
