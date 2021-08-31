//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.Nullable;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BaseMessage extends AppEntity implements Serializable {
    public static final String TABLE_CONVERSATIONS = "Conversations";
    protected int id = (new Random()).nextInt((1000000 - 1) + 1) + 1;
    ;
    protected String userId;
    protected String receiverId;
    protected String type;
    protected String receiverType;
    protected String category;
    protected long sentAt = Calendar.getInstance().getTime().getTime();
    protected long deliveredAt;
    protected long readAt;
    protected JSONObject metadata;
    protected long readByMeAt;
    protected long deliveredToMeAt;
    protected long deletedAt;
    protected long editedAt;
    protected String deletedBy;
    protected String editedBy;
    protected long updatedAt;
    protected int parentMessageId;
    protected int replyCount;
    protected String groupId;
    protected Map<String, Object> reactions;

    public BaseMessage(String receiverId, String type, String receiverType) {
        this.receiverId = receiverId;
        this.type = type;
        this.receiverType = receiverType;
    }


    public BaseMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getId() {
        return this.id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverType() {
        return this.receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public long getSentAt() {
        return this.sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDeliveredAt() {
        return this.deliveredAt;
    }

    public void setDeliveredAt(long deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public long getReadAt() {
        return this.readAt;
    }

    public void setReadAt(long readAt) {
        this.readAt = readAt;
    }

    public JSONObject getMetadata() {
        return this.metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public long getReadByMeAt() {
        return this.readByMeAt;
    }

    public void setReadByMeAt(long readByMeAt) {
        this.readByMeAt = readByMeAt;
    }

    public long getDeliveredToMeAt() {
        return this.deliveredToMeAt;
    }

    public void setDeliveredToMeAt(long deliveredToMeAt) {
        this.deliveredToMeAt = deliveredToMeAt;
    }

    public long getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getEditedAt() {
        return this.editedAt;
    }

    public void setEditedAt(long editedAt) {
        this.editedAt = editedAt;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getEditedBy() {
        return this.editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }


    public int getParentMessageId() {
        return this.parentMessageId;
    }

    public void setParentMessageId(int parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public int getReplyCount() {
        return this.replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public Map<String,Object> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Object> reactions) {
        this.reactions = reactions;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof BaseMessage)) {
            return false;
        } else {
            BaseMessage a = (BaseMessage) obj;
            return a.getId() == this.id;
        }
    }
}
