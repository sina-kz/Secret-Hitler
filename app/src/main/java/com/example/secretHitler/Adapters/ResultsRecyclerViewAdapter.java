package com.example.secretHitler.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Enums.Team;
import com.example.secretHitler.Controller.GameMethods;
import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.MyViewHolderResult> {
    private Context mContext;
    private ArrayList<Player> mData;
    private ArrayList<TextView> winningTextViews;

    public ResultsRecyclerViewAdapter(Context mContext, ArrayList<Player> mData, ArrayList<TextView> winningTextViews) {
        this.mContext = mContext;
        this.mData = mData;
        this.winningTextViews = winningTextViews;
    }

    @NonNull
    @Override
    public MyViewHolderResult onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.result_player_item, parent, false);
        return new MyViewHolderResult(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderResult holder, int position) {
        holder.playerName.setText(mData.get(position).getName());
        if (mData.get(position).getTeam() == Team.LIBERAL) {
            if (GameMethods.isLiberalsWon()) {
                holder.playerWinningResult.setText("برنده");
                holder.playerWinningResult.setTextColor(Color.GREEN);
            } else {
                holder.playerWinningResult.setText("بازنده");
                holder.playerWinningResult.setTextColor(Color.RED);
                holder.state.setVisibility(View.INVISIBLE);
            }
        } else {
            if (!GameMethods.isLiberalsWon()) {
                holder.playerWinningResult.setText("برنده");
                holder.playerWinningResult.setTextColor(Color.GREEN);
            } else {
                holder.playerWinningResult.setText("بازنده");
                holder.playerWinningResult.setTextColor(Color.RED);
                holder.state.setVisibility(View.INVISIBLE);
            }
        }
        winningTextViews.add(holder.playerWinningResult);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderResult extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView playerWinningResult;
        ImageView state;

        public MyViewHolderResult(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            playerWinningResult = itemView.findViewById(R.id.winning_textview);
            state = itemView.findViewById(R.id.state);
        }
    }
}
