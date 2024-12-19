package work.exceptions;

public class NotFoundException extends ClassNotFoundException {
    public NotFoundException(String message) {
        super(message);
    }
}
