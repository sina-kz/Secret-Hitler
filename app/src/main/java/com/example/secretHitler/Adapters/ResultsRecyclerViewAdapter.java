package com.example.secretHitler.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        System.out.println(position + mData.get(position).getName() + mData.get(position).isWon());
        if (mData.get(position).getTeam() == Team.LIBERAL) {
            if(GameMethods.isLiberalsWon()) {
                holder.playerWinningResult.setText("برنده");
                holder.playerWinningResult.setTextColor(Color.GREEN);
            }
            else {
                holder.playerWinningResult.setText("بازنده");
                holder.playerWinningResult.setTextColor(Color.RED);
            }
        } else {
            if(!GameMethods.isLiberalsWon()) {
                holder.playerWinningResult.setText("برنده");
                holder.playerWinningResult.setTextColor(Color.GREEN);
            }
            else {
                holder.playerWinningResult.setText("بازنده");
                holder.playerWinningResult.setTextColor(Color.RED);
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

        public MyViewHolderResult(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            playerWinningResult = itemView.findViewById(R.id.winning_textview);
        }
    }
}
