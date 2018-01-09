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
package es.gob.signaturereport.standalone.maudit.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueObjectException;

import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.maudit.item.AuditField;
import es.gob.signaturereport.maudit.item.AuditOperation;
import es.gob.signaturereport.maudit.item.AuditService;
import es.gob.signaturereport.maudit.item.AuditTransaction;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceFieldValue;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.pojo.ActionTypePOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.EventLogPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.FieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ServicesPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceFieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceFieldPOJO_;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO_;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO_;
import es.gob.signaturereport.persistence.utils.IAttributeEntityConstants;
import es.gob.signaturereport.standalone.maudit.i18n.LanguageStandalone;
import es.gob.signaturereport.standalone.persistence.em.StandaloneAuditEntityManager;

/** 
 * <p>Class that manages the persistence operation of audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public final class StandaloneAuditPersistenceFacade {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StandaloneAuditPersistenceFacade.class);
	/**
	 * Attribute that represents a class instance. 
	 */
	private static StandaloneAuditPersistenceFacade instance = null;

	/**
	 * Attribute that represents the batch size to commit. 
	 */
	private static final int BATCH_SIZE = 100;
	
	/**
	 * Attribute that represents the audit field. 
	 */
	private LinkedHashMap<String, FieldPOJO> fields = new LinkedHashMap<String, FieldPOJO>();

	
	/**
	 * Constructor method for the class AuditPersistenceFacade.java. 
	 */
	private StandaloneAuditPersistenceFacade() {
		
		try {
			getFields();
		} catch (ConfigurationException e) {
			
		}
	}

	/**
	 * Gets an instance of the class. 
	 * @return	An {@link StandaloneAuditPersistenceFacade} object. 
	 */
	public static StandaloneAuditPersistenceFacade getInstance() {
		if (instance == null) {
			instance = new StandaloneAuditPersistenceFacade();
		}
		return instance;
	}
	


	/**
	 * Store the event file.
	 * @param identifier 		Store identifier.		   
	 * @param eventCustodyType 	Custody mode.
	 * @param creationTime	   	Creation date of event file. 
	 * @param file			  	 Event file. Optional.			   
	 * @param sendAlarm			Indicates if the alarm is sent. 
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public void storeEventFile(Long identifier, String eventCustodyType, Date creationTime, byte[ ] file, boolean sendAlarm) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		EntityTransaction tx = null;
		
		try {
			
			final EventLogPOJO eventLog = new EventLogPOJO();
			
			if (file != null) {
				eventLog.setContent(file);
				eventLog.setStoreTime(new Date());
			}
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			tx.begin();
			em.persist(eventLog);
			tx.commit();			
			
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_015);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new StandaloneAuditPersistenceException(DatabaseException.UNKNOWN_ERROR, msg, e);
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Evaluates if exists event file with the supplied identifier. 
	 * @param eventId	Event file identifier.
	 * @param sendAlarm	 Indicates if the alarm is sent.
	 * @return	True if exists, otherwise false.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public boolean existEventFile(Long eventId, boolean sendAlarm) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		EntityTransaction tx = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			tx.begin();
			final EventLogPOJO eventLog = (EventLogPOJO) em.find(EventLogPOJO.class, eventId);
			tx.commit();
						
			return (eventLog != null);
			
		} catch (Exception e) {
			String msg = LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_018, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new StandaloneAuditPersistenceException(DatabaseException.UNKNOWN_ERROR, msg, e);
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Gets the custody mode of event file associated to supplied identifier.
	 * @param eventId	Event file identifier.
	 * @return			Custody mode.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public String getEventCustodyMode(Long eventId) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		EntityTransaction tx = null;
		EventLogPOJO eventLog = new EventLogPOJO();
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			tx.begin();
			eventLog = em.find(EventLogPOJO.class, eventId);
			tx.commit();
				
		} catch (Exception e) {
			
			String msg = LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_018, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new StandaloneAuditPersistenceException(DatabaseException.UNKNOWN_ERROR, msg, e);
			
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
		
		return eventLog.getStoreType();
		
	}

	/**
	 * Updates the content of event record associate to supplied identifier.
	 * @param eventId	Event file identifier.
	 * @param contentToStore	Content to store.
	 * @param storeType Store type. Optional.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public void updateEventContent(Long eventId, byte[ ] contentToStore, String storeType) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		EntityTransaction tx = null;
		EventLogPOJO eventLog = null;
		
		try {
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			tx.begin();
			eventLog = em.find(EventLogPOJO.class, eventId);
					
			if (eventLog == null) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_017, new Object[ ] { eventId }));
			}
			eventLog.setContent(contentToStore);
			eventLog.setStoreTime(new Date());
			if (storeType != null) {
				eventLog.setStoreType(storeType);
			}
			
			em.merge(eventLog);
			tx.commit();
		} catch (Exception e) {
		
			String msg = LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_023, new Object[ ] { eventId });
			LOGGER.error(msg, e);
			throw new StandaloneAuditPersistenceException(DatabaseException.UNKNOWN_ERROR, msg, e);
			
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Registers the supplied opened transactions list.
	 * @param transactions	opened transactions list.
	 * @param sendAlarm		Indicates if the alarm is sent.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public void saveOpenedTraces(Map<String, List<TraceI>> transactions, boolean sendAlarm) throws StandaloneAuditPersistenceException {
				
		EntityManager em = null;
		EntityTransaction tx = null;
		LinkedHashMap<String, List<TraceI>> transactionsToCommit = new LinkedHashMap<String, List<TraceI>>();
		try {
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			if ((transactions != null) && (!(transactions.isEmpty()))) {
				tx.begin();
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
							em.persist(persistenceTrace);
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
											em.persist(tf);
										}
									}
								}
							}
						}
					}
					if ((i % BATCH_SIZE == 0) || (i == ids.length - 1)) {
						em.flush();
						em.clear();
						tx.commit();
						transactionsToCommit.clear();
						if (!(em.isOpen())) {
							em.close();
							em = StandaloneAuditEntityManager.getInstance().getEntityManager();
							tx = em.getTransaction();
						}
						LOGGER.info(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_030, new Object[ ] { ids[i] }));
					}
				}
			}
		} catch (Exception e) {
			
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_029);
			LOGGER.error(msg, e);
			if (!(transactionsToCommit.isEmpty())) {
				transactions.putAll(transactionsToCommit);
				transactionsToCommit.clear();
			}
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			
			throw new StandaloneAuditPersistenceException(StandaloneAuditPersistenceException.UNKNOWN_ERROR, msg, e);
			
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Registers the supplied closed transactions list.
	 * @param eventId		Event file identifier.
	 * @param transactions	List of closed transactions.
	 * @param sendAlarm		Indicates if an alarm is sent if an error occurs.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public void saveCompletedTraces(Long eventId, Map<String, List<TraceI>> transactions, boolean sendAlarm) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		EntityTransaction tx = null;
		LinkedHashMap<String, List<TraceI>> transactionsToCommit = new LinkedHashMap<String, List<TraceI>>();
		try {
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			
			if ((transactions != null) && (!(transactions.isEmpty()))) {
				String[ ] ids = new String[transactions.size()];
				ids = (String[ ]) transactions.keySet().toArray(ids);
				for (int i = 0; i < ids.length; ++i) {
					List<TraceI> traces = transactions.remove(ids[i]);
					if (traces != null) {
						tx.begin();
						transactionsToCommit.put(ids[i], traces);
						boolean ok = true;

						TransactionPOJO tran = new TransactionPOJO();
						tran.setCreationTime(traces.get(0).getCreationTime());
						ServicesPOJO service = new ServicesPOJO();
						service.setServicePk(traces.get(0).getServiceIdentifier());
						tran.setService(service);
						tran.setEventId(eventId);
						tran.setTransactionPk(new Long(traces.get(0).getSecuenceId()));
						
						try {
							em.persist(tran);
								
						} catch (NonUniqueObjectException nuoe) {
							
							LOGGER.warn(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_030, new Object[ ] { Long.valueOf(traces.get(0).getSecuenceId()) }));
							ok = false;
						}
												
						if (ok) {
							for (int j = 1; j < traces.size(); ++j) {
								TraceI trace = traces.get(j);
								if ((trace.getActionId() != 0) && (trace.getActionId() != 1)) {
									TraceTransactionPOJO persistenceTrace = new TraceTransactionPOJO();
									persistenceTrace.setCreationTime(trace.getCreationTime());
									ActionTypePOJO actionType = new ActionTypePOJO();
									actionType.setActionType(trace.getActionId());
									persistenceTrace.setActionType(actionType);
									persistenceTrace.setTransaction(new Long(trace.getSecuenceId()));
									em.persist(persistenceTrace);
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
													em.persist(tf);
												}
											}
										}
									}
								}
							}
						}
					}
					
					if ((i % BATCH_SIZE == 0) || (i == ids.length - 1)) {
						em.flush();
						em.clear();
						tx.commit();
						transactionsToCommit.clear();
						if (!(em.isOpen())) {
							em.close();
							em = StandaloneAuditEntityManager.getInstance().getEntityManager();
							tx = em.getTransaction();
						}
						LOGGER.info(LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_030, new Object[ ] { ids[i] }));
					}
				}
			}
		} catch (Exception e) {
			
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_029);
			LOGGER.error(msg, e);
			
			if (!(transactionsToCommit.isEmpty())) {
				transactions.putAll(transactionsToCommit);
				transactionsToCommit.clear();
			}
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			
			throw new StandaloneAuditPersistenceException(StandaloneAuditPersistenceException.UNKNOWN_ERROR, msg, e);
			
		} finally {
			
			if (em != null && em.isOpen()) {
				em.close();
			}
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
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public List<AuditTransaction> getTransactions(Date beginningTime, Date endingDate, Integer service, String application, Integer firstResult, Integer maxResults) throws StandaloneAuditPersistenceException {
				
		EntityManager em = null;
		
		List<AuditTransaction> transactions = new ArrayList<AuditTransaction>();
		
		try {

			final FieldPOJO appField = this.fields.get(TraceI.APPLICATION_ID);
			
			TypedQuery<TransactionPOJO> queryTransactions = null;
			
			List<Predicate> predList = new LinkedList<Predicate>();
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
					
			// Preparo la sentencia dinámica
			CriteriaBuilder critBuilder = em.getCriteriaBuilder();
			CriteriaQuery<TransactionPOJO> criteria = critBuilder.createQuery(TransactionPOJO.class);
			Root<TransactionPOJO> transaction = criteria.from(TransactionPOJO.class);
			
			if (beginningTime != null) {
			
				predList.add(critBuilder.greaterThanOrEqualTo(transaction.<Date> get(IAttributeEntityConstants.ATTR_CREATIONTIME), beginningTime));
			}
			
			if (endingDate != null) {
				
				predList.add(critBuilder.lessThanOrEqualTo(transaction.<Date> get(IAttributeEntityConstants.ATTR_CREATIONTIME), endingDate));
			}
			
			if (service != null) {
				final ServicesPOJO servicePojo = new ServicesPOJO();
				servicePojo.setServicePk(service);
				predList.add(critBuilder.equal(transaction.<ServicesPOJO> get(IAttributeEntityConstants.ATTR_SERVICE), servicePojo));
			}
			
			if (application != null && !"".equals(application.trim())) {
				
				Join<TransactionPOJO, TraceTransactionPOJO> transactionJoinTraceTransaction = transaction.join(TransactionPOJO_.traces);
				Join<TraceTransactionPOJO, TraceFieldPOJO> traceTransactionJoinTraceField =  transactionJoinTraceTransaction.join(TraceTransactionPOJO_.traceFields);
							
				predList.add(critBuilder.and(critBuilder.equal(traceTransactionJoinTraceField.get(TraceFieldPOJO_.field), appField), critBuilder.like(traceTransactionJoinTraceField.get(TraceFieldPOJO_.fieldValue), application)));
				
				//crit.createCriteria(TRACES).createCriteria(TRACEFIELDS).add(Restrictions.and(Restrictions.eq(FIELD, appField), Restrictions.like(FIELDVALUE, application)));
			}
			
			criteria.orderBy(critBuilder.desc(transaction.get(TransactionPOJO_.creationTime)));
						
			Predicate[ ] predArray = predList.toArray(new Predicate[predList.size()]);
			
			// Combino las restricciones para formar el WHERE
			criteria = criteria.where(predArray);
			
			queryTransactions = em.createQuery(criteria);
			
			if (firstResult != null) {
				queryTransactions = queryTransactions.setFirstResult(firstResult.intValue());
				
			}
			if (maxResults != null) {
				queryTransactions = queryTransactions.setMaxResults(maxResults.intValue());
			}
			criteria.select(transaction);
			
			List<TransactionPOJO> dbTrans = null;
			
			try {
				
				dbTrans = queryTransactions.getResultList();
												
			} catch (PersistenceException pe) {
			
				String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_041);
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			}
									
			if (transactions != null) {
				for (int i = 0; i < dbTrans.size(); i++) {
					TransactionPOJO t = dbTrans.get(i);
					AuditTransaction at = new AuditTransaction(t.getService().getServicePk(), t.getEventId(), t.getTransactionPk(), t.getCreationTime());
					transactions.add(at);
				}
			}

			return transactions;
			
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_041);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			throw new StandaloneAuditPersistenceException(StandaloneAuditPersistenceException.UNKNOWN_ERROR, msg,e);
		} finally {
			
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Gets transaction details associated to supplied identifier. 
	 * @param transactionId	Transaction identifier.
	 * @return	Audit transaction details.
	 * @throws StandaloneAuditPersistenceException	If an error occurs.
	 */
	public AuditTransaction getTransaction(long transactionId) throws StandaloneAuditPersistenceException {
		
		EntityManager em = null;
		AuditTransaction transaction = null;
		
		try {
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			final TransactionPOJO t = em.find(TransactionPOJO.class, transactionId);
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
		} catch (Exception e) {
			String msg = LanguageStandalone.getFormatResStandaloneAudit(LanguageKeys.AUD_042, new Object[ ] { String.valueOf(transactionId) });
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			throw new StandaloneAuditPersistenceException(StandaloneAuditPersistenceException.UNKNOWN_ERROR, msg, e);
		}
	}

	/**
	 * Gets all services registered into audit module.
	 * @return	Array of {@link AuditService} objects.
	 */
	@SuppressWarnings("unchecked")
	public AuditService[] getAllServices(){
			
		EntityManager em = null;
		AuditService[] services = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			
			String consulta = "SELECT obj FROM ServicesPOJO AS obj ";
			Query query = em.createQuery(consulta);
			
			final List<ServicesPOJO> serviceList = (List<ServicesPOJO>) query.getResultList();
			
			if(serviceList != null && !serviceList.isEmpty()){
    			services = new AuditService[serviceList.size()];
    			for(int i=0;i<services.length;i++){
    				ServicesPOJO  s = serviceList.get(i);
    				services[i] = new AuditService(s.getServicePk(),LanguageStandalone.getResStandaloneAudit(s.getDescription()));
    			}
			}
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_061);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		
		return services;
	}

	/**
	 * Gets all operations registered in the audit module.
	 * @return	Audit operation list.
	 */
	@SuppressWarnings("unchecked")
	public AuditOperation[] getAllOperations(){
			
		EntityManager em = null;
		AuditOperation[] operations = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			
			String consulta = "SELECT obj FROM ActionTypePOJO AS obj ";
			Query query = em.createQuery(consulta);
			
			List<ActionTypePOJO> actionList = (List<ActionTypePOJO>) query.getResultList();
			if(actionList!=null && ! actionList.isEmpty()){
				operations = new AuditOperation[actionList.size()];
				for(int i = 0;i<operations.length;i++){
					ActionTypePOJO at = actionList.get(i);
					operations[i] = new AuditOperation(at.getActionType(), LanguageStandalone.getResStandaloneAudit(at.getDescription()));
				}
			}
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_062);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		return operations;
	}

	/**
	 * Gets all audit fields registered into audit module.
	 * @return	Array of {@link AuditField} object.
	 */
	@SuppressWarnings("unchecked")
	public AuditField[] getAllFields(){
			
		EntityManager em = null;
		AuditField[] auditFields = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			
			String consulta = "SELECT obj FROM ActionTypePOJO AS obj ";
			Query query = em.createQuery(consulta);
			
			List<FieldPOJO> fieldList = (List<FieldPOJO>) query.getResultList();
			if(fieldList!=null && !fieldList.isEmpty()){
				auditFields = new AuditField[fieldList.size()];
				for(int i=0;i<auditFields.length;i++){
					FieldPOJO f = fieldList.get(i);
					auditFields[i] = new AuditField(f.getLabel(), LanguageStandalone.getResStandaloneAudit(f.getDescription()));
				}
			}
			
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_028);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
		return auditFields;
	}
	
	/**
	 * Loads all fields registered into audit module.
	 */
	@SuppressWarnings("unchecked")
	private void getFields() throws ConfigurationException{
		
		EntityManager em = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			String consulta = "SELECT obj FROM FieldPOJO AS obj ";
			Query query = em.createQuery(consulta);
			
			final List<FieldPOJO> fields = (List<FieldPOJO>) query.getResultList();
			
			if (fields == null)
			{
				String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_074);
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg);
			}
			
			Iterator<FieldPOJO> fieldIt = fields.iterator();
			while (fieldIt.hasNext()) {
				FieldPOJO f = fieldIt.next();
				this.fields.put(f.getLabel(), f);
			}
		} catch (Exception e) {
			String msg = LanguageStandalone.getResStandaloneAudit(LanguageKeys.AUD_028);
			LOGGER.error(msg, e);
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
		} 
	}
}
