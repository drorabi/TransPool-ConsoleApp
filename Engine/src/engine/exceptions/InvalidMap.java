package engine.exceptions;

public class InvalidMap extends Exception {
    String size;
    private final String EXCEPTION_MESSAGE;
    public InvalidMap(String size){
        this.size =size;
        EXCEPTION_MESSAGE ="ERROR: The map's size is invalid because the " + this.size;
    }
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
