package me.neo.synapser.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import me.neo.synapser.net.handler.HandlerRegistry;
import me.neo.synapser.utils.ByteArray;
import me.neo.synapser.utils.SLogger;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class NettyPacketHandler extends ChannelHandlerAdapter {
    private SLogger logger = new SLogger("Handler Session");
    private PlayerSession session;
    private GameServer server;
    public NettyPacketHandler(GameServer server) {
        this.server = server;
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.session = new PlayerSession(server, ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        List<Byte> bytes = new ArrayList<>();
        try {
            while (buf.isReadable()) {
                bytes.add(buf.readByte());
            }

            if (!bytes.isEmpty()) {
                byte[] data = new byte[bytes.size()];
                for (int i = 0; i < data.length; i++) {
                    data[i] = bytes.get(i);
                }

                InboundPacketDecoder decoder = new InboundPacketDecoder(data);
                logger.debug("Packet ID: %d, Packet Length: %d", decoder.getPacketId(), decoder.getLength());
                logger.debug("Inbound Data: %s", ByteArray.arrayToString(data));

                boolean handled = HandlerRegistry.handle(session, decoder);
                if (decoder.hasLeftovers() && handled) {
                    logger.debug("Leftover Data: %s", ByteArray.arrayToString(decoder.getData()));
                    ByteBuf leftOver = ByteBufAllocator.DEFAULT.buffer();
                    leftOver.writeBytes(decoder.getData());
                    channelRead(ctx, leftOver);
                }
            }
        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }
}
