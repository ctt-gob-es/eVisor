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
 * <b>File:</b><p>es.gob.signaturereport.maudit.EventCollectorThread.java.</p>
 * <b>Description:</b><p> Class that represents a thread for storing the audit transactions.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>29/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 29/07/2011.
 */
package es.gob.signaturereport.maudit;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.gob.signaturereport.persistence.exception.AuditPersistenceException;
import es.gob.signaturereport.maudit.access.AuditPersistenceFacade;
import es.gob.signaturereport.maudit.log.BackupAuditTracesAppender;
import es.gob.signaturereport.maudit.log.traces.TraceI;

/** 
 * <p>Class that represents a thread for storing the audit transactions.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 29/07/2011.
 */
public class EventCollectorThread extends Thread {

	/**
	 * Attribute that represents the class for that manages of the backup audit log traces. 
	 */
	private static final Logger AUDIT_BACKUP_LOGGER = Logger.getLogger(BackupAuditTracesAppender.LOGGER_NAME);
	/**
	 * Attribute that represents the class for that manages of the backup audit log traces. 
	 */
	private static final Logger LOGGER = Logger.getLogger(EventCollectorThread.class);

	/**
	 * Attribute that represents the event file identifier. 
	 */
	private Long eventFileId = null;

	/**
	 * Attribute that represents a list of completed transactions. 
	 */
	private Map<String, List<TraceI>> transactions = null;
	/**
	 * Attribute that represents a list of uncompleted transactions. 
	 */
	private Map<String, List<TraceI>> uncompletedTransactions = null;

	/**
	 * Constructor method for the class EventCollectorThread.java.
	 * @param eventId	Event file identifier.
	 * @param completedTrans	Completed transactions list. 
	 * @param uncompletedTrans 	Uncompleted transactions list.
	 */
	public EventCollectorThread(Long eventId, Map<String, List<TraceI>> completedTrans, Map<String, List<TraceI>> uncompletedTrans) {
		this.eventFileId = eventId;
		this.transactions = completedTrans;
		this.uncompletedTransactions = uncompletedTrans;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			if ((this.transactions != null) && (!(this.transactions.isEmpty()))) {
				AuditPersistenceFacade.getInstance().saveCompletedTraces(this.eventFileId, this.transactions, true);
			}
			if ((this.uncompletedTransactions != null) && (!(this.uncompletedTransactions.isEmpty()))){
				AuditPersistenceFacade.getInstance().saveOpenedTraces(this.uncompletedTransactions,true);
			}
		} catch (AuditPersistenceException e) {
			LOGGER.error(e);
			if ((this.transactions != null) && (!(this.transactions.isEmpty()))) {
				String[] ids = new String[this.transactions.size()];
				ids = (String[ ]) this.transactions.keySet().toArray(ids);
				for (int i = 0; i < ids.length; i++) {
					List<TraceI> traces = this.transactions.remove(ids[i]);
					while (!(traces.isEmpty())) {
						AUDIT_BACKUP_LOGGER.info(((TraceI) traces.remove(0)).getMessage());
					}
				}
			}

			if ((this.uncompletedTransactions != null) && (!(this.uncompletedTransactions.isEmpty()))) {
				String[] ids = new String[this.uncompletedTransactions.size()];
				ids = (String[ ]) this.uncompletedTransactions.keySet().toArray(ids);
				for (int i = 0; i < ids.length; i++) {
					List<TraceI> traces = this.uncompletedTransactions.remove(ids[i]);
					while (!(traces.isEmpty())){
						AUDIT_BACKUP_LOGGER.info(((TraceI) traces.remove(0)).getMessage());
					}
				}
			}
		}
	}

}
