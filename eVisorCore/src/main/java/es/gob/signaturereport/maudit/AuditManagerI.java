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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.AuditManagerI.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.gob.signaturereport.maudit.item.AuditField;
import es.gob.signaturereport.maudit.item.AuditOperation;
import es.gob.signaturereport.maudit.item.AuditService;
import es.gob.signaturereport.maudit.item.AuditTransaction;
import es.gob.signaturereport.maudit.log.traces.TraceI;

/**
 * <p>Interface that provides all methods and constants used to managing the audit message.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public interface AuditManagerI extends Serializable {	/* Services identifier*/

	/**
	 * Attribute that represents the identifier of generation report service.
	 */
	int GENERATION_REPORT_SRVC = 0;

	/**
	 * Attribute that represents the identifier of validation report service.
	 */
	int VALIDATION_REPORT_SRVC = 1;

	/* Actions*/

	/**
	 * Attribute that represents the beginning of a event transaction.
	 */
	int OPEN_TRANSACTION_ACT = 0;

	/**
	 * Attribute that represents the end of a event transaction.
	 */
	int CLOSE_TRANSACTION_ACT = 1;

	/**
	 * Attribute that represents the identifier of start generation report action.
	 */
	int START_GENERATION_REPORT_ACT = 2;

	/**
	 * Attribute that represents the identifier of end generation report action.
	 */
	int END_GENERATION_REPORT_ACT = 3;

	/**
	 * Attribute that represents the identifier of store web service request action.
	 */
	int STORE_REQUEST_ACT = 4;
	/**
	 * Attribute that represents the identifier of store web service response action.
	 */
	int STORE_RESPONSE_ACT = 5;

	/**
	 * Attribute that represents the identifier of start validation report action.
	 */
	int START_VALIDATION_REPORT_ACT = 6;

	/**
	 * Attribute that represents the identifier of end validation report action.
	 */
	int END_VALIDATION_REPORT_ACT = 7;

	/**
	 * Attribute that represents the identifier getting template information action.
	 */
	int TEMPLATE_INFORMATION_ACT = 8;
	
	/* Service - Operations */

	/**
	 * Attribute that represents the order operations associated to the generate report service. 
	 */
	int[] GENERATION_REPORT_OP  ={OPEN_TRANSACTION_ACT,STORE_REQUEST_ACT,START_GENERATION_REPORT_ACT,TEMPLATE_INFORMATION_ACT,END_GENERATION_REPORT_ACT,STORE_RESPONSE_ACT,CLOSE_TRANSACTION_ACT }; 
	
	/**
	 * Attribute that represents the order operations associated to the validate report service. 
	 */
	int[] VALIDATION_REPORT_OP  ={OPEN_TRANSACTION_ACT,STORE_REQUEST_ACT,START_VALIDATION_REPORT_ACT,END_VALIDATION_REPORT_ACT,STORE_RESPONSE_ACT,CLOSE_TRANSACTION_ACT }; 

	/* Custody Type*/

	/**
	 * Attribute that represents custody type blob.
	 */
	String BLOB_CUSTODY_TYPE = "BLOB";

	/**
	 * Attribute that represents custody type hash.
	 */
	String HASH_CUSTODY_TYPE = "HASH";

	/* CURRENT SOAP MESSAGE PROPERTY*/

	/**
	 * Attribute that represents the property use to store sequence identifier in the context.
	 */
	String SEQUENCE_ID_CONTEXT = "sequenceId";

//	/**
//	 * Gets the value of the identifier of current log event.
//	 * @return the value of the identifier of current log event.
//	 */
//	long getEventId();
//
//	/**
//	 * Sets the value of the identifier of current log event.
//	 * @param eventId The value for the identifier of current log event.
//	 */
//	void setEventId(long eventId);

	/**
	 * Method which reports whether the request is stored.
	 * @return	True if the request is stored, otherwise false.
	 */
	boolean isCustodyRequest();

	/**
	 * Method which reports whether the response is stored.
	 * @return	True if the response is stored, otherwise false.
	 */
	boolean isCustodyResponse();

	/**
	 * Store a SOAP Request Message.
	 * @param soap SOAP Message.
	 * @param	transactionId	Event transaction identifier.
	 * @return Custody identifier.
	 * @throws AuditException if an error occurs.
	 */
	String storeRequestMessage(byte[ ] soap, long transactionId) throws AuditException;

	/**
	 * Store a SOAP Response Message.
	 * @param soap SOAP Message.
	 * @param	transactionId	Event transaction identifier.
	 * @return Custody identifier.
	 * @throws AuditException if an error occurs.
	 */
	String storeResponseMessage(byte[ ] soap, long transactionId) throws AuditException;


	/**
	 * Open an event transaction.
	 * @param serviceId		Service identifier.
	 * @param messageHash Message hash.
	 * @return	Event identifier.
	 */
	long openTransaction(int serviceId, String messageHash);

	/**
	 * Add trace to the current transaction.
	 * @param trace	Trace information.
	 */
	void addTrace(TraceI trace);

	/**
	 * Close the event transaction associated to supplied identifier.
	 * @param sequenceId		Sequence identifier.
	 * @param serviceId		Service identifier.
	 * @param messageHash Message hash.
	 */
	void closeTransaction(long sequenceId, int serviceId, String messageHash);

	/**
	 * Gets a transaction list that fit to the supplied values.
	 * @param beginingTime	Beginning time. Required.
	 * @param endingDate	Ending time.	May be null.
	 * @param service		Service identifier.	May be null.
	 * @param application	Application identifier. May be null.
	 * @param firstResult	Position of first returned record. May be null.
	 * @param maxResults	Maximum number of records returned.
	 * @return	List of transaction that fit to the supplied values.
	 * @throws AuditException	If an error occurs.
	 */
	List<AuditTransaction> getTransactions(Date beginingTime, Date endingDate, Integer service, String application, Integer firstResult, Integer maxResults) throws AuditException;

	/**
	 * Gets an audit transaction that associated to supplied identifier.
	 * @param transactionId		Audit transaction identifier.
	 * @return	A {@link AuditTransaction} object that contains the information associated to supplied identifier. Null if the transaction is not found.
	 * @throws AuditException	If an error occurs.
	 */
	AuditTransaction getTransaction(long transactionId) throws AuditException;

	/**
	 * Gets all services registered into the audit module.
	 * @return	Array of {@link AuditService} object that contains the identifier and description of a services registered into the audit module.
	 */
	AuditService[ ] getServices();
	
	/**
	 * Gets all operations registered into the audit module.
	 * @return	Array of {@link AuditOperation} object that contains the identifier and description of a operation registered into the audit module.
	 */
	AuditOperation[ ] getOperations();
	
	/**
	 * Gets all fields registered into the audit module.
	 * @return	Array of {@link AuditField} object that contains the identifier and description of a field registered into the audit module.
	 */
	AuditField[ ] getFields();
}
