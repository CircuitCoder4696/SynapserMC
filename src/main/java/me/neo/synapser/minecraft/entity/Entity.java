package me.neo.synapser.minecraft.entity;

import me.neo.synapser.net.GameServer;

import java.util.concurrent.atomic.AtomicInteger;

public class Entity {
    public static AtomicInteger ENTITY_COUNTER = new AtomicInteger(0);
    private final int entityId;
    private final GameServer server;
    private String customName;
    public Entity(GameServer server) {
        this.entityId = ENTITY_COUNTER.getAndIncrement();
        this.server = server;
    }

    public int getEntityId() {
        return this.entityId;
    }
}
