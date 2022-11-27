package me.neo.synapser.net;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.neo.synapser.types.Longs;
import me.neo.synapser.types.VarInt;
import me.neo.synapser.utils.SLogger;

public class OutboundPacketBuilder {
	private List<Byte> data = new ArrayList<>();
    private int packetId;

    public OutboundPacketBuilder(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketId() {
        return packetId;
    }
    private void addBytes(byte[] bytes) {
        for (byte b : bytes) {
            addByte(b);
        }
    }

    public OutboundPacketBuilder addByte(byte b) {
        data.add(b);
        return this;
    }

    public OutboundPacketBuilder addVarInt(int value) {
    	addBytes(VarInt.create(value));
    	return this;
    }

    public OutboundPacketBuilder addString(String value) {
    	addVarInt(value.length());
    	addBytes(value.getBytes(StandardCharsets.UTF_8));
    	return this;
    }

    public OutboundPacketBuilder addBoolean(boolean value) {
        addByte((byte) (value ? 0x01 : 0x00));
        return this;
    }

    public OutboundPacketBuilder addLong(long value) {
        addBytes(Longs.longToBytes(value));
        return this;
    }

    public byte[] build() {
        List<Byte> tempData = new ArrayList<>();
        byte[] id = VarInt.create(packetId);
        byte[] len = VarInt.create(data.size() + id.length);
        for (byte b : len) {
            tempData.add(b);
        }
        for (byte b : id) {
            tempData.add(b);
        }
        tempData.addAll(data);

        byte[] out = new byte[tempData.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = tempData.get(i);
        }
        return out;
    }
}
