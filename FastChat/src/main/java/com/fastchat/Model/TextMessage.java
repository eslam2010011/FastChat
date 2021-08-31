//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

public class TextMessage extends BaseMessage {
    private String text;

    public TextMessage(String receiverId, String receiverType, String text) {
        super(receiverId, "text", receiverType);
        this.text = text;
    }



    public TextMessage() {
    }
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }








}
