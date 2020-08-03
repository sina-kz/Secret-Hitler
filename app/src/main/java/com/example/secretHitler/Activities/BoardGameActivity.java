package com.example.secretHitler.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secretHitler.Controller.CommonController;
import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.Models.PolicyCard;
import com.example.secretHitler.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class BoardGameActivity extends AppCompatActivity {

    ArrayList<Player> lstPlayer;
    ArrayList<CheckBox> lstCheckBoxes;
    ArrayList<Player> activePlayers;
    RecyclerView recyclerView;
    TextView showPresidentTextView;
    Button activateButton;
    Dialog mDialog;
    Dialog fascistDialog;
    ImageView fascistMap, liberalMap;
    ImageView reject1, reject2, reject3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivity();
        SharedPreferences sharedPreferences = getSharedPreferences("my shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        lstPlayer = gson.fromJson(json, type);
        assert lstPlayer != null;
        GameMethods.setAllPlayers(lstPlayer);
        if (GameMethods.getAllPlayers().size() > 8) {
            fascistMap.setImageResource(R.drawable.fascist_board_9_10);
        }
        if (GameMethods.getAllPlayers().size() < 7) {
            fascistMap.setImageResource(R.drawable.fascist_board_5_6);
        }
        if (GameMethods.isFirstTimeCreated()) {
            GameMethods.initializePresident(GameMethods.getAllPlayers());
            GameMethods.setFirstTimeCreated(false);
        }
        CommonController.showChancellors(this, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton
                , mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
    }

    public void initializeActivity() {
        setContentView(R.layout.activity_board_game);

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_vote);
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        lstCheckBoxes = new ArrayList<>();
        for (CheckBox checkBox : lstCheckBoxes) {
            checkBox.setChecked(false);
        }
        recyclerView = findViewById(R.id.recyclerview_chanceler);
        showPresidentTextView = findViewById(R.id.chanceler_text);
        activateButton = findViewById(R.id.activate_button);
        fascistMap = findViewById(R.id.facscist_board);
        liberalMap = findViewById(R.id.liberal_board);

        reject1 = findViewById(R.id.reject_image_1);
        reject2 = findViewById(R.id.reject_image_2);
        reject3 = findViewById(R.id.reject_image_3);
        fascistDialog = new Dialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                PolicyCard choosePolicyCard = data.getParcelableExtra("RESULT");
                mDialog.dismiss();
                if (choosePolicyCard == null) {
                    GameMethods.nextPresident(activePlayers);
                    CommonController.showChancellors(this, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton
                            , mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                    return;
                }
                Toast.makeText(this, "سیاست " + choosePolicyCard.getType().toString().toLowerCase() + " تصویب شد", Toast.LENGTH_LONG).show();
                switch (choosePolicyCard.getType()) {
                    case LIBERAL:
                        CommonController.handleLiberalMap(liberalMap);
                        CommonController.handleFascistMap(fascistMap);
                        CommonController.checkLiberalCardApproval(this, lstCheckBoxes, recyclerView,
                                showPresidentTextView, activateButton, mDialog, liberalMap, fascistMap,
                                reject1, reject2, reject3, fascistDialog);
                        break;
                    case FASCIST:
                        CommonController.handleLiberalMap(liberalMap);
                        CommonController.handleFascistMap(fascistMap);
                        CommonController.checkFascistCardApproval(this, lstCheckBoxes, recyclerView,
                                showPresidentTextView, activateButton, mDialog, liberalMap, fascistMap,
                                reject1, reject2, reject3, fascistDialog);
                        break;
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}