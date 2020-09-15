package engine.exceptions;

public class NoOptionalStationsToGoNext extends Exception {
    final String EXCEPTION_MESSAGE="There are no stations to go next\n";
    public NoOptionalStationsToGoNext(){}
    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
