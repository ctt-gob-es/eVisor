/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2013-,2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.tsa.services.ServiceMBeanSupport.java.</p>
 * <b>Description:</b><p>Class that implements the start and stop methods for the MBeans. This class must be extended for all MBeans of the platform.</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * <b>Date:</b><p>16/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 16/05/2013.
 */
package es.gob.signaturereport.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

import es.gob.signaturereport.services.exception.MBeanServiceException;

/**
 * <p>Class that implements the start and stop methods for the MBeans. This class must be extended for all MBeans of the platform.</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * @version 1.0, 16/05/2013.
 */
public abstract class ServiceMBeanSupport implements ServiceMBean {

    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ServiceMBeanSupport.class);

    /**
     * Attribute that indicates if the MBean is started (true) or not (false).
     */
    private boolean mbeanStarted = false;

    /**
     * {@inheritDoc}
     * @see es.gob.tsa.services.ServiceMBean#start()
     */
    @Override
    @PostConstruct
    public final void start() throws MBeanServiceException {
	if (isMBeanStarted()) {
	    //LOGGER.warn(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG001, new Object[ ] { getServiceName() }));
	} else {
	    //LOGGER.debug(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG002, new Object[ ] { getServiceName() }));
	    startService();
	    mbeanStarted = true;
	    //LOGGER.debug(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG003, new Object[ ] { getServiceName() }));
	}
    }

    /**
     * {@inheritDoc}
     * @see es.gob.tsa.services.ServiceMBean#stop()
     */
    @Override
    @PreDestroy
    public final void stop() throws MBeanServiceException {
	if (isMBeanStarted()) {
	    //LOGGER.debug(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG004, new Object[ ] { getServiceName() }));
	    stopService();
	    mbeanStarted = false;
	    //LOGGER.debug(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG005, new Object[ ] { getServiceName() }));
	} else {
	    //LOGGER.warn(Language.getFormatResCoreGeneral(ICoreGeneralKeys.SMBS_LOG006, new Object[ ] { getServiceName() }));
	}
    }

    /**
     * {@inheritDoc}
     * @see es.gob.tsa.services.ServiceMBean#isMBeanStarted()
     */
    @Override
    public final boolean isMBeanStarted() {
	return mbeanStarted;
    }

    /**
     * Method that realizes the needed operations to initialize the MBean.
     * @throws MBeanServiceException If the method fails.
     */
    protected abstract void startService() throws MBeanServiceException;

    /**
     * Method that realizes the needed operations to stop the MBean.
     * @throws MBeanServiceException If the method fails.
     */
    protected abstract void stopService() throws MBeanServiceException;

}
