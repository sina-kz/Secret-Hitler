package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.secretHitler.Fragments.Show_Role_Fragment;
import com.example.secretHitler.R;

public class Show_Role extends AppCompatActivity {

    private Show_Role_Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__role);

        fragment = new Show_Role_Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
