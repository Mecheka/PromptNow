package com.example.suriya.promptnom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.MainActivity;
import com.example.suriya.promptnom.util.Device;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class AddDeviceFragment extends Fragment {

    private static final int PICK_IMAGE_REQUST = 101;
    private ImageView imgDevice;
    private Spinner spBrand;
    private EditText editTextMobile, editTextCPU, editTextRam, editTextROM, editTextDisplay;
    private RadioGroup rgOS;
    private RadioButton rbAndroid, rbIOS;
    private MaterialFancyButton btnSave;
    private Uri uriDevice;
    private DatabaseReference mData;
    private String deviceImageUrl;
    private ProgressDialog loadingDialog;

    public AddDeviceFragment() {
        super();
    }

    public static AddDeviceFragment newInstance() {
        AddDeviceFragment fragment = new AddDeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = FirebaseDatabase.getInstance().getReference("Device");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_device, container, false);
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

        // Get


        // Set OnClick
        imgDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosImage();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDevice();
            }
        });
    }

    private void choosImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriDevice = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        uriDevice);
                imgDevice.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDevice() {

        StorageReference deviceStorageRef = FirebaseStorage.getInstance().getReference(
                "device/" + System.currentTimeMillis() + ".jpg");
        Bitmap oldDrawable;

        final String deviceId = mData.push().getKey();
        final String deviceBrand = spBrand.getSelectedItem().toString();
        final String deviceName = editTextMobile.getText().toString();
        final String deviceCpu = editTextCPU.getText().toString();
        final String deviceRam = editTextRam.getText().toString();
        final String deviceRom = editTextROM.getText().toString();
        final String displaySize = editTextDisplay.getText().toString();
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

        if (uriDevice != null) {
            loadingDialog = ProgressDialog.show(getActivity(), "Upload", "Uploading...",
                    false, true);

            final String finalDeviceOS = deviceOS;
            deviceStorageRef.putFile(uriDevice).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingDialog.dismiss();
                    deviceImageUrl = taskSnapshot.getDownloadUrl().toString();
                    Device device = new Device(deviceId, deviceImageUrl, deviceBrand, deviceName,
                            deviceCpu, deviceRam, deviceRom, displaySize, finalDeviceOS, false);
                    mData.child(deviceId).setValue(device);
                    getActivity().finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "Faile " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    loadingDialog.setMessage("Upload " + (int) progress + " %");
                }
            });
        }else {
            Toast.makeText(getActivity(), "No Select Image", Toast.LENGTH_SHORT).show();
        }
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
