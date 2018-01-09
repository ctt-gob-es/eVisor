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
 * <b>File:</b><p>es.gob.signaturereport.tools.SignatureUtils.java.</p>
 * <b>Description:</b><p>Utility class for managing electronic signature.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>14/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 14/02/2011.
 */
package es.gob.signaturereport.tools;

import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <b>File:</b><p>es.gob.signaturereport.tools.SignatureUtils.java.</p>
 * <p>Utility class for managing electronic signature.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 14/02/2011.
 */
public final class SignatureUtils {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(SignatureUtils.class);

	/**
	 * Attribute that represents a signature that is not XML. 
	 */
	public static final int BINARY_SIG = 0;

	/**
	 * Attribute that represents a signature that is enveloping XML Signature. 
	 */
	public static final int XML_ENVELOPING_SIG = 1;

	/**
	 * Attribute that represents a signature that is enveloped or detached XML Signature. 
	 */
	public static final int XML_ENV_DET_SIG = 2;

	/**
	 * Attribute that represents the local name of XML Signature Element. 
	 */
	private static final String XML_SIG_ELEMENT = "Signature";

	/**
	 * Attribute that represents the name space of XML Signature specification. 
	 */
	public static final String XML_SIG_NS = "http://www.w3.org/2000/09/xmldsig#";

	/**
	 * Attribute that represents the extension used for the Binary Signatures. 
	 */
	private static final String ASN_SIGNATURE_EXTENSION = "p7s";

	static {
		org.apache.xml.security.Init.init();
	}

	/**
	 * Constructor method for the class SignatureUtils.java. 
	 */
	private SignatureUtils() {
		super();
	}

	/**
	 * Gets the signature file extension.
	 * @param signature	Signature.
	 * @return	File extension.
	 * @throws ToolsException	If an error occurs.
	 */
	public static String getSignatureFileExtension(byte[] signature) throws ToolsException{
		String extension =  FileUtils.getFileExtension(signature);
		if(extension==null || extension.equals(FileUtils.DATA_FILE_EXTENSION)){
			return ASN_SIGNATURE_EXTENSION;
		}else{
			return extension;
		}
	}
	/**
	 * Gets the signature type.
	 * @param signature 	Electronic signature.
	 * @return	Signature Type. The values returned are:
	 * <br/> {@link SignatureUtils#BINARY_SIG} - If the signature is not XML.
	 * <br/> {@link SignatureUtils#XML_ENVELOPING_SIG} - If the signature is enveloping XML Signature.
	 * <br/> {@link SignatureUtils#XML_ENV_DET_SIG} - If the signature is detached o enveloped XML Signature.
	 * @throws ToolsException If an error occurs while processing the signature.
	 */
	public static int getSignatureType(byte[ ] signature) throws ToolsException {
		boolean isXml = FileUtils.isXML(signature);
		if (isXml) {
			Document xml = XMLUtils.getDocument(signature);
			Node node = xml.getDocumentElement();
			if (node.getLocalName().equals(XML_SIG_ELEMENT) && node.getNamespaceURI().equals(XML_SIG_NS)) {
				return XML_ENVELOPING_SIG;
			} else {
				return XML_ENV_DET_SIG;
			}
		} else {
			return BINARY_SIG;
		}
	}

	/**
	 * Check the XML Signature status.
	 * @param signature		XML that contains one or more XML Signature element.
	 * @param cert		Signer CertificateItem.
	 * @return			True if the signature is valid.
	 * @throws ToolsException	If an error occurs while processing the signature.
	 */
	public static boolean verifyXMLSignature(byte[ ] signature, X509Certificate cert) throws ToolsException {
		Document xml = XMLUtils.getDocument(signature);
		if (xml == null) {
			return false;
		}
		NodeList list = xml.getElementsByTagNameNS(XML_SIG_NS, XML_SIG_ELEMENT);
		if (list == null || list.getLength() == 0) {
			return false;
		}
		boolean ok = true;
		int i = 0;
		while (ok && i < list.getLength()) {
			Element element = (Element) list.item(i);
			try {
				XMLSignature xmlSig = new XMLSignature(element, "");
				ok = xmlSig.checkSignatureValue(cert.getPublicKey());
			} catch (XMLSignatureException e) {
				String msg = Language.getMessage(LanguageKeys.UTIL_006);
				LOGGER.error(msg, e);
				throw new ToolsException(ToolsException.INVALID_SIGNATURE, msg,e);
			} catch (XMLSecurityException e) {
				String msg = Language.getMessage(LanguageKeys.UTIL_006);
				LOGGER.error(msg, e);
				throw new ToolsException(ToolsException.INVALID_SIGNATURE, msg,e);
			}
			i++;
		}
		return ok;
	}

}
