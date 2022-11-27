package me.neo.synapser.minecraft.entity;

import me.neo.synapser.minecraft.GameProfile;
import me.neo.synapser.net.GameServer;

import java.util.UUID;

public class Player {
    private final GameServer server;
    private final GameProfile profile;
    private String displayName;

    public Player(GameServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;

        this.displayName = profile.getName();
    }
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getUsername() {
        return this.profile.getName();
    }

    public UUID getUniqueID() {
        return this.profile.getUniqueID();
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}
