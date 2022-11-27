package me.neo.synapser.net.handler.login;

import me.neo.synapser.net.OutboundPacketBuilder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.SLogger;

public class LoginSuccessPacket extends Packet {
    public LoginSuccessPacket() {
        super(Bound.CLIENT, SessionState.LOGIN, 0x02);
    }

    @Override
    public void handle(PlayerSession session, OutboundPacketBuilder outbound) {
        SLogger.getGlobal().debug("%s %s", session.getPlayer().getUsername(), session.getPlayer().getUniqueID());
        outbound.addUUID(session.getPlayer().getUniqueID());
        outbound.addString(session.getPlayer().getUsername());
        outbound.addPropertySet(session.getPlayer().getProfile().getProperties());
        session.send(outbound);
    }
}
