//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

public class CustomMessage extends BaseMessage {
    private String subType;
    private JSONObject customData;

    public CustomMessage(String receiverUid, String receiverType, String customType, @NonNull JSONObject customData) {
        super(receiverUid, customType, receiverType);
        this.setCategory("custom");
        this.customData = customData;
    }
    private CustomMessage() {
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public JSONObject getCustomData() {
        return this.customData;
    }

    public void setCustomData(JSONObject customData) {
        this.customData = customData;
    }



    public int hashCode() {
        return this.id;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof CustomMessage)) {
            return false;
        } else {
            CustomMessage a = (CustomMessage)obj;
            return a.getId() == this.id;
        }
    }
}
