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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.file.BackupAlarm.java.</p>
 * <b>Description:</b><p>Class that collects the alarms could not be recorded in database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>06/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 06/07/2011.
 */
package es.gob.signaturereport.malarm.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.access.AlarmPersistenceFacade;
import es.gob.signaturereport.malarm.item.AlarmData;
import es.gob.signaturereport.malarm.item.PendingAlarm;
import es.gob.signaturereport.malarm.item.ReportedAlarm;
import es.gob.signaturereport.persistence.malarm.model.pojo.AlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO;
import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Class that collects the alarms could not be recorded in database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 06/07/2011.
 */
public final class BackupAlarm {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(BackupAlarm.class);

	/**
	 * Attribute that represents logger of backup messages. 
	 */
	private static final Logger LOGGER_BACK = Logger.getLogger(AlarmAppender.LOGGER_NAME);

	/**
	 * Attribute that represents the key used to identify the message for adding alarm to log. 
	 */
	private static final String MSG_OP_ADD = "BACK_AL_001";

	/**
	 * Attribute that represents the key used to identify the message for changing the alarm status to reported. 
	 */
	private static final String MSG_OP_REP = "BACK_AL_002";

	/**
	 * Attribute that represents the identifier used to identify the operation for adding alarm to log. 
	 */
	private static final int ADD_OP = 0;

	/**
	 * Attribute that represents the identifier used to identify the operation for changing the alarm status to reported. 
	 */
	private static final int REPORTED_OP = 1;
	/**
	 * Attribute that represents the time format. 
	 */
	private static final String TIME_FORMAT = UtilsTime.FORMATO_FECHA_ESTANDAR_ADICIONAL;

	/**
	 * Attribute that represents an instance of the class. 
	 */
	private static BackupAlarm instance = null;

	/**
	 * Attribute that represents the key for identifier of log event. 
	 */
	private static final String ID_KEY = "ID";
	/**
	 * Attribute that represents the key for operation property. 
	 */
	private static final String OP_KEY = "OP";
	
	/**
	 * Attribute that represents the key for alarm identifier. 
	 */
	private static final String ALARM_KEY = "ALARM";
	
	/**
	 * Attribute that represents the key for creation time. 
	 */
	private static  final String CREATION_KEY = "CREATION";
	
	/**
	 * Attribute that represents the key for lock status property. 
	 */
	private static final String LOCK_KEY = "LOCK";
	
	/**
	 * Attribute that represents the key for the mails. 
	 */
	private static final String MAILS_KEY = "MAILS";

	/**
	 * Attribute that represents the key for communication key. 
	 */
	private static final String COMMUNICATION_KEY = "COMMUNICATION";
	
	/**
	 * Attribute that represents the key for more information key. 
	 */
	private static final String MOREINFO_KEY = "MOREINFO";
	
	/**
	 * Attribute that represents the keys for identifiers of reported alarms. 
	 */
	private static final String LOGIDS_KEY = "LOGIDS";
	/**
	 * Attribute that represents the pending alarms. 
	 */
	private LinkedHashMap<String, LinkedHashMap<Long, PendingAlarm>> alarmPending = new LinkedHashMap<String, LinkedHashMap<Long, PendingAlarm>>();
	
	/**
     * Attribute that represents the alarms and summaries events scheme manager BOs.
     */
    @Inject
    private AlarmPersistenceFacade alarmsPersistenceFacade;

	/**
	 * Constructor method for the class BackupAlarm.java. 
	 */
	private BackupAlarm() {
	}

	/**
	 * Gets an instance of the class.
	 * @return	A {@link BackupAlarm} object.
	 */
	public static BackupAlarm getInstance() {
		if (instance == null) {
			instance = new BackupAlarm();
		}
		return instance;
	}

	/**
	 * Adds the alarm information to log file.
	 * @param logId	Log event identifier.
	 * @param alarm	Alarm information.
	 * @param creation	Creation time of alarm.
	 * @param moreInfo  Additional information.
	 */
	public void addAlarmToLog(final long logId, final AlarmData alarm, final Date creation, final String moreInfo) {
		String mails = "";
		if (alarm.getReceivers() != null && alarm.getReceivers().length > 0) {
			mails = mails + alarm.getReceivers()[0];
			for (int i = 1; i < alarm.getReceivers().length; i++) {
				mails = mails + "," + alarm.getReceivers()[i];
			}
		}
		addAlarmToLog(logId, alarm.getAlarmId(), alarm.isLock(), mails, creation,moreInfo);
	}

	/**
	 * Adds the alarm information to log file.
	 * @param logId		Log event identifier.
	 * @param alarmId	Alarm identifier.
	 * @param lock		Lock status of alarm.
	 * @param reported	Mails to reported.
	 * @param creation	Creation time.
	 * @param moreInfo  Additional information.
	 */
	public void addAlarmToLog(final long logId, final String alarmId, final boolean lock, final String reported, final Date creation, final String moreInfo) {
		UtilsTime utils = new UtilsTime(creation);
		String additionalInfo = "";
		if (moreInfo != null) {
			additionalInfo = moreInfo;
		}
		LOGGER_BACK.info(Language.getFormatMessage(MSG_OP_ADD, new Object[ ] { String.valueOf(logId), alarmId, utils.toString(TIME_FORMAT), String.valueOf(lock), reported, additionalInfo,String.valueOf(ADD_OP) }));
		if (!alarmPending.containsKey(alarmId)) {
			alarmPending.put(alarmId, new LinkedHashMap<Long, PendingAlarm>());
		}
		PendingAlarm alarm = new PendingAlarm(alarmId, logId, creation, moreInfo);
		alarmPending.get(alarmId).put(new Long(logId), alarm);
	}

