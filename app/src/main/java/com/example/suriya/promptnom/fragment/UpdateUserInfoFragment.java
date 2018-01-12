package com.example.suriya.promptnom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ReTransition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UpdateUserInfoFragment extends Fragment {

    private ImageView imgProfile;
    private EditText edName, edLastName, edPhone;
    private TextView tvRule, tvEmail, tvPos;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnSave;
    private DatabaseReference mDataUser;
    private FirebaseAuth mAuth;

    public UpdateUserInfoFragment() {
        super();
    }

    public static UpdateUserInfoFragment newInstance() {
        UpdateUserInfoFragment fragment = new UpdateUserInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataUser = FirebaseDatabase.getInstance().getReference("Employee");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_user, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        final FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = getActivity().getIntent();
        final String userID = intent.getStringExtra("userID");
        imgProfile = (ImageView) rootView.findViewById(R.id.imgProfile);
        edName = (EditText) rootView.findViewById(R.id.editTextName);
        edLastName = (EditText) rootView.findViewById(R.id.editTextLastName);
        tvRule = (TextView) rootView.findViewById(R.id.tvRule);
        tvPos = (TextView) rootView.findViewById(R.id.tvPosition);
        edPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        btnSave = (MaterialFancyButton) rootView.findViewById(R.id.btnSave);

        // Get & Set
        mDataUser.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                Glide.with(getActivity()).load(user.getPhotoUrl().toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgProfile);
                edName.setText(emp.getEmpName());
                edLastName.setText(emp.getEmpLastname());
                tvRule.setText(emp.getRuleID());
                tvPos.setText(emp.getPosID());
                edPhone.setText(emp.getPhone());
                tvEmail.setText(emp.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile(userID);
            }
        });

    }

    private void updateUserProfile(String userID) {

        String name = edName.getText().toString();
        String lastName = edLastName.getText().toString();
        String rule = tvRule.getText().toString();
        String pos = tvPos.getText().toString();
        String phone = edPhone.getText().toString();
        String email = tvEmail.getText().toString();

        Employee emp = new Employee(userID, name, lastName, rule, pos, phone, email);

        mDataUser.child(userID).setValue(emp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Update Complete", Toast.LENGTH_SHORT).show();
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
