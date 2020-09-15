package engine.exceptions;

public class InvalidRideStartMinutes extends Exception {
    String owner;
    final String EXCEPTION_MESSAGE;
    public InvalidRideStartMinutes(String owner){
        this.owner=owner;
        EXCEPTION_MESSAGE=owner + "'s ride is invalid, minutes number should be between 0 to 59";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}

