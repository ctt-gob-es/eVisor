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
 * <b>File:</b><p>es.gob.signaturereport.maudit.item.AuditTransaction.java.</p>
 * <b>Description:</b><p>Class that represent an audit transaction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>29/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 29/07/2011.
 */
package es.gob.signaturereport.maudit.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.gob.signaturereport.maudit.log.traces.TraceI;


/** 
 * <p>Class that represent an audit transaction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 29/07/2011.
 */
public class AuditTransaction {
	
	/**
	 * Attribute that represents the service identifier. 
	 */
	private int serviceId = -1;
	
	/**
	 * Attribute that represents the identifier of file event that contains this transaction. 
	 */
	private Long eventLogId = null;
	
	/**
	 * Attribute that represents the audit transaction identifier. 
	 */
	private Long transactionId = null;
	
	/**
	 * Attribute that represents the transaction creation time. 
	 */
	private Date creationTime = null;
	
	/**
	 * Attribute that represents list of traces created. 
	 */
	private List<TraceI> traces = new ArrayList<TraceI>();

	/**
	 * Constructor method for the class AuditTransaction.java.
	 * @param id		Service identifier.
	 * @param eventId	Event file identifier.
	 * @param transId	Transaction identifier.
	 * @param time 	Creation time.
	 */
	public AuditTransaction(int id, Long eventId, Long transId, Date time) {
		super();
		this.serviceId = id;
		this.eventLogId = eventId;
		this.transactionId = transId;
		this.creationTime = time;
	}
	
	/**
	 * Gets the value of the service identifier.
	 * @return the value of the service identifier.
	 */
	public int getServiceId() {
		return serviceId;
	}

	
	/**
	 * Sets the value of the service identifier.
	 * @param id The value for the service identifier.
	 */
	public void setServiceId(int id) {
		this.serviceId = id;
	}

	
	/**
	 * Gets the value of the event file identifier.
	 * @return the value of the event file identifier.
	 */
	public Long getEventLogId() {
		return eventLogId;
	}

	
	/**
	 * Sets the value of the event file identifier.
	 * @param eventId The value for the event file identifier.
	 */
	public void setEventLogId(Long eventId) {
		this.eventLogId = eventId;
	}

	
	/**
	 * Gets the value of the transaction identifier.
	 * @return the value of the transaction identifier.
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	
	/**
	 * Sets the value of the transaction identifier.
	 * @param transId The value for the transaction identifier.
	 */
	public void setTransactionId(Long transId) {
		this.transactionId = transId;
	}

	
	/**
	 * Gets the traces of the transaction.
	 * @return the traces of the transaction.
	 */
	public List<TraceI> getTraces() {
		return traces;
	}

	
	/**
	 * Sets the traces of the transaction.
	 * @param auditTraces Traces of the transaction.
	 */
	public void setTraces(List<TraceI> auditTraces) {
		this.traces = auditTraces;
	}


	


	
	/**
	 * Gets the value of the creation time.
	 * @return the value of the creation time.
	 */
	public Date getCreationTime() {
		return creationTime;
	}


	
	/**
	 * Sets the value of the creation time.
	 * @param time The value for the creation time.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}
	
	
}
