package me.neo.synapser.minecraft.nbt;

import java.nio.ByteBuffer;
import java.util.List;

public class NBTInt extends NBTTag {
    private final int data;
    public NBTInt(String name, int v) {
        super(name);
        this.id = NBTID.TAG_INT;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addInt(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addInt(output, this.data);
    }
}
