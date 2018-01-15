package com.example.suriya.promptnom.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.promptnom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingupFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail, editTextPass;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnSingup;
    private TextView tvBactToLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingDialog;

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
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPass = (EditText) rootView.findViewById(R.id.editTextPass);
        btnSingup = (MaterialFancyButton) rootView.findViewById(R.id.btnSingup);
        tvBactToLogin = (TextView) rootView.findViewById(R.id.tvBacttologin);

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
        }
    }

    private void singupemail() {
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString();

        if (email.isEmpty()){
            editTextEmail.setError("Enter your Email");
            editTextEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()){
            editTextPass.setError("Enter your Password");
            editTextPass.requestFocus();
            return;
        }
        if (pass.length() < 6){
            editTextPass.setError("กรุณาใส่รหัสผ่านให้มากกว่า 6 ตัว");
            editTextPass.requestFocus();
            return;
        }
        loadingDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle("Sing up");
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(true);
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    loadingDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "OK ผ่านได้",
                            Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }else {
                    loadingDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "ON ไม่ผ่าน",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
