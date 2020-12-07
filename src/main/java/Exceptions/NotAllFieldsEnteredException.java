package Exceptions;

public class NotAllFieldsEnteredException extends Exception {
   public NotAllFieldsEnteredException() {
       super("Not all fields were entered");
   }
}
