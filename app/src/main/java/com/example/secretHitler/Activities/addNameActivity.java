package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.secretHitler.Adapters.RecyclerViewAdapter;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.List;

public class addNameActivity extends AppCompatActivity {

    List<Player> lstPlayer;
    Button addButton, start_showing_role;
    EditText nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
        lstPlayer = new ArrayList<>();
        addButton = findViewById(R.id.addButton);
        nameText = findViewById(R.id.name);
        start_showing_role = findViewById(R.id.start_showing_role);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstPlayer);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(myAdapter);


        final int numberOfPlayers = getIntent().getIntExtra("NUM_OF_PLAYER", 10);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                for (Player player : lstPlayer) {
                    if (player.getName().equals(nameText.getText().toString())) {
                        Toast.makeText(addNameActivity.this, "نام بازیکن قبلا وارد شده است", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }
                if (flag) {
                    Player player = new Player();
                    player.setName(nameText.getText().toString());
                    lstPlayer.add(player);
                    myAdapter.notifyItemInserted(lstPlayer.size() - 1);
                    if (numberOfPlayers == lstPlayer.size()) {
                        addButton.setEnabled(false);
                    }
                }
            }
        });
        start_showing_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 7/23/20 
            }
        });

    }
}