package engine.exceptions;

public class NoMatchingRides   extends Exception {
    final String EXCEPTION_MESSAGE="Sorry! there are no matching rides in the system at this moment ";
    public NoMatchingRides(){}
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}