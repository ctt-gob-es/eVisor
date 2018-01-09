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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Keystore.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the KEYSTORE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;


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
 * <p>This class represents a record contained into the KEYSTORE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/04/2011.
 */
@Entity
@Table(name = "KEYSTORE")
@NamedQueries({ 
	@NamedQuery(name = "increaseVersion", query = "UPDATE KeystorePOJO SET ksVersion = ksVersion + 1 WHERE ksName LIKE :keystoreId"),
	@NamedQuery(name = "findKSByName", query = "SELECT ks FROM KeystorePOJO ks WHERE ks.ksName = :keystoreId"),
	@NamedQuery(name = "getVersion", query = "SELECT ks.ksVersion FROM KeystorePOJO ks WHERE ks.ksName = :keystoreId")

})
public class KeystorePOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -4811480062362404109L;
	
	/**
	 * Attribute that represents the KS_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_KEYSTORE_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_KEYSTORE_GEN", sequenceName = "SQ_KEYSTORE",allocationSize=0)
    @Column(name = "KS_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private int ksPk;
	/**
	 * Attribute that represents the KS_NAME column. 
	 */
	@Column(name = "KS_NAME")
	private String ksName;
	/**
	 * Attribute that represents the KS_VERSION column. 
	 */
	@Column(name = "KS_VERSION")
	private int ksVersion;
	
	/**
	 * Attribute that represents the association to CERTIFICATE table. 
	 */
	@OneToMany(mappedBy = "keystore" , fetch = FetchType.EAGER)
	@Where(clause = "ENDINGTIME IS NULL")
	private Set<CertificatePOJO> certificates = new HashSet<CertificatePOJO>();


	/**
     * Gets the value of the attribute {@link #ksPk}.
     * @return the value of the attribute {@link #ksPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public int getKsPk() {
    	// CHECKSTYLE:ON
		return this.ksPk;
	}

    /**
     * Sets the value of the attribute {@link #ksPk}.
     * @param appPkParam The value for the attribute {@link #ksPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setKsPk(int pk) {
		// CHECKSTYLE:ON
		this.ksPk = pk;
	}

	/**
     * Gets the value of the attribute {@link #ksName}.
     * @return the value of the attribute {@link #ksName}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public String getKsName() {
		// CHECKSTYLE:ON
		return this.ksName;
	}

	/**
	 * Sets the value of the attribute {@link #ksName}.
	 * 
	 * @param The value for the attribute {@link #ksName}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setKsName(String name) {
		// CHECKSTYLE:ON
		this.ksName = name;
	}

	/**
     * Gets the value of the attribute {@link #ksVersion}.
     * @return the value of the attribute {@link #ksVersion}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    
	public int getKsVersion() {
    	// CHECKSTYLE:ON
		return this.ksVersion;
	}

    /**
	 * Sets the value of the attribute {@link #ksVersion}.
	 * 
	 * @param The value for the attribute {@link #ksVersion}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setKsVersion(int version) {
		this.ksVersion = version;
	}

	/**
	 * Gets the associated certificates.
	 * @return	Associated certificates
	 */
	
	public Set<CertificatePOJO> getCertificates() {
		return this.certificates;
	}

	/**
	 * Sets the associated certificates.
	 * @param certs	Associated certificates
	 */
	public void setCertificates(Set<CertificatePOJO> certs) {
		this.certificates = certs;
	}

}
