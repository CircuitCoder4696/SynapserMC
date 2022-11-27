package me.neo.synapser.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Properties {
    private final File file;
    private final Map<String, String> map;
    public Properties(String path) {
        file = new File(path);
        map = new HashMap<>();
    }

    public void load() {
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<String> values = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    values.add(line);
                }

                values.forEach((str) -> {
                    String[] val = str.split("=");
                    map.put(val[0], val.length < 2 ? "" : val[1]);
                });

                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void save() {
        StringBuilder output = new StringBuilder();
        map.forEach((s1, s2) -> {
            output.append(s1).append("=").append(s2).append("\n");
        });
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(output.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean has(String path) {
        return map.containsKey(path);
    }

    public String get(String path) {
        return map.get(path);
    }

    public void set(String path, String value) {
        map.put(path, value);
    }
}
