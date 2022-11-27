package me.neo.synapser;

import me.neo.synapser.exceptions.EulaAgreementException;
import me.neo.synapser.minecraft.entity.Player;
import me.neo.synapser.minecraft.chat.ChatParser;
import me.neo.synapser.minecraft.chat.ITextComponent;
import me.neo.synapser.net.GameServer;
import me.neo.synapser.utils.SLogger;
import me.neo.synapser.utils.config.ServerConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.*;

public class Synapser {
    private static GameServer server;
    private static ServerConfig config;
    private static Set<Player> onlinePlayers = new HashSet<>();
    private static String favicon = "";
    private static KeyPair keyPair;
    private static Cipher encrypter;
    private static Cipher decrypter;


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
    public static KeyPair getKeyPair() { return keyPair; }
    public static boolean getOnlineMode() { return config.getBoolean(ServerConfig.Key.SERVER_ONLINE); }
    public static byte[] decryptWithPrivate(byte[] data) {
        try {
            return decrypter.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] encryptWithPrivate(byte[] data) {
        try {
            return encrypter.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void newKeyPair() {
        try {
            keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            encrypter = Cipher.getInstance("RSA");
            decrypter = Cipher.getInstance("RSA");
            encrypter.init(Cipher.ENCRYPT_MODE, getKeyPair().getPrivate());
            decrypter.init(Cipher.DECRYPT_MODE, getKeyPair().getPrivate());

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
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
