package top.palexu.netty.protocal;

import lombok.Data;

/**
 * @author palexu * @since 2019/06/28 11:27
 */
@Data
public class Packet {
    public static final int MAGIC_NUMBER = 0x87654321;
    private byte version;
    private byte algorithm;
    private byte command;
}
