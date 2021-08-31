package com.fastchat.Core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.Reaction;
import com.fastchat.Model.User;

import java.util.List;

public class UiConfig {

    private ChatListener.OnChatListener onChatListener;

    public boolean ButtonVideo = true;

    public boolean ButtonVoice = true;

    public boolean BottomView = true;

    public boolean InfoView = true;

    public boolean IncludeReactions = true;

    public boolean IncludeEmoji = false;

    public boolean IncludeVideo = false;



    public String colorGeneral = null;

    public int colorRightMeMessage;

    public int colorRightOtherMessage;


    public int backgroundChat = -1;

    public List<Reaction> reactions = null;

    public void setButtonVideo(boolean buttonVideo) {
        ButtonVideo = buttonVideo;
    }


    public void setButtonVoice(boolean buttonVoice) {
        ButtonVoice = buttonVoice;
    }


    public void setBottomView(boolean bottomView) {
        BottomView = bottomView;
    }


    public void setInfoView(boolean infoView) {
        InfoView = infoView;
    }


    public void setColorGeneral(String colorGeneral) {
        this.colorGeneral = colorGeneral;
    }

    public boolean isIncludeEmoji() {
        return IncludeEmoji;
    }

    public void setIncludeEmoji(boolean includeEmoji) {
        IncludeEmoji = includeEmoji;
    }

    public boolean isIncludeVideo() {
        return IncludeVideo;
    }

    public void setIncludeVideo(boolean includeVideo) {
        IncludeVideo = includeVideo;
    }

    public void setColorRightMeMessage(int colorRightMeMessage) {
        this.colorRightMeMessage = colorRightMeMessage;
    }


    public void setColorRightOtherMessage(int colorRightOtherMessage) {
        this.colorRightOtherMessage = colorRightOtherMessage;
    }


    public void setBackgroundChat(int backgroundChat) {
        this.backgroundChat = backgroundChat;
    }

    public int getBackgroundChat() {
        return backgroundChat;
    }

    public boolean isButtonVideo() {
        return ButtonVideo;
    }

    public boolean isButtonVoice() {
        return ButtonVoice;
    }

    public boolean isBottomView() {
        return BottomView;
    }

    public boolean isInfoView() {
        return InfoView;
    }

    public boolean isIncludeReactions() {
        return IncludeReactions;
    }

    public void setIncludeReactions(boolean includeReactions) {
        IncludeReactions = includeReactions;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public String getColorGeneral() {
        return colorGeneral;
    }

    public int getColorRightMeMessage() {
        return colorRightMeMessage;
    }

    public int getColorRightOtherMessage() {
        return colorRightOtherMessage;
    }

    public interface ChatListener {
        interface OnChatListener {

            void onBackPressed();

            void onVideo(User user, BaseMessage baseMessage);

            void onVoice(User user, BaseMessage baseMessage);

        }


    }

    public UiConfig setChatListener(@NonNull ChatListener.OnChatListener onChatListener) {
        this.onChatListener = onChatListener;
        return this;
    }


    @Nullable
    public ChatListener.OnChatListener getOnChatListener() {
        return onChatListener;
    }

}
