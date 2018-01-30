package com.example.suriya.promptnom.fragment;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.ConnectUserActivity;
import com.example.suriya.promptnom.activity.UpdateDeviceActivity;
import com.example.suriya.promptnom.adapter.ItemDeviceAdapter;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Device;
import com.example.suriya.promptnom.util.Employee;
import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.util.ReDevice;
import com.example.suriya.promptnom.util.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class DeviceDetailFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_DEVICE = 504;
    private ImageView imgDevice;
    private TextView tvBrand, tvName, tvCpu, tvRam, tvRom, tvDisplay, tvOs;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnAddItem, btnUpdate, btnDelete;
    private ListView listViewItem;
    private ItemDeviceAdapter adapter;
    private ArrayList<ItemDevice> itemList = new ArrayList<>();
    private FirebaseDatabase mDataRef;
    private DatabaseReference mDataRefTran;
    private DatabaseReference mDataRefItem;
    private DatabaseReference mDataRefUserTran;
    private DatabaseReference mDataRefUser;
    private DatabaseReference mDataRefDevice;
    private FirebaseAuth mAuth;
    private Employee employee = null;

    public DeviceDetailFragment() {
        super();
    }

    public static DeviceDetailFragment newInstance() {
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRef = FirebaseDatabase.getInstance();
        mDataRefTran = FirebaseDatabase.getInstance().getReference("Transition");
        mDataRefItem = FirebaseDatabase.getInstance().getReference("Item");
        mDataRefUserTran = FirebaseDatabase.getInstance().getReference("User-Transition");
        mDataRefDevice = FirebaseDatabase.getInstance().getReference("Device");
        mDataRefUser = FirebaseDatabase.getInstance().getReference("Employee");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_detail, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        final String ruleIDEmp = EmployeeManager.getInstance().getRuleID();
        imgDevice = (ImageView) rootView.findViewById(R.id.imgDevice);
        tvBrand = (TextView) rootView.findViewById(R.id.tvBrand);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvCpu = (TextView) rootView.findViewById(R.id.tvCPU);
        tvRam = (TextView) rootView.findViewById(R.id.tvRam);
        tvRom = (TextView) rootView.findViewById(R.id.tvRom);
        tvDisplay = (TextView) rootView.findViewById(R.id.tvSize);
        tvOs = (TextView) rootView.findViewById(R.id.tvOS);
        btnAddItem = (MaterialFancyButton) rootView.findViewById(R.id.btnAddItem);
        btnUpdate = (MaterialFancyButton) rootView.findViewById(R.id.btnUpdate);
        btnDelete = (MaterialFancyButton) rootView.findViewById(R.id.btnDelete);
        listViewItem = (ListView) rootView.findViewById(R.id.listViewItem);

        // Set!!!
        final Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDevice");

        loadDevice(reDevice);
        if (ruleIDEmp.equals("Employee")) {
            btnAddItem.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
        btnAddItem.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        // Get!!!
        mDataRef.getReference("Item").child(reDevice.getDeviceID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ItemDevice item = ds.getValue(ItemDevice.class);
                            itemList.add(item);
                            adapter = new ItemDeviceAdapter(getActivity(), itemList);
                            listViewItem.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        listViewItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ruleIDEmp.equals("Admin")) {
                    ItemDevice device = itemList.get(i);
                    showupdateItem(device.getItemId(), device.getItemNumber(), device.getItemStatus());
                    return true;
                } else {
                    return false;
                }
            }
        });
        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final ItemDevice itemDevice = itemList.get(position);
                if (ruleIDEmp.equals("Employee")) {
                    if (itemDevice.getItemStatus().equals("Done")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                                R.style.AppCompatAlertDialogStyle);
                        builder.setMessage("คุณต้องการยืมอุปกรณ์หรือไม่").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        if (itemDevice.getItemStatus().equals("Lend")) {
                                            Toast.makeText(getActivity(), "อปกรณ์นี้ได้ถูกยืมไปแล้ว", Toast.LENGTH_SHORT).show();
                                        } else {
                                            lendDevice(itemDevice.getItemId(), itemDevice.getItemNumber());
                                        }
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        showUserTranDialog(itemDevice.getItemId());
                    }
                }else {
                    if (itemDevice.getItemStatus().equals("Lend")) {
                        showUserTranDialog(itemDevice.getItemId());
                    }
                }
            }
        });
    }

    private void showUserTranDialog(final String itemId) {

        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_user_tran, null);
        dBuilder.setView(dialogView);
        dBuilder.setTitle("ชื่อผู้ยืม");

        final TextView tvUserTran = (TextView) dialogView.findViewById(R.id.tvUserTran);
        final TextView tvDateLend = (TextView) dialogView.findViewById(R.id.tvDateLend);
        Button btnCancle = (Button) dialogView.findViewById(R.id.btnCancle);
        Button btnConn = (Button) dialogView.findViewById(R.id.btnConn);

        mDataRefTran.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tranData : dataSnapshot.getChildren()) {
                    final Transition tran = tranData.getValue(Transition.class);
                    if (tran.getItemID().equals(itemId) && tran.isLendState() == true){

                        mDataRefUser.child(tran.getEmpID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Employee emp = dataSnapshot.getValue(Employee.class);
                                employee = emp;
                                tvUserTran.setText("ผู้ยืม : " + emp.getEmpName());
                                tvDateLend.setText("ยืมเมื่อ : " + tran.getDateLand());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final AlertDialog dialog = dBuilder.create();
        dialog.show();

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Intent sendEmp = new Intent(getActivity(), ConnectUserActivity.class);
                sendEmp.putExtra("Employee", employee);
                startActivity(sendEmp);
                dialog.dismiss();
                
            }
        });

    }

    private void loadDevice(ReDevice reDevice) {
        mDataRef.getReference("Device").child(reDevice.getDeviceID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ReDevice reDevice = dataSnapshot.getValue(ReDevice.class);
                        Glide.with(Contextor.getInstance().getContext().getApplicationContext())
                                .load(reDevice.getUrlPhotoDevice().toString())
                                .into(imgDevice);
                        tvBrand.setText(reDevice.getBrand());
                        tvName.setText(reDevice.getDeviceName());
                        tvCpu.setText("CPU " + reDevice.getDeviceCpu());
                        tvRam.setText("Ram " + reDevice.getDeviceRam());
                        tvRom.setText("Rom " + reDevice.getDeviceRom());
                        tvDisplay.setText("Display Size " + reDevice.getDisplaySize());
                        tvOs.setText("OS " + reDevice.getDeviceOS());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void lendDevice(String itemId, String number) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = getActivity().getIntent();
        ReDevice device = intent.getParcelableExtra("ReDevice");
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Item")
                .child(device.getDeviceID())
                .child(itemId);
        String tranID = mDataRefTran.push().getKey();
        String empID = user.getUid();
        String dateLand = dateFormat.format(calendar.getTime());

        Transition tran = new Transition(tranID, itemId, device.getDeviceID(), empID, dateLand, true);

        ItemDevice itemDevice = new ItemDevice(itemId, number, "Lend");
        mDataRefTran.child(tranID).setValue(tran);
        mDataRefUserTran.child(empID).child(tranID).setValue(tran);
        dR.setValue(itemDevice);
        Toast.makeText(getActivity(), "ยืมแล้ว ", Toast.LENGTH_SHORT).show();

    }

    private void showupdateItem(final String itemId, String itemNumber, String itemStatus) {

        AlertDialog.Builder dBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_item, null);
        dBuilder.setView(dialogView);
        dBuilder.setTitle("แก้ไขอุปกรณ์");

        final EditText etNumber = (EditText) dialogView.findViewById(R.id.etNumber);
        final Spinner spStatusItem = (Spinner) dialogView.findViewById(R.id.spStatusItem);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);

        final AlertDialog d = dBuilder.create();
        d.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etNumber.getText().toString();
                String statusItem = spStatusItem.getSelectedItem().toString();
                if (number.isEmpty()) {
                    etNumber.setError("Pleas enter Serialnumber");
                    etNumber.requestFocus();
                    return;
                }
                updateItemDevice(itemId, number, statusItem);
                d.dismiss();
            }
        });
    }

    private boolean updateItemDevice(String itemId, String number, String statusItem) {

        Intent intent = getActivity().getIntent();
        ReDevice device = intent.getParcelableExtra("ReDevice");
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Item")
                .child(device.getDeviceID())
                .child(itemId);

        ItemDevice itemDevice = new ItemDevice(itemId, number, statusItem);
        dR.setValue(itemDevice);
        Toast.makeText(getActivity(), "Update", Toast.LENGTH_SHORT);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddItem:
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.contentContainer, AddItemDeviceFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btnUpdate:
                Intent intent = getActivity().getIntent();
                ReDevice reDevice = intent.getParcelableExtra("ReDevice");
                Intent intentSecond = new Intent(getActivity(), UpdateDeviceActivity.class);
                intentSecond.putExtra("ReDeviceMain", reDevice);
                startActivityForResult(intentSecond, REQUEST_DEVICE);

                break;
            case R.id.btnDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                        R.style.AppCompatAlertDialogStyle);
                builder.setMessage("Are your sure delete device ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteDevice();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    private void deleteDevice() {

        Intent intent = getActivity().getIntent();
        ReDevice reDevice = intent.getParcelableExtra("ReDevice");
        Device device = new Device(reDevice.getDeviceID(), reDevice.getUrlPhotoDevice(), reDevice.getBrand(),
                reDevice.getDeviceName(), reDevice.getDeviceCpu(), reDevice.getDeviceRam(), reDevice.getDeviceRom(),
                reDevice.getDeviceRam(), reDevice.getDeviceOS(), true);
        mDataRefDevice.child(reDevice.getDeviceID()).setValue(device).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Delete Complete", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEVICE) {
            if (resultCode == RESULT_OK) {
                ReDevice reDevice = data.getParcelableExtra("ReDevice");
                loadDevice(reDevice);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listViewItem == null) {

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
