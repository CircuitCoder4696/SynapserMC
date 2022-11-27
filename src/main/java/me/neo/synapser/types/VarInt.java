package me.neo.synapser.types;

public class VarInt {
    public static byte[] create(int value) {
        byte[] bytes = new byte[5];
        int i = 0;
        while ((value & 0xFFFFFF80) != 0L) {
            bytes[i++] = ((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        bytes[i] = ((byte) (value & 0x7F));
        byte[] out = new byte[i + 1];
        for (; i >= 0; i--) {
            out[i] = bytes[i];
        }
        return out;
    }
    public static int read(byte[] bytes) {
        int value = 0;
        int i = 0;
        for (byte b : bytes) {
            value |= (b & 0x7F) << i;
            if ((b & 0x80) == 0) break;
            i += 7;
            if (i > 35) throw new RuntimeException("VarInt is too big");
        }
        return value;
    }
}
