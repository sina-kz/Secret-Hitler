package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secretHitler.Adapters.RecyclerViewAdapter;
import com.example.secretHitler.Adapters.RecyclerViewAdapterChanceler;
import com.example.secretHitler.Models.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BoardGameActivity extends AppCompatActivity {

    ArrayList<Player> lstPlayer;
    ArrayList<CheckBox> lstCheckBoxes;
    RecyclerView recyclerView;
    TextView showPresidentTextView;
    Button activateButton;
    CheckBox selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_game);

        SharedPreferences sharedPreferences = getSharedPreferences("my shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        lstPlayer = gson.fromJson(json, type);

        lstCheckBoxes = new ArrayList<>();
        for (CheckBox checkBox : lstCheckBoxes) {
            checkBox.setChecked(false);
        }
        GameMethods.initializePresident(lstPlayer);
        recyclerView = findViewById(R.id.recyclerview_chanceler);
        showPresidentTextView = findViewById(R.id.chanceler_text);
        activateButton = findViewById(R.id.activate_button);
        showChancellors();
    }

    @SuppressLint("SetTextI18n")
    public void showChancellors() {
        ArrayList<Player> activePlayers = GameMethods.activePlayers(lstPlayer);
        ArrayList<Player> assignableChancellors = GameMethods.assignableChancellors(activePlayers);
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(this, assignableChancellors, lstCheckBoxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        showPresidentTextView.setText(GameMethods.getCurrentPresident().getName() + "\n" + "چنسلر را انتخاب کن");
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (CheckBox checkBox : lstCheckBoxes) {
                    if (checkBox.isChecked()) {
                        selected = checkBox;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    Toast.makeText(BoardGameActivity.this, "لطفا یک بازیکن را به عنوان چنسلر انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else {
                    // assign the selected Chancellor by using GameMethods.assignChancellorFunction
                    // Show the dialogBox (create a function for that part: handle "ya" and "nein")
                }
            }
        });
    }
}