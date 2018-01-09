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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.SignatureManager.java.</p>
 * <b>Description:</b><p>Class that manages the validation and generation of electronic signature.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/02/2011.
 */
package es.gob.signaturereport.mfirma;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.TransformFactory;
import es.gob.signaturereport.messages.transform.TransformI;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI;
import es.gob.signaturereport.mfirma.generators.SignatureGeneratorFactory;
import es.gob.signaturereport.mfirma.generators.SignatureGeneratorI;
import es.gob.signaturereport.mfirma.invoker.ServiceInvokerException;
import es.gob.signaturereport.mfirma.invoker.ServiceInvokerFactory;
import es.gob.signaturereport.mfirma.invoker.ServiceInvokerI;
import es.gob.signaturereport.mfirma.responses.SignedDocument;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.AfirmaResponse;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.FileUtils;
import es.gob.signaturereport.tools.SignatureUtils;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.UtilsBase64;
import es.gob.signaturereport.tools.XMLUtils;

/** 
 * <p>Class that manages the validation and generation of electronic signature.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/02/2011.
 */
public class SignatureManager {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(SignatureManager.class);

	/**
	 * Attribute that represents the location of verify response into the SOAP response. 
	 */
	private static final String RESPONSE_LOCATION = "/*[local-name()='Envelope']/*[local-name()='Body']/*/*";

	/**
	 * Attribute that represents the key used to sign. 
	 */
	private static PrivateKey key = null;

	/**
	 * Attribute that represents the certificate used to sign. 
	 */
	private static X509Certificate signerCertificate = null;

	/**
	 * Attribute that represents the namespaces of the ignored XML elements. 
	 */
	private static ArrayList<String> ignoredNS = new ArrayList<String>();

	/**
	 * Attribute that represents the class for encoding/decoding in Base64. 
	 */
	private UtilsBase64 base64 = new UtilsBase64();
		
	static {

		// Obtenemos la claves para la firma.
		FileInputStream in = null;
		try {
			KeyStore keystore = KeyStore.getInstance(StaticSignatureReportProperties.getProperty("signaturereport.global.sign.keystore.type"));
			in = new FileInputStream(StaticSignatureReportProperties.getProperty("signaturereport.global.sign.keystore"));
			char[ ] password = StaticSignatureReportProperties.getProperty("signaturereport.global.sign.keystore.password").toCharArray();
			String alias = StaticSignatureReportProperties.getProperty("signaturereport.global.sign.keystore.alias");
			keystore.load(in, password);
			signerCertificate = (X509Certificate) keystore.getCertificate(alias);
			key = (PrivateKey) keystore.getKey(alias, password);
			int i = 1;
			String ns = StaticSignatureReportProperties.getProperty("signaturereport.global.excludeNS." + i);
			while (ns != null && ns.trim().length() > 0) {
				ignoredNS.add(ns);
				i++;
				ns = StaticSignatureReportProperties.getProperty("signaturereport.global.excludeNS." + i);
			}
		} catch (Exception e) {
			LOGGER.error(Language.getMessage(LanguageKeys.SM_029), e);
		}
	}

	/**
	 * Constructor method for the class SignatureManager.java. 
	 */
	public SignatureManager() {

	}

