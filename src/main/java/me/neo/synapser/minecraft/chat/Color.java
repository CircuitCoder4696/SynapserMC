package me.neo.synapser.minecraft.chat;

public enum Color {
    BLACK("black", '0'),
    DARK_BLUE("dark_blue", '1'),
    DARK_GREEN("dark_green", '2'),
    DARK_CYAN("dark_aqua", '3'),
    DARK_RED("dark_red", '4'),
    PURPLE("purple", '5'),
    GOLD("gold", '6'),
    GRAY("gray", '7'),
    DARK_GRAY("dark_gray", '8'),
    BLUE("blue", '9'),
    GREEN("green", 'a'),
    AQUA("aqua", 'b'),
    RED("red", 'c'),
    PINK("pink", 'd'),
    YELLOW("yellow", 'e'),
    WHITE("white", 'f');
    private final String name;
    private final char code;

    public static Color getByCode(char c) {
        for (Color color : Color.values()) {
            if (color.code == c) return color;
        }
        return null;
    }
    Color(String name, char code) {
        this.name = name;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public char getCode() {
        return code;
    }
}
