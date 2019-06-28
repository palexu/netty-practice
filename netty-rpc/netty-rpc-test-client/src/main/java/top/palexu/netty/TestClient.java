package top.palexu.netty;

import io.netty.channel.Channel;
import top.palexu.netty.consumer.RpcClient;
import top.palexu.netty.protocol.RpcRequest;

/**
 * @author palexu * @since 2019/06/28 14:54
 */
public class TestClient {

    public static void main(String[] args) throws InterruptedException {
        RpcRequest request = new RpcRequest();
        request.setClazz("IBookService");
        request.setMethod("getBook");
        request.setArgs(new String[]{"SCIP"});


        Channel start = new RpcClient().start("127.0.0.1", 10800);
        start.writeAndFlush(request);
    }
}
