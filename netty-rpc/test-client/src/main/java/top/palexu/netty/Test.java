package top.palexu.netty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author palexu * @since 2019/06/28 23:31
 */
@Service
@Slf4j
public class Test {
    private RpcProxy rpcProxy;

    @Autowired
    public Test(RpcProxy rpcProxy) {
        this.rpcProxy = rpcProxy;

        IBookService iBookService = this.rpcProxy.create(IBookService.class);
        Book book = iBookService.getBook("harry potter");
        log.info("test prc call {}", book);
    }
}
