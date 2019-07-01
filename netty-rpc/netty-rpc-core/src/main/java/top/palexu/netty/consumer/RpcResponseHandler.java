package top.palexu.netty.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.palexu.netty.protocol.RpcResponse;

import java.util.concurrent.TimeUnit;

/**
 * @author palexu * @since 2019/06/28 16:30
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private Rpc rpc;

    public RpcResponseHandler(Rpc rpc) {
        this.rpc = rpc;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        rpc.put(msg, 10, TimeUnit.SECONDS);
        log.info("rpc response write 2 sq");
    }
}
