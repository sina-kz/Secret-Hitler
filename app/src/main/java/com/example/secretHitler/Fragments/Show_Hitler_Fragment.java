package com.example.secretHitler.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class Show_Hitler_Fragment extends Fragment {
    private EasyFlipView hitlerFlipView;
    private int counter;
    private ArrayList<Player> players;
    private Player player;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.role_hitler_fragment, container, false);
        hitlerFlipView = view.findViewById(R.id.hitler_flipview);
        hitlerFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                if (counter % 2 == 0) {
                    ArrayList<Player> teammates = GameMethods.teammatesToBeShown(players, player);
                    textView.setText(player.getName() + "\n" + player.teammatesString(teammates));
                } else
                    textView.setText(player.getName());
                counter++;
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
