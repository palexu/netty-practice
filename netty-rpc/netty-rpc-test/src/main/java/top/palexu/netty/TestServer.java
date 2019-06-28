package top.palexu.netty;

import top.palexu.netty.producer.RpcServer;

/**
 * @author palexu * @since 2019/06/28 16:26
 */
public class TestServer {
    public static void main(String[] args) throws InterruptedException {
        new RpcServer().start();
    }
}
