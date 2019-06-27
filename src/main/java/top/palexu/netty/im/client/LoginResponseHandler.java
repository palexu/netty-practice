package top.palexu.netty.im.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.packet.LoginRequestPacket;
import top.palexu.netty.im.protocol.packet.LoginResponsePacket;
import top.palexu.netty.im.util.UserUtil;

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
        loginRequestPacket.setUserId(UUID.randomUUID().toString().substring(0, 4));
        loginRequestPacket.setUsername("pale");
        loginRequestPacket.setPassword("xu");

        System.out.println("我是" + loginRequestPacket);

        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    private void handleLoginResponse(Channel channel, LoginResponsePacket packet) {

        if (packet.isSuccess()) {
            System.out.println("登录成功!");
            UserUtil.setLogin(channel);
        } else {
            System.out.println("登录失败，msg: " + packet.getMsg());
        }
    }
}