	/**
	 * Adds the reported information to log.
	 * @param alarmId	Alarm identifier
	 * @param logIds	Log identifiers
	 * @param communicationDate	Communication time.
	 */
	public void setAlarmsAsReported(final String alarmId, final Long[ ] logIds, final Date communicationDate) {
		UtilsTime utils = new UtilsTime(communicationDate);
		String ids = "";
		LinkedHashMap<Long, PendingAlarm> pendings = alarmPending.get(alarmId);
		if (pendings == null) {
			pendings = new LinkedHashMap<Long, PendingAlarm>();
		}
		if (logIds != null && logIds.length > 0) {
			ids = ids  + logIds[0].longValue();
			pendings.remove(logIds[0]);
			for (int i = 1; i < logIds.length; i++) {
				ids = ids + "," + logIds[i].longValue();
				pendings.remove(logIds[i]);
			}
		}
		alarmPending.put(alarmId, pendings);
		LOGGER_BACK.info(Language.getFormatMessage(MSG_OP_REP, new Object[ ] { alarmId, utils.toString(TIME_FORMAT), ids, String.valueOf(REPORTED_OP) }));
	}

	/**
	 * Gets a pending alarms.
	 * @param alarmId	Alarm identifier.
	 * @return	Pending alarms.
	 */
	public ArrayList<PendingAlarm> getPendingAlarms(final String alarmId) {
		ArrayList<PendingAlarm> alarms = new ArrayList<PendingAlarm>();
		LinkedHashMap<Long, PendingAlarm> alarmsMap = alarmPending.get(alarmId);
		if(alarmsMap!=null){
			Iterator<PendingAlarm> alarmsIt = alarmsMap.values().iterator();
			while(alarmsIt.hasNext()){
				PendingAlarm alarm = alarmsIt.next();
				alarms.add(alarm);
			}
		}
		return alarms;
	}

	/**
	 * Process the log file.
	 * @param fileLogPath	Path to file.
	 */
	public void processLogFile(final String fileLogPath) {
		alarmPending.clear();
		try {
			FileReader reader = new FileReader(fileLogPath);
			BufferedReader br = new BufferedReader(reader);
			String msg = null;
			ArrayList<LogAlarmPOJO> logAlarmList = new ArrayList<LogAlarmPOJO>();
			ArrayList <ReportedAlarm> alarmsAsReported = new ArrayList<ReportedAlarm>();
			while ((msg = br.readLine()) != null) {
				try {
					LinkedHashMap<String, String> params = readLine(msg);
					if (!params.isEmpty()) {
						String operation = params.get(OP_KEY);
						if (operation != null) {
							int op = Integer.parseInt(operation);
							switch (op) {
								case ADD_OP:
									LogAlarmPOJO log = new LogAlarmPOJO();
									log.setLogPk(Long.parseLong(params.get(ID_KEY)));
									log.setCreationtime(UtilsTime.convierteFecha(params.get(CREATION_KEY), TIME_FORMAT));
									log.setIslock(Boolean.parseBoolean(params.get(LOCK_KEY)));
									log.setReported(params.get(MAILS_KEY));
									log.setMoreInfo(params.get(MOREINFO_KEY));
									AlarmPOJO alarm = new AlarmPOJO();
									alarm.setAlarmId(params.get(ALARM_KEY));
									log.setAlarm(alarm);
									logAlarmList.add(log);
									break;
								case REPORTED_OP:
									Long[ ] logIdentifiers = null;
									String[] ids = params.get(LOGIDS_KEY).split(",");
									logIdentifiers = new Long[ids.length];
									for(int i=0;i<ids.length;i++){
										logIdentifiers[i] = new Long(ids[i].trim());
									}
									Date communicationTime = UtilsTime.convierteFecha(params.get(COMMUNICATION_KEY), TIME_FORMAT);;
									String alarmId = params.get(ALARM_KEY);
									ReportedAlarm repAlarm = new ReportedAlarm(alarmId, logIdentifiers, communicationTime);
									alarmsAsReported.add(repAlarm );
									break;
								default:
									LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_016, new Object[ ] { msg }));
									break;
							}
						}

					}
				} catch (Exception e) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_015, new Object[ ] { msg }), e);
				}
			}
			//Comunicamos las alarmas
			alarmsPersistenceFacade.addAlarmsToLogAndReported(logAlarmList, alarmsAsReported);
			
		} catch (Exception e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_014, new Object[ ] { fileLogPath }), e);
		}
	}

	/**
	 * Extracts the parameters and values include into a line.
	 * @param line	Line of log.
	 * @return	Map that contains the parameters.
	 */
	public LinkedHashMap<String, String> readLine(final String line) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		if (line != null) {
			char[ ] logLineChars = line.trim().toCharArray();
			for (int i = 0; i < logLineChars.length; i++) {
				String key = "";
				while (i < logLineChars.length && logLineChars[i] != '=') {
					key = key + logLineChars[i];
					i++;
				}

				i = i + 2;
				String value = "";
				while (i < logLineChars.length && logLineChars[i] != ')') {
					value = value + logLineChars[i];
					i++;
				}
				if (key.length() > 0 && value.length() > 0) {
					params.put(key, value);
				}
				i++;
			}
		}
		return params;
	}

}
