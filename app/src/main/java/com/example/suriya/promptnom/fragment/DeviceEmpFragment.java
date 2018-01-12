package com.example.suriya.promptnom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.DeviceDetailEmpActivity;
import com.example.suriya.promptnom.adapter.DeviceAdapter;
import com.example.suriya.promptnom.util.ReDevice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DeviceEmpFragment extends Fragment {

    private ListView listViewDevice;
    private DatabaseReference mDataRefDevice, mDataRefTran;
    private DeviceAdapter adapter;
    private ArrayList<ReDevice> devicesList = new ArrayList<>();

    public DeviceEmpFragment() {
        super();
    }

    public static DeviceEmpFragment newInstance() {
        DeviceEmpFragment fragment = new DeviceEmpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lend_device_emp, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        listViewDevice = (ListView)rootView.findViewById(R.id.listViewDevice);
        mDataRefDevice = FirebaseDatabase.getInstance().getReference("Device");
        mDataRefDevice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                devicesList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ReDevice reDevice = ds.getValue(ReDevice.class);
                    devicesList.add(reDevice);
                    adapter = new DeviceAdapter(getActivity(), devicesList);
                    listViewDevice.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DeviceDetailEmpActivity.class);
                intent.putExtra("ReDevice", devicesList.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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
}
