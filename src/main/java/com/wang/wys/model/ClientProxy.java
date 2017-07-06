package com.wang.wys.model;

import com.wang.wys.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by Ryan on 17/6/24.
 */
public class ClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public ClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setIfaceClass(method.getDeclaringClass());
        rpcRequest.setFuncName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParams(args);
        return null;
    }

    public RPCResponse doRequest(final RPCRequest rpcRequest) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        RPCResponse response = null;
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

        return response;
    }
}
