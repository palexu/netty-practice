package top.palexu.netty;

import lombok.Data;
import top.palexu.netty.protocal.Packet;

/**
 * @author palexu * @since 2019/06/28 10:52
 */
@Data
public class RpcRequest extends Packet {
    private long requestId;
    private String clazz;
    private String method;
    private Object[] args;
}
