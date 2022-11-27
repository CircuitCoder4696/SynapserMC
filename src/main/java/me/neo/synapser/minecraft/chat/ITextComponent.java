package me.neo.synapser.minecraft.chat;

public interface ITextComponent {
    static ITextComponent create() {
        return new TextComponentBase();
    }
    void setString(String str);
    String getString();
    void setInsertionString(String str);
    String getInsertionString();

    void setStyle(Style style);
    Style getStyle();
}
