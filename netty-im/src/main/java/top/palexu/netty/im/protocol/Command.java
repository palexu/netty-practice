package top.palexu.netty.im.protocol;

/**
 * @author palexu * @since 2019/06/26 14:52
 */
public interface Command {
    Byte LOGIN_REQUEST = 1;
    Byte LOGIN_RESPONSE = 2;
    Byte MESSAGE_REQUEST = 3;
    Byte MESSAGE_RESPONSE = 4;
    Byte HEARTBEAT_REQUEST = 5;
    Byte HEARTBEAT_RESPONSE = 6;
}
