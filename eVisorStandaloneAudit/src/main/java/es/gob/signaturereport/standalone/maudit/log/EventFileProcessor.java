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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.EventFileProcessor.java.</p>
 * <b>Description:</b><p>Class for processing the event file in uncoupled mode.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>02/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 02/08/2011.
 */
package es.gob.signaturereport.standalone.maudit.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.mail.Mail;
import es.gob.signaturereport.malarm.mail.MailSender;
import es.gob.signaturereport.malarm.mail.SMTPMailServer;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.log.BackupAuditTracesAppender;
import es.gob.signaturereport.maudit.log.EventFilenameFilter;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.standalone.maudit.access.StandaloneAuditPersistenceException;
import es.gob.signaturereport.standalone.maudit.access.StandaloneAuditPersistenceFacade;
import es.gob.signaturereport.standalone.maudit.i18n.LanguageStandalone;
import es.gob.signaturereport.tools.FileUtils;
import es.gob.signaturereport.tools.ToolsException;

/** 
 * <p>Class for processing the event file in uncoupled mode.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 02/08/2011.
 */
public class EventFileProcessor {

	/**
	 * Attribute that represents the path to file properties. 
	 */
	private static final String FILE_PROPERTIES = "/auditprocessor.properties";
	/**
	 * Attribute that represents the key used to indicate the folder that included the event file. 
	 */
	private static final String FILE_EVENT_DIR = "signaturereport.audit.eventFile.dir";
	/**
	 * Attribute that represents the key used to indicate the date pattern. 
	 */
	private static final String DATEPATTERN = "signaturereport.audit.eventFile.datepattern";
	/**
	 * Attribute that represents the key used to indicate the custody mode. 
	 */
	private static final String CUSTODY_MODE = "signaturereport.audit.eventFile.custody.type";
	/**
	 * Attribute that represents the key used to indicate the event file is stored. 
	 */
	private static final String IS_CUSTODY = "signaturereport.audit.eventFile.custody";
	
	/**
	 * Attribute that represents the default batch size. 
	 */
	private static final int NUM_TRANSACTIONS = 100;
	
	/**
	 * Attribute that represents the event file path. 
	 */
	private String eventFilePath = null;

	/**
	 * Attribute that represents the event file identifier. 
	 */
	private Long eventFileId = null;

	/**
	 * Attribute that represents the creation time. 
	 */
	private Date creationTime = null;

	/**
	 * Attribute that represents the custody mode. 
	 */
	private String custodyMode = null;

	/**
	 * Attribute that represents a flag that indicates if the event file is stored. 
	 */
	private boolean isCustody = false;

	/**
	 * Attribute that represents the receivers of the alarm. 
	 */
	private String[ ] receivers = null;

	/**
	 * Attribute that represents the alarm sender. 
	 */
	private String sender = null;

	/**
	 * Attribute that represents a flag that indicates if the alarm is sent. 
	 */
	private boolean sendAlarm = false;
	/**
	 * Attribute that represents the object that manages the backup log of the class.
	 */
	private static final Logger AUDIT_BACKUP_LOGGER = Logger.getLogger(BackupAuditTracesAppender.LOGGER_NAME);
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(EventFileProcessor.class);
	/**
	 * Attribute that represents the key used to indicate the alarm is sent. 
	 */
	private static final String SEND_ALARM = "signaturereport.alarm.activate";
	/**
	 * Attribute that represents the key used to indicate the alarm sender. 
	 */
	private static final String SENDER = "signaturereport.alarm.sender";
	/**
	 * Attribute that represents the key used to indicate the alarm receivers. 
	 */
	private static final String RECEIVERS = "signaturereport.alarm.receivers";
	/**
	 * Attribute that represents the key used to indicate the SMTP host. 
	 */
	private static final String SMTP_HOST = "signaturereport.alarm.smtp.host";
	/**
	 * Attribute that represents the key used to indicate the SMTP port. 
	 */
	private static final String SMTP_PORT = "signaturereport.alarm.smtp.port";
	/**
	 * Attribute that represents the key used to indicate the SMTP user. 
	 */
	private static final String SMTP_USER = "signaturereport.alarm.smtp.user";
	/**
	 * Attribute that represents the key used to indicate the SMTP password. 
	 */
	private static final String SMTP_PASS = "signaturereport.alarm.smtp.password";
	
