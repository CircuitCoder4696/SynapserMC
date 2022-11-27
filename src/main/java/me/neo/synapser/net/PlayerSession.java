package me.neo.synapser.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.neo.synapser.types.SessionState;
import me.neo.synapser.utils.ByteArray;
import me.neo.synapser.utils.SLogger;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class PlayerSession {
    private final GameServer server;
    private final ChannelHandlerContext channel;
    private int protocolVersion = 0;
    private SocketAddress address;
    private SessionState state = SessionState.HANDSHAKE;
    private long pingTime = 0;

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

    public SocketAddress getAddress() {
        return this.address;
    }

    public void setSessionState(SessionState state) {
        this.state = state;
    }

    public SessionState getSessionState() {
        return this.state;
    }

    ByteBuf buf;
    public void send(OutboundPacketBuilder outbound) {
        byte[] out = outbound.build();
        buf = Unpooled.wrappedBuffer(out);
        InboundPacketDecoder decoder = new InboundPacketDecoder(buf.array());
        SLogger.getGlobal().debug("Outbound Packet: %d %d", decoder.getPacketId(), decoder.getLength());
        SLogger.getGlobal().debug("Outbound Data: %s", ByteArray.arrayToString(decoder.getData()));
        channel.writeAndFlush(buf);
        buf.release();
    }
}
