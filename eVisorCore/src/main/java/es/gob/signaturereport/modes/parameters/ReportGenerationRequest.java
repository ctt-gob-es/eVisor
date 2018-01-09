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
 * <b>File:</b><p>es.gob.signaturereport.modes.parameters.ReportGenerationRequest.java.</p>
 * <b>Description:</b><p> Class that contains the input parameters for generation of signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>26/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 26/09/2011.
 */
package es.gob.signaturereport.modes.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;


/** 
 * <p>Class that contains the input parameters for generation of signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 26/09/2011.
 */
public class ReportGenerationRequest {

	/**
	 * Attribute that represents an identifier of application. 
	 */
	private String applicationId = null;
	
	/**
	 * Attribute that represents an identifier of template. 
	 */
	private String templateId = null;

	/**
	 * Attribute that represents a digital signature that to be used to generate report. 
	 */
	private byte[ ] signature = null;

	/**
	 * Attribute that represents a location of a electronic signature into a external repository.. 
	 */
	private  RepositoryLocation externalSignature = null;
	
	/**
	 * Attribute that represents a verify signature response returned by '@firma' platform.. 
	 */
	private AfirmaResponse afirmaResponse  = null;
	/**
	 * Attribute that represents a document signed. 
	 */
	private byte[ ] document = null;
	
	/**
	 * Attribute that represents a flag that indicates if the report will be signed. 
	 */
	private boolean serverSign = false; 
	/**
	 * Attribute that represents a location of a signed document into a external repository.. 
	 */
	private RepositoryLocation externalDocument = null;
	/**
	 * Attribute that represents the bar codes list to include into signature report. 
	 */
	private ArrayList<Barcode> barcodes =null;
	/**
	 * Attribute that represents the additional parameters included in the request. 
	 */
	private LinkedHashMap<String, String> additionalParameters = null;
	
	/**
	 * Constructor method for the class ReportGenerationRequest.java. 
	 */
	public ReportGenerationRequest() {
	}
	
	/**
	 * Gets the value of application identifier.
	 * @return the value of application identifier.
	 */
	public String getApplicationId() {
		return applicationId;
	}

	
	/**
	 * Sets the value of application identifier.
	 * @param appId The value for application identifier.
	 */
	public void setApplicationId(String appId) {
		this.applicationId = appId;
	}

	
	/**
	 * Gets the value of template identifier.
	 * @return the value of template identifier.
	 */
	public String getTemplateId() {
		return templateId;
	}

	
	/**
	 * Sets the value of template identifier.
	 * @param tempId The value for template identifier.
	 */
	public void setTemplateId(String tempId) {
		this.templateId = tempId;
	}

	
	/**
	 * Gets the value of signature.
	 * @return the value of signature.
	 */
	public byte[ ] getSignature() {
		return signature;
	}

	
	/**
	 * Sets the value of signature.
	 * @param eSignature The value for signature.
	 */
	public void setSignature(byte[ ] eSignature) {
		if(eSignature!=null){
			this.signature = Arrays.copyOf(eSignature,eSignature.length);
		}else{
			this.signature = null;
		}
	}

	
	/**
	 * Gets the value of location a signature in the external repository.
	 * @return the value of location a signature in the external repository.
	 */
	public RepositoryLocation getExternalSignature() {
		return externalSignature;
	}

	
	/**
	 * Sets the value of location a signature in the external repository.
	 * @param extSignature The value for location a signature in the external repository.
	 */
	public void setExternalSignature(RepositoryLocation extSignature) {
		this.externalSignature = extSignature;
	}

	
	/**
	 * Gets the value of the SOAP verify response..
	 * @return the value of the SOAP verify response..
	 */
	public AfirmaResponse getAfirmaResponse() {
		return afirmaResponse;
	}

	
	/**
	 * Sets the value of the SOAP verify response..
	 * @param afResponse The value for the SOAP verify response..
	 */
	public void setAfirmaResponse(AfirmaResponse afResponse) {
		this.afirmaResponse = afResponse;
	}

	
	/**
	 * Gets the value of the signed document.
	 * @return the value of the signed document.
	 */
	public byte[ ] getDocument() {
		return document;
	}

	
	/**
	 * Sets the value of the signed document.
	 * @param doc The value for the signed document.
	 */
	public void setDocument(byte[ ] doc) {
		if(doc!=null){
			this.document = Arrays.copyOf(doc, doc.length);
		}else{
			this.document = null;
		}
	}

	
	/**
	 * Gets the value of the flag that indicates if the report will be signed.
	 * @return the value of the flag that indicates if the report will be signed.
	 */
	public boolean isServerSign() {
		return serverSign;
	}

	
	/**
	 * Sets the value of the flag that indicates if the report will be signed.
	 * @param isSign The value for the flag that indicates if the report will be signed.
	 */
	public void setServerSign(boolean isSign) {
		this.serverSign = isSign;
	}

	
	/**
	 * Gets the value of the location of the signed document into a external repository..
	 * @return the value of the location of the signed document into a external repository..
	 */
	public RepositoryLocation getExternalDocument() {
		return externalDocument;
	}

	
	/**
	 * Sets the value of the location of the signed document into a external repository..
	 * @param exDocument The value for the location of the signed document into a external repository..
	 */
	public void setExternalDocument(RepositoryLocation exDocument) {
		this.externalDocument = exDocument;
	}

	
	/**
	 * Gets the value of the bar code list.
	 * @return the value of the bar code list.
	 */
	public ArrayList<Barcode> getBarcodes() {
		return barcodes;
	}

	
	/**
	 * Sets the value of the bar code list.
	 * @param barcodeList The value for the bar code list.
	 */
	public void setBarcodes(ArrayList<Barcode> barcodeList) {
		this.barcodes = barcodeList;
	}

	
	/**
	 * Gets the value of the additional parameters included into the request.
	 * @return the value of the additional parameters included into the request.
	 */
	public LinkedHashMap<String, String> getAdditionalParameters() {
		return additionalParameters;
	}

	
	/**
	 * Sets the value of the additional parameters included into the request.
	 * @param addParameters The value for the additional parameters included into the request.
	 */
	public void setAdditionalParameters(LinkedHashMap<String, String> addParameters) {
		this.additionalParameters = addParameters;
	}


	

}
