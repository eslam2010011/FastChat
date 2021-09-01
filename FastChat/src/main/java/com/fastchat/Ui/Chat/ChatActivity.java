package com.fastchat.Ui.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fastchat.Core.FastChat;
import com.fastchat.R;

public class ChatActivity extends AppCompatActivity {
    fragmentChat fragment = new fragmentChat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainvmv);
        Bundle bundle = new Bundle();
        bundle.putString("userId", getIntent().getStringExtra("userId"));
        bundle.putString("receiverId", getIntent().getStringExtra("receiverId"));
        bundle.putString("groupId", getIntent().getStringExtra("groupId"));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();


    }



    @Override
    protected void onStart() {
        super.onStart();
        FastChat.getFastChat().getChatInteract().lastActive(getIntent().getStringExtra("userId"), true);
    }

    @Override
    public void onBackPressed() {
        if (FastChat.getUiConfig().getOnChatListener() != null) {
            FastChat.getUiConfig().getOnChatListener().onBackPressed();
            ;

        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FastChat.getFastChat().getChatInteract().lastActive(getIntent().getStringExtra("userId"), false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // FastPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}