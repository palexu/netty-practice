package top.palexu.netty.im.util;

import io.netty.channel.Channel;
import top.palexu.netty.im.protocol.XtalkAttributes;

/**
 * @author junyu.x@shulidata.com
 * @since 2019/06/26 21:19
 */
public class LoginUtil {
    public static void setLogin(Channel channel) {
        channel.attr(XtalkAttributes.LOGIN).set(true);
    }

    public static boolean isLogin(Channel channel) {
        return channel.attr(XtalkAttributes.LOGIN).get() != null;
    }

}
