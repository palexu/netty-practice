package top.palexu.netty;

import org.springframework.stereotype.Component;

/**
 * @author palexu * @since 2019/06/28 10:54
 */
@Component
public class ServiceRegistry {
    public String get() {
        return "localhost:8081";
    }
}
