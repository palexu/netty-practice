package top.palexu.netty.im.protocol.packet;

import lombok.Data;

/**
 * @author palexu * @since 2019/06/26 14:51
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     *
     * @return
     */
    public abstract Byte getCommand();
}
