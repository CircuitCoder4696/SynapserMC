package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTShort extends NBTTag {
    private final short data;
    public NBTShort(String name, short v) {
        super(name);
        this.id = NBTID.TAG_SHORT;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addShort(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addShort(output, this.data);
    }
}
