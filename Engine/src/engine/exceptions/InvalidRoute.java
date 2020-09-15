package engine.exceptions;

public class InvalidRoute extends Exception {
    String station;
    String name;
    private final String EXCEPTION_MESSAGE;
    public InvalidRoute(String Name,String station){
        this.station =station;
        this.name=Name;
        EXCEPTION_MESSAGE ="ERROR: " + this.name  + "'s ride is invalid because its going through unknown path between " + this.station;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
