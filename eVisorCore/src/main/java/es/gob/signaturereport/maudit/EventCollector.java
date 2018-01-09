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
 * <b>File:</b><p>es.gob.signaturereport.maudit.EventCollector.java.</p>
 * <b>Description:</b><p>Class that represents an audit event collector.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/07/2011.
 */
package es.gob.signaturereport.maudit;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.gob.signaturereport.maudit.access.AuditPersistenceFacade;
import es.gob.signaturereport.maudit.log.BackupAuditFileProcessor;
import es.gob.signaturereport.maudit.log.BackupAuditTracesAppender;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/** 
 * <p>Class that represents an audit event collector.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/07/2011.
 */
public final class EventCollector {

	/**
	 * Attribute that represents an instance class. 
	 */
	private static EventCollector instance = null;

	/**
	 * Attribute that represents event file identifier. 
	 */
	private String eventId = null;

	/**
	 * Attribute that represents a flag that indicates if the current event file is stored. 
	 */
	private boolean storedCurrentEventFile = false;

	/**
	 * Attribute that represents a flag that indicates if the event file is stored. 
	 */
	private boolean custodyEventFile = false;
	/**
	 * Constant that represents the property key used to information the event custody mode. 
	 */
	private static final String EVENT_CUSTODY_TYPE = "signaturereport.audit.eventFile.custody.type";
	
	/**
	 * Attribute that represents the event custody mode. 
	 */
	private String eventCustodyType = null;

	/**
	 * Attribute that represents the list of audit transactions. 
	 */
	private Map<String, List<TraceI>> transactions = new LinkedHashMap<String, List<TraceI>>();
	
	/**
	 * Attribute that represents the list of closed audit transactions. 
	 */
	private Map<String, List<TraceI>> closedTransactions = new LinkedHashMap<String, List<TraceI>>();
	
	/**
	 * Attribute that represents the list of uncompleted audit transactions. 
	 */
	private Map<String, List<TraceI>> uncompletedTransactions = new LinkedHashMap<String, List<TraceI>>();

	/**
	 * Attribute that represents the current transaction number. 
	 */
	private int transactionNumber = 0;
	
	/**
	 * Constant that represents the property key used to information the event collector mode. 
	 */
	private static final String COLLECTOR_MODE_PROP = "signaturereport.audit.eventcollector.mode";
	
	/**
	 * Constant that represents the batch collector mode. 
	 */
	public static final int BATCH_MODE = 0;
	
	/**
	 * Constant that represents the uncoupled collector mode. 
	 */
	public static final int UNCOUPLED_MODE = 1;
	/**
	 * Constant that represents the property key used to information the batch size. 
	 */
	private static final String BATCH_SIZE_PROP = "signaturereport.audit.eventcollector.batchsize";
	/**
	 * Constant that represents the property key used to information if the event file is stored. 
	 */
	private static final String CUSTODY_EVENT_FILE_PROP = "signaturereport.audit.eventFile.custody";
	
	/**
	 * Constant that represents an audit opening operation. 
	 */
	private static final int OPEN_OPERATION = 0;
	/**
	 * Constant that represents an audit adding operation. 
	 */
	private static final int ADD_OPERATION = 1;
	/**
	 * Constant that represents an audit closing operation. 
	 */
	private static final int CLOSE_OPERATION = 2;
	/**
	 * Constant that represents an audit flushing operation. 
	 */
	private static final int FLUSH_OPERATION = 3;
	
	/**
	 * Constant that represents the property key used to information if the backup collector is active.
	 */
	private static final String ACTIVE_BACKUP_PROP = "signaturereport.audit.backupcollector.active";
	
	/**
	 * Constant that represents the property key used to information the period of backup collector.
	 */
	private static final String BACKUP_PERIOD_PROP = "signaturereport.audit.backupcollector.period";
	
	/**
	 * Constant that represents the default period for the collector. 
	 */
	private static final int DEFAULT_PERIOD= 1800000;
	
	/**
	 * Attribute that represents the default batch size. 
	 */
	private static final int DEFAULT_BATCH_SIZE = 100;

	/**
	 * Attribute that represents collector mode. 
	 */
	private int mode = UNCOUPLED_MODE;

