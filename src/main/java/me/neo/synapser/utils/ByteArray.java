package me.neo.synapser.utils;

public class ByteArray {
    public static String arrayToString(byte[] array) {
        StringBuilder output = new StringBuilder();
        for (byte b : array) {
            output.append(String.format("%02X ", b));
        }
        return output.toString();
    }
}
