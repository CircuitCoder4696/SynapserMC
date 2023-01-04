package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTByteArray extends NBTTag {
    private final List<Byte> data;
    public NBTByteArray(String name, List<Byte> v) {
        super(name);
        this.id = NBTID.TAG_BYTE_ARRAY;
        this.data = v;
    }

    public void addByte(byte value) {
        this.data.add(value);
    }

    public byte getByte(int index) {
        return this.data.get(index);
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addInt(output, this.data.size());
        NBTUtils.addBytes(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addInt(output, this.data.size());
        NBTUtils.addBytes(output, this.data);
    }
}
