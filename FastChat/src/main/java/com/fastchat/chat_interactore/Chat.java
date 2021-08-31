package com.fastchat.chat_interactore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.Call;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class Chat {
    private static DatabaseReference mMeasureDatabaseReferences, mTypingReferences, mfcmDatabaseReferences, mUserDatabaseReferences, mMessagesDatabaseReferences, ConversationsDatabaseReferences, CallDatabaseReferences;

    CallbackListener callbackListener;

    public interface CallbackListener {
        public void onSuccess(BaseMessage textMessage);
    }

    private FirebaseFirestore db;
    FirebaseDatabase database;
    public static final String REF_CHAT = "chats";
    public static final String REF_GROUP = "groups";
    public static final String REF_INBOX = "inbox";
    public Chat() {
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        mUserDatabaseReferences = database.getReference().child("users");
        mMessagesDatabaseReferences = database.getReference().child("messages");
        mTypingReferences = database.getReference().child("Typing");
        mMeasureDatabaseReferences = database.getReference().child("measure");
        mfcmDatabaseReferences = database.getReference().child("fcmTokens");
        ConversationsDatabaseReferences = database.getReference().child(BaseMessage.TABLE_CONVERSATIONS);
        CallDatabaseReferences = database.getReference().child("Call");
        mMessagesDatabaseReferences.keepSynced(true);
        ConversationsDatabaseReferences.keepSynced(true);
        mUserDatabaseReferences.keepSynced(true);        //example: userId="9" and myId="5" -->> chat child = "5-9"

    }
;
    public FirebaseFirestore getDb() {
        return db;
    }


    public static String getChatChild(String userId, String receiverId) {
        String[] temp = {userId, receiverId};
        Arrays.sort(temp);
        return temp[0] + "-" + temp[1];
    }

    public void writeMessage(BaseMessage textMessage, CallbackListener callbackListener) {


        DatabaseReference chatRef, inboxRef;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//get firebase instance
        chatRef = firebaseDatabase.getReference(REF_CHAT);//instantiate chat's firebase reference
        inboxRef = firebaseDatabase.getReference(REF_INBOX);//instantiate inbox's firebase reference

        mMessagesDatabaseReferences.child(textMessage.getGroupId()).push().setValue(textMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callbackListener.onSuccess(textMessage);
                if (textMessage instanceof Call) {
                    setConversation(textMessage, textMessage.getUserId(), textMessage.getReceiverId());
                    setConversation(textMessage, textMessage.getReceiverId(), textMessage.getUserId());
                } else {
                    setConversation(textMessage, textMessage.getUserId(), textMessage.getReceiverId());
                    setConversation(textMessage, textMessage.getReceiverId(), textMessage.getUserId());
                }
            }
        });
    }

    public void setConversation(BaseMessage baseMessage, String userId, String a) {
        ConversationsDatabaseReferences.child(userId).child(a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                } else {
                    ConversationsDatabaseReferences.child(userId).child(a).setValue(baseMessage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setCall(BaseMessage user, String userId, String a) {
        ConversationsDatabaseReferences.child(userId).child(a).setValue(user);
    }


    public void updateFile(String groupId,String key, String userId, String a, String url) {
        mMessagesDatabaseReferences.child(groupId).child(key).child("attachment").child("fileUrl").setValue(url);
    }

    public void updateMessages(String groupId,String userId, String receiverId, String key, String KeyUpdate) {
        mMessagesDatabaseReferences.child(groupId).child(key).child(KeyUpdate).setValue(Calendar.getInstance().getTime().getTime());
    }
    public void deleteMessages(String groupId, String key, String KeyUpdate) {
        mMessagesDatabaseReferences.child(groupId).child(key).child(KeyUpdate).setValue(Calendar.getInstance().getTime().getTime());
    }

    public void addReaction(String groupId,String userId, String receiverId, String key, String KeyUpdate, String KeyEmoji, String feeling) {
        mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    String KeyEmoji_ = snapshot.child("KeyEmoji").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    //  mMessagesDatabaseReferences.child(userId).child(receiverId).child(key).child("reactions").child("reaction").child(KeyEmoji_).removeValue();
                    if (KeyEmoji_ != null) {
                        mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("reaction").child(KeyEmoji_).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                    String count = snapshot.child("count").getValue(String.class);
                                    if (count.equals("1")) {
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("count", "1");
                                        map.put("feeling", feeling);
                                        mMessagesDatabaseReferences.child(groupId) .child(key).child("reactions").child("reaction").child(KeyEmoji_).removeValue();
                                        mMessagesDatabaseReferences.child(groupId) .child(key).child("reactions").child("reaction").child(KeyEmoji).setValue(map);
                                        mMessagesDatabaseReferences.child(groupId) .child(key).child("reactions").child("users").child(userId).child("KeyEmoji").setValue(KeyEmoji);
                                    } else {

                                    }

                                } else {


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("count", "1");
                    map.put("feeling", feeling);
                    HashMap<String, String> map_user = new HashMap<>();
                    map_user.put("id", userId);
                    map_user.put("KeyEmoji", KeyEmoji);
                    mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("reaction").child(KeyEmoji).setValue(map);
                    mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("users").child(userId).setValue(map_user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void deleteReaction( String groupId,String userId, String receiverId, String key, String KeyUpdate, String KeyEmoji, String feeling) {
        mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    String KeyEmoji_ = snapshot.child("KeyEmoji").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("reaction").child(KeyEmoji_).removeValue();
                    mMessagesDatabaseReferences.child(groupId).child(key).child("reactions").child("users").child(userId).removeValue();
                    //  mMessagesDatabaseReferences.child(userId).child(receiverId).child(key).child("reactions").child("reaction").child(KeyEmoji_).removeValue();
//                        mMessagesDatabaseReferences.child(userId).child(receiverId).child(key).child("reactions").child("reaction").child(KeyEmoji_).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
//                                    String count = snapshot1.child("count").getValue(String.class);
//
//
//                                }else {
//
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addTyping(String userId, String receiverId, String key, String KeyUpdate, boolean Typing) {
        mTypingReferences.child(userId).child(receiverId).child(KeyUpdate).setValue(Typing);
    }

    public DatabaseReference GetTyping(String userId, String receiverId) {
        return mTypingReferences.child(userId).child(receiverId);
    }

    public void removeReaction(String userId, String receiverId, String key, String KeyUpdate) {
        mMessagesDatabaseReferences.child(userId).child(key).child(KeyUpdate).removeValue();
    }

    public DatabaseReference CheckConversation(String userId, String receiverId) {
       return ConversationsDatabaseReferences.child(userId).child(receiverId);
    }


    public Query getMessages(int page, String groupId, String receiverId) {
        return mMessagesDatabaseReferences.child(groupId).limitToLast(page);
    }

    public DatabaseReference getMessages(String userId, String receiverId) {
        return mMessagesDatabaseReferences.child(userId);
    }
    public Query getLastMessages(String GroupId) {
        return mMessagesDatabaseReferences.child(GroupId).orderByKey().limitToLast(1);
    }

    public Query getConversation(int page, String userId) {
        return ConversationsDatabaseReferences.child(userId).orderByChild("sentAt").limitToFirst(page);
    }

    public void setUser(User user2, String userId) {
        mUserDatabaseReferences.child(userId).setValue(user2);
    }

    public DatabaseReference getUser(String userId) {
        return mUserDatabaseReferences.child(userId);
    }

    public void lastActive(String userId, String KeyUpdate, boolean onlineNow) {
        if (onlineNow) {
            mUserDatabaseReferences.child(userId).child(KeyUpdate).setValue(-1);
        } else {
            mUserDatabaseReferences.child(userId).child(KeyUpdate).setValue(ServerValue.TIMESTAMP);

        }
    }

    public void fcmTokens(String userId, String fcmtoken) {
        mUserDatabaseReferences.child(userId).child("token").setValue(fcmtoken);
    }

    public DatabaseReference getFcmToken(String userId) {
        return mfcmDatabaseReferences.child(userId);
    }

    public static boolean write_today(String userId, String a) {
        new Chat();
        final boolean[] exists = new boolean[1];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String today = sdf.format(Calendar.getInstance().getTime());
        mMessagesDatabaseReferences.child(userId).child(a).orderByChild("sentAt").startAt(today).endAt(today).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    exists[0] = true;
                    Log.d("exists700", "ok");
                } else {
                    exists[0] = false;
                    Log.d("exists700", "no");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return exists[0];
    }


 /*   public static CallManager callManager(){
        new Chat();
       CallManager.getInstance().setDatabaseReference(CallDatabaseReferences,mMessagesDatabaseReferences);
        return   CallManager.getInstance();
    }
*/


    public void ChangeCall(String myuid, String uid, String sessionId, String CALL_STATUS, String time) {
        mMessagesDatabaseReferences.child(myuid).child(uid).orderByChild("sessionId").equalTo(sessionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Log.d("DataSnapshot700", "0");
                    String call = String.valueOf(snapshot1.child("callStatus").getValue());
                    if (call.equals(FastChatConstants.CALL_STATUS_ANSWERED)) {
                        snapshot1.getRef().child("joinedAt").setValue(Calendar.getInstance().getTime().getTime());
                        Log.d("DataSnapshot700", "1");
                    }
                    if (call.equals(FastChatConstants.CALL_STATUS_REJECTED)) {
                        snapshot1.getRef().child("rejectedAt").setValue(Calendar.getInstance().getTime().getTime());
                        Log.d("DataSnapshot700", "2");

                    }
                    if (call.equals(FastChatConstants.CALL_STATUS_ENDED)) {
                        snapshot1.getRef().child("endedAt").setValue(Calendar.getInstance().getTime().getTime());
                        snapshot1.getRef().child("callTime").setValue(time);
                        Log.d("DataSnapshot700", "3");

                    }

                    if (call.equals(FastChatConstants.CALL_STATUS_CANCELLED)) {
                        snapshot1.getRef().child("cancelledAt").setValue(Calendar.getInstance().getTime().getTime());
                        Log.d("DataSnapshot700", "4");

                    }


                    snapshot1.getRef().child("callStatus").setValue(CALL_STATUS);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static DatabaseReference gettMeasure(String userId, String a) {
        new Chat();
        return mMeasureDatabaseReferences.child(userId).child(a);
    }

}
