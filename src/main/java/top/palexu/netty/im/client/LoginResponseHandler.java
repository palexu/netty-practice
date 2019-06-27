package top.palexu.netty.im.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.LoginRequestPacket;
import top.palexu.netty.im.protocol.LoginResponsePacket;
import top.palexu.netty.im.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * @author palexu * @since 2019/06/27 10:23
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        handleLoginResponse(ctx.channel(), msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + " 客户端开始登录");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("pale");
        loginRequestPacket.setPassword("xu");

        ctx.channel().writeAndFlush(loginRequestPacket);
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
