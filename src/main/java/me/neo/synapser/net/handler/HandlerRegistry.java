package me.neo.synapser.net.handler;

import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerRegistry {
    private static List<Packet> packets = new ArrayList<>();
    public static void register(Packet packet) {
        packets.add(packet);
    }

    public static boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        AtomicBoolean handled = new AtomicBoolean(false);
        for (Packet packet : packets) {
            if (packet.getPacketId() == decoder.getPacketId() && session.getSessionState() == packet.getSessionState()) {
                handled.set(packet.handle(session, decoder));
                break;
            }
        }
        return handled.get();
    }
}
