package com.wang.wys.handler;

import com.wang.wys.model.RPCRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Ryan on 17/6/4.
 */
public class BizHandler extends SimpleChannelInboundHandler<RPCRequest> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {

    }
}
