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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.GenerationReportv1d0.java.</p>
 * <b>Description:</b><p> Class to read a XML request and create a XML response as specified by the generation report service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>31/03/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 31/03/2011.
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
import es.gob.signaturereport.tools.XMLUtils;

/**
 * <p>Class to read a XML request and create a XML response as specified by the generation report service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 31/03/2011.
 */
public class GenerationReportv1d0 implements GenerationReportTransformI {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(GenerationReportv1d0.class);

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#marshal(java.util.LinkedHashMap)
	 */
	public String marshal(LinkedHashMap<String, String> parameters) throws TransformException {
		Document doc = getTemplate();

		try {
			String code = parameters.remove(RESULTCODE);
			if (code != null) {
				XMLUtils.includeElementValue(doc, RESULTCODE, code, Node.TEXT_NODE);
			}
			String message = parameters.remove(RESULTMESSAGE);
			if(message != null){
				XMLUtils.includeElementValue(doc, RESULTMESSAGE, message, Node.TEXT_NODE);
			}
			String encodedReport = parameters.remove(REPORT);
			if(encodedReport != null){
				XMLUtils.includeElementValue(doc, REPORT, encodedReport, Node.TEXT_NODE);
			}else{
				ArrayList<String> toRemove = new ArrayList<String>();
				toRemove.add(REPORT);
				XMLUtils.removeNodes(doc, toRemove);
			}
			return new String(XMLUtils.getXMLBytes(doc),"UTF-8");
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_008);
			LOGGER.error(msg,e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#unmarshal(java.lang.String)
	 */
	public LinkedHashMap<String, Object> unmarshal(String xml) throws TransformException {
		try {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			Document doc = XMLUtils.getDocument(xml.getBytes("UTF-8"));

			String appId = XMLUtils.getNodeValue(doc.getDocumentElement(), APPLICATIONID);
			if (appId != null) {
				response.put(APPLICATIONID, appId);
			}

			String templateId = XMLUtils.getNodeValue(doc.getDocumentElement(), TEMPLATEID);
			if (templateId != null) {
				response.put(TEMPLATEID, templateId);
			}

			NodeList signatureList = XMLUtils.getNodes(doc.getDocumentElement(), SIGNATURE);

			if (signatureList != null && signatureList.getLength() == 1) {
				LinkedHashMap<String, String> signature = new LinkedHashMap<String, String>();
				String encodedSignature = XMLUtils.getNodeValue(signatureList.item(0), ENCODEDSIGNATURE);
				if (encodedSignature != null) {
					signature.put(ENCODEDSIGNATURE, encodedSignature);
				} else {
					String verifyRes = XMLUtils.getNodeValue(signatureList.item(0), VALIDATIONRESPONSE);
					if (verifyRes != null) {
						signature.put(VALIDATIONRESPONSE, verifyRes);
					} else {
						NodeList signatureChild = XMLUtils.getNodes(signatureList.item(0), REPOSITORYLOCATION);
						if (signatureChild != null && signatureChild.getLength() == 1) {
							String repositoryId = XMLUtils.getNodeValue(signatureChild.item(0), REPOSITORYID);
							String objectId = XMLUtils.getNodeValue(signatureChild.item(0), OBJECTID);
							if (repositoryId != null && objectId != null) {
								signature.put(REPOSITORYID, repositoryId);
								signature.put(OBJECTID, objectId);
							}
						}
					}
				}
				if (!signature.isEmpty()) {
					response.put(SIGNATURE, signature);
				}

			}

			NodeList documentList = XMLUtils.getNodes(doc.getDocumentElement(), DOCUMENT);

			if (documentList != null && documentList.getLength() == 1) {
				LinkedHashMap<String, String> document = new LinkedHashMap<String, String>();
				String encodedDocument = XMLUtils.getNodeValue(documentList.item(0), ENCODEDDOCUMENT);
				if (encodedDocument != null) {
					document.put(ENCODEDDOCUMENT, encodedDocument);
				} else {
					NodeList documentChild = XMLUtils.getNodes(documentList.item(0), REPOSITORYLOCATION);
					if (documentChild != null && documentChild.getLength() == 1) {
						String repositoryId = XMLUtils.getNodeValue(documentChild.item(0), REPOSITORYID);
						String objectId = XMLUtils.getNodeValue(documentChild.item(0), OBJECTID);
						if (repositoryId != null && objectId != null) {
							document.put(REPOSITORYID, repositoryId);
							document.put(OBJECTID, objectId);
						}
					}

				}
				if (!document.isEmpty()) {
					response.put(DOCUMENT, document);
				}

			}

			String includeSignature = XMLUtils.getNodeValue(doc.getDocumentElement(), INCLUDESIGNATURE);
			if (includeSignature != null) {
				response.put(INCLUDESIGNATURE, includeSignature);
			}

			NodeList barcodesList = XMLUtils.getNodes(doc.getDocumentElement(), BARCODE);
			if (barcodesList != null && barcodesList.getLength() > 0) {
				ArrayList<LinkedHashMap<String, Object>> barcodes = new ArrayList<LinkedHashMap<String, Object>>();
				for (int i = 0; i < barcodesList.getLength(); i++) {
					LinkedHashMap<String, Object> barcode = new LinkedHashMap<String, Object>();
					String type = XMLUtils.getNodeValue(barcodesList.item(i), TYPE);
					String msg = XMLUtils.getNodeValue(barcodesList.item(i), MESSAGE);
					if (type != null && msg != null) {
						barcode.put(TYPE, type);
						barcode.put(MESSAGE, msg);
					}
					NodeList confList = XMLUtils.getNodes(barcodesList.item(i), CONFIGURATIONPARAMS);
					if (confList != null && confList.getLength() > 0) {
						LinkedHashMap<String, String> confParams = new LinkedHashMap<String, String>();
						for (int j = 0; j < confList.getLength(); j++) {
							String paramId = XMLUtils.getNodeValue(confList.item(j), PARAMETERID);
							String paramValue = XMLUtils.getNodeValue(confList.item(j), PARAMETERVALUE);
							if (paramId != null && paramValue != null) {
								confParams.put(paramId, paramValue);
							}
						}
						if (!confParams.isEmpty()) {
							barcode.put(CONFIGURATIONPARAMS, confParams);
						}
					}
					if (!barcode.isEmpty()) {
						barcodes.add(barcode);
					}
				}
				if (!barcodes.isEmpty()) {
					response.put(BARCODE, barcodes);
				}
			}

			NodeList extParamsList = XMLUtils.getNodes(doc.getDocumentElement(), EXTERNALPARAMS);
			if (extParamsList != null && extParamsList.getLength() > 0) {
				LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
				for (int i = 0; i < extParamsList.getLength(); i++) {
					String paramId = XMLUtils.getNodeValue(extParamsList.item(i), PARAMETERID);
					String paramValue = XMLUtils.getNodeValue(extParamsList.item(i), PARAMETERVALUE);
					if (paramId != null && paramValue != null) {
						params.put(paramId, paramValue);
					}
				}
				if (!params.isEmpty()) {
					response.put(EXTERNALPARAMS, params);
				}
			}
			return response;
		} catch (ToolsException e) {
			LOGGER.error(e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, e.getDescription());
		} catch (UnsupportedEncodingException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_014);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}

	}

	/**
	 * Gets a document element used as creation template.
	 * @return	Template as DOM document.
	 * @throws TransformException If an error occurs getting the template.
	 */
	protected Document getTemplate() throws TransformException {
		try {
			return XMLUtils.getDocument(FileSystemReader.getInstance().getGenerationTemplate());
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_006);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.TEMPLATE_ERROR, msg);
		}
	}

}
