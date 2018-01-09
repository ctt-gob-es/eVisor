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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.TemplateContent.java.</p>
 * <b>Description:</b><p> Class that represents a record of the "TEMPLATECONTENT" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/06/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/** 
 * <p>Class that represents a record of the "TEMPLATECONTENT" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/06/2011.
 */
@Entity
@Table(name = "TEMPLATECONTENT")
@NamedQueries({
	  @NamedQuery(name="findTCById",
	              query="SELECT tc FROM TemplateContentPOJO tc WHERE tc.contentId = :contentId")
	})
public class TemplateContentPOJO {
	
	/**
	 * Attribute that represents the CONTENT_ID column. 
	 */
	@Id
	@Column(name = "CONTENT_ID", unique = true, nullable = false)
	private String contentId;
	
	/**
	 * Attribute that represents the TREPORT column. 
	 */
	@Column(name = "TREPORT")
	@Lob
	private byte[] content;

	
	/**
     * Gets the value of the attribute {@link #contentId}.
     * @return the value of the attribute {@link #contentId}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public String getContentId() {
		// CHECKSTYLE:ON
		return contentId;
	}

	
	/**
     * Sets the value of the attribute {@link #contentId}.
     * @return the value of the attribute {@link #contentId}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setContentId(String contentIdentifier) {
		// CHECKSTYLE:ON
		this.contentId = contentIdentifier;
	}

	
	/**
     * Gets the value of the attribute {@link #content}.
     * @return the value of the attribute {@link #content}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	
	public byte[] getContent() {
		// CHECKSTYLE:ON
		return content;
	}

	
	/**
     * Sets the value of the attribute {@link #content}.
     * @return the value of the attribute {@link #content}.
     */
	// CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because Hibernate JPA needs not final access methods.
	public void setContent(byte[] templateContent) {
		this.content = templateContent;
	}
	
}
