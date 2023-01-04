package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTDouble extends NBTTag {
    private final double data;
    public NBTDouble(String name, double v) {
        super(name);
        this.id = NBTID.TAG_DOUBLE;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addDouble(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addDouble(output, this.data);
    }
}
