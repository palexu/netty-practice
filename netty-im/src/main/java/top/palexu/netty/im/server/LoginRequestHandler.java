package top.palexu.netty.im.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.palexu.netty.im.protocol.packet.LoginRequestPacket;
import top.palexu.netty.im.protocol.packet.LoginResponsePacket;
import top.palexu.netty.im.util.UserUtil;

/**
 * @author palexu * @since 2019/06/27 10:21
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        handleLogin(ctx, msg);
    }

    private void handleLogin(ChannelHandlerContext ctx, LoginRequestPacket reqeust) {
        LoginResponsePacket response = new LoginResponsePacket();
        if (valid(reqeust)) {
            UserUtil.setLogin(reqeust, ctx.channel());
            response.setSuccess(true);
            System.out.println("用户登录成功, userId=" + reqeust.getUserId());
        } else {
            response.setSuccess(false);
            response.setMsg("用户名或密码错误");
        }
        ctx.channel().writeAndFlush(response);
        System.out.println("send resposne: " + response);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}
