package top.palexu.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author palexu * @since 2019/06/28 14:47
 */
@ChannelHandler.Sharable
public class CodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.encode(ctx.alloc().ioBuffer(), msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        Packet decode = PacketCodeC.INSTANCE.decode(msg);
        out.add(decode);
    }
}
