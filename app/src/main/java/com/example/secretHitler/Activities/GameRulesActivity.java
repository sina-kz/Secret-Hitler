package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.secretHitler.Fragments.RulesPageFragment1;
import com.example.secretHitler.Fragments.RulesPageFragment2;
import com.example.secretHitler.Fragments.RulesPageFragment3;
import com.example.secretHitler.Fragments.RulesPageFragment4;
import com.example.secretHitler.Fragments.RulesPageFragment5;
import com.example.secretHitler.Fragments.RulesPageFragment6;
import com.example.secretHitler.R;

public class GameRulesActivity extends AppCompatActivity {
    private AppCompatImageView next, previous;
    private TextView pageNumber;
    private Fragment fragment;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

        next = (AppCompatImageView) findViewById(R.id.rules_next);
        previous = (AppCompatImageView) findViewById(R.id.rules_previous);
        pageNumber = (TextView) findViewById(R.id.rules_pageNumber);
        index = 1;

        checkPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.rules_container, fragment)
                .addToBackStack(null).commit();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != 1){
                    index--;
                    checkPage();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.rules_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != 6){
                    index++;
                    checkPage();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                            .replace(R.id.rules_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
                else{
                    finish();
                }
            }
        });

    }

    protected void checkPage(){
        switch (index){
            case 1:
                fragment = new RulesPageFragment1();
                previous.setVisibility(View.INVISIBLE);
                break;
            case 2:
                fragment = new RulesPageFragment2();
                previous.setVisibility(View.VISIBLE);
                break;
            case 3:
                fragment = new RulesPageFragment3();
                break;
            case 4:
                fragment = new RulesPageFragment4();
                break;
            case 5:
                fragment = new RulesPageFragment5();
                break;
            case 6:
                fragment = new RulesPageFragment6();
                break;
        }
        pageNumber.setText(Integer.toString(index) + " of 6");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
