package top.palexu.netty.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author palexu * @since 2019/06/28 11:36
 */
public class RpcClient {
    public static void main(String[] args) {
    }

    public void start(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        ChannelFuture channelFuture;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
//                            ch.pipeline()
//                                    .addLast(new PrintHandler());
                        }
                    });

            channelFuture = bootstrap.connect().sync();

            channelFuture
                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < 100; i++) {
                                            channelFuture.channel().writeAndFlush("hello");
                                        }
                                    }
                                }).start();
                                System.out.println("connect success!");

                                return;
                            }
                            System.out.println("connect fail!");
                        }
                    });
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
