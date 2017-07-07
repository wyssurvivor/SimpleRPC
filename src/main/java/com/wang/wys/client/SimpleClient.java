package com.wang.wys.client;

import com.wang.wys.handler.Decoder;
import com.wang.wys.handler.Encoder;
import com.wang.wys.handler.SimpleClientHandler;
import com.wang.wys.handler.TestClientHandler;
import com.wang.wys.model.RPCRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ryan on 17/6/17.
 */
public class SimpleClient {
    private final String host;
    private final int port;
    private final RPCRequest rpcRequest;
    public SimpleClient(String host, int port, RPCRequest rpcRequest) {
        this.host = host;
        this.port = port;
        this.rpcRequest = rpcRequest;
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new SimpleClientHandler(rpcRequest));
//                            socketChannel.pipeline().addLast(new TestClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new SimpleClient("localhost", 3494, null).start();
    }
}
