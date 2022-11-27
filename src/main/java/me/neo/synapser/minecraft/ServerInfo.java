package me.neo.synapser.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.neo.synapser.Synapser;
import me.neo.synapser.minecraft.chat.TextComponentBase;
import me.neo.synapser.minecraft.entity.Player;

public class ServerInfo {
    public static String Version = "1.19";
    public static int Protocol = 759;

    public static String getInfo() {
        JsonObject obj = new JsonObject();
        JsonObject field;

        field = new JsonObject();
        field.addProperty("name", Version);
        field.addProperty("protocol", Protocol);
        obj.add("version", field);

        field = new JsonObject();
        field.addProperty("max", Synapser.getMaxPlayers());
        JsonArray arr = new JsonArray();
        for (Player p : Synapser.getOnlinePlayers()) {
            JsonObject player = new JsonObject();
            player.addProperty("name", p.getUsername());
            player.addProperty("id", p.getUniqueID().toString());
            arr.add(player);
        }
        field.addProperty("online", Synapser.getOnlinePlayers().size());
        if (Synapser.getOnlinePlayers().size() > 0) field.add("sample", arr);
        obj.add("players", field);
        obj.add("description", ((TextComponentBase)Synapser.getMOTD()).serialize());
        if (!Synapser.getFavicon().equals("")) obj.addProperty("favicon", Synapser.getFavicon());
        obj.addProperty("previewsChat", Synapser.getPreviewsChat());
        obj.addProperty("enforcesSecureChat", false);
        return obj.toString();
    }
}
