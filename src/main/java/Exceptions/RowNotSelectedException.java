package Exceptions;

public class RowNotSelectedException extends Exception {
    public  RowNotSelectedException() {
        super("No row was selected");
    }

}
