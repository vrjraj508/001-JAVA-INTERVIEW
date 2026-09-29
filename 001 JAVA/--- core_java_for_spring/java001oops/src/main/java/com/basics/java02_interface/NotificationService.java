package com.basics.java02_interface;

public interface NotificationService {
    void sendNotification(String userId,String message);
    boolean isAvailable();
    String getChannelName();
}


