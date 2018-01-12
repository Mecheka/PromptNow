package com.example.suriya.promptnom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.Employee;
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
public class UsetinfoDetailFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvName, tvLastName, tvRule, tvPos, tvPhone, tvEmail;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnUpdate;
    private DatabaseReference mDateUser;
    private FirebaseAuth mAuth;

    public UsetinfoDetailFragment() {
        super();
    }

    public static UsetinfoDetailFragment newInstance() {
        UsetinfoDetailFragment fragment = new UsetinfoDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateUser = FirebaseDatabase.getInstance().getReference("Employee");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_usetinfo_detail, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        final FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = getActivity().getIntent();
        String userID = intent.getStringExtra("userID");
        imgProfile = (ImageView)rootView.findViewById(R.id.imgProfile);
        tvName = (TextView)rootView.findViewById(R.id.tvName);
        tvLastName = (TextView)rootView.findViewById(R.id.tvLastName);
        tvRule = (TextView)rootView.findViewById(R.id.tvRule);
        tvPos = (TextView)rootView.findViewById(R.id.tvPosition);
        tvPhone = (TextView)rootView.findViewById(R.id.tvPhone);
        tvEmail = (TextView)rootView.findViewById(R.id.tvEmail);
        btnUpdate = (MaterialFancyButton)rootView.findViewById(R.id.btnUpdate);

        // Get & Set
        loadProfile(user, userID);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.contentContainer,
                        UpdateUserInfoFragment.newInstance()).addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadProfile(final FirebaseUser user, String userID) {
        mDateUser.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                Glide.with(getActivity()).load(user.getPhotoUrl().toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgProfile);
                tvName.setText(emp.getEmpName());
                tvLastName.setText(emp.getEmpLastname());
                tvRule.setText(getString(R.string.rule)+" "+emp.getRuleID());
                tvPos.setText(getString(R.string.pos)+" "+emp.getPosID());
                tvPhone.setText(getString(R.string.phone)+" "+emp.getPhone());
                tvEmail.setText(getString(R.string.email)+" "+emp.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        loadProfile(user, userID);
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
