package me.neo.synapser.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class SLogger implements System.Logger {
    private final String name;
    private final DateFormat format;
    public SLogger(String name) {
        this.name = name;
        format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    }
    private static final SLogger def = new SLogger("default");

    public static SLogger getGlobal() {
        return def;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLoggable(Level level) {
        return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        System.out.printf("%s [%s] [%s] %s\n", format.format(new Date()), Thread.currentThread().threadId(), level.getName(), msg);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        log(level, bundle, String.format(format, params), (Throwable) null);
    }

    public void info(String format, Object... params) {
        log(Level.INFO, null, format, params);
    }

    public void debug(String format, Object... params) {
        log(Level.DEBUG, null, format, params);
    }
}
