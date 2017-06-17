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

/**
 * Created by Ryan on 17/6/4.
 */
public class BizHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        System.out.println("handling request");
        RPCRequest rpcRequest = (RPCRequest) msg;
        RPCResponse rpcResponse = new RPCResponse();
        rpcResponse.setValue("res:"+rpcRequest.getValue());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcResponse);

        context.write(Unpooled.copiedBuffer(byteArrayOutputStream.toByteArray()));
    }

    public void channelReadComplete(ChannelHandlerContext context) {
        System.out.println("read complete");
        context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }

}
