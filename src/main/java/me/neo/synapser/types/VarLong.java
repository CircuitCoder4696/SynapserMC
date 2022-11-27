package me.neo.synapser.types;

public class VarLong
{
    public static byte[] create(long value)
    {
        byte[] bytes = new byte[10];
        int i = 0;
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0)
        {
            bytes[i++] = (byte)((value & 0x7FL) | 0x80);
            value >>= 7;
        }
        bytes[i++] = (byte)(value & 0x7FL);
        return bytes;
    }

    public static long read(byte[] bytes)
    {
        long value = 0;
        int i = 0;
        long b1 = 0;
        for (byte b : bytes)
        {
            b1 = b;
            if ((b & 0x80) == 0) break;
            value |= ((long)b & 0x7F) << i;
            i += 7;
            if (i > 63) throw new RuntimeException("VarLong is too big");
        }
        return value | (b1 << i);
    }
}