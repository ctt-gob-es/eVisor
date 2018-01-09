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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.TemplateData.java.</p>
 * <b>Description:</b><p> Class that contains the configuration information associated to a template stored into the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>24/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 24/02/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.util.Arrays;

import es.gob.signaturereport.persistence.utils.Constants;


/** 
 * <p>Class that contains the configuration information associated to a template stored into the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 24/02/2011.
 */
public class TemplateData {

    /**
     * Attribute that represents the identifier of template. 
     */
    private String identifier = null;
    /**
     * Attribute that represents the type of report associated to template. 
     */
    private int reportType = Constants.PDF_REPORT;

    /**
     * Attribute that represents if the report should be generated although the signature is invalid. 
     */
    private boolean forceGeneration = false;

    /**
     * Attribute that represents the mode that the document will be included into the report. The values might be:<br/>
     *   {@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_NONE} The document doesn't include into report.<br/>
     *   {@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_CONCAT} Document and report will be concatenated. <br/>
     *   {@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_EMBED} The document will be embed into the report.<br/>
     *   {@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_IMAGE} The report include the document as image. <br/>
     *   
     */
    private int modeDocInclude = Constants.INC_SIGNED_DOC_NONE;

    /**
     * Attribute that represents the rule that  document and report will be concatenated. 
     * <br/>Examples:
     * <br/>REP+DOC: report + document.
     * <br/>REP(1) + DOC(2-4) + DOC(6) + REP(2-4). Concatenate the pages selected
     */
    private String concatenationRule = null;

    /**
     * Attribute that is used to indicate the range of the document page to draw. If this is null all  document pages  will be to extract.
     * A example: 1-4,4,10, 56-60 
     */
    private String pagesRange = null;

    /**
     * Attribute that indicates if the signature is included into the report as attachment. 
     */
    private boolean attachSignature = false;

    /**
     * Attribute that indicates if the signed document is included into the report as attachment. 
     */
    private boolean attachDocument = false;

    /**
     * Attribute that indicates if the signer certificate is included into the report as attachment.
     */
    private boolean attachCertificate = false;

    /**
     *  Attribute that indicates if the XML response is included into the report as attachment. 
     */
    private boolean attchResponse = false;

    /**
     * Attribute that indicates if the document included into the signature will be included into the report as attachment.
     */
    private boolean attchDocInSignature = false;

    /**
     * Attribute that represents the template file to create report. 
     */
    private byte[] template = null;

    /**
     * Gets the value of the type of report associated to template.
     * @return the value of the type of report associated to template.
     */
    public int getReportType() {
        return reportType;
    }


    /**
     * Sets the value of the type of report associated to template.
     * @param type The value for the type of report associated to template.
     */
    public void setReportType(int type) {
        this.reportType = type;
    }


    /**
     * Gets the value of the identifier of template.
     * @return the value of the identifier of template.
     */
    public String getIdentifier() {
        return identifier;
    }


    /**
     * Sets the value of the identifier of template.
     * @param reportId The value for the identifier of template.
     */
    public void setIdentifier(String reportId) {
        this.identifier = reportId;
    }


    /**
     * Constructor method for the class TemplateData.java.
     * @param templateId	Identifier of template.
     * @param templateType 	Type of report.
     */
    public TemplateData(String templateId, int templateType) {
    	super();
    	this.identifier = templateId;
    	this.reportType = templateType;
    }


    /**
     * Gets if the report should be generated although the signature is invalid. 
     * @return true report should be generated.
     */
    public boolean isForceGeneration() {
        return forceGeneration;
    }


    /**
     * Sets if the report should be generated although the signature is invalid.
     * @param force 	True, if you force the creation of the report.
     */
    public void setForceGeneration(boolean force) {
        this.forceGeneration = force;
    }


    /**
     * Gets the value of the template file to create report.
     * @return The template file to create report.
     */
    public byte[ ] getTemplate() {
        return template;
    }


    /**
     * Sets the value of the template file to create report.
     * @param xslTemplate The template file to create report.
     */
    public void setTemplate(byte[ ] xslTemplate) {
    	if(xslTemplate!=null){
    		this.template = Arrays.copyOf(xslTemplate,xslTemplate.length);
    	}else{
    		this.template = null;
    	}
        
    }



    /**
     * Gets the mode that the document will be included into the report.
     * @return	Mode that the document will be included into the report. The values allowed:
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_NONE}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_IMAGE}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_CONCAT}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_NONE}. 
     */
    public int getModeDocInclude() {
        return modeDocInclude;
    }


