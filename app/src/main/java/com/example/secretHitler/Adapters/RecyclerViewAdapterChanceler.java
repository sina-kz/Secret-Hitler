package com.example.secretHitler.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterChanceler extends RecyclerView.Adapter<RecyclerViewAdapterChanceler.MyViewHolderChanceler> {

    private Context mContext;
    private ArrayList<Player> mData;
    private ArrayList<CheckBox> mCheckBoxes;

    public RecyclerViewAdapterChanceler(Context mContext, ArrayList<Player> mData, ArrayList<CheckBox> mCheckBoxes) {
        this.mContext = mContext;
        this.mData = mData;
        this.mCheckBoxes = mCheckBoxes;
    }

    @NonNull
    @Override
    public MyViewHolderChanceler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.chanceler_item, parent, false);
        return new MyViewHolderChanceler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderChanceler holder, final int position) {

        holder.player_name.setText(mData.get(position).getName());
        mCheckBoxes.add(holder.player_checkbox);
        holder.player_checkbox.setChecked(mCheckBoxes.get(position).isChecked());
        holder.player_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (CheckBox checkBox : mCheckBoxes) {
                        checkBox.setChecked(false);
                    }
                    holder.player_checkbox.setChecked(true);
                } else {
                    holder.player_checkbox.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderChanceler extends RecyclerView.ViewHolder {

        TextView player_name;
        CheckBox player_checkbox;


        public MyViewHolderChanceler(@NonNull View itemView) {
            super(itemView);

            player_name = (TextView) itemView.findViewById(R.id.chanceler_text_view);
            player_checkbox = (CheckBox) itemView.findViewById(R.id.chanceler_checkbox);
        }
    }

}
