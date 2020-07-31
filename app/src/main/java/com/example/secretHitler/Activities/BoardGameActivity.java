package com.example.secretHitler.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Models.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.Models.PolicyCard;
import com.example.secretHitler.R;
import com.example.secretHitler.Utils.Numbers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class BoardGameActivity extends AppCompatActivity {

    ArrayList<Player> lstPlayer;
    ArrayList<CheckBox> lstCheckBoxes;
    ArrayList<Player> activePlayers;
    ArrayList<Player> assignableChancellors;
    ArrayList<PolicyCard> activePolicies;
    RecyclerView recyclerView;
    TextView showPresidentTextView;
    Button activateButton;
    Dialog mDialog;
    Dialog fascistDialog;
    ImageView fascistMap, liberalMap;

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
        if (GameMethods.isFirstTimeCreated()) {
            GameMethods.initializePresident(GameMethods.getAllPlayers());
            GameMethods.setFirstTimeCreated(false);
        }
        showChancellors();
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
    }

    @SuppressLint("SetTextI18n")
    public void showChancellors() {
        activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
        assignableChancellors = GameMethods.assignableChancellors(activePlayers);
        activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());
        if (activePolicies.size() < Numbers.accessibleNumberOfPolicies) {
            ArrayList<PolicyCard> newPolicies = GameMethods.reorderPolicies(activePolicies);
            GameMethods.setAllPolicies(newPolicies);
            activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());
        }
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(this, assignableChancellors, lstCheckBoxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        showPresidentTextView.setText(GameMethods.getCurrentPresident().getName() + "\n" + "چنسلر را انتخاب کن");
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showDialogBox() {
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
                        GameMethods.completeAssignChancellor();
                        Intent intent = new Intent(getBaseContext(), PolicyChoose.class);
                        startActivityForResult(intent, 1);
                    }
                });
                dialog_vote_no_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRejectChancellor();
                    }
                });
            }
        }
    }

    public void onRejectChancellor() {
        PolicyCard rejectResult = GameMethods.threeRejectsPolicy(activePlayers, activePolicies);
        if (rejectResult == null) {
            mDialog.dismiss();
            initializeActivity();
            showChancellors();
        } else {
            GameMethods.usePolicy(rejectResult);
            Toast.makeText(BoardGameActivity.this, "تعداد ریجکت های متوالی از حد مجاز عبور کرد، بنابراین آخرین سیاست استفاده نشده به صورت خودکار تصویب شد.", Toast.LENGTH_LONG).show();
            mDialog.dismiss();
            initializeActivity();
            if (rejectResult.getType() == Team.LIBERAL) {
                handleLiberalMap();
                checkLiberalCardApproval();
            } else {
                handleFascistMap();
                showChancellors();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                PolicyCard choosePolicyCard = data.getParcelableExtra("RESULT");
                assert choosePolicyCard != null;
                Toast.makeText(this, "سیاست " + choosePolicyCard.getType().toString().toLowerCase() + " تصویب شد", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
                switch (choosePolicyCard.getType()) {
                    case LIBERAL:
                        handleLiberalMap();
                        checkLiberalCardApproval();
                        break;
                    case FASCIST:
                        handleFascistMap();
                        checkFascistCardApproval();
                        break;
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkFascistCardApproval() {
        boolean fascistIsWon = GameMethods.checkWinStateForFascists();
        if (fascistIsWon){
            //TODO
        }else {
            switch (GameMethods.getNumberOfFascistsUsedPolicies()){
                case 2:
                    showFascistDialogBoxSecondOrder();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
            GameMethods.nextPresident(activePlayers);
            showChancellors();
        }

    }

    public void checkLiberalCardApproval() {
        boolean liberalsWon = GameMethods.checkWinStateForLiberals(GameMethods.getAllPlayers());
        if (liberalsWon) {
            //to be done
        } else {
            GameMethods.nextPresident(activePlayers);
            showChancellors();
        }
    }

    private void handleLiberalMap() {
        switch (GameMethods.getNumberOfLiberalsUsedPolicies()) {
            case 0:
                liberalMap.setImageResource(R.drawable.lib_board);
                break;
            case 1:
                liberalMap.setImageResource(R.drawable.liberal_board_one_success);
                break;
            case 2:
                liberalMap.setImageResource(R.drawable.liberal_board_two_success);
                break;
            case 3:
                liberalMap.setImageResource(R.drawable.liberal_board_three_success);
                break;
            case 4:
                liberalMap.setImageResource(R.drawable.liberal_board_four_success);
                break;
        }
    }

    private void handleFascistMap() {
        switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
            case 0:
                fascistMap.setImageResource(R.drawable.facist_board);
                break;
            case 1:
                fascistMap.setImageResource(R.drawable.facist_board_one_fail_7_8);
                break;
            case 2:
                fascistMap.setImageResource(R.drawable.facist_board_two_fail_7_8);
                break;
            case 3:
                fascistMap.setImageResource(R.drawable.facist_board_three_fail_7_8);
                break;
            case 4:
                fascistMap.setImageResource(R.drawable.facist_board_four_fail_7_8);
                break;
            case 5:
                fascistMap.setImageResource(R.drawable.facist_board_five_fail_7_8);
                break;
        }
    }

    public void showFascistDialogBoxSecondOrder(){
        fascistDialog = new Dialog(this);
        fascistDialog.setContentView(R.layout.dialog_player_team);
        Objects.requireNonNull(fascistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView subject = (TextView) fascistDialog.findViewById(R.id.fascistDialogHeader);
        final Button button = (Button) fascistDialog.findViewById(R.id.fascistDialogButton);
        RecyclerView otherPlayersRV = (RecyclerView) fascistDialog.findViewById(R.id.fascistDialogPlayers);

        final ArrayList<CheckBox> fCheckBoxes = new ArrayList<>();
        for (CheckBox checkBox: fCheckBoxes) {
            checkBox.setChecked(false);
        }

        final ArrayList<Player> otherPlayers = GameMethods.otherPlayers(activePlayers, GameMethods.getCurrentPresident());
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(this, otherPlayers, fCheckBoxes);
        otherPlayersRV.setLayoutManager(new LinearLayoutManager(this));
        otherPlayersRV.setAdapter(myAdapter);

        fascistDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);
                ImageView team = (ImageView) fascistDialog.findViewById(R.id.fascistDialogTeamFront);
                ImageView back = (ImageView) fascistDialog.findViewById(R.id.fascistDialogTeamBack);
                AnimatorSet front_anim, back_anim;

                double scale = getResources().getDisplayMetrics().density;
                team.setCameraDistance((float) (8000 * scale));
                back.setCameraDistance((float) (8000 * scale));

                front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
                back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);

                front_anim.setTarget(back);
                back_anim.setTarget(team);

                front_anim.start();
                back_anim.start();

                int index = -1;
                boolean flag = false;
                for(int i = 0; i < otherPlayers.size(); i++){
                    if (fCheckBoxes.get(i).isChecked()){
                        index = i;
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    Toast.makeText(BoardGameActivity.this, "لطفا یک بازیکن را انتخاب کنید", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(otherPlayers.get(index).getTeam()==Team.LIBERAL) team.setImageDrawable(getResources().getDrawable(R.drawable.liberal_team));
                    else team.setImageDrawable(getResources().getDrawable(R.drawable.fascist_team));
                }
            }
        });
    }
}