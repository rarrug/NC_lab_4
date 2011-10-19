package ttracker.dao.exc;

public class TrackerException extends Exception {

    public TrackerException() {
        super();
    }

    public TrackerException(String message) {
        super(message);
    }

    public TrackerException(Throwable exc) {
        super(exc);
    }

    public TrackerException(String message, Throwable exc) {
        super(message, exc);
    }

}
