package hu.okrim.productreviewappcomplete.exception;

public class UserIsNotActiveException extends RuntimeException {
    public UserIsNotActiveException(String string) {
        super(string);
    }
}
