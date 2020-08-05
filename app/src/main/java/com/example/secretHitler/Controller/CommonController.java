package com.example.secretHitler.Controller;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
                                       final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                       final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                       final ImageView reject3, final Dialog fascistDialog) {
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
                showDialogBox(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                        mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private static void showDialogBox(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                      final RecyclerView recyclerView, final TextView showPresidentTextView,
                                      final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                      final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                      final ImageView reject3, final Dialog fascistDialog) {
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
                TextView dialog_president = mDialog.findViewById(R.id.president_id_in_dialog);
                TextView dialog_chancellor = mDialog.findViewById(R.id.chanceler_id_in_dialog);
                dialog_president.setText("رئیس جمهور: " + GameMethods.getCurrentPresident().getName());
                dialog_chancellor.setText("چنسلر: " + GameMethods.getCurrentChancellor().getName());
                ImageView dialog_vote_yes_image = mDialog.findViewById(R.id.yes_vote_button);
                ImageView dialog_vote_no_image = mDialog.findViewById(R.id.no_vote_button);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                dialog_vote_yes_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GameMethods.completeAssignChancellor();
                        handleRejectImage(0, reject1, reject2, reject3);
                        boolean fascistsWon = GameMethods.checkWinStateForFascists();
                        if (fascistsWon) {
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
                        onRejectChancellor(activity, lstCheckBoxes, recyclerView, showPresidentTextView,
                                activateButton, mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        handleRejectImage(1, reject1, reject2, reject3);
                    }
                });
            }
        }
    }

    private static void onRejectChancellor(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                           final RecyclerView recyclerView, final TextView showPresidentTextView,
                                           final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                           final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                           final ImageView reject3, final Dialog fascistDialog) {
        final PolicyCard rejectResult = GameMethods.threeRejectsPolicy(activePolicies);
        if (rejectResult == null) {
            mDialog.dismiss();
            activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
            GameMethods.nextPresident(activePlayers);
            showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                    mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
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
                    handleLiberalMap(liberalMap);
                    handleFascistMap(fascistMap);
                    if (rejectResult.getType() == Team.LIBERAL) {
                        checkLiberalCardApproval(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                    } else {
                        checkFascistCardApproval(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                    }
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private static void handleRejectImage(int code, ImageView reject1, ImageView reject2, ImageView reject3) {
        if (code == 0) {
            reject1.setImageResource(R.drawable.checkbox);
            reject2.setImageResource(R.drawable.checkbox);
            reject3.setImageResource(R.drawable.checkbox);
        } else {
            switch (GameMethods.getNumberOfRejects()) {
                case 0:
                    reject1.setImageResource(R.drawable.checkbox);
                    reject2.setImageResource(R.drawable.checkbox);
                    reject3.setImageResource(R.drawable.checkbox);
                    break;
                case 1:
                    reject1.setImageResource(R.drawable.reject_image);
                    break;
                case 2:
                    reject1.setImageResource(R.drawable.reject_image);
                    reject2.setImageResource(R.drawable.reject_image);
                    break;
                case 3:
                    reject1.setImageResource(R.drawable.reject_image);
                    reject2.setImageResource(R.drawable.reject_image);
                    reject3.setImageResource(R.drawable.reject_image);
                    break;
            }
        }
    }

    public static void handleLiberalMap(ImageView liberalMap) {
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

    public static void handleFascistMap(ImageView fascistMap) {
        if (GameMethods.getAllPlayers().size() == 5 || GameMethods.getAllPlayers().size() == 6) {
            switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
                case 0:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6);
                    break;
                case 1:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6_one_fail);
                    break;
                case 2:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6_two_fail);
                    break;
                case 3:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6_three_fail);
                    break;
                case 4:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6_four_fail);
                    break;
                case 5:
                    fascistMap.setImageResource(R.drawable.fascist_board_5_6_five_fail);
                    break;
            }
        }
        if (GameMethods.getAllPlayers().size() == 7 || GameMethods.getAllPlayers().size() == 8) {
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
        if (GameMethods.getAllPlayers().size() == 9 || GameMethods.getAllPlayers().size() == 10) {
            switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
                case 0:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10);
                    break;
                case 1:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10_one_fail);
                    break;
                case 2:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10_two_fail);
                    break;
                case 3:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10_three_fail);
                    break;
                case 4:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10_four_fail);
                    break;
                case 5:
                    fascistMap.setImageResource(R.drawable.fascist_board_9_10_five_fail);
                    break;
            }
        }
    }

    public static void checkLiberalCardApproval(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                                final RecyclerView recyclerView, final TextView showPresidentTextView,
                                                final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                                final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                                final ImageView reject3, final Dialog fascistDialog) {
        boolean liberalsWon = GameMethods.checkWinStateForLiberals(GameMethods.getAllPlayers());
        GameMethods.nextPresident(activePlayers);
        showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
        if (liberalsWon) {
            final Intent intent = new Intent(activity.getBaseContext(), ShowResultsActivity.class);
            intent.putExtra("LIBERAL_WON", true);
            activity.startActivity(intent);
        }
    }

    public static void checkFascistCardApproval(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                                final RecyclerView recyclerView, final TextView showPresidentTextView,
                                                final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                                final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                                final ImageView reject3, final Dialog fascistDialog) {
        try {
            fascistDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean fascistIsWon = GameMethods.checkWinStateForFascists();
        if (fascistIsWon && !GameMethods.getCurrentChancellor().isHitler()) {
            final Intent intent = new Intent(activity.getBaseContext(), ShowResultsActivity.class);
            intent.putExtra("LIBERAL_WON", false);
            activity.startActivity(intent);
        } else {
            if (GameMethods.getAllPlayers().size() == 5 || GameMethods.getAllPlayers().size() == 6) {
                switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
                    case 1:
                    case 2:
                        activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                        GameMethods.nextPresident(activePlayers);
                        showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        break;
                    case 3:
                        activePolicies = GameMethods.activePolicies(activePolicies);
                        showPolicyPeekDialogBox(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        break;
                    case 4:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 4);
                        break;
                    case 5:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 5);
                        break;
                }
            }
            if (GameMethods.getAllPlayers().size() == 7 || GameMethods.getAllPlayers().size() == 8) {
                switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
                    case 1:
                        activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                        GameMethods.nextPresident(activePlayers);
                        showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        break;
                    case 2:
                        showFascistDialogBoxSecondOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        break;
                    case 3:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 3);
                        break;
                    case 4:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 4);
                        break;
                    case 5:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 5);
                        break;
                }
            }
            if (GameMethods.getAllPlayers().size() == 9 || GameMethods.getAllPlayers().size() == 10) {
                switch (GameMethods.getNumberOfFascistsUsedPolicies()) {
                    case 1:
                    case 2:
                        showFascistDialogBoxSecondOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                        break;
                    case 3:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 3);
                        break;
                    case 4:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 4);
                        break;
                    case 5:
                        showFascistDialogBoxUpperOrder(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                                mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog, 5);
                        break;
                }
            }
