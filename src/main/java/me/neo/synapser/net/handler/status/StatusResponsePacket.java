package me.neo.synapser.net.handler.status;

import me.neo.synapser.minecraft.ServerInfo;
import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.OutboundPacketBuilder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.SLogger;

public class StatusResponsePacket extends Packet {
    public StatusResponsePacket() {
        super(Bound.CLIENT, SessionState.STATUS, 0x00);
    }

    @Override
    public void handle(PlayerSession session, OutboundPacketBuilder outbound) {
        outbound.addString(ServerInfo.getInfo());
        InboundPacketDecoder decoder = new InboundPacketDecoder(outbound.build());
        session.send(outbound);
    }
}
