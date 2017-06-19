package com.wang.wys.handler;

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

/**
 * Created by Ryan on 17/6/17.
 */
public class SimpleClientHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println("client connected");
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setValue(Thread.currentThread().getName()+" connected");
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] bytes = rpcRequest.getBytes();
        CodecUtil.writeHead(byteBuf, bytes.length);
        byteBuf.writeBytes(rpcRequest.getBytes());
        ctx.writeAndFlush(byteBuf);
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
        byte msgType = stream.readByte();
        if(msgType == MsgType.RESPONSE.getValue()) {
            System.out.println(stream.readUTF());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
