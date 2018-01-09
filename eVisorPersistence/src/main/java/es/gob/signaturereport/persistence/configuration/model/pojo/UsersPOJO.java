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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Users.java.</p>
 * <b>Description:</b><p> Class that represents a record of the "USERS" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/06/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>Class that represents a record of the "USERS" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/06/2011.
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
	  @NamedQuery(name="findUserByLogin",
	              query="SELECT us FROM UsersPOJO us WHERE us.userLogin = :login AND us.endingtime is null"),
	  @NamedQuery(name = "findAllUsers", query = "SELECT us FROM UsersPOJO us WHERE us.endingtime is NULL ORDER BY us.userLogin")
	})
public class UsersPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = 1520787242947091612L;
	/**
	 * Attribute that represents the USER_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_USERS_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_USERS_GEN", sequenceName = "SQ_USERS",allocationSize=0)
    @Column(name = "USER_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long userPk;
	/**
	 * Attribute that represents the association to PERSONALDATA table. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_DATA", nullable = false)
	private PersonaldataPOJO personalData;
	/**
	 * Attribute that represents the USER_LOGIN column. 
	 */
	@Column(name = "USER_LOGIN")
	private String userLogin;
	/**
	 * Attribute that represents the USER_PASS column. 
	 */
	@Column(name = "USER_PASS")
	private String userPass;
	/**
	 * Attribute that represents the USER_LOCKED column. 
	 */
	@Column(name = "USER_LOCKED")
	@Type(type = Constants.CONS_YES_NO)
	private boolean userLocked;
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
     * Gets the value of the attribute {@link #userPk}.
     * @return the value of the attribute {@link #userPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public Long getUserPk() {
    	// CHECKSTYLE:ON
		return this.userPk;
	}

    /**
     * Sets the value of the attribute {@link #userPk}.
     * @return the value of the attribute {@link #userPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUserPk(Long pk) {
		// CHECKSTYLE:ON
		this.userPk = pk;
	}

	/**
     * Gets the value of the attribute {@link #personalData}.
     * @return the value of the attribute {@link #personalData}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public PersonaldataPOJO getPersonalData() {
    	// CHECKSTYLE:ON
		return this.personalData;
	}

    /**
     * Sets the value of the attribute {@link #personalData}.
     * @return the value of the attribute {@link #personalData}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPersonalData(PersonaldataPOJO data) {
		// CHECKSTYLE:ON
		this.personalData = data;
	}

	/**
     * Gets the value of the attribute {@link #userLogin}.
     * @return the value of the attribute {@link #userLogin}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public String getUserLogin() {
    	// CHECKSTYLE:ON
		return this.userLogin;
	}

    /**
     * Sets the value of the attribute {@link #userLogin}.
     * @return the value of the attribute {@link #userLogin}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUserLogin(String login) {
		// CHECKSTYLE:ON
		this.userLogin = login;
	}

	/**
     * Gets the value of the attribute {@link #userPass}.
     * @return the value of the attribute {@link #userPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public String getUserPass() {
    	// CHECKSTYLE:ON
		return this.userPass;
	}

    /**
     * Sets the value of the attribute {@link #userPass}.
     * @return the value of the attribute {@link #userPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUserPass(String password) {
		// CHECKSTYLE:ON
		this.userPass = password;
	}

	/**
     * Gets the value of the attribute {@link #userPass}.
     * @return the value of the attribute {@link #userPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public boolean getUserLocked() {
    	// CHECKSTYLE:ON
		return this.userLocked;
	}

    /**
     * Gets the value of the attribute {@link #userPass}.
     * @return the value of the attribute {@link #userPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setUserLocked(boolean isLocked) {
		// CHECKSTYLE:ON
		this.userLocked = isLocked;
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

}
