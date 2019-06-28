package top.palexu.netty.im.protocol.packet;

import lombok.Data;

import static top.palexu.netty.im.protocol.Command.MESSAGE_RESPONSE;

/**
 * @author palexu * @since 2019/06/26 17:48
 */
@Data
public class MessageResponsePacket extends Packet {
    private String fromUserId;
    private String fromUserName;
    private String msg;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
