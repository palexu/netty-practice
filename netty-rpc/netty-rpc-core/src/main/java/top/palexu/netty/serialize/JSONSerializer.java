package top.palexu.netty.serialize;


import com.alibaba.fastjson.JSON;

/**
 * @author palexu * @since 2019/06/26 14:57
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class clazz, byte[] bytes) {
        return (T) JSON.parseObject(bytes, clazz);
    }
}
