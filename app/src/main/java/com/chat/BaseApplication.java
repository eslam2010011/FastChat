package com.chat;

import androidx.multidex.MultiDexApplication;

import com.fastchat.Core.FastChat;
import com.fastchat.Core.UiConfig;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.Reaction;
import com.fastchat.Model.User;
import com.fastchat.chat_interactore.ChatInteract;
import com.fastchat.chat_interactore.NotificationContractor;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;



public class BaseApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        //https://emojipedia.org/facebook/

        UiConfig uiConfig = new UiConfig();
        uiConfig.setButtonVideo(true);
        uiConfig.setButtonVoice(true);
        uiConfig.setInfoView(true);
        uiConfig.setBottomView(true);
        uiConfig.setColorGeneral("#3D51FE");
        uiConfig.setBackgroundChat(R.drawable.chat_bg);
        uiConfig.setIncludeReactions(true);
        List<Reaction> list=new ArrayList<>();
        list.add(new Reaction("url_reaction","Key_reaction"));
        uiConfig.setReactions(Reaction.getReactions());
        uiConfig.setReactions(list);
        FastChat.init(this, uiConfig,new NotificationContractorX());


        // FastChat.getFastCache().setChatInteract(new ChatInteract());
        FastChat.getFastChat().getChatInteract().createUser("1", "Eslam");


    }
}
