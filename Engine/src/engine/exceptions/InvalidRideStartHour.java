package engine.exceptions;

public class InvalidRideStartHour extends Exception {
    String owner;
    final String EXCEPTION_MESSAGE;
    public InvalidRideStartHour(String owner){
        this.owner=owner;
        EXCEPTION_MESSAGE=owner + "'s ride has an Invalid starting hour, hour must be between 0 to 23";
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}


