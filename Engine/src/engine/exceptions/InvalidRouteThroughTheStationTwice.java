package engine.exceptions;

public class InvalidRouteThroughTheStationTwice extends Exception {
    String station;
    String name;
    private final String EXCEPTION_MESSAGE;
    public InvalidRouteThroughTheStationTwice(String Name,String station){
        this.station =station;
        this.name=Name;
        EXCEPTION_MESSAGE ="ERROR: " + this.name  + " ride is invalid because its going through the station " + this.station +" twice";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}