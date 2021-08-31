//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class Call extends BaseMessage implements Serializable {
    private String sessionId= UUID.randomUUID().toString();
    private String callStatus;
    private String action;
    private String rawData;
    private long initiatedAt= Calendar.getInstance().getTime().getTime();
    private long joinedAt;
    private long rejectedAt;
    private long cancelledAt;
    private long endedAt;
    private long  callTime;
    private AppEntity callInitiator;
    private AppEntity callReceiver;
    public Call(@NonNull String receiverId, String receiverType, String callType) {
        this.receiverId = receiverId;
        this.receiverType = receiverType;
        this.type = callType;
    }

    private Call() {
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCallStatus() {
        return this.callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRawData() {
        return this.rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public long getInitiatedAt() {
        return this.initiatedAt;
    }

    public void setInitiatedAt(long initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public AppEntity getCallInitiator() {
        return this.callInitiator;
    }

    public void setCallInitiator(AppEntity callInitiator) {
        this.callInitiator = callInitiator;
    }

    public AppEntity getCallReceiver() {
        return this.callReceiver;
    }

    public void setCallReceiver(AppEntity callReceiver) {
        this.callReceiver = callReceiver;
    }

    public long getJoinedAt() {
        return this.joinedAt;
    }

    public void setJoinedAt(long joinedAt) {
        this.joinedAt = joinedAt;
    }

    public long getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(long rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public long getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(long cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }


}
