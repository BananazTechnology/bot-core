package tech.bananaz.exceptions;

@SuppressWarnings("serial")
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }
}
