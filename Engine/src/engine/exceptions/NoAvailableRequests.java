package engine.exceptions;

public class NoAvailableRequests   extends Exception {
    final String EXCEPTION_MESSAGE="There are no available requests in the system";
    public NoAvailableRequests(){}
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
