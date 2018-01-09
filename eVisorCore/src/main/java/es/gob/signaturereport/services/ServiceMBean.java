package es.gob.signaturereport.services;

import es.gob.signaturereport.services.exception.MBeanServiceException;


/** 
 * <p>Interface that defines the basic methods for create a MBean service..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/07/2016.
 */
public interface ServiceMBean {

    /**
     * Method that starts the service.
     * @throws MBeanServiceException In case of some error.
     */
    void start() throws MBeanServiceException;

    /**
     * Method that stops the service.
     * @throws MBeanServiceException In case of some error.
     */
    void stop() throws MBeanServiceException;

    /**
     * Method that checks if the MBean is started (true) or not (false). All the methods defined in the MBean must consult this method before doing any action, and perform consequently.
     * @return a boolean that indicates if the MBean is started (true) or not (false).
     */
    boolean isMBeanStarted();

}
