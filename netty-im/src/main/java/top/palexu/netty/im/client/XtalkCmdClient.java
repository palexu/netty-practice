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
import top.palexu.netty.im.protocol.PacketCodecHandler;
import top.palexu.netty.im.protocol.packet.MessageRequestPacket;
import top.palexu.netty.im.server.PacketCheckAndSplitHandler;
import top.palexu.netty.im.util.UserUtil;

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
                                    .addLast(new PacketCheckAndSplitHandler(Integer.MAX_VALUE, 7, 4))
                                    .addLast(new PacketCodecHandler())
                                    .addLast(new HeartBeatTimerHandler())
                                    .addLast(new LoginResponseHandler())
                                    .addLast(new MsgResponseHandler());
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
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("连接成功!");
                            console((ChannelFuture) future);
//                            autoSend((ChannelFuture) future);
                            return;
                        }
                        System.out.println("连接失败! 剩余尝试次数" + retry);
                        if (retry == 0) {
                            return;

                        }
                        int order = 5 - retry + 1;
                        int delay = 1 << order;
                        bootstrap.config().group().schedule(new Runnable() {
                            @Override
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
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "console-thread");
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    if (!UserUtil.isLogin(channelFuture.channel())) {
                        continue;
                    }
                    System.out.println("输入消息:");
                    Scanner scanner = new Scanner(System.in);
                    String msg = scanner.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setToUserId(msg.split("#")[0]);
                    packet.setMsg(msg.split("#")[1]);

                    channelFuture.channel().writeAndFlush(packet);
                }
            }
        });
    }

    private void autoSend(final ChannelFuture channelFuture) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "console-thread");
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!Thread.interrupted() && i < 1000) {
                    if (!UserUtil.isLogin(channelFuture.channel())) {
                        continue;
                    }
                    i++;
                    String msg = "寒蝉凄切②，对长亭晚③，骤雨初歇。都门帐饮无绪④，留恋处（一说：方留恋处 [1]  ），兰舟催发⑤。执手相看泪眼，竟无语凝噎⑥。念去去⑦，千里烟波，暮霭沉沉楚天阔⑧。多情自古伤离别，更那堪冷落清秋节！今宵酒醒何处⑨？杨柳岸，晓风残月。此去经年⑩，应是良辰好景虚设。便纵有千种风情⑪，更与何人说⑫？ [2] ";

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMsg(msg);

                    channelFuture.channel().writeAndFlush(packet);
                }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        XtalkCmdClient client = new XtalkCmdClient(args[0], Integer.parseInt(args[1]));
        client.start();
    }
}
