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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.AuditManager.java.</p>
 * <b>Description:</b><p>Class that represents the audit manager.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit;

import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.access.AuditPersistenceFacade;
import es.gob.signaturereport.maudit.item.AuditField;
import es.gob.signaturereport.maudit.item.AuditOperation;
import es.gob.signaturereport.maudit.item.AuditService;
import es.gob.signaturereport.maudit.item.AuditTransaction;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.persistence.exception.AuditPersistenceException;
import es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceFacade;
import es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceType;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.UniqueNumberGenerator;

/**
 * <p>Class that represents the audit manager.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
@Singleton
@ManagedBean
public class AuditManager implements AuditManagerI {

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = 7678642077903350977L;
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AuditManager.class);
	/**
	 * Attribute that represents the name of audit logger.
	 */
	private static final String AUDIT_LOGGER_NAME = "EventLogger";
	/**
	 * Attribute that represents the object that manages the event log.
	 */
	private static final Logger AUDIT_LOGGER = Logger.getLogger(AUDIT_LOGGER_NAME);
	/**
	 * Attribute that represents the key used to indicate whether the request custody.
	 */
	private static final String CUSTODY_REQUEST_KEY = "signaturereport.audit.custody.request";
	/**
	 * Attribute that represents the key used to indicate whether the response custody.
	 */
	private static final String CUSTODY_RESPONSE_KEY = "signaturereport.audit.custody.response";

	/**
	 * Attribute that represents a class instance.
	 */
	private static AuditManager instance = null;
	/**
	 * Attribute that represents whether the request is stored.
	 */
	private boolean custodyRequest = false;
	/**
	 * Attribute that represents whether the response is stored.
	 */
	private boolean custodyResponse = false;

	/**
	 * Attribute that represents all services registered into the audit module.
	 */
	private AuditService[ ] services = null;

	/**
	 * Attribute that represents all operations registered into the audit module.
	 */
	private AuditOperation[ ] operations = null;

	/**
	 * Attribute that represents all fields registered into the audit module.
	 */
	private AuditField[ ] fields = null;
    
    /**
     * Attribute that represents the alarms and summaries events scheme manager BOs.
     */
    @Inject
    private AuditPersistenceFacade auditPersistenceFacade;
	
	
	/**
	 * {@link Const}ructor method for the class AuditManager.java.
	 */
	@PostConstruct
	public final void init() {
		
		instance = this;
						
		String storeRequest = StaticSignatureReportProperties.getProperty(CUSTODY_REQUEST_KEY);
		if (storeRequest != null) {
			LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_005, new Object[ ] { CUSTODY_REQUEST_KEY, storeRequest }));
			this.custodyRequest = Boolean.parseBoolean(storeRequest.trim().toLowerCase());
		} else {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { CUSTODY_REQUEST_KEY }));
		}

		String storeResponse = StaticSignatureReportProperties.getProperty(CUSTODY_RESPONSE_KEY);
		if (storeResponse != null) {
			LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_005, new Object[ ] { CUSTODY_RESPONSE_KEY, storeResponse }));
			this.custodyResponse = Boolean.parseBoolean(storeResponse.trim().toLowerCase());
		} else {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { CUSTODY_RESPONSE_KEY }));
		}

		this.services = auditPersistenceFacade.getAllServices();

		this.operations = auditPersistenceFacade.getAllOperations();

		this.fields = auditPersistenceFacade.getAllFields();
		
		SOAPPersistenceFacade.getInstance().setSoapRequestType(SOAPPersistenceType.DB);
		SOAPPersistenceFacade.getInstance().setSoapResponseType(SOAPPersistenceType.DB);
	}
	
	/**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	instance = null;
    }

	/**
	 * Gets a class instance.
	 * @return	An instance of the class.
	 */
	public static AuditManager getInstance() {
		if (instance == null) {
			instance = new AuditManager();
		}
		return instance;
	}

