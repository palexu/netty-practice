package top.palexu.netty.im.protocol.packet;

import lombok.Data;

import static top.palexu.netty.im.protocol.Command.LOGIN_REQUEST;

/**
 * @author palexu * @since 2019/06/26 14:53
 */
@Data
public class LoginRequestPacket extends Packet {
    private String userId;
    private String username;
    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}

