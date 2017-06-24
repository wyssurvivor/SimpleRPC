package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import sun.rmi.transport.Channel;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ryan on 17/6/4.
 */
public class BizHandler extends ChannelInboundHandlerAdapter {
    private Map<Class, Object> bizMap = new ConcurrentHashMap<Class, Object>();

    public BizHandler(Class ifaceClass, Object implObj) {
        bizMap.put(ifaceClass, implObj);
    }

    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        RPCRequest rpcRequest = (RPCRequest) msg;
        Object target = bizMap.get(rpcRequest.getIfaceClass());
        Method method = rpcRequest.getIfaceClass().getDeclaredMethod(rpcRequest.getFuncName(), rpcRequest.getParamTypes());
        Object result = method.invoke(target, rpcRequest.getParams());

        RPCResponse rpcResponse = new RPCResponse();
        rpcResponse.setResult(result);
        context.pipeline().writeAndFlush(rpcResponse);
    }

    public void channelReadComplete(ChannelHandlerContext context) {
//        System.out.println("read complete");
        context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }

}
