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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.Templatereport.java.</p>
 * <b>Description:</b><p>This class represents a record contained into the TEMPLATEREPORT table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;

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

import org.hibernate.annotations.Type;

import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>This class represents a record contained into the TEMPLATEREPORT table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Entity
@Table(name = "TEMPLATEREPORT")
@NamedQueries({
	  @NamedQuery(name="findTRPKsByIdList",
	              query="SELECT tr.trPk FROM TemplateReportPOJO tr WHERE tr.trId in :trIdList AND tr.endingtime is null"),
	  @NamedQuery(name="findTRPById",
	  			query="SELECT tr FROM TemplateReportPOJO tr WHERE tr.trId = :trId AND tr.endingtime is null"),
	  @NamedQuery(name="findAllActiveTR",
	  			query="SELECT tr FROM TemplateReportPOJO tr WHERE tr.endingtime is null")
	  			
	})
//SELECT TR_PK FROM TEMPLATEREPORT WHERE TR_ID IN (:templateId_list) AND ENDINGTIME IS NULL
public class TemplateReportPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = 7820994348573543711L;
	/**
	 * Attribute that represents the TR_PK column. 
	 */
	@Id
    @GeneratedValue(generator = "SQ_REPORT_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SQ_REPORT_GEN", sequenceName = "SQ_REPORT",allocationSize=0)
    @Column(name = "TR_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long trPk;
	/**
	 * Attribute that represents the association to ATTACHMENT table. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TR_ATT", nullable = false)
	private AttachmentPOJO attachment;
	/**
	 * Attribute that represents the TR_ID column. 
	 */
	 @Column(name = "TR_ID")
	private String trId;
	/**
	 * Attribute that represents the TR_TYPE column. 
	 */
	@Column(name = "TR_TYPE")
	private int trType;
	/**
	 * Attribute that represents the association to TEMPLATECONTENT table. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TR_CONTENT", nullable = false)
	private TemplateContentPOJO trContent;
	/**
	 * Attribute that represents the TR_FORCE column. 
	 */
	@Column(name = "TR_FORCE")
	@Type(type = Constants.CONS_YES_NO)
	private boolean trForce;
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
	 * Attribute that represents the association to APPLICATION table. 
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	           name="APPTEMPLATE",
	           joinColumns=@JoinColumn(name="T_REPORT", referencedColumnName="TR_PK", nullable = false), 
	           inverseJoinColumns=@JoinColumn(name="APP", referencedColumnName="APP_PK", nullable = false))
	private Set<ApplicationPOJO> applications;
	/**
	 * Attribute that represents the DI_TYPE column. 
	 */
	@Column(name = "DI_TYPE")
	private int diType;
	/**
	 * Attribute that represents the CONCATRULE column. 
	 */
	 @Column(name = "CONCATRULE")
	private String concatrule;
	/**
	 * Attribute that represents the PAGE_RANGE column. 
	 */
	@Column(name = "PAGERANGE")
	private String pageRange;

	/**
	 * Constructor method for the class Templatereport.java. 
	 */
	public TemplateReportPOJO() {
	}

	/**
     * Gets the value of the attribute {@link #trPk}.
     * @return the value of the attribute {@link #trPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public Long getTrPk() {
    	// CHECKSTYLE:ON
		return this.trPk;
	}

    /**
     * Sets the value of the attribute {@link #trPk}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setTrPk(Long pk) {
		// CHECKSTYLE:ON
		this.trPk = pk;
	}

	/**
     * Gets the value of the attribute {@link #attachment}.
     * @return the value of the attribute {@link #attachment}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public AttachmentPOJO getAttachment() {
    	// CHECKSTYLE:ON
		return this.attachment;
	}

    /**
     * Sets the value of the attribute {@link #attachment}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setAttachment(AttachmentPOJO att) {
		// CHECKSTYLE:ON
		this.attachment = att;
	}

	/**
    * Gets the value of the attribute {@link #trId}.
    * @return the value of the attribute {@link #trId}.
    */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getTrId() {
    	// CHECKSTYLE:ON
		return this.trId;
	}

    /**
     * Sets the value of the attribute {@link #trId}.
     */
     // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
     // because Hibernate JPA needs not final access methods.
	public void setTrId(String id) {
		// CHECKSTYLE:ON
		this.trId = id;
	}

	/**
	 * Gets the value of the attribute {@link #trType}.
	 * @return the value of the attribute {@link #trType}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public int getTrType() {
		// CHECKSTYLE:ON
		return this.trType;
	}

	/**
	 * Sets the value of the attribute {@link #trType}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setTrType(int type) {
		// CHECKSTYLE:ON
		this.trType = type;
	}

	/**
	 * Gets the value of the attribute {@link #trContent}.
	 * @return the value of the attribute {@link #trContent}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public TemplateContentPOJO getTrContent() {
		// CHECKSTYLE:ON
		return this.trContent;
	}

	/**
	 * Sets the value of the attribute {@link #trContent}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setTrContent(TemplateContentPOJO content) {
		// CHECKSTYLE:ON
		this.trContent = content;
	}

	/**
	 * Gets the value of the attribute {@link #trForce}.
	 * @return the value of the attribute {@link #trForce}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public boolean getTrForce() {
		// CHECKSTYLE:ON
		return this.trForce;
	}

	/**
	 * Sets the value of the attribute {@link #trForce}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setTrForce(boolean force) {
		// CHECKSTYLE:ON
		this.trForce = force;
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
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
    public void setEndingtime(Date endingtimeParam) {
		// CHECKSTYLE:ON
		this.endingtime = endingtimeParam;
    }

	/**
	 * Gets the associated applications.
	 * @return The associated applications.
	 */
	public Set<ApplicationPOJO> getApplications() {
		return this.applications;
	}

	/**
	 * Sets the associated applications.
	 * @param apps The associated applications.
	 */
	public void setApplications(Set<ApplicationPOJO> apps) {
		this.applications = apps;
	}
	
	/**
    * Gets the value of the attribute {@link #concatrule}.
    * @return the value of the attribute {@link #concatrule}.
    */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public String getConcatrule() {
    	// CHECKSTYLE:ON
	    return concatrule;
	}

    /**
     * Sets the value of the attribute {@link #concatrule}.
     * @param The value for the attribute {@link #concatrule}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setConcatrule(String rule) {
		// CHECKSTYLE:ON
	    this.concatrule = rule;
	}

	/**
	 * Gets the value of the attribute {@link #pageRange}.
	 * @return the value of the attribute {@link #pageRange}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	
	public String getPageRange() {
		// CHECKSTYLE:ON
	    return pageRange;
	}
	
	 /**
     * Sets the value of the attribute {@link #pageRange}.
     * @param The value for the attribute {@link #pageRange}.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setPageRange(String range) {
		// CHECKSTYLE:ON
	    this.pageRange = range;
	}

	/**
	 * Gets the value of the attribute {@link #diType}.
	 * @return the value of the attribute {@link #diType}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public int getDiType() {
		// CHECKSTYLE:ON
	    return diType;
	}
		
	/**
	 * Sets the value of the attribute {@link #diType}.
	 * @return the value of the attribute {@link #diType}.
	 */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
	// because Hibernate JPA needs not final access methods.
	public void setDiType(int includingType) {
		// CHECKSTYLE:ON
	    this.diType = includingType;
	}

}
