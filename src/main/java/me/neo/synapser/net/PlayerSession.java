package me.neo.synapser.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import me.neo.synapser.minecraft.chat.TextComponentBase;
import me.neo.synapser.minecraft.entity.Player;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.ByteArray;
import me.neo.synapser.utils.SLogger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.random.RandomGenerator;

public class PlayerSession {
    private final GameServer server;
    private final ChannelHandlerContext channel;
    private int protocolVersion = 0;
    private SocketAddress address;
    private SessionState state = SessionState.HANDSHAKE;
    private long pingTime = 0;
    private Player player;
    private byte[] verifyToken;
    private String username;
    private SecretKey sharedSecret;
    private PublicKey publicKey;
    private Cipher encrypter;
    private Cipher decrypter;
    private boolean encrypted;

    public PlayerSession(GameServer server, ChannelHandlerContext channel) {
        this.server = server;
        this.channel = channel;
        this.address = channel.channel().remoteAddress();
    }

    public void setPingTime(long value) {
        this.pingTime = value;
    }

    public long getPingTime() {
        return this.pingTime;
    }

    public void setProtocolVersion(int value) {
        this.protocolVersion = value;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public InetAddress getAddress() {
        return ((InetSocketAddress)this.address).getAddress();
    }

    public void setSessionState(SessionState state) {
        this.state = state;
    }

    public SessionState getSessionState() {
        return this.state;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return this.username; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
    public boolean getEncrypted() { return this.encrypted; }
    public void setSharedSecret(SecretKey key) {
        this.sharedSecret = key;
        try {
            this.encrypter = Cipher.getInstance("AES/CFB8/NoPadding");
            this.encrypter.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
            this.decrypter = Cipher.getInstance("AES/CFB8/NoPadding");
            this.decrypter.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    public SecretKey getSharedSecret() {
        return this.sharedSecret;
    }
    public void setPublicKey(PublicKey key) {
        this.publicKey = key;
    }
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    public boolean checkVerifyToken(byte[] token) {
        if (token.length < 4) return false;
        for (int i = 0; i < 4; i++) {
            if (token[i] != verifyToken[i]) return false;
        }
        return true;
    }

    public byte[] getVerifyToken() {
        if (verifyToken == null) {
            verifyToken = new byte[4];
            RandomGenerator.getDefault().nextBytes(verifyToken);
        }
        return verifyToken;
    }

    public byte[] encryptData(byte[] data) {
        try {
            return encrypter.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decryptData(byte[] data) {
        try {
            return decrypter.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    ByteBuf buf;
    public void send(OutboundPacketBuilder outbound) {
        byte[] out = outbound.build();
        InboundPacketDecoder decoder = new InboundPacketDecoder(out);
        if (getEncrypted()) {
            out = encryptData(out);
        }
        buf = Unpooled.wrappedBuffer(out);
        SLogger.getGlobal().debug("Outbound Packet: %d %d", decoder.getPacketId(), decoder.getLength());
        SLogger.getGlobal().debug("Outbound Data: %s", ByteArray.arrayToString(decoder.getData()));
        channel.writeAndFlush(buf);
    }

    public void send(TextComponentBase component) {
        OutboundPacketBuilder builder = null;
        if (getSessionState() == SessionState.LOGIN) {
            builder = new OutboundPacketBuilder(0x00);
        } else if (getSessionState() == SessionState.PLAY) {
            builder = new OutboundPacketBuilder(0x19);
        }
        if (builder != null) builder.addTextComponent(component);
    }

    public void disconnect() {
        TextComponentBase component = new TextComponentBase();
        component.setString("Reason not specified.");
        disconnect(component);
    }
    public void disconnect(TextComponentBase component) {
        if (getSessionState() == SessionState.PLAY || getSessionState() == SessionState.LOGIN) {
            send(component);
        } else {
            channel.close();
        }
    }
}
