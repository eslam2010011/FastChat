package com.fastchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fastchat.Core.FastChat;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.User;
import com.fastchat.R;
import com.fastchat.Ui.Chat.ChatActivity;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.utils.TypingStat;
import com.fastchat.utils.Utils;
import com.fastchat.widget.Avatar;
import com.fastchat.widget.BadgeCount;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ConversationListAdapter extends FirebaseRecyclerAdapter<BaseMessage, RecyclerView.ViewHolder> {

    private Context context;
    String userId;

    public ConversationListAdapter(Context context, FirebaseRecyclerOptions<BaseMessage> options, String userId) {
        super(options);
        this.userId = userId;
        this.context = context;
    }


    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_list_row, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BaseMessage baseMessage) {
        String key = getRef(position).getKey();
        ConversationViewHolder conversationViewHolder = (ConversationViewHolder) holder;
        String lastMessageText = null;
        String type = null;
        String category = null;
        if (baseMessage != null) {
            type = baseMessage.getType();
            category = baseMessage.getCategory();
            conversationViewHolder.messageTime.setVisibility(View.VISIBLE);
        } else {
            lastMessageText = "tap_to_start_conversation";//context.getResources().getString(R.string.tap_to_start_conversation);
            conversationViewHolder.txtUserMessage.setMarqueeRepeatLimit(100);
            conversationViewHolder.txtUserMessage.setHorizontallyScrolling(true);
            conversationViewHolder.txtUserMessage.setSingleLine(true);
            conversationViewHolder.messageTime.setVisibility(View.GONE);
        }
        conversationViewHolder.conversationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FastChat.showChatActivity(view.getContext(), userId, key, baseMessage.getGroupId());
            }
        });
        new Chat().getLastMessages(baseMessage.getGroupId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaseMessage baseMessage1 = dataSnapshot.getValue(BaseMessage.class);
                    String text = dataSnapshot.child("text").getValue(String.class);
                    conversationViewHolder.txtUserMessage.setText(Utils.getLastMessage(baseMessage1, userId, text));
                    //setStatusIcon(conversationViewHolder.messageTime, baseMessage1);
                    conversationViewHolder.messageTime.setText(Utils.getLastMessageDate(baseMessage1.getSentAt()));
                    Log.d("BaseMessage", baseMessage1.getCategory() + "  " + baseMessage1.getReceiverId() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FastChat.getFastChat().getChatInteract().getUser(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                if ((user1 != null ? user1.getUserName() : null) != null) {
                    ((ConversationViewHolder) holder).avUser.setInitials(user1.getUserName());
                    conversationViewHolder.txtUserName.setText(user1.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (baseMessage.getUserId().equals(userId)) {
            FastChat.getFastChat().getChatInteract().GetTyping(baseMessage.getReceiverId(), baseMessage.getUserId()).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("IsTyping")) {
                        boolean IsTyping = dataSnapshot.child("IsTyping").getValue(boolean.class);
                        if (IsTyping) {
                            conversationViewHolder.txtUserMessage.setText(TypingStat.getStatString(conversationViewHolder.itemView.getContext(), 1));
                            //  tv_tiem.setText(TypingStat.getStatString(getActivity(), 1));
                            //  tv_tiem.setTextColor(R.color.colorPrimary);
                            //  LinearLayoutTyping.setVisibility(View.VISIBLE);
                        } else {
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            FastChat.getFastChat().getChatInteract().GetTyping(baseMessage.getUserId(), baseMessage.getReceiverId()).addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("IsTyping")) {
                        boolean IsTyping = dataSnapshot.child("IsTyping").getValue(boolean.class);
                        if (IsTyping) {
                            conversationViewHolder.txtUserMessage.setText("typing...");
                            //  tv_tiem.setText(TypingStat.getStatString(getActivity(), 1));
                            //  tv_tiem.setTextColor(R.color.colorPrimary);
                            //  LinearLayoutTyping.setVisibility(View.VISIBLE);
                        } else {
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        conversationViewHolder.avUser.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    }

    private void setStatusIcon(TextView txtTime, BaseMessage baseMessage1) {
        if (baseMessage1.getUserId().equals(userId)) {
            if (baseMessage1.getReadAt() != 0) {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getReadAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_double_tick, 0);
                txtTime.setCompoundDrawablePadding(10);
            } else if (baseMessage1.getDeliveredAt() != 0) {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getDeliveredAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_all_black_24dp, 0);
                txtTime.setCompoundDrawablePadding(10);
            } else {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getSentAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                txtTime.setCompoundDrawablePadding(10);
            }
        } else {
            txtTime.setText(Utils.getHeaderDate(baseMessage1.getSentAt()));
        }
    }


    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        private TextView txtUserMessage;
        public TextView txtUserName;
        public TextView tvUser;
        private Avatar avUser;
        private RelativeLayout conversationView;
        public TextView messageTime;
        public TextView tvSeprator;
        BadgeCount messageCount;

        ConversationViewHolder(@NonNull View view) {
            super(view);
            tvSeprator = view.findViewById(R.id.tvSeprator);
            tvUser = view.findViewById(R.id.tv_user);
            txtUserMessage = view.findViewById(R.id.txt_user_message);
            txtUserName = view.findViewById(R.id.txt_user_name);
            messageTime = view.findViewById(R.id.messageTime);
            messageCount = view.findViewById(R.id.messageCount);
            avUser = view.findViewById(R.id.av_user);
            conversationView = view.findViewById(R.id.conversationView);
        }
    }
}
