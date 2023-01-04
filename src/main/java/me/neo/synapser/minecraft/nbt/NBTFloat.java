package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTFloat extends NBTTag {
    private final float data;
    public NBTFloat(String name, float v) {
        super(name);
        this.id = NBTID.TAG_FLOAT;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addFloat(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addFloat(output, this.data);
    }
}
