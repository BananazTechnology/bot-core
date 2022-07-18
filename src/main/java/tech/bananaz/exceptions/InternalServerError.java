package tech.bananaz.exceptions;

@SuppressWarnings("serial")
public class InternalServerError extends RuntimeException {

    public InternalServerError(String message) {
        super(message);
    }
}
