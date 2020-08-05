package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.secretHitler.Fragments.President_Policy_Fragment;
import com.example.secretHitler.R;


public class PolicyChoose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_choose);

        President_Policy_Fragment presidentPolicyFragment = new President_Policy_Fragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.policy_container, presidentPolicyFragment).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {

    }
}