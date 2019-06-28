package top.palexu.netty.im.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.packet.HeartBeatRequestPacket;
import top.palexu.netty.im.protocol.packet.HeartBeatResponsePacket;

/**
 * @author palexu * @since 2019/06/27 16:44
 */
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
        ctx.writeAndFlush(new HeartBeatResponsePacket());
        System.out.println("收到心跳包，返回心跳响应");
    }
}
