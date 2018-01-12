package com.example.suriya.promptnom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.fragment.UsetinfoDetailFragment;

public class UserinfoUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_update);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, UsetinfoDetailFragment.newInstance())
                    .commit();
        }
    }
}
