package top.palexu.netty.im.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.*;

/**
 * @author palexu * @since 2019/06/26 15:24
 */
public class MsgRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        handleMessage(ctx,  msg);
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageRequestPacket reqeust) {
        System.out.println("message receive: " + reqeust.getMsg());

        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMsg("received: " + reqeust.getMsg());
        ctx.channel().writeAndFlush(responsePacket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}
