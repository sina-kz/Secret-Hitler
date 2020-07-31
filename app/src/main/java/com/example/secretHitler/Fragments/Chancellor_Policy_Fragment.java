package com.example.secretHitler.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Models.PolicyCard;
import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.Objects;

public class Chancellor_Policy_Fragment extends Fragment {
    private ImageView card1_front, card2_front;
    private ImageView card1_back, card2_back;
    private Button reverse;
    private AnimatorSet front_anim1, front_anim2;
    private AnimatorSet back_anim1, back_anim2;
    private boolean isFront = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chancellor_policy_card_fragment, container, false);

        Bundle bundle = this.getArguments();
        ArrayList<PolicyCard> policyCardArrayList = bundle.getParcelableArrayList("POLICY");

        card1_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_front);
        card2_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_front);
        card1_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_back);
        card2_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_back);
        reverse = (Button) view.findViewById(R.id.chancellor_reverse_cards_button);

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

        double scale = getResources().getDisplayMetrics().density;
        card1_front.setCameraDistance((float) (8000 * scale));
        card2_front.setCameraDistance((float) (8000 * scale));
        card1_back.setCameraDistance((float) (8000 * scale));
        card2_back.setCameraDistance((float) (8000 * scale));

        front_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        front_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFront) {
                    front_anim1.setTarget(card2_front);
                    back_anim1.setTarget(card2_back);
                    front_anim1.start();
                    back_anim1.start();

                    front_anim2.setTarget(card1_front);
                    back_anim2.setTarget(card1_back);
                    front_anim2.start();
                    back_anim2.start();


                    isFront = false;
                } else {
                    front_anim1.setTarget(card2_back);
                    back_anim1.setTarget(card2_front);
                    back_anim1.start();
                    front_anim1.start();

                    front_anim2.setTarget(card1_back);
                    back_anim2.setTarget(card1_front);
                    back_anim2.start();
                    front_anim2.start();


                    isFront = true;
                }
            }
        });

        return view;
    }

}
