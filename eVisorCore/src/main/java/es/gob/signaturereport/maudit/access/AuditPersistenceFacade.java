// Copyright (C) 2017 MINHAP, Gobierno de España
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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.AuditPersistenceFacade.java.</p>
 * <b>Description:</b><p> Class that manages the persistence operation of audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.maudit.item.AuditField;
import es.gob.signaturereport.maudit.item.AuditOperation;
import es.gob.signaturereport.maudit.item.AuditService;
import es.gob.signaturereport.maudit.item.AuditTransaction;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceFieldValue;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.persistence.exception.AuditPersistenceException;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IActionTypeBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IEventLogBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IFieldBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.IServicesBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITraceFieldBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITraceTransactionBO;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ActionTypePOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.EventLogPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.FieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ServicesPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceFieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO;

/** 
 * <p>Class that manages the persistence operation of audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
@Singleton
@ManagedBean
public class AuditPersistenceFacade {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AuditPersistenceFacade.class);
	/**
	 * Attribute that represents a class instance. 
	 */
	private static AuditPersistenceFacade instance = null;
	
	/**
	 * Attribute that represents the audit field. 
	 */
	private LinkedHashMap<String, FieldPOJO> fields = new LinkedHashMap<String, FieldPOJO>();
	
	/**
	 * Attribute that allows to operate with information about event logs of the platform.
	 */
	@Inject
	private IEventLogBO eventLogBO;
	
	/**
	 * Attribute that allows to operate with information about trace transactions of the platform.
	 */
	@Inject
	private ITraceTransactionBO traceTransactionBO;
	
	/**
	 * Attribute that allows to operate with information about fields of the platform.
	 */
	@Inject
	private IFieldBO fieldBO;
	
	/**
	 * Attribute that allows to operate with information about fields of the platform.
	 */
	@Inject
	private ITraceFieldBO traceFieldBO;
	
	/**
	 * Attribute that allows to operate with information about transaction of the platform.
	 */
	@Inject
	private ITransactionBO transactionBO;
	
	/**
	 * Attribute that allows to operate with information about services of the platform.
	 */
	@Inject
	private IServicesBO servicesBO;
	
	/**
	 * Attribute that allows to operate with information about the action types of the platform.
	 */
	@Inject
	private IActionTypeBO actinTypesBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;
	
	/**
	 * Constructor method for the class AuditPersistenceFacade.java. 
	 */
