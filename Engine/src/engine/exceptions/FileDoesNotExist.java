package engine.exceptions;

public class FileDoesNotExist  extends Exception {
    final String EXCEPTION_MESSAGE="ERROR: File does not exist!";
    public FileDoesNotExist(){}
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
