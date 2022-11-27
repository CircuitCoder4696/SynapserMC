package me.neo.synapser;

import me.neo.synapser.exceptions.EulaAgreementException;
import me.neo.synapser.minecraft.Player;
import me.neo.synapser.minecraft.chat.ChatParser;
import me.neo.synapser.minecraft.chat.ITextComponent;
import me.neo.synapser.net.GameServer;
import me.neo.synapser.utils.SLogger;
import me.neo.synapser.utils.config.ServerConfig;

import java.io.*;
import java.util.*;

public class Synapser {
    private static GameServer server;
    private static ServerConfig config;
    private static Set<Player> onlinePlayers = new HashSet<>();
    private static String favicon = "";

    public static GameServer getServer() {
        return server;
    }

    public static ServerConfig getConfig() {
        return config;
    }

    public static ITextComponent getMOTD() {
        return ChatParser.parse(config.getString(ServerConfig.Key.SERVER_MOTD), '&');
    }

    public static int getMaxPlayers() {
        return config.getInt(ServerConfig.Key.SERVER_MAX_PLAYERS);
    }

    public static Set<Player> getRawOnlinePlayers() {
        return onlinePlayers;
    }

    public static Collection<Player> getOnlinePlayers() {
        return Collections.unmodifiableSet(onlinePlayers);
    }

    public static String getFavicon() {
        return favicon;
    }

    public static boolean getPreviewsChat() {
        return config.getBoolean(ServerConfig.Key.SERVER_PREVIEWS_CHAT);
    }

    public static boolean getEulaAgreement() {
        return config.getBoolean(ServerConfig.Key.EULA_AGREEMENT);
    }

    public static void main(String[] args) throws IOException, EulaAgreementException {
        config = new ServerConfig();
        server = new GameServer();

        File faviconFile = new File("icon.png");
        if (faviconFile.exists()) {
            InputStream reader = new FileInputStream(faviconFile);
            byte[] bytes = reader.readAllBytes();
            reader.close();

            favicon = String.format("data:image/png;base64,%s", Base64.getEncoder().encodeToString(bytes));
            SLogger.getGlobal().debug(favicon);
        }

        server.start();
    }
}
