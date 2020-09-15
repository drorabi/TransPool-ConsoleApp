package engine.exceptions;

public class InvalidRequestDepartureDestination extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidRequestDepartureDestination(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="ERROR: the request going through unknown station under the name " + this.station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}