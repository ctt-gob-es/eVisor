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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Uo.java.</p>
 * <b>Description:</b><p> Class that represents a record of the "UO" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/06/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;


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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>This class represents a record contained into the PLATFORM table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/04/2011.
 */
@Entity
@Table(name = "UO")
@NamedQueries({
 @NamedQuery(name = "findUosById", query = "SELECT uo FROM UoPOJO uo WHERE uo.uoId = :uoId AND uo.endingtime is null") })
public class UoPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents serial version of the class. 
	 */
	private static final long serialVersionUID = 8158749563841165542L;

	/**
	 * Attribute that represents the UO_PK column. 
	 */
	@Id
	@GeneratedValue(generator = "SQ_UO_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SQ_UO_GEN", sequenceName = "SQ_UO",allocationSize=0)
	@Column(name = "UO_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long uoPk;

	/**
	 * Attribute that represents the UO_ID column. 
	 */
	@Column(name = "UO_ID")
	private String uoId;
	/**
	 * Attribute that represents the UO_NAME column. 
	 */
	@Column(name = "UO_NAME")
	private String uoName;
	/**
	 * Attribute that represents the CREATIONTIME column. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationtime;
	/**
	 * Attribute that represents the ENDINGTIME column. 
	 */
	@Column(name = "ENDINGTIME")
	private Date endingtime;
	/**
	 * Attribute that represents the association to organizational units contained into this. 
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UOREL", joinColumns = { @JoinColumn(name = "PARENT_UO", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "UO", nullable = false) })
	private Set<UoPOJO> uosForUo = new HashSet<UoPOJO>(0);
	/**
	 * Attribute that represents the association to organizational unit that contains this. 
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UOREL", joinColumns = { @JoinColumn(name = "UO", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "PARENT_UO", nullable = false) })
	private Set<UoPOJO> uosForParentUo = new HashSet<UoPOJO>(0);
	/**
	 * Attribute that represents the association to applications contained into this organizational unit. 
	 */
	@OneToMany(mappedBy = "uo" , fetch = FetchType.EAGER)
	private Set<ApplicationPOJO> applications = new HashSet<ApplicationPOJO>(0);


	/**
     * Gets the value of the attribute {@link #uoPk}.
     * @return the value of the attribute {@link #uoPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public Long getUoPk() {
    	// CHECKSTYLE:ON
		return this.uoPk;
	}

    /**
     * Sets the value of the attribute {@link #uoPk}.
     * @param appPkParam The value for the attribute {@link #uoPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUoPk(Long pk) {
		// CHECKSTYLE:ON
		this.uoPk = pk;
	}

	/**
     * Gets the value of the attribute {@link #uoId}.
     * @return the value of the attribute {@link #uoId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

	public String getUoId() {
    	// CHECKSTYLE:ON
		return this.uoId;
	}

    /**
     * Sets the value of the attribute {@link #uoId}.
     * @param The value for the attribute {@link #uoId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUoId(String id) {
		// CHECKSTYLE:ON
		this.uoId = id;
	}

	/**
     * Gets the value of the attribute {@link #uoName}.
     * @return the value of the attribute {@link #uoName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

	public String getUoName() {
    	// CHECKSTYLE:ON
		return this.uoName;
	}

    /**
     * Sets the value of the attribute {@link #uoName}.
     * @param The value for the attribute {@link #uoName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUoName(String name) {
		// CHECKSTYLE:ON
		this.uoName = name;
	}

	/**
     * Gets the value of the attribute {@link #creationtime}.
     * @return the value of the attribute {@link #creationtime}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

    public Date getCreationtime() {
		// CHECKSTYLE:ON
		return creationtime;
    }

    /**
     * Sets the value of the attribute {@link #creationtime}.
     * @param The value for the attribute {@link #creationtime}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setCreationtime(Date creationtimeParam) {
		// CHECKSTYLE:ON
		this.creationtime = creationtimeParam;
    }

    /**
     * Gets the value of the attribute {@link #endingtime}.
     * @return the value of the attribute {@link #endingtime}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

    public Date getEndingtime() {
		// CHECKSTYLE:ON
		return endingtime;
    }

    /**
     * Sets the value of the attribute {@link #endingtime}.
     * @param The value for the attribute {@link #endingtime}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setEndingtime(Date endingtimeParam) {
		// CHECKSTYLE:ON
		this.endingtime = endingtimeParam;
    }

	/**
	 * Gets the association to organizational units contained into this.
	 * @return	The association to organizational units contained into this.
	 */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.

	public Set<UoPOJO> getUosForUo() {
		// CHECKSTYLE:ON
		return this.uosForUo;
	}

	/**
	 * Sets the association to organizational units contained into this.
	 * @param uos	The association to organizational units contained into this.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setUosForUo(Set<UoPOJO> uos) {
		// CHECKSTYLE:ON
		this.uosForUo = uos;
	}

	/**
	 * Gets the association to organizational unit that contains this.
	 * @return	The association to organizational unit that contains this.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.

	public Set<UoPOJO> getUosForParentUo() {
		// CHECKSTYLE:ON
		return this.uosForParentUo;
	}

	/**
	 * Sets the association to organizational unit that contains this.
	 * @param uos The association to organizational unit that contains this.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
		// because Hibernate JPA needs not final access methods.
	public void setUosForParentUo(Set<UoPOJO> uos) {
		// CHECKSTYLE:ON
		this.uosForParentUo = uos;
	}

	/**
	 * Gets the association to applications contained into this organizational unit. 
	 * @return	The association to applications contained into this organizational unit. 
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.

	public Set<ApplicationPOJO> getApplications() {
		return this.applications;
	}

	/**
	 * Sets the association to applications contained into this organizational unit. 
	 * @param apps The association to applications contained into this organizational unit. 
	 */
	public void setApplications(Set<ApplicationPOJO> apps) {
		this.applications = apps;
	}
	

}
