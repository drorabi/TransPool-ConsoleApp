package engine.exceptions;

public class InvalidPathNames extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidPathNames(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="ERROR: Two paths are going between " + this.station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
