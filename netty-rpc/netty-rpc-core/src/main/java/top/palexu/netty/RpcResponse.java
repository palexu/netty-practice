package top.palexu.netty;

import lombok.Data;
import top.palexu.netty.protocal.Packet;

/**
 * @author palexu * @since 2019/06/28 10:52
 */
@Data
public class RpcResponse extends Packet {
    private long requestId;
    private Object data;
}
