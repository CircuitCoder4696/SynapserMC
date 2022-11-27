package me.neo.synapser.net.handler.handshake;

import me.neo.synapser.minecraft.ServerInfo;
import me.neo.synapser.minecraft.chat.ChatParser;
import me.neo.synapser.minecraft.chat.TextComponentBase;
import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.types.SessionState;

public class HandshakePacket extends Packet {
    public HandshakePacket() {
        super(Bound.SERVER, SessionState.HANDSHAKE, 0x00);
    }

    @Override
    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        session.setProtocolVersion(decoder.getVarInt());
        decoder.getString();
        decoder.getUnsignedShort();
        session.setSessionState(SessionState.fromInt(decoder.getVarInt()));
        if (session.getProtocolVersion() < ServerInfo.Protocol) {
            session.disconnect(ChatParser.parse("Outdated client! I'm running " + ServerInfo.Version));
        } else if (session.getProtocolVersion() > ServerInfo.Protocol) {
            session.disconnect(ChatParser.parse("Outdated server! I'm still running " + ServerInfo.Version));
        }

        return true;
    }
}
