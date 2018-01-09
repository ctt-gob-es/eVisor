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
 * <b>File:</b><p>es.gob.signaturereport.malarm.AlarmThread.java.</p>
 * <b>Description:</b><p>A thread that collects all the occurrence of an alarm and communicates it to its addressees.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>06/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 06/06/2011.
 */
package es.gob.signaturereport.malarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.access.AlarmPersistenceFacade;
import es.gob.signaturereport.malarm.item.AlarmData;
import es.gob.signaturereport.malarm.item.PendingAlarm;
import es.gob.signaturereport.malarm.mail.Mail;
import es.gob.signaturereport.malarm.mail.MailSender;
import es.gob.signaturereport.malarm.mail.SMTPMailServer;
import es.gob.signaturereport.persistence.exception.AlarmPersistenceException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO;
import es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO;
import es.gob.signaturereport.tools.UtilsTime;


/** 
 * <p>A thread that collects all the occurrence of an alarm and communicates it to its addressees.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 06/06/2011.
 */
public class AlarmThread extends Thread {
	
	/**
	 * Attribute that represents the message 'AL_MSG_001'. 
	 */
	private static final String AL_MSG_001 = "AL_MSG_001";
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AlarmThread.class);
	
	
	/**
	 * Attribute that represents the mail sender. 
	 */
	private static final String SENDER = AlarmManager.getInstance().getMailSender();
	
	/**
	 * Attribute that represents the mail server configuration. 
	 */
	private static final SMTPMailServer SERVER_CONF = AlarmManager.getInstance().getMailServerConfiguration();
	/**
	 * Attribute that represents the alarm information. 
	 */
	private AlarmData alarm = null;
	
	/**
	 * Attribute that represents time to sleep. 
	 */
	private long timeToSleep = 0;

	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			sleep(timeToSleep);
			communicateAlarm();
		} catch (InterruptedException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_006, new Object[]{this.alarm.getAlarmId()}),e);
		}
	}
	/**
	 * Constructor method for the class AlarmThread.java.
	 */
	public AlarmThread() {
		super();
	} 

	/**
	 * Start the process to communicate an alarm.
	 * @param timeToStart Time to start.
	 * @param alarmData Alarm information.
	 */
	public void sendAlarm(long timeToStart,AlarmData alarmData){
		this.timeToSleep = timeToStart;
		this.alarm = alarmData;
		this.start();
	}
	
	/**
	 * Communicate the alarm.
	 */
	private void communicateAlarm(){
		try {

			ArrayList <PendingAlarm> pending = AlarmPersistenceFacade.getInstance().getPendingAlarms(alarm.getAlarmId());
			if(pending!=null && !pending.isEmpty()){
				LOGGER.info(Language.getFormatMessage(LanguageKeys.AL_007, new Object[]{alarm.getAlarmId()}));
				String message = alarm.getMessage()+"\n";
				UtilsTime utilTime = new UtilsTime();
				Long[ ] ids =new Long[pending.size()]; 
				for(int i=0;i<pending.size();i++){
					
					PendingAlarm pendingAlarm = pending.get(i);
					ids[i] = new Long(pendingAlarm.getLogId());
					utilTime.setFecha(pendingAlarm.getCreationTime());
					String addInfo = "";
					if(pendingAlarm.getMoreInformation()!=null){
						addInfo = pendingAlarm.getMoreInformation();
					}
					message = message + Language.getFormatMessage(AL_MSG_001, new Object[]{alarm.getAlarmId(),""+pendingAlarm.getLogId(),utilTime.toString(UtilsTime.FORMATO_FECHA_ESTANDAR),addInfo})+"\n";
				}
				Date commDate = new Date();
				
				Mail mail = new Mail(SENDER, alarm.getReceivers(), alarm.getSubject(), message);
				MailSender mailSender = new MailSender(mail, SERVER_CONF);
				mailSender.start();
				AlarmPersistenceFacade.getInstance().setAlarmsAsReported(alarm.getAlarmId(), ids, commDate);
				LOGGER.info(Language.getFormatMessage(LanguageKeys.AL_011, new Object[]{alarm.getAlarmId()}));
			}		
		} catch (AlarmPersistenceException e) {
			LOGGER.info(Language.getFormatMessage(LanguageKeys.AL_008, new Object[]{alarm.getAlarmId()}),e);
		}
		
	}
}
