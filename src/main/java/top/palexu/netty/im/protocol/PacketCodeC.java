package top.palexu.netty.im.protocol;

import io.netty.buffer.ByteBuf;
import top.palexu.netty.im.protocol.packet.*;
import top.palexu.netty.im.serialize.JSONSerializer;
import top.palexu.netty.im.serialize.Serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author palexu * @since 2019/06/26 15:02
 */
public class PacketCodeC {
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    public static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap = new HashMap<Byte, Class<? extends Packet>>();
    private static final Map<Byte, Serializer> serializerMap = new HashMap<Byte, Serializer>();

    static {
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        serializerMap.put(new JSONSerializer().getSerializerAlgorithm(), new JSONSerializer());
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
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
