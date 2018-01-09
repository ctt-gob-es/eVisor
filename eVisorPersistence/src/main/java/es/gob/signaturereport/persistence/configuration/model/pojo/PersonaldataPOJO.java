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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Personaldata.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the PERSONALDATA table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <p>Class that represents the representation of the <i>PERSONALDATA</i> database table 
 * as a Plain Old Java Object.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/05/2013.
 */
@Entity
@Table(name = "PERSONALDATA")
@NamedQueries({ 
	@NamedQuery(name = "findAllPersons", query = "SELECT pd FROM PersonaldataPOJO pd WHERE pd.endingtime is NULL ORDER BY pd.pdSurname"),
	@NamedQuery(name = "findPerson", query = "SELECT pd FROM PersonaldataPOJO pd WHERE pd.endingtime is NULL AND pd.pdName like :name AND pd.pdSurname like :surname AND pd.pdEmail like :email AND pd.pdPhone like :phone")
})
public class PersonaldataPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = 1732920433463563548L;
	/**
	 * Attribute that represents the PD_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_PERSONAL_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_PERSONAL_GEN", sequenceName = "SQ_PERSONAL",allocationSize=0)
    @Column(name = "PD_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long pdPk;
	/**
	 * Attribute that represents the PD_NAME column. 
	 */
	@Column(name = "PD_NAME")
	private String pdName;
	/**
	 * Attribute that represents the PD_SURNAME column. 
	 */
	@Column(name = "PD_SURNAME")
	private String pdSurname;
	/**
	 * Attribute that represents the PD_PHONE column. 
	 */
	@Column(name = "PD_PHONE")
	private BigDecimal pdPhone;
	/**
	 * Attribute that represents the PD_EMAIL column. 
	 */
	@Column(name = "PD_EMAIL")
	private String pdEmail;
	/**
	 * Attribute that represents the association to APPLICATION table. 
	 */
    @OneToMany(mappedBy = "personalData" , fetch = FetchType.LAZY)
    @Where(clause = "ENDINGTIME IS NULL")
	private Set<ApplicationPOJO> applications = new HashSet<ApplicationPOJO>(0);
	/**
	 * Attribute that represents the association to USER table. 
	 */
	@OneToMany(mappedBy = "personalData" , fetch = FetchType.EAGER)
	@Where(clause = "ENDINGTIME IS NULL")
	private Set<UsersPOJO> users = new HashSet<UsersPOJO>(0);
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
     * Gets the value of the attribute {@link #pdPk}.
     * @return the value of the attribute {@link #pdPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public Long getPdPk() {
    	// CHECKSTYLE:ON
		return pdPk;
	}

    /**
     * Sets the value of the attribute {@link #appPkParam}.
     * @param appPkParam The value for the attribute {@link #appPkParam}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPdPk(Long pdPkParam) {
		// CHECKSTYLE:ON
		this.pdPk = pdPkParam;
	}

	/**
     * Gets the value of the attribute {@link #pdName}.
     * @return the value of the attribute {@link #pdName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getPdName() {
		// CHECKSTYLE:ON
		return this.pdName;
	}

	/**
     * Sets the value of the attribute {@link #appPkParam}.
     * @param appPkParam The value for the attribute {@link #appPkParam}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPdName(String pdNameParam) {
		// CHECKSTYLE:ON
		pdName = pdNameParam;
	}

	/**
     * Gets the value of the attribute {@link #pdSurname}.
     * @return the value of the attribute {@link #pdSurname}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getPdSurname() {
		// CHECKSTYLE:ON
		return pdSurname;
	}

	/**
     * Sets the value of the attribute {@link #pdSurname}.
     * @param appPkParam The value for the attribute {@link #pdSurname}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPdSurname(String surnameParam) {
		// CHECKSTYLE:ON
		pdSurname = surnameParam;
	}

	/**
     * Gets the value of the attribute {@link #pdPhone}.
     * @return the value of the attribute {@link #pdPhone}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public BigDecimal getPdPhone() {
		// CHECKSTYLE:ON
		return pdPhone;
	}

	/**
     * Sets the value of the attribute {@link #pdPhone}.
     * @param appPkParam The value for the attribute {@link #pdPhone}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPdPhone(BigDecimal phoneNumberParam) {
		// CHECKSTYLE:ON
		pdPhone = phoneNumberParam;
	}

	/**
     * Gets the value of the attribute {@link #pdEmail}.
     * @return the value of the attribute {@link #pdEmail}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getPdEmail() {
		// CHECKSTYLE:ON
		return pdEmail;
	}

	/**
     * Sets the value of the attribute {@link #pdEmail}.
     * @param appPkParam The value for the attribute {@link #pdEmail}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPdEmail(String mail) {
		pdEmail = mail;
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
	 * Gets the associated applications.
	 * @return	Applications.
	 */
	public Set<ApplicationPOJO> getApplications() {
		return this.applications;
	}

	/**
	 * Sets the associated applications.
	 * @param apps	Applications.
	 */
	public void setApplications(Set<ApplicationPOJO> apps) {
		this.applications = apps;
	}

	/**
	 * Gets the associates users.
	 * @return	Users.
	 */
	public Set<UsersPOJO> getUsers() {
		return this.users;
	}

	/**
	 * Sets the associated users.
	 * @param users Users.	
	 */
	public void setUsers(Set<UsersPOJO> users) {
		this.users = users;
	}
}
