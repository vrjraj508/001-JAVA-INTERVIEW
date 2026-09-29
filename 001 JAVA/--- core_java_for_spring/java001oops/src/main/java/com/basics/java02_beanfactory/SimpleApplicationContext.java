package com.basics.java02_beanfactory;

import com.basics.App;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleApplicationContext implements ApplicationContext {
    private final String id;
    private final Map<String,Object> beans = new LinkedHashMap<>();
    private BeanFactory parent;
    private boolean active = false;

    public SimpleApplicationContext(String id){
        this.id = id;
    }

    public SimpleApplicationContext(String id,BeanFactory parent){
        this.id = id;
        this.parent = parent;
    }

    public void registerBean(String name,Object bean){
        if(active){
            throw new IllegalStateException("Cannot register beans after context is active");
        }

        beans.put(name,bean);
    }



    @Override
    public String getId() {
        return id;
    }

    @Override
    public void refresh() {
        active = true;
        System.out.println("ID:"+id+ "Context Refreshed...."+beans.size()+" beans active.");

    }

    @Override
    public void close() {
        active = false;

        System.out.println("ID: "+id+" Context closed");

    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return  parent;
    }

    @Override
    public boolean containsLocalBean(String name) {
        return beans.containsKey(name);
    }

    @Override
    public String[] getAllBeanName() {
        return beans.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanCount() {
        return  beans.size();
    }

    @Override
    public <T> String[] getBeanNamesByType(Class<T> type) {
       List<String> result = new ArrayList<>();

       for(Map.Entry<String,Object> entry: beans.entrySet()){
           if(type.isAssignableFrom(entry.getValue().getClass())){
               result.add(entry.getKey());

           }
       }

       return  result.toArray(new String [0]);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String,T> result = new LinkedHashMap<>();
        for(Map.Entry<String,Object> entry: beans.entrySet()){
            if(type.isAssignableFrom((entry.getValue().getClass()))){
                result.put(entry.getKey(),(T) entry.getValue());
            }
        }

        return  result;
    }

    @Override
    public Object getBean(String name) {
        Object bean = beans.get(name);


        if(bean ==null && parent !=null){
            System.out.println("Id:"+id+" name:"+name+" not local -- delegating to parent context");
            return parent.getBean(name);
        }

        if(bean ==null){
            throw new RuntimeException("No bean named:"+ name);
        }
        return  bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        for(Object bean : beans.values()){
            if(type.isAssignableFrom(bean.getClass())){
                return (T) bean;
            }
        }
        if(parent !=null){
            return  parent.getBean(type);
        }

        throw new RuntimeException("No bean of type :"+type.getSimpleName());
    }

    @Override
    public boolean containsBean(String name) {
       return beans.containsKey(name) || (parent !=null && parent.containsBean(name));
    }
}
