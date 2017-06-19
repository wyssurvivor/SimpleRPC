package com.wang.wys.handler;

import com.wang.wys.model.MsgType;
import com.wang.wys.model.RPCRequest;
import com.wang.wys.util.CodecUtil;
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

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        if (!CodecUtil.preCheck(byteBuf)) {
            byteBuf.resetReaderIndex();
            return;
        }

        int dataLen = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLen) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[dataLen];
        byteBuf.readBytes(bytes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
        byte msgType = stream.readByte();
        if (msgType == MsgType.REQUEST.getValue()) {
            RPCRequest rpcRequest = new RPCRequest();
            rpcRequest.setValue(stream.readUTF());
            list.add(rpcRequest);
        }
    }
}
