package engine.exceptions;

public class InvalidPathDepatureDestination extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidPathDepatureDestination(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="ERROR: a path is going through unknown station under the name " + this.station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
