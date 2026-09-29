package com.basics.java02_beanfactory;

public interface HierarchicalBeanFactory extends BeanFactory{
    BeanFactory getParentBeanFactory();
    boolean containsLocalBean(String name);
}
