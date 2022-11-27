package me.neo.synapser.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.neo.synapser.Synapser;
import me.neo.synapser.exceptions.EulaAgreementException;
import me.neo.synapser.net.handler.HandlerRegistry;
import me.neo.synapser.net.handler.PacketRegistry;
import me.neo.synapser.net.handler.handshake.HandshakePacket;
import me.neo.synapser.net.handler.status.StatusPingPacket;
import me.neo.synapser.net.handler.status.StatusPongPacket;
import me.neo.synapser.net.handler.status.StatusRequestPacket;
import me.neo.synapser.net.handler.status.StatusResponsePacket;
import me.neo.synapser.utils.SLogger;
import me.neo.synapser.utils.config.ServerConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameServer {
    /**
     * Netty event loops
     */
    private static class NettyGroups {
        public static NioEventLoopGroup create() { return new NioEventLoopGroup(); }
    }

    private final NioEventLoopGroup[] groups = {NettyGroups.create(), NettyGroups.create()};

    private final SLogger logger = new SLogger("Server Thread");
    private final ServerBootstrap bootstrap;
    private final ServerConfig config;
    public GameServer() {
        this.bootstrap = new ServerBootstrap();
        this.config = new ServerConfig();
    }

    public void start() throws EulaAgreementException {
        if (!Synapser.getEulaAgreement()) {
            logger.info("Eula has not been agreed to yet. Please set eula.agreement to true in server.properties if you agree to the EULA on minecraft.net");
            throw new EulaAgreementException();
        }
        try {
            bootstrap.group(groups[0], groups[1])
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyPacketHandler(this))
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String configAddress = config.getString(ServerConfig.Key.SERVER_IP);
            configAddress = configAddress.equals("") ? "0.0.0.0" : configAddress;
            InetAddress address = InetAddress.getByName(configAddress);
            int port = config.getInt(ServerConfig.Key.SERVER_PORT);
            ChannelFuture future = bootstrap.bind(address, port);
            this.setup();
            logger.info("Started Synapser server on %s:%s", address.getHostAddress(), port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException | UnknownHostException e) {
            throw new RuntimeException(e);
        } finally {
            logger.info("Stopping server");
            groups[0].shutdownGracefully();
            groups[1].shutdownGracefully();
        }
    }

    public void setup() {
        HandlerRegistry.register(new HandshakePacket());
        HandlerRegistry.register(new StatusRequestPacket());
        HandlerRegistry.register(new StatusPingPacket());

        PacketRegistry.register(new StatusResponsePacket());
        PacketRegistry.register(new StatusPongPacket());
    }
}
