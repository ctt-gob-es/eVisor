package es.gob.signaturereport.services.exception;


/** 
 * <p>Class that manages the exceptions related to MBean services.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/07/2016.
 */
public class MBeanServiceException extends Exception {

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 2552673142462110066L;

    /**
     * Constructor method for the class MBeanServiceException.java.
     */
    public MBeanServiceException() {
	super();
    }

    /**
     * Constructor method for the class MBeanServiceException.java.
     * @param message Error message.
     */
    public MBeanServiceException(String message) {
	super(message);
    }

    /**
     * Constructor method for the class MBeanServiceException.java.
     * @param cause Error cause.
     */
    public MBeanServiceException(Throwable cause) {
	super(cause);

    }

    /**
     * Constructor method for the class MBeanServiceException.java.
     * @param message Error message.
     * @param cause Error cause.
     */
    public MBeanServiceException(String message, Throwable cause) {
	super(message, cause);
    }

}
