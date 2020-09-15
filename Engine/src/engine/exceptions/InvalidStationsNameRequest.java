package engine.exceptions;

public class InvalidStationsNameRequest extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidStationsNameRequest(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="ERROR:  there is no station in the system under the name " + station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
