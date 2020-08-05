package com.example.secretHitler.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Adapters.ResultsRecyclerViewAdapter;
import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.Objects;

public class ShowResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        ArrayList<Player> players = GameMethods.getAllPlayers();
        ArrayList<Player> liberals = GameMethods.liberalsTeam(players);
        ArrayList<Player> fascists = GameMethods.fascistsTeam(players);
        boolean liberalsWon = Objects.requireNonNull(getIntent().getExtras()).getBoolean("LIBERAL_WON");
        setFinishOnTouchOutside(liberalsWon);
        GameMethods.setLiberalsWon(liberalsWon);
        Button endGameButton = findViewById(R.id.end_game);
        ArrayList<TextView> liberalsTextViews = new ArrayList<>();
        ArrayList<TextView> fascistsTextViews = new ArrayList<>();
        RecyclerView liberalsRecyclerView = findViewById(R.id.liberals_recyclerview);
        ResultsRecyclerViewAdapter liberalsAdapter = new ResultsRecyclerViewAdapter(this, liberals, liberalsTextViews);
        liberalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        liberalsRecyclerView.setAdapter(liberalsAdapter);
        RecyclerView fascistsRecyclerView = findViewById(R.id.fascists_recyclerview);
        ResultsRecyclerViewAdapter fascistsAdapter = new ResultsRecyclerViewAdapter(this, fascists, fascistsTextViews);
        fascistsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fascistsRecyclerView.setAdapter(fascistsAdapter);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMethods.reinitialize();
                startActivity(new Intent(getApplicationContext(), SetupActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
