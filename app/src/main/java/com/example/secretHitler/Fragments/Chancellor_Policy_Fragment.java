package com.example.secretHitler.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

import com.example.secretHitler.R;

import java.util.Objects;

public class Chancellor_Policy_Fragment extends Fragment {
    ImageView card1_front, card2_front;
    ImageView card1_back, card2_back;
    Button reverse;
    AnimatorSet front_anim;
    AnimatorSet back_anim;
    boolean isFront = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chancellor_policy_card_fragment, container, false);

        card1_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_front);
        card2_front = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_front);
        card1_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_1_back);
        card2_back = (ImageView) view.findViewById(R.id.chancellor_policy_card_2_back);
        reverse = (Button) view.findViewById(R.id.chancellor_reverse_cards_button);

        double scale = getResources().getDisplayMetrics().density;
        card1_front.setCameraDistance((float) (8000 * scale));
        card2_front.setCameraDistance((float) (8000 * scale));
        card1_back.setCameraDistance((float) (8000 * scale));
        card2_back.setCameraDistance((float) (8000 * scale));

        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFront) {
                    front_anim.setTarget(card1_front);
                    back_anim.setTarget(card1_back);
                    front_anim.start();
                    back_anim.start();

                    front_anim.setTarget(card2_front);
                    back_anim.setTarget(card2_back);
                    front_anim.start();
                    back_anim.start();

                    isFront = false;
                } else {
                    front_anim.setTarget(card1_back);
                    back_anim.setTarget(card1_front);
                    back_anim.start();
                    front_anim.start();

                    front_anim.setTarget(card2_back);
                    back_anim.setTarget(card2_front);
                    back_anim.start();
                    front_anim.start();

                    isFront = true;
                }
            }
        });

        return view;
    }
}
