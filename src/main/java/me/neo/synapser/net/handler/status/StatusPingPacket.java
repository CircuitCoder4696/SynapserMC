package me.neo.synapser.net.handler.status;

import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.net.handler.PacketRegistry;
import me.neo.synapser.types.SessionState;

public class StatusPingPacket extends Packet {
    public StatusPingPacket() {
        super(Bound.SERVER, SessionState.STATUS, 0x01);
    }

    @Override
    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        session.setPingTime(decoder.getLong());
        PacketRegistry.handle(session, StatusPongPacket.class);
        return true;
    }
}
