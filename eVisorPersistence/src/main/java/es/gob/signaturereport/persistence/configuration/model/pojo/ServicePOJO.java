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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Service.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the SERVICE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * <p>This class represents a record contained into the SERVICE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Entity
@Table(name = "SERVICE")
@IdClass(ServiceId.class)
public class ServicePOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class.
	 */
	private static final long serialVersionUID = -2117444235609916211L;
	/**
	 * Attribute that represents the SERVICE_ID column. 
	 */
	@Id
	@Column(name = "SERVICE_ID", nullable = false)
	private String serviceId;

	/**
	 * Attribute that represents the association to PLATFORM table. 
	 */
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLATFORM", nullable = false)
	private PlatformPOJO platform;
	/**
	 * Attribute that represents S_LOCATION column.
	 */
	
	@Column(name = "S_LOCATION")
	private String SLocation;
	
	/**
	 * Attribute that represents S_OPERATION column.
	 */
	@Column(name = "S_OPERATION")
	private String SOperation;
	
	/**
	 * Attribute that represents S_TIMEOUT column.
	 */
	@Column(name = "S_TIMEOUT")
	private long STimeout;
	

	/**
     * Gets the value of the attribute {@link #serviceId}.
     * @return the value of the attribute {@link #serviceId}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public String getServiceId() {
		// CHECKSTYLE:ON
		return this.serviceId;
	}

	/**
     * Sets the value of the attribute {@link #serviceId}.
     * @return the value of the attribute {@link #serviceId}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setServiceId(String serviceId) {
		// CHECKSTYLE:ON
		this.serviceId = serviceId;
	}

	/**
     * Gets the value of the attribute {@link #platform}.
     * @return the value of the attribute {@link #platform}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public PlatformPOJO getPlatform() {
		// CHECKSTYLE:ON
		return this.platform;
	}

	/**
     * Sets the value of the attribute {@link #platform}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPlatform(PlatformPOJO platform) {
		// CHECKSTYLE:ON
		this.platform = platform;
	}

	/**
     * Gets the value of the attribute {@link #SLocation}.
     * @return the value of the attribute {@link #SLocation}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public String getSLocation() {
		// CHECKSTYLE:ON
		return this.SLocation;
	}

	/**
     * Sets the value of the attribute {@link #SLocation}.
     * @return the value of the attribute {@link #SLocation}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setSLocation(String location) {
		// CHECKSTYLE:ON
		this.SLocation = location;
	}

	/**
     * Gets the value of the attribute {@link #SOperation}.
     * @return the value of the attribute {@link #SOperation}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public String getSOperation() {
		return this.SOperation;
	}

	/**
     * Sets the value of the attribute {@link #SOperation}.
     * @return the value of the attribute {@link #SOperation}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setSOperation(String operation) {
		this.SOperation = operation;
	}

	/**
     * Gets the value of the attribute {@link #STimeout}.
     * @return the value of the attribute {@link #STimeout}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public long getSTimeout() {
		return this.STimeout;
	}

	/**
     * Sets the value of the attribute {@link #STimeout}.
     * @return the value of the attribute {@link #STimeout}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setSTimeout(long time) {
		this.STimeout = time;
	}

}