//	/**
//	 * Gets the value of the identifier of current log event.
//	 * @return the value of the identifier of current log event.
//	 */
//	public long getEventId() {
//		return this.eventId;
//	}
//
//	/**
//	 * Sets the value of the identifier of current log event.
//	 * @param eventFileId The value for the identifier of current log event.
//	 */
//	public void setEventId(long eventFileId) {
//		LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_001, new Object[ ] { String.valueOf(eventFileId) }));
//		this.eventId = eventFileId;
//	}

	/**
	 * Method which reports whether the request is stored.
	 * @return	True if the request is stored, otherwise false.
	 */
	public boolean isCustodyRequest() {
		return this.custodyRequest;
	}

	/**
	 * Method which reports whether the response is stored.
	 * @return	True if the response is stored, otherwise false.
	 */
	public boolean isCustodyResponse() {
		return this.custodyResponse;
	}

	/**
	 * Store a SOAP Request Message.
	 * @param soap SOAP Message.
	 * @param transactionId		Event transaction identifier.
	 * @return Custody identifier.
	 * @throws AuditException if an error occurs.
	 */
	public String storeRequestMessage(byte[ ] soap,long transactionId) throws AuditException {
		try {
			return SOAPPersistenceFacade.getInstance().storeSOAPRequest(soap,transactionId);
		} catch (AuditPersistenceException e) {
			int code = AuditException.UNKNOWN_ERROR;
			if (e.getCode() == AuditPersistenceException.INVALID_INPUT_PARAMETERS || e.getCode() == AuditPersistenceException.ITEM_NOT_FOUND) {
				code = AuditException.INVALID_INPUT_PARAMETERS;
			}
			throw new AuditException(code, e.getDescription(),e);
		}
	}

	/**
	 * Store a SOAP Response Message.
	 * @param soap SOAP Message.
	 * @param transactionId Audit transaction identifier.
	 * @return Custody identifier.
	 * @throws AuditException if an error occurs.
	 */
	public String storeResponseMessage(byte[ ] soap,long transactionId) throws AuditException {
		try {
			return SOAPPersistenceFacade.getInstance().storeSOAPResponse(soap,transactionId);
		} catch (AuditPersistenceException e) {
			int code = AuditException.UNKNOWN_ERROR;
			if (e.getCode() == AuditPersistenceException.INVALID_INPUT_PARAMETERS || e.getCode() == AuditPersistenceException.ITEM_NOT_FOUND) {
				code = AuditException.INVALID_INPUT_PARAMETERS;
			}
			throw new AuditException(code, e.getDescription(),e);
		}
	}

	/**
	 * Open an event transaction.
	 * @param serviceId		Service identifier.
	 * @param messageHash Message hash.
	 * @return	Event identifier.
	 */
	public long openTransaction(int serviceId, String messageHash) {
		long sequenceId = UniqueNumberGenerator.getInstance().getNumber();
		TraceI trace = TraceFactory.getTraceElement(sequenceId, OPEN_TRANSACTION_ACT, serviceId, new Date(), messageHash);
		AUDIT_LOGGER.info(trace.getMessage());
		EventCollector.getInstance().openTransaction(trace);
		return sequenceId;
	}

	/**
	 * Add trace to the current transaction.
	 * @param trace	Trace information.
	 */
	public void addTrace(TraceI trace) {
		AUDIT_LOGGER.info(trace.getMessage());
		EventCollector.getInstance().addTrace(trace);
	}

	/**
	 * Close the event transaction associated to supplied identifier.
	 * @param sequenceId		Sequence identifier.
	 * @param serviceId		Service identifier.
	 * @param messageHash Message hash.
	 */
	public void closeTransaction(long sequenceId, int serviceId, String messageHash) {
		TraceI trace = TraceFactory.getTraceElement(sequenceId, CLOSE_TRANSACTION_ACT, serviceId, new Date(), messageHash);
		AUDIT_LOGGER.info(trace.getMessage());
		EventCollector.getInstance().closeTransaction(trace);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.AuditManagerI#getTransactions(java.util.Date, java.util.Date, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<AuditTransaction> getTransactions(Date beginingTime, Date endingDate, Integer service, String application, Integer firstResult, Integer maxResults) throws AuditException {
		if (beginingTime == null) {
			throw new AuditException(AuditException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_060));
		}
		try {
			return auditPersistenceFacade.getTransactions(beginingTime, endingDate, service, application, firstResult, maxResults);
		} catch (AuditPersistenceException e) {
			throw new AuditException(AuditException.UNKNOWN_ERROR, e.getDescription(),e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.AuditManagerI#getTransaction(long)
	 */
	public AuditTransaction getTransaction(long transactionId) throws AuditException {
		try {
			AuditTransaction tran = auditPersistenceFacade.getTransaction(transactionId);
			if(tran!=null){
				//Ordenamos las trazas ya que pueden venir desordenadas de BBDD
				int[] order = null; 
				if(tran.getServiceId()== GENERATION_REPORT_SRVC){
					order = GENERATION_REPORT_OP;
				}else if(tran.getServiceId()== VALIDATION_REPORT_SRVC){
					order =VALIDATION_REPORT_OP;
				}
				if(order!=null){
					TraceI[] orderTraces = new TraceI[order.length];
					TraceI currentTrace = null;
					while(!tran.getTraces().isEmpty()){
						currentTrace = tran.getTraces().remove(0);
						int i = 0;
						while (i<order.length && currentTrace.getActionId()!= order[i]){
							i++;
						}
						if(i<order.length){
							orderTraces[i] = currentTrace;
						}
					}
					for(int i=0;i<orderTraces.length;i++){
						if(orderTraces[i]!=null){
							tran.getTraces().add(orderTraces[i]);
						}
					}
				}
			}
			return tran;
		} catch (AuditPersistenceException e) {
			throw new AuditException(AuditException.UNKNOWN_ERROR, e.getDescription(),e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.AuditManagerI#getServices()
	 */
	public AuditService[ ] getServices() {
		return services;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.AuditManagerI#getOperations()
	 */
	public AuditOperation[ ] getOperations() {
		return operations;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.AuditManagerI#getFields()
	 */
	public AuditField[ ] getFields() {
		return fields;
	}
	
	
}
