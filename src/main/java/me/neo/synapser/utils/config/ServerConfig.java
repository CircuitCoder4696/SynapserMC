package me.neo.synapser.utils.config;

import me.neo.synapser.utils.Properties;

import java.util.Objects;

public class ServerConfig {
    private final Properties props;
    public ServerConfig() {
        props = new Properties("server.properties");
        load();
    }

    public void load() {
        props.load();
        for (Key k : Key.values()) {
            if (!props.has(k.path)) {
                props.set(k.path, k.def.toString());
            }
        }
        props.save();
    }

    public int getInt(Key key) {
        if (props.has(key.path)) {
            int value = Integer.parseInt(props.get(key.path));
            if (key.validator != null) {
                if (key.validator.validate(value)) {
                    return value;
                }
            }
            return value;
        }
        props.set(key.path, key.def.toString());
        return (int)key.def;
    }

    public String getString(Key key) {
        if (props.has(key.path)) {
            String value = props.get(key.path);
            if (key.validator != null) {
                if (key.validator.validate(value)) {
                    return value;
                }
            }
            return value;
        }
        props.set(key.path, key.def.toString());
        return (String) key.def;
    }

    public boolean getBoolean(Key key) {
        String str = getString(key);
        return Objects.equals(str, "true");
    }

    public enum Key {
        EULA_AGREEMENT("eula.agreement", false),
        SERVER_PORT("server.port", 25565, Validator.PORT),
        SERVER_IP("server.ip", ""),
        SERVER_MOTD("server.motd", "A Synapser Minecraft Server"),
        SERVER_MAX_PLAYERS("server.max_players", 20),
        SERVER_PREVIEWS_CHAT("server.previews_chat", false);
        private final String path;
        private final Object def;
        private final Validator validator;

        Key(String path, Object def) {
            this(path, def, null);
        }

        Key(String path, Object def, Validator validator) {
            this.path = path;
            this.def = def;
            this.validator = validator;
        }
    }
}
