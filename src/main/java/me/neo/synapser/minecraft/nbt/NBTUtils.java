package me.neo.synapser.minecraft.nbt;

import me.neo.synapser.types.Longs;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NBTUtils {
    public static void addShort(List<Byte> output, short value) {
        ByteBuffer buf = ByteBuffer.allocate(Short.BYTES);
        buf.putShort(value);
        buf.flip();
        addBytes(output, buf.array());
    }

    public static void addInt(List<Byte> output, int value) {
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
        buf.putInt(value);
        buf.flip();
        addBytes(output, buf.array());
    }

    public static void addLong(List<Byte> output, long value) {
        addBytes(output, Longs.longToBytes(value));
    }

    public static void addFloat(List<Byte> output, float value) {
        ByteBuffer buf = ByteBuffer.allocate(Float.BYTES);
        buf.putFloat(value);
        buf.flip();
        addBytes(output, buf.array());
    }

    public static void addDouble(List<Byte> output, double value) {
        ByteBuffer buf = ByteBuffer.allocate(Double.BYTES);
        buf.putDouble(value);
        buf.flip();
        addBytes(output, buf.array());
    }

    public static void addByte(List<Byte> output, byte data) {
        output.add(data);
    }

    public static void addString(List<Byte> output, String data) {
        addShort(output, (short) data.length());
        addBytes(output, data.getBytes(StandardCharsets.UTF_8));
    }

    public static void addBytes(List<Byte> output, byte[] data) {
        for (byte b : data) {
            output.add(b);
        }
    }

    public static void addBytes(List<Byte> output, List<Byte> data) {
        output.addAll(data);
    }
}
