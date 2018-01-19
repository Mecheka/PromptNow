package com.example.suriya.promptnom.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TransitionDetailFragment extends Fragment {

    private ImageView imgDevice;
    private TextView tvBrand, tvName, tvNumber, tvEmpName, tvDateLend, tvDateReturn, tvStatus;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnReturn;
    private DatabaseReference mDataRefEmp, mDataRefItem, mDataRefTran, mDataRefUserTran;

    public TransitionDetailFragment() {
        super();
    }

    public static TransitionDetailFragment newInstance() {
        TransitionDetailFragment fragment = new TransitionDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRefEmp = FirebaseDatabase.getInstance().getReference("Employee");
        mDataRefItem = FirebaseDatabase.getInstance().getReference("Item");
        mDataRefTran = FirebaseDatabase.getInstance().getReference("Transition");
        mDataRefUserTran = FirebaseDatabase.getInstance().getReference("User-Transition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transition_detail, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        final String ruleID = EmployeeManager.getInstance().getRuleID();
        Intent intent = getActivity().getIntent();
        final ReTransition tran = intent.getParcelableExtra("ReTransition");
        Log.d("DeviceID ", tran.getDeviceID());
        Log.d("ItemID ", tran.getItemID());
        imgDevice = (ImageView) rootView.findViewById(R.id.imgDevice);
        tvBrand = (TextView) rootView.findViewById(R.id.tvBrand);
        tvName = (TextView) rootView.findViewById(R.id.tvBrandName);
        tvNumber = (TextView) rootView.findViewById(R.id.tvNumber);
        tvEmpName = (TextView) rootView.findViewById(R.id.tvEmpName);
        tvDateLend = (TextView) rootView.findViewById(R.id.tvDateLend);
        tvDateReturn = (TextView) rootView.findViewById(R.id.tvDateReturn);
        tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
        btnReturn = (MaterialFancyButton) rootView.findViewById(R.id.btnReturn);

        // Get!!!

        // Set!!!
        Glide.with(getActivity()).load(tran.getUrlDevice()).into(imgDevice);
        tvBrand.setText(tran.getBrand());
        tvName.setText(tran.getName());
        tvNumber.setText("Serail Number " + tran.getNumber());
        mDataRefEmp.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                tvEmpName.setText("ผู้ยืม " + emp.getEmpName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tvDateLend.setText("Date Lend " + tran.getDateLend());

        /**mDataRefTran.child(tran.getTranID()).addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Transition transition = dataSnapshot.getValue(Transition.class);
        if (transition.isLendState() == false) {
        btnReturn.setVisibility(View.GONE);
        tvStatus.setText(tran.getStatus());
        } else {
        tvStatus.setText(tran.getStatus());
        tvStatus.setTextColor(getResources().getColor(R.color.lendstate));
        }
        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
        });*/

        mDataRefUserTran.child(tran.getEmpID()).child(tran.getTranID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = getActivity();
                if (activity != null) {
                    Transition trans = dataSnapshot.getValue(Transition.class);
                    if (trans != null) {
                        if (trans.isLendState() == false) {
                            tvDateReturn.setText("Date Return " + trans.getDateReturn());
                            tvDateReturn.setVisibility(View.VISIBLE);
                            tvStatus.setTextColor(getResources().getColor(R.color.donestate));
                            tvStatus.setText("Done");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnReturn.setVisibility(View.GONE);
                        } else {
                            tvStatus.setTextColor(getResources().getColor(R.color.lendstate));
                            tvStatus.setText("Lend");
                            tvStatus.setVisibility(View.VISIBLE);
                            if (ruleID.equals("Employee")) {
                                btnReturn.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                builder.setMessage("คุณต้องการคืนอุปกรณ์หรือไม่").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                returnDevice(tran.getTranID(), tran.getItemID(), tran.getEmpID(),
                                        tran.getDeviceID(), tran.getDateLend(), tran.getNumber());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void returnDevice(String tranID, String itemID, String empID, String deviceID, String dateLend, String number) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateReturn = dateFormat.format(calendar.getTime());

        Transition tran = new Transition(tranID, itemID, deviceID, empID, dateLend, dateReturn, false);
        ItemDevice item = new ItemDevice(itemID, number, "Done");

        mDataRefItem.child(deviceID).child(itemID).setValue(item);
        mDataRefTran.child(tranID).setValue(tran);
        mDataRefUserTran.child(empID).child(tranID).setValue(tran);
        Toast.makeText(getActivity(), "คืนเรียบร้อย", Toast.LENGTH_SHORT).show();
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
