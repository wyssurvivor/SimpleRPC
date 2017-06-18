package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Ryan on 17/6/4.
 */
public class Decoder extends ByteToMessageDecoder {

    public boolean preCheck(ByteBuf byteBuf) {
        byte[] markBytes = new byte[3];
        int msgLen = byteBuf.readInt();
        ObjectOutputStream stream = new ObjectOutputStream(new ByteArrayOutputStream());
        if(msgLen+4 < byteBuf.readableBytes()) {
            return false;
        }



    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        if(len < 4 || !preCheck(byteBuf)) {
            ReferenceCountUtil.release(byteBuf);
            return ;
        }

        preCheck(byteBuf);
        byte[] bytes = new byte[1024];
        byteBuf.readBytes(bytes, 0, byteBuf.readableBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        RPCRequest rpcRequest = (RPCRequest)new ObjectInputStream(byteArrayInputStream).readObject();
        System.out.println("decoding content:"+rpcRequest.getValue());
        list.add(rpcRequest);
    }
}
