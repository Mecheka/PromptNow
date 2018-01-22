package com.example.suriya.promptnom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.adapter.TransitionAdapter;
import com.example.suriya.promptnom.util.ReTransition;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class LendTransirionFragment extends Fragment {

    private ListView listViewTran;
    private DatabaseReference mDataRoot, mDataItem, mDataTran, mDataUserTran, mDataUser;
    private TransitionAdapter adapter;
    private ArrayList<ReTransition> tramsitionList = new ArrayList<>();

    public LendTransirionFragment() {
        super();
    }

    public static LendTransirionFragment newInstance() {
        LendTransirionFragment fragment = new LendTransirionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lend_transition, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

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
