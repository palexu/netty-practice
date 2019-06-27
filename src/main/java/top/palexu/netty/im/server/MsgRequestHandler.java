package top.palexu.netty.im.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.packet.MessageRequestPacket;
import top.palexu.netty.im.protocol.packet.MessageResponsePacket;
import top.palexu.netty.im.util.User;
import top.palexu.netty.im.util.UserUtil;

/**
 * @author palexu * @since 2019/06/26 15:24
 */
public class MsgRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        handleMessage(ctx, msg);
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageRequestPacket request) {
        System.out.println("message receive: " + request.getMsg());

        Channel toUserChannel = UserUtil.channel(request.getToUserId());
        if (null != toUserChannel) {
            //转发
            User from = UserUtil.user(ctx.channel());
            MessageResponsePacket to = new MessageResponsePacket();
            to.setFromUserId(from.getUserId());
            to.setFromUserName(from.getUserName());
            to.setMsg(request.getMsg());
            toUserChannel.writeAndFlush(to);

            //告诉发送者结果
//            MessageResponsePacket responsePacket = new MessageResponsePacket();
//            responsePacket.setMsg("消息发送成功: " + request.getMsg());
//            ctx.channel().writeAndFlush(responsePacket);
        } else {
            MessageResponsePacket responsePacket = new MessageResponsePacket();
            responsePacket.setMsg("不存在该用户或对方未在线: " + request.getMsg());
            ctx.channel().writeAndFlush(responsePacket);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}
