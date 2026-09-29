package com.basics.java02_interface;

class EmailNotificationService implements NotificationService{
    private String smtpServer;

    public EmailNotificationService(String smtpServer){
        this.smtpServer =smtpServer;
    }


    @Override
    public void sendNotification(String userId, String message) {
        System.out.println("Email Connecting to SMTP..."+smtpServer);
        System.out.println("Email Sending to User ..."+message);
        System.out.println("Email Message Delivered ...");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }
}

class SmsNotificationService implements NotificationService{
    private String smsGatewayUrl;

    public SmsNotificationService(String smsGatewayUrl){
        this.smsGatewayUrl = smsGatewayUrl;
    }


    @Override
    public void sendNotification(String userId, String message) {
        System.out.println("SMS Connecting to gateway"+smsGatewayUrl);
        System.out.println("SMS Sending to user:"+userId+" message: "+message);
        System.out.println("SMS delivered");

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}

class PhoneNotificationService implements  NotificationService{
    String phoneNo;

    public PhoneNotificationService(String phoneNo){
        this.phoneNo = phoneNo;
    }

    @Override
    public void sendNotification(String userId, String message) {
        System.out.println("Phone Calling the phone no"+phoneNo);
        System.out.println("Phone Call Connected Notification Sent"+message+" userId: "+userId);
        System.out.println("Phone Message connected User validated");

    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getChannelName() {
        return "Phone";
    }
}

class OrderService{
    private NotificationService notificationService;

    public OrderService(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    public void placeOrder(String userId, String productName,double amount){
        System.out.println("Placing order ...........");
        System.out.println("Placing order for "+productName+" User:"+userId+" Amount:"+amount);

        //After the order is processed
        if(notificationService.isAvailable()){
            String message= "Your order for product name:"+productName+"is placed for userId:"+userId+" Amount charged: "+amount;
            notificationService.sendNotification(userId,message);
        }else{
            System.out.println("Notification Service is unavailable! Cannot send notification to the user !");
        }

        System.out.println("Order placed for ammount:"+amount+" Notification Sent via channel: "+notificationService.getChannelName());
    }


}

public class MyMain {



    public static void main(String[] args) {

        NotificationService smsNotify = new EmailNotificationService("smtp.gmail.com");
        NotificationService emailNotify = new SmsNotificationService("virj@example.com");
        NotificationService phoneNotify = new PhoneNotificationService("1234567899");

        OrderService amazon = new OrderService(smsNotify);

        amazon.placeOrder("viraj101","Smartphone",45600);



    }
}
