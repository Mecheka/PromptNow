package com.example.suriya.promptnom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.promptnom.activity.MainActivity;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail, editTextPass;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnLogin;
    private TextView tvSingup;
    private ProgressDialog loadingDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataRefUser;

    public LoginFragment() {
        super();
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDataRefUser = FirebaseDatabase.getInstance().getReference("Employee");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPass = (EditText) rootView.findViewById(R.id.editTextPass);
        btnLogin = (MaterialFancyButton) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvSingup = (TextView) rootView.findViewById(R.id.tvSingup);
        tvSingup.setOnClickListener(this);
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
            case R.id.btnLogin:
                loginEmail();
                break;
            case R.id.tvSingup:
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.from_right, R.anim.to_left,
                                R.anim.from_left, R.anim.to_right)
                        .replace(R.id.contentContainer, SingupFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    private void loginEmail() {

        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Enter your Email");
            editTextEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            editTextPass.setError("Enter your Password");
            editTextPass.requestFocus();
            return;
        }
        loadingDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle("Log in");
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(true);
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            mDataRefUser.child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Employee emp = dataSnapshot.getValue(Employee.class);
                                    EmployeeManager.getInstance().setRuleID(emp.getRuleID());
                                    Log.d("RuleID ", emp.getRuleID());
                                    Intent intent = new Intent(getActivity().getApplicationContext(),
                                            MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });

    }
}
