package me.neo.synapser.minecraft.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class TextComponentBase implements ITextComponent {
    List<ITextComponent> components = new ArrayList<>();
    private String string = "";
    private String insertionString;
    private Style style = new Style();
    @Override
    public void setString(String str) {
        this.string = str;
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public void setInsertionString(String str) {
        insertionString = str;
    }

    @Override
    public String getInsertionString() {
        return insertionString;
    }

    public void appendChild(ITextComponent component) {
        components.add(component);
    }

    public List<ITextComponent> getChildren() {
        return components;
    }

    @Override
    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public Style getStyle() {
        return style;
    }

    public JsonObject serialize() {
        JsonObject obj = serialize(this);
        JsonArray extras = new JsonArray();
        components.forEach((component -> {
            extras.add(serialize(component));
        }));
        if (!extras.isEmpty()) obj.add("extras", extras);
        return obj;
    }

    public JsonObject serialize(ITextComponent component) {
        JsonObject obj = new JsonObject();
        obj.addProperty("text", component.getString());
        Style cstyle = component.getStyle();
        if (cstyle != null) {
            if (cstyle.isBold()) obj.addProperty("bold", true);
            if (cstyle.isItalic()) obj.addProperty("italic", true);
            if (cstyle.isObfuscated()) obj.addProperty("obfuscated", true);
            if (cstyle.isStrikethrough()) obj.addProperty("strikethrough", true);
            if (cstyle.isUnderlined()) obj.addProperty("underlined", true);
            if (cstyle.getColor() != null) obj.addProperty("color", cstyle.getColor().getName());
            if (cstyle.getFont() != null) obj.addProperty("font", cstyle.getFont().getData());
        }
        if (component.getInsertionString() != null) {
            obj.addProperty("insertion", component.getInsertionString());
        }
        return obj;
    }
}
