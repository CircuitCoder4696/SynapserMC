package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTLongArray extends NBTTag {
    private final List<Long> data;
    public NBTLongArray(String name, List<Long> v) {
        super(name);
        this.id = NBTID.TAG_LONG_ARRAY;
        this.data = v;
    }

    public void addLong(long value) {
        this.data.add(value);
    }

    public long getLong(int index) {
        return this.data.get(index);
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addInt(output, this.data.size());
        for (long i : data) {
            NBTUtils.addLong(output, i);
        }
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addInt(output, this.data.size());
        for (long i : data) {
            NBTUtils.addLong(output, i);
        }
    }
}
