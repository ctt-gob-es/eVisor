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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.file.AlarmCollectorThread.java.</p>
 * <b>Description:</b><p>Thread that collects the alarms that have not been recorded in database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>05/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 05/07/2011.
 */
package es.gob.signaturereport.malarm.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.tools.UtilsTime;


/** 
 * <p>Thread that collects the alarms that have not been recorded in database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 05/07/2011.
 */
public class AlarmCollectorThread extends Thread {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AlarmCollectorThread.class);
	
	/**
	 * Attribute that represents a flag that indicates if must continue the execution. 
	 */
	private boolean continueExecution = true;
	
	/**
	 * Attribute that represents the default period of this task. 
	 */
	private static final long DEFAULT_PERIOD = 30000;
	/**
	 * Attribute that represents the period of execution. 
	 */
	private long period = DEFAULT_PERIOD;

	
	/**
	 * Gets the value of the flag to indicate if the execution must continue the execution.
	 * @return the value of the flag to indicate if the execution must continue the execution.
	 */
	public boolean isContinueExecution() {
		return continueExecution;
	}

	
	/**
	 * Sets the value of the flag to indicate if the execution must continue the execution.
	 * @param isContinued The value for the flag to indicate if the execution must continue the execution.
	 */
	public void setContinueExecution(boolean isContinued) {
		this.continueExecution = isContinued;
	}

	
	/**
	 * Gets the value of the task period.
	 * @return the value of the task period.
	 */
	public long getPeriod() {
		return period;
	}

	
	/**
	 * Sets the value of the task period.
	 * @param executionPeriod The value for the task period.
	 */
	public void setPeriod(long executionPeriod) {
		this.period = executionPeriod;
	}


	/**
	 * Constructor method for the class AlarmCollectorThread.java.
	 * @param executionPeriod Execution period.
	 */
	public AlarmCollectorThread(long executionPeriod) {
		super();
		this.period = executionPeriod;
	}
	
	/**
	 * Starts the threads.
	 */
	public void startThread(){
		this.start();
	}


	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(continueExecution){
			try {
				processAlarmLog();
				sleep(period);
			} catch (InterruptedException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.AL_018),e);
			}
		}
	}
	
	/**
	 * Saves a copy of the "backupAlarm.log" file and requests the processing of this.
	 */
	private void processAlarmLog(){
		Logger logger = Logger.getLogger(AlarmAppender.LOGGER_NAME);
		AlarmAppender appender = (AlarmAppender) logger.getAppender(AlarmAppender.APPENDER_NAME);
		if(appender!=null){
			String logPath = appender.getFile();
			File logFile = new File(logPath);
			if(logFile.exists()&&logFile.length()>0 && ((System.currentTimeMillis() - logFile.lastModified())>period)){
				UtilsTime utilsTime = new UtilsTime();
				String backupLogPath = logPath+"."+utilsTime.toString(UtilsTime.FORMATO_FECHA_JUNTA);
				File backupLog = new File(backupLogPath);
				appender.closeAlarmFile();
				logFile.renameTo(backupLog);
				try {
					appender.openAlarmFile();
					BackupAlarm.getInstance().processLogFile(backupLogPath);
				} catch (IOException e) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AL_013, new Object[]{logPath}),e);
				}
				
			}
		}
	}
	
}
