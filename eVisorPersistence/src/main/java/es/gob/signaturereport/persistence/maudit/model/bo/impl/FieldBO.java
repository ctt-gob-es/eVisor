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
import es.gob.signaturereport.persistence.maudit.model.interfaz.IFieldBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.FieldPOJO;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FieldBO implements IFieldBO {
	
	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -6724809981047722173L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(FieldBO.class);
	
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private transient IAuditEntityManager em;

	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IFieldBO#getAll()
	 */
    @SuppressWarnings("unchecked")
	@Override
	public List<FieldPOJO> getAll() throws DatabaseException {
		
    	List<FieldPOJO> fields = null;
    	
    	try {
    		
    		fields = (List<FieldPOJO>) em.findAll(FieldPOJO.class);
    		
    	} catch (PersistenceException pe) {
    		
    		String msg = Language.getMessage(LanguageKeys.AUD_028);
    		throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
    	
    	}
    	
		return fields;
	}

}
