package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTLong extends NBTTag {
    private final long data;
    public NBTLong(String name, long v) {
        super(name);
        this.id = NBTID.TAG_LONG;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addLong(output, this.data);
    }

    public void listSerialize(List<Byte> output) {
        NBTUtils.addLong(output, this.data);
    }
}
