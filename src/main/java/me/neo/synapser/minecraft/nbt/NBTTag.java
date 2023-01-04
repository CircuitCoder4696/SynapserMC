package me.neo.synapser.minecraft.nbt;

import java.util.List;

public abstract class NBTTag {
    protected String name;
    public NBTTag(String name) {
        this.name = name;
    }
    protected NBTID id;
    public abstract void serialize(List<Byte> output);

    public void listSerialize(List<Byte> output) {

    }
}
