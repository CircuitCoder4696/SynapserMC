package me.neo.synapser.minecraft.nbt;

import java.util.Arrays;

public enum NBTID {
    TAG_END(0),
    TAG_BYTE(1),
    TAG_SHORT(2),
    TAG_INT(3),
    TAG_LONG(4),
    TAG_FLOAT(5),
    TAG_DOUBLE(6),
    TAG_BYTE_ARRAY(7),
    TAG_STRING(8),
    TAG_LIST(9),
    TAG_COMPOUND(10),
    TAG_INT_ARRAY(11),
    TAG_LONG_ARRAY(12);
    private final int id;
    NBTID(int v) {
        this.id = v;
    }

    public int getId() {
        return id;
    }
    public static NBTID fromInt(int id) {
        return Arrays.stream(NBTID.values()).filter((v) -> v.getId() == id).findFirst().orElse(TAG_END);
    }
}
