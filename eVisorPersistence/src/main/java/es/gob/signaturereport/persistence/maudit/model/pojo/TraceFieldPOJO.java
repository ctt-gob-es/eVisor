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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.TraceField.java.</p>
 * <b>Description:</b><p> Class that represents a record of the T_TRACEFIELDS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>20/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 20/07/2011.
 */
package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>Class that represents a record of the T_TRACEFIELDS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/10/2011.
 */
@Entity
@Table(name = "T_TRACEFIELDS")
public class TraceFieldPOJO implements Serializable {
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 6320865620488459075L;

	/**
	 * Attribute that represents the trace-field identifier. 
	 */
	@Id
	@GeneratedValue(generator = "T_TRACEFIELD_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "T_TRACEFIELD_SEQ_GEN", sequenceName = "T_TRACEFIELD_SEQ",allocationSize=0)
    @Column(name = "T_TRACEFIELD_PK", unique = true, nullable = false, length = NumberConstants.INT_19)
	private Long traceFieldPk = null;
	
	/**
	 * Attribute that represents the trace associated to this trace-field. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "T_TRACE", nullable = false) 
	private TraceTransactionPOJO traceTransaction;
	
	/**
	 * Attribute that represents the field type. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FIELD", nullable = false) 
	private FieldPOJO field;
	
	/**
	 * Attribute that represents the value of the field. 
	 */
	@Column(name = "F_VALUE", length = NumberConstants.INT_4000)
	private String fieldValue;
	
	/**
	 * Attribute that represents the record creation time. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime;
	
	/**
	 * Attribute that represents the type of value field. 
	 */
	@Column(name = "F_VALUETYPE", length = NumberConstants.INT_255)
	private String fieldValueType;
	
	/**
	 * Gets the value of the trace-field identifier.
	 * @return the value of the trace-field identifier.
	 */
	public Long getTraceFieldPk() {
		return traceFieldPk;
	}
	
	/**
	 * Sets the value of the trace-field identifier.
	 * @param traceFieldId The value for the trace-field identifier.
	 */
	public void setTraceFieldPk(Long traceFieldId) {
		this.traceFieldPk = traceFieldId;
	}
	
	/**
	 * Gets the value of the trace associated to this field.
	 * @return the value of the trace associated to this field.
	 */
	public TraceTransactionPOJO getTraceTransaction() {
		return traceTransaction;
	}
	
	/**
	 * Sets the value of the trace associated to this field.
	 * @param traceTrans The value for the trace associated to this field.
	 */
	public void setTraceTransaction(TraceTransactionPOJO traceTrans) {
		this.traceTransaction = traceTrans;
	}


	
	/**
	 * Gets the value of the field type.
	 * @return the value of the field type.
	 */
	public FieldPOJO getField() {
		return field;
	}


	
	/**
	 * Sets the value of the field type.
	 * @param fieldType The value for the field type.
	 */
	public void setField(FieldPOJO fieldType) {
		this.field = fieldType;
	}


	
	/**
	 * Gets the value of the field value.
	 * @return the value of the field value.
	 */
	public String getFieldValue() {
		return fieldValue;
	}


	
	/**
	 * Sets the value of the field value.
	 * @param value The value for the field value.
	 */
	public void setFieldValue(String value) {
		this.fieldValue = value;
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
	 * Gets the value of the type of field value.
	 * @return the value of the type of field value.
	 */
	public String getFieldValueType() {
		return fieldValueType;
	}


	
	/**
	 * Sets the value of the type of field value.
	 * @param valueType The value for the type of field value.
	 */
	public void setFieldValueType(String valueType) {
		this.fieldValueType = valueType;
	}


	
	
}
