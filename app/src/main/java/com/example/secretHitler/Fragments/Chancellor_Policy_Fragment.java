package com.example.secretHitler.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.PolicyCard;
import com.example.secretHitler.R;
import com.example.secretHitler.Utils.Numbers;

import java.util.ArrayList;
import java.util.Objects;

public class Chancellor_Policy_Fragment extends Fragment {
    private ImageView card1_front, card2_front;
    private ImageView card1_back, card2_back;
    private Button reverse, vetoButton;
    private AnimatorSet front_anim1, front_anim2;
    private AnimatorSet back_anim1, back_anim2;
    private boolean isFront = true;
    private boolean eliminate = false;
    private Dialog vetoDialog;
    private TextView showMessage;
    ArrayList<PolicyCard> policyCardArrayList;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chancellor_policy_card_fragment, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        policyCardArrayList = bundle.getParcelableArrayList("POLICY");

        card1_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_front);
        card2_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_front);
        card1_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_back);
        card2_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_back);
        reverse = (Button) view.findViewById(R.id.chancellor_reverse_cards_button);
        vetoButton = (Button) view.findViewById(R.id.veto_button);
        showMessage = view.findViewById(R.id.chancellor_message);

        if (policyCardArrayList.get(0).getType() == Team.LIBERAL) {
            card1_back.setImageResource(R.drawable.liberal_card);
        } else {
            card1_back.setImageResource(R.drawable.facist_card);
        }
        if (policyCardArrayList.get(1).getType() == Team.LIBERAL) {
            card2_back.setImageResource(R.drawable.liberal_card);
        } else {
            card2_back.setImageResource(R.drawable.facist_card);
        }
        if (GameMethods.getNumberOfFascistsUsedPolicies() == Numbers.fascistsPoliciesToWin - 1) {
            vetoButton.setVisibility(View.VISIBLE);
            vetoButton.setEnabled(true);
        }

        double scale = getResources().getDisplayMetrics().density;
        card1_front.setCameraDistance((float) (8000 * scale));
        card2_front.setCameraDistance((float) (8000 * scale));
        card1_back.setCameraDistance((float) (8000 * scale));
        card2_back.setCameraDistance((float) (8000 * scale));

        front_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        front_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);
        showMessage.setText("نخست وزیر (" + GameMethods.getCurrentChancellor().getName() + ") کارت ها را برگردان");

        card1_back.setClickable(false);
        card2_back.setClickable(false);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminate = true;
                if (isFront) {
                    showMessage.setText("نخست وزیر (" + GameMethods.getCurrentChancellor().getName() + ") سیاست دلخواهت را تصویب کن.");
                    front_anim1.setTarget(card2_front);
                    back_anim1.setTarget(card2_back);
                    front_anim1.start();
                    back_anim1.start();

                    front_anim2.setTarget(card1_front);
                    back_anim2.setTarget(card1_back);
                    front_anim2.start();
                    back_anim2.start();

                    card1_back.setClickable(true);
                    card2_back.setClickable(true);

                    isFront = false;
                } else {
                    showMessage.setText("نخست وزیر (" + GameMethods.getCurrentChancellor().getName() + ") کارت ها را برگردان");
                    front_anim1.setTarget(card2_back);
                    back_anim1.setTarget(card2_front);
                    back_anim1.start();
                    front_anim1.start();

                    front_anim2.setTarget(card1_back);
                    back_anim2.setTarget(card1_front);
                    back_anim2.start();
                    front_anim2.start();

                    card1_back.setClickable(false);
                    card2_back.setClickable(false);

                    isFront = true;
                }
            }
        });

        card1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eliminate) {
                    Intent intent = new Intent();
                    intent.putExtra("RESULT", policyCardArrayList.get(0));

                    GameMethods.skipPolicy(policyCardArrayList.get(1));
                    GameMethods.usePolicy(policyCardArrayList.get(0));


                    Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        card2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eliminate) {
                    Intent intent = new Intent();
                    intent.putExtra("RESULT", policyCardArrayList.get(1));

                    GameMethods.skipPolicy(policyCardArrayList.get(0));
                    GameMethods.usePolicy(policyCardArrayList.get(1));

                    Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        vetoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vetoDialog = new Dialog(Objects.requireNonNull(getContext()));
                vetoDialog.setContentView(R.layout.dialog_box_veto);
                Objects.requireNonNull(vetoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView presidentText = (TextView) vetoDialog.findViewById(R.id.president_id_in_dialog_veto);
                presidentText.setText("رئیس جمهور: " + GameMethods.getCurrentPresident().getName());
                ImageView yesButton = (ImageView) vetoDialog.findViewById(R.id.yes_veto_button);
                ImageView noButton = (ImageView) vetoDialog.findViewById(R.id.no_veto_button);
                vetoDialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vetoDialog.dismiss();
                        GameMethods.useVetoPower(policyCardArrayList);
                        System.out.println(GameMethods.activePolicies(GameMethods.getAllPolicies()).size());
                        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, new Intent());
                        getActivity().finish();
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vetoDialog.dismiss();
                        vetoButton.setEnabled(false);
                    }
                });
            }
        });

        return view;
    }

}
