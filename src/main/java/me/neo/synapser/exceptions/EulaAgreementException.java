package me.neo.synapser.exceptions;

public class EulaAgreementException extends Exception {
    @Override
    public String getMessage() {
        return "The eula has not been agreed to";
    }
}
