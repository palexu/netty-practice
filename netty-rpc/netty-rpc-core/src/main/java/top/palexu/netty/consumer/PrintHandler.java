package top.palexu.netty.consumer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author palexu * @since 2019/06/28 15:06
 */
@ChannelHandler.Sharable
public class PrintHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("printHandler active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("printHandler read:" + msg);
        super.channelRead(ctx, msg);
    }
}
