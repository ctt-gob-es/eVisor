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
 * <b>File:</b><p>es.gob.tsa.persistence.configuration.model.bo.ConfigurationManagerBOs.java.</p>
 * <b>Description:</b><p>Class manager for the Business Objects in configuration scheme.</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * <b>Date:</b><p>14/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import es.gob.signaturereport.persistence.maudit.model.interfaz.IActionTypeBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IFieldBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IServicesBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ISoapsBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITraceFieldBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITraceTransactionBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO;

/**
 * <p>Class manager for the Business Objects in configuration scheme.</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * @version 1.0, 14/05/2013.
 */
@Singleton
@ManagedBean
public class AuditManagerBOs {

    /**
     * Attribute that represents the unique instance of this class.
     */
    private static AuditManagerBOs instance = null;
    
    /**
     * Attribute that implements all the operations related with the management of action types.
     */
    @Inject
    private IActionTypeBO actionTypeBO;
    
    /**
     * Attribute that implements all the operations related with the management of event logs.
     */
    @Inject
    private IEventLogBO eventLogBO;
    
    /**
     * Attribute that implements all the operations related with the management of fields.
     */
    @Inject
    private IFieldBO fieldBO;
    
    /**
     * Attribute that implements all the operations related with the management of services.
     */
    @Inject
    private IServicesBO servicesBO;
    
    /**
     * Attribute that implements all the operations related with the management of organizational SOAPS messages.
     */
    @Inject
    private ISoapsBO soapsBO;
    
    /**
     * Attribute that implements all the operations related with the management of trace fields.
     */
    @Inject
    private ITraceFieldBO traceFieldBO;
    
    /**
     * Attribute that implements all the operations related with the management of trace transactions.
     */
    @Inject
    private ITraceTransactionBO traceTransactionBO;
    
    /**
     * Attribute that implements all the operations related with the management of transactions.
     */
    @Inject
    private ITransactionBO transactionBO;

    /**
     * Method that obtains the instance of the class.
     * @return the unique instance of the class.
     */
    public static AuditManagerBOs getInstance() {
	if (instance == null) {
	    instance = new AuditManagerBOs();
	}
	return instance;
    }

    /**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {
	instance = this;
    }

    /**
     * Method that destroy the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
	instance = null;
    }

	
	/**
	 * Gets the value of the attribute {@link #actionTypeBO}.
	 * @return the value of the attribute {@link #actionTypeBO}.
	 */
	public IActionTypeBO getActionTypeBO() {
		return actionTypeBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #actionTypeBO}.
	 * @param actionTypeBO The value for the attribute {@link #actionTypeBO}.
	 */
	public void setActionTypeBO(IActionTypeBO actionTypeBO) {
		this.actionTypeBO = actionTypeBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #eventLogBO}.
	 * @return the value of the attribute {@link #eventLogBO}.
	 */
	public IEventLogBO getEventLogBO() {
		return eventLogBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #eventLogBO}.
	 * @param eventLogBO The value for the attribute {@link #eventLogBO}.
	 */
	public void setEventLogBO(IEventLogBO eventLogBO) {
		this.eventLogBO = eventLogBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #fieldBO}.
	 * @return the value of the attribute {@link #fieldBO}.
	 */
	public IFieldBO getFieldBO() {
		return fieldBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #fieldBO}.
	 * @param fieldBO The value for the attribute {@link #fieldBO}.
	 */
	public void setFieldBO(IFieldBO fieldBO) {
		this.fieldBO = fieldBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #servicesBO}.
	 * @return the value of the attribute {@link #servicesBO}.
	 */
	public IServicesBO getServicesBO() {
		return servicesBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #servicesBO}.
	 * @param servicesBO The value for the attribute {@link #servicesBO}.
	 */
	public void setServicesBO(IServicesBO servicesBO) {
		this.servicesBO = servicesBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #soapsBO}.
	 * @return the value of the attribute {@link #soapsBO}.
	 */
	public ISoapsBO getSoapsBO() {
		return soapsBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #soapsBO}.
	 * @param soapsBO The value for the attribute {@link #soapsBO}.
	 */
	public void setSoapsBO(ISoapsBO soapsBO) {
		this.soapsBO = soapsBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #traceFieldBO}.
	 * @return the value of the attribute {@link #traceFieldBO}.
	 */
	public ITraceFieldBO getTraceFieldBO() {
		return traceFieldBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #traceFieldBO}.
	 * @param traceFieldBO The value for the attribute {@link #traceFieldBO}.
	 */
	public void setTraceFieldBO(ITraceFieldBO traceFieldBO) {
		this.traceFieldBO = traceFieldBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #traceTransactionBO}.
	 * @return the value of the attribute {@link #traceTransactionBO}.
	 */
	public ITraceTransactionBO getTraceTransactionBO() {
		return traceTransactionBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #traceTransactionBO}.
	 * @param traceTransactionBO The value for the attribute {@link #traceTransactionBO}.
	 */
	public void setTraceTransactionBO(ITraceTransactionBO traceTransactionBO) {
		this.traceTransactionBO = traceTransactionBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #transactionBO}.
	 * @return the value of the attribute {@link #transactionBO}.
	 */
	public ITransactionBO getTransactionBO() {
		return transactionBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #transactionBO}.
	 * @param transactionBO The value for the attribute {@link #transactionBO}.
	 */
	public void setTransactionBO(ITransactionBO transactionBO) {
		this.transactionBO = transactionBO;
	}
	
}
