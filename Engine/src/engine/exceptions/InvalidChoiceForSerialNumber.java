package engine.exceptions;

public class InvalidChoiceForSerialNumber extends Exception {
    int size;
    private final String EXCEPTION_MESSAGE;

    public InvalidChoiceForSerialNumber(int size) {
        this.size = size;
        EXCEPTION_MESSAGE = "ERROR: There is no ride with the serial number " + this.size;
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}

