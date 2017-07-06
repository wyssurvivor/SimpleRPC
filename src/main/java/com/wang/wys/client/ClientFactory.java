package com.wang.wys.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Ryan on 17/6/24.
 */
public class ClientFactory {
    public <T> T getClient(Class<T> ifaceClass, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(ifaceClass.getClassLoader(), new Class[]{ifaceClass}, handler);
    }
}
