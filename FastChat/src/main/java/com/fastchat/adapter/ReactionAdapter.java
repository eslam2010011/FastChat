package com.fastchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fastchat.Core.FastChat;
import com.fastchat.Model.Reaction;
import com.fastchat.R;
import com.fastchat.widget.Avatar;
import com.fastchat.widget.BadgeCount;

import java.util.ArrayList;
import java.util.List;

public class ReactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Reaction> reaction = new ArrayList<>();

    ReactionLocalAdapter.Action action;

    public interface Action {
        void getReaction(Reaction reaction);
    }

    public ReactionAdapter(ReactionLocalAdapter.Action action) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sb_view_emoji_reaction_component, parent, false);
        return new ReactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReactionViewHolder reactionViewHolder = (ReactionViewHolder) holder;
        Reaction reaction_ = reaction.get(position);
        Glide.with(holder.itemView.getContext()).load(reaction_.getUrlEmoji()).diskCacheStrategy(DiskCacheStrategy.ALL).into(reactionViewHolder.ivEmoji);
        ((ReactionViewHolder) holder).ivEmoji.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                action.getReaction(reaction_);
                reaction.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return reaction.size();
    }

    public class ReactionViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView ivEmoji;
        public TextView tvCount;

        ReactionViewHolder(@NonNull View view) {
            super(view);
            ivEmoji = view.findViewById(R.id.ivEmoji);
            tvCount = view.findViewById(R.id.tvCount);
        }
    }
}
