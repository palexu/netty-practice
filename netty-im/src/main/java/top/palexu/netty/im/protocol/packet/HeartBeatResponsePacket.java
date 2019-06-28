package top.palexu.netty.im.protocol.packet;

import static top.palexu.netty.im.protocol.Command.HEARTBEAT_RESPONSE;

/**
 * @author palexu * @since 2019/06/27 16:31
 */
public class HeartBeatResponsePacket extends Packet{
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
