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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.Transaction.java.</p>
 * <b>Description:</b><p>Class that represents a record of the S_TRANSACTIONS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>20/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 20/07/2011.
 */
package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <p>Class that represents a record of the S_TRANSACTIONS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/10/2011.
 */
@Entity
@Table(name = "S_TRANSACTIONS")
public class TransactionPOJO implements Serializable {
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 4163770445432111666L;

	/**
	 * Attribute that represents the transaction identifier. 
	 */
	@Id
    @Column(name = "S_TRANSACTION_PK", unique = true, nullable = false, length = NumberConstants.INT_19)
	private Long transactionPk;
	
	/**
	 * Attribute that represents the service invoked in this transaction. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICE", nullable = false) 
	private ServicesPOJO service;
	
	/**
	 * Attribute that represents the record creation time. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime;
	
	/**
	 * Attribute that represents the archive event identifier. 
	 */
	@Column(name = "EVENTLOG")
	private Long eventId;
	
	/**
	 * Attribute that represents the traces associated to this transaction. 
	 */
	@OneToMany(mappedBy = "transaction" , fetch = FetchType.EAGER)
	private Set<TraceTransactionPOJO> traces = new HashSet<TraceTransactionPOJO>();

		
	/**
	 * Gets the value of the transaction identifier.
	 * @return the value of the transaction identifier.
	 */
	public Long getTransactionPk() {
		return transactionPk;
	}

	
	/**
	 * Sets the value of the transaction identifier.
	 * @param transactionId The value for the transaction identifier.
	 */
	public void setTransactionPk(Long transactionId) {
		this.transactionPk = transactionId;
	}

	
	/**
	 * Gets the value of the service.
	 * @return the value of the service.
	 */
	public ServicesPOJO getService() {
		return service;
	}

	
	/**
	 * Sets the value of the service.
	 * @param auditService The value for the service.
	 */
	public void setService(ServicesPOJO auditService) {
		this.service = auditService;
	}

	
	/**
	 * Gets the value of the record creation time.
	 * @return the value of the record creation time.
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	
	/**
	 * Sets the value of the record creation time.
	 * @param time The value for the record creation time.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}

	
	/**
	 * Gets the value of the event identifier.
	 * @return the value of the event identifier.
	 */
	public Long getEventId() {
		return eventId;
	}

	
	/**
	 * Sets the value of the event identifier.
	 * @param id The value for the event identifier.
	 */
	public void setEventId(Long id) {
		this.eventId = id;
	}

	
	/**
	 * Gets the value of the associated traces.
	 * @return the value of the associated traces.
	 */
	public Set<TraceTransactionPOJO> getTraces() {
		return traces;
	}

	
	/**
	 * Sets the value of the associated traces.
	 * @param tranTraces The value for the associated traces.
	 */
	public void setTraces(Set<TraceTransactionPOJO> tranTraces) {
		this.traces = tranTraces;
	}
	
	
}
