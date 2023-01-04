package me.neo.synapser.minecraft.nbt;

import me.neo.synapser.utils.SLogger;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class NBTReader {
    private final InputStream reader;
    public NBTReader(InputStream reader) {
        this.reader = reader;
    }

    private int position = 0;

    List<Byte> input = new ArrayList<>();
    public NBTCompound read() throws NBTReadException, IOException {
        for (byte b : reader.readAllBytes()) {
            input.add(b);
        }

        NBTCompound root = null;

        if (input.get(0) != NBTID.TAG_COMPOUND.getId()) throw new NBTReadException("NBT stream does not start with a NBT Compound Tag");
        root = (NBTCompound) getTag(true, null);

        return root;
    }

    private NBTTag getTag(boolean hasName, NBTID useId) {
        NBTID id = useId;
        if (id == null) id = NBTID.fromInt(input.get(position++));
        if (id == NBTID.TAG_END) {
            SLogger.getGlobal().debug("End");
            return null;
        }
        String name = null;
        if (hasName) name = getString();
        switch (id) {
            case TAG_COMPOUND -> {
                SLogger.getGlobal().debug("Compound (%s)", name);
                NBTCompound compound = new NBTCompound(name);
                NBTTag tag;
                while ((tag = getTag(true, null)) != null) {
                    compound.addTag(tag);
                }
                return compound;
            }
            case TAG_BYTE -> {
                byte b = input.get(position++);
                SLogger.getGlobal().debug("Byte (%s) %d", name, b);
                return new NBTByte(name, b);
            }
            case TAG_SHORT -> {
                short v = getShort();
                SLogger.getGlobal().debug("Short (%s) %d", name, v);
                return new NBTShort(name, v);
            }
            case TAG_INT -> {
                int v = getInt();
                SLogger.getGlobal().debug("Int (%s) %d", name, v);
                return new NBTInt(name, v);
            }
            case TAG_LONG -> {
                long v = getLong();
                SLogger.getGlobal().debug("Long (%s) %d", name, v);
                return new NBTLong(name, v);
            }
            case TAG_FLOAT -> {
                float v = getFloat();
                SLogger.getGlobal().debug("Float (%s) %f", name, v);
                return new NBTFloat(name, v);
            }
            case TAG_DOUBLE -> {
                double v = getDouble();
                SLogger.getGlobal().debug("Double (%s) %f", name, v);
                return new NBTDouble(name, v);
            }
            case TAG_BYTE_ARRAY -> {
                int length = getInt();
                NBTByteArray nbtByteArray = new NBTByteArray(name, new ArrayList<>());
                for (int i = 0; i < length; i++, position++) {
                    nbtByteArray.addByte(input.get(position));
                }
                SLogger.getGlobal().debug("Byte Array (%s) %d", name, length);
                return nbtByteArray;
            }
            case TAG_STRING -> {
                String str = getString();
                SLogger.getGlobal().debug("String (%s) %s", name, str);
                return new NBTString(name, str);
            }
            case TAG_LIST -> {
                NBTID listId = NBTID.fromInt(input.get(position++));
                NBTList<NBTTag> tags = new NBTList<>(name);
                int len = getInt();
                SLogger.getGlobal().debug("List (%s) %d", name, len);
                for (int i = 0; i < len; i++) {
                    tags.addTag(getTag(false, listId));
                }
                return tags;
            }
            case TAG_INT_ARRAY -> {
                int length = getInt();
                NBTIntArray nbtByteArray = new NBTIntArray(name, new ArrayList<>());
                for (int i = 0; i < length; i++, position++) {
                    nbtByteArray.addInt(getInt());
                }
                SLogger.getGlobal().debug("Int Array (%s) %d", name, length);
                return nbtByteArray;
            }
            case TAG_LONG_ARRAY -> {
                int length = getInt();
                NBTLongArray nbtByteArray = new NBTLongArray(name, new ArrayList<>());
                for (int i = 0; i < length; i++, position++) {
                    nbtByteArray.addLong(getLong());
                }
                SLogger.getGlobal().debug("Long Array (%s) %d", name, length);
                return nbtByteArray;
            }
        }
        return null;
    }

    private String getString() {
        short len = getShort();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = (char)((int)input.get(position));
            position++;
            builder.append(c);
        }
        return builder.toString();
    }

    private short getShort() {
        ByteBuffer buf = ByteBuffer.allocate(Short.BYTES);
        for (int i = 0; i < Short.BYTES; i++, position++) {
            buf.put(input.get(position));
        }
        buf.flip();
        return buf.getShort();
    }

    private int getInt() {
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
        for (int i = 0; i < Integer.BYTES; i++, position++) {
            buf.put(input.get(position));
        }
        buf.flip();
        return buf.getInt();
    }

    private float getFloat() {
        ByteBuffer buf = ByteBuffer.allocate(Float.BYTES);
        for (int i = 0; i < Float.BYTES; i++, position++) {
            buf.put(input.get(position));
        }
        buf.flip();
        return buf.getFloat();
    }

    private double getDouble() {
        ByteBuffer buf = ByteBuffer.allocate(Double.BYTES);
        for (int i = 0; i < Double.BYTES; i++, position++) {
            buf.put(input.get(position));
        }
        buf.flip();
        return buf.getDouble();
    }

    private long getLong() {
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        for (int i = 0; i < Long.BYTES; i++, position++) {
            buf.put(input.get(position));
        }
        buf.flip();
        return buf.getLong();
    }

    public static class NBTReadException extends Exception {
        public NBTReadException(String message) {
            super(message);
        }
    }
}
