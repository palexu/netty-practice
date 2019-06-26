package top.palexu.netty.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.palexu.netty.im.protocol.*;
import top.palexu.netty.im.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * @author palexu * @since 2019/06/26 15:24
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + " 客户端开始登录");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("pale");
        loginRequestPacket.setPassword("xu");

        ByteBuf buffer = PacketCodeC.INSTANCE.encode(loginRequestPacket);

        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            handleLoginResponse(ctx.channel(), (LoginResponsePacket) packet);
        } else if (packet instanceof MessageResponsePacket) {
            handleMessageResponse((MessageResponsePacket) packet);
        }
    }

    private void handleMessageResponse(MessageResponsePacket packet) {

        System.out.println("message response: " + packet.getMsg());
    }

    private void handleLoginResponse(Channel channel, LoginResponsePacket packet) {

        if (packet.isSuccess()) {
            System.out.println("登录成功!");
            LoginUtil.setLogin(channel);
        } else {
            System.out.println("登录失败，msg: " + packet.getMsg());
        }
    }
}
