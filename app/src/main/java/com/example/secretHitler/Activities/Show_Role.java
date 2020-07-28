package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Show_Role extends AppCompatActivity {

    private TextView textView;
    private AppCompatImageView next, previous;
    private Fragment fragment;
    private int index;
    List<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__role);

        textView = (TextView) findViewById(R.id.role_name);
        next = (AppCompatImageView) findViewById(R.id.next);
        previous = (AppCompatImageView) findViewById(R.id.previous);
        index = 0;

        final Intent intent = getIntent();
        players = intent.getParcelableArrayListExtra("Players");
        final Intent boardGameIntent = new Intent(getBaseContext(), BoardGameActivity.class);
        checkTeam(players);

        textView.setText(players.get(index).getName());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left
                        , R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

        previous.setVisibility(View.INVISIBLE);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != 0) {
                    index--;
                    checkTeam(players);
                    textView.setText(players.get(index).getName());
                } else {
                    previous.setVisibility(View.INVISIBLE);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != players.size() - 1) {
                    index++;
                    checkTeam(players);
                    textView.setText(players.get(index).getName());
                    previous.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("my shared", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(players);
                    editor.putString("list", json);
                    editor.apply();
                    startActivity(boardGameIntent);
                }
            }
        });

    }

    protected void checkTeam(List<Player> players) {
        if (players.get(index).getTeam().equals(Team.LIBERAL)) {
            fragment = new Show_Liberal_Fragment();
        } else {
            if (players.get(index).isHitler()) {
                fragment = new Show_Hitler_Fragment();
            } else {
                fragment = new Show_Fascist_Fragment();
            }
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left
                        , R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
