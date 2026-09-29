package com.basics.java02_beanfactory;

class PaymentServiceImpl{
    public String process(double amount){
        return "Payment processed for RS:"+amount;
    }
}

class UserServiceImpl{
    public String findUser(String id){
        return "User found with id:"+id;
    }
}

class OrderServiceImpl{
    public String createOrder(String id){
        return "Order created for id:"+id;
    }
}

class InventoryServiceImpl{
    public boolean checkStock(String item){
        return  true;
    }
}

public class MyMain {

    public static void main(String[] args) {
        SimpleApplicationContext rootCtx = new SimpleApplicationContext("RootContext");

        rootCtx.registerBean("paymentService",new PaymentServiceImpl());
        rootCtx.registerBean("userService",new UserServiceImpl());
        rootCtx.registerBean("inventoryService",new InventoryServiceImpl());

        rootCtx.refresh();

        SimpleApplicationContext webCtx =  new SimpleApplicationContext("WebContent", rootCtx);
        webCtx.registerBean("orderService",new OrderServiceImpl());
        webCtx.refresh();

        BeanFactory basicClient = webCtx;

        System.out.println(basicClient.getBean("orderService"));
        System.out.println(basicClient.getBean("paymentService"));
    }
}
