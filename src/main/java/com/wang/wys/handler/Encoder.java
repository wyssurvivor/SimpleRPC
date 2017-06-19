package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import com.wang.wys.util.CodecUtil;
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
        System.out.println("in server encoder");
        byte[] contents = rpcResponse.getBytes();
        CodecUtil.writeHead(byteBuf, contents.length);
        byteBuf.writeBytes(contents);
//        channelHandlerContext.writeAndFlush(byteBuf);
    }
}
