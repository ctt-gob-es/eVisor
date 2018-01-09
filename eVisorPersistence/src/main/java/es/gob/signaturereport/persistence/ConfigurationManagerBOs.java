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

import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO;

/**
 * <p>Class manager for the Business Objects in configuration scheme.</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * @version 1.0, 14/05/2013.
 */
@Singleton
@ManagedBean
public class ConfigurationManagerBOs {

    /**
     * Attribute that represents the unique instance of this class.
     */
    private static ConfigurationManagerBOs instance = null;
    
    /**
     * Attribute that implements all the operations related with the management of applications.
     */
    @Inject
    private IApplicationModuleBO applicationBO;
    
    /**
     * Attribute that implements all the operations related with the management of keystores.
     */
    @Inject
    private IKeystoreModuleBO keystoreModuleBO;
    
    /**
     * Attribute that implements all the operations related with the management of platforms.
     */
    @Inject
    private IPlatformModuleBO platformModuleBO;
    
    /**
     * Attribute that implements all the operations related with the management of templates.
     */
    @Inject
    private ITemplateModuleBO templateModuleBO;
    
    /**
     * Attribute that implements all the operations related with the management of organizational units.
     */
    @Inject
    private IUoModuleBO uoModuleBO;
    
    /**
     * Attribute that implements all the operations related with the management of users.
     */
    @Inject
    private IUserModuleBO userModuleBO;

    /**
     * Method that obtains the instance of the class.
     * @return the unique instance of the class.
     */
    public static ConfigurationManagerBOs getInstance() {
	if (instance == null) {
	    instance = new ConfigurationManagerBOs();
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
	 * Gets the value of the attribute {@link #applicationBO}.
	 * @return the value of the attribute {@link #applicationBO}.
	 */
	public IApplicationModuleBO getApplicationBO() {
		return applicationBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #applicationBO}.
	 * @param applicationBO The value for the attribute {@link #applicationBO}.
	 */
	public void setApplicationBO(IApplicationModuleBO applicationBO) {
		this.applicationBO = applicationBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #keystoreModuleBO}.
	 * @return the value of the attribute {@link #keystoreModuleBO}.
	 */
	public IKeystoreModuleBO getKeystoreModuleBO() {
		return keystoreModuleBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #keystoreModuleBO}.
	 * @param keystoreModuleBO The value for the attribute {@link #keystoreModuleBO}.
	 */
	public void setKeystoreModuleBO(IKeystoreModuleBO keystoreModuleBO) {
		this.keystoreModuleBO = keystoreModuleBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #platformModuleBO}.
	 * @return the value of the attribute {@link #platformModuleBO}.
	 */
	public IPlatformModuleBO getPlatformModuleBO() {
		return platformModuleBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #platformModuleBO}.
	 * @param platformModuleBO The value for the attribute {@link #platformModuleBO}.
	 */
	public void setPlatformModuleBO(IPlatformModuleBO platformModuleBO) {
		this.platformModuleBO = platformModuleBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #templateModuleBO}.
	 * @return the value of the attribute {@link #templateModuleBO}.
	 */
	public ITemplateModuleBO getTemplateModuleBO() {
		return templateModuleBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #templateModuleBO}.
	 * @param templateModuleBO The value for the attribute {@link #templateModuleBO}.
	 */
	public void setTemplateModuleBO(ITemplateModuleBO templateModuleBO) {
		this.templateModuleBO = templateModuleBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #uoModuleBO}.
	 * @return the value of the attribute {@link #uoModuleBO}.
	 */
	public IUoModuleBO getUoModuleBO() {
		return uoModuleBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #uoModuleBO}.
	 * @param uoModuleBO The value for the attribute {@link #uoModuleBO}.
	 */
	public void setUoModuleBO(IUoModuleBO uoModuleBO) {
		this.uoModuleBO = uoModuleBO;
	}

	
	/**
	 * Gets the value of the attribute {@link #userModuleBO}.
	 * @return the value of the attribute {@link #userModuleBO}.
	 */
	public IUserModuleBO getUserModuleBO() {
		return userModuleBO;
	}

	
	/**
	 * Sets the value of the attribute {@link #userModuleBO}.
	 * @param userModuleBO The value for the attribute {@link #userModuleBO}.
	 */
	public void setUserModuleBO(IUserModuleBO userModuleBO) {
		this.userModuleBO = userModuleBO;
	}
   
}
