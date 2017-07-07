package com.wang.wys.handler.client;

import com.wang.wys.model.RPCResponse;
import com.wang.wys.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by wangyongshan on 17-7-7.
 */
public class ClientDecoder extends ByteToMessageDecoder {
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
        RPCResponse rpcResponse = (RPCResponse) stream.readObject();
        list.add(rpcResponse);
    }
}
