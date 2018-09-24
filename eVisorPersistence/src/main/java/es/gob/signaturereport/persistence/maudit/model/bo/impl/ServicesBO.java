package es.gob.signaturereport.persistence.maudit.model.bo.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.em.IAuditEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IServicesBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ServicesPOJO;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServicesBO implements IServicesBO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6724809981047722173L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ServicesBO.class);
	
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private transient IAuditEntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<ServicesPOJO> getAll() throws DatabaseException {
		
		List<ServicesPOJO> services = null;
		
		try {
			
			services = (List<ServicesPOJO>) em.findAll(ServicesPOJO.class);
			
		} catch (PersistenceException pe) {
			
			String msg = Language.getMessage(LanguageKeys.AUD_061);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}
		
		return services;
	}

}
