package com.basics.java02_beanfactory;

public interface BeanFactory {
    Object getBean(String name);
    <T> T getBean(Class<T> type);
    boolean containsBean(String name);
}