	/**
	 * Attribute that represents batch size. 
	 */
	private int batchSize = DEFAULT_BATCH_SIZE;
			
	
	/**
	 * Constructor method for the class EventCollector.java. 
	 */
	private EventCollector() {
		this.eventCustodyType = StaticSignatureReportProperties.getProperty(EVENT_CUSTODY_TYPE);
		String modeStr = StaticSignatureReportProperties.getProperty(COLLECTOR_MODE_PROP);
		try {
			this.mode = Integer.parseInt(modeStr.trim());
			String batchSizeStr = StaticSignatureReportProperties.getProperty(BATCH_SIZE_PROP);
			this.batchSize = Integer.parseInt(batchSizeStr.trim());
		} catch (NumberFormatException e) {
			this.mode = UNCOUPLED_MODE;
		}
		String custody = StaticSignatureReportProperties.getProperty(CUSTODY_EVENT_FILE_PROP);
		if (custody != null) {
			this.custodyEventFile = Boolean.parseBoolean(custody.trim());
		}
		if (this.mode == BATCH_MODE) {
			Runtime.getRuntime().addShutdownHook(new Thread() {

				public void run() {
					if (EventCollector.this.mode == BATCH_MODE) {
						Logger backUpLogger = Logger.getLogger(BackupAuditTracesAppender.LOGGER_NAME);
						if (!(EventCollector.this.transactions.isEmpty())) {
							String[ ] ids = new String[EventCollector.this.transactions.size()];
							ids = (String[ ]) EventCollector.this.transactions.keySet().toArray(ids);
							for (int i = 0; i < ids.length; i++) {
								List<TraceI> traces = EventCollector.this.transactions.remove(ids[i]);
								while (!(traces.isEmpty())) {
									backUpLogger.info(((TraceI) traces.remove(0)).getMessage());
								}
							}
						}

						if (!(EventCollector.this.closedTransactions.isEmpty())) {
							String[ ] ids = new String[EventCollector.this.closedTransactions.size()];
							ids = (String[ ]) EventCollector.this.closedTransactions.keySet().toArray(ids);
							for (int i = 0; i < ids.length; i++) {
								List<TraceI> traces = EventCollector.this.closedTransactions.remove(ids[i]);
								while (!(traces.isEmpty())) {
									backUpLogger.info(((TraceI) traces.remove(0)).getMessage());
								}
							}
						}

						if (!(EventCollector.this.uncompletedTransactions.isEmpty())) {
							String[ ] ids = new String[EventCollector.this.uncompletedTransactions.size()];
							ids = (String[ ]) EventCollector.this.uncompletedTransactions.keySet().toArray(ids);
							for (int i = 0; i < ids.length; i++) {
								List<TraceI> traces = EventCollector.this.uncompletedTransactions.remove(ids[i]);
								while (!(traces.isEmpty())) {
									backUpLogger.info(((TraceI) traces.remove(0)).getMessage());
								}
							}
						}
					}
				}

			});
			boolean activeBackup = Boolean.parseBoolean(StaticSignatureReportProperties.getProperty(ACTIVE_BACKUP_PROP));
			if (activeBackup) {
				long period = 0;
				try {
					period = Long.parseLong(StaticSignatureReportProperties.getProperty(BACKUP_PERIOD_PROP));
				} catch (NumberFormatException nfe) {
					period = DEFAULT_PERIOD;
				}
				BackupAuditFileProcessor processor = new BackupAuditFileProcessor(period);
				processor.start();
			}
		}
	}

	/**
	 * Gets a class instance.
	 * @return	An instance of the class.
	 */
	public static EventCollector getInstance() {
		if (instance == null) {
			instance = new EventCollector();
		}
		return instance;
	}

	/**
	 * Gets the file event identifier.
	 * @return	Identifier.
	 */
	public String getEventId() {
		return this.eventId;
	}

	/**
	 * Flush of the collector.
	 * @param eventFileId	The next event file identifier.
	 */
	public void flush(String eventFileId) {
		if (this.mode == BATCH_MODE) {
			modifyTransactionList(FLUSH_OPERATION, null);
		}
		this.eventId = eventFileId;
		this.storedCurrentEventFile = false;
	}
	
	/**
	 * Flush of the collector.
	 */
	public void flush() {
		if (this.mode == BATCH_MODE) {
			modifyTransactionList(FLUSH_OPERATION, null);
		}
	}

	/**
	 * Opens an audit transaction.
	 * @param trace	Audit trace.
	 */
	public void openTransaction(TraceI trace) {
		if (this.mode == BATCH_MODE) {
			if ((!(this.storedCurrentEventFile)) && (this.custodyEventFile)) {
				storeEvent();
			}
			modifyTransactionList(OPEN_OPERATION, trace);
		}
	}
	/**
	 * Closes an audit transaction.
	 * @param trace	Audit trace.
	 */
	public void closeTransaction(TraceI trace) {
		if (this.mode == BATCH_MODE) {
			modifyTransactionList(CLOSE_OPERATION, trace);
		}
	}
	/**
	 * Adds an audit transaction.
	 * @param trace	Audit trace.
	 */
	public void addTrace(TraceI trace) {
		if (this.mode == BATCH_MODE) {
			modifyTransactionList(ADD_OPERATION, trace);
		}
	}

