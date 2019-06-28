package top.palexu.netty.im.protocol;

import io.netty.util.AttributeKey;
import top.palexu.netty.im.util.User;

/**
 * @author junyu.x@shulidata.com
 * @since 2019/06/26 21:18
 */
public interface XtalkAttributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<User> USER = AttributeKey.newInstance("user");
}
