package me.neo.synapser.minecraft.chat;

public enum FormattingCode {
    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINED('n'),
    ITALIC('o'),
    RESET('r');
    private final char code;
    FormattingCode(char code) {
        this.code = code;
    }

    public boolean check(char c) {
        return code == c;
    }
}
