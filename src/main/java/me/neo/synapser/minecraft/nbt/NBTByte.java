package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTByte extends NBTTag {
    private final byte data;

    public NBTByte(String name, byte v) {
        super(name);
        this.id = NBTID.TAG_BYTE;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addByte(output, data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addByte(output, data);
    }
}
