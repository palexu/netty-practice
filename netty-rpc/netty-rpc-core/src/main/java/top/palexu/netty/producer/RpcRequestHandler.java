package top.palexu.netty.producer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.palexu.netty.protocol.RpcRequest;
import top.palexu.netty.protocol.RpcResponse;

/**
 * @author palexu * @since 2019/06/28 16:23
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {
    //这里维护一个映射，

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        System.out.println("invok " + msg);
        RpcResponse response = new RpcResponse();
        response.setData("hello world");
        ctx.writeAndFlush(response);
    }
}
