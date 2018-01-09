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
import es.gob.signaturereport.persistence.maudit.model.interfaz.IActionTypeBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ActionTypePOJO;

/**
 * <p>Class manager for the Business Objects action types.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ActionTypeBO implements IActionTypeBO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6724809981047722173L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(EventLogBO.class);
	
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private IAuditEntityManager em;
    
	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IActionTypeBO#getAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ActionTypePOJO> getAll() throws DatabaseException {
		
		List<ActionTypePOJO> actionTypes = null;
		
		try {
			
			actionTypes = (List<ActionTypePOJO>) em.findAll(ActionTypePOJO.class);
			
		} catch (PersistenceException pe) {
			
			String msg = Language.getMessage(LanguageKeys.AUD_062);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}
		
		return actionTypes;
	}

}
