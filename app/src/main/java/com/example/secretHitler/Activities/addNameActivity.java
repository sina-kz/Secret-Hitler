package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.secretHitler.Adapters.RecyclerViewAdapter;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.List;

public class addNameActivity extends AppCompatActivity {

    List<Player> lstPlayer;
    Button addButton;
    EditText nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
        lstPlayer = new ArrayList<>();
        addButton = findViewById(R.id.addButton);
        nameText = findViewById(R.id.name);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstPlayer);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(myAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player player = new Player();
                player.setName(nameText.getText().toString());
                lstPlayer.add(player);
                myAdapter.notifyItemInserted(lstPlayer.size() - 1);
            }
        });
    }
}