package top.palexu.netty.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.palexu.netty.im.protocol.*;

/**
 * @author palexu * @since 2019/06/26 15:24
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet reqeust = PacketCodeC.INSTANCE.decode(byteBuf);

        if (reqeust instanceof LoginRequestPacket) {
            handleLogin(ctx, (LoginRequestPacket) reqeust);
        } else if (reqeust instanceof MessageRequestPacket) {
            handleMessage(ctx, (MessageRequestPacket) reqeust);
        }

    }

    private void handleMessage(ChannelHandlerContext ctx, MessageRequestPacket reqeust) {
        MessageRequestPacket messageRequest = reqeust;
        System.out.println("message receive: " + messageRequest.getMsg());

        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMsg("received: " + messageRequest.getMsg());
        ctx.channel().writeAndFlush(PacketCodeC.INSTANCE.encode(responsePacket));
    }

    private void handleLogin(ChannelHandlerContext ctx, LoginRequestPacket reqeust) {
        LoginResponsePacket response = new LoginResponsePacket();
        if (valid(reqeust)) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setMsg("用户名或密码错误");
        }
        ctx.channel().writeAndFlush(PacketCodeC.INSTANCE.encode(response));
        System.out.println("send resposne: " + response);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
