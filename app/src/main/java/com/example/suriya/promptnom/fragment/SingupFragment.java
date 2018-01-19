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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.MainActivity;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.Position;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingupFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 110;
    private CircleImageView imgProfile;
    private MaterialEditText editTextName, editTextLastName, editTextEmail, editTextPass;
    private RadioGroup rgOperator;
    private Spinner spPos;
    private MaterialEditText editTextPhone;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnSingup;
    private TextView tvBactToLogin;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatabaseReference mDataRefUser;
    private DatabaseReference mDataRefPos;
    private ProgressDialog loadingDialog;
    private Uri uriProfile;
    private ArrayList<String> posOptions = new ArrayList<>();

    public SingupFragment() {
        super();
    }

    public static SingupFragment newInstance() {
        SingupFragment fragment = new SingupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("profile");
        mDataRefUser = FirebaseDatabase.getInstance().getReference("Employee");
        mDataRefPos = FirebaseDatabase.getInstance().getReference("Position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_singin, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        imgProfile = (CircleImageView) rootView.findViewById(R.id.icon);
        editTextName = (MaterialEditText) rootView.findViewById(R.id.editTextName);
        editTextLastName = (MaterialEditText) rootView.findViewById(R.id.editTextLastName);
        editTextEmail = (MaterialEditText) rootView.findViewById(R.id.editTextEmail);
        editTextPass = (MaterialEditText) rootView.findViewById(R.id.editTextPass);
        rgOperator = (RadioGroup) rootView.findViewById(R.id.rgOperator);
        spPos = (Spinner) rootView.findViewById(R.id.spPos);
        editTextPhone = (MaterialEditText) rootView.findViewById(R.id.editTextPhone);
        btnSingup = (MaterialFancyButton) rootView.findViewById(R.id.btnSingup);
        tvBactToLogin = (TextView) rootView.findViewById(R.id.tvBacttologin);

        mDataRefPos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataPos : dataSnapshot.getChildren()){
                    String value = dataPos.getValue(String.class);
                    posOptions.add(value);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contextor.getInstance()
                    .getContext().getApplicationContext(), R.layout.spinner_item, posOptions);
                    spPos.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rgOperator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int buttonID) {
                if (buttonID == R.id.rbEmp) {
                    spPos.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000);
                    spPos.setAnimation(anim);
                } else {
                    Animation anim = new AlphaAnimation(1.0f, 0.0f);
                    anim.setDuration(1000);
                    spPos.startAnimation(anim);
                    spPos.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            spPos.setVisibility(View.GONE);
                        }
                    }, 850);

                }
            }
        });

        imgProfile.setOnClickListener(this);
        btnSingup.setOnClickListener(this);
        tvBactToLogin.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSingup:
                singupemail();
                break;
            case R.id.tvBacttologin:
                getFragmentManager().popBackStack();
                break;
            case R.id.icon:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {

        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(imageIntent.ACTION_GET_CONTENT);
        startActivityForResult(imageIntent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getData() != null) {
                        uriProfile = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                                    uriProfile);
                            imgProfile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private boolean isValidName(String name) {
        return Pattern.compile("^[a-zA-Z]{5,20}$").matcher(name).matches();
    }

    private void singupemail() {
        final String name = editTextName.getText().toString();
        final String lastname = editTextLastName.getText().toString();
        final String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString();
        String rule = null;

        // Swith case Radio Group
        switch (rgOperator.getCheckedRadioButtonId()) {
            case R.id.rbAdmin:
                rule = "Admin";
                break;
            case R.id.rbEmp:
                rule = "Employee";
                break;
        }

        final String pos = spPos.getSelectedItem().toString();
        final String phone = editTextPhone.getText().toString();

        if (name.isEmpty()) {
            editTextName.setError("กรุณาใส่ชื่อของคุณด้วยคะ");
            editTextName.requestFocus();
            return;
        }
        if (isValidName(name) == false) {
            editTextName.setError("ชื่อของคุณจะต้องไม่มีตัวเลขผสมอยู่ด้วยคะ");
            editTextName.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {
            editTextLastName.setError("กรุณาใส่นามสกลุลของคุณด้วยคะ");
            editTextLastName.requestFocus();
            return;
        }
        if (isValidName(lastname) == false) {
            editTextLastName.setError("นามสกุลของคุณจะต้องไม่มีตัวเลขผสมอยู่คะ");
            editTextLastName.requestFocus();
            return;
        }
        if (isValidEmaillId(email) == false) {
            editTextEmail.setError("นี้ไม่ใช่อีเมลกรุฯาใส่อีเมลของคุณคะ");
            editTextEmail.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("กรุณาใส่อีเมลของคุณด้วยค่ะ");
            editTextEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            editTextPass.setError("กรุณาใส่รหัสผ่านของคุณด้วยคะ");
            editTextPass.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            editTextPass.setError("รหัสผ่านอย่างน้อย 6 ตัวคะ");
            editTextPass.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            editTextPhone.setError("กรุณาใส่เบอร์มือถือของคุณค่ะ");
            editTextPhone.requestFocus();
            return;
        }
        if (phone.length() < 10) {
            editTextPhone.setError("เบอร์ของคุณจะต้องเท่ากับ 10 ตัว");
            editTextPhone.requestFocus();
            return;
        }

        loadingDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle("Sing up");
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(true);
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();

        final String finalRule = rule;
        final String finalRule1 = rule;
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String userID = mAuth.getCurrentUser().getUid();
                            StorageReference userProfile = mStorageRef.child(userID + ".jpg");
                            if (uriProfile != null) {
                                userProfile.putFile(uriProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().getDownloadUrl().toString();

                                            FirebaseUser user = mAuth.getCurrentUser();

                                            UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .setPhotoUri(Uri.parse(downloadUrl))
                                                    .build();

                                            user.updateProfile(userProfile);

                                            Employee emp = new Employee(userID, name, lastname, finalRule, pos, phone, email);

                                            EmployeeManager.getInstance().setRuleID(finalRule1);

                                            mDataRefUser.child(userID).setValue(emp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(getActivity().getApplicationContext(), "OK ผ่านได้",
                                                            Toast.LENGTH_SHORT).show();
                                                    sendToMainActivity();
                                                }
                                            });
                                        }
                                    }
                                });
                            }else {
                                imgProfile.setDrawingCacheEnabled(true);
                                imgProfile.buildDrawingCache();
                                Bitmap bitmap = imgProfile.getDrawingCache();
                                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
                                byte[] data = boas.toByteArray();

                                UploadTask uploadTask = userProfile.putBytes(data);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task != null){
                                            String dawnloadUrl = task.getResult().getDownloadUrl().toString();

                                            FirebaseUser user = mAuth.getCurrentUser();

                                            UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .setPhotoUri(Uri.parse(dawnloadUrl))
                                                    .build();

                                            user.updateProfile(userProfile);

                                            Employee emp = new Employee(userID, name, lastname, finalRule, pos, phone, email);

                                            EmployeeManager.getInstance().setRuleID(finalRule1);

                                            mDataRefUser.child(userID).setValue(emp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(getActivity().getApplicationContext(), "OK ผ่านได้",
                                                            Toast.LENGTH_SHORT).show();
                                                    sendToMainActivity();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "ON ไม่ผ่าน",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendToMainActivity() {

        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();

    }
}
