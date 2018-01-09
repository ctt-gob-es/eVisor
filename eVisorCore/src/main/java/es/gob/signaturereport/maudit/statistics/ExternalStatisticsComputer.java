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
 * <b>File:</b><p>es.gob.signaturereport.maudit.ExternalStatisticsComputer.java.</p>
 * <b>Description:</b><p>Class that calculate the statistics of the service use.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.maudit.statistics;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.mail.Mail;
import es.gob.signaturereport.malarm.mail.MailSender;
import es.gob.signaturereport.malarm.mail.SMTPMailServer;
import es.gob.signaturereport.persistence.maudit.statistics.StatisticPersistenceException;
import es.gob.signaturereport.persistence.maudit.statistics.StatisticPersistenceFacade;
import es.gob.signaturereport.tools.UtilsResources;
import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Class that calculate the statistics of the service use.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
public class ExternalStatisticsComputer {

	/**
	 * Attribute that represents the default update value. 
	 */
	private static final boolean DEFAULT_UPDATE_VALUE = false;
	/**
	 * Attribute that represents the properties file name. 
	 */
	private static final String COMPUTER_PROPERTIES ="/statisticcomputer.properties";
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(ExternalStatisticsComputer.class);

	/**
	 * Attribute that represents if the current value will be updated. 
	 */
	private boolean updateValues = false;
	
	/**
	 * Attribute that represents the time used for calculating the statistics. 
	 */
	private Date calculationDate = null;
	
	/**
	 * Attribute that represents a flag that indicates if the class has been initialized. 
	 */
	private boolean initialized = false;
	
	/**
	 * Attribute that represents a flag that indicates if an error occurs this must be sent by e-mail. 
	 */
	private boolean sendAlarm = false;
	
	/**
	 * Attribute that represents the key used to indicate if the communication of alarm is active. 
	 */
	private static final String SEND_ALARM = "signaturereport.alarm.activate";
	
	/**
	 * Attribute that represents the key used to indicate the sender of the alarm. 
	 */
	private static final String SENDER = "signaturereport.alarm.sender";
	/**
	 * Attribute that represents the key used to indicate the receivers of the alarm. 
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
	 * Attribute that represents the number of milliseconds that contains a day. 
	 */
	private static final int DAY_IN_MILLIS = 86400000;
	/**
	 * Attribute that represents the SMTP configuration. 
	 */
	private SMTPMailServer mailServerConf = null;

	/**
	 * Attribute that represents e-mail sender. 
	 */
	private String sender = null;

	/**
	 * Attribute that represents e-mail receivers. 
	 */
	private String[ ] receivers = null;
	
	/**
	 * Method that starts the calculation of statistics.
	 * @param args Input parameters. The allowed values are [date (dd/MM/yyyy)] [update(true/false)]
	 */
	public static void main(String[ ] args) {
		Date stDate = null; 
		boolean update = DEFAULT_UPDATE_VALUE;
		if(args==null || args.length==0){
			//Si no se especifica fecha de calculo se realizar� sobre los datos del dña anterior al de ejecuci�n
			long time = System.currentTimeMillis()- DAY_IN_MILLIS;
			stDate = new Date(time);
		}else{
			String stDateStr = args[0];
			try {
				stDate = UtilsTime.convierteFecha(stDateStr, StatisticsManager.TIME_PATTERN);
				if(args.length>1){
					update = Boolean.parseBoolean(args[1]);
				}
				
			} catch (ParseException e) {
				LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_048, new Object[]{args[0]}));
			}
		}
		ExternalStatisticsComputer computer = new ExternalStatisticsComputer(update, stDate);
		computer.computeStatistics();
		
	}

	/**
	 * Constructor method for the class ExternalStatisticsComputer.java.
	 * @param update		If the current value will be updated.
	 * @param time 	Calculation date.
	 */
	public ExternalStatisticsComputer(boolean update, Date time) {
		super();
		this.updateValues = update;
		this.calculationDate = time;
		init();
	}
	
	/**
	 * Initialization.
	 */
	private void init(){
		InputStream  in = ExternalStatisticsComputer.class.getResourceAsStream(COMPUTER_PROPERTIES);
		Properties prop = new Properties();
		try {
			prop.load(in);
			
			this.sendAlarm = Boolean.parseBoolean(prop.getProperty(SEND_ALARM));
			if (this.sendAlarm) {
				this.sender = prop.getProperty(SENDER);
				this.receivers = prop.getProperty(RECEIVERS).split(";");
				String host = prop.getProperty(SMTP_HOST );
				String port = prop.getProperty(SMTP_PORT);
				this.mailServerConf = new SMTPMailServer(host, port);
				String user = prop.getProperty( SMTP_USER);
				if ((user != null) && (!(user.equals("")))) {
					this.mailServerConf.setUser(user);
				}
				String pass = prop.getProperty(SMTP_PASS);
				if ((pass != null) && (!(pass.equals("")))){
					this.mailServerConf.setPassword(pass);
				}
			}
			this.initialized = true;
		} catch (IOException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_049, new Object[]{COMPUTER_PROPERTIES}));
		}finally{
			UtilsResources.safeCloseInputStream(in);
		}
		
	}
	
	/**
	 * Computes statistics.
	 */
	public void computeStatistics(){
		if(this.initialized){
			try {
				StatisticPersistenceFacade.getInstance().computeStatistics(calculationDate, updateValues, false);
			} catch (StatisticPersistenceException e) {
				LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_051, new Object[]{calculationDate.toString()}));
				if(sendAlarm){
					communicateAlarm(e.getDescription());
				}
			}
		}else{
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_049, new Object[]{COMPUTER_PROPERTIES}));
		}
	}
	
	/**
	 * Sets a e-mail with a error description.
	 * @param msg	Error description.
	 */
	private void communicateAlarm(String msg) {
		Mail mail = new Mail();
		mail.setMessage(msg);
		mail.setReceivers(this.receivers);
		mail.setSender(this.sender);
		mail.setSubject(Language.getMessage(LanguageKeys.AUD_052));
		MailSender s = new MailSender(mail, this.mailServerConf);
		s.start();
	}

}
