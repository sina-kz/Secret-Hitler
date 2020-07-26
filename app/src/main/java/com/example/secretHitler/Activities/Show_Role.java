package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Fragments.Show_Fascist_Fragment;
import com.example.secretHitler.Fragments.Show_Hitler_Fragment;
import com.example.secretHitler.Fragments.Show_Liberal_Fragment;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.List;

public class Show_Role extends AppCompatActivity {

    private TextView textView;
    private AppCompatImageView  next, previous;
    private Fragment fragment;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__role);

        textView = (TextView) findViewById(R.id.role_name);
        next = (AppCompatImageView) findViewById(R.id.next);
        previous = (AppCompatImageView) findViewById(R.id.previous);
        index = 0;

        Intent intent = getIntent();
        final List<Player> players = intent.getParcelableArrayListExtra("Players");
        checkTeam(players);

        textView.setText(players.get(index).getName());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( index != 0){
                    index--;
                    checkTeam(players);
                }
                textView.setText(players.get(index).getName());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != players.size() - 1){
                    index++;
                    checkTeam(players);
                }
                textView.setText(players.get(index).getName());
            }
        });

    }
    protected void checkTeam(List<Player> players){
        if(players.get(index).getTeam().equals(Team.LIBERAL)){
            fragment = new Show_Liberal_Fragment();
        }
        else{
            if (players.get(index).isHitler()){
                fragment = new Show_Hitler_Fragment();
            }
            else {
                fragment = new Show_Fascist_Fragment();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
