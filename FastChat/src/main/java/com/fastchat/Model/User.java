package com.fastchat.Model;

import android.os.Parcel;
import android.os.Parcelable;
 import android.util.Log;

import androidx.annotation.NonNull;

import com.fastchat.chat_interactore.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class User implements Parcelable {

    private String userName;
    private String userId;
    private String userProfile;
    private String userEmail;
    private String UserDisplayName;
    private Long lastSeenAt;
    private boolean isActive = true;
    String token;

    public User() {
    }


    public User(String userId) {
        this.userId = userId;
    }

    public User(String userName, String userId, String userProfile, String userEmail) {
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
        this.userEmail = userEmail;
    }

    public User(String userName, String userId, String userProfile, String userEmail,String token) {
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
        this.userEmail = userEmail;
        this.token=token;
    }

    public HashMap<String, Object> build(String userName, String userId, String userProfile, String userEmail, boolean isActive, boolean isOnline, long lastSeenAt) {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("userId", userId);
        obj.put("userName", userName);
        obj.put("userProfile", userProfile);
        obj.put("userEmail", userEmail);
        obj.put("isActive", isActive);
        obj.put("isOnline", isOnline);
        obj.put("lastSeenAt", lastSeenAt);
        return obj;
    }

    User(HashMap<String, Object> obj) {
        if (obj.containsKey("userId")) {
            this.userId = obj.get("userId").toString();
        }
    }

    public String getUserDisplayName() {
        return UserDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        UserDisplayName = userDisplayName;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    private User(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        userProfile = in.readString();
        userEmail = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public Long getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Long lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(userProfile);
        dest.writeString(userEmail);
    }
    public static User getUserFromId(final String userId) {
        final User user = new User();
        new Chat().getUser(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1=snapshot.getValue(User.class);
                if (user1 != null) {
                    user.setUserId(user1.getUserId());
                }
                if (user1 != null) {
                    user.setUserName(user1.getUserName());
                }
                if (user1 != null) {
                    user.setUserProfile(user1.getUserProfile());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
