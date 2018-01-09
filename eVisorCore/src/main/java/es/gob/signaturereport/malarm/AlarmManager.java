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
 * <b>File:</b><p>es.gob.signaturereport.malarm.AlarmManager.java.</p>
 * <b>Description:</b><p>Class that manage the alarm of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.access.AlarmPersistenceFacade;
import es.gob.signaturereport.malarm.file.AlarmCollectorThread;
import es.gob.signaturereport.malarm.item.AlarmData;
import es.gob.signaturereport.malarm.mail.SMTPMailServer;
import es.gob.signaturereport.persistence.exception.AlarmPersistenceException;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/** 
 * <p>Class that manage the alarm of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
@Singleton
@ManagedBean
public class AlarmManager implements Serializable{

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -6790132625177950086L;

	// --- ALARM OF THE SYSTEM ---
	/**
	 * Attribute that represents the alarm identifier associated to database error. 
	 */
	public static final String ALARM_001 = "ALARM_001";
	
	/**
	 * Attribute that represents the alarm identifier associated to "@firma" connection error. 
	 */
	public static final String ALARM_002 = "ALARM_002";
	
	/**
	 * Attribute that represents the alarm identifier associated to Open Office connection error. 
	 */
	public static final String ALARM_003 = "ALARM_003";
	// ---

	/**
	 * Attribute that represents the max length associated to the additional information length. 
	 */
	private static final int MOREINFO_MAX_LENGTH = 4000;
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AlarmManager.class);

	/**
	 * Attribute that represents the key used to indicate the mail server host. 
	 */
	private static final String HOST = "signaturereport.alarm.smtp.host";

	/**
	 * Attribute that represents the key used to indicate the mail server port. 
	 */
	private static final String PORT = "signaturereport.alarm.smtp.port";

	/**
	 * Attribute that represents the key used to indicate the authentication user of mail server. 
	 */
	private static final String USER = "signaturereport.alarm.smtp.user";

	/**
	 * Attribute that represents the key used to indicate the authentication password of mail server. 
	 */
	private static final String PASSWORD = "signaturereport.alarm.smtp.password";

	/**
	 * Attribute that represents the key used to indicate the sender of mail.  
	 */
	private static final String SENDER = "signaturereport.alarm.sender";
	
	/**
	 * Attribute that indicates if the backup collector is active. 
	 */
	private static final String BACKUP_ACTIVE ="signaturereport.alarm.backupcollector.active";

	
	/**
	 * Attribute that represents the period execution of backup collector. 
	 */
	private static final String BACKUP_PERIOD = "signaturereport.alarm.backupcollector.period";
	/**
	 * Attribute that represents the mail sender. 
	 */
	private String mailSender = null;

	/**
	 * Attribute that represents the mail server configuration. 
	 */
	private SMTPMailServer mailServerConfiguration = null;

	/**
	 * Attribute that represents an instance of the class. 
	 */
	private static AlarmManager instance = null;

	/**
	 * Attribute that represents controller of occurrence of an alarm and communicates it to its addressees. 
	 */
	private AlarmController controller = new AlarmController();
	
	/**
     * Attribute that represents the alarms and summaries events scheme manager BOs.
     */
    @Inject
    private AlarmPersistenceFacade alarmsPersistenceFacade;
    
       
    /**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {
    	mailServerConfiguration = new SMTPMailServer();
		mailServerConfiguration.setHost(StaticSignatureReportProperties.getProperty(HOST));
		mailServerConfiguration.setPort(StaticSignatureReportProperties.getProperty(PORT));
		String user = StaticSignatureReportProperties.getProperty(USER);
		if (user != null && !user.trim().equals("")) {
			mailServerConfiguration.setUser(user);
		}
		String password = StaticSignatureReportProperties.getProperty(PASSWORD);
		if (password != null && !password.trim().equals("")) {
			mailServerConfiguration.setUser(password);
		}
		mailSender = StaticSignatureReportProperties.getProperty(SENDER);

				
		//Lanzamos la tarea de procesado del backup de alarmas.
		
		String backActive = StaticSignatureReportProperties.getProperty(BACKUP_ACTIVE);
		if(backActive!=null && Boolean.valueOf(backActive)){
			String period = StaticSignatureReportProperties.getProperty(BACKUP_PERIOD);
			try{
				AlarmCollectorThread thread = new AlarmCollectorThread(Long.parseLong(period));
				thread.startThread();
			}catch(NumberFormatException e){
				LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_017, new Object[]{period}),e);
			}
		}
		
		instance = this;
    }

    /**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	instance = null;
    }

	/**
	 * Constructor method for the class AlarmManager.java. 
	 */
