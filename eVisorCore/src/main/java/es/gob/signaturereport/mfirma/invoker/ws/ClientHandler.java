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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.invoker.ws.ClientHandler.java.</p>
 * <b>Description:</b><p> Class responsible for securing SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>16/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 16/02/2011.
 */
package es.gob.signaturereport.mfirma.invoker.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.handlers.BasicHandler;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <p>Class responsible for securing SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 16/02/2011.
 */

public class ClientHandler extends BasicHandler {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class);

	/**
	* Attribute that represents serial version of class. 
	*/
	private static final long serialVersionUID = -6858558124862409466L;

	/**
	 * Attribute that indicates the authorization type of the web service request. 
	 */
	private int secOption = ConfigurationFacade.WITHOUT_AUTHENTICATION;

	/**
	 * Attribute that represents the type of keystore that contains the certificate to sign the SOAP request. 
	 */
	private String keystoreType = null;

	/**
	 * Attribute that represents the password of keystore that contains the certificate to sign the SOAP request. 
	 */
	private String keystorePassword = null;

	/**
	 * Attribute that represents the alias of certificate to sign the SOAP request. 
	 */
	private String keystoreAlias = null;

	/**
	 * Attribute that represents the password associated to the alias of certificate to sign the SOAP request. 
	 */
	private String aliasPassword = null;

	/**
	 * Attribute that represents the path to keystore that contains the certificate to sign the SOAP request. 
	 */
	private String keystorePath = null;

	/**
	 * Attribute that represents the user for including into security token 'UserNameToken'. 
	 */
	private String user = null;

	/**
	 * Attribute that represents the user password for including into security token 'UserNameToken'. 
	 */
	private String password = null;

	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext msgContext) throws AxisFault {
		SOAPMessage msg, secMsg;
		Document doc = null;

		secMsg = null;
		try {
			msg = msgContext.getCurrentMessage();
			doc = ((org.apache.axis.message.SOAPEnvelope) msg.getSOAPPart().getEnvelope()).getAsDocument();
			if (this.secOption == ConfigurationFacade.USER_PASS_AUTHENTICATION) {
				secMsg = this.createUserNameToken(doc);
			} else if (this.secOption == ConfigurationFacade.CERTIFICATE_AUTHENTICATION) {
				secMsg = this.createBinarySecurityToken(doc);
			}

			if (!(this.secOption == ConfigurationFacade.WITHOUT_AUTHENTICATION)) {
				// Modificaci�n de la petici�n SOAP
				((SOAPPart) msgContext.getRequestMessage().getSOAPPart()).setCurrentMessage(secMsg.getSOAPPart().getEnvelope(), SOAPPart.FORM_SOAPENVELOPE);
			}
		} catch (Exception e) {
			String errorMsg = Language.getMessage(LanguageKeys.INVK_003);
			LOGGER.error(errorMsg, e);
			throw new AxisFault(errorMsg, e);
		}
	}

	/**
	 * Constructor method for the class ClientHandler.java.
	 * @param securityOption 	 Parameter that indicates the authorization type of the web service request.
	 */
	public ClientHandler(int securityOption) {
		super();
		this.secOption = securityOption;
	}

	/**
	 * Gets the value of the type of keystore that contains the certificate to sign the SOAP request.
	 * @return the value of the type of keystore that contains the certificate to sign the SOAP request.
	 */
	public String getKeystoreType() {
		return keystoreType;
	}

	/**
	 * Sets the value of the type of keystore that contains the certificate to sign the SOAP request..
	 * @param storeType The value for the type of keystore that contains the certificate to sign the SOAP request.
	 */
	public void setKeystoreType(String storeType) {
		this.keystoreType = storeType;
	}

	/**
	 * Gets the value of the password of keystore that contains the certificate to sign the SOAP request.
	 * @return the value of the password of keystore that contains the certificate to sign the SOAP request.
	 */
	public String getKeystorePassword() {
		return keystorePassword;
	}

	/**
	 * Sets the value of the password of keystore that contains the certificate to sign the SOAP request.
	 * @param storePassword The value for the password of keystore that contains the certificate to sign the SOAP request.
	 */
	public void setKeystorePassword(String storePassword) {
		this.keystorePassword = storePassword;
	}

	/**
	 * Gets the value of the alias of certificate to sign the SOAP request..
	 * @return the value of the alias of certificate to sign the SOAP request..
	 */
	public String getKeystoreAlias() {
		return keystoreAlias;
	}

	/**
	 * Sets the value of the alias of certificate to sign the SOAP request..
	 * @param storeAlias The value for the alias of certificate to sign the SOAP request..
	 */
	public void setKeystoreAlias(String storeAlias) {
		this.keystoreAlias = storeAlias;
	}

	/**
	 * Gets the value of the password associated to the alias of certificate to sign the SOAP request.
	 * @return the value of the password associated to the alias of certificate to sign the SOAP request.
	 */
	public String getAliasPassword() {
		return aliasPassword;
	}

	/**
	 * Sets the value of the password associated to the alias of certificate to sign the SOAP request.
	 * @param aliasCertpassword The value for the password associated to the alias of certificate to sign the SOAP request.
	 */
	public void setAliasPassword(String aliasCertpassword) {
		this.aliasPassword = aliasCertpassword;
	}

	/**
	 * Gets the value of the path to keystore that contains the certificate to sign the SOAP request..
	 * @return the value of the path to keystore that contains the certificate to sign the SOAP request..
	 */
	public String getKeystorePath() {
		return keystorePath;
	}

	/**
	 * Sets the value of the path to keystore that contains the certificate to sign the SOAP request..
	 * @param path The value for the path to keystore that contains the certificate to sign the SOAP request..
	 */
	public void setKeystorePath(String path) {
		this.keystorePath = path;
	}

	/**
	 * Gets the value of the user for including into security token 'UserNameToken'.
	 * @return the value of the user for including into security token 'UserNameToken'.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the value of the user for including into security token 'UserNameToken'.
	 * @param userId The value for the user for including into security token 'UserNameToken'.
	 */
	public void setUser(String userId) {
		this.user = userId;
	}

	/**
	 * Gets the value of the user password for including into security token 'UserNameToken'..
	 * @return the value of the user password for including into security token 'UserNameToken'..
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the value of the user password for including into security token 'UserNameToken'..
	 * @param userPassword The value for the user password for including into security token 'UserNameToken'..
	 */
	public void setPassword(String userPassword) {
		this.password = userPassword;
	}

	/**
	 * Includes a security header of UserNameToken type into SOAP request supplied.
	 * @param soapEnvelopeRequest XML Document that represents a SOAP request.
	 * @return SOAP request that includes a security header of UserNameToken type.
	 * @throws TransformerException 				If a transform error occurs.
	 * @throws SOAPException 						If an error occurs while creating the security header.
	 * @throws IOException 							If an error occurs while reading the input streams.
	 */
	private SOAPMessage createUserNameToken(Document soapEnvelopeRequest) throws  TransformerException,  IOException, SOAPException {
		ByteArrayOutputStream baos;
		Document secSOAPReqDoc;
		DOMSource source;
		Element element;
		SOAPMessage res;
		StreamResult streamResult;
		String secSOAPReq;
		WSSecUsernameToken wsSecUsernameToken;
		WSSecHeader wsSecHeader;

		// Inserci�n del tag wsse:Security y userNameToken
		wsSecHeader = new WSSecHeader(null, false);
		wsSecUsernameToken = new WSSecUsernameToken();
		wsSecUsernameToken.setPasswordType(WSConstants.PASSWORD_DIGEST);
		wsSecUsernameToken.setUserInfo(this.user, this.password);
		wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
		wsSecUsernameToken.prepare(soapEnvelopeRequest);
		// Añadimos una marca de tiempo inidicando la fecha de creaci�n del tag
		wsSecUsernameToken.addCreated();
		wsSecUsernameToken.addNonce();
		// Modificaci�n de la petici�n
		secSOAPReqDoc = wsSecUsernameToken.build(soapEnvelopeRequest, wsSecHeader);
		element = secSOAPReqDoc.getDocumentElement();

		// Transformaci�n del elemento DOM a String
		source = new DOMSource(element);
		baos = new ByteArrayOutputStream();
		streamResult = new StreamResult(baos);
		TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
		secSOAPReq = new String(baos.toByteArray());

		// Creaci�n de un nuevo mensaje SOAP a partir del mensaje SOAP
		// securizado formado
		MessageFactory mf = new org.apache.axis.soap.MessageFactoryImpl();
		res = mf.createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));

		return res;
	}

	/**
	 * Includes a security header of BinarySecurityToken type into SOAP request supplied.
	 * @param soapEnvelopeRequest XML Document that represents a SOAP request.
	 * @return	SOAP request that includes a security header of BinarySecurityToken type.
	 * @throws TransformerException 				If a transform error occurs.
	 * @throws SOAPException 						If an error occurs while creating the security header.
	 * @throws IOException 							If an error occurs while reading the input streams.
	 */
	private SOAPMessage createBinarySecurityToken(Document soapEnvelopeRequest) throws  TransformerException, IOException, SOAPException  {
		ByteArrayOutputStream baos;
		Crypto crypto;
		Document secSOAPReqDoc;
		DOMSource source;
		Element element;
		StreamResult streamResult;
		String secSOAPReq;
		SOAPMessage res;
		WSSecSignature wsSecSignature;
		WSSecHeader wsSecHeader;

		crypto = null;
		wsSecHeader = null;
		wsSecSignature = null;
		// Inserci�n del tag wsse:Security y BinarySecurityToken
		wsSecHeader = new WSSecHeader(null, false);
		wsSecSignature = new WSSecSignature();
		crypto = CryptoFactory.getInstance("org.apache.ws.security.components.crypto.Merlin", this.initializateCryptoProperties());
		// Indicaci�n para que inserte el tag BinarySecurityToken
		wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
		wsSecSignature.setUserInfo(this.keystoreAlias, this.aliasPassword);
		wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
		wsSecSignature.prepare(soapEnvelopeRequest, crypto, wsSecHeader);

		// Modificaci�n y firma de la petici�n
		secSOAPReqDoc = wsSecSignature.build(soapEnvelopeRequest, crypto, wsSecHeader);
		element = secSOAPReqDoc.getDocumentElement();
		// Transformaci�n del elemento DOM a String
		source = new DOMSource(element);
		baos = new ByteArrayOutputStream();
		streamResult = new StreamResult(baos);
		TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
		secSOAPReq = new String(baos.toByteArray());

		// Creaci�n de un nuevo mensaje SOAP a partir del mensaje SOAP
		// securizado formado
		MessageFactory mf = new org.apache.axis.soap.MessageFactoryImpl();
		res = mf.createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));

		return res;
	}

	/**
	 * Gets all properties to be initialized with the cryptographic manager WSS4J.
	 * @return Properties to be initialized with the cryptographic manager WSS4J. 
	 */
	private Properties initializateCryptoProperties() {
		Properties res = new Properties();
		res.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
		res.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", this.keystoreType);
		res.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", this.keystorePassword);
		res.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", this.keystoreAlias);
		res.setProperty("org.apache.ws.security.crypto.merlin.alias.password", this.aliasPassword);
		res.setProperty("org.apache.ws.security.crypto.merlin.file", this.keystorePath);
		return res;
	}
}
