package engine.exceptions;

public class InvalidRideStartDay extends Exception {
    String owner;
    final String EXCEPTION_MESSAGE;
    public InvalidRideStartDay(String owner){
        this.owner=owner;
        EXCEPTION_MESSAGE=owner + "'s ride has an Invalid starting day number, day number must be between 1 to 7";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}


