package com.fastchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fastchat.Model.Reaction;
import com.fastchat.R;
import com.fastchat.Ui.Reaction.ReactionBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ReactionLocalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Reaction> reaction = new ArrayList<>();
    Action action;

    public interface Action {
        void getReaction(Reaction reaction);
    }

    public ReactionLocalAdapter(Action action) {
        this.action = action;

    }


    public void addData(List<Reaction> reactions) {
        reaction.addAll(reactions);
        notifyDataSetChanged();

    }

    public void addOnce(Reaction reactions) {
        reaction.add(reactions);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_reaction_layout, parent, false);
        return new ReactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReactionViewHolder reactionViewHolder = (ReactionViewHolder) holder;
        Reaction reaction_ = reaction.get(position);
        Glide.with(holder.itemView.getContext()).load(reaction_.getUrlEmoji()).diskCacheStrategy(DiskCacheStrategy.ALL).into(reactionViewHolder.ivEmoji);
        reactionViewHolder.ivEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.getReaction(reaction_);

            }
        });


    }


    @Override
    public int getItemCount() {
        return reaction.size();
    }

    public class ReactionViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView ivEmoji;

        ReactionViewHolder(@NonNull View view) {
            super(view);
            ivEmoji = view.findViewById(R.id.ivEmoji);
        }
    }
}
