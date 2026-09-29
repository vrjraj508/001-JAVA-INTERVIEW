package com.basics.java02_beanfactory;

import java.util.Map;

public interface ListableBeanFactory extends  BeanFactory{
    String [] getAllBeanName();
    int getBeanCount();
    <T> String [] getBeanNamesByType(Class<T>  type);
    <T>Map<String,T> getBeansOfType(Class<T> type);
}
