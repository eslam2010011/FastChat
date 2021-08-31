package com.fastchat.Core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.fastchat.Model.User;
import com.fastchat.Ui.Chat.ChatActivity;
import com.fastchat.Ui.Conversation.FragmentConversation;
import com.fastchat.adapter.ConversationListAdapter;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.chat_interactore.ChatContractor;
import com.fastchat.chat_interactore.ChatInteract;
import com.fastchat.chat_interactore.NotificationContractor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

public class FastChat {
    static public Context context;
    private static FastChat mFastChat;
    static public UiConfig uiConfig;
    static public ChatContractor chatInteract;
    static public NotificationContractor notificationContractor;

    /**
     * @param context
     * @param uiConfig
     * @param notificationContractor
     */

    public static void init(Context context, UiConfig uiConfig, NotificationContractor notificationContractor) {
        init(context, uiConfig, new ChatInteract(), notificationContractor);
    }

    public static void init(Context context, UiConfig uiConfig, ChatContractor chatInteract, NotificationContractor notificationContractor) {
        FastChat.context = context;
        FastChat.uiConfig = uiConfig;
        FastChat.chatInteract = chatInteract;
        FastChat.notificationContractor = notificationContractor;
        FirebaseApp.initializeApp(context);
        EmojiManager.install(new TwitterEmojiProvider());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .build());

    }


    public static FastChat getFastChat() {
        if (mFastChat == null) {
            mFastChat = new FastChat();
        }

        return mFastChat;
    }


    public FastChat() {

    }


    /**
     * @param context
     * @param userId     this user  id
     * @param receiverId this receiver Id
     * @param groupId    put {groupId} null if this first Conversation
     */
    public static void showChatActivity(Context context, String userId, String receiverId, String groupId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("receiverId", receiverId);
        intent.putExtra("groupId", Chat.getChatChild(userId, receiverId));
        context.startActivity(intent);
    }

  /*  public static Intent ChatActivityIntent(Context context, String userId, String receiverId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("receiverId", receiverId);
        return intent;
    }
*/
    public static FragmentConversation showConversationFragment(String userId) {
        FragmentConversation fragmentConversation = new FragmentConversation();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragmentConversation.setArguments(bundle);
        return fragmentConversation;
    }


    public ChatContractor getChatInteract() {
        return chatInteract;
    }

    public NotificationContractor getNotificationContractor() {
        return notificationContractor;
    }

    public static UiConfig getUiConfig() {
        return uiConfig;
    }


    public static Context getContext() {
        return context;
    }

}
