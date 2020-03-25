package util.exception;

/**
 *
 * @author Ko Jia Le
 */
public class InvalidLoginCredentialException extends Exception {

    public InvalidLoginCredentialException() {
    }

     public InvalidLoginCredentialException(String msg) {
        super(msg);
    }
}
