package com.example.suriya.promptnom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suriya.promptnom.activity.AddDeviceActivity;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.DeviceDetailActivity;
import com.example.suriya.promptnom.adapter.DeviceAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ReDevice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DeviceFragment extends Fragment implements View.OnClickListener {

    private ProgressBar progressBar;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnAddDevice;
    private TextView tvSelect;
    private FirebaseAuth mAuth;
    private DatabaseReference mData;
    private ListView listViewDevice;
    private DeviceAdapter adapter;
    private ArrayList<ReDevice> deviceList = new ArrayList<>();

    public DeviceFragment() {
        super();
    }

    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        initInstances(rootView);
        return rootView;
    }


    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        final String ruleIDEmp = EmployeeManager.getInstance().getRuleID();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        tvSelect = (TextView) rootView.findViewById(R.id.tvSelect);
        btnAddDevice = (MaterialFancyButton) rootView.findViewById(R.id.btnAddDevice);
        btnAddDevice.setOnClickListener(this);
        listViewDevice = (ListView) rootView.findViewById(R.id.listViewDevice);
        if (ruleIDEmp != null) {
            if (ruleIDEmp.equals("Admin")) {
                btnAddDevice.setVisibility(View.VISIBLE);
            } else {
                tvSelect.setVisibility(View.VISIBLE);
            }
        }

        mData = FirebaseDatabase.getInstance().getReference("Device");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deviceList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ReDevice d = ds.getValue(ReDevice.class);
                    if (d.isDeleteState() == false) {
                        deviceList.add(d);
                        adapter = new DeviceAdapter(getActivity(), deviceList);
                        listViewDevice.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
                intent.putExtra("ReDevice", deviceList.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAddDevice:
                startActivity(new Intent(getActivity().getApplicationContext(), AddDeviceActivity.class));
                break;
        }

    }
}
