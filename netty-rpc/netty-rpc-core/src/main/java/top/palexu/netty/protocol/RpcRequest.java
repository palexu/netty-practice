package top.palexu.netty.protocol;

import lombok.Data;

/**
 * @author palexu * @since 2019/06/28 10:52
 */
@Data
public class RpcRequest extends Packet {
    private long requestId;
    private String clazz;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] args;

    @Override
    byte getCommand() {
        return Command.METHOD_REQUEST;
    }
}
