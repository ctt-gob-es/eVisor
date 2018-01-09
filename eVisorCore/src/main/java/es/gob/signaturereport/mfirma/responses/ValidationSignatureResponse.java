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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse.java.</p>
 * <b>Description:</b><p>Class that contains the information that returns a validation signature process.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/02/2011.
 */
package es.gob.signaturereport.mfirma.responses;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** 
 * <p>Class that contains the information that returns a validation signature process.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/02/2011.
 */
public class ValidationSignatureResponse {

	/**
	 * Attribute that represents the verification response returned by '@firma'. 
	 */
	private String xmlResponse = null;

	/**
	 * Attribute that represents the table with the results of the validation of electronic signatures. 
	 */
	private LinkedHashMap<String, Object> validationInfo = null;

	/**
	 * Attribute that represents signed document list. 
	 */
	private List<SignedDocument> signedDocumentList = new ArrayList<SignedDocument>();
	
	/**
	 * Constructor method for the class ValidationSignatureResponse.java.
	 * @param xml	The verification response returned by '@firma'.
	 * @param valInfo 	Table with the results of the validation of electronic signatures.
	 */
	public ValidationSignatureResponse(String xml, LinkedHashMap<String, Object> valInfo) {
		super();
		this.xmlResponse = xml;
		this.validationInfo = valInfo;
	}

	/**
	 * Gets the value of the verification response returned by '@firma'.
	 * @return the value of the verification response returned by '@firma'.
	 */
	public String getXmlResponse() {
		return xmlResponse;
	}

	/**
	 * Sets the value of the verification response returned by '@firma'.
	 * @param response The value for the verification response returned by '@firma'.
	 */
	public void setXmlResponse(String response) {
		this.xmlResponse = response;
	}

	/**
	 * Gets the value of the table with the results of the validation of electronic signatures. 
	 * @return the value of the table with the results of the validation of electronic signatures. 
	 */
	public LinkedHashMap<String, Object> getValidationInfo() {
		return validationInfo;
	}

	/**
	 * Sets the value of the table with the results of the validation of electronic signatures. 
	 * @param info The value for the table with the results of the validation of electronic signatures. 
	 */
	public void setValidationInfo(LinkedHashMap<String, Object> info) {
		this.validationInfo = info;
	}

	
	/**
	 * Gets the value of the signed document list.
	 * @return the value of the signed document list.
	 */
	public List<SignedDocument> getSignedDocumentList() {
		return signedDocumentList;
	}

	
	/**
	 * Sets the value of the signed document list.
	 * @param signedDocumentList The value for the signed document list.
	 */
	public void setSignedDocumentList(List<SignedDocument> signedDocumentList) {
		this.signedDocumentList = signedDocumentList;
	}

	/**
	 * Constructor method for the class ValidationSignatureResponse.java.
	 * @param xmlResponse	The verification response returned by '@firma'.
	 * @param validationInfo	Table with the results of the validation of electronic signatures.
	 * @param signedDocumentList 	Signed document list. 
	 */
	public ValidationSignatureResponse(String xmlResponse, LinkedHashMap<String, Object> validationInfo, List<SignedDocument> signedDocumentList) {
		super();
		this.xmlResponse = xmlResponse;
		this.validationInfo = validationInfo;
		this.signedDocumentList = signedDocumentList;
	}

}