	/**
	 * Store the event file.
	 */
	private void storeEvent() {
		try {
			if (!(this.storedCurrentEventFile)) {
				Long id = new Long(this.eventId);
				this.storedCurrentEventFile = AuditPersistenceFacade.getInstance().existEventFile(id, true);
				if (!(this.storedCurrentEventFile)){
					AuditPersistenceFacade.getInstance().storeEventFile(id, this.eventCustodyType, new Date(), null, true);
				}
			}
		} catch (Exception e) {
			this.storedCurrentEventFile = false;
		}
	}

	/**
	 * Gets the custody mode of the event file.
	 * @return	Custody mode.
	 */
	public String getEventCustodyType() {
		return this.eventCustodyType;
	}

	/**
	 * Include the supplied trace in the current transaction list. 
	 * @param operation		Operation type. May be add, open o close operation.
	 * @param trace			Trace operation.
	 */
	private synchronized void modifyTransactionList(int operation, TraceI trace) {
		String transactionId = null;
		if (trace != null) {
			transactionId = String.valueOf(trace.getSecuenceId());
		}
		switch (operation) {
			case OPEN_OPERATION:
				List<TraceI> traces = this.transactions.get(transactionId);
				if (traces == null) {
					traces = new ArrayList<TraceI>();
				}
				traces.add(trace);
				this.transactions.put(transactionId, traces);
				break;
			case ADD_OPERATION:
				List<TraceI> currentTraces = this.transactions.get(transactionId);
				if (currentTraces == null) {
					currentTraces = this.uncompletedTransactions.get(transactionId);
					if (currentTraces == null) {
						currentTraces = new ArrayList<TraceI>();
					}
					currentTraces.add(trace);
					this.uncompletedTransactions.put(transactionId, currentTraces);
				} else {
					currentTraces.add(trace);
					this.transactions.put(transactionId, currentTraces);
				}
				break;
			case CLOSE_OPERATION:
				this.transactionNumber ++;
				List<TraceI> closedTraces = this.transactions.remove(transactionId);
				if (closedTraces != null) {
					closedTraces.add(trace);
					this.closedTransactions.put(transactionId, closedTraces);
				} else {
					closedTraces = this.uncompletedTransactions.get(transactionId);
					if (closedTraces == null) {
						closedTraces = new ArrayList<TraceI>();
					}
					closedTraces.add(trace);
					this.uncompletedTransactions.put(transactionId, closedTraces);
				}
				if (this.transactionNumber>= this.batchSize){
					if (!(this.closedTransactions.isEmpty())) {
						LinkedHashMap<String,List<TraceI>> clone = new LinkedHashMap<String,List<TraceI>>();
						clone.putAll(this.closedTransactions);
						EventCollectorThread thread = new EventCollectorThread(new Long(this.eventId), clone, null);
						thread.start();
						this.closedTransactions.clear();
					}
					this.transactionNumber = 0;
				}
				break;
			case FLUSH_OPERATION:
				LinkedHashMap <String,List<TraceI>> transactionsClone = new LinkedHashMap <String,List<TraceI>>();
				LinkedHashMap <String,List<TraceI>>  unClone = new LinkedHashMap <String,List<TraceI>>();

				if (!(this.closedTransactions.isEmpty())) {
					transactionsClone.putAll(this.closedTransactions);
					this.closedTransactions.clear();
				}
				if (!(this.transactions.isEmpty())) {
					transactionsClone.putAll(this.transactions);
					this.transactions.clear();
				}
				if (!(this.uncompletedTransactions.isEmpty())) {
					unClone.putAll(this.uncompletedTransactions);
					this.uncompletedTransactions.clear();
				}
				if (this.eventId != null) {
					EventCollectorThread thread = new EventCollectorThread(new Long(this.eventId), transactionsClone, unClone);
					thread.start();
				}
				this.transactionNumber = 0;
		}
	}

	/**
	 * Gets the mode operation of the collector.
	 * @return	Mode operation.
	 */
	public int getMode() {
		return this.mode;
	}

	/**
	 * Gets if the event file is stored.
	 * @return	True if the event file is stored. Otherwise false.
	 */
	public boolean isCustodyEventFile() {
		return this.custodyEventFile;
	}
}