//	private AuditPersistenceFacade() {
//		
//		try {
//			getFields();
//		} catch (ConfigurationException e) {
//			
//		}
//	}

	/**
	 * Gets an instance of the class. 
	 * @return	An {@link AuditPersistenceFacade} object. 
	 */
	public static AuditPersistenceFacade getInstance() {
		if (instance == null) {
			instance = new AuditPersistenceFacade();
		}
		return instance;
	}
	
	/**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {
    	instance = this;
    	
    	try {
			getFields();
		} catch (ConfigurationException e) {
			
		}
    }

    /**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	instance = null;
    }
	
	

	/**
	 * Store the event file.
	 * @param identifier 		Store identifier.		   
	 * @param eventCustodyType 	Custody mode.
	 * @param creationTime	   	Creation date of event file. 
	 * @param file			  	 Event file. Optional.			   
	 * @param sendAlarm			Indicates if the alarm is sent. 
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public void storeEventFile(Long identifier, String eventCustodyType, Date creationTime, byte[ ] file, boolean sendAlarm) throws AuditPersistenceException {
		
		try {
			
			eventLogBO.storeEventFile(identifier, eventCustodyType, creationTime, file);
			
		} catch (DatabaseException e) {
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			
			String msg = Language.getMessage(LanguageKeys.AUD_015);
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg,e);
		} 
	}

	/**
	 * Evaluates if exists event file with the supplied identifier. 
	 * @param eventId	Event file identifier.
	 * @param sendAlarm	 Indicates if the alarm is sent.
	 * @return	True if exists, otherwise false.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public boolean existEventFile(Long eventId, boolean sendAlarm) throws AuditPersistenceException {
		
		try {
			
			return eventLogBO.existEventFile(eventId);
			
		} catch (DatabaseException e) {
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
						
			String msg = Language.getFormatMessage(LanguageKeys.AUD_018, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		} 
	}

	/**
	 * Gets the custody mode of event file associated to supplied identifier.
	 * @param eventId	Event file identifier.
	 * @return			Custody mode.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public String getEventCustodyMode(Long eventId) throws AuditPersistenceException {
		
		try {
			
			EventLogPOJO eventLog = eventLogBO.getEventLog(eventId);
			
			if (eventLog == null) {
				throw new AuditPersistenceException(AuditPersistenceException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.AUD_017, new Object[ ] { eventId }));
			}
			return eventLog.getStoreType();
			
		} catch (DatabaseException e) {
			
			String msg = Language.getFormatMessage(LanguageKeys.AUD_018, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		} 
	}

	/**
	 * Updates the content of event record associate to supplied identifier.
	 * @param eventId	Event file identifier.
	 * @param contentToStore	Content to store.
	 * @param storeType Store type. Optional.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public void updateEventContent(Long eventId, byte[ ] contentToStore, String storeType) throws AuditPersistenceException {
		
		try {
			eventLogBO.updateEventContent(eventId, contentToStore, storeType);
			
		} catch (DatabaseException e) {
			
			String msg = Language.getFormatMessage(LanguageKeys.AUD_023, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		} 
	}

	/**
	 * Registers the supplied opened transactions list.
	 * @param transactions	opened transactions list.
	 * @param sendAlarm		Indicates if the alarm is sent.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public void saveOpenedTraces(Map<String, List<TraceI>> transactions, boolean sendAlarm) throws AuditPersistenceException {
				
		try {
			
			if ((transactions != null) && (!(transactions.isEmpty()))) {
				String[ ] ids = new String[transactions.size()];
				ids = (String[ ]) transactions.keySet().toArray(ids);
				for (int i = 0; i < ids.length; ++i) {
					List<TraceI> traces = transactions.remove(ids[i]);
					for (int j = 1; j < traces.size(); ++j) {
						TraceI trace = traces.get(j);
						if ((trace.getActionId() != 0) && (trace.getActionId() != 1)) {
							final TraceTransactionPOJO persistenceTrace = new TraceTransactionPOJO();
							persistenceTrace.setCreationTime(trace.getCreationTime());
							final ActionTypePOJO persistenceAction = new ActionTypePOJO();
							persistenceAction.setActionType(trace.getActionId());
							persistenceTrace.setActionType(persistenceAction);
							persistenceTrace.setTransaction(new Long(trace.getSecuenceId()));
							traceTransactionBO.save(persistenceTrace);
							
							Map<String, List<TraceFieldValue>> traceFields = trace.getFields();
							if ((traceFields != null) && (!(traceFields.isEmpty()))) {
								Iterator<String> fieldIds = traceFields.keySet().iterator();
								while (fieldIds.hasNext()) {
									String fieldId = fieldIds.next();
									FieldPOJO f = this.fields.get(fieldId);
									if (f != null) {
										Iterator<TraceFieldValue> fieldValues = traceFields.get(fieldId).iterator();
										while (fieldValues.hasNext()) {
											TraceFieldValue value = fieldValues.next();
											final TraceFieldPOJO tf = new TraceFieldPOJO();
											tf.setCreationTime(new Date());
											tf.setField(f);
											tf.setFieldValue(value.getFieldValue());
											tf.setFieldValueType(value.getFieldValueType());
											tf.setTraceTransaction(persistenceTrace);
											
											traceFieldBO.save(tf);
											
										}
									}
								}
							}
						}
					}
					
					LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_030, new Object[ ] { ids[i] }));
				}
			}
		} catch (DatabaseException e) {
			
			String msg = Language.getMessage(LanguageKeys.AUD_029);
			LOGGER.error(msg, e);
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		} 
	}

	/**
	 * Registers the supplied closed transactions list.
	 * @param eventId		Event file identifier.
	 * @param transactions	List of closed transactions.
	 * @param sendAlarm		Indicates if an alarm is sent if an error occurs.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public void saveCompletedTraces(Long eventId, Map<String, List<TraceI>> transactions, boolean sendAlarm) throws AuditPersistenceException {
		
		LinkedHashMap<String, List<TraceI>> transactionsToCommit = new LinkedHashMap<String, List<TraceI>>();
		try {
					
			if ((transactions != null) && (!(transactions.isEmpty()))) {
				String[ ] ids = new String[transactions.size()];
				ids = (String[ ]) transactions.keySet().toArray(ids);
				for (int i = 0; i < ids.length; ++i) {
					List<TraceI> traces = transactions.remove(ids[i]);
					if (traces != null) {
						transactionsToCommit.put(ids[i], traces);
						boolean ok = true;

						final TransactionPOJO tran = new TransactionPOJO();
						tran.setCreationTime(traces.get(0).getCreationTime());
						final ServicesPOJO service = new ServicesPOJO();
						service.setServicePk(traces.get(0).getServiceIdentifier());
						tran.setService(service);
						tran.setEventId(eventId);
						tran.setTransactionPk(new Long(traces.get(0).getSecuenceId()));
						try {
							
							transactionBO.save(tran);
						} catch (DatabaseException e) {
							LOGGER.warn(Language.getFormatMessage(LanguageKeys.AUD_030, new Object[ ] { Long.valueOf(traces.get(0).getSecuenceId()) }));
							ok = false;
						}
						if (ok) {
							for (int j = 1; j < traces.size(); ++j) {
								TraceI trace = traces.get(j);
								if ((trace.getActionId() != 0) && (trace.getActionId() != 1)) {
									final TraceTransactionPOJO persistenceTrace = new TraceTransactionPOJO();
									persistenceTrace.setCreationTime(trace.getCreationTime());
									final ActionTypePOJO actionType = new ActionTypePOJO();
									actionType.setActionType(trace.getActionId());
									persistenceTrace.setActionType(actionType);
									persistenceTrace.setTransaction(new Long(trace.getSecuenceId()));
									
									traceTransactionBO.save(persistenceTrace);
									
									Map<String, List<TraceFieldValue>> traceFields = trace.getFields();
									if ((traceFields != null) && (!(traceFields.isEmpty()))) {
										Iterator<String> fieldIds = traceFields.keySet().iterator();
										while (fieldIds.hasNext()) {
											String fieldId = fieldIds.next();
											FieldPOJO f = this.fields.get(fieldId);
											if (f != null) {
												Iterator<TraceFieldValue> fieldValues = traceFields.get(fieldId).iterator();
												while (fieldValues.hasNext()) {
													TraceFieldValue value = fieldValues.next();
													TraceFieldPOJO tf = new TraceFieldPOJO();
													tf.setCreationTime(new Date());
													tf.setField(f);
													tf.setFieldValue(value.getFieldValue());
													tf.setFieldValueType(value.getFieldValueType());
													tf.setTraceTransaction(persistenceTrace);
													
													traceFieldBO.save(tf);
													
												}
											}
										}
									}
								}
							}
						}
					}
					LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_030, new Object[ ] { ids[i] }));
				
				}
			}
		} catch (DatabaseException e) {
			
			String msg = Language.getMessage(LanguageKeys.AUD_029);
			LOGGER.error(msg, e);
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		} 
	}

	/**
	 * Gets a transaction list (no transaction details) that matches with the supplied parameters. 
	 * @param beginningTime	Beginning time.
	 * @param endingDate	Ending time.
	 * @param service		Service identifier.
	 * @param application	Application identifier.
	 * @param firstResult	Index of first result.
	 * @param maxResults	Max number of the records.
	 * @return	Transaction list.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public List<AuditTransaction> getTransactions(Date beginningTime, Date endingDate, Integer service, String application, Integer firstResult, Integer maxResults) throws AuditPersistenceException {
				
		List<AuditTransaction> transactions = new ArrayList<AuditTransaction>();
		
		try {

			final FieldPOJO appField = this.fields.get(TraceI.APPLICATION_ID);
			
			List<TransactionPOJO> dbTrans = transactionBO.getTransactions(beginningTime, endingDate, service, application, firstResult, maxResults, appField);
						
			if (dbTrans != null) {
				for (int i = 0; i < dbTrans.size(); i++) {
					TransactionPOJO t = dbTrans.get(i);
					AuditTransaction at = new AuditTransaction(t.getService().getServicePk(), t.getEventId(), t.getTransactionPk(), t.getCreationTime());
					transactions.add(at);
				}
			}

			return transactions;
			
		} catch (DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.AUD_041);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg,e);
		} 
	}

	/**
	 * Gets transaction details associated to supplied identifier. 
	 * @param transactionId	Transaction identifier.
	 * @return	Audit transaction details.
	 * @throws AuditPersistenceException	If an error occurs.
	 */
	public AuditTransaction getTransaction(long transactionId) throws AuditPersistenceException {
		
		AuditTransaction transaction = null;
		
		try {
			
			final TransactionPOJO t = transactionBO.getTransaction(transactionId);
			if (t != null) {
				transaction = new AuditTransaction(t.getService().getServicePk(), t.getEventId(), t.getTransactionPk(), t.getCreationTime());
				Iterator<TraceTransactionPOJO> traces = t.getTraces().iterator();
				while (traces.hasNext()) {
					TraceTransactionPOJO traceTran = traces.next();
					TraceI trace = TraceFactory.getTraceElement(traceTran.getTransaction().longValue(), traceTran.getActionType().getActionType(), t.getService().getServicePk(), traceTran.getCreationTime(), null);
					Iterator<TraceFieldPOJO> traceFields = traceTran.getTraceFields().iterator();
					while (traceFields.hasNext()) {
						TraceFieldPOJO field = traceFields.next();
						String fieldLabel = field.getField().getLabel();
						List<TraceFieldValue> fieldValues = trace.getFields().get(fieldLabel);
						if (fieldValues == null) {
							fieldValues = new ArrayList<TraceFieldValue>();
						}
						TraceFieldValue value = new TraceFieldValue(field.getFieldValue(), field.getFieldValueType());
						fieldValues.add(value);
						trace.getFields().put(fieldLabel, fieldValues);
					}
					transaction.getTraces().add(trace);
				}
			}

			return transaction;
		} catch (DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AUD_042, new Object[ ] { String.valueOf(transactionId) });
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg, e);
		}
	}

	/**
	 * Gets all services registered into audit module.
	 * @return	Array of {@link AuditService} objects.
	 */
	public AuditService[] getAllServices(){
			
		AuditService[] services = null;
		
		try {
			
			final List<ServicesPOJO> serviceList = servicesBO.getAll();
			if(serviceList != null && !serviceList.isEmpty()){
    			services = new AuditService[serviceList.size()];
    			for(int i=0;i<services.length;i++){
    				ServicesPOJO  s = serviceList.get(i);
    				services[i] = new AuditService(s.getServicePk(),Language.getMessage(s.getDescription()));
    			}
			}
		} catch (DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.AUD_061);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		
		return services;
	}

	/**
	 * Gets all operations registered in the audit module.
	 * @return	Audit operation list.
	 */
	public AuditOperation[] getAllOperations(){
				
		AuditOperation[] operations = null;
		
		try {
			
			List<ActionTypePOJO> actionList = actinTypesBO.getAll();
			if(actionList!=null && ! actionList.isEmpty()){
				operations = new AuditOperation[actionList.size()];
				for(int i = 0;i<operations.length;i++){
					ActionTypePOJO at = actionList.get(i);
					operations[i] = new AuditOperation(at.getActionType(), Language.getMessage(at.getDescription()));
				}
			}
		} catch (DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.AUD_062);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		return operations;
	}

	/**
	 * Gets all audit fields registered into audit module.
	 * @return	Array of {@link AuditField} object.
	 */
	public AuditField[] getAllFields(){
				
		AuditField[] auditFields = null;
		
		try {
			
			List<FieldPOJO> fieldList = fieldBO.getAll();
			if(fieldList!=null && !fieldList.isEmpty()){
				auditFields = new AuditField[fieldList.size()];
				for(int i=0;i<auditFields.length;i++){
					FieldPOJO f = fieldList.get(i);
					auditFields[i] = new AuditField(f.getLabel(), Language.getMessage(f.getDescription()));
				}
			}
			
		} catch (DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.AUD_028);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		return auditFields;
	}
	
	/**
	 * Loads all fields registered into audit module.
	 */
	private void getFields() throws ConfigurationException{
		
		try {
			
			final List<FieldPOJO> fields = fieldBO.getAll();
			
			if (fields == null)
			{
				String msg = Language.getMessage(LanguageKeys.AUD_074);
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg);
			}
			
			Iterator<FieldPOJO> fieldIt = fields.iterator();
			while (fieldIt.hasNext()) {
				FieldPOJO f = fieldIt.next();
				this.fields.put(f.getLabel(), f);
			}
		} catch (DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.AUD_028);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
	}
}
