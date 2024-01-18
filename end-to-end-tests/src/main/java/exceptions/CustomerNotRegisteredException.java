package exceptions;

public class CustomerNotRegisteredException extends Exception {
    public CustomerNotRegisteredException() {
        super("Customer not registered");
    }
}