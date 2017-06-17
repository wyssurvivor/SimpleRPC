package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.*;

/**
 * Created by Ryan on 17/6/17.
 */
public class SimpleClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println("client connected");
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setValue("netty connected");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcRequest);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(byteArrayOutputStream.toByteArray());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("receive response");
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[1024];
        byteBuf.readBytes(bytes, 0, byteBuf.readableBytes());
        RPCResponse rpcResponse = (RPCResponse) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        System.out.println(rpcResponse.getValue());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
