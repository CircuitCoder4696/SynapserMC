package me.neo.synapser.minecraft;

import java.util.*;

public class GameProfile {
    private final String name;
    private final UUID id;
    private Set<Property> properties = new HashSet<>();
    public GameProfile(UUID id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public UUID getUniqueID() {
        return this.id;
    }
    public Set<Property> getProperties() {
        return this.properties;
    }
}
