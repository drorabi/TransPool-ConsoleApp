package engine.exceptions;

public class InvalidRequestName extends Exception {
    int name;
    private final String EXCEPTION_MESSAGE;
    public InvalidRequestName(int name){
        this.name =name;
        EXCEPTION_MESSAGE ="ERROR: there is no request under the serial number " + this.name ;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
