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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d3r1.java.</p>
 * <b>Description:</b><p> Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v.5.3.1.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>17/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 17/02/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.tools.FileSystemReader;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.UniqueIdGenerator;
import es.gob.signaturereport.tools.UtilsBase64;
import es.gob.signaturereport.tools.XMLUtils;

/**
 * <p>Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v.5.3.1.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 17/02/2011.
 */
public class DSSValidateSignaturev5d3r1 implements DSSVerifyTransformI {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(DSSValidateSignaturev5d3r1.class);

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#marshal(java.util.LinkedHashMap)
	 */
	public String marshal(LinkedHashMap<String, String> parameters) throws TransformException {

		Document xml = getTemplate();

		ArrayList<String> nodeToRemove = new ArrayList<String>();

		fillDocument(xml, parameters, nodeToRemove);

		fillSignature(xml, parameters, nodeToRemove);

		fillOptionalOutputs(xml, parameters, nodeToRemove);

		return getStringXML(xml, nodeToRemove);

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#unmarshal(java.lang.String)
	 */
	public LinkedHashMap<String, Object> unmarshal(String xml) throws TransformException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		Document doc = null;
		try {
			doc = XMLUtils.getDocument(xml.getBytes("UTF-8"));
		} catch (ToolsException e) {
			LOGGER.error(e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, e.getDescription());
		} catch (UnsupportedEncodingException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_014);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}

		readGlobalResult(doc, response);

		readIndividualSignatureReport(doc, response);

