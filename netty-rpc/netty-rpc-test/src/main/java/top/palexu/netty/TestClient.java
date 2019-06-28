package top.palexu.netty;

import top.palexu.netty.consumer.RpcClient;

/**
 * @author palexu * @since 2019/06/28 14:54
 */
public class TestClient {

    public static void main(String[] args) throws InterruptedException {
        RpcRequest request = new RpcRequest();
        request.setClazz("IBookService");
        request.setMethod("getBook");
        request.setArgs(new String[]{"SCIP"});


        new RpcClient().start("127.0.0.1", 10800);
    }
}
