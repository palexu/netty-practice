package top.palexu.netty;

import lombok.Data;

import java.util.Date;

/**
 * @author palexu * @since 2019/06/28 10:45
 */
@Data
public class Book {
    private String bookName;
    private int pageSize;
    private Date bookDate;
    private double price;
    private boolean isChinese;
}
