package io.github.mxiwbr.capturebiomes.exceptions;

/**
 * Gets thrown if a plugin update could not be completed successful
 */
public class UpdateException extends RuntimeException {
    public UpdateException(String message) {
        super(message);
    }
}
