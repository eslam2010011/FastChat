//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class ReplyMessageG extends BaseMessage {
    private Map<String, Object> replyMessage ;
    int position;
    String text;

    public ReplyMessageG(String receiverId, String text, String receiverType,   int position ) {
        super(receiverId, "reply", receiverType);
         this.position=position;
        this.text=text;

    }



    public ReplyMessageG() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<String, Object> getReplyMessage() {
        return  replyMessage;
    }


    public void setReplyMessage(Map<String, Object> replyMessage) {
        this.replyMessage = replyMessage;
    }
}
