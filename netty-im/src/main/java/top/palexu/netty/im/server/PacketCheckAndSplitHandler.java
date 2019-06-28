package top.palexu.netty.im.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static top.palexu.netty.im.protocol.PacketCodeC.MAGIC_NUMBER;

/**
 * @author palexu * @since 2019/06/27 11:19
 */
public class PacketCheckAndSplitHandler extends LengthFieldBasedFrameDecoder {
    public PacketCheckAndSplitHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //拒绝非本协议的客户端
        if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
