package com.fastchat.chat_interactore;

import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.User;

public interface NotificationContractor {
    public void sendNotification(String tokenFcm, User user, BaseMessage baseMessage);
}
