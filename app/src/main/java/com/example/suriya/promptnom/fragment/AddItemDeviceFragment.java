package com.example.suriya.promptnom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReDevice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class AddItemDeviceFragment extends Fragment {

    private TextView tvBrandName;
    private EditText etNumber;
    private Button btnSave;
    private ProgressDialog upload;
    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;

    public AddItemDeviceFragment() {
        super();
    }

    public static AddItemDeviceFragment newInstance() {
        AddItemDeviceFragment fragment = new AddItemDeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        tvBrandName = (TextView)rootView.findViewById(R.id.tvBrandName);
        etNumber = (EditText)rootView.findViewById(R.id.etNumber);
        btnSave = (Button)rootView.findViewById(R.id.btnSave);

        // Get Intent
        Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDevice");

        // Set!!!
        tvBrandName.setText(reDevice.getBrand()+" "+reDevice.getDeviceName());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNumber();
            }
        });

    }

    private void saveNumber() {

        Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDevice");

        mDataRef = FirebaseDatabase.getInstance().getReference("Item").child(reDevice.getDeviceID());

        String number = etNumber.getText().toString();

        if (number.isEmpty()){
            etNumber.setError("Pleas enter Serailnumber");
            etNumber.requestFocus();
            return;
        }

        String id = mDataRef.push().getKey();
        ItemDevice item = new ItemDevice(id, number, "Done");
        upload = ProgressDialog.show(getActivity(), "Upload", "Upload...", false, true);
        mDataRef.child(id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                upload.dismiss();
                getFragmentManager().popBackStack();
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
