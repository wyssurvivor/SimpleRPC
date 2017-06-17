package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by Ryan on 17/6/4.
 */
public class Decoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte[] bytes = new byte[1024];
        byteBuf.readBytes(bytes, 0, byteBuf.readableBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        RPCRequest rpcRequest = (RPCRequest)new ObjectInputStream(byteArrayInputStream).readObject();
        System.out.println("decoding content:"+rpcRequest.getValue());
        list.add(rpcRequest);
    }
}
