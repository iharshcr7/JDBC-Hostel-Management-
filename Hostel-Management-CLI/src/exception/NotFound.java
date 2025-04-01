package exception;

public class NotFound extends Exception {
    public NotFound(String s) {
        super(s);
    }
}

class CapacityExceeded extends Exception {
    public CapacityExceeded(String s) {
        super(s);
    }
}

// Exception for invalid user inputs
class InvalidInput extends Exception {
    public InvalidInput(String s) {
        super(s);
    }
}