	/**
	 * Attribute that represents the number of milliseconds that represents a day. 
	 */
	private static final int DAY_IN_MILLIS = 86400000;
	
	/**
	 * Attribute that represents the number of milliseconds that represents a second. 
	 */
	private static final int SECOND_IN_MILLIS = 1000;
	
	/**
	 * Attribute that represents the SMTP server configuration. 
	 */
	private SMTPMailServer mailServerConf = null;

	/**
	 * Start the execution.
	 * @param args	Input parameters, one only is defined, the path to the event file.
	 */
	public static void main(String[ ] args) {
		String filePath = null;
		if ((args != null) && (args.length > 0)) {
			filePath = args[0];
		}
		EventFileProcessor processor = new EventFileProcessor(filePath);
		processor.readAndSave();
	}

	/**
	 * Constructor method for the class EventFileProcessor.java.
	 * @param filePath Path to event file.
	 */
	public EventFileProcessor(String filePath) {
		InputStream in = EventFileProcessor.class.getResourceAsStream(FILE_PROPERTIES);
		Properties properties = new Properties();
		try {
			properties.load(in);
			String datepattern = properties.getProperty(DATEPATTERN).trim();
			if (filePath != null) {
				this.eventFilePath = filePath;
			} else {
				String dir = properties.getProperty(FILE_EVENT_DIR);
				long yesterdayMillis = System.currentTimeMillis() - DAY_IN_MILLIS;
				Date yesterday = new Date(yesterdayMillis);
				SimpleDateFormat sdf = new SimpleDateFormat(datepattern);
				String endFile = sdf.format(yesterday);
				EventFilenameFilter filter = new EventFilenameFilter(endFile);
				File auditFolder = new File(dir);
				String[ ] auditFiles = auditFolder.list(filter);
				if ((auditFiles != null) && (auditFiles.length == 1)){
					this.eventFilePath = auditFolder.getAbsolutePath() + "/" + auditFiles[0];
				}else {
					LOGGER.error(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_032, new Object[ ] { filePath }));
				}
			}
			File eventFile = new File(this.eventFilePath);
			String id = eventFile.getName().substring(0, eventFile.getName().indexOf('.'));
			this.eventFileId = new Long(id);
			String creation = eventFile.getName().substring(eventFile.getName().lastIndexOf('.') + 1);
			SimpleDateFormat sdf = new SimpleDateFormat(datepattern);
			try {
				this.creationTime = sdf.parse(creation);
			} catch (ParseException e) {
				this.creationTime = new Date();
			}
			this.custodyMode = properties.getProperty(CUSTODY_MODE).trim();
			this.isCustody = Boolean.parseBoolean(properties.getProperty(IS_CUSTODY));
			this.sendAlarm = Boolean.parseBoolean(properties.getProperty(SEND_ALARM));
			if (this.sendAlarm) {
				this.sender = properties.getProperty(SENDER);
				this.receivers = properties.getProperty(RECEIVERS).split(";");
				String host = properties.getProperty(SMTP_HOST );
				String port = properties.getProperty(SMTP_PORT);
				this.mailServerConf = new SMTPMailServer(host, port);
				String user = properties.getProperty( SMTP_USER);
				if ((user != null) && (!(user.equals("")))) {
					this.mailServerConf.setUser(user);
				}
				String pass = properties.getProperty(SMTP_PASS);
				if ((pass != null) && (!(pass.equals("")))){
					this.mailServerConf.setPassword(pass);
				}
			}
		} catch (IOException e) {
			LOGGER.error(LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_033), e);
		} catch (NumberFormatException nfe) {
			LOGGER.error(LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_034), nfe);
		}
	}


	/**
	 * Reads the event file and store the included information.
	 */
	public void readAndSave() {
		LOGGER.info(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_031, new Object[ ] { this.eventFilePath }));
		long time = System.currentTimeMillis();
		boolean ok = true;
		if (this.isCustody) {
			ok = storeEventFile();
		}

		if (ok) {
			ok = saveAuditTraces();
		}

		if (ok) {
			storeContent();
		}
		time = System.currentTimeMillis() - time;
		time = time / SECOND_IN_MILLIS;
		LOGGER.info(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_035, new Object[ ] { Long.valueOf(time) }));
	}

	/**
	 * Saves the event file.
	 * @return	True if the operation finished successfully. Otherwise false.
	 */
	private boolean storeEventFile() {
		try {
			boolean stored = StandaloneAuditPersistenceFacade.getInstance().existEventFile(this.eventFileId, false);
			if (!(stored)){
				StandaloneAuditPersistenceFacade.getInstance().storeEventFile(this.eventFileId, this.custodyMode, this.creationTime, null, false);
			}
		} catch (StandaloneAuditPersistenceException e) {
			String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_036);
			LOGGER.error(message, e);
			if (this.sendAlarm) {
				communicateAlarm(message);
			}
			return false;
		}
		return true;
	}

	/**
	 * Saves the audit transactions.
	 * @return	True if the operation finished successfully. Otherwise false. 
	 */
	private boolean saveAuditTraces() {
		boolean ok = false;
		File eventFile = new File(this.eventFilePath);
		try {
			FileReader fileReader = new FileReader(eventFile);
			BufferedReader bf = new BufferedReader(fileReader);
			String line = null;
			LinkedHashMap<String,List<TraceI>> closedTransactions = new LinkedHashMap<String,List<TraceI>>();
			LinkedHashMap<String,List<TraceI>> transactions = new LinkedHashMap<String,List<TraceI>>();
			LinkedHashMap<String,List<TraceI>> openedTransactions = new LinkedHashMap<String,List<TraceI>>();
			int i = 0;
			while ((line = bf.readLine()) != null) {
				TraceI trace = TraceFactory.getTraceElement(line);
				if (trace != null) {
					String transactionId = String.valueOf(trace.getSecuenceId());
					if (trace.getActionId() == AuditManagerI.OPEN_TRANSACTION_ACT) {
						List<TraceI> traces = transactions.get(transactionId);
						if (traces == null) {
							traces = new ArrayList<TraceI>();
							traces.add(trace);
							transactions.put(transactionId, traces);
						}
					} else if (trace.getActionId() == AuditManagerI.CLOSE_TRANSACTION_ACT) {
						List<TraceI> traces =  transactions.remove(transactionId);
						if (traces != null) {
							++i;
							traces.add(trace);
							closedTransactions.put(transactionId, traces);
						} else {
							traces = openedTransactions.get(transactionId);
							if (traces == null) {
								traces = new ArrayList<TraceI>();
							}
							traces.add(trace);
							openedTransactions.put(transactionId, traces);
						}
						if (i >= NUM_TRANSACTIONS) {
							try {
								StandaloneAuditPersistenceFacade.getInstance().saveCompletedTraces(this.eventFileId, closedTransactions, false);
							} catch (StandaloneAuditPersistenceException e) {
								String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_038);
								LOGGER.error(message, e);
								if (this.sendAlarm) {
									communicateAlarm(message);
								}
								if (!(closedTransactions.isEmpty())) {
									Iterator<String> it = closedTransactions.keySet().iterator();
									while (it.hasNext()) {
										List<TraceI> errorTraces = closedTransactions.get(it.next());
										for (int j = 0; j < errorTraces.size(); j++) {
											AUDIT_BACKUP_LOGGER.info(((TraceI) errorTraces.get(j)).getMessage());
										}
									}
								}
							}
							i = 0;
							closedTransactions.clear();
						}
					} else {
						List<TraceI> traces = transactions.get(transactionId);
						if (traces != null) {
							traces.add(trace);
						} else {
							traces = openedTransactions.get(transactionId);
							if (traces == null) {
								traces = new ArrayList<TraceI>();
							}
							traces.add(trace);
							openedTransactions.put(transactionId, traces);
						}
					}
				}
			}
			if (!(closedTransactions.isEmpty())) {
				try {
					StandaloneAuditPersistenceFacade.getInstance().saveCompletedTraces(this.eventFileId, closedTransactions, false);
				} catch (StandaloneAuditPersistenceException e) {
					String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_038);
					LOGGER.error(message, e);
					if (this.sendAlarm) {
						communicateAlarm(message);
					}
					if (!(closedTransactions.isEmpty())) {
						Iterator<String> it = closedTransactions.keySet().iterator();
						while (it.hasNext()) {
							List<TraceI> errorTraces = closedTransactions.get(it.next());
							for (int j = 0; j < errorTraces.size(); j++) {
								AUDIT_BACKUP_LOGGER.info(((TraceI) errorTraces.get(j)).getMessage());
							}
						}
					}
				}
				closedTransactions.clear();
			}

			if (!(transactions.isEmpty())) {
				try {
					StandaloneAuditPersistenceFacade.getInstance().saveCompletedTraces(this.eventFileId, transactions, false);
				} catch (StandaloneAuditPersistenceException e) {
					String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_038);
					LOGGER.error(message, e);
					if (this.sendAlarm) {
						communicateAlarm(message);
					}
					if (!(transactions.isEmpty())) {
						Iterator<String> it = transactions.keySet().iterator();
						while (it.hasNext()) {
							List<TraceI> errorTraces = transactions.get(it.next());
							for (int j = 0; j < errorTraces.size(); j++) {
								AUDIT_BACKUP_LOGGER.info(((TraceI) errorTraces.get(j)).getMessage());
							}
						}
					}
				}
				transactions.clear();
			}

			if (!(openedTransactions.isEmpty())) {
				try {
					StandaloneAuditPersistenceFacade.getInstance().saveOpenedTraces(openedTransactions, false);
				} catch (StandaloneAuditPersistenceException e) {
					String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_038);
					LOGGER.error(message, e);
					if (this.sendAlarm) {
						communicateAlarm(message);
					}
					if (!(openedTransactions.isEmpty())) {
						Iterator<String> it = openedTransactions.keySet().iterator();
						while (it.hasNext()) {
							List<TraceI> errorTraces = openedTransactions.get(it.next());
							for (int j = 0; j < errorTraces.size(); j++) {
								AUDIT_BACKUP_LOGGER.info(((TraceI) errorTraces.get(j)).getMessage());
							}
						}
					}
				}
				openedTransactions.clear();
			}
			ok = true;
		} catch (FileNotFoundException e) {
			String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_039);
			LOGGER.error(message, e);
			if (this.sendAlarm){
				communicateAlarm(message);
			}
		} catch (IOException e) {
			String message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_039);
			LOGGER.error(message, e);
			if (this.sendAlarm) {
				communicateAlarm(message);
			}
		} 

		return ok;
	}

	/**
	 * Saves the event file content.
	 */
	private void storeContent() {
		String message;
		try {
			byte[ ] file = FileUtils.getFile(this.eventFilePath);
			byte[ ] hash = null;
			if (this.custodyMode.equals(AuditManagerI.HASH_CUSTODY_TYPE)) {
				MessageDigest md = MessageDigest.getInstance("SHA1");
				hash = md.digest(file);
			}
			if (hash != null){
				StandaloneAuditPersistenceFacade.getInstance().updateEventContent(this.eventFileId, hash, this.custodyMode);
			}else{
				StandaloneAuditPersistenceFacade.getInstance().updateEventContent(this.eventFileId, file, this.custodyMode);
			}
		} catch (ToolsException e) {
			message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_039);
			LOGGER.error(message, e);
			if (this.sendAlarm){
				communicateAlarm(message);
			}
		} catch (NoSuchAlgorithmException e) {
			message = LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_027, new Object[ ] { this.eventFilePath });
			LOGGER.error(message, e);
			if (this.sendAlarm){
				communicateAlarm(message);
			}
		} catch (StandaloneAuditPersistenceException e) {
			message = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_040);
			LOGGER.error(message, e);
			if (this.sendAlarm){
				communicateAlarm(message);
			}
		}
	}

	/**
	 * Sends a alarm about an execution error.
	 * @param msg	Error description.
	 */
	private void communicateAlarm(String msg) {
		Mail mail = new Mail();
		mail.setMessage(msg);
		mail.setReceivers(this.receivers);
		mail.setSender(this.sender);
		mail.setSubject(LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_037));
		MailSender mailSender = new MailSender(mail, this.mailServerConf);
		mailSender.start();
	}
}
