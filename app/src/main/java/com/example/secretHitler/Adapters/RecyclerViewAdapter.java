package com.example.secretHitler.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secretHitler.Models.Player;
import com.example.secretHitler.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Player> mData;
    private Dialog myDialog;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.playerName.setText(mData.get(position).getName());
        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.dialog_edit);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final EditText dialog_text = (EditText) myDialog.findViewById(R.id.dialog_edit_text);
                Button dialog_button = (Button) myDialog.findViewById(R.id.done);
                dialog_text.setText(mData.get(holder.getAdapterPosition()).getName());
                myDialog.show();
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean flag = true;
                        for (Player player : mData) {
                            if (player.getName().equals(dialog_text.getText().toString())) {
                                Toast.makeText(mContext, "نام بازیکن قبلا وارد شده است", Toast.LENGTH_SHORT).show();
                                flag = false;
                            }
                        }
                        if (flag) {
                            mData.get(holder.getAdapterPosition()).setName(dialog_text.getText().toString());
                            notifyDataSetChanged();
                            myDialog.dismiss();
                        }
                    }
                });
            }
        });
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
                Button button = (Button) ((Activity) mContext).findViewById(R.id.addButton);
                Button startRoleButton = (Button) ((Activity) mContext).findViewById(R.id.start_showing_role);
                button.setEnabled(true);
                startRoleButton.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView playerName;
        ImageView imageDelete;
        ImageView imageEdit;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = (TextView) itemView.findViewById(R.id.name_text);
            imageDelete = (ImageView) itemView.findViewById(R.id.delete_button);
            imageEdit = (ImageView) itemView.findViewById(R.id.edit_button);
            cardView = (CardView) itemView.findViewById(R.id.cardView_id);
        }
    }


    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, mData.size());
    }
}
