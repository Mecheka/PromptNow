package com.example.suriya.promptnom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.fragment.ConnectUserFragment;
import com.example.suriya.promptnom.util.Employee;

public class ConnectUserActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_user);

        Employee employee = getIntent().getParcelableExtra("Employee");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, ConnectUserFragment.newInstance(employee))
                    .commit();
        }

        initInstance();
    }

    private void initInstance() {

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
