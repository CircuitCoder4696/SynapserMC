package me.neo.synapser.minecraft.nbt;

import java.util.ArrayList;
import java.util.List;

public class NBTCompound extends NBTTag {
    private List<NBTTag> tags = new ArrayList<>();

    public NBTCompound(String name) {
        super(name);
        this.name = name;
        this.id = NBTID.TAG_COMPOUND;
    }

    public void addTag(NBTTag tag) {
        this.tags.add(tag);
    }
    public NBTTag getTag(int index) {
        return this.tags.get(index);
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        for (NBTTag tag : tags) {
            tag.serialize(output);
        }
        NBTUtils.addByte(output, (byte) NBTID.TAG_END.getId());
    }

    @Override
    public void listSerialize(List<Byte> output) {
        for (NBTTag tag : tags) {
            tag.serialize(output);
        }
        NBTUtils.addByte(output, (byte) NBTID.TAG_END.getId());
    }
}
