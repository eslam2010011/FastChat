package com.fastchat.Model;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reaction {

    public static List<Reaction> reactions = new ArrayList<>();
    private String UrlEmoji;
    private String KeyEmoji;
    List<String> userIds=new ArrayList<>();
    private int count;

    public Reaction(@Nullable String UrlEmoji, String KeyEmoji) {
        this.UrlEmoji = UrlEmoji;
        this.KeyEmoji = KeyEmoji;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getUrlEmoji() {
        return UrlEmoji;
    }

    public void setUrlEmoji(String urlEmoji) {
        UrlEmoji = urlEmoji;
    }

    public String getKeyEmoji() {
        return KeyEmoji;
    }

    public void setKeyEmoji(String keyEmoji) {
        KeyEmoji = keyEmoji;
    }

    public static List<Reaction> getReactions() {
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/grinning-face_1f600.png","Grinning_Face"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/face-with-tears-of-joy_1f602.png","Face_with_Tears"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/smiling-face-with-smiling-eyes_1f60a.png","Smiling_Face"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/grimacing-face_1f62c.png","Grimacing_Face"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/loudly-crying-face_1f62d.png"," Loudly_Crying"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/thumbs-up-sign_1f44d.png","Like"));
        reactions.add(new Reaction("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/facebook/65/thumbs-down-sign_1f44e.png","disLike"));
        return reactions;
    }




}