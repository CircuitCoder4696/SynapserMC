package me.neo.synapser.net.handler;

import me.neo.synapser.net.OutboundPacketBuilder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;

import java.util.ArrayList;
import java.util.List;

public class PacketRegistry {
    private static List<Packet> packets = new ArrayList<>();
    public static void register(Packet packet) {
        packets.add(packet);
    }

    public static void handle(PlayerSession session, Class<? extends Packet> pClass) {
        for (Packet packet : packets) {
            if (packet.getClass() == pClass && session.getSessionState() == packet.getSessionState()) {
                packet.handle(session, new OutboundPacketBuilder(packet.getPacketId()));
                break;
            }
        }
    }
}
