package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.secretHitler.Adapters.RecyclerViewAdapter;
import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;
import com.example.secretHitler.Utils.Numbers;

import java.util.ArrayList;

public class addNameActivity extends AppCompatActivity {

    ArrayList<Player> lstPlayer;
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
        start_showing_role.setEnabled(false);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstPlayer);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(myAdapter);


        final int numberOfPlayers = getIntent().getIntExtra("NUM_OF_PLAYER", 10);

        start_showing_role.setEnabled(false);
        start_showing_role.setClickable(false);
        start_showing_role.setAlpha((float) 0.5);

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
                if (nameText.getText().toString().equals("")) {
                    Toast.makeText(addNameActivity.this, "نام بازیکن خالی است", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (flag) {
                    Player player = new Player();
                    player.setName(nameText.getText().toString());
                    nameText.setText("");
                    lstPlayer.add(player);
                    myAdapter.notifyItemInserted(lstPlayer.size() - 1);
                    if (numberOfPlayers == lstPlayer.size()) {
                        addButton.setEnabled(false);
                        addButton.setClickable(false);
                        addButton.setAlpha((float) 0.5);

                        start_showing_role.setEnabled(true);
                        start_showing_role.setClickable(true);
                        start_showing_role.setAlpha((float) 1.0);
                    }
                }
            }
        });
        start_showing_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lstPlayer.size() < numberOfPlayers) {
                    Toast.makeText(addNameActivity.this, "تعداد بازیکنان برای تعیین نقش کافی نیست", Toast.LENGTH_SHORT).show();
                } else {
                    GameMethods.initializePolicies();
                    switch (lstPlayer.size()) {
                        case 5:
                            GameMethods.assignTeams(lstPlayer, Numbers.fivePlayersGameNumberOfLiberals, Numbers.fivePlayersGameNumberOfFascists);
                            break;
                        case 6:
                            GameMethods.assignTeams(lstPlayer, Numbers.sixPlayersGameNumberOfLiberals, Numbers.sixPlayersGameNumberOfFascists);
                            break;
                        case 7:
                            GameMethods.assignTeams(lstPlayer, Numbers.sevenPlayersGameNumberOfLiberals, Numbers.sevenPlayersGameNumberOfFascists);
                            break;
                        case 8:
                            GameMethods.assignTeams(lstPlayer, Numbers.eightPlayersGameNumberOfLiberals, Numbers.eightPlayersGameNumberOfFascists);
                            break;
                        case 9:
                            GameMethods.assignTeams(lstPlayer, Numbers.ninePlayersGameNumberOfLiberals, Numbers.ninePlayersGameNumberOfFascists);
                            break;
                        case 10:
                            GameMethods.assignTeams(lstPlayer, Numbers.tenPlayersGameNumberOfLiberals, Numbers.tenPlayersGameNumberOfFascists);
                            break;
                    }
                    Intent intent = new Intent(getBaseContext(), Show_Role.class);
                    intent.putParcelableArrayListExtra("Players", lstPlayer);
                    startActivity(intent);
                }
            }
        });

    }

}