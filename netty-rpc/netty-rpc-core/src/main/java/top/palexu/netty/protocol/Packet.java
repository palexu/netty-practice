package top.palexu.netty.protocol;

import lombok.Data;

/**
 * @author palexu * @since 2019/06/28 11:27
 */
@Data
public abstract class Packet {
    public static final int MAGIC_NUMBER = 0x87654321;
    private byte version;
    private byte algorithm;

    abstract byte getCommand();
}
