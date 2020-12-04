package de.fxnm.exceptions;

public class FileScannerException extends Exception {

    public FileScannerException(final String messsage) {
        super(messsage);
    }

    public FileScannerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
