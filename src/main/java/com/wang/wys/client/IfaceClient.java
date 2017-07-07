package com.wang.wys.client;

import com.wang.wys.handler.SimpleClientHandler;
import com.wang.wys.handler.client.ClientBizHandler;
import com.wang.wys.handler.client.ClientDecoder;
import com.wang.wys.handler.client.ClientEncoder;
import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by wangyongshan on 17-7-6.
 */
public class IfaceClient {
    private String host;
    private int port;
    private ChannelFuture future;
    private EventLoopGroup group;
    private EventExecutor eventExecutor; // 用于Promise的连接池
    private FixedChannelPool channelPool; // 连接池，一个客户端应该能够发起多个请求。
    public IfaceClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;

        this.eventExecutor = new DefaultEventExecutor(Executors.newFixedThreadPool(32));
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        this.channelPool = new FixedChannelPool(bootstrap, new ChannelPoolHandler() {
            public void channelReleased(Channel channel) throws Exception {
            }

            public void channelAcquired(Channel channel) throws Exception {
            }

            public void channelCreated(Channel channel) throws Exception {
                channel.pipeline().addLast(new ClientEncoder()).addLast(new ClientDecoder()).addLast(new ClientBizHandler());
            }
        }, 32);
    }

    public RPCResponse process(final RPCRequest rpcRequest) throws ExecutionException, InterruptedException {
        final Promise<RPCResponse> promise = new DefaultPromise<RPCResponse>(this.eventExecutor);
        this.channelPool.acquire().addListener(new GenericFutureListener<Future<? super Channel>>() {
            public void operationComplete(Future<? super Channel> future) throws Exception {
                Channel channel = null;
                try {
                    channel = (Channel) future.get();
                    channel.pipeline().get(ClientBizHandler.class).sendMessage(rpcRequest, promise);
                } catch (Exception e) {
                    promise.setFailure(e);
                }
            }
        });
        RPCResponse response = promise.get();
        return response;
    }

    public void close() {
        this.group.shutdownGracefully();
    }

}
