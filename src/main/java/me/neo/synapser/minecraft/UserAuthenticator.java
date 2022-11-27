package me.neo.synapser.minecraft;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.neo.synapser.net.PlayerSession;
import me.neo.synapser.utils.SLogger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;

public class UserAuthenticator {
    public record Response(int status, JsonObject data) {

    }
    private static String hasJoinedURL = "https://sessionserver.mojang.com/session/minecraft/hasJoined";
     public static Response authenticate(PlayerSession session, byte[] serverHash) {
         SLogger.getGlobal().info("User Authentication: Attempting to login user %s", session.getUsername());
         try {
             URL url = new URL(String.format("%s?username=%s&serverId=%s", hasJoinedURL, session.getUsername(), new BigInteger(serverHash).toString(16)));
             HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
             con.setRequestMethod("GET");
             con.setRequestProperty("Content-Type", "application/json");
             int status = con.getResponseCode();
             BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
             StringBuilder content = new StringBuilder();
             String line;
             while ((line = reader.readLine()) != null) {
                 content.append(line);
             }
             reader.close();
             con.disconnect();
             return new Response(status, JsonParser.parseString(content.toString()).getAsJsonObject());
         } catch (IOException e) {
             throw new RuntimeException(e);
         }

     }
}
