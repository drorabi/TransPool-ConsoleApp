package engine.exceptions;

public class NoInformationInTheSystem extends Exception {
        final String EXCEPTION_MESSAGE="There is no data in the system, please load a file first or exit\n";
    public NoInformationInTheSystem(){}
        @Override
        public String getMessage() {return EXCEPTION_MESSAGE;}
}

