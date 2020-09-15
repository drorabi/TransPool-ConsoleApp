package engine.exceptions;

public class InvalidStationsCoordinates extends Exception {
    String first;
    String second;
    String coordinates;
    private final String EXCEPTION_MESSAGE;
    public InvalidStationsCoordinates(String first, String second,String coordinates){
        this.first=first;
        this.second=second;
        this.coordinates =coordinates;
        EXCEPTION_MESSAGE ="ERROR: the stations " + first + " and " + second + " has the same coordinates " + coordinates;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
