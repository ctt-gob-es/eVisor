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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Attachment.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the ATTACHMENT table.</p>
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <p>This class represents a record contained into the ATTACHMENT table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Entity
@Table(name = "ATTACHMENT")
public class AttachmentPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -2080425081215312629L;

	/**
	 * Attribute that represents the ATT_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_ATTACHMENT_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_ATTACHMENT_GEN", sequenceName = "SQ_ATTACHMENT",allocationSize=0)
    @Column(name = "ATT_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private int attPk;

	/**
	 * Attribute that represents the ATT_SIG column. 
	 */
	@Column(name = "ATT_SIG")
	@Type(type = Constants.CONS_YES_NO)
	private boolean attSig;

	/**
	 * Attribute that represents the ATT_CERT column. 
	 */
	@Column(name = "ATT_CERT")
	@Type(type = Constants.CONS_YES_NO)
	private boolean attCert;

	/**
	 * Attribute that represents the ATT_DOC column. 
	 */
	@Column(name = "ATT_DOC")
	@Type(type = Constants.CONS_YES_NO)
	private boolean attDoc;

	/**
	 * Attribute that represents the ATT_RESP column. 
	 */
	@Column(name = "ATT_RESP")
	@Type(type = Constants.CONS_YES_NO)
	private boolean attResp;

	/**
	 * Attribute that represents the ATT_DOC_SIG column. 
	 */
	@Column(name = "ATT_DOC_SIG")
	@Type(type = Constants.CONS_YES_NO)
	private boolean attDocSig;

	/**
	 * Attribute that represents the association to TEMPLATEREPORT table. 
	 */
	@OneToMany(mappedBy = "attachment" , fetch = FetchType.EAGER)
	private Set<TemplateReportPOJO> templatereports = new HashSet<TemplateReportPOJO>(0);

	/**
     * Gets the value of the attribute {@link #attPk}.
     * @return the value of the attribute {@link #attPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public int getAttPk() {
    	// CHECKSTYLE:ON
		return this.attPk;
	}

    /**
     * Sets the value of the attribute {@link #attPk}.
     * @param pk The value for the attribute {@link #attPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setAttPk(int pk) {
		// CHECKSTYLE:ON
		this.attPk = pk;
	}

	/**
	 * Indicates if the signature is attached.
	 * Gets the value of the attribute {@link #attSig}.
	 * @return	True if the signature is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public boolean getAttSig() {
		// CHECKSTYLE:ON
		return this.attSig;
	}

	/**
	 * Sets if the signature is attached.
	 * Sets the value of the attribute {@link #isAttSig}.
	 * @param isAttSig True if the signature is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setAttSig(boolean isAttSig) {
		// CHECKSTYLE:ON
		this.attSig = isAttSig;
	}

	/**
	 * Gets if the certificate is attached.
	 * Gets the value of the attribute {@link #attCert}.
	 * @return True if the certificate is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.

	public boolean getAttCert() {
		// CHECKSTYLE:ON
		return this.attCert;
	}

	/**
	 * Sets if the certificate is attached.
	 * Sets the value of the attribute {@link #isAttCert}.
	 * @param isAttCert True if the certificate is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setAttCert(boolean isAttCert) {
		// CHECKSTYLE:ON
		this.attCert = isAttCert;
	}

	/**
	 * Gets if the document is attached.
	 * Gets the value of the attribute {@link #attDoc}.
	 * @return True if the document is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public boolean getAttDoc() {
		// CHECKSTYLE:ON
		return this.attDoc;
	}

	/**
	 * Sets if the document is attached.
	 * Sets the value of the attribute {@link #isAttDoc}.
	 * @param isAttDoc	True if the document is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setAttDoc(boolean isAttDoc) {
		// CHECKSTYLE:ON
		this.attDoc = isAttDoc;
	}

	/**
	 * Gets if the response is attached.
	 * Gets the value of the attribute {@link #attResp}.
	 * @return True if the response is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public boolean getAttResp() {
		// CHECKSTYLE:ON
		return this.attResp;
	}

	/**
	 * Sets the value of the attribute {@link #isAttResp}.
	 * @param isAttResp True if the response is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setAttResp(boolean isAttResp) {
		// CHECKSTYLE:ON
		this.attResp = isAttResp;
	}

	/**
	 * Gets the template list associates to this record.
	 * @return	Associated template list.
	 */

	public Set<TemplateReportPOJO> getTemplatereports() {
		return this.templatereports;
	}

	/**
	 * Sets the template list associated to this record.
	 * @param templates	Template list
	 */
	public void setTemplatereports(Set<TemplateReportPOJO> templates) {
		this.templatereports = templates;
	}


	/**
	 * Gets if the response is attached.
	 * Gets the value of the attribute {@link #attDocSig}.
	 * @return True if the response is attached. Otherwise false.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.

	public boolean getAttDocSig() {
		// CHECKSTYLE:ON
		return attDocSig;
	}


	/**
	 * Sets the value of the document included into response is attached.
	 * Sets the value of the attribute {@link #attDocSig}.
	 * @param attDocSig The value for the document included into response is attached.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setAttDocSig(boolean attDocSig) {
		// CHECKSTYLE:ON
		this.attDocSig = attDocSig;
	}

}
