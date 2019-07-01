package top.palexu.netty.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import top.palexu.netty.protocol.CodecHandler;

/**
 * @author palexu * @since 2019/06/28 11:36
 */
@Slf4j
@Configuration
public class RpcClient {

    public static Channel start(Rpc rpc, String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        ChannelFuture channelFuture;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new CodecHandler())
                                .addLast(new RpcResponseHandler(rpc));
                    }
                });

        channelFuture = bootstrap.connect().sync();

        channelFuture
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("connect success!");
                        return;
                    }
                    log.info("connect fail!");
                });

        return channelFuture.channel();
    }

}
