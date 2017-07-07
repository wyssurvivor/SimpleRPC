package com.wang.wys.client;

import com.wang.wys.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangyongshan on 17-6-19.
 */
public class MultiClient {
    static class ClientThread implements Runnable {
        String host;
        int port;

        EventLoopGroup group;

        public ClientThread(String host, int port) {
            this.host = host;
            this.port = port;
            group = new NioEventLoopGroup(4, new DefaultThreadFactory("clientThreadGroup", true));
        }

        public void run() {
            // EventLoopGroup group = new NioEventLoopGroup();
            // 此处如果每次都新建eventloopgroup，那么用不了几个就会报异常，原因暂时未查明。只是查到issue里有人说要使用一个eventloopgroup
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new SimpleClientHandler(null));
                                // socketChannel.pipeline().addLast(new
                                // TestClientHandler());
                            }
                        });
                ChannelFuture future = bootstrap.connect().sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 3494;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 90; i++) {
            executorService.execute(new ClientThread(host, port));
            Thread.sleep(20);
        }
        executorService.shutdown();
    }
}
