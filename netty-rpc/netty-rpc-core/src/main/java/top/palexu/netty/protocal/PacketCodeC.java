package top.palexu.netty.protocal;

import io.netty.buffer.ByteBuf;
import top.palexu.netty.RpcRequest;
import top.palexu.netty.RpcResponse;
import top.palexu.netty.serialize.JSONSerializer;
import top.palexu.netty.serialize.Serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author palexu * @since 2019/06/26 15:02
 */
public class PacketCodeC {
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap = new HashMap<Byte, Class<? extends Packet>>();
    private static final Map<Byte, Serializer> serializerMap = new HashMap<Byte, Serializer>();

    static {
        packetTypeMap.put(Command.METHOD_REQUEST, RpcRequest.class);
        packetTypeMap.put(Command.METHOD_RESPONSE, RpcResponse.class);

        serializerMap.put(new JSONSerializer().getSerializerAlgorithm(), new JSONSerializer());
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(Packet.MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        //跳过 magic number
        byteBuf.skipBytes(4);
        //跳过 版本号
        byteBuf.skipBytes(1);
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return serializer.deserialize(requestType, bytes);
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
