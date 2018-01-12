package com.example.suriya.promptnom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.Device;
import com.example.suriya.promptnom.util.ReDevice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UpdateDeviceFragment extends Fragment {

    private ImageView imgDevice;
    private Spinner spBrand;
    private EditText editTextMobile, editTextCPU, editTextRam, editTextROM, editTextDisplay;
    private RadioGroup rgOS;
    private RadioButton rbAndroid, rbIOS;
    private MaterialFancyButton btnSave;
    private DatabaseReference mDataRef;

    public UpdateDeviceFragment() {
        super();
    }

    public static UpdateDeviceFragment newInstance() {
        UpdateDeviceFragment fragment = new UpdateDeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDeviceMain");
        mDataRef = FirebaseDatabase.getInstance().getReference("Device").child(reDevice.getDeviceID());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_device, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        imgDevice = (ImageView) rootView.findViewById(R.id.imgAddDevice);
        spBrand = (Spinner) rootView.findViewById(R.id.spBrand);
        editTextMobile = (EditText) rootView.findViewById(R.id.editTextMobile);
        editTextCPU = (EditText) rootView.findViewById(R.id.editTextCpu);
        editTextRam = (EditText) rootView.findViewById(R.id.editTextRam);
        editTextROM = (EditText) rootView.findViewById(R.id.editTextRom);
        editTextDisplay = (EditText) rootView.findViewById(R.id.editTextDisplaySize);
        rgOS = (RadioGroup) rootView.findViewById(R.id.rgOs);
        rbAndroid = (RadioButton) rootView.findViewById(R.id.rbAndroid);
        rbIOS = (RadioButton) rootView.findViewById(R.id.rbIOS);
        btnSave = (MaterialFancyButton) rootView.findViewById(R.id.btnSave);

        // Get!!!
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReDevice reDevice1 = dataSnapshot.getValue(ReDevice.class);
                Glide.with(getActivity())
                        .load(reDevice1.getUrlPhotoDevice())
                        .into(imgDevice);
                switch (reDevice1.getBrand()) {
                    case "Samsung":
                        spBrand.setSelection(0);
                        break;
                    case "ASUS":
                        spBrand.setSelection(1);
                        break;
                    case "OPPO":
                        spBrand.setSelection(2);
                        break;
                    case "Apple":
                        spBrand.setSelection(3);
                        break;
                    case "Huawei":
                        spBrand.setSelection(4);
                        break;
                    case "nubia":
                        spBrand.setSelection(5);
                        break;
                    case "SONY":
                        spBrand.setSelection(6);
                        break;
                }

                editTextMobile.setText(reDevice1.getDeviceName());
                editTextCPU.setText(reDevice1.getDeviceCpu());
                editTextRam.setText(reDevice1.getDeviceRam());
                editTextROM.setText(reDevice1.getDeviceRom());
                editTextDisplay.setText(reDevice1.getDisplaySize());

                if (reDevice1.getDeviceOS().equals("Android")) {
                    rgOS.check(R.id.rbAndroid);
                } else {
                    rgOS.check(R.id.rbIOS);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDevice();
            }
        });


    }

    private void updateDevice() {

        Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDeviceMain");

        String deviceBrand = spBrand.getSelectedItem().toString();
        String deviceName = editTextMobile.getText().toString();
        String deviceCpu = editTextCPU.getText().toString();
        String deviceRam = editTextRam.getText().toString();
        String deviceRom = editTextROM.getText().toString();
        String displaySize = editTextDisplay.getText().toString();
        String deviceOS = null;

        switch (rgOS.getCheckedRadioButtonId()) {
            case R.id.rbAndroid:
                deviceOS = "Android";
                break;
            case R.id.rbIOS:
                deviceOS = "IOS";
                break;
        }

        if (deviceName.isEmpty()) {
            editTextMobile.setError("Pleas enter your device name.");
            editTextMobile.requestFocus();
            return;
        }
        if (deviceCpu.isEmpty()) {
            editTextCPU.setError("Pleas enter your device CPU.");
            editTextCPU.requestFocus();
            return;
        }
        if (deviceRam.isEmpty()) {
            editTextRam.setError("Pleas enter your device RAM.");
            editTextRam.requestFocus();
            return;
        }
        if (deviceRom.isEmpty()) {
            editTextROM.setError("Pleas enter your device ROM.");
            editTextROM.requestFocus();
            return;
        }
        if (displaySize.isEmpty()) {
            editTextDisplay.setError("Pleas enter your display size.");
            editTextDisplay.requestFocus();
            return;
        }
        if (deviceBrand.equals("Apple") && deviceOS.equals("Android")) {
            rbAndroid.setError("Android is not Apple.");
            Toast.makeText(getActivity().getApplicationContext(), "Android is not Apple.",
                    Toast.LENGTH_LONG).show();
            rbAndroid.requestFocus();
            return;
        }
        if (deviceOS.equals("IOS") && !deviceBrand.equals("Apple")) {
            rbIOS.setError("IOS is Apple's only.");
            Toast.makeText(getActivity().getApplicationContext(), "IOS is Apple's only.",
                    Toast.LENGTH_LONG).show();
            rbIOS.requestFocus();
            return;
        }

        Device device = new Device(reDevice.getDeviceID(), reDevice.getUrlPhotoDevice(),
                deviceBrand, deviceName, deviceCpu,deviceRam,deviceRom,displaySize,deviceOS, false);
        mDataRef.setValue(device);
        Toast.makeText(getActivity(), "Update", Toast.LENGTH_SHORT).show();
        backToDeviceDetail(reDevice);
    }

    private void backToDeviceDetail(ReDevice reDevice) {

        Intent intent = new Intent();
        intent.putExtra("ReDevice", reDevice);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
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
