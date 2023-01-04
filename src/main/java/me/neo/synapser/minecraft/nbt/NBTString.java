package me.neo.synapser.minecraft.nbt;

import java.util.List;

public class NBTString extends NBTTag {
    private final String data;
    public NBTString(String name, String v) {
        super(name);
        this.id = NBTID.TAG_STRING;
        this.data = v;
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        NBTUtils.addString(output, this.data);
    }

    @Override
    public void listSerialize(List<Byte> output) {
        NBTUtils.addString(output, this.data);
    }
}
