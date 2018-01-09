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


package es.gob.signaturereport.persistence.configuration.model.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;



/** 
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
@Entity
@Table(name = "APPLICATION")
@NamedQueries({ @NamedQuery(name = "findAppById", query = "SELECT app FROM ApplicationPOJO app WHERE app.appId = :appId AND app.endingtime is null") })
public class ApplicationPOJO implements Serializable {

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = -507652084612721291L;

    /**
     * Attribute that represents the object PK.
     */
    @Id
    @GeneratedValue(generator = "SQ_APPLICATION_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_APPLICATION_GEN", sequenceName = "SQ_APPLICATION",allocationSize=0)
    @Column(name = "APP_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
    private Long appPk;

    /**
     * Attribute that represents the key to the person in charge of the application.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APP_RESP", nullable = false) 
    private PersonaldataPOJO personalData;

    /**
     * Attribute that represents the organizational unit to which the application belongs.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UO", nullable = false)
    private UoPOJO uo;

    /**
     * Attribute that represents the authentication certificate for web services in case of this option is enabled.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTH_CERT", nullable = false)
    private CertificatePOJO certificate;

    /**
     * Attribute that represents the @firma platform used.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLATFORM", nullable = false)
    private PlatformPOJO platform;

    /**
     * Attribute that represents the application identifier.
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * Attribute that represents the application name.
     */
    @Column(name = "APP_NAME")
    private String appName;

    /**
     * Attribute that represents the creation date of the application.
     */
    @Column(name = "CREATIONTIME")
    private Date creationtime;

    /**
     * Attribute that represents the ending date of the application.
     */
    @Column(name = "ENDINGTIME")
    private Date endingtime;

    /**
     * Attribute that represents the authentication type of the web services: user/password or certificate.
     */
    @Column(name = "AUTH_TYPE", precision = NumberConstants.INT_2)
    private Integer authType;

    /**
     * Attribute that represents the username in the web service authorization using user/password.
     */
    @Column(name = "AUTH_USER")
    private String authUser;

    /**
     * Attribute that represents the password in the web service authorization using user/password.
     */
    @Column(name = "AUTH_PASS")    
    private String authPass;

    /**
	 * Attribute that represents the association to TEMPLATEREPORT table. 
	 */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
	           name="APPTEMPLATE",
	           joinColumns=@JoinColumn(name="APP", referencedColumnName="APP_PK", nullable = false), 
	           inverseJoinColumns=@JoinColumn(name="T_REPORT", referencedColumnName="TR_PK", nullable = false))
	private Set<TemplateReportPOJO> templatereports;

    /**
     * Gets the value of the attribute {@link #appPk}.
     * @return the value of the attribute {@link #appPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public Long getAppPk() {
		// CHECKSTYLE:ON
		return appPk;
    }

    /**
     * Sets the value of the attribute {@link #appPkParam}.
     * @param appPkParam The value for the attribute {@link #appPkParam}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAppPk(Long appPkParam) {
	// CHECKSTYLE:ON
	this.appPk = appPkParam;
    }

    /**
     * Gets the value of the attribute {@link #personalData}.
     * @return the value of the attribute {@link #personalData}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

	public PersonaldataPOJO getPersonalData() {
		// CHECKSTYLE:ON
    	return personalData;
	}

    /**
     * Sets the value of the attribute {@link #personalData}.
     * @param The value for the attribute {@link #personalData}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPersonalData(PersonaldataPOJO personalData) {
		// CHECKSTYLE:ON
		this.personalData = personalData;
	}

	/**
     * Gets the value of the attribute {@link #uo}.
     * @return the value of the attribute {@link #uo}.
     */

    public UoPOJO getUo() {
    	// CHECKSTYLE:ON
    	return uo;
    }

    /**
     * Sets the value of the attribute {@link #uo}.
     * @param The value for the attribute {@link #uo}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setUo(UoPOJO uoParam) {
		// CHECKSTYLE:ON
		this.uo = uoParam;
    }

    /**
     * Gets the value of the attribute {@link #certificate}.
     * @return the value of the attribute {@link #certificate}.
     */

    public CertificatePOJO getCertificate() {
    	// CHECKSTYLE:ON
    	return certificate;
    }

    /**
     * Sets the value of the attribute {@link #certificate}.
     * @param The value for the attribute {@link #certificate}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setCertificate(CertificatePOJO certificateParam) {
		// CHECKSTYLE:ON
		this.certificate = certificateParam;
    }

    /**
     * Gets the value of the attribute {@link #platform}.
     * @return the value of the attribute {@link #platform}.
     */

    public PlatformPOJO getPlatform() {
    	// CHECKSTYLE:ON
    	return platform;
    }

    /**
     * Sets the value of the attribute {@link #platform}.
     * @param The value for the attribute {@link #platform}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setPlatform(PlatformPOJO platformParam) {
		// CHECKSTYLE:ON
		this.platform = platformParam;
    }

    /**
     * Gets the value of the attribute {@link #appId}.
     * @return the value of the attribute {@link #appId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public String getAppId() {
    	// CHECKSTYLE:ON
    	return appId;
    }

    /**
     * Sets the value of the attribute {@link #appId}.
     * @param appIdParam The value for the attribute {@link #appId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAppId(String appIdParam) {
		// CHECKSTYLE:ON
		this.appId = appIdParam;
    }

    /**
     * Gets the value of the attribute {@link #appName}.
     * @return the value of the attribute {@link #appName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public String getAppName() {
    	// CHECKSTYLE:ON
    	return appName;
    }

    /**
     * Sets the value of the attribute {@link #appName}.
     * @param appNameParam The value for the attribute {@link #appName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAppName(String appNameParam) {
		// CHECKSTYLE:ON
		this.appName = appNameParam;
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
     * @param creationtimeParam The value for the attribute {@link #creationtime}.
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
     * @param endingtimeParam The value for the attribute {@link #endingtime}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setEndingtime(Date endingtimeParam) {
		// CHECKSTYLE:ON
		this.endingtime = endingtimeParam;
    }

    /**
     * Gets the value of the attribute {@link #authType}.
     * @return the value of the attribute {@link #authType}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public Integer getAuthType() {
		// CHECKSTYLE:ON
		return authType;
    }

    /**
     * Sets the value of the attribute {@link #authType}.
     * @param authTypeParam The value for the attribute {@link #authType}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAuthType(Integer authTypeParam) {
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
     * @param authUserParam The value for the attribute {@link #authUser}.
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
     * @param authPassParam The value for the attribute {@link #authPass}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setAuthPass(String authPassParam) {
		// CHECKSTYLE:ON
		this.authPass = authPassParam;
    }

    /**
	 * Gets the value of the attribute {@link #templatereports}.
	 * @return the value of the attribute {@link #templatereports}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public Set<TemplateReportPOJO> getTemplatereports() {
		// CHECKSTYLE:ON
		return templatereports;
	}

	/**
	 * Sets the value of the attribute {@link #templatereports}.
	 * @param templatereportsList The value for the attribute {@link #templatereports}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setTemplatereports(Set<TemplateReportPOJO> templatereportsList)
	{
		templatereports = templatereportsList;
	}

	/**
	 * Adds a template to the template list of this application.
	 * @param template	Template.
	 */
	public void addTemplate(TemplateReportPOJO template){
		if (template != null){
			if (templatereports == null) {
				templatereports = new HashSet<TemplateReportPOJO>();
			}
			this.templatereports.add(template);
		}
	}

    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
	// CHECKSTYLE:ON
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	ApplicationPOJO other = (ApplicationPOJO) obj;
	if (appPk == null) {
	    if (other.appPk != null) {
		return false;
	    }
	} else if (!appPk.equals(other.appPk)) {
	    return false;
	}
	return true;
    }

}
