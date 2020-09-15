package engine.exceptions;

public class NoTripsInTheSystem extends Exception {
    private final String EXCEPTION_MESSAGE;

    public NoTripsInTheSystem() {

        EXCEPTION_MESSAGE = "ERROR: There are no trips in the system yet!";
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
