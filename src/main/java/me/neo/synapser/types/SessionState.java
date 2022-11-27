package me.neo.synapser.types;

public enum SessionState {
    HANDSHAKE,
    STATUS,
    LOGIN,
    PLAY;

    public static SessionState fromInt(int value) {
        if (value < 0) value = 0;
        if (value > 3) value = 3;
        return SessionState.values()[value];
    }
}
