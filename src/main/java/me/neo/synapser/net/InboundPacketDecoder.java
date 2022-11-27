package me.neo.synapser.net;

import me.neo.synapser.types.Longs;
import me.neo.synapser.types.VarInt;
import me.neo.synapser.utils.SLogger;

import java.util.Arrays;
import java.util.UUID;

public class InboundPacketDecoder {
    private byte[] data;
    private int length;
    private int packetId;
    public InboundPacketDecoder(byte[] data) {
        this.data = data;
        this.length = getVarInt();
        this.packetId = getVarInt();
    }

    private void discard(int amount) {
        data = Arrays.copyOfRange(data, amount, data.length);
    }

    public boolean hasLeftovers() {
        return this.data.length > 0;
    }

    public int getLength() {
        return length;
    }

    public int getPacketId() {
        return packetId;
    }

    public byte[] getData() {
        return data;
    }

    public int getVarInt() {
        int value = VarInt.read(data);
        discard(VarInt.create(value).length);
        return value;
    }

    public String getString() {
        int length = getVarInt();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++) {
            output.append((char) data[i]);
        }
        discard(length);
        return output.toString();
    }

    public int getUnsignedShort() {
        int value = (data[0] & 0xff) + ((data[1] & 0xff) << 8);
        discard(2);
        return value;
    }

    public boolean getBoolean() {
        boolean bool = data[0] == 0x01;
        discard(1);
        return bool;
    }

    public long getLong() {
        byte[] longBytes = Arrays.copyOf(data, Long.BYTES);
        discard(Long.BYTES);
        return Longs.bytesToLong(longBytes);
    }

    public UUID getUUID() {
        UUID uuid = new UUID(getLong(), getLong());
        return uuid;
    }

    public byte[] getBytes(int amount) {
        byte[] bytes = new byte[amount];
        System.arraycopy(data, 0, bytes, 0, amount);
        discard(amount);
        return bytes;
    }
}
