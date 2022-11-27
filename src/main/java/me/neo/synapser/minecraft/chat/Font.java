package me.neo.synapser.minecraft.chat;

public enum Font {
    UNIFORM("minecraft:uniform"),
    ALT("minecraft:alt"),
    DEFAULT("minecraft:default");
    private final String data;
    Font(String data) {
        this.data = data;
    }
    public String getData() {
        return data;
    }
}