//            activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
//            GameMethods.nextPresident(activePlayers);
//            showChancellors();
        }
    }

    private static void showFascistDialogBoxSecondOrder(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                                        final RecyclerView recyclerView, final TextView showPresidentTextView,
                                                        final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                                        final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                                        final ImageView reject3, final Dialog fascistDialog) {
        fascistDialog.setContentView(R.layout.dialog_player_team);
        Objects.requireNonNull(fascistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button button = fascistDialog.findViewById(R.id.fascistDialogButton);
        RecyclerView otherPlayersRV = fascistDialog.findViewById(R.id.fascistDialogPlayers);

        final ArrayList<CheckBox> fCheckBoxes = new ArrayList<>();
        for (CheckBox checkBox : fCheckBoxes) {
            checkBox.setChecked(false);
        }

        final ArrayList<Player> otherPlayers = GameMethods.otherPlayers(activePlayers, GameMethods.getCurrentPresident());
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(activity, otherPlayers, fCheckBoxes);
        otherPlayersRV.setLayoutManager(new LinearLayoutManager(activity));
        otherPlayersRV.setAdapter(myAdapter);
        fascistDialog.setCanceledOnTouchOutside(false);
        fascistDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView team = fascistDialog.findViewById(R.id.fascistDialogTeamFront);
                ImageView back = fascistDialog.findViewById(R.id.fascistDialogTeamBack);
                AnimatorSet front_anim, back_anim;

                double scale = activity.getResources().getDisplayMetrics().density;
                team.setCameraDistance((float) (8000 * scale));
                back.setCameraDistance((float) (8000 * scale));

                front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.front_animator);
                back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.back_animator);

                front_anim.setTarget(back);
                back_anim.setTarget(team);

                if (button.getText().equals("تایید")) {
                    int index = -1;
                    boolean flag = false;
                    for (int i = 0; i < otherPlayers.size(); i++) {
                        if (fCheckBoxes.get(i).isChecked()) {
                            index = i;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(activity, "لطفا یک بازیکن را انتخاب کنید", Toast.LENGTH_SHORT).show();
                    } else {
                        front_anim.start();
                        back_anim.start();
                        if (otherPlayers.get(index).getTeam() == Team.LIBERAL)
                            team.setImageDrawable(activity.getResources().getDrawable(R.drawable.liberal_team));
                        else
                            team.setImageDrawable(activity.getResources().getDrawable(R.drawable.fascist_team));
                        button.setText("بستن");
                    }

                } else if (button.getText().equals("بستن")) {
                    fascistDialog.dismiss();
                    activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                    GameMethods.nextPresident(activePlayers);
                    showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                            mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                }


            }
        });
    }

    private static void showFascistDialogBoxUpperOrder(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                                       final RecyclerView recyclerView, final TextView showPresidentTextView,
                                                       final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                                       final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                                       final ImageView reject3, final Dialog fascistDialog, final int round) {
        fascistDialog.setContentView(R.layout.dialog_choose_player);
        Objects.requireNonNull(fascistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView subject = fascistDialog.findViewById(R.id.fascistDialogHeaderAct);
        final Button button = fascistDialog.findViewById(R.id.fascistDialogButtonAct);
        RecyclerView otherPlayersRV = fascistDialog.findViewById(R.id.fascistDialogPlayersAct);

        final ArrayList<CheckBox> fCheckBoxes = new ArrayList<>();
        for (CheckBox checkBox : fCheckBoxes) {
            checkBox.setChecked(false);
        }

        final ArrayList<Player> otherPlayers = GameMethods.otherPlayers(activePlayers, GameMethods.getCurrentPresident());
        RecyclerViewAdapterChanceler myAdapter = new RecyclerViewAdapterChanceler(activity, otherPlayers, fCheckBoxes);
        otherPlayersRV.setLayoutManager(new LinearLayoutManager(activity));
        otherPlayersRV.setAdapter(myAdapter);
        if (round == 3) {
            subject.setText("رئیس جمهور باید رئیس جمهور بعدی را انتخاب کند");
        } else {
            subject.setText("رئیس جمهور باید یک نفر را بکشد");
            if (round == 5) {
                subject.append("\nقدرت وتو نیز فعال شد");
            }
        }
        fascistDialog.setCanceledOnTouchOutside(false);
        fascistDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals("تایید")) {

                    int index = -1;
                    boolean flag = false;
                    for (int i = 0; i < otherPlayers.size(); i++) {
                        if (fCheckBoxes.get(i).isChecked()) {
                            index = i;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(activity, "لطفا یک بازیکن را انتخاب کنید", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                    } else {
                        if (round == 3) {
                            GameMethods.selectPresident(otherPlayers.get(index));
                        } else {
                            GameMethods.kill(otherPlayers.get(index));
                            if (round == 5) {
                                GameMethods.setVetoEnable(true);
                            }
                            boolean liberalsWon = GameMethods.checkWinStateForLiberals(GameMethods.getAllPlayers());
                            if (liberalsWon) {
                                final Intent intent = new Intent(activity.getBaseContext(), ShowResultsActivity.class);
                                intent.putExtra("LIBERAL_WON", true);
                                activity.startActivity(intent);
                            }
                            activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                        }
                        button.setText("بستن");
                    }
                } else if (button.getText().equals("بستن")) {
                    fascistDialog.dismiss();
                    activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                    if (round != 3) {
                        GameMethods.nextPresident(activePlayers);
                    }
                    showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                            mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                }
            }
        });
    }

    private static void showPolicyPeekDialogBox(final Activity activity, final ArrayList<CheckBox> lstCheckBoxes,
                                                final RecyclerView recyclerView, final TextView showPresidentTextView,
                                                final Button activateButton, final Dialog mDialog, final ImageView liberalMap,
                                                final ImageView fascistMap, final ImageView reject1, final ImageView reject2,
                                                final ImageView reject3, final Dialog fascistDialog) {
        fascistDialog.setContentView(R.layout.dialog_policy_peek);
        Objects.requireNonNull(fascistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button button = fascistDialog.findViewById(R.id.policy_peek_button);
        final ImageView card1_front = fascistDialog.findViewById(R.id.first_policy_front);
        final ImageView card1_back = fascistDialog.findViewById(R.id.first_policy_back);
        final ImageView card2_front = fascistDialog.findViewById(R.id.second_policy_front);
        final ImageView card2_back = fascistDialog.findViewById(R.id.second_policy_back);
        final ImageView card3_front = fascistDialog.findViewById(R.id.third_policy_front);
        final ImageView card3_back = fascistDialog.findViewById(R.id.third_policy_back);

        final AnimatorSet front_anim1, front_anim2, front_anim3;
        final AnimatorSet back_anim1, back_anim2, back_anim3;

        double scale = activity.getResources().getDisplayMetrics().density;
        card1_front.setCameraDistance((float) (8000 * scale));
        card2_front.setCameraDistance((float) (8000 * scale));
        card3_front.setCameraDistance((float) (8000 * scale));
        card1_back.setCameraDistance((float) (8000 * scale));
        card2_back.setCameraDistance((float) (8000 * scale));
        card3_back.setCameraDistance((float) (8000 * scale));

        front_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.front_animator);
        back_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.back_animator);

        front_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.front_animator);
        back_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.back_animator);

        front_anim3 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.front_animator);
        back_anim3 = (AnimatorSet) AnimatorInflater.loadAnimator(activity.getApplicationContext(), R.animator.back_animator);

        card1_back.setClickable(false);
        card2_back.setClickable(false);
        card3_back.setClickable(false);
        fascistDialog.setCanceledOnTouchOutside(false);
        fascistDialog.show();
        final ArrayList<PolicyCard> topThree = GameMethods.pickThreePolicies(activePolicies);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals("برگرداندن کارت ها")) {
                    if (topThree.get(0).getType() == Team.LIBERAL) {
                        card1_back.setImageResource(R.drawable.liberal_card);
                    } else {
                        card1_back.setImageResource(R.drawable.facist_card);
                    }
                    if (topThree.get(1).getType() == Team.LIBERAL) {
                        card2_back.setImageResource(R.drawable.liberal_card);
                    } else {
                        card2_back.setImageResource(R.drawable.facist_card);
                    }
                    if (topThree.get(2).getType() == Team.LIBERAL) {
                        card3_back.setImageResource(R.drawable.liberal_card);
                    } else {
                        card3_back.setImageResource(R.drawable.facist_card);
                    }

                    front_anim1.setTarget(card1_front);
                    back_anim1.setTarget(card1_back);
                    front_anim1.start();
                    back_anim1.start();

                    front_anim2.setTarget(card2_front);
                    back_anim2.setTarget(card2_back);
                    front_anim2.start();
                    back_anim2.start();

                    front_anim3.setTarget(card3_front);
                    back_anim3.setTarget(card3_back);
                    front_anim3.start();
                    back_anim3.start();

                    button.setText("بستن");
                } else if (button.getText().equals("بستن")) {
                    fascistDialog.dismiss();
                    activePlayers = GameMethods.activePlayers(GameMethods.getAllPlayers());
                    GameMethods.nextPresident(activePlayers);
                    showChancellors(activity, lstCheckBoxes, recyclerView, showPresidentTextView, activateButton,
                            mDialog, liberalMap, fascistMap, reject1, reject2, reject3, fascistDialog);
                }
            }
        });
    }

}
