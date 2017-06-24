package com.wang.wys.server;

import com.wang.wys.handler.BizHandler;
import com.wang.wys.handler.Decoder;
import com.wang.wys.handler.Encoder;
import com.wang.wys.handler.TestServerHandler;
import com.wang.wys.impl.SimpleIfaceImpl;
import com.wang.wys.interfaces.SimpleIface;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ryan on 17/6/4.
 */
public class SimpleServer {
    private Class ifaceClass;
    private Object implObj;
    private int port;
    public SimpleServer(int port, Class ifaceClass, Object implObj) {
        this.port = port;
        this.ifaceClass = ifaceClass;
        this.implObj = implObj;
//        initCheck();
    }

    private void initCheck() {
        if(ifaceClass.isInstance(implObj)) {
            throw new IllegalArgumentException("无效的输入实现类");
        }
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new Decoder())
                                    .addLast(new BizHandler(ifaceClass, implObj))
                                    .addLast(new Encoder());
//                            socketChannel.pipeline().addLast(new TestServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("server start up");
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 3494;
        Class ifaceClass = SimpleIface.class;
        Object obj = new SimpleIfaceImpl();
        new SimpleServer(port, ifaceClass, obj).start();
    }
}