		return response;
	}

	/**
	 * This method include the content of global result element into the Map supplied.
	 * @param doc	XML document to search the result.
	 * @param outputs	Map to include the result.
	 * @throws TransformException	There was an error processing the XML.
	 */
	protected void readGlobalResult(Document doc, LinkedHashMap<String, Object> outputs) throws TransformException {
		try {
			NodeList nl = XMLUtils.getNodes(doc.getDocumentElement(), ABS_RESULT_RES);
			if (nl != null && nl.getLength() == 1) {
				readResult(nl.item(0), outputs);
			}
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_012);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}

	/**
	 * This method include the content of IndividualSignatureReport elements into the Map supplied.
	 * @param doc	XML document to search.
	 * @param outputs	Map to include the individual signature information.
	 * @throws TransformException	There was an error processing the XML.
	 */
	protected void readIndividualSignatureReport(Document doc, LinkedHashMap<String, Object> outputs) throws TransformException {
		try {
			ArrayList<LinkedHashMap<String, Object>> signers = new ArrayList<LinkedHashMap<String, Object>>();
			NodeList nl = XMLUtils.getNodes(doc.getDocumentElement(), IND_SIG_REPORT_RES);
			for (int i = 0; i < nl.getLength(); i++) {
				LinkedHashMap<String, Object> signer = new LinkedHashMap<String, Object>();
				NodeList results = XMLUtils.getNodes(nl.item(i), PART_RESULT_RES);
				if (results != null && results.getLength() == 1) {
					readResult(results.item(0), signer);
				}
				NodeList certInfoList = XMLUtils.getNodes(nl.item(i), CERT_INFO_RES);
				if (certInfoList != null && certInfoList.getLength() == 1) {
					readCertificateInfo(certInfoList.item(0), signer);
				}
				// Sello de tiempo
				String creationTime = XMLUtils.getNodeValue(nl.item(i), CREATION_TIME_RES);
				if (creationTime != null) {
					signer.put(CREATION_TIME_RES, creationTime);
				}
				// Certificado firmante
				String serialNumber = XMLUtils.getNodeValue(nl.item(i), SIGNER_CERT_SN_RES);
				if (serialNumber != null) {
					NodeList pathValidity = XMLUtils.getNodes(nl.item(i), PATHVALIDITYDETAIL_RES);
					if (pathValidity != null && pathValidity.getLength() == 1) {
						NodeList certValidity = XMLUtils.getNodes(pathValidity.item(0), CERTIFICATEVALIDITY_RES);
						if (certValidity != null) {
							boolean found = false;
							int j = 0;
							while (!found && j < certValidity.getLength()) {
								String sn = XMLUtils.getNodeValue(certValidity.item(j), X509SERIALNUMBER_RES);
								found = sn != null && sn.equals(serialNumber);
								j++;
							}
							if (found) {
								j--;
								String content = XMLUtils.getNodeValue(certValidity.item(j), CERTIFICATE_VALUE_RES);
								if (content != null) {
									signer.put(CERTIFICATE_VALUE_RES, content);
								}
							}
						}

					}
				}
				signers.add(signer);
			}
			if (!signers.isEmpty()) {
				outputs.put(IND_SIG_REPORT_RES, signers);
			}
			return;
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_013);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}

	/**
	 * Gets  the XML request as String. Previously removed nodes supplied.
	 * @param xml		Document for removing the nodes supplied and transform to String.
	 * @param nodeToRemove	List of XPath that locate the nodes to remove.
	 * @return			XML request as String.
	 * @throws TransformException	There was an error processing the XML.
	 */
	protected String getStringXML(Document xml, ArrayList<String> nodeToRemove) throws TransformException {
		String xmlStr = null;
		try {
			XMLUtils.removeNodes(xml, nodeToRemove);
		} catch (ToolsException e1) {
			String msg = Language.getMessage(LanguageKeys.TRAN_009);
			LOGGER.error(msg + e1.getMessage());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}

		try {
			xmlStr = new String(XMLUtils.getXMLBytes(xml));
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_008);
			LOGGER.error(msg + e.getMessage());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
		return xmlStr;
	}

	/**
	 * Gets a document element used as creation template.
	 * @return	Template as DOM document.
	 * @throws TransformException If an error occurs getting the template.
	 */
	protected Document getTemplate() throws TransformException {
		try {
			return XMLUtils.getDocument(FileSystemReader.getInstance().getDssVerifyTemplate());
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_006);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.TEMPLATE_ERROR, msg);
		}
	}

	/**
	 * Method that includes the document signed into XML request.
	 * @param xml		XML request.
	 * @param parameters	{@link LinkedHashMap} that contains the object to include into the request.
	 * @param nodeToRemove	{@link ArrayList} that contains the elements that must be removed of request.
	 * @throws TransformException	If   occurs an error.
	 */
	protected void fillDocument(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		try {

			if (parameters.containsKey(BASE64XML_REQ)) {
				String encodedDocument = parameters.get(BASE64XML_REQ);
				XMLUtils.includeElementValue(xml, BASE64XML_REQ, encodedDocument, Node.CDATA_SECTION_NODE);
				nodeToRemove.add(BASE64DATA_REQ);
				nodeToRemove.add(DOCUMENT_OTHER_REQ);
			} else if (parameters.containsKey(BASE64DATA_REQ)) {
				String encodedDocument = parameters.get(BASE64DATA_REQ);
				XMLUtils.includeElementValue(xml, BASE64DATA_REQ, encodedDocument, Node.CDATA_SECTION_NODE);
				nodeToRemove.add(BASE64XML_REQ);
				nodeToRemove.add(DOCUMENT_OTHER_REQ);
			} else {
				nodeToRemove.add(BASE64DATA_REQ);
				nodeToRemove.add(BASE64XML_REQ);
				nodeToRemove.add(DOCUMENT_OTHER_REQ);
			}
		} catch (ToolsException te) {
			String msg = Language.getMessage(LanguageKeys.TRAN_010);
			LOGGER.error(msg + te.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
		return;
	}

	/**
	 * Method that includes the signature into XML request.
	 * @param xml		XML request.
	 * @param parameters	{@link LinkedHashMap} that contains the object to include into the request.
	 * @param nodeToRemove	{@link ArrayList} that contains the elements that must be removed of request.
	 * @throws TransformException	If   occurs an error.
	 */
	protected void fillSignature(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		try {

			if (parameters.containsKey(B64SIGNATURE_REQ)) {
				String signatureEncoded = parameters.get(B64SIGNATURE_REQ);
				XMLUtils.includeElementValue(xml, B64SIGNATURE_REQ, signatureEncoded, Node.CDATA_SECTION_NODE);
				nodeToRemove.add(DOCUMENT_WITH_ID_REQ);
				nodeToRemove.add(SIGNATUREPTR_REQ);
				nodeToRemove.add(SIG_OTHER_REQ);
				nodeToRemove.add(SIG_XMLDSIG_REQ);
			} else if (parameters.containsKey(SIGNATUREPTR_REQ)) {
				String signatureEncoded = parameters.get(SIGNATUREPTR_REQ);
				String id = UniqueIdGenerator.generateStringUniqueId();
				XMLUtils.includeElementValue(xml, SIGNATUREPTR_ATT_REQ, id, Node.TEXT_NODE);
				XMLUtils.includeElementValue(xml, DOCUMENT_ATT_ID_REQ, id, Node.TEXT_NODE);
				XMLUtils.includeElementValue(xml, BASE64XML_WITH_ID_REQ, signatureEncoded, Node.CDATA_SECTION_NODE);
				nodeToRemove.add(B64SIGNATURE_REQ);
				nodeToRemove.add(SIG_XMLDSIG_REQ);
				nodeToRemove.add(SIG_OTHER_REQ);
			} else if (parameters.containsKey(SIG_XMLDSIG_REQ)) {
				String dsig = parameters.get(SIG_XMLDSIG_REQ);
				byte[] signature = new UtilsBase64().decode(dsig);
				Document xmlDsig = XMLUtils.getDocument(signature);
				XMLUtils.includeElementValue(xml, SIGNATURE_OBJECT_REQ, xmlDsig.getDocumentElement(), Node.ELEMENT_NODE);
				nodeToRemove.add(B64SIGNATURE_REQ);
				nodeToRemove.add(DOCUMENT_WITH_ID_REQ);
				nodeToRemove.add(SIGNATUREPTR_REQ);
				nodeToRemove.add(SIG_OTHER_REQ);
			} else {
				nodeToRemove.add(B64SIGNATURE_REQ);
				nodeToRemove.add(DOCUMENT_WITH_ID_REQ);
				nodeToRemove.add(SIGNATUREPTR_REQ);
				nodeToRemove.add(SIG_OTHER_REQ);
			}

		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_010);
			LOGGER.error(msg + e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
		return;
	}

	/**
	 * Method that includes the optional inputs into XML request.
	 * @param xml		XML request.
	 * @param parameters	{@link LinkedHashMap} that contains the object to include into the request.
	 * @param nodeToRemove	{@link ArrayList} that contains the elements that must be removed of request.
	 * @throws TransformException	If   occurs an error.
	 */
	protected void fillOptionalOutputs(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		try {
			if (parameters.containsKey(APP_NAME_REQ)) {
				String appId = parameters.get(APP_NAME_REQ);
				XMLUtils.includeElementValue(xml, APP_NAME_REQ, appId, Node.TEXT_NODE);
			} else {
				nodeToRemove.add(APP_NAME_REQ);
			}
			// Este optionalOutputs no est� soportado en la versi�n 5.3.1
			nodeToRemove.add(RETURNSIGNEDDATAINFO_REQ);
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_010);
			LOGGER.error(msg + e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
		return;
	}

	/**
	 * This method include the content of 'dss:Result' elements into the Map supplied.
	 * @param result	'dss:Result' element.
	 * @param outputs	Map to include the content of 'dss:Result'.
	 * @throws ToolsException There was an error processing the XML.
	 */
	private void readResult(Node result, LinkedHashMap<String, Object> outputs) throws ToolsException {
		String major = XMLUtils.getNodeValue(result, RESULT_MAJOR_RES);
		if (major != null) {
			outputs.put(RESULT_MAJOR_RES, major);
		}
		String minor = XMLUtils.getNodeValue(result, RESULT_MINOR_RES);
		if (minor != null) {
			outputs.put(RESULT_MINOR_RES, minor);
		}
		String message = XMLUtils.getNodeValue(result, RESULT_MSG_RES);
		if (message != null) {
			outputs.put(RESULT_MSG_RES, message);
		}
	}

	/**
	 * This method include the certificate information associated to signer certificate into the Map supplied.
	 * @param item	'afxp:ReadableCertificateInfo' element.
	 * @param signer	Map to include.
	 */
	private void readCertificateInfo(Node item, LinkedHashMap<String, Object> signer) {
		NodeList readableFields = item.getChildNodes();
		LinkedHashMap<String, String> certInfo = new LinkedHashMap<String, String>();
		for (int i = 0; i < readableFields.getLength(); i++) {
			if (readableFields.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node readField = readableFields.item(i);
				String fieldId = null;
				String fieldValue = null;
				NodeList childs = readField.getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node fieldNode = childs.item(j);
					if (fieldNode.getNodeType() == Node.ELEMENT_NODE && fieldNode.getFirstChild()!=null) {
						if (fieldNode.getLocalName().equals(FIELDIDENTITY_NAME)) {
							fieldId = fieldNode.getFirstChild().getNodeValue();
						} else if (fieldNode.getLocalName().equals(FIELDVALUE_NAME)) {
							fieldValue = fieldNode.getFirstChild().getNodeValue();
						}
					}
				}
				if (fieldId != null && fieldValue != null) {
					certInfo.put(fieldId, fieldValue);
				}
			}
		}
		if (!certInfo.isEmpty()) {
			signer.put(CERT_INFO_RES, certInfo);
		}
	}

}
