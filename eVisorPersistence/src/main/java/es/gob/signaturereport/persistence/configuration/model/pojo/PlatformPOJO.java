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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Platform.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the PLATFORM table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
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
import javax.persistence.ManyToOne;
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
@Table(name = "PLATFORM")
@NamedQueries({
	  @NamedQuery(name="findPlatformById",
	              query="SELECT pf FROM PlatformPOJO pf WHERE pf.pfId = :pfId AND pf.endingtime is null"),
	  @NamedQuery(name="findAllActivePlatforms",
	              query="SELECT pf FROM PlatformPOJO pf WHERE pf.endingtime is null ORDER BY pf.pfId asc")
	})
public class PlatformPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -8522379048890937820L;
	
	/**
	 * Attribute that represents the PF_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_PLATFORM_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_PLATFORM_GEN", sequenceName = "SQ_PLATFORM",allocationSize=0)
    @Column(name = "PF_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long pfPk;
	/**
	 * Attribute that represents the association to CERTIFICATE table by AUTH_CERT column. 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTH_CERT", nullable = false)
	private CertificatePOJO certificateByAuthCert;
	/**
	 * Attribute that represents the association to CERTIFICATE table by SOAPTRUSTED column. 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOAPTRUSTED", nullable = false)
	private CertificatePOJO certificateBySoaptrusted;
	/**
	 * Attribute that represents the PF_ID column. 
	 */
	@Column(name = "PF_ID")
	private String pfId;
	/**
	 * Attribute that represents the PF_APP column. 
	 */
	 @Column(name = "PF_APP")
	private String pfApp;
	/**
	 * Attribute that represents the PF_VERSION column. 
	 */
	 @Column(name = "PF_VERSION")
	private String pfVersion;
	/**
	 * Attribute that represents the CREATIONTIME column. 
	 */
	 @Column(name = "CREATIONTIME")
	private Date creationtime;
	/**
	 * Attribute that represents the AUTH_TYPE column. 
	 */
	 @Column(name = "AUTH_TYPE", precision = NumberConstants.INT_2)
	private int authType;
	/**
	 * Attribute that represents the AUTH_USER column. 
	 */
	@Column(name = "AUTH_USER")
	private String authUser;
	/**
	 * Attribute that represents the AUTH_PASS column. 
	 */
	@Column(name = "AUTH_PASS")
	private String authPass;
	/**
	 * Attribute that represents the ENDINGTIME column. 
	 */
	@Column(name = "ENDINGTIME")
	private Date endingtime;
	/**
	 * Attribute that represents the association to SERVICES table. 
	 */
	@OneToMany(mappedBy = "platform" , fetch = FetchType.EAGER)
	private Set<ServicePOJO> services = new HashSet<ServicePOJO>(0);
	/**
	 * Attribute that represents the association to application table. 
	 */
	@OneToMany(mappedBy = "platform" , fetch = FetchType.EAGER)
	private Set<ApplicationPOJO> applications = new HashSet<ApplicationPOJO>(0);

	/**
	 * Constructor method for the class Platform.java. 
	 */
	public PlatformPOJO() {
	}


	/**
     * Gets the value of the attribute {@link #pfPk}.
     * @return the value of the attribute {@link #pfPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public Long getPfPk() {
    	// CHECKSTYLE:ON
		return this.pfPk;
	}

    /**
     * Sets the value of the attribute {@link #pfPk}.
     * @param appPkParam The value for the attribute {@link #pfPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPfPk(Long pk) {
		// CHECKSTYLE:ON
		this.pfPk = pk;
	}

	/**
     * Gets the value of the attribute {@link #certificateByAuthCert}.
     * @return the value of the attribute {@link #certificateByAuthCert}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
   
	public CertificatePOJO getCertificateByAuthCert() {
    	// CHECKSTYLE:ON
		return this.certificateByAuthCert;
	}

    /**
     * Sets the value of the attribute {@link #certificateByAuthCert}.
     * @param The value for the attribute {@link #certificateByAuthCert}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setCertificateByAuthCert(CertificatePOJO certificate) {
		// CHECKSTYLE:ON
		this.certificateByAuthCert = certificate;
	}

	/**
     * Gets the value of the attribute {@link #certificateBySoaptrusted}.
     * @return the value of the attribute {@link #certificateBySoaptrusted}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public CertificatePOJO getCertificateBySoaptrusted() {
    	// CHECKSTYLE:ON
		return this.certificateBySoaptrusted;
	}

    /**
     * Sets the value of the attribute {@link #certificateBySoaptrusted}.
     * @param The value for the attribute {@link #certificateBySoaptrusted}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setCertificateBySoaptrusted(CertificatePOJO certificate) {
		// CHECKSTYLE:ON
		this.certificateBySoaptrusted = certificate;
	}

	/**
     * Gets the value of the attribute {@link #pfId}.
     * @return the value of the attribute {@link #pfId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
   
	public String getPfId() {
    	// CHECKSTYLE:ON
		return this.pfId;
	}

    /**
     * Sets the value of the attribute {@link #pfId}.
     * @param The value for the attribute {@link #pfId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPfId(String platformId) {
		// CHECKSTYLE:ON
		this.pfId = platformId;
	}

	/**
     * Gets the value of the attribute {@link #pfApp}.
     * @return the value of the attribute {@link #pfApp}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
   
	public String getPfApp() {
    	// CHECKSTYLE:ON
		return this.pfApp;
	}

    /**
     * Sets the value of the attribute {@link #pfApp}.
     * @param The value for the attribute {@link #pfApp}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPfApp(String application) {
		// CHECKSTYLE:ON
		this.pfApp = application;
	}

	/**
     * Gets the value of the attribute {@link #pfVersion}.
     * @return the value of the attribute {@link #pfVersion}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public String getPfVersion() {
    	// CHECKSTYLE:ON
		return this.pfVersion;
	}

    /**
     * Sets the value of the attribute {@link #pfVersion}.
     * @param The value for the attribute {@link #pfVersion}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPfVersion(String version) {
		// CHECKSTYLE:ON
		this.pfVersion = version;
	}


	/**
     * Gets the value of the attribute {@link #authType}.
     * @return the value of the attribute {@link #authType}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
    public int getAuthType() {
		// CHECKSTYLE:ON
		return authType;
    }

    /**
     * Sets the value of the attribute {@link #authType}.
     * @param The value for the attribute {@link #authType}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAuthType(int authTypeParam) {
		// CHECKSTYLE:ON
		this.authType = authTypeParam;
    }

    /**
     * Gets the value of the attribute {@link #authUser}.
     * @return the value of the attribute {@link #authUser}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
    public String getAuthUser() {
		// CHECKSTYLE:ON
		return authUser;
    }

    /**
     * Sets the value of the attribute {@link #authUser}.
     * @param The value for the attribute {@link #authUser}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAuthUser(String authUserParam) {
		// CHECKSTYLE:ON
		this.authUser = authUserParam;
    }

    /**
     * Gets the value of the attribute {@link #authPass}.
     * @return the value of the attribute {@link #authPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
    public String getAuthPass() {
		// CHECKSTYLE:ON
		return authPass;
    }

    /**
     * Sets the value of the attribute {@link #authPass}.
     * @param The value for the attribute {@link #authPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAuthPass(String authPassParam) {
		// CHECKSTYLE:ON
		this.authPass = authPassParam;
    }

	/**
	 * Gets the associated services.
	 * @return	Associated services.
	 */
    
	public Set<ServicePOJO> getServices() {
		return this.services;
	}

	/**
	 * Sets the associated services.
	 * @param platformServices Associated services.
	 */
	public void setServices(Set<ServicePOJO> platformServices) {
		this.services = platformServices;
	}

	/**
	 * Gets the associated applications.
	 * @return	Associated applications.
	 */
	
	public Set<ApplicationPOJO> getApplications() {
		return applications;
	}

	/**
	 * Sets the associated applications.
	 * @param apps 	Associated applications.
	 */
	public void setApplications(Set<ApplicationPOJO> apps) {
		this.applications = apps;
	}

	/**
	 * Add a service to the platform.
	 * @param service	Platform service.
	 */
	public void addService(ServicePOJO service){
		if(this.services==null){
			this.services = new HashSet<ServicePOJO>();
		}
			this.services.add(service);
	}


	 	/**
	     * Gets the value of the attribute {@link #creationtime}.
	     * @return the value of the attribute {@link #creationtime}.
	     */
	    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	    // because Hibernate JPA needs not final access methods.
	    @Column(name = "CREATIONTIME")
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
