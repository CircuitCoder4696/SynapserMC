package me.neo.synapser.utils.config;

import java.util.function.Function;

public enum Validator {
    PORT((value) -> {
        if (value instanceof Integer val) {
            return val > 0 && val <= 65535;
        }
        return false;
    });
    private final Function<Object, Boolean> func;
    public boolean validate(Object obj) {
        return func.apply(obj);
    }
    Validator(Function<Object, Boolean> func) {
        this.func = func;
    }
}