//	private AlarmManager() {
//		// try {
//		mailServerConfiguration = new SMTPMailServer();
//		mailServerConfiguration.setHost(SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, HOST));
//		mailServerConfiguration.setPort(SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, PORT));
//		String user = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, USER);
//		if (user != null && !user.trim().equals("")) {
//			mailServerConfiguration.setUser(user);
//		}
//		String password = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, PASSWORD);
//		if (password != null && !password.trim().equals("")) {
//			mailServerConfiguration.setUser(password);
//		}
//		mailSender = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, SENDER);
//
//				
//		//Lanzamos la tarea de procesado del backup de alarmas.
//		
//		String backActive = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, BACKUP_ACTIVE);
//		if(backActive!=null && Boolean.valueOf(backActive)){
//			String period = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.ALARM_SECTION_ID, BACKUP_PERIOD);
//			try{
//				AlarmCollectorThread thread = new AlarmCollectorThread(Long.parseLong(period));
//				thread.startThread();
//			}catch(NumberFormatException e){
//				LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_017, new Object[]{period}),e);
//			}
//		}
//		
//	}

	/**
	 * Gets an instance of the class.
	 * @return	An instance of the class.
	 */
	public static AlarmManager getInstance() {
		if (instance == null) {
			instance = new AlarmManager();
		}
		return instance;
	}

	/**
	 * Gets the information associated to supplied alarm identifier.
	 * @param identifier	Alarm identifier.
	 * @return	An {@link AlarmData} object that contains the information.
	 * @throws AlarmException	If an error occurs. The values might be:<br/>
	 * 		{@link AlarmException#INVALID_INPUT_PARAMETERS} If the identifier is null, empty or invalid.<br/>
	 * 		{@link AlarmException#UNKNOWN_ERROR} Other error.
	 */
	public AlarmData getAlarm(String identifier) throws AlarmException {
		if (identifier == null || identifier.trim().length() == 0) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AL_001));
		}
		if (!isAlarmIdentifier(identifier)) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { identifier }));
		}
		try {
			return getAlarmInformation(identifier);
		} catch (AlarmPersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AL_002, new Object[ ] { identifier });
			LOGGER.error(msg, e);
			if (e.getCode() == AlarmPersistenceException.ITEM_NOT_FOUND) {
				throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { identifier }),e);
			} else {
				throw new AlarmException(AlarmException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * Sets the alarm receivers.
	 * @param alarmId	Alarm identifier.	
	 * @param mails		Mails direction. May be null.
	 * @throws AlarmException	If an error occurs.	The values might be:<br/>
	 * 		{@link AlarmException#INVALID_INPUT_PARAMETERS} If the identifier is null, empty or invalid.<br/>
	 * 		{@link AlarmException#UNKNOWN_ERROR} Other error.
	 */
	public void setAlarmReceivers(String alarmId, String[ ] mails) throws AlarmException {
		if (alarmId == null || alarmId.trim().length() == 0) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AL_001));
		}
		if (!isAlarmIdentifier(alarmId)) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { alarmId }));
		}

		try {
			alarmsPersistenceFacade.setAlarmReceivers(alarmId, mails);
		} catch (AlarmPersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AL_004, new Object[ ] { alarmId });
			LOGGER.error(msg, e);
			if (e.getCode() == AlarmPersistenceException.ITEM_NOT_FOUND) {
				throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { alarmId }),e);
			} else {
				throw new AlarmException(AlarmException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * Sets the alarm information.
	 * @param alarmId	Alarm identifier.
	 * @param isEnabled	True if the alarm is enabled. Otherwise, false.
	 * @param mails		Mails direction. May be null.
	 * @param standbytime Time which the alarm is standby.
	 * @throws AlarmException	If an error occurs. The values might be:<br/>
	 * 		{@link AlarmException#INVALID_INPUT_PARAMETERS} If the identifier is null, empty or invalid.<br/>
	 * 		{@link AlarmException#UNKNOWN_ERROR} Other error.
	 */
	public void setAlarmInformation(String alarmId, boolean isEnabled, long standbytime, String[ ] mails) throws AlarmException {
		if (alarmId == null || alarmId.trim().length() == 0) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AL_001));
		}
		if (!isAlarmIdentifier(alarmId)) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { alarmId }));
		}
		try {
			alarmsPersistenceFacade.setAlarmInformation(alarmId, isEnabled, standbytime, mails);
		} catch (AlarmPersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AL_005, new Object[ ] { alarmId });
			LOGGER.error(msg, e);
			if (e.getCode() == AlarmPersistenceException.ITEM_NOT_FOUND) {
				throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { alarmId }),e);
			} else {
				throw new AlarmException(AlarmException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * Method that allows to communicate an alarm.
	 * @param alarmId	Alarm identifier.
	 * @param moreInfo  Additional information.
	 * 
	 */
	public void communicateAlarm(String alarmId,String moreInfo) {
		if (alarmId == null || alarmId.trim().length() == 0) {
			LOGGER.error(Language.getMessage(LanguageKeys.AL_001));
		}
		if (!isAlarmIdentifier(alarmId)) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_003, new Object[ ] { alarmId }));
		}
		try {
			if(moreInfo != null && moreInfo.length()>MOREINFO_MAX_LENGTH){
				alarmsPersistenceFacade.addAlarmToLog(alarmId,new Date(),moreInfo.substring(0,(MOREINFO_MAX_LENGTH-1)));
			}else{
				alarmsPersistenceFacade.addAlarmToLog(alarmId,new Date(),moreInfo);
			}
			AlarmData alarm = getAlarmInformation(alarmId);
			controller.communicateAlarm(alarm);
		} catch (AlarmPersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AL_012, new Object[ ] { alarmId });
			LOGGER.error(msg, e);
		}
	}

	/**
	 * Gets the value of the mail server configuration.
	 * @return the value of the mail server configuration.
	 */
	public SMTPMailServer getMailServerConfiguration() {
		return mailServerConfiguration;
	}

	/**
	 * Gets the value of the mail sender.
	 * @return the value of the mail sender.
	 */
	public String getMailSender() {
		return mailSender;
	}
	
	/**
	 * Gets the alarm description.
	 * @param alarmId	Alarm identifier.
	 * @return	Alarm description if the alarm exists. Otherwise null.
	 * @throws AlarmException If an error occurs.
	 */
	public String getAlarmDescription(String alarmId) throws AlarmException{
		if (alarmId == null || alarmId.trim().length() == 0) {
			throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AL_001));
		}
		try {
			String  tokenDescription = alarmsPersistenceFacade.getAlarmDescription(alarmId);
			return Language.getMessage(tokenDescription);
		} catch (AlarmPersistenceException e) {
			if (e.getCode() == AlarmPersistenceException.INVALID_INPUT_PARAMETERS || e.getCode() == AlarmPersistenceException.ITEM_NOT_FOUND) {
				throw new AlarmException(AlarmException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else {
				throw new AlarmException(AlarmException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * Checks if the supplied identifier is valid.
	 * @param identifier	Alarm identifier.
	 * @return	True if the identifier is valid, otherwise returns false.
	 */
	private boolean isAlarmIdentifier(String identifier) {
		if (identifier == null || identifier.trim().length() == 0) {
			return false;
		} else {
			return identifier.equals(ALARM_001) || identifier.equals(ALARM_002)  || identifier.equals(ALARM_003);
		}
	}

	/**
	 * Gets the alarm configuration.
	 * @param alarmId	Alarm identifier.
	 * @return	An {@link AlarmData} object with the configuration information.
	 * @throws AlarmPersistenceException	If an error occurs.
	 */
	private AlarmData getAlarmInformation(String alarmId) throws AlarmPersistenceException {
		AlarmData alarm = alarmsPersistenceFacade.getAlarm(alarmId);
		AlarmData aux = alarm.newInstance();
		aux.setSubject(Language.getMessage(alarm.getSubject()));
		aux.setMessage(Language.getMessage(alarm.getMessage()));
		aux.setDescription(Language.getMessage(alarm.getDescription()));
		return aux;

	}

}
