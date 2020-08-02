package com.example.secretHitler.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Activities.PolicyChoose;
import com.example.secretHitler.Activities.ShowResultsActivity;
import com.example.secretHitler.Adapters.RecyclerViewAdapterChanceler;
import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.Models.PolicyCard;
import com.example.secretHitler.R;
import com.example.secretHitler.Utils.Numbers;

import java.util.ArrayList;
import java.util.Objects;

public class CommonController {
    private static ArrayList<Player> activePlayers;
    private static ArrayList<Player> assignableChancellors;
    private static ArrayList<PolicyCard> activePolicies;

    @SuppressLint("SetTextI18n")
    public static void showChancellors(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                       final RecyclerView recyclerView, final TextView showPresidentTextView,
                                       final Button activateButton, final Dialog mDialog) {
        activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
        lstCheckBoxes.clear();
        if (!GameMethods.getCurrentPresident().isActive()) {
            GameMethods.nextPresidentWhenKill(activePlayers);
        }
        assignableChancellors = GameMethods.assignableChancellors(activePlayers);
        activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());
        if (activePolicies.size() < Numbers.accessibleNumberOfPolicies) {
            ArrayList<PolicyCard> newPolicies = GameMethods.reorderPolicies(activePolicies);
            GameMethods.setAllPolicies(newPolicies);
            activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());

        }
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(activity, assignableChancellors, lstCheckBoxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(myAdapter);
        showPresidentTextView.setText(GameMethods.getCurrentPresident().getName() + "\n" + "چنسلر را انتخاب کن");
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton, mDialog);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private static void showDialogBox(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                      final RecyclerView recyclerView, final TextView showPresidentTextView,
                                      final Button activateButton, final Dialog mDialog) {
        boolean flag = false;
        int i = 0;
        for (; i < lstCheckBoxes.size(); i++) {
            if (lstCheckBoxes.get(i).isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            Toast.makeText(activity, "لطفا یک بازیکن را به عنوان چنسلر انتخاب کنید", Toast.LENGTH_SHORT).show();
        } else {
            Player selectedChancellor = assignableChancellors.get(i);
            selectedChancellor = GameMethods.assignChancellor(activePlayers, selectedChancellor);
            if (selectedChancellor == null) {
                Toast.makeText(activity, "چنسلر قابل انتخاب نیست", Toast.LENGTH_SHORT).show();
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
                        boolean fascistsWon = GameMethods.checkWinStateForFascists();
                        if (fascistsWon && GameMethods.getNumberOfFascistsUsedPolicies() != Numbers.fascistsPolicesToActiveHitler) {
                            final Intent intent = new Intent(activity, ShowResultsActivity.class);
                            intent.putExtra("LIBERAL_WON", false);
                            activity.startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, PolicyChoose.class);
                            activity.startActivityForResult(intent, 1);
                        }
                    }
                });
                dialog_vote_no_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRejectChancellor(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton, mDialog);
                    }
                });
            }
        }
    }

    private static void onRejectChancellor(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                           final RecyclerView recyclerView, final TextView showPresidentTextView,
                                           final Button activateButton, final Dialog mDialog) {
        final PolicyCard rejectResult = GameMethods.threeRejectsPolicy(activePlayers, activePolicies);
        if (rejectResult == null) {
            mDialog.dismiss();
            activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
            GameMethods.nextPresident(activePlayers);
            showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton, mDialog );
        } else {
            GameMethods.usePolicy(rejectResult);

            mDialog.dismiss();
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_three_rejects);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button back = dialog.findViewById(R.id.threeRejectsButton);
            ImageView imageView = dialog.findViewById(R.id.threeRejectsImage);
            if (rejectResult.getType() == Team.FASCIST) {
                imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.facist_card));
            } else {
                imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.liberal_card));
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
//                    handleLiberalMap();
//                    handleFascistMap();
                    if (rejectResult.getType() == Team.LIBERAL) {
//                        checkLiberalCardApproval();
                    } else {
//                        checkFascistCardApproval();
                    }
                }
            });
            dialog.show();
        }
    }
}
