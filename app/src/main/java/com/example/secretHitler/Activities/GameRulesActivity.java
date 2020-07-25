package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.secretHitler.R;

public class GameRulesActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rules);

        textView = (TextView) findViewById(R.id.rulesTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}