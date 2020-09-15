package engine.exceptions;

public class InvalidDisplayNoRequests extends Exception {
        private final String EXCEPTION_MESSAGE;

    public InvalidDisplayNoRequests() {

            EXCEPTION_MESSAGE = "ERROR: There are no requests in the system yet!";
        }

        @Override
        public String getMessage() {
            return EXCEPTION_MESSAGE;
        }
    }

