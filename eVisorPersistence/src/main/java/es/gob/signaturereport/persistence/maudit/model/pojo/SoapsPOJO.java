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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.SOAP.java.</p>
 * <b>Description:</b><p> Class that represents a record of the SOAP table.</p>
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
import javax.persistence.Id;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <p>Class that represents a record of the SOAPS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 20/07/2011.
 */
@Entity
@Table(name = "SOAPS")
public class SoapsPOJO implements Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -7203644933069108987L;
	
	/**
	 * Attribute that represents the value of SOAP_PK column. 
	 */
	@Id
    @Column(name = "SOAP_PK", unique = true, nullable = false, length = NumberConstants.INT_19)
	private long soapPk;
	
	/**
	 * Attribute that represents the value of SOAP_TYPE column. 
	 */
	@Column(name = "soap_type", length = NumberConstants.INT_1)
	private int soapType;
	
	/**
	 * Attribute that represents the value of S_TRANSACTION column. 
	 */
	@Column(name = "S_TRANSACTION", length = NumberConstants.INT_19)
	private long transaction;
	
	/**
	 * Attribute that represents the value of the HASH_ALG column. 
	 */
	@Column(name = "HASH_ALG", length = NumberConstants.INT_50)
	private String hashAlgorithm;
	
	/**
	 * Attribute that represents the value of the HASH_VAL column. 
	 */
	@Column(name = "HASH_VAL", length = NumberConstants.INT_50)
	private String hashValue;
	
	/**
	 * Attribute that represents the value of CREATIONTIME column. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime;

		
	/**
	 * Gets the value of the value of SOAP_PK column.
	 * @return the value of the value of SOAP_PK column.
	 */
	public long getSoapPk() {
		return soapPk;
	}

	
	/**
	 * Sets the value of the value of SOAP_PK column.
	 * @param id The value for the value of SOAP_PK column.
	 */
	public void setSoapPk(long id) {
		this.soapPk = id;
	}

	
	/**
	 * Gets the value of the value of SOAP_TYPE column.
	 * @return the value of the value of SOAP_TYPE column.
	 */
	public int getSoapType() {
		return soapType;
	}

	
	/**
	 * Sets the value of the value of SOAP_TYPE column.
	 * @param type The value for the value of SOAP_TYPE column.
	 */
	public void setSoapType(int type) {
		this.soapType = type;
	}

	
	/**
	 * Gets the value of the value of S_TRANSACTION column.
	 * @return the value of the value of S_TRANSACTION column.
	 */
	public long getTransaction() {
		return transaction;
	}

	
	/**
	 * Sets the value of the value of S_TRANSACTION column.
	 * @param transactionId The value for the value of S_TRANSACTION column.
	 */
	public void setTransaction(long transactionId) {
		this.transaction = transactionId;
	}

	
	/**
	 * Gets the value of the value of the HASH_ALG column.
	 * @return the value of the value of the HASH_ALG column.
	 */
	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	
	/**
	 * Sets the value of the value of the HASH_ALG column.
	 * @param algorithm The value for the value of the HASH_ALG column.
	 */
	public void setHashAlgorithm(String algorithm) {
		this.hashAlgorithm = algorithm;
	}

	
	/**
	 * Gets the value of the value of the HASH_VAL column.
	 * @return the value of the value of the HASH_VAL column.
	 */
	public String getHashValue() {
		return hashValue;
	}

	
	/**
	 * Sets the value of the value of the HASH_VAL column.
	 * @param hash The value for the value of the HASH_VAL column.
	 */
	public void setHashValue(String hash) {
		this.hashValue = hash;
	}

	
	/**
	 * Gets the value of the value of the CREATIONTIME column.
	 * @return the value of the value of the CREATIONTIME column.
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	
	/**
	 * Sets the value of the value of the CREATIONTIME column.
	 * @param time The value for the value of the CREATIONTIME column.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}

}
