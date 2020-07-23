package com.example.secretHitler.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Player> mData;

    public RecyclerViewAdapter(Context mContext, List<Player> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.playerName.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView playerName;
        ImageView imageButton;
        ImageView imageEdit;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = (TextView) itemView.findViewById(R.id.name_text);
            imageButton = (ImageView) itemView.findViewById(R.id.delete_button);
            imageEdit = (ImageView) itemView.findViewById(R.id.edit_button);
            cardView = (CardView) itemView.findViewById(R.id.cardView_id);
        }
    }
}
