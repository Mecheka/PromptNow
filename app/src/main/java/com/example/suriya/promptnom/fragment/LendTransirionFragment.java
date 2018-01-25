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

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.TransitionDetailActivity;
import com.example.suriya.promptnom.adapter.TransitionAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Device;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class LendTransirionFragment extends Fragment {

    private ListView listViewTran;
    private DatabaseReference mDataRoot, mDataItem, mDataTran, mDataUserTran, mDataUser, mDataDevice;
    private FirebaseAuth mAuth;
    private TransitionAdapter adapter;
    private ArrayList<ReTransition> transitionList = new ArrayList<>();
    private ArrayList<String> mKeyTransition = new ArrayList<>();
    private ValueEventListener employeeValueEventListener;
    private ValueEventListener adminValueEventListener;

    public LendTransirionFragment() {
        super();
    }

    public static LendTransirionFragment newInstance() {
        LendTransirionFragment fragment = new LendTransirionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRoot = FirebaseDatabase.getInstance().getReference();
        mDataUserTran = mDataRoot.child("User-Transition");
        mDataUser = mDataRoot.child("Employee");
        mDataItem = mDataRoot.child("Item");
        mDataTran = mDataRoot.child("Transition");
        mDataDevice = mDataRoot.child("Device");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lend_transition, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        String ruleId = EmployeeManager.getInstance().getRuleID();
        final String userId = mAuth.getCurrentUser().getUid();
        listViewTran = (ListView) rootView.findViewById(R.id.lendTransition);

        adapter = new TransitionAdapter(getActivity(), transitionList);
        listViewTran.setAdapter(adapter);

        employeeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transitionList.clear();

                for (DataSnapshot dataTran : dataSnapshot.getChildren()) {

                    final Transition transition = dataTran.getValue(Transition.class);

                    if (transition.isLendState() == true) {

                        mDataDevice.child(transition.getDeviceID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final Device device = dataSnapshot.getValue(Device.class);

                                mDataItem.child(device.getDeviceID()).child(transition.getItemID())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                transitionList.clear();

                                                final ItemDevice itemDevice = dataSnapshot.getValue(ItemDevice.class);

                                                mDataUser.child(userId).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        Employee employee = dataSnapshot.getValue(Employee.class);

                                                        ReTransition reTransition = new ReTransition(device.getDeviceID(),
                                                                device.getUrlPhotoDevice(), device.getBrand(), device.getDeviceName(),
                                                                itemDevice.getItemId(), itemDevice.getItemNumber(), itemDevice.getItemStatus(),
                                                                employee.getEmpID(), employee.getEmpName(), transition.getTranID(), transition.getDateLand(), transition.isLendState());
                                                        transitionList.add(0, reTransition);
                                                        adapter.notifyDataSetChanged();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        adminValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                transitionList.clear();

                for (DataSnapshot dataTran : dataSnapshot.getChildren()) {

                    final Transition transition = dataTran.getValue(Transition.class);

                    if (transition.isLendState() == true) {

                        mDataDevice.child(transition.getDeviceID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final Device device = dataSnapshot.getValue(Device.class);

                                mDataItem.child(device.getDeviceID()).child(transition.getItemID())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                transitionList.clear();

                                                final ItemDevice itemDevice = dataSnapshot.getValue(ItemDevice.class);

                                                mDataUser.child(transition.getEmpID()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        Employee employee = dataSnapshot.getValue(Employee.class);

                                                        ReTransition reTransition = new ReTransition(device.getDeviceID(),
                                                                device.getUrlPhotoDevice(), device.getBrand(), device.getDeviceName(),
                                                                itemDevice.getItemId(), itemDevice.getItemNumber(), itemDevice.getItemStatus(),
                                                                employee.getEmpID(), employee.getEmpName(), transition.getTranID(), transition.getDateLand(), transition.isLendState());
                                                        transitionList.add(0, reTransition);
                                                        adapter.notifyDataSetChanged();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (ruleId.equals("Employee")) {

            mDataUserTran.child(userId).addValueEventListener(employeeValueEventListener);

        } else {

            mDataTran.addValueEventListener(adminValueEventListener);

        }

        listViewTran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent tranIntent = new Intent(getActivity(), TransitionDetailActivity.class);
                tranIntent.putExtra("ReTransition", transitionList.get(position));
                startActivity(tranIntent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

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
    public void onStop() {
        super.onStop();
        String ruleId = EmployeeManager.getInstance().getRuleID();
        if (ruleId.equals("Admin")) {
            if (adminValueEventListener != null) {
                mDataRoot.removeEventListener(adminValueEventListener);
            }
        } else {
            if (employeeValueEventListener != null) {
                mDataRoot.removeEventListener(employeeValueEventListener);
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
