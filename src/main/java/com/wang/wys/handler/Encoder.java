package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Ryan on 17/6/4.
 */
public class Encoder extends MessageToByteEncoder<RPCRequest> {
    protected void encode(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest, ByteBuf byteBuf) throws Exception {

    }
}
