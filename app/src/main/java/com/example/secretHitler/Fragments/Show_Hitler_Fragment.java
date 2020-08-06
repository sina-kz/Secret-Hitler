package com.example.secretHitler.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;

public class Show_Hitler_Fragment extends Fragment {
    private int counter;
    private ArrayList<Player> players;
    private Player player;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.role_hitler_fragment, container, false);
        final ImageView backOfCard = (ImageView) view.findViewById(R.id.back_of_hitler_card);
        final ImageView frontOfCard = (ImageView) view.findViewById(R.id.front_of_hitler_card);
        final AnimatorSet front_anim1;
        final AnimatorSet back_anim1;
        final boolean[] isFront = {true};

        double scale = getResources().getDisplayMetrics().density;
        backOfCard.setCameraDistance((float) ((8000) * scale));
        frontOfCard.setCameraDistance((float) (8000 * scale));

        front_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        backOfCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (isFront[0]) {
                    front_anim1.setTarget(frontOfCard);
                    back_anim1.setTarget(backOfCard);
                    front_anim1.start();
                    back_anim1.start();

                    isFront[0] = false;

                    ArrayList<Player> teammates = GameMethods.teammatesToBeShown(players, player);
                    textView.setText(player.getName() + "\n" + player.teammatesString(teammates));
                } else {
                    front_anim1.setTarget(backOfCard);
                    back_anim1.setTarget(frontOfCard);
                    back_anim1.start();
                    front_anim1.start();


                    isFront[0] = true;

                    textView.setText(player.getName());
                }
            }
        });
        return view;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