    /**
     * Sets the mode that the document will be included into the report.
     * @param includeMode	Mode that the document will be included into the report. The values allowed:
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_NONE}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_IMAGE}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_CONCAT}.
     * <br/>	{@link TemplateConfigurationFacadeI#INC_SIGNED_DOC_NONE}. 
     */
    public void setModeDocInclude(int includeMode) {
        this.modeDocInclude = includeMode;
    }


    /**
     * Gets the rule that  document and report will be concatenated. 
     * @return String that represents the rule that  document and report will be concatenated. 
     * <br/>Examples:
     * <br/>REP+DOC: report + document.
     * <br/>REP(1) + DOC(2-4) + DOC(6) + REP(2-4). Concatenate the pages selected
     */
    public String getConcatenationRule() {
        return concatenationRule;
    }

    /**
     * Sets the rule that  document and report will be concatenated. 
     * @param concatRule Rule that  document and report will be concatenated. 
     * <br/>Examples:
     * <br/>REP+DOC: report + document.
     * <br/>REP(1) + DOC(2-4) + DOC(6) + REP(2-4). Concatenate the pages selected
     */
    public void setConcatenationRule(String concatRule) {
        this.concatenationRule = concatRule;
    }


    /**
     * Returns the range of the document page to draw.
     * @return	String that represents the range of the document page to draw. If this is null all  document pages  will be to extract.
     * A example: 1-4,4,10, 56-60 
     */
    public String getPagesRange() {
        return pagesRange;
    }


    /**
     * Sets the range of the document page to draw.
     * @param range	String that represents the range of the document page to draw. If this is null all  document pages  will be to extract.
     * A example: 1-4,4,10, 56-60
     */
    public void setPagesRange(String range) {
        this.pagesRange = range;
    }


    /**
     * Indicates if the signature is included into the report as attachment.
     * @return	True if the signature is included into the report as attachment. Otherwise false.
     */
    public boolean isAttachSignature() {
        return attachSignature;
    }


    /**
     * Sets if the signature is included into the report as attachment.
     * @param isAttachSignature	True if the signature is included into the report as attachment. Otherwise false.
     */
    public void setAttachSignature(boolean isAttachSignature) {
        this.attachSignature = isAttachSignature;
    }


    /**
     * Gets if the signed document is included into the report as attachment.
     * @return	True if the signed document is included into the report as attachment. Otherwise false.
     */
    public boolean isAttachDocument() {
        return attachDocument;
    }


    /**
     * Gets if the signed document is included into the report as attachment.
     * @param isAttachDocument	True if the signed document is included into the report as attachment. Otherwise false.
     */
    public void setAttachDocument(boolean isAttachDocument) {
        this.attachDocument = isAttachDocument;
    }


    /**
     * Indicates if the signer certificate is included into the report as attachment.
     * @return True if the signer certificate is included into the report as attachment. Otherwise false.
     */
    public boolean isAttachCertificate() {
        return attachCertificate;
    }


    /**
     * Sets if the signer certificate is included into the report as attachment.
     * @param isAttachCertificate	True if the signer certificate is included into the report as attachment. Otherwise false.
     */
    public void setAttachCertificate(boolean isAttachCertificate) {
        this.attachCertificate = isAttachCertificate;
    }


    /**
     * Indicates if the XML response is included into the report as attachment.
     * @return	True if the XML response is included into the report as attachment. Otherwise false.
     */
    public boolean isAttchResponse() {
        return attchResponse;
    }


    /**
     * Set if the XML response is included into the report as attachment.
     * @param isAttchResponse	True if the XML response is included into the report as attachment. Otherwise false.
     */
    public void setAttchResponse(boolean isAttchResponse) {
        this.attchResponse = isAttchResponse;
    }


	/**
	 * Gets if the document included into the signature will be included into the report as attachment.
	 * @return True if the document included into the signature will be included into the report as attachment, otherwise false.
	 */
	public boolean isAttchDocInSignature() {
		return attchDocInSignature;
	}


	/**
	 * Sets if the document included into the signature will be included into the report as attachment.
	 * @param attchDocInSignature True if the document included into the signature will be included into the report as attachment, otherwise false.
	 */
	public void setAttchDocInSignature(boolean attchDocInSignature) {
		this.attchDocInSignature = attchDocInSignature;
	}


}
