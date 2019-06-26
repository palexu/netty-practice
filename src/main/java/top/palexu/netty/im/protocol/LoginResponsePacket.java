package top.palexu.netty.im.protocol;

import lombok.Data;

import static top.palexu.netty.im.protocol.Command.LOGIN_RESPONSE;

/**
 * @author palexu * @since 2019/06/26 15:38
 */
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;
    private String msg;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
