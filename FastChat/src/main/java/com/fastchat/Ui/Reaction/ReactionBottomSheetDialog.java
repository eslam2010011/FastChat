package com.fastchat.Ui.Reaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastchat.Core.FastChat;
import com.fastchat.Model.Reaction;
import com.fastchat.R;
import com.fastchat.adapter.ReactionLocalAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ReactionBottomSheetDialog extends BottomSheetDialog {
    RecyclerView recyclerView_reaction;
    Button Delete_Message;
    public Action action;

    public interface Action {
        void getReaction(Reaction reaction);
        void deleteMessage(Button button);

    }

    public ReactionBottomSheetDialog(@NonNull Context context, Action action) {
        super(context, R.style.SheetDialog);
        this.action = action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_emoji_reaction);
        init();
    }

    public void init() {
        ReactionLocalAdapter reactionLocalAdapter = new ReactionLocalAdapter(new ReactionLocalAdapter.Action() {
            @Override
            public void getReaction(Reaction reaction) {
                action.getReaction(reaction);
            }
        });
        reactionLocalAdapter.addData(FastChat.getUiConfig().getReactions());
        recyclerView_reaction = findViewById(R.id.recyclerView_reaction);
        Delete_Message = findViewById(R.id.Delete_Message);

        recyclerView_reaction.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView_reaction.setAdapter(reactionLocalAdapter);

        Delete_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.deleteMessage(Delete_Message);
            }
        });
    }
}
