package engine.exceptions;

public class NotXmlFile  extends Exception {
    final String EXCEPTION_MESSAGE="ERROR: The file must end with '.xml'   ";
    public NotXmlFile(){}
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}


