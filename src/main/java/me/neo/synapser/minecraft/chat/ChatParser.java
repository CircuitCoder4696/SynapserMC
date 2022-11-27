package me.neo.synapser.minecraft.chat;

import me.neo.synapser.utils.SLogger;

import java.util.ArrayList;
import java.util.List;

public class ChatParser {

    public static TextComponentBase parse(String string) {
        return parse(string, '§');
    }
    public static TextComponentBase parse(String string, char code) {
        int position;
        List<ITextComponent> components = new ArrayList<>();
        components.add(ITextComponent.create());
        for (position = 0; position < string.length(); position++) {
            char c = string.charAt(position);
            ITextComponent component = components.get(components.size() - 1);
            if (c == '\n') {
                ITextComponent newLine = ITextComponent.create();
                newLine.setString("\n");
                components.add(newLine);
                continue;
            }

            if (c != code) {
                component.setString(component.getString() + c);
                continue;
            }

            char formatCode = string.charAt(position + 1);
            position += 1;
            if (FormattingCode.RESET.check(formatCode)) {
                components.add(ITextComponent.create());
            }

            if (component.getString().length() > 0) {
                ITextComponent newComponent = ITextComponent.create();
                setFormat(formatCode, newComponent);
                components.add(newComponent);
            } else {
                setFormat(formatCode, component);
            }
        }

        TextComponentBase componentBase = (TextComponentBase) components.get(0);
        components.remove(0);
        for (ITextComponent component : components) {
            componentBase.appendChild(component);
        }
        return componentBase;
    }

    private static void setFormat(char c, ITextComponent component) {
        if (FormattingCode.OBFUSCATED.check(c)) component.getStyle().setObfuscated(true);
        else if (FormattingCode.BOLD.check(c)) component.getStyle().setBold(true);
        else if (FormattingCode.STRIKETHROUGH.check(c)) component.getStyle().setStrikethrough(true);
        else if (FormattingCode.UNDERLINED.check(c)) component.getStyle().setUnderlined(true);
        else if (FormattingCode.ITALIC.check(c)) component.getStyle().setItalic(true);
        else {
            Color color = Color.getByCode(c);
            if (color != null) {
                component.getStyle().setColor(color);
            }
        }
    }
}
