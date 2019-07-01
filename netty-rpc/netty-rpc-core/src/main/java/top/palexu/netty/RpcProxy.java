package top.palexu.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.palexu.netty.consumer.Rpc;
import top.palexu.netty.consumer.RpcClient;
import top.palexu.netty.protocol.RpcRequest;
import top.palexu.netty.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author palexu * @since 2019/06/28 10:50
 */
@Component
public class RpcProxy {
    @Autowired
    ServiceRegistry serviceRegistry;

    private static final Map<String, Object> resultMap = new HashMap<>();

    private static final ThreadPoolExecutor rpcThreadPool = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    /**
     * 给定接口，创建对应的proxy实体类，
     * 对该proxy实体类任何接口的调用，都会代理给远程服务完成
     *
     * @param interfaceClass 给定接口
     * @param <T>            接口类
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //构造rpc请求
                        RpcRequest request = new RpcRequest();
                        request.setClazz(interfaceClass.getSimpleName());
                        request.setMethod(method.getName());
                        setPramTypes(args, request);
                        request.setArgs(args);

                        //获取服务提供者地址
                        String hostAndPort = serviceRegistry.get();
                        String host = hostAndPort.split(":")[0];
                        int port = Integer.valueOf(hostAndPort.split(":")[1]);

                        //发送请求
                        Rpc rpc = new Rpc();
                        RpcClient.start(rpc, "127.0.0.1", 10800).writeAndFlush(request);
                        RpcResponse response = rpc.get(10, TimeUnit.SECONDS);

                        //获取结果
                        return response.getData();
                    }
                });
    }

    private void setPramTypes(Object[] args, RpcRequest request) {
        Class<?>[] parameterTypes = new Class[args.length];
        int i = 0;
        for (Object arg : args) {
            parameterTypes[i] = arg.getClass();
            i++;
        }
        request.setParameterTypes(parameterTypes);
    }
}
