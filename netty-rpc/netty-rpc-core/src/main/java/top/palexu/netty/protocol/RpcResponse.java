package top.palexu.netty.protocol;

import lombok.Data;

/**
 * @author palexu * @since 2019/06/28 10:52
 */
@Data
public class RpcResponse extends Packet {
    private long requestId;
    private Class<?> returnType;
    private Object data;

    @Override
    byte getCommand() {
        return Command.METHOD_RESPONSE;
    }
}
