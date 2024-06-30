package imc.rps.exception;

public class PlayerInitializationException extends RuntimeException {

    public PlayerInitializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
