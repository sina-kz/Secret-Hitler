package com.example.secretHitler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Dialog mDialog;

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

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_vote);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        final ArrayList<Player> activePlayers = GameMethods.activePlayers(lstPlayer);
        final ArrayList<Player> assignableChancellors = GameMethods.assignableChancellors(activePlayers);
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(this, assignableChancellors, lstCheckBoxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        showPresidentTextView.setText(GameMethods.getCurrentPresident().getName() + "\n" + "چنسلر را انتخاب کن");
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(activePlayers, assignableChancellors);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showDialogBox(ArrayList<Player> activePlayers, ArrayList<Player> assignableChancellors) {
        boolean flag = false;
        int i = 0;
        for (; i < lstCheckBoxes.size(); i++) {
            if (lstCheckBoxes.get(i).isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            Toast.makeText(BoardGameActivity.this, "لطفا یک بازیکن را به عنوان چنسلر انتخاب کنید", Toast.LENGTH_SHORT).show();
        } else {
            Player selectedChancellor = assignableChancellors.get(i);
            selectedChancellor = GameMethods.assignChancellor(activePlayers, selectedChancellor);
            if (selectedChancellor == null) {
                Toast.makeText(BoardGameActivity.this, "چنسلر قابل انتخاب نیست", Toast.LENGTH_SHORT).show();
            } else {
                TextView dialog_president = (TextView) mDialog.findViewById(R.id.president_id_in_dialog);
                TextView dialog_chancellor = (TextView) mDialog.findViewById(R.id.chanceler_id_in_dialog);
                dialog_president.setText("رئیس جمهور: " + GameMethods.getCurrentPresident().getName());
                dialog_chancellor.setText("چنسلر: " + GameMethods.getCurrentChancellor().getName());
                ImageView dialog_vote_yes_image = (ImageView) mDialog.findViewById(R.id.yes_vote_button);
                ImageView dialog_vote_no_image = (ImageView) mDialog.findViewById(R.id.no_vote_button);
                mDialog.show();

                dialog_vote_yes_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //yes action
                    }
                });
                dialog_vote_no_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //no action
                    }
                });
            }
        }
    }
}