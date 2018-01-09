package es.gob.signaturereport.persistence.malarm.model.bo.interfaz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.malarm.model.pojo.AlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.ReceiversPOJO;

/**
 * <p>Interface that defines all the operations related with the management of alarms.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IAlarmModuleBO extends Serializable {

	/**
	 * Gets an alarm by its identifier.
	 * @param alarmId Alarm identifier.
	 * @return The entity object Alarm
	 */
	AlarmPOJO getAlarm(String alarmId) throws DatabaseException;

	/**
	 * Gets a list of receivers from a list of receiver's mail
	 * @param receivers Array of receiver's mails
	 * @return List of ReceiversPOJO
	 */
	List<ReceiversPOJO> getReceiversListEMail(String[ ] receivers);

	/**
	 * Inserts a new receiver record in database
	 * @param receiverPojo Receiver object
	 * @return boolean indicating if the operation was successful
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */ 
	boolean insertReceiver(ReceiversPOJO receiverPojo) throws DatabaseException;

	/**
	 * Inserts a new alarm log in database
	 * @param logAlarmPojo LogAlarm object
	 * @return boolean indicating if the operation was successful
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	boolean insertLogAlarm(LogAlarmPOJO logAlarmPojo) throws DatabaseException;

	/**
	 * @param commtime Time of the alarm communication
	 * @param logAlarmPKs Array of LOGALARM PKs
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void setCommunicationTime(Date commtime, Long[ ] logAlarmPKs) throws DatabaseException;

	/**
	 * Sets the date of las communication
	 * @param alarmId Alarm identifier
	 * @param commTime Communication time of the alarm
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void setLastCommunication(String alarmId, Date commTime) throws DatabaseException;

	/**
	 * Gets the alarm logs pending communication
	 * @param alarmId Alarm identifier
	 * @return List of alarm logs pending communication
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	List<LogAlarmPOJO> getPendingAlarms(String alarmId) throws DatabaseException;
	
	/**
	 * Update the values of an alarm
	 * @param alarmPOJO Alarm to update
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void updateAlarm(AlarmPOJO alarmPOJO) throws DatabaseException;

	/**
	 * Gets all the alarms
	 * @return List of alarms
	 * @throws DatabaseException If an error occurs. The value might be:<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	List<AlarmPOJO> findAllAlarms() throws DatabaseException;

}
