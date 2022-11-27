package me.neo.synapser.net.handler.login;

import me.neo.synapser.Synapser;
import me.neo.synapser.minecraft.GameProfile;
import me.neo.synapser.minecraft.entity.Player;
import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.net.handler.PacketRegistry;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.SLogger;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

public class LoginStartPacket extends Packet {
    public LoginStartPacket() {
        super(Bound.SERVER, SessionState.LOGIN, 0x00);
    }

    @Override
    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        String username = decoder.getString();
        session.setUsername(username);
        boolean hasSigData = decoder.getBoolean();
        if (hasSigData) {
            long timestamp = decoder.getLong();
            int pubKeyLen = decoder.getVarInt();
            byte[] pubKey = decoder.getBytes(pubKeyLen);
            try {
                session.setPublicKey(KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey)));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            int signatureLen = decoder.getVarInt();
            byte[] signature = decoder.getBytes(signatureLen);
        }

        if (!Synapser.getOnlineMode()) {
            session.setPlayer(new Player(Synapser.getServer(), new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8)), username)));
        } else {
            PacketRegistry.handle(session, EncryptionRequestPacket.class);
        }
        return true;
    }
}
