package top.palexu.netty.im.util;

import io.netty.channel.Channel;
import top.palexu.netty.im.protocol.XtalkAttributes;
import top.palexu.netty.im.protocol.packet.LoginRequestPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author junyu.x@shulidata.com
 * @since 2019/06/26 21:19
 */
public class UserUtil {

    private static final Map<String, Channel> userIdMap = new HashMap<String, Channel>();

    public static void setLogin(LoginRequestPacket loginRequest, Channel channel) {
        userIdMap.put(loginRequest.getUserId(), channel);
        channel.attr(XtalkAttributes.LOGIN).set(true);

        User user = new User();
        user.setUserId(loginRequest.getUserId());
        user.setUserName(loginRequest.getUsername());
        channel.attr(XtalkAttributes.USER).set(user);
    }

    public static void setLogin(Channel channel) {
        channel.attr(XtalkAttributes.LOGIN).set(true);
    }

    public static boolean isLogin(Channel channel) {
        return channel.attr(XtalkAttributes.LOGIN).get() != null;
    }

    public static User user(Channel channel) {
        return channel.attr(XtalkAttributes.USER).get();
    }

    public static Channel channel(String userId) {
        return userIdMap.get(userId);
    }
}
