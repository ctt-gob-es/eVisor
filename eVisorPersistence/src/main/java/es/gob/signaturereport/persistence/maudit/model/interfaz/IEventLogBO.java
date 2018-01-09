package es.gob.signaturereport.persistence.maudit.model.interfaz;

import java.io.Serializable;
import java.util.Date;

import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.pojo.EventLogPOJO;

/**
 * <p>Interface that defines all the operations related with the management of event logs.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IEventLogBO extends Serializable {
	
	/**
	 * Store the event file.
	 * @param identifier 		Store identifier.		   
	 * @param eventCustodyType 	Custody mode.
	 * @param creationTime	   	Creation date of event file. 
	 * @param file			  	 Event file. Optional.			   
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void storeEventFile(Long identifier, String eventCustodyType, Date creationTime, byte[ ] file) throws DatabaseException;
	
	/**
	 * Evaluates if exists event file with the supplied identifier. 
	 * @param eventId	Event file identifier.
	 * @return	True if exists, otherwise false.
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	boolean existEventFile(Long eventId) throws DatabaseException;
	
	/**
	 * Gets the EventLogPOJO associated with the supplied identifier. 
	 * @param eventId	Event file identifier.
	 * @return	the EventLogPOJO 
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	EventLogPOJO getEventLog(Long eventId) throws DatabaseException;
	
	/**
	 * Updates the content of event record associate to supplied identifier.
	 * @param eventId	Event file identifier.
	 * @param contentToStore	Content to store.
	 * @param storeType Store type. Optional.
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void updateEventContent(Long eventId, byte[ ] contentToStore, String storeType) throws DatabaseException;
}
