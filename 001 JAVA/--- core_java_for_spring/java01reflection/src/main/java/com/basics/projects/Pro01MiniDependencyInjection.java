package com.basics.projects;

import javax.swing.text.html.parser.DTDConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Inject {

}

class DatabaseService{
    public String query(String sql){
        return "Result of the Query is"+sql;
    }
}

class EmailService{
    public void send(String message){
        System.out.println("Email Sent:"+message);
    }
}

class OrderService{
    @Inject
    private DatabaseService db;

    @Inject
    private EmailService email;

    public void placeOrder(String item){
        String result = db.query("SELECT * FROM orders WHERE item="+item+"");
        email.send("Order Placed");
        System.out.println("DB says:"+result);

    }
}

class DIContainer{
    private Map<Class<?>, Object> registry = new HashMap<>();

    public <T> void register(Class<T> type, T instance){
        registry.put(type,instance);
    }

    public <T> T getInstance(Class<T> type) throws Exception{
        T instance = type.getDeclaredConstructor().newInstance();

        for(Field field:type.getDeclaredFields()){
            if(field.isAnnotationPresent(Inject.class)){
                Object dependency = registry.get(field.getType());

                if(dependency ==null){
                    throw new RuntimeException("No registered instance for:"+ field.getType());
                }

                field.setAccessible(true);

                field.set(instance,dependency);
            }
        }

        return instance;
    }
}

public class Pro01MiniDependencyInjection {

    public static void main(String[] args) throws Exception {
        DIContainer container = new DIContainer();

        container.register(DatabaseService.class,new DatabaseService());
        container.register(EmailService.class,new EmailService());

        OrderService orderService = container.getInstance(OrderService.class);

        orderService.placeOrder("Laptop");
    }
}
