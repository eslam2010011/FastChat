package com.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fastchat.Core.FastChat;
import com.fastchat.Core.UiConfig;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Ui.Chat.fragmentChat;
import com.fastchat.Ui.Conversation.FragmentConversation;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.widget.audio_record_view.AudioRecordView;

import java.util.List;


public class MainActivity extends AppCompatActivity  {
    FragmentConversation fragment = new FragmentConversation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       getSupportFragmentManager().beginTransaction().replace(R.id.layout, FastChat.showConversationFragment("2")).commit();

    }



}