package top.palexu.netty.producer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.palexu.netty.protocol.RpcRequest;
import top.palexu.netty.protocol.RpcResponse;

import java.lang.reflect.Method;

/**
 * @author palexu * @since 2019/06/28 16:23
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        System.out.println("invok " + msg);

        Object clazzObject = RpcServer.getContext().getBean(msg.getClazz());
        Class clazz = clazzObject.getClass();
        Method declaredMethod = clazz.getDeclaredMethod(msg.getMethod(), msg.getParameterTypes());

        Object data = declaredMethod.invoke(clazzObject, msg.getArgs());

        RpcResponse response = new RpcResponse();
        response.setData(data);
        response.setReturnType(data.getClass());
        ctx.writeAndFlush(response);
    }
}
