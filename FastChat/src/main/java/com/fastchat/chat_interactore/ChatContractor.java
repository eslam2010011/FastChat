package com.fastchat.chat_interactore;import com.fastchat.Model.BaseMessage;import com.fastchat.Model.User;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.Query;import java.io.File;public interface ChatContractor {    public void sendMediaMessage(String GroupId, File file, String filetype, String receiverId, String userId, Chat.CallbackListener callbackListener);    public void sendMessage(String GroupId, String message, String receiverId, String userId, Chat.CallbackListener callbackListener);    public void sendMessageLink(String GroupId, String message, String receiverId, String userId, Chat.CallbackListener callbackListener);    public void sendMessageCall(String GroupId, String callType, String receiverId, String userId, Chat.CallbackListener callbackListener);    public void sendReplyMessage(String GroupId, BaseMessage baseMessage, int position, String text, String receiverId, String userId, Chat.CallbackListener callbackListener);    public void changeCall(String userId, String receiverId, String sessionId, String CALL_STATUS, String time);    public void UpdateStatusMessage(String groupId, String userId, String receiverId, String messageId, String keyUpdate);    public Query GetMessage(int page, String userId, String receiverId);    public Query GetConversation(int page, String userId);    public DatabaseReference CheckConversation(String userId, String receiverId);    public void createUser(String userId, String name);    public void lastActive(String userId, boolean onlineNow);    public void fcmTokens(String userId, String fcmTokens);    public DatabaseReference getFcmToken(String userId);    public void createUser(User user);    public DatabaseReference getUser(String userId);    public void addReaction(String groupId, String userId, String receiverId, String key, String KeyUpdate, String KeyEmoji, String feeling);    public void deleteReaction(String groupId, String userId, String receiverId, String key, String KeyUpdate, String KeyEmoji, String feeling);    public void addTyping(String userId, String receiverId, String key, String KeyUpdate, boolean Typing);    public DatabaseReference GetTyping(String userId, String receiverId);}