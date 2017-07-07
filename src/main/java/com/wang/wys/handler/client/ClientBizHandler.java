package com.wang.wys.handler.client;

import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wangyongshan on 17-7-7.
 */
public class ClientBizHandler extends SimpleChannelInboundHandler<RPCResponse>{
    private ChannelHandlerContext context;
    private BlockingQueue<Promise<RPCResponse>> messageList = new ArrayBlockingQueue<Promise<RPCResponse>>(16);

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        this.context = context;
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        System.out.println("client channel inactive");
        super.channelInactive(context);
        synchronized (this) {
            Promise<RPCResponse> promise = null;
            Exception err = new IOException("Connection lost");
            while((promise = messageList.poll())!=null) {
                promise.setFailure(err);
            }
            messageList = null;
        }
    }

    public Promise<RPCResponse> sendMessage(RPCRequest request, final Promise<RPCResponse> promise) {
        synchronized (this) {
            if(messageList == null) {
                promise.setFailure(new IllegalStateException());
            } else if(messageList.offer(promise)) {
                this.context.writeAndFlush(request).addListener(new GenericFutureListener<Future<? super Void>>() {
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        try {
                            future.get();
                        } catch (Exception e) {
                            promise.setFailure(e);
                        }
                    }
                });
            } else {
                promise.setFailure(new BufferOverflowException());
            }
            return promise;
        }
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
        synchronized (this) {
            if(messageList != null) {
                messageList.poll().setSuccess(rpcResponse);
            }
        }
    }
}
