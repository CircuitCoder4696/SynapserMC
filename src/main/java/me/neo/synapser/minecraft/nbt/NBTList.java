package me.neo.synapser.minecraft.nbt;

import java.util.ArrayList;
import java.util.List;

public class NBTList<T extends NBTTag> extends NBTTag {
    private final List<T> tags = new ArrayList<>();
    public NBTList(String name) {
        super(name);
        this.id = NBTID.TAG_LIST;
    }

    public void addTag(T tag) {
        tags.add(tag);
    }

    public T getTag(int index) {
        return tags.get(index);
    }

    @Override
    public void serialize(List<Byte> output) {
        NBTUtils.addByte(output, (byte)id.getId());
        NBTUtils.addString(output, name);
        if (tags.size() > 0) {
            NBTID id = tags.get(0).id;
            NBTUtils.addByte(output, (byte)id.getId());
            NBTUtils.addInt(output, tags.size());
            for (T tag : tags) {
                tag.listSerialize(output);
            }
        } else {
            NBTUtils.addByte(output, (byte)NBTID.TAG_END.getId());
            NBTUtils.addInt(output, 0);
        }
    }
}
