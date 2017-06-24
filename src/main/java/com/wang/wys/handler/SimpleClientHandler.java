package com.wang.wys.handler;

import com.wang.wys.interfaces.SimpleIface;
import com.wang.wys.model.Constants;
import com.wang.wys.model.MsgType;
import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import com.wang.wys.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 17/6/17.
 */
public class SimpleClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println("client connected");
        RPCRequest rpcRequest = generateRpcRequest();
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] bytes = rpcRequest.getBytes();
        CodecUtil.writeHead(byteBuf, bytes.length);
        byteBuf.writeBytes(rpcRequest.getBytes());

        ctx.writeAndFlush(byteBuf);
    }

    private RPCRequest generateRpcRequest() {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setIfaceClass(SimpleIface.class);
        rpcRequest.setFuncName("add");

        int paramLen = 2;
        Class[] paramTypes = new Class[paramLen];
        paramTypes[0] = int.class;
        paramTypes[1] = int.class;
        rpcRequest.setParamTypes(paramTypes);

        Object[] params = new Object[paramLen];
        params[0] = new Integer(1);
        params[1] = new Integer(200);
        rpcRequest.setParams(params);

        return rpcRequest;
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("receive response");
        ByteBuf byteBuf = (ByteBuf) msg;
        byteBuf.markReaderIndex();
        if(!CodecUtil.preCheck(byteBuf)) {
            byteBuf.resetReaderIndex();
            return;
        }

        int dataLen = byteBuf.readInt();
        if(dataLen < byteBuf.readableBytes()) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[dataLen];
        byteBuf.readBytes(bytes);
        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));
//        byte msgType = stream.readByte();
//        if(msgType == MsgType.RESPONSE.getValue()) {
//            System.out.println(stream.readInt());
//        }
        RPCResponse response = (RPCResponse) stream.readObject();
        System.out.println((Integer) response.getResult());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
