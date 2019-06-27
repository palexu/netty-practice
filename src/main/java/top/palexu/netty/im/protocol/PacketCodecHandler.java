package top.palexu.netty.im.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import top.palexu.netty.im.protocol.packet.Packet;

import java.util.List;

/**
 * @author palexu * @since 2019/06/27 15:44
 */
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.encode(ctx.channel().alloc().ioBuffer(), msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(msg));
    }
}
