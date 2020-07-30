package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.secretHitler.Fragments.Chancellor_Policy_Fragment;
import com.example.secretHitler.Fragments.President_Policy_Fragment;
import com.example.secretHitler.R;

public class PolicyChoose extends AppCompatActivity {

    private President_Policy_Fragment presidentPolicyFragment;
    private Chancellor_Policy_Fragment chancellorPolicyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_choose);

        presidentPolicyFragment = new President_Policy_Fragment();
        chancellorPolicyFragment = new Chancellor_Policy_Fragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.policy_container, chancellorPolicyFragment).addToBackStack(null).commit();
    }
}