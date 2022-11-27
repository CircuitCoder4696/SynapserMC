package me.neo.synapser.net.handler.login;

import me.neo.synapser.Synapser;
import me.neo.synapser.net.OutboundPacketBuilder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.SLogger;

public class EncryptionRequestPacket extends Packet {
    public EncryptionRequestPacket() {
        super(Bound.CLIENT, SessionState.LOGIN, 0x01);
    }

    @Override
    public void handle(PlayerSession session, OutboundPacketBuilder outbound) {
        outbound.addString("");
        byte[] pubKey = Synapser.getKeyPair().getPublic().getEncoded();
        outbound.addVarInt(pubKey.length);
        outbound.addBytes(pubKey);
        outbound.addVarInt(4);
        outbound.addBytes(session.getVerifyToken());
        session.send(outbound);
    }
}
