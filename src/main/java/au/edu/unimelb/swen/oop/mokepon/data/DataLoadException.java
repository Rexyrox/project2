package au.edu.unimelb.swen.oop.mokepon.data;

// Raised when mokepon.game is missing, unreadable, or malformed.
public class DataLoadException extends Exception {

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataLoadException(String message) {
        super(message);
    }
}
