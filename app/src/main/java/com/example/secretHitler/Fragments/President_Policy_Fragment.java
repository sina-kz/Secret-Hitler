package com.example.secretHitler.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
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

public class President_Policy_Fragment extends Fragment {
    private ImageView card1_front, card2_front, card3_front;
    private ImageView card1_back, card2_back, card3_back;
    private Button reverse;
    private AnimatorSet front_anim1, front_anim2, front_anim3;
    private AnimatorSet back_anim1, back_anim2, back_anim3;
    private boolean isFront = true;
    private boolean eliminate = false;
    private TextView showMessage;
    private ArrayList<PolicyCard> threeTops;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.president_policy_card_fragment, container, false);

        card1_front = view.findViewById(R.id.president_policy_card_1_front);
        card2_front = view.findViewById(R.id.president_policy_card_2_front);
        card3_front = view.findViewById(R.id.president_policy_card_3_front);
        card1_back = view.findViewById(R.id.president_policy_card_1_back);
        card2_back = view.findViewById(R.id.president_policy_card_2_back);
        card3_back = view.findViewById(R.id.president_policy_card_3_back);
        reverse = view.findViewById(R.id.president_reverse_cards_button);
        showMessage = view.findViewById(R.id.president_message);
        ArrayList<PolicyCard> activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());
        if (activePolicies.size() < Numbers.accessibleNumberOfPolicies) {
            ArrayList<PolicyCard> newPolicies = GameMethods.reorderPolicies(activePolicies);
            GameMethods.setAllPolicies(newPolicies);
            activePolicies = GameMethods.activePolicies(GameMethods.getAllPolicies());
        }
        threeTops = GameMethods.pickThreePolicies(activePolicies);
        showMessage.setText("رییس جمهور (" + GameMethods.getCurrentPresident().getName() + ") کارت ها را برگردان");
        setImages(threeTops);

        double scale = getResources().getDisplayMetrics().density;
        card1_front.setCameraDistance((float) (8000 * scale));
        card2_front.setCameraDistance((float) (8000 * scale));
        card3_front.setCameraDistance((float) (8000 * scale));
        card1_back.setCameraDistance((float) (8000 * scale));
        card2_back.setCameraDistance((float) (8000 * scale));
        card3_back.setCameraDistance((float) (8000 * scale));

        front_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        front_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        front_anim3 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        back_anim3 = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        card1_back.setClickable(false);
        card2_back.setClickable(false);
        card3_back.setClickable(false);

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminate = true;
                if (isFront) {
                    showMessage.setText("رییس جمهور (" + GameMethods.getCurrentPresident().getName() + ") کارت مورد نظرت را بسوزان");
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

                    card1_back.setClickable(true);
                    card2_back.setClickable(true);
                    card3_back.setClickable(true);

                    isFront = false;
                } else {
                    showMessage.setText("رییس جمهور (" + GameMethods.getCurrentPresident().getName() + ") کارت ها را برگردان");
                    front_anim1.setTarget(card1_back);
                    back_anim1.setTarget(card1_front);
                    front_anim1.start();
                    back_anim1.start();

                    front_anim2.setTarget(card2_back);
                    back_anim2.setTarget(card2_front);
                    front_anim2.start();
                    back_anim2.start();

                    front_anim3.setTarget(card3_back);
                    back_anim3.setTarget(card3_front);
                    front_anim3.start();
                    back_anim3.start();

                    card1_back.setClickable(false);
                    card2_back.setClickable(false);
                    card3_back.setClickable(false);

                    isFront = true;
                }
            }
        });
        card1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eliminate) {
                    card1_back.setVisibility(View.INVISIBLE);
                    card1_front.setVisibility(View.INVISIBLE);
                    GameMethods.skipPolicy(threeTops.get(0));
                    threeTops.remove(0);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("POLICY", threeTops);
                    Chancellor_Policy_Fragment chancellorPolicyFragment = new Chancellor_Policy_Fragment();
                    chancellorPolicyFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.policy_container, chancellorPolicyFragment)
                            .commit();

                }
            }
        });
        card2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eliminate) {
                    card2_back.setVisibility(View.INVISIBLE);
                    card2_front.setVisibility(View.INVISIBLE);
                    GameMethods.skipPolicy(threeTops.get(1));
                    threeTops.remove(1);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("POLICY", threeTops);
                    Chancellor_Policy_Fragment chancellorPolicyFragment = new Chancellor_Policy_Fragment();
                    chancellorPolicyFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.policy_container, chancellorPolicyFragment)
                            .commit();
                }
            }
        });
        card3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eliminate) {
                    card3_back.setVisibility(View.INVISIBLE);
                    card3_front.setVisibility(View.INVISIBLE);
                    GameMethods.skipPolicy(threeTops.get(2));
                    threeTops.remove(2);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("POLICY", threeTops);
                    Chancellor_Policy_Fragment chancellorPolicyFragment = new Chancellor_Policy_Fragment();
                    chancellorPolicyFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.policy_container, chancellorPolicyFragment)
                            .commit();
                }
            }
        });
        return view;
    }

    public void setImages(ArrayList<PolicyCard> threeTops) {
        if (threeTops.get(0).getType() == Team.LIBERAL) {
            card1_back.setImageResource(R.drawable.liberal_card);
        } else {
            card1_back.setImageResource(R.drawable.facist_card);
        }

        if (threeTops.get(1).getType() == Team.LIBERAL) {
            card2_back.setImageResource(R.drawable.liberal_card);
        } else {
            card2_back.setImageResource(R.drawable.facist_card);
        }
        if (threeTops.get(2).getType() == Team.LIBERAL) {
            card3_back.setImageResource(R.drawable.liberal_card);
        } else {
            card3_back.setImageResource(R.drawable.facist_card);
        }
    }
}

