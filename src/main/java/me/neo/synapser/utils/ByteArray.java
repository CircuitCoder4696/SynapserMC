package me.neo.synapser.utils;

import java.util.List;

public class ByteArray {
    public static String arrayToString(byte[] array) {
        StringBuilder output = new StringBuilder();
        for (byte b : array) {
            output.append(String.format("%02X ", b));
        }
        return output.toString();
    }

    public static String listToString(List<Byte> list) {
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return arrayToString(data);
    }
}
