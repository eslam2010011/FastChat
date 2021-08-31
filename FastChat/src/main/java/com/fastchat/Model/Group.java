//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Group extends AppEntity {
    private String guid;
    private String name;
    private String type;
    private String password;
    private String icon;
    private String description;
    private String owner;
    private JSONObject metadata;
    private long createdAt;
    private long updatedAt;
    private boolean hasJoined;
    private long joinedAt = -1L;
    private String scope;
    private int membersCount = 0;

    public Group(String guid, String name, String groupType, String password) {
        this.guid = guid;
        this.name = name;
        this.type = groupType;
        this.password = password;
    }

    public Group(String guid, String name, String groupType, String password, String icon, String description) {
        this.guid = guid;
        this.name = name;
        this.type = groupType;
        this.password = password;
        this.icon = icon;
        this.description = description;
    }

    public Group() {
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroupType() {
        return this.type;
    }

    public void setGroupType(String groupType) {
        this.type = groupType;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getMetadata() {
        return this.metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isJoined() {
        return this.hasJoined;
    }

    public void setHasJoined(boolean hasJoined) {
        this.hasJoined = hasJoined;
    }

    public long getJoinedAt() {
        return this.joinedAt;
    }

    public void setJoinedAt(long joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getMembersCount() {
        return this.membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap();
        if (this.guid != null) {
            map.put("guid", this.guid);
        }

        if (this.name != null) {
            map.put("name", this.name);
        }

        if (this.icon != null) {
            map.put("icon", this.icon);
        }

        if (this.description != null) {
            map.put("description", this.description);
        }

        if (this.owner != null) {
            map.put("owner", this.owner);
        }

        if (this.type != null) {
            map.put("type", this.type);
        }

        if (this.type != null && this.type.equalsIgnoreCase("password") && this.password != null && !TextUtils.isEmpty(this.password)) {
            map.put("password", this.password);
        }

        if (this.metadata != null) {
            map.put("metadata", this.metadata.toString());
        }

        if (this.createdAt != -1L) {
            map.put("createdAt", String.valueOf(this.createdAt));
        }

        if (this.updatedAt != -1L) {
            map.put("updatedAt", String.valueOf(this.updatedAt));
        }

        if (this.getJoinedAt() != -1L) {
            map.put("joinedAt", String.valueOf(this.joinedAt));
        }

        if (this.scope != null) {
            map.put("scope", this.scope);
        }

        map.put("hasJoined", String.valueOf(this.hasJoined ? 1 : 0));
        return map;
    }

    public static Group fromJson(String json) {
        Group group = new Group();

        try {
            JSONObject groupObject = new JSONObject(json);
            if (groupObject.has("guid")) {
                group.setGuid(groupObject.getString("guid"));
            }

            if (groupObject.has("name")) {
                group.setName(groupObject.getString("name"));
            }

            if (groupObject.has("icon")) {
                group.setIcon(groupObject.getString("icon"));
            }

            if (groupObject.has("description")) {
                group.setDescription(groupObject.getString("description"));
            }

            if (groupObject.has("owner")) {
                group.setOwner(groupObject.getString("owner"));
            }

            if (groupObject.has("type")) {
                group.setGroupType(groupObject.getString("type"));
            }

            if (groupObject.has("password")) {
                group.setPassword(groupObject.getString("password"));
            }

            if (groupObject.has("metadata")) {
                group.setMetadata(groupObject.getJSONObject("metadata"));
            }

            if (groupObject.has("createdAt")) {
                group.setCreatedAt(groupObject.getLong("createdAt"));
            }

            if (groupObject.has("updatedAt")) {
                group.setUpdatedAt(groupObject.getLong("updatedAt"));
            }

            if (groupObject.has("hasJoined")) {
                group.setHasJoined(groupObject.getBoolean("hasJoined"));
            }

            if (groupObject.has("scope")) {
                group.setScope(groupObject.getString("scope"));
            }

            if (groupObject.has("joinedAt")) {
                group.setJoinedAt(groupObject.getLong("joinedAt"));
            }

            if (groupObject.has("membersCount")) {
                group.setMembersCount(groupObject.getInt("membersCount"));
            }

            return group;
        } catch (JSONException var3) {
            var3.printStackTrace();
            return group;
        }
    }

    public static List<Group> fromJSONArray(String response) throws JSONException {
        List<Group> groups = new ArrayList();
        JSONObject mainObject = new JSONObject(response);
        if (mainObject.has("data")) {
            JSONArray dataArray = mainObject.getJSONArray("data");

            for(int i = 0; i < dataArray.length(); ++i) {
                Group group = fromJson(dataArray.getJSONObject(i).toString());
                groups.add(group);
            }
        }

        return groups;
    }

    public String toString() {
        return "Group{guid='" + this.guid + '\'' + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", password='" + this.password + '\'' + ", icon='" + this.icon + '\'' + ", description='" + this.description + '\'' + ", owner='" + this.owner + '\'' + ", metadata=" + this.metadata + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ", hasJoined=" + this.hasJoined + ", joinedAt=" + this.joinedAt + ", scope='" + this.scope + '\'' + ", membersCount=" + this.membersCount + '}';
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.guid});
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof Group)) {
            return false;
        } else {
            Group group = (Group)obj;
            return group.getGuid().equalsIgnoreCase(this.guid);
        }
    }
}
