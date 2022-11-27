package me.neo.synapser.net.handler.status;

import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.net.handler.PacketRegistry;
import me.neo.synapser.types.SessionState;

public class StatusRequestPacket extends Packet {
    public StatusRequestPacket() {
        super(Bound.SERVER, SessionState.STATUS, 0x00);
    }

    @Override
    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        PacketRegistry.handle(session, StatusResponsePacket.class);
        return true;
    }
}
