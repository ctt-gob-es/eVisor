// Copyright (C) 2018, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/** 
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.db.AlarmPersistenceFacade.java.</p>
 * <b>Description:</b><p>Class that manages the persistence operations associated to system alarms</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm.access;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.keystore.KeystorePersistenceFacade;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.file.BackupAlarm;
import es.gob.signaturereport.malarm.item.AlarmData;
import es.gob.signaturereport.malarm.item.PendingAlarm;
import es.gob.signaturereport.malarm.item.ReportedAlarm;
import es.gob.signaturereport.persistence.exception.AlarmPersistenceException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO;
import es.gob.signaturereport.persistence.malarm.model.pojo.AlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.ReceiversPOJO;
import es.gob.signaturereport.tools.UniqueNumberGenerator;

/** 
 * <p>Class that manages the persistence operations associated to system alarms.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
@Singleton
@ManagedBean
public class AlarmPersistenceFacade {

	/**
	 * Attribute that represents instance of the class.
	 */
	private static AlarmPersistenceFacade instance = null;
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AlarmPersistenceFacade.class);

	/**
	 * Attribute that represents the property used to identify the ALARM_ID column. 
	 */
	private static final String ALARM_ID_PROP = "alarmId";

	/**
	 * Attribute that represents the property used to identify the 'Get Pending Alarm' query . 
	 */
	private static final String GET_PENDING_QUERY = "getPendingAlarms";

	/**
	 * Attribute that represents the query used to set various alarms as reported. 
	 */
	private static final String COMMTIME_QUERY = "setCommunicationTime";
	/**
	 * Attribute that represents the property used to identify the MAIL column. 
	 */
	private static final String MAIL_PROP = "mail";

	/**
	 * Attribute that represents the property used to identify the COMTIME column. 
	 */
	private static final String COMTIME_PROP = "comtime";
	/**
	 * Attribute that represents the property used to identify the LOG_PK column. 
	 */
	private static final String LOG_PK_LIST_PROP = "logPk_list";

	/**
	 * Attribute that represents the query used to set last communication time. 
	 */
	private static final String LAST_COMM_QUERY = "setLastCommunication";

	/**
	 * Attribute that represents a cache for the alarms information. 
	 */
	private LinkedHashMap<String, AlarmData> alarmCache = new LinkedHashMap<String, AlarmData>();
	
	/**
	 * Attribute that allows to operate with information of the alarms of the platform.
	 */
	@Inject
	private IAlarmModuleBO alarmBO;
	
	/**
	 * Gets a instance of the class.
	 * @return A instance of class.
	 */
	public static AlarmPersistenceFacade getInstance() {

		if (instance == null) {
			instance = new AlarmPersistenceFacade();
		}
		return instance;
	}

	/**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {    	
    	initializeCache();
    	instance = this;
    }

    /**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	alarmCache = null;
    }


	/**
	 * Gets the alarm description.
	 * @param alarmId	Alarm identifier.
	 * @return	Alarm description if the alarm exists. Otherwise null.
	 * @throws AlarmPersistenceException If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If the alarm doesn't exist.<br/>
	 */
	public String getAlarmDescription(String alarmId) throws AlarmPersistenceException{
		if (alarmId == null || alarmId.trim().length() == 0) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}
		AlarmData alarmData = this.alarmCache.get(alarmId);
		if (alarmData != null) {
			return alarmData.getDescription();
		} else {
			throw new AlarmPersistenceException(AlarmPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_115, new Object[ ] { alarmId }));
		}
	}
	/**
	 * Gets the registered information associated to supplied alarm.
	 * @param alarmId	Alarm identifier.
	 * @return	An {@link AlarmData} object that contains the information.
	 * @throws AlarmPersistenceException	If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If the alarm doesn't exist.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public AlarmData getAlarm(final String alarmId) throws AlarmPersistenceException {
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}

		AlarmData ad = null;
		try {

			AlarmPOJO alarm = alarmBO.getAlarm(alarmId);

			if (alarm == null) {
				throw new AlarmPersistenceException(AlarmPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_115, new Object[ ] { alarmId }));
			} else {
				ad = new AlarmData();
				ad.setAlarmId(alarm.getAlarmId());
				ad.setSubject(alarm.getSubject());
				ad.setMessage(alarm.getMessage());
				ad.setLock(alarm.isLock());
				ad.setStandbyTime(alarm.getStandbytime());
				ad.setLastCommunication(alarm.getLastcom());
				ad.setDescription(alarm.getDescription());
				final Set<ReceiversPOJO> recSet = alarm.getReceiverses();
				int numRec = recSet.size();
				if (numRec > 0) {
					String[ ] receivers = new String[numRec];
					Iterator<ReceiversPOJO> recIt = recSet.iterator();
					int i = 0;
					while (recIt.hasNext() && i < numRec) {
						final ReceiversPOJO rec = recIt.next();
						receivers[i] = rec.getMail();
						i++;
					}
					ad.setReceivers(receivers);
				}

			}
		} catch (DatabaseException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.DB_117, new Object[ ] { alarmId }), e);
			// Como actualmente no se tiene conexión a DB devolvemos los valores
			// cacheado
			ad = alarmCache.get(alarmId);
		} finally {
			if (ad != null) {
				// Actualizamos la cache
				alarmCache.put(ad.getAlarmId(), ad);
			}
		}
		return ad;
	}

	/**
	 * Sets the alarm receivers.
	 * @param alarmId	Alarm identifier.
	 * @param receivers	List of receivers.
	 * @throws AlarmPersistenceException	If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If the alarm doesn't exist.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void setAlarmReceivers(String alarmId, String[ ] receivers) throws AlarmPersistenceException {
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}

		try {

			AlarmPOJO alarm = alarmBO.getAlarm(alarmId);

			if (alarm == null) {
				throw new AlarmPersistenceException(AlarmPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_115, new Object[ ] { alarmId }));
			} else {
				if (receivers != null) {

					final List<ReceiversPOJO> recDb = alarmBO.getReceiversListEMail(receivers);

					final LinkedHashMap<String, Long> recs = new LinkedHashMap<String, Long>();
					if (recDb != null && !recDb.isEmpty()) {
						for (int i = 0; i < recDb.size(); i++) {
							ReceiversPOJO r = recDb.get(i);
							recs.put(r.getMail(), r.getRecPk());
						}
					}
					final HashSet<ReceiversPOJO> recsToSave = new HashSet<ReceiversPOJO>();
					for (int i = 0; i < receivers.length; i++) {
						final ReceiversPOJO receiver = new ReceiversPOJO();
						if (recs.containsKey(receivers[i])) {
							receiver.setRecPk(recs.get(receivers[i]));
						} else {
							receiver.setMail(receivers[i]);

							alarmBO.insertReceiver(receiver);
						}

						recsToSave.add(receiver);
					}
					alarm.setReceiverses(recsToSave);
				} else {
					alarm.getReceiverses().clear();
				}

			}
		} catch (DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.DB_113, new Object[ ] { alarmId });
			LOGGER.error(msg, e);
			throw new AlarmPersistenceException(AlarmPersistenceException.UNKNOWN_ERROR, msg,e);
		}  finally {
			// actualizamos la cache
			final AlarmData alarmData = alarmCache.get(alarmId);
			alarmData.setReceivers(receivers);
			alarmCache.put(alarmId, alarmData);
		}
	}

	/**
	 * Add an alarm to the alarm history.
	 * @param alarmId	Alarm identifier.
	 * @param creationTime Time of alarm creation.
	 * @param moreInfo	Additional information.
	 * @throws AlarmPersistenceException If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If the alarm doesn't exist.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void addAlarmToLog(String alarmId, Date creationTime,String moreInfo) throws AlarmPersistenceException {
		long idLog = UniqueNumberGenerator.getInstance().getNumber();
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}

		try {

			final AlarmPOJO alarm = alarmBO.getAlarm(alarmId);

			if (alarm == null) {
				throw new AlarmPersistenceException(AlarmPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_115, new Object[ ] { alarmId }));
			} else {
				final LogAlarmPOJO logAlarm = new LogAlarmPOJO();
				logAlarm.setLogPk(new Long(idLog));
				logAlarm.setAlarm(alarm);
				logAlarm.setCreationtime(creationTime);
				logAlarm.setIslock(alarm.isLock());
				logAlarm.setMoreInfo(moreInfo);
				final Iterator<ReceiversPOJO> itRec = alarm.getReceiverses().iterator();
				String reported = null;
				if (itRec.hasNext()) {
					reported = ((ReceiversPOJO) itRec.next()).getMail();
					while (itRec.hasNext()) {
						reported = reported + "," + ((ReceiversPOJO) itRec.next()).getMail();
					}
				}
				logAlarm.setReported(reported);
				alarmBO.insertLogAlarm(logAlarm);

			}
		} catch (DatabaseException e) {
			BackupAlarm.getInstance().addAlarmToLog(idLog, alarmCache.get(alarmId), creationTime, moreInfo);
			LOGGER.error(Language.getFormatMessage(LanguageKeys.DB_118, new Object[ ] { alarmId }), e);
		}
	}

	/**
	 * Sets the list of alarm as reported.
	 * @param alarmId	Alarm identifier.
	 * @param logIds	Array of history identifiers.
	 * @param communicationDate	Communication time.
	 * @throws AlarmPersistenceException	If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If one or more that identifiers don't exist.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void setAlarmsAsReported(final String alarmId, final Long[ ] logIds, final Date communicationDate) throws AlarmPersistenceException {
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}
		if (logIds == null) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_120));
		}
		if (communicationDate == null) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_121));
		}

		try {

			alarmBO.setCommunicationTime(communicationDate, logIds);

			alarmBO.setLastCommunication(alarmId, communicationDate);

		} catch (DatabaseException e) {
			BackupAlarm.getInstance().setAlarmsAsReported(alarmId, logIds, communicationDate);
			LOGGER.error(Language.getFormatMessage(LanguageKeys.DB_122, new Object[ ] { alarmId }), e);

		} finally {
			// actualizamos la cache
			AlarmData ad = alarmCache.get(alarmId);
			ad.setLastCommunication(communicationDate);
			alarmCache.put(alarmId, ad);
		}
	}

	/**
	 * Gets all history identifier associated to supplied alarm identified and are not yet communication. These alarms can not be locked.
	 * @param alarmId	Alarm identifier.
	 * @return	History identifiers.
	 * @throws AlarmPersistenceException	If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public ArrayList <PendingAlarm> getPendingAlarms(String alarmId) throws AlarmPersistenceException {
		ArrayList <PendingAlarm> alarms = new ArrayList <PendingAlarm>();
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}

		try {

			List<LogAlarmPOJO> listLogAlarm = alarmBO.getPendingAlarms(alarmId);

			Iterator<LogAlarmPOJO> listLogAlarmIt = listLogAlarm.iterator();

			while (listLogAlarmIt.hasNext()) {

				LogAlarmPOJO logAlarm = (LogAlarmPOJO) listLogAlarmIt.next();
				final PendingAlarm pendingAlarm = new PendingAlarm(alarmId, logAlarm.getLogPk(), logAlarm.getCreationtime(), logAlarm.getMoreInfo());
				alarms.add(pendingAlarm);
			}
		} catch (DatabaseException e) {
			alarms = BackupAlarm.getInstance().getPendingAlarms(alarmId);
			LOGGER.error(Language.getFormatMessage(LanguageKeys.DB_119, new Object[ ] { alarmId }), e);
		}
		return alarms;
	}

	/**
	 * Sets the alarm information.
	 * @param alarmId	Alarm identifier.
	 * @param isEnabled	Alarm status.
	 * @param standbyTime Time which the alarm is standby.
	 * @param mails Mails direction. May be null.
	 * @throws AlarmPersistenceException	If an error occurs.  The values might be:<br/>
	 * 		{@link AlarmPersistenceException#INVALID_INPUT_PARAMETERS} If the input parameters is not valid.<br/>
	 * 		{@link AlarmPersistenceException#ITEM_NOT_FOUND} If the alarm doesn't exist.<br/>
	 * 		{@link AlarmPersistenceException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void setAlarmInformation(String alarmId, boolean isEnabled, long standbyTime, String[ ] mails) throws AlarmPersistenceException {
		if (alarmId == null || alarmId.trim().equals("")) {
			throw new AlarmPersistenceException(AlarmPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_114));
		}

		try {

			AlarmPOJO alarm = alarmBO.getAlarm(alarmId);
			if (alarm == null) {
				throw new AlarmPersistenceException(AlarmPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_115, new Object[ ] { alarmId }));
			} else {
				alarm.setLock(isEnabled);
				alarm.setStandbytime(standbyTime);
				if (mails != null) {

					final List<ReceiversPOJO> recDb = alarmBO.getReceiversListEMail(mails);

					final LinkedHashMap<String, Long> recs = new LinkedHashMap<String, Long>();

					if (recDb != null && !recDb.isEmpty()) {
						for (int i = 0; i < recDb.size(); i++) {
							ReceiversPOJO r = recDb.get(i);
							recs.put(r.getMail(), r.getRecPk());
						}
					}
					final HashSet<ReceiversPOJO> recsToSave = new HashSet<ReceiversPOJO>();
					for (int i = 0; i < mails.length; i++) {
						ReceiversPOJO receiver = new ReceiversPOJO();
						if (recs.containsKey(mails[i])) {
							receiver.setRecPk(recs.get(mails[i]));
						} else {
							receiver.setMail(mails[i]);
							alarmBO.insertReceiver(receiver);
						}

						recsToSave.add(receiver);
					}
					alarm.setReceiverses(recsToSave);
				} else {
					alarm.getReceiverses().clear();
				}
				alarmBO.updateAlarm(alarm);

			}
		} catch (DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.DB_116, new Object[ ] { "" + isEnabled, "" + standbyTime, alarmId });
			LOGGER.error(msg, e);
			throw new AlarmPersistenceException(AlarmPersistenceException.UNKNOWN_ERROR, msg,e);
		} finally {
			// actualizamos la cache
			AlarmData ad = alarmCache.get(alarmId);
			ad.setLock(isEnabled);
			ad.setReceivers(mails);
			ad.setStandbyTime(standbyTime);
			alarmCache.put(alarmId, ad);
		}
	}

	/**
	 * Adds the alarm list to log and change status to reported.
	 * @param alarms	List of alarms to include the log.
	 * @param reportedAlarm	List of alarms which status will be change to reported.
	 */
	public void addAlarmsToLogAndReported(ArrayList<LogAlarmPOJO> alarms, ArrayList<ReportedAlarm> reportedAlarm) {
		boolean ok = true;
		if (alarms != null && !alarms.isEmpty()) {

			try {

				for (int i = 0; i < alarms.size(); i++) {

					alarmBO.insertLogAlarm(alarms.get(i));
				}

			} catch (DatabaseException e) {
				ok = false;
				for (int i = 0; i < alarms.size(); i++) {
					LogAlarmPOJO logAlarm = alarms.get(i);
					BackupAlarm.getInstance().addAlarmToLog(logAlarm.getLogPk(), logAlarm.getAlarm().getAlarmId(), logAlarm.getIslock(), logAlarm.getReported(), logAlarm.getCreationtime(),logAlarm.getMoreInfo());
				}
				LOGGER.error(Language.getMessage(LanguageKeys.DB_126), e);

			}
		}

		if (reportedAlarm != null && !reportedAlarm.isEmpty()) {
			if (ok) {
				
				final LinkedHashMap<String, Date> commTime = new LinkedHashMap<String, Date>();
				try {
					
					for (int i = 0; i < reportedAlarm.size(); i++) {
						ReportedAlarm repAlarm = reportedAlarm.get(i);
						Date lastComm = commTime.get(repAlarm.getAlarmId());
						if (lastComm == null || (lastComm.before(repAlarm.getCommunicationTime()))) {
							lastComm = repAlarm.getCommunicationTime();
						}
						commTime.put(repAlarm.getAlarmId(), lastComm);

						alarmBO.setCommunicationTime(repAlarm.getCommunicationTime(), repAlarm.getLogIdentifiers());
					}
					final Iterator<String> alarmIds = commTime.keySet().iterator();
					while (alarmIds.hasNext()) {
						String alarmId = alarmIds.next();
						Date lastComm = commTime.get(alarmId);

						alarmBO.setLastCommunication(alarmId, lastComm);
					}
					
				} catch (DatabaseException e) {
					for (int i = 0; i < reportedAlarm.size(); i++) {
						ReportedAlarm repAlarm = reportedAlarm.get(i);
						BackupAlarm.getInstance().setAlarmsAsReported(repAlarm.getAlarmId(), repAlarm.getLogIdentifiers(), repAlarm.getCommunicationTime());
					}
					LOGGER.error(Language.getMessage(LanguageKeys.DB_127), e);
					
				} finally {
					// actualizamos la cache
					final Iterator<String> alarmIds = commTime.keySet().iterator();
					while (alarmIds.hasNext()) {
						final String alarmId = alarmIds.next();
						final Date lastComm = commTime.get(alarmId);
						final AlarmData alarmData = alarmCache.get(alarmId);
						alarmData.setLastCommunication(lastComm);
						alarmCache.put(alarmId, alarmData);
					}
				}
			} else {
				for (int i = 0; i < reportedAlarm.size(); i++) {
					final ReportedAlarm repAlarm = reportedAlarm.get(i);
					BackupAlarm.getInstance().setAlarmsAsReported(repAlarm.getAlarmId(), repAlarm.getLogIdentifiers(), repAlarm.getCommunicationTime());
				}
			}
		}
	}


	/**
	 * Initialize the alarm cache.
	 */
	private void initializeCache() {
		if (alarmCache == null) {
			alarmCache = new LinkedHashMap<String, AlarmData>();
		}

		try {
			Set<ReceiversPOJO> recSet = null;
			String[ ] receivers = null;
			List<AlarmPOJO> alarms = alarmBO.findAllAlarms();
			for (int i = 0; i < alarms.size(); i++) {
				AlarmData ad = new AlarmData();
				AlarmPOJO alarm = (AlarmPOJO) alarms.get(i);
				ad.setAlarmId(alarm.getAlarmId());
				ad.setSubject(alarm.getSubject());
				ad.setMessage(alarm.getMessage());
				ad.setDescription(alarm.getDescription());
				ad.setLock(alarm.isLock());
				ad.setStandbyTime(alarm.getStandbytime());
				ad.setLastCommunication(alarm.getLastcom());
				recSet = alarm.getReceiverses();
				int numRec = recSet.size();
				if (numRec > 0) {
					receivers = new String[numRec];
					final Iterator<ReceiversPOJO> recIt = recSet.iterator();
					int j = 0;
					while (recIt.hasNext() && j < numRec) {
						final ReceiversPOJO rec = recIt.next();
						receivers[j] = rec.getMail();
						j++;
					}
					ad.setReceivers(receivers);
				}
				alarmCache.put(ad.getAlarmId(), ad);
			}

		} catch (DatabaseException e) {
			LOGGER.fatal(Language.getMessage(LanguageKeys.DB_125), e);
		}
	}

}
