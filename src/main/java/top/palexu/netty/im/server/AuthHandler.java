package top.palexu.netty.im.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.palexu.netty.im.util.UserUtil;

/**
 * @author palexu * @since 2019/06/27 11:45
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (UserUtil.isLogin(ctx.channel())) {
            System.out.println("登录鉴权通过...");
            ctx.fireChannelRead(msg);
            ctx.channel().pipeline().remove(this);
            return;
        }
        System.out.println("登录鉴权失败，关闭连接...");
        ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("移除" + this.getClass().getSimpleName());
    }
}
