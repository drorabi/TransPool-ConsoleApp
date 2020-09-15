package engine.exceptions;

public class InvalidDisplayNoRides extends Exception {
    private final String EXCEPTION_MESSAGE;

    public InvalidDisplayNoRides() {

        EXCEPTION_MESSAGE = "ERROR: There are no rides in the system yet!";
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
