package top.palexu.netty.im.protocol.packet;

import static top.palexu.netty.im.protocol.Command.HEARTBEAT_REQUEST;

/**
 * @author palexu * @since 2019/06/27 16:31
 */
public class HeartBeatRequestPacket extends Packet{
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
