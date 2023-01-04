package me.neo.synapser.minecraft.nbt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NBTWriter {
    private final File file;
    public NBTWriter(File file) {
        this.file = file;
    }

    public void write(NBTCompound root) {
        List<Byte> output = new ArrayList<>();
        root.serialize(output);
        try {
            file.createNewFile();
            OutputStream writer = new FileOutputStream(file);
            for (Byte b : output) {
                writer.write(b);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
