//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

public class LinkMessage extends BaseMessage {
    private String text;

    public LinkMessage(String receiverId, String receiverType, String text) {
        super(receiverId, "link", receiverType);
        this.text = text;
    }

    public LinkMessage() {
    }
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }








}