	/**
	 * This method checks the status of a electronic signature by interaction with the <b>@firma</b> platform.
	 * @param platformId	Identifier that represents a "@firma configuration".
	 * @param signature		Digital signature that to be checked.
	 * @param uuid		Unique identifier that represents a signature located into a external repository.
	 * @param repositoryId	Identifier of external repository that contains the signature.
	 * @param document		Document signed.	
	 * @param uuidDocument	Unique identifier that represents a document located into a external repository.
	 * @param repIdDocument	Identifier of external repository that contains the document.	
	 * @return			Results of validation process.
	 * @throws SignatureManagerException 	If an error occurs.
	 */
	public ValidationSignatureResponse validateSignature(String platformId, byte[ ] signature, String uuid, String repositoryId, byte[ ] document, String uuidDocument, String repIdDocument) throws SignatureManagerException {

		// Obtenemos la configuración de la plataforma @firma
		AfirmaData afirmaConf = getPlatformConfiguration(platformId);
		// Una petición de validación de firma en gestor documental solo esta
		// disponible a partir de la versión 5.5 de @firma
		if (uuid != null && repositoryId != null && (afirmaConf.getVersion().equals(AfirmaConfigurationFacadeI.VERSION_5_3_1) || afirmaConf.getVersion().equals(AfirmaConfigurationFacadeI.VERSION_5_4))) {
			throw new SignatureManagerException(SignatureManagerException.NOT_SUPPORTED, Language.getFormatMessage(LanguageKeys.SM_004, new Object[ ] { afirmaConf.getVersion() }));
		}
		String version = afirmaConf.getVersion();
		String xmlRequest = null;
		TransformI transform = null;
		try {
			transform = TransformFactory.getTransform(ConfigurationFacade.VALIDATE_SIGNATURE_SERVICE, version, AfirmaConfigurationFacadeI.AFIRMA_PLATFORM);
			LinkedHashMap<String, String> parameters = getVerifyParameters(afirmaConf.getApplicationId(), signature, uuid, repositoryId, document, uuidDocument, repIdDocument);
			xmlRequest = transform.marshal(parameters);
		} catch (TransformException e) {
			String msg = Language.getMessage(LanguageKeys.SM_008);
			LOGGER.error(msg, e);
			throw new SignatureManagerException(SignatureManagerException.XML_REQUEST_ERROR, msg, e);
		}

		if (xmlRequest == null) {
			String msg = Language.getMessage(LanguageKeys.SM_009);
			LOGGER.error(msg);
			throw new SignatureManagerException(SignatureManagerException.XML_REQUEST_ERROR, msg);
		}
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.SM_010, new String[ ] { xmlRequest }));

		// Invocación WS
		String xmlResponse = null;
		try {
			ServiceInvokerI serviceInvoker = ServiceInvokerFactory.getServiceInvoker(afirmaConf);
			xmlResponse = (String) serviceInvoker.invokeService(ConfigurationFacade.VALIDATE_SIGNATURE_SERVICE, new Object[ ] { xmlRequest });
		} catch (ServiceInvokerException e) {
			String msg = Language.getMessage(LanguageKeys.SM_011);
			LOGGER.error(msg, e);
			throw new SignatureManagerException(SignatureManagerException.AFIRMA_INVOKER_ERROR, msg, e);
		}

		if (xmlResponse == null) {
			String msg = Language.getMessage(LanguageKeys.SM_012);
			LOGGER.error(msg);
			throw new SignatureManagerException(SignatureManagerException.AFIRMA_INVOKER_ERROR, msg);
		}
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.SM_013, new String[ ] { xmlResponse }));

		try {
			LinkedHashMap<String, Object> resTrans = transform.unmarshal(xmlResponse);
			List<SignedDocument> signedDocuments = null;
			if (resTrans.containsKey(DSSVerifyTransformI.CONTENT_DATA_RES)) {
				signedDocuments = new ArrayList<SignedDocument>();
				String encodedSigDocument = (String) resTrans.remove(DSSVerifyTransformI.CONTENT_DATA_RES);
				byte[ ] sigDocument = base64.decode(encodedSigDocument);
				try {
					String mimeType = FileUtils.getMediaType(sigDocument);
					String extension = FileUtils.getFileExtension(mimeType);
					signedDocuments.add(new SignedDocument(sigDocument, mimeType, extension));
				} catch (ToolsException e) {
					String msg = Language.getMessage(LanguageKeys.SM_033);
					LOGGER.error(msg + e.getMessage());
					throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
				}
			} else if (resTrans.containsKey(DSSVerifyTransformI.REFS_XPATH_RES)) {
				List<String> xpathList = (List<String>) resTrans.get(DSSVerifyTransformI.REFS_XPATH_RES);
				if (!xpathList.isEmpty()) {
					try {
						signedDocuments = extractDocumentsFromXMLSignature(signature, xpathList);
					} catch (ToolsException e) {
						String msg = Language.getMessage(LanguageKeys.SM_033);
						LOGGER.error(msg + e.getMessage());
						throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
					}
				}

			}
			return new ValidationSignatureResponse(xmlResponse, resTrans, signedDocuments);
		} catch (TransformException e) {
			String msg = Language.getMessage(LanguageKeys.SM_014);
			LOGGER.error(msg, e);
			throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
		}

	}

	/**
	 * This method processes a validation response of  '@firma' to extract the information contained in the message supplied.
	 * @param platformId	Identifier of '@firma' platform.
	 * @param response		Web service response returned by '@firma' platform.
	 * @return			{@link ValidationSignatureResponse} that contains the verification information.
	 * @throws SignatureManagerException	If an error occurs while reading response.
	 */
	public ValidationSignatureResponse readValidateResponse(String platformId, AfirmaResponse response) throws SignatureManagerException {
		String xmlResponse = null;
		// Obtenemos la configuraci�n de la plataforma @firma
		AfirmaData afirmaConf = getPlatformConfiguration(platformId);
		if (response.getSoapResponse() != null) {
			String certAlias = afirmaConf.getSoapTrusted();
			X509Certificate cert = null;
			try {
				cert = ConfigurationFacade.getInstance().getCertificate(ConfigurationFacade.SOAP_TRUSTED_KEYSTORE, certAlias);
			} catch (ConfigurationException e) {
				String msg = Language.getFormatMessage(LanguageKeys.SM_016, new Object[ ] { certAlias });
				LOGGER.error(msg, e);
				throw new SignatureManagerException(SignatureManagerException.UNKNOWN_ERROR, msg, e);
			}

			if (cert == null) {
				String msg = Language.getFormatMessage(LanguageKeys.SM_016, new Object[ ] { certAlias });
				LOGGER.error(msg);
				throw new SignatureManagerException(SignatureManagerException.UNKNOWN_ERROR, msg);
			} else {
				validSOAPSignature(response.getSoapResponse(), cert);
			}
			xmlResponse = extractVerifyResponse(response.getSoapResponse());
		}

		if (xmlResponse == null) {
			xmlResponse = response.getXmlResponse();
		}

		if (xmlResponse != null) {
			try {
				TransformI transform = TransformFactory.getTransform(ConfigurationFacade.VALIDATE_SIGNATURE_SERVICE, afirmaConf.getVersion(), AfirmaConfigurationFacadeI.AFIRMA_PLATFORM);
				LinkedHashMap<String, Object> responseMap = transform.unmarshal(xmlResponse);
				ArrayList<SignedDocument> signedDocuments = null;
				if (responseMap.containsKey(DSSVerifyTransformI.CONTENT_DATA_RES)) {
					signedDocuments = new ArrayList<SignedDocument>();
					String encodedSigDocument = (String) responseMap.remove(DSSVerifyTransformI.CONTENT_DATA_RES);
					byte[ ] sigDocument = base64.decode(encodedSigDocument);
					try {
						String mimeType = FileUtils.getMediaType(sigDocument);
						String extension = FileUtils.getFileExtension(mimeType);
						signedDocuments.add(new SignedDocument(sigDocument, mimeType, extension));
					} catch (ToolsException e) {
						String msg = Language.getMessage(LanguageKeys.SM_033);
						LOGGER.error(msg + e.getMessage());
						throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
					}
				}
				if (responseMap.isEmpty()) {
					String msg = Language.getMessage(LanguageKeys.SM_019);
					LOGGER.error(msg);
					throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg);
				}
				return new ValidationSignatureResponse(xmlResponse, responseMap);
			} catch (TransformException e) {
				String msg = Language.getMessage(LanguageKeys.SM_014);
				LOGGER.error(msg, e);
				throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
			}
		} else {
			String msg = Language.getMessage(LanguageKeys.SM_017);
			LOGGER.error(msg);
			throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg);
		}
	}

	/**
	 * Signs the document supplied. 
	 * @param document		Document to be signed.
	 * @param signatureFormat	Signature format.
	 * @return	Electronic signature.
	 * @throws SignatureManagerException	If an error occurs.
	 */
	public byte[ ] serverSign(byte[ ] document, String signatureFormat) throws SignatureManagerException {
		if (document == null || signatureFormat == null) {
			String msg = Language.getMessage(LanguageKeys.SM_020);
			LOGGER.error(msg);
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, msg);
		}
		SignatureGeneratorI sg = SignatureGeneratorFactory.getSignatureGenerator(signatureFormat);
		return sg.generateSignature(document, key, signerCertificate);
	}

	/**
	 * This method provides the configuration of "@firma platform".
	 * @param platformId	Identifier of "@firma platform".
	 * @return		Information of configuration associated a "@firma platform".
	 * @throws SignatureManagerException If an error occurs.
	 */
	private AfirmaData getPlatformConfiguration(String platformId) throws SignatureManagerException {
		AfirmaData data = null;
		try {
			data = ConfigurationFacade.getInstance().getAfirmaConfiguration(platformId);
		} catch (ConfigurationException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.SM_001, new Object[ ] { platformId }) + e.getMessage());
			if (e.getCode() == ConfigurationException.ITEM_NO_FOUND) {
				throw new SignatureManagerException(SignatureManagerException.INVALID_PLATFORM_ID, Language.getFormatMessage(LanguageKeys.SM_002, new Object[ ] { platformId }), e);
			}
		}
		if (data == null) {
			throw new SignatureManagerException(SignatureManagerException.UNKNOWN_ERROR, Language.getFormatMessage(LanguageKeys.SM_003, new Object[ ] { platformId }));
		}
		return data;
	}

	/**
	 * Gets the input parameters for creating XML request of verification signature. 
	 * @param applicationId	Application identifier.
	 * @param signature		Digital signature that to be checked.
	 * @param uuid		Unique identifier that represents a signature located into a external repository.
	 * @param repositoryId	Identifier of external repository that contains the signature.
	 * @param document		Document signed.
	 * @param uuidDocument	Unique identifier that represents a document located into a external repository.
	 * @param repIdDocument	Identifier of external repository that contains the document.			
	 * @return			{@link LinkedHashMap} that contains the parameters.
	 * @throws SignatureManagerException	If an error occurs while obtaining the parameters.
	 */
	private LinkedHashMap<String, String> getVerifyParameters(String applicationId, byte[ ] signature, String uuid, String repositoryId, byte[ ] document, String uuidDocument, String repIdDocument) throws SignatureManagerException {
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		// Identificador de aplicaci�n
		parameters.put(DSSVerifyTransformI.APP_NAME_REQ, applicationId);
		// Firma electr�nica
		if (signature != null) {
			String sigParamKey = null;
			try {
				int type = SignatureUtils.getSignatureType(signature);
				if (type == SignatureUtils.XML_ENVELOPING_SIG) {
					sigParamKey = DSSVerifyTransformI.SIG_XMLDSIG_REQ;
				} else if (type == SignatureUtils.XML_ENV_DET_SIG) {
					sigParamKey = DSSVerifyTransformI.SIGNATUREPTR_REQ;
				} else {
					sigParamKey = DSSVerifyTransformI.B64SIGNATURE_REQ;
				}
			} catch (ToolsException e) {
				String msg = Language.getMessage(LanguageKeys.SM_005);
				LOGGER.error(msg + e.getDescription());
				throw new SignatureManagerException(SignatureManagerException.SIGNATURE_PROCESS_ERROR, msg, e);
			}
			String encodedSig = base64.encodeBytes(signature);
			parameters.put(sigParamKey, encodedSig);
		} else {
			if (uuid != null && repositoryId != null) {
				parameters.put(DSSVerifyTransformI.SIG_OBJECTID_REQ, uuid);
				parameters.put(DSSVerifyTransformI.SIG_REPOSITORYID_REQ, repositoryId);
			} else {
				String msg = Language.getMessage(LanguageKeys.SM_006);
				LOGGER.error(msg);
				throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, msg);
			}
		}

		// Documento firmado
		if (document != null) {
			try {
				boolean isXml = FileUtils.isXML(document);
				if (isXml) {
					parameters.put(DSSVerifyTransformI.BASE64XML_REQ, base64.encodeBytes(document));
				} else {
					parameters.put(DSSVerifyTransformI.BASE64DATA_REQ, base64.encodeBytes(document));
				}
			} catch (ToolsException e) {
				String msg = Language.getMessage(LanguageKeys.SM_007);
				LOGGER.error(msg + e.getDescription());
				throw new SignatureManagerException(SignatureManagerException.DOCUMENT_PROCESS_ERROR, msg, e);

			}

		} else {
			if (uuidDocument != null && repIdDocument != null) {
				parameters.put(DSSVerifyTransformI.DOCUMENT_UUID_REQ, uuid);
				parameters.put(DSSVerifyTransformI.DOCUMENT_REPOSITORYID_REQ, repositoryId);
			}
		}
		return parameters;
	}

	/**
	 * Validates the signature included into SOAP response.
	 * @param signature		SOAP response to validate.
	 * @param cert		CertificateItem of signer.
	 * @throws SignatureManagerException	If the signature is not valid or other error.
	 */
	private void validSOAPSignature(byte[ ] signature, X509Certificate cert) throws SignatureManagerException {
		boolean validSig = false;
		// Proceso de validaci�n de la firma SOAP
		try {
			validSig = SignatureUtils.verifyXMLSignature(signature, cert);
		} catch (ToolsException e) {
			if (e.getCode() == ToolsException.INVALID_SIGNATURE) {
				String msg = Language.getMessage(LanguageKeys.SM_015);
				LOGGER.error(msg, e);
				throw new SignatureManagerException(SignatureManagerException.INVALID_SOAP_SIGNATURE, msg, e);
			} else {
				String msg = Language.getMessage(LanguageKeys.SM_017);
				LOGGER.error(msg, e);
				throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
			}

		}
		if (!validSig) {
			String msg = Language.getMessage(LanguageKeys.SM_015);
			LOGGER.error(msg);
			throw new SignatureManagerException(SignatureManagerException.INVALID_SOAP_SIGNATURE, msg);
		}
	}

	/**
	 * Extracts the verification response from the SOAP response supplied.
	 * @param soap	SOAP response to search the verification response.
	 * @return		Verification response.
	 * @throws SignatureManagerException	If an error occurs while processing of the SOAP message.
	 */
	private String extractVerifyResponse(byte[ ] soap) throws SignatureManagerException {
		try {
			Document doc = XMLUtils.getDocument(soap);
			return XMLUtils.getNodeValue(doc.getDocumentElement(), RESPONSE_LOCATION);
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.SM_017);
			LOGGER.error(msg, e);
			throw new SignatureManagerException(SignatureManagerException.XML_RESPONSE_ERROR, msg, e);
		}
	}

	/**
	 * Gets the documents included into a signature from the specified xpath.
	 * @param signature	Signature.
	 * @param xpathList	Xpath location.
	 * @return	List of documents.
	 * @throws ToolsException	If an error occurs while the XML is processed.
	 */
	private List<SignedDocument> extractDocumentsFromXMLSignature(byte[ ] signature, List<String> xpathList) throws ToolsException {
		ArrayList<SignedDocument> documents = new ArrayList<SignedDocument>();

		Document sigDoc = XMLUtils.getDocument(signature);
		for (int i = 0; i < xpathList.size(); i++) {
			String xpath = xpathList.get(i);
			// La plataforma de firma en las versiones anteriores a la 5.5_004 y
			// 5.6 no generaba correctamente las expresiones Xpath
			xpath = verifyXpathExpression(xpath);
			LOGGER.info(Language.getFormatMessage(LanguageKeys.SM_034, new Object[ ] { xpath }));
			if (xpath.equals("/")) {
				// Firma enveloped
				SignedDocument sigDocument = new SignedDocument(signature, FileUtils.XML_MEDIA_TYPE, FileUtils.getFileExtension(FileUtils.XML_MEDIA_TYPE));
				documents.add(sigDocument);
				LOGGER.info(Language.getFormatMessage(LanguageKeys.SM_035, new Object[ ] { xpath }));
			} else {
				NodeList nl = XMLUtils.getNodes(sigDoc.getDocumentElement(), xpath);
				if (nl != null && nl.getLength() > 0) {
					Node signedNode = nl.item(0);
					byte[ ] signedValue = null;
					if (signedNode.getNamespaceURI() == null || signedNode.getNamespaceURI().equals("") || !ignoredNS.contains(signedNode.getNamespaceURI())) {
						// Cualquier elemento fuera de la firma
						Node child = signedNode.getFirstChild();
						if (child != null && (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE)) {
							signedValue = child.getNodeValue().getBytes();
						} else {
							signedValue = XMLUtils.getXMLBytes(child);
						}
					} else if (signedNode.getLocalName().equals("Object") && SignatureUtils.XML_SIG_NS.equals(signedNode.getNamespaceURI())) {
						// Elemento ds:Object
						Node child = signedNode.getFirstChild();
						if (child != null && (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE)) {
							signedValue = child.getNodeValue().getBytes();
						} else {
							if (child.getNamespaceURI() == null || child.getNamespaceURI().equals("") || !ignoredNS.contains(child.getNamespaceURI())) {
								signedValue = XMLUtils.getXMLBytes(child);
							}

						}
					}
					if (signedValue != null) {
						boolean isB64 = base64.isBase64(new String(signedValue));
						byte[ ] content = null;
						if (isB64) {
							content = base64.decode(signedValue, 0, signedValue.length);
						} else {
							content = signedValue;
						}
						String mime = FileUtils.getMediaType(content);
						String extension = FileUtils.getFileExtension(mime);
						documents.add(new SignedDocument(content, mime, extension));
						LOGGER.info(Language.getFormatMessage(LanguageKeys.SM_035, new Object[ ] { xpath }));
					}

				}
			}

		}

		return documents;
	}

	/**
	 * Check the XPath expression.
	 * @param xpath	XPath expression.
	 * @return	Corrected XPath expression.
	 */
	private String verifyXpathExpression(String xpath) {
		// La plataforma de firma en las versiones anteriores a la 5.5_004 y 5.6
		// no generaba correctamente las expresiones Xpath
		String validXpath = null;
		if (xpath.equals("//")) {
			validXpath = "/";
		} else {
			int startIndex = xpath.indexOf("//[@Id=");
			if (startIndex == 0) {
				String id = xpath.substring(startIndex + 7, xpath.indexOf("]"));
				validXpath = "//*[@Id='" + id + "']";
			} else {
				validXpath = xpath;
			}

		}
		return validXpath;
	}

}
