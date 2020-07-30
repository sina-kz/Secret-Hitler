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
    AnimatorSet front_anim1, front_anim2;
    AnimatorSet back_anim1, back_anim2;
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
