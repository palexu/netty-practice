package top.palexu.netty;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * @author palexu * @since 2019/06/28 17:13
 */
@RpcService
@Service("IBookService")
public class BookServiceImpl implements IBookService {
    @Override
    public Book getBook(String bookName) {
        Book book = new Book();
        book.setBookName(bookName);
        book.setBookDate(new Date());
        book.setChinese(true);
        book.setPageSize(new Random().nextInt(300));
        book.setPrice(33.3D);
        return book;
    }
}
