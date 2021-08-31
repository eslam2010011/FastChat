//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class MessageReceipt {
    public static final String RECEIPT_TYPE_DELIVERED = "delivered";
    public static final String RECEIPT_TYPE_READ = "read";
    private int messageId;
    private User sender;
    private String receivertype;
    private String receiverId;
    private long timestamp;
    private String receiptType;
    private long deliveredAt;
    private long readAt;

    public MessageReceipt() {
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getReceivertype() {
        return this.receivertype;
    }

    public void setReceivertype(String receivertype) {
        this.receivertype = receivertype;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiptType() {
        return this.receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
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

    public static List<MessageReceipt> receiptsFromJSON(JSONArray receiptsArray, String receiverId, String receivertype, int messageId) throws JSONException {
        List<MessageReceipt> receiptsList = new ArrayList();

        for(int i = 0; i < receiptsArray.length(); ++i) {
            MessageReceipt messageReceipt = new MessageReceipt();
            messageReceipt.setReceivertype(receivertype);
            messageReceipt.setReceiverId(receiverId);
            messageReceipt.setMessageId(messageId);
            JSONObject receiptObject = receiptsArray.getJSONObject(i);
            if (receiptObject.has("recipient")) {
               // messageReceipt.setSender(User.fromJson(receiptObject.getJSONObject("recipient").toString()));
            } else {
             //   messageReceipt.setSender(CometChat.getLoggedInUser());
            }

            if (receiptObject.has("deliveredAt")) {
                messageReceipt.setReceiptType("delivered");
                messageReceipt.setTimestamp(receiptObject.getLong("deliveredAt"));
                messageReceipt.setDeliveredAt(receiptObject.getLong("deliveredAt"));
            }

            if (receiptObject.has("readAt")) {
                messageReceipt.setReceiptType("read");
                messageReceipt.setTimestamp(receiptObject.getLong("readAt"));
                messageReceipt.setReadAt(receiptObject.getLong("readAt"));
            }

            receiptsList.add(messageReceipt);
        }

        return receiptsList;
    }

    public String toString() {
        return "MessageReceipt{messageId=" + this.messageId + ", sender=" + this.sender + ", receivertype='" + this.receivertype + '\'' + ", receiverId='" + this.receiverId + '\'' + ", timestamp=" + this.timestamp + ", receiptType='" + this.receiptType + '\'' + ", deliveredAt=" + this.deliveredAt + ", readAt=" + this.readAt + '}';
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ReceiptType {
    }
}
