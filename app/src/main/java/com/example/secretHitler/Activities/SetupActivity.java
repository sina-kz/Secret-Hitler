package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    private CheckBox ch5, ch6, ch7, ch8, ch9, ch10;
    private int numOfPlayer = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ch5 = findViewById(R.id.check5);
        ch6 = findViewById(R.id.check6);
        ch7 = findViewById(R.id.check7);
        ch8 = findViewById(R.id.check8);
        ch9 = findViewById(R.id.check9);
        ch10 = findViewById(R.id.check10);

        Button startGameButton = findViewById(R.id.startButton);
        Button gameRuleButton = findViewById(R.id.gameRule);
        final List<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(ch5);
        checkBoxes.add(ch6);
        checkBoxes.add(ch7);
        checkBoxes.add(ch8);
        checkBoxes.add(ch9);
        checkBoxes.add(ch10);


        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch5.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch5.setChecked(true);
                    numOfPlayer = 5;
                } else {
                    ch5.setChecked(false);
                    numOfPlayer = -1;

                }
            }
        });
        ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch6.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch6.setChecked(true);
                    numOfPlayer = 6;
                } else {
                    ch6.setChecked(false);
                    numOfPlayer = -1;
                }
            }
        });
        ch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch7.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch7.setChecked(true);
                    numOfPlayer = 7;
                } else {
                    ch7.setChecked(false);
                    numOfPlayer = -1;
                }
            }
        });
        ch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch8.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch8.setChecked(true);
                    numOfPlayer = 8;
                } else {
                    ch8.setChecked(false);
                    numOfPlayer = -1;
                }
            }
        });
        ch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch9.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch9.setChecked(true);
                    numOfPlayer = 9;
                } else {
                    ch9.setChecked(false);
                    numOfPlayer = -1;
                }
            }
        });
        ch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ch10.isChecked()) {
                    for (CheckBox checkBox : checkBoxes) {
                        checkBox.setChecked(false);
                    }
                    ch10.setChecked(true);
                    numOfPlayer = 10;
                } else {
                    ch10.setChecked(false);
                    numOfPlayer = -1;
                }
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numOfPlayer != -1) {
                    Intent intent = new Intent(getBaseContext(), addNameActivity.class);
                    intent.putExtra("NUM_OF_PLAYER", numOfPlayer);
                    startActivity(intent);
                } else {
                    Toast.makeText(SetupActivity.this, "لطفا تعداد بازیکنان را انتخاب کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gameRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ScrollingRulesActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}
