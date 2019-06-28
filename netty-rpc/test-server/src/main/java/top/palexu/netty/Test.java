package top.palexu.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author palexu * @since 2019/06/28 17:45
 */
@Component
public class Test {
    private IBookService iBookService;

    @Autowired
    public Test(IBookService iBookService) {
        System.out.println("autowire ibookService ->" + (null != iBookService));
        this.iBookService = iBookService;
    }
}
