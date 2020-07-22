package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import com.example.secretHitler.R;

public class LaunchActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        progressBar = (ProgressBar) findViewById(R.id.progressBarID); // 5 seconds to complete
        handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBar.getProgress() != progressBar.getMax()){
                    try{
                        Thread.sleep(50);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.incrementProgressBy(1);
                        }
                    });
                }
                startActivity(new Intent(getApplicationContext(), SetupActivity.class));
            }
        }).start();
    }
}