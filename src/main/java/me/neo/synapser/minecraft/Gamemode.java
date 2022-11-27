package me.neo.synapser.minecraft;

import java.util.Arrays;

public enum Gamemode {
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);
    private final int id;
    Gamemode(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Gamemode fromInt(int v) {
        return Arrays.stream(Gamemode.values()).filter((predicate) -> predicate.id == v).findFirst().orElse(SURVIVAL);
    }
}
