package engine.exceptions;

public class InvalidRequestMinutes extends Exception {
    int minutes;
    private final String EXCEPTION_MESSAGE;
    public InvalidRequestMinutes(int minutes){
        this.minutes =minutes;
        EXCEPTION_MESSAGE ="ERROR: " + minutes+ " is an invalid number of minutes, number of minutes has to be between 0 to 59";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}