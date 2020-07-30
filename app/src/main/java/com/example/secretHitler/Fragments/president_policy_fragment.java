package com.example.secretHitler.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secretHitler.R;

public class president_policy_fragment extends Fragment {
    ImageView card1, card2, card3;
    Button reverse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.president_policy_card_fragment, container, false);

        card1 = (ImageView) view.findViewById(R.id.president_policy_card_1_front);
        card2 = (ImageView) view.findViewById(R.id.president_policy_card_2_front);
        card3 = (ImageView) view.findViewById(R.id.president_policy_card_3_front);
        reverse = (Button) view.findViewById(R.id.president_reverse_cards_button);

        return view;
    }
}
