package es.gob.signaturereport.persistence.maudit.model.bo.impl;

import java.util.Date;

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
import es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.EventLogPOJO;

/**
 * <p>Class manager for the Business Objects in configuration module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EventLogBO implements IEventLogBO {

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
    
    // CHECKSTYLE:ON
		
	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO#storeEventFile(Long, String, Date, byte[], boolean)
	 */
	@Override
	public final void storeEventFile(final Long identifier, final String eventCustodyType, final Date creationTime, final byte[ ] file) throws DatabaseException {
		
		try {
			
			final EventLogPOJO eventLog = new EventLogPOJO();
			
			eventLog.setEventLogPK(identifier);
			eventLog.setStoreType(eventCustodyType);
			eventLog.setCreationTime(creationTime);
			
			if (file != null) {
				eventLog.setContent(file);
				eventLog.setStoreTime(new Date());
			}
			
			em.persist(eventLog);
			
		} catch (PersistenceException pe) {
			String msg = Language.getMessage(LanguageKeys.AUD_015);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO#existEventFile(java.lang.Long)
	 */
	@Override
	public final boolean existEventFile(final Long eventId) throws DatabaseException {
		
		try {
				
			final EventLogPOJO eventLog = (EventLogPOJO) em.load(EventLogPOJO.class, eventId);
			
			return (eventLog != null);
			
		} catch (PersistenceException he) {
			String msg = Language.getFormatMessage(LanguageKeys.AUD_018, new Object[ ] { eventId });
			LOGGER.error(msg, he);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,he);
		} 
	}
	
	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO#getEventLog(java.lang.Long)
	 */
	@Override
	public final EventLogPOJO getEventLog(final Long eventId) throws DatabaseException
	{
		return (EventLogPOJO) em.load(EventLogPOJO.class, eventId);
	}
	
	
	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO#updateEventContent(java.lang.Long, byte[], java.lang.String)
	 */
	@Override
	public void updateEventContent(Long eventId, byte[ ] contentToStore, String storeType) throws DatabaseException {
		
		try {
			final EventLogPOJO eventLog = (EventLogPOJO) em.load(EventLogPOJO.class, eventId);
			
			if (eventLog == null) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.AUD_017, new Object[ ] { eventId }));
			}
			eventLog.setContent(contentToStore);
			eventLog.setStoreTime(new Date());
			if (storeType != null) {
				eventLog.setStoreType(storeType);
			}
			
			em.merge(eventLog);
			
		} catch (PersistenceException pe) {
		
			String msg = Language.getFormatMessage(LanguageKeys.AUD_023, new Object[ ] { eventId });
			LOGGER.error(msg, pe);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
}
