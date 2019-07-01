package top.palexu.netty.producer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import top.palexu.netty.protocol.CodecHandler;

/**
 * @author palexu * @since 2019/06/28 11:36
 */
@Slf4j
@Configuration
public class RpcServer implements ApplicationContextAware {

    private static ApplicationContext context;

    private static final int port = 10800;

    public static ApplicationContext getContext() {
        return context;
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
                                    .addLast(new CodecHandler())
                                    .addLast(new RpcRequestHandler());
                        }
                    });
            ChannelFuture channelFuture = bind(serverBootstrap, port, 5);

            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }
    }

    private ChannelFuture bind(ServerBootstrap serverBootstrap, int port, int retry) {
        if (retry == 0) {
            log.info("retry count = 0, fail to start rpc server", retry);
            return null;
        }
        return serverBootstrap
                .localAddress(port)
                .bind()
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("rpc server start at port {} success！", port);
                        return;
                    }
                    log.info("rpc server start fail, remain retry count {}", retry - 1);
                    this.bind(serverBootstrap, port + 1, retry - 1);
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        context = applicationContext;

        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
