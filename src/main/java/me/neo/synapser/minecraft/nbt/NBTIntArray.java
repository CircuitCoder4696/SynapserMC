package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTIntArray extends NBTTag {
    private final List<Integer> data;
    public NBTIntArray(String name, List<Integer> v) {
        super(name);
        this.id = NBTID.TAG_INT_ARRAY;
        this.data = v;
    }

    public void addInt(int value) {
        data.add(value);
    }

    public int getInt(int index) {
        return data.get(index);
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addInt(output, this.data.size());
        for (int i : data) {
            NBTUtils.addInt(output, i);
        }
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addInt(output, this.data.size());
        for (int i : data) {
            NBTUtils.addInt(output, i);
        }
    }
}
