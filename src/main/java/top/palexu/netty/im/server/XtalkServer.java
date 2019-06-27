package top.palexu.netty.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.palexu.netty.im.protocol.PacketDecoder;
import top.palexu.netty.im.protocol.PacketEncoder;

/**
 * @author palexu * @since 2019/06/25 17:01
 */
public class XtalkServer {
    private int port;

    public XtalkServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4))
                                    .addLast(new PacketDecoder())
                                    .addLast(new LoginRequestHandler())
                                    .addLast(new MsgRequestHandler())
                                    .addLast(new PacketEncoder());
                        }
                    });

            serverBootstrap
                    //是否开启TCP底层心跳机制，true为开启
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启
                    .childOption(ChannelOption.TCP_NODELAY, true);
            //系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
//                    .childOption(ChannelOption.SO_BACKLOG, 1024);

            ChannelFuture channelFuture = serverBootstrap.bind(port)
                    .sync()
                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                System.out.println("绑定成功,port: " + port);
                            } else {
                                System.out.println("绑定失败,port: " + port);
                            }
                        }
                    });

            channelFuture.channel().closeFuture().sync();

        } finally {
            System.out.println("shutting down...");
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        XtalkServer xtalkServer = new XtalkServer(Integer.parseInt(args[0]));
        xtalkServer.start();
    }
}
