package com.example.suriya.promptnom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.promptnom.activity.LoginandSignupActivity;
import com.example.suriya.promptnom.activity.MainActivity;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UsetInfoFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;
    private CircleImageView imgGallery;
    private EditText edittextName, edittextLastname, edittextPhone;
    private RadioGroup rgRule;
    private Spinner spPos;
    private TextView tvEmail;
    private MaterialFancyButton btnSave;
    private ProgressDialog loadingDialog;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri uriProfile;
    private String profileImageUrl;

    public UsetInfoFragment() {
        super();
    }

    public static UsetInfoFragment newInstance() {
        UsetInfoFragment fragment = new UsetInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("Employee");
        storage = FirebaseStorage.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userinfo, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        imgGallery = (CircleImageView) rootView.findViewById(R.id.imgGellery);
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        edittextName = (EditText) rootView.findViewById(R.id.editTextName);
        edittextLastname = (EditText) rootView.findViewById(R.id.editTextLastName);
        rgRule = (RadioGroup) rootView.findViewById(R.id.rgOperator);
        spPos = (Spinner) rootView.findViewById(R.id.spPos);
        edittextPhone = (EditText) rootView.findViewById(R.id.edittextPhone);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        tvEmail.setText(email);
        btnSave = (MaterialFancyButton) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserinfo();
            }
        });
    }

    private void saveUserinfo() {

        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        String name = edittextName.getText().toString().trim();
        String lastname = edittextLastname.getText().toString().trim();
        String rule = null;
        String pos = spPos.getSelectedItem().toString();
        String phone = edittextPhone.getText().toString().trim();
        String email = user.getEmail();

        switch (rgRule.getCheckedRadioButtonId()) {
            case R.id.rbAdmin:
                rule = "Admin";
                break;
            case R.id.rbEmp:
                rule = "Employee";
                break;
        }

        if (name.isEmpty()) {
            edittextName.setError("Enter your name");
            edittextName.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {
            edittextLastname.setError("Enter your lastname");
            edittextLastname.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            edittextPhone.setError("Enter your phone number");
            edittextPhone.requestFocus();
            return;
        }

        Employee emp = new Employee(id, name, lastname, rule, pos, phone, email);
        mData.child(id).setValue(emp);
        uploadImageFirebase();

    }

    private void backToMain() {

        FirebaseUser user = mAuth.getCurrentUser();
        String uName = user.getDisplayName();
        Intent intent = new Intent();
        intent.putExtra("uName", uName);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriProfile = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        uriProfile);
                imgGallery.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageFirebase() {

        StorageReference proStorageReference = FirebaseStorage.getInstance().getReference("profile/" + System.currentTimeMillis() + ".jpg");

        if (uriProfile != null) {
            loadingDialog = ProgressDialog.show(getActivity(),
                    "Upload", "Loading...",
                    false, true);

            proStorageReference.putFile(uriProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingDialog.dismiss();
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String displayName = edittextName.getText().toString();
                    if (user != null) {
                        UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .setPhotoUri(Uri.parse(profileImageUrl))
                                .build();

                        user.updateProfile(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Contextor.getInstance().getContext().getApplicationContext(),
                                        "Uploaded profile", Toast.LENGTH_LONG).show();
                                backToMain();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingDialog.dismiss();
                    Toast.makeText(Contextor.getInstance().getContext().getApplicationContext(),
                            "Faile" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    loadingDialog.setMessage("Upload" + (int) progress + "%");
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

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
}
