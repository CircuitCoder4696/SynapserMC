package me.neo.synapser.types;

import java.nio.ByteBuffer;

public class Longs {
    private static ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
    public static long bytesToLong(byte[] bytes) {
        buf.clear();
        buf.put(bytes);
        buf.flip();
        return buf.getLong();
    }

    public static byte[] longToBytes(long v) {
        buf.clear();
        buf.putLong(v);
        buf.flip();
        return buf.array();
    }
}
