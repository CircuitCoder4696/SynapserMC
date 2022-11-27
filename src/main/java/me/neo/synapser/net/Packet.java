package me.neo.synapser.net;

import me.neo.synapser.types.SessionState;

public abstract class Packet {
    private final Bound bound;
    private final int id;
    private final SessionState state;
    public Packet(Bound bound, SessionState state, int id) {
        this.bound = bound;
        this.id = id;
        this.state = state;
    }

    public Bound getBound() {
        return bound;
    }

    public int getPacketId() {
        return id;
    }

    public SessionState getSessionState() {
        return state;
    }

    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        return false;
    }
    public void handle(PlayerSession session, OutboundPacketBuilder outbound) {

    }

    public enum Bound {
        CLIENT,
        SERVER
    }
}
