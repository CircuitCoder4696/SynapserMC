package me.neo.synapser.net.handler.status;

import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.OutboundPacketBuilder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.types.SessionState;

public class StatusPongPacket extends Packet {
    public StatusPongPacket() {
        super(Bound.CLIENT, SessionState.STATUS, 0x01);
    }

    @Override
    public void handle(PlayerSession session, OutboundPacketBuilder outbound) {
        outbound.addLong(session.getPingTime());
        session.send(outbound);
    }
}
