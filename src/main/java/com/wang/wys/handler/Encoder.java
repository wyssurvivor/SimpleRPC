package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Ryan on 17/6/4.
 */
public class Encoder extends MessageToByteEncoder<RPCResponse> {
    protected void encode(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse, ByteBuf byteBuf) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcResponse);
        byteBuf.writeBytes(byteArrayOutputStream.toByteArray());
        System.out.println("encoded obj:"+rpcResponse);
    }
}
