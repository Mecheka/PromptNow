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

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.ConnectUserActivity;
import com.example.suriya.promptnom.activity.TransitionDetailActivity;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReDevice;
import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TransitionDetailFragment extends Fragment {

    private ImageView imgDevice;
    private TextView tvBrand, tvName, tvNumber, tvEmpName, tvDateLend, tvDateReturn, tvStatus;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnReturn, btnLend, btnConnUser;
    private DatabaseReference mDataRefEmp, mDataRefItem, mDataRefTran, mDataRefUserTran,
            mDataRefDevice;
    private FirebaseAuth mAuth;
    private static Transition trasition = null;
    private ArrayList<Transition> lendList = new ArrayList<>();
    private boolean refDataTranDone = false;

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
        mDataRefDevice = FirebaseDatabase.getInstance().getReference("Device");
        mAuth = FirebaseAuth.getInstance();
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
        final Intent intent = getActivity().getIntent();
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
        btnLend = (MaterialFancyButton) rootView.findViewById(R.id.btnLend);
        btnConnUser = (MaterialFancyButton) rootView.findViewById(R.id.btnConnUser);

        // Get!!!

        // Set!!!
        Glide.with(getActivity()).load(tran.getUrlDevice()).into(imgDevice);
        tvBrand.setText(tran.getBrand());
        tvName.setText(tran.getName());
        tvNumber.setText(getResources().getString(R.string.number) + " " + tran.getNumber());
        mDataRefEmp.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                tvEmpName.setText(getResources().getString(R.string.tran_emp_name) + " " + emp.getEmpName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tvDateLend.setText(getResources().getString(R.string.datelend) + " " + tran.getDateLend());

        mDataRefUserTran.child(tran.getEmpID()).child(tran.getTranID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = getActivity();
                if (activity != null) {
                    Transition trans = dataSnapshot.getValue(Transition.class);
                    if (trans != null) {

                        if (trans.isLendState() == false) {
                            tvDateReturn.setText(getResources().getString(R.string.datereturn) + " "
                                    + trans.getDateReturn());
                            tvDateReturn.setVisibility(View.VISIBLE);
                            tvStatus.setTextColor(getResources().getColor(R.color.donestate));
                            tvStatus.setText(getResources().getString(R.string.is_done));
                            tvStatus.setVisibility(View.VISIBLE);
                            btnReturn.setVisibility(View.GONE);
                            if (ruleID.equals("Employee")) {
                                btnLend.setVisibility(View.VISIBLE);
                                tvStatus.setVisibility(View.GONE);
                            }else {
                                btnConnUser.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvStatus.setTextColor(getResources().getColor(R.color.lendstate));
                            tvStatus.setText(getResources().getString(R.string.is_lend));
                            tvStatus.setVisibility(View.VISIBLE);
                            if (ruleID.equals("Employee")) {
                                btnReturn.setVisibility(View.VISIBLE);
                                tvStatus.setVisibility(View.GONE);
                            }else {
                                btnConnUser.setVisibility(View.VISIBLE);
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

        btnLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                builder.setMessage("คุณต้องการยืมอุปกรณ์อีกครั้งหรือไม่").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lendDeviceAgain(tran.getItemID(), tran.isLendState());
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

        btnConnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataRefEmp.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        intentUser(employee);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void lendDeviceAgain(final String itemID, final boolean lendState) {

        Intent intent = getActivity().getIntent();
        final ReTransition tran = intent.getParcelableExtra("ReTransition");

        refDataTranDone = false;

        mDataRefTran.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lendList.clear();
                for (DataSnapshot dataTran : dataSnapshot.getChildren()) {
                    Transition transition = dataTran.getValue(Transition.class);
                    if (transition.getItemID().equals(tran.getItemID()) && transition.isLendState() == true) {
                        trasition = transition;
                        lendList.add(transition);
                        refDataTranDone = true;
                    }
                }
                nextstep(tran);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void nextstep(ReTransition tran) {
        if (refDataTranDone) {
            if (trasition != null) {
                if (getActivity() != null) {
                    Log.d("True transition kyo", "true");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                            R.style.AppCompatAlertDialogStyle);
                    builder.setMessage("อุปกรณ์นี้ได้ถูกผู้ยืมไปแล้วต้องการติดต่อผู้ยืมหรือไม่").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    connectUser(lendList.get(0));
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    trasition = null;
                }
            }
        } else {
            Log.d("False transition kyo", "false");
            lendDevice(tran.getDeviceID(), tran.getItemID(), tran.getNumber());
        }

    }

    private void lendDevice(String deviceID, String itemID, String number) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FirebaseUser user = mAuth.getCurrentUser();
        mDataRefItem
                .child(deviceID)
                .child(itemID);
        String tranID = mDataRefTran.push().getKey();
        String empID = user.getUid();
        String dateLand = dateFormat.format(calendar.getTime());

        Transition tran = new Transition(tranID, itemID, deviceID, empID, dateLand, true);

        ItemDevice itemDevice = new ItemDevice(itemID, number, "Lend");
        mDataRefTran.child(tranID).setValue(tran);
        mDataRefUserTran.child(empID).child(tranID).setValue(tran);
        mDataRefItem.child(deviceID).child(itemID).setValue(itemDevice);
        Toast.makeText(Contextor.getInstance().getContext().getApplicationContext(), "ยืมแล้ว ", Toast.LENGTH_SHORT).show();

    }

    private void connectUser(Transition transition) {

        mDataRefEmp.child(transition.getEmpID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Employee employee = dataSnapshot.getValue(Employee.class);
                intentUser(employee);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void intentUser(Employee employee) {
        Intent connnectUser = new Intent(getActivity(), ConnectUserActivity.class);
        connnectUser.putExtra("Employee", employee);
        startActivity(connnectUser);
    }

    private void returnDevice(String tranID, String itemID, String empID, String deviceID, String dateLend, String number) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        lendList.clear();
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
