//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.Nullable;

public class MediaMessage extends BaseMessage {
     private String caption;
    private Attachment attachment;

    public MediaMessage(String receiverId, String type, String receiverType) {
        super(receiverId, type, receiverType);
    }

    public MediaMessage(String caption, Attachment attachment) {
        this.caption = caption;
        this.attachment = attachment;
    }

    public MediaMessage() {
    }



    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }


    public int hashCode() {
        return this.id;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof MediaMessage)) {
            return false;
        } else {
            MediaMessage a = (MediaMessage)obj;
            return a.getId() == this.id;
        }
    }
}
