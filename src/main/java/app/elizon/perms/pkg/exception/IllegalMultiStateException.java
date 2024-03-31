package app.elizon.perms.pkg.exception;

public class IllegalMultiStateException extends Exception {

    private final String message;

     public IllegalMultiStateException(String message) {
         this.message = message;
     }

    @Override
    public void printStackTrace() {
        System.err.println("Error in parsing: IllegalMultiStateException (" + message + ")");
        super.printStackTrace();
    }
}
