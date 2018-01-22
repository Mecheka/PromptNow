package com.example.suriya.promptnom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.util.Employee;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ConnectUserFragment extends Fragment {

    private Employee employee;
    private CircleImageView imgProfile;
    private TextView tvName, tvLastName, tvPos, tvPhone, tvEmail;

    public ConnectUserFragment() {
        super();
    }

    public static ConnectUserFragment newInstance(Employee employee) {
        ConnectUserFragment fragment = new ConnectUserFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee", employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employee = getArguments().getParcelable("employee");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect_user, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfile);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvLastName = (TextView) rootView.findViewById(R.id.tvLastName);
        tvPos = (TextView) rootView.findViewById(R.id.tvPosition);
        tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);

        // Set !!!
        // Set image
        Glide.with(Contextor.getInstance().getContext().getApplicationContext())
                .load(employee.getImageUrl())
                .into(imgProfile);

        tvName.setText(employee.getEmpName());
        tvLastName.setText(employee.getEmpLastname());
        tvPos.setText(getResources().getString(R.string.pos) + " " + employee.getPosID());
        tvPhone.setText(getResources().getString(R.string.phone) + " " + employee.getPhone());
        tvEmail.setText(getResources().getString(R.string.email) + " " + employee.getEmail());

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
