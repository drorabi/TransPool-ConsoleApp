package engine.exceptions;

public class InvalidPathForTrip extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidPathForTrip(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="There is no path between " + this.station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}

