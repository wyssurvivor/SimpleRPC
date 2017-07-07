package com.wang.wys.client;

import com.wang.wys.interfaces.SimpleIface;
import com.wang.wys.model.ClientConfig;
import com.wang.wys.model.ClientInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Ryan on 17/6/24.
 */
public class ClientFactory {
    public static <T> T getCLient(ClientConfig clientConfig, Class<T> ifaceClass) throws InterruptedException {
        IfaceClient client = new IfaceClient(clientConfig.getHost(), clientConfig.getPort());
        InvocationHandler handler = new ClientInvocationHandler(client);
        return (T) Proxy.newProxyInstance(ClientFactory.class.getClassLoader(), new Class[]{ifaceClass}, handler);
    }

    public static void main(String [] args) throws Exception {
        String host = "localhost";
        int port = 3494;
        ClientConfig config = new ClientConfig();
        config.setHost(host);
        config.setPort(port);
        SimpleIface bizClient = ClientFactory.getCLient(config, SimpleIface.class);
        System.out.println(bizClient.add(1, 1));
        Thread.currentThread().sleep(2000);
        System.out.println(bizClient.add(20, 21));
        bizClient.close();
        System.exit(0);
    }
}
