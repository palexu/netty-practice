package top.palexu.netty.consumer;

import top.palexu.netty.protocol.RpcResponse;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author palexu * @since 2019/07/01 10:43
 */
public class Rpc {

    private SynchronousQueue<RpcResponse> sq = new SynchronousQueue<>();

    public RpcResponse get(long time, TimeUnit timeUnit) throws InterruptedException {
        return sq.poll(time, timeUnit);
    }

    public boolean put(RpcResponse response, long time, TimeUnit timeUnit) throws InterruptedException {
        return sq.offer(response, time, timeUnit);
    }
}
