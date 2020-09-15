package engine.exceptions;

public class InvalidStationsLocation  extends Exception {
    String station;
    private final String EXCEPTION_MESSAGE;
    public InvalidStationsLocation(String station){
        this.station =station;
        EXCEPTION_MESSAGE ="ERROR:  the station " + this.station + " is out of boundaries";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}

