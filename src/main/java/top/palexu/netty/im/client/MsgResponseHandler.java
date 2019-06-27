package top.palexu.netty.im.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.packet.MessageResponsePacket;

/**
 * @author palexu * @since 2019/06/26 15:24
 */
public class MsgResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket packet) throws Exception {
        handleMessageResponse(packet);
    }


    private void handleMessageResponse(MessageResponsePacket packet) {
        System.out.println("message response: " + packet.getMsg());
    }


}
