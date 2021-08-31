//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Action extends BaseMessage {
    private static final String TAG = Action.class.getSimpleName();
    private AppEntity actioBy;
    private AppEntity actionFor;
    private AppEntity actionOn;
    private String message;
    private String rawData;
    private String action;
    private String oldScope;
    private String newScope;

    public Action() {
    }

    public AppEntity getActioBy() {
        return this.actioBy;
    }

    public void setActioBy(AppEntity actioBy) {
        this.actioBy = actioBy;
    }

    public AppEntity getActionFor() {
        return this.actionFor;
    }

    public void setActionFor(AppEntity actionFor) {
        this.actionFor = actionFor;
    }

    public AppEntity getActionOn() {
        return this.actionOn;
    }

    public void setActionOn(AppEntity actionOn) {
        this.actionOn = actionOn;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRawData() {
        return this.rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldScope() {
        return this.oldScope;
    }

    public void setOldScope(String oldScope) {
        this.oldScope = oldScope;
    }

    public String getNewScope() {
        return this.newScope;
    }

    public void setNewScope(String newScope) {
        this.newScope = newScope;
    }


}
