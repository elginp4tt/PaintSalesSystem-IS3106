
package util.exception;

/**
 *
 * @author user
 */
public class EmployeeUsernameExistException extends Exception {

    public EmployeeUsernameExistException() {
    }

    public EmployeeUsernameExistException(String msg) {
        super(msg);
    }
}
