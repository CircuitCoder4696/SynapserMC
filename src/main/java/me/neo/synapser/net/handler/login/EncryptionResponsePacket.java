package me.neo.synapser.net.handler.login;

import com.google.gson.JsonObject;
import me.neo.synapser.Synapser;
import me.neo.synapser.minecraft.GameProfile;
import me.neo.synapser.minecraft.Property;
import me.neo.synapser.minecraft.UserAuthenticator;
import me.neo.synapser.minecraft.chat.ChatParser;
import me.neo.synapser.minecraft.chat.TextComponentBase;
import me.neo.synapser.minecraft.entity.Player;
import me.neo.synapser.net.InboundPacketDecoder;
import me.neo.synapser.net.Packet;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.net.handler.PacketRegistry;
import me.neo.synapser.types.Longs;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.SHA;
import me.neo.synapser.utils.SLogger;

import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptionResponsePacket extends Packet {
    public EncryptionResponsePacket() {
        super(Bound.SERVER, SessionState.LOGIN, 0x01);
    }

    @Override
    public boolean handle(PlayerSession session, InboundPacketDecoder decoder) {
        byte[] secret = Synapser.decryptWithPrivate(decoder.getBytes(decoder.getVarInt()));
        session.setSharedSecret(new SecretKeySpec(secret, "AES"));
        boolean hasVerifyToken = decoder.getBoolean();
        TextComponentBase component = ChatParser.parse("Client could not be authenticated.");
        if (hasVerifyToken) {
            byte[] token = Synapser.decryptWithPrivate(decoder.getBytes(decoder.getVarInt()));
            if (!session.checkVerifyToken(token)) {
                session.disconnect(component);
                return false;
            }
        } else {
            long salt = decoder.getLong();
            byte[] messageSignature = decoder.getBytes(decoder.getVarInt());
            try {
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(session.getPublicKey());
                signature.update(session.getVerifyToken());
                signature.update(Longs.longToBytes(salt));

                if (!signature.verify(messageSignature)) {
                    session.disconnect(component);
                    return false;
                }
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                throw new RuntimeException(e);
            }
        }

        SHA.SHA1 hash = new SHA.SHA1();
        hash.update("");
        hash.update(session.getSharedSecret().getEncoded());
        hash.update(Synapser.getKeyPair().getPublic().getEncoded());

        UserAuthenticator.Response res = UserAuthenticator.authenticate(session, hash.hexdigest());
        if (res.status() != 200) {
            session.disconnect(ChatParser.parse("Authentication failed. Authentication server might be down."));
            return false;
        }

        Pattern pattern = Pattern.compile("(.{8})(.{4})(.{4})(.{4})(.{12})");
        Matcher matcher = pattern.matcher(res.data().get("id").getAsString());
        List<String> strings = new ArrayList<>();
        if (matcher.find()) {
            strings.add(matcher.group(1));
            strings.add(matcher.group(2));
            strings.add(matcher.group(3));
            strings.add(matcher.group(4));
            strings.add(matcher.group(5));
        }
        UUID uuid = UUID.fromString(String.join("-", strings));
        session.setPlayer(new Player(Synapser.getServer(), new GameProfile(uuid, session.getUsername())));
        SLogger.getGlobal().info("User Authentication: Account successfully verified.", session.getUsername(), uuid.toString());
        SLogger.getGlobal().info("[%s] Player %s verified with UUID %s", session.getAddress().getHostAddress(), session.getPlayer().getUsername(), session.getPlayer().getUniqueID().toString());

        res.data().get("properties").getAsJsonArray().forEach((jsonElement -> {
            JsonObject prop = jsonElement.getAsJsonObject();
            Set<Property> properties = session.getPlayer().getProfile().getProperties();
            properties.add(new Property(
                    prop.get("name").getAsString(),
                    prop.get("value").getAsString(),
                    prop.get("signature").getAsString()));
        }));
        session.setEncrypted(true);
        PacketRegistry.handle(session, LoginSuccessPacket.class);
        return true;
    }
}
