package engine.exceptions;

public class InvalidRequestHour extends Exception {
    int hour;
    private final String EXCEPTION_MESSAGE;
    public InvalidRequestHour(int hour){
        this.hour =hour;
        EXCEPTION_MESSAGE ="ERROR: " + hour + " is an invalid number of hour, number of hour has to be between 0 to 23";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
