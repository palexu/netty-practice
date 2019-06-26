package top.palexu.netty.im.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.palexu.netty.im.protocol.MessageRequestPacket;
import top.palexu.netty.im.protocol.PacketCodeC;
import top.palexu.netty.im.protocol.PacketDecoder;
import top.palexu.netty.im.protocol.PacketEncoder;
import top.palexu.netty.im.util.LoginUtil;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author palexu * @since 2019/06/25 17:12
 */
public class XtalkCmdClient {
    private String host;
    private int port;

    public XtalkCmdClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new PacketDecoder())
                                    .addLast(new ClientHandler())
                                    .addLast(new PacketEncoder());
                        }
                    });
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true);
            bootstrap.attr(AttributeKey.newInstance("clientName"), "javaCmd");
            connect(bootstrap, 5);
        } finally {
            group.shutdownGracefully().sync();
        }


    }

    private void connect(final Bootstrap bootstrap, final int retry) throws InterruptedException {
        final ChannelFuture channelFuture = bootstrap.connect().sync()
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("连接成功!");
                            console((ChannelFuture) future);
                            return;
                        }
                        System.out.println("连接失败! 剩余尝试次数" + retry);
                        if (retry == 0) {
                            return;

                        }
                        int order = 5 - retry + 1;
                        int delay = 1 << order;
                        bootstrap.config().group().schedule(new Runnable() {
                            public void run() {
                                try {
                                    connect(bootstrap, retry - 1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, delay, TimeUnit.SECONDS);
                    }
                });


        channelFuture.channel().closeFuture().sync();
    }

    private void console(final ChannelFuture channelFuture) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "console-thread");
            }
        });
        executor.submit(new Runnable() {
            public void run() {
                while (!Thread.interrupted()) {
                    if (!LoginUtil.isLogin(channelFuture.channel())) {
                        continue;
                    }
                    System.out.println("输入消息:");
                    Scanner scanner = new Scanner(System.in);
                    String msg = scanner.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMsg(msg);

                    channelFuture.channel().writeAndFlush(PacketCodeC.INSTANCE.encode(packet));
                }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        XtalkCmdClient client = new XtalkCmdClient(args[0], Integer.parseInt(args[1]));
        client.start();
    }
}
