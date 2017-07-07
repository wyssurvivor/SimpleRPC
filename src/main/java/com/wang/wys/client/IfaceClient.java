package com.wang.wys.client;

import com.wang.wys.handler.SimpleClientHandler;
import com.wang.wys.handler.client.ClientBizHandler;
import com.wang.wys.handler.client.ClientDecoder;
import com.wang.wys.handler.client.ClientEncoder;
import com.wang.wys.model.RPCRequest;
import com.wang.wys.model.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wangyongshan on 17-7-6.
 */
public class IfaceClient {
    private String host;
    private int port;
    private ChannelFuture future;
    private EventLoopGroup group;
    private EventExecutor eventExecutor;
    public IfaceClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;

        this.eventExecutor = new DefaultEventExecutor(Executors.newFixedThreadPool(32));
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ClientEncoder())
                                .addLast(new ClientDecoder())
                                .addLast(new ClientBizHandler());

                    }
                });
        future = bootstrap.connect().sync();
    }

    public RPCResponse process(RPCRequest rpcRequest) throws ExecutionException, InterruptedException {
        Promise<RPCResponse> promise = new DefaultPromise<RPCResponse>(this.eventExecutor);
        promise = future.channel().pipeline().get(ClientBizHandler.class).sendMessage(rpcRequest, promise);
        RPCResponse response = promise.get();
        return response;
    }

    public void close() {
        this.group.shutdownGracefully();
    }

}
