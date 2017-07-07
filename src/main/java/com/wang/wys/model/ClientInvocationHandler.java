package com.wang.wys.model;

import com.wang.wys.client.IfaceClient;
import com.wang.wys.client.SimpleClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by wangyongshan on 17-7-6.
 */
public class ClientInvocationHandler implements InvocationHandler {

    private IfaceClient client;
    public ClientInvocationHandler(IfaceClient client) {
        this.client = client;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setIfaceClass(method.getDeclaringClass());
        rpcRequest.setFuncName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParams(args);
        RPCResponse rpcResponse = client.process(rpcRequest);
        return rpcResponse.getResult();
    }
}
