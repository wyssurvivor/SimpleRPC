package com.wang.wys.handler.client;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by wangyongshan on 17-7-7.
 */
public class ClientEncoder extends MessageToByteEncoder<RPCRequest> {
    protected void encode(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest, ByteBuf byteBuf) throws Exception {
        byte[] contents = rpcRequest.getBytes();
        CodecUtil.writeHead(byteBuf, contents.length);
        byteBuf.writeBytes(contents);
    }
}
