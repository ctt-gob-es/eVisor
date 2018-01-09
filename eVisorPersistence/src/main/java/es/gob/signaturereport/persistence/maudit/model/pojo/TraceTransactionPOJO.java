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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.TraceTransaction.java.</p>
 * <b>Description:</b><p> Class that represents a record of the T_TRACES table.</p>
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>Class that represents a record of the T_TRACES table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/10/2011.
 */
@Entity
@Table(name = "T_TRACES")
public class TraceTransactionPOJO implements Serializable {
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -8955408582641378321L;

	/**
	 * Attribute that represents the trace identifier. 
	 */
	@Id
	@GeneratedValue(generator = "T_TRACE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "T_TRACE_SEQ_GEN", sequenceName = "T_TRACE_SEQ",allocationSize=0)
    @Column(name = "T_TRACE_PK", unique = true, nullable = false, length = NumberConstants.INT_19)
	private Long tracePk;

	/**
	 * Attribute that represents transaction identifier associated to this trace. 
	 */
	@Column(name = "S_TRANSACTION", length = NumberConstants.INT_19)
	private Long transaction;
	
	/**
	 * Attribute that represents the action type associated to this trace. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACTIONTYPE", nullable = false) 
	private ActionTypePOJO actionType;
	
	/**
	 * Attribute that represents the record creation time . 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime;
	
	/**
	 * Attribute that represents the fields associated o this trace. 
	 */
	@OneToMany(mappedBy = "traceTransaction" , fetch = FetchType.EAGER)
	private Set<TraceFieldPOJO> traceFields = new HashSet<TraceFieldPOJO>();
	
	/**
	 * Constructor method for the class TraceTransaction.java. 
	 */
	public TraceTransactionPOJO() {
		super();
	}

	
	/**
	 * Gets the value of the trace identifier.
	 * @return the value of the trace identifier.
	 */
	public Long getTracePk() {
		return tracePk;
	}

	
	/**
	 * Sets the value of the trace identifier.
	 * @param traceId The value for the trace identifier.
	 */
	public void setTracePk(Long traceId) {
		this.tracePk = traceId;
	}

	
	/**
	 * Gets the value of the transaction identifier.
	 * @return the value of the transaction identifier.
	 */
	public Long getTransaction() {
		return transaction;
	}

	
	/**
	 * Sets the value of the transaction identifier.
	 * @param transactionId The value for the transaction identifier.
	 */
	public void setTransaction(Long transactionId) {
		this.transaction = transactionId;
	}

	
	/**
	 * Gets the value of the action type.
	 * @return the value of the action type.
	 */
	public ActionTypePOJO getActionType() {
		return actionType;
	}

	
	/**
	 * Sets the value of the action type.
	 * @param action The value for the action type.
	 */
	public void setActionType(ActionTypePOJO action) {
		this.actionType = action;
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

	
	/**
	 * Gets the value of the fields associated to this trace.
	 * @return the value of the fields associated to this trace.
	 */
	public Set<TraceFieldPOJO> getTraceFields() {
		return traceFields;
	}

	
	/**
	 * Sets the value of the fields associated to this trace.
	 * @param fields The value for the fields associated to this trace.
	 */
	public void setTraceFields(Set<TraceFieldPOJO> fields) {
		this.traceFields = fields;
	}

	
}
