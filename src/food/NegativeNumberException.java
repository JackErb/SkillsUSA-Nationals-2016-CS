package food;

/**
 * A simple Exception subclass that properly expresses the negative number error. It is utilized in the Controller class.
 */
public class NegativeNumberException extends Exception {
    public NegativeNumberException() {
        super("Negative Number Exception");
    }
}
