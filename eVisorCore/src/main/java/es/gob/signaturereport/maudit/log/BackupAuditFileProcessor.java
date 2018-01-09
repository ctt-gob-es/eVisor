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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.BackupAuditFileProcessor.java.</p>
 * <b>Description:</b><p>Class that represents a thread for reading the backup audit event file.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.maudit.log;

import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.EventCollector;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.tools.UtilsTime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/** 
 * <p>Class that represents a thread for reading the backup audit event file.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
public class BackupAuditFileProcessor extends Thread {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(BackupAuditFileProcessor.class);

	/**
	 * Attribute that represents the execution period. 
	 */
	private long period = 0;

	/**
	 * Attribute that represents a flag that indicates if the system continues with the execution. 
	 */
	private boolean continueExecution = true;

	/**
	 * Constructor method for the class BackupAuditFileProcessor.java.
	 * @param executionPeriod Execution period.
	 */
	public BackupAuditFileProcessor(long executionPeriod) {
		this.period = executionPeriod;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			if (this.continueExecution) {
				processEventFile();
				sleep(this.period);
			}
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Read the backup event file.
	 */
	private void processEventFile() {
		Logger logger = Logger.getLogger(BackupAuditTracesAppender.LOGGER_NAME);
		BackupAuditTracesAppender appender = (BackupAuditTracesAppender) logger.getAppender(BackupAuditTracesAppender.APPENDER_NAME);
		if (appender != null) {
			String logPath = appender.getFile();
			File logFile = new File(logPath);
			if ((logFile.exists()) && (logFile.length() > 0) && (System.currentTimeMillis() - logFile.lastModified() > this.period)) {
				UtilsTime utilsTime = new UtilsTime();
				String backupLogPath = logPath + "." + utilsTime.toString(UtilsTime.FORMATO_FECHA_JUNTA);
				File backupLog = new File(backupLogPath);
				appender.closeBackupAuditFile();
				logFile.renameTo(backupLog);
				try {
					appender.openBackupAuditFile();
					FileReader fileReader = new FileReader(backupLogPath);
					BufferedReader bf = new BufferedReader(fileReader);
					String line = null;
					while ((line = bf.readLine()) != null) {
						TraceI trace = TraceFactory.getTraceElement(line);
						if (trace != null){
							switch (trace.getActionId()) {
								case AuditManagerI.OPEN_TRANSACTION_ACT:
									EventCollector.getInstance().openTransaction(trace);
									break;
								case AuditManagerI.CLOSE_TRANSACTION_ACT:
									EventCollector.getInstance().closeTransaction(trace);
									break;
								default:
									EventCollector.getInstance().addTrace(trace);
							}
						}
					}
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
	}

	/**
	 * Gets if the system continues with the execution.
	 * @return True if it continues, otherwise false.
	 */
	public boolean isContinueExecution() {
		return this.continueExecution;
	}

	/**
	 * Sets if the system continues with the execution.
	 * @param contExecution	True if it continues, otherwise false.
	 */
	public void setContinueExecution(boolean contExecution) {
		this.continueExecution = contExecution;
	}
}
