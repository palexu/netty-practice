package top.palexu.netty.im.protocol;

import lombok.Data;

import static top.palexu.netty.im.protocol.Command.MESSAGE_REQUEST;

/**
 * @author palexu * @since 2019/06/26 17:48
 */
@Data
public class MessageRequestPacket extends Packet {
    private String msg;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
