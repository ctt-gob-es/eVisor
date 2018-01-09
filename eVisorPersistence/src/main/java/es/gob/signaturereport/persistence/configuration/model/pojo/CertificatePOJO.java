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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Certificate.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the CERTIFICATE table.</p>
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>This class represents a record contained into the CERTIFICATE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/04/2011.
 */
@Entity
@Table(name = "CERTIFICATE")
@NamedQueries({
	  @NamedQuery(name="findCertyId",
	              query="SELECT cer.certPk FROM CertificatePOJO cer, KeystorePOJO ks WHERE cer.certId LIKE :cert_alias AND cer.endingtime IS NULL AND ks.ksPk = cer.keystore.ksPk AND ks.ksName LIKE :keystoreId"),
	  @NamedQuery(name="findCertById",
	              query="SELECT cer FROM CertificatePOJO cer, KeystorePOJO ks WHERE cer.certId LIKE :cert_alias AND cer.endingtime IS NULL AND ks.ksPk = cer.keystore.ksPk AND ks.ksName LIKE :keystoreId")
	  })

public class CertificatePOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = 8374604941817065850L;

	/**
	 * Attribute that represents the CERT_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_CERTIFICATE_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_CERTIFICATE_GEN", sequenceName = "SQ_CERTIFICATE",allocationSize=0)
    @Column(name = "CERT_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long certPk;
	/**
	 * Attribute that represents the association to KEYSTORE table. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KEYSTORE", nullable = false)
	private KeystorePOJO keystore;
	/**
	 * Attribute that represents the CERT_ID column. 
	 */
	 @Column(name = "CERT_ID")
	private String certId;
	/**
	 * Attribute that represents the CERT_CONTENT column. 
	 */
    @Column(name = "CERT_CONTENT")
    @Lob
	private byte[ ] certContent;
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
	 * Attribute that represents the PRIV_KEY column. 
	 */
    @Column(name = "PRIV_KEY")
	private byte[] privateKey;
	/**
	 * Attribute that represents the KEY_ALG column. 
	 */
    @Column(name = "KEY_ALG")
	private String keyAlgorithm;
	/**
	 * Attribute that represents the association to APPLICATION table. 
	 */
    @OneToMany(mappedBy = "certificate" , fetch = FetchType.LAZY)
	private Set<ApplicationPOJO> applications = new HashSet<ApplicationPOJO>(0);
	/**
	 * Attribute that represents the association to PLATFORM table by AUTH_CERT column. 
	 */
    @OneToMany(mappedBy = "certificateByAuthCert" , fetch = FetchType.LAZY)
	private Set<PlatformPOJO> platformsForAuthCert = new HashSet<PlatformPOJO>(0);
	/**
	 * Attribute that represents the association to PLATFORM table by SOAPTRUSTED column. 
	 */
    @OneToMany(mappedBy = "certificateBySoaptrusted" , fetch = FetchType.LAZY)
	private Set<PlatformPOJO> platformsForSoaptrusted = new HashSet<PlatformPOJO>(0);


	/**
     * Gets the value of the attribute {@link #certPk}.
     * @return the value of the attribute {@link #certPk}.
     */
    
	public Long getCertPk() {
    	
		return this.certPk;
	}

    /**
     * Sets the value of the attribute {@link #certPk}.
     * @param appPkParam The value for the attribute {@link #certPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setCertPk(Long pk) {
		// CHECKSTYLE:ON
		this.certPk = pk;
	}

	 /**
     * Gets the value of the attribute {@link #keystore}.
     * @return the value of the attribute {@link #v}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public KeystorePOJO getKeystore() {
    	// CHECKSTYLE:ON
		return this.keystore;
	}

    /**
     * Sets the value of the attribute {@link #keystore}.
     * @param appPkParam The value for the attribute {@link #keystore}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setKeystore(KeystorePOJO keyStore) {
		// CHECKSTYLE:ON
		this.keystore = keyStore;
	}

	/**
     * Gets the value of the attribute {@link #certId}.
     * @return the value of the attribute {@link #certId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getCertId() {
    	// CHECKSTYLE:ON
		return this.certId;
	}

    /**
     * Sets the value of the attribute {@link #certId}.
     * @param appPkParam The value for the attribute {@link #certId}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setCertId(String certificateId) {
		// CHECKSTYLE:ON
		this.certId = certificateId;
	}

	/**
     * Gets the value of the attribute {@link #certContent}.
     * @return the value of the attribute {@link #certContent}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public byte[ ] getCertContent() {
    	// CHECKSTYLE:ON
		return this.certContent;
	}

    /**
     * Sets the value of the attribute {@link #certContent}.
     * @param appPkParam The value for the attribute {@link #certContent}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setCertContent(byte[ ] content) {
		// CHECKSTYLE:ON
		this.certContent = content;
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
	 * Gets the applications associate to this record.
	 * @return	Associated application list. 
	 */
	public Set<ApplicationPOJO> getApplications() {
		return this.applications;
	}

	/**
	 * Sets the applications associate to this record.
	 * @param applicationList	Associated application list.
	 */
	public void setApplications(Set<ApplicationPOJO> applicationList) {
		this.applications = applicationList;
	}

	/**
	 * Gets the platforms associates to this record by the authentication certificate.
	 * @return	Associated platforms.
	 */
	public Set<PlatformPOJO> getPlatformsForAuthCert() {
		return this.platformsForAuthCert;
	}

	/**
	 * Sets the platforms associates to this record by the authentication certificate.
	 * @param platforms	Associated platforms.
	 */
	public void setPlatformsForAuthCert(Set<PlatformPOJO> platforms) {
		this.platformsForAuthCert = platforms;
	}

	/**
	 * Gets the platforms associates to this record by the SOAP trusted certificate.
	 * @return	Associated platforms.
	 */
	public Set<PlatformPOJO> getPlatformsForSoaptrusted() {
		return this.platformsForSoaptrusted;
	}

	/**
	 * Sets the platforms associates to this record by the SOAP trusted certificate.
	 * @param platforms	Associated platforms.
	 */
	public void setPlatformsForSoaptrusted(Set<PlatformPOJO> platforms) {
		this.platformsForSoaptrusted = platforms;
	}


	/**
     * Gets the value of the attribute {@link #privateKey}.
     * @return the value of the attribute {@link #privateKey}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public byte[] getPrivateKey() {
		// CHECKSTYLE:ON
		return privateKey;
	}


	/**
     * Sets the value of the attribute {@link #privateKey}.
     * @param The value for the attribute {@link #privateKey}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPrivateKey(byte[] privKey) {
		// CHECKSTYLE:ON
		this.privateKey = privKey;
	}


	/**
     * Gets the value of the attribute {@link #keyAlgorithm}.
     * @return the value of the attribute {@link #keyAlgorithm}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getKeyAlgorithm() {
		// CHECKSTYLE:ON
		return keyAlgorithm;
	}


	/**
	 * Sets the value of the attribute {@link #keyAlgorithm}.
	 * 
	 * @param The value for the attribute {@link #keyAlgorithm}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setKeyAlgorithm(String keyAlg) {
		// CHECKSTYLE:ON
		this.keyAlgorithm = keyAlg;
	}

}
