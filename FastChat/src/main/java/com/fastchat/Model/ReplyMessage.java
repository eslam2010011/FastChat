//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

public class ReplyMessage extends BaseMessage {
    private BaseMessage replyMessage ;
    int position;
    String text;

    public ReplyMessage(String receiverId, String text,String receiverType, BaseMessage replyMessage ,int position ) {
        super(receiverId, "reply", receiverType);
        this.replyMessage  = replyMessage ;
        this.position=position;
        this.text=text;

    }



    public ReplyMessage() {
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

    public BaseMessage getReplyMessage() {
        return  replyMessage;
    }


    public void setReplyMessage(BaseMessage replyMessage) {
        this.replyMessage = replyMessage;
    }
}
