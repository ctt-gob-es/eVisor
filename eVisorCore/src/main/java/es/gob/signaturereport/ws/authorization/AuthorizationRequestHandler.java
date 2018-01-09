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
 * <b>File:</b><p>es.gob.signaturereport.ws.authorization.AuthorizationRequestHandler.java.</p>
 * <b>Description:</b><p>Class responsible for the authorization process of a Web Service request.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/06/2011.
 */
package es.gob.signaturereport.ws.authorization;

import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Vector;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.apache.ws.axis.security.handler.WSDoAllHandler;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.keystore.KeystorePersistenceFacade;
import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.impl.GenerationReportTransformI;
import es.gob.signaturereport.messages.transform.impl.ValidationReportTransformI;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.tools.CertificateUtils;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;

/**
 * <p>Class responsible for the authorization process of a Web Service request.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/06/2011.
 */
@Named
@RequestScoped
public class AuthorizationRequestHandler extends WSDoAllHandler {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AuthorizationRequestHandler.class);
	/**
	 * Attribute that represents the serial version of the class.
	 */
	private static final long serialVersionUID = -253489781869678110L;
	
	/**
	 * Attribute that represents the property used to indicate the WSS4 provider. 
	 */
	private static final String CRYPTOPROVIDER_PROP = "org.apache.ws.security.crypto.provider";

	/**
	 * Attribute that represents the property used to indicate the keys store type. 
	 */
	private static final String KEYSTORETYPE_PROP = "org.apache.ws.security.crypto.merlin.keystore.type";

	/**
	 * Attribute that represents the property used to indicate the keys store password. 
	 */
	private static final String KEYSTOREPASSWORD_PROP = "org.apache.ws.security.crypto.merlin.keystore.password";

	/**
	 * Attribute that represents the property used to indicate the keys store file path. 
	 */
	private static final String KEYSTOREFILEPATH_PROP = "org.apache.ws.security.crypto.merlin.file";
	
	/**
	 * Attribute that represents the provider class for WSS4J. 
	 */
	private static final String WSS4JPROVIDER = "org.apache.ws.security.components.crypto.Merlin";

	/**
	 * Attribute that represents cryptography properties. 
	 */
	private static Properties crypto_prop = new Properties ();
	
	/**
	 * Attribute that represents the path to 'BinarySecurityToken' element specified into WSSE. 
	 */
	private static final String  BINARYSECURITYTOKEN_PATH = "/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Security']/*[local-name()='BinarySecurityToken']";
	
	static{
		try {
			
			String path = KeystorePersistenceFacade.getInstance().getKeystorePath(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE);
			String type = KeystorePersistenceFacade.getInstance().getKeystoreType(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE);
			String password = KeystorePersistenceFacade.getInstance().getKeystorePassword(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE);
			if(path!=null && type !=null && password !=null){
				crypto_prop.setProperty(CRYPTOPROVIDER_PROP,WSS4JPROVIDER);
				crypto_prop.setProperty(KEYSTORETYPE_PROP,type);
				crypto_prop.setProperty(KEYSTOREPASSWORD_PROP,password);
				crypto_prop.setProperty(KEYSTOREFILEPATH_PROP,path);
			}else{
				LOGGER.fatal(Language.getMessage(LanguageKeys.MSG_WS_023));
			}
		} catch (ConfigurationException e) {
			String msg = Language.getMessage(LanguageKeys.MSG_WS_022);
			LOGGER.fatal(msg+e.getDescription());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		// 1. Obtenemos el identificador de aplicación
		String applicationId = getApplicationId(messageContext);
		if (applicationId == null) {
			// El identificador de aplicación es un parámetro obligatorio
			String msg = Language.getMessage(LanguageKeys.MSG_WS_011);
			LOGGER.error(msg);
			throw new AxisFault(WSAuthorizationConstants.FAILED_AUTHENTICATION, msg, null, null);
		}
		// 2. Obtenemos el metodo de autorizacion WS para la aplicacion.
		ApplicationData appData = null;
		try {
			appData = ConfigurationFacade.getInstance().getAppConf().getApplicationData(applicationId);
		} catch (ConfigurationException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.MSG_WS_012, new Object[ ] { applicationId }) + e.getDescription());
			throw new AxisFault(Language.getMessage(LanguageKeys.MSG_WS_013));
		}
		if (appData == null) {
			// No existe la aplicación
			String msg = Language.getFormatMessage(LanguageKeys.MSG_WS_014, new Object[ ] { applicationId });
			LOGGER.error(msg);
			throw new AxisFault(WSAuthorizationConstants.FAILED_AUTHENTICATION, msg, null, null);
		}
		try {
			// 3. Analizamos el método de autorizacion
			if (appData.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) {
				LOGGER.info(Language.getMessage(LanguageKeys.MSG_WS_016));
				UserPasswordAutorizationCallBackHandler cb = new UserPasswordAutorizationCallBackHandler(appData.getAuthUser(), appData.getAuthPass());
				WSSecurityEngine.getInstance().processSecurityHeader(getSOAPAsDocument(messageContext), null, cb, null);
				if (!cb.isValid()) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.MSG_WS_017, new Object[] {applicationId} ));
					throw getFault(new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION));
				}
			} else if (appData.getAuthenticationType() == ConfigurationFacade.CERTIFICATE_AUTHENTICATION) {
				LOGGER.info(Language.getMessage(LanguageKeys.MSG_WS_019));
				Document soapDoc= getSOAPAsDocument(messageContext);
				if(!isBinarySecurityToken(soapDoc)){
					LOGGER.error(Language.getMessage(LanguageKeys.MSG_WS_026));
					throw getFault(new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION));
				}
				String aliasCert = appData.getAuthCertAlias();
				try {
					X509Certificate certAuth = KeystorePersistenceFacade.getInstance().getCertificate(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE, aliasCert);
					if(certAuth != null){
						Vector<?> results = WSSecurityEngine.getInstance().processSecurityHeader(soapDoc, null, null, CryptoFactory.getInstance(WSS4JPROVIDER,crypto_prop));
						boolean  valid = false;
						if(results !=null && !results.isEmpty()){
							int i =0;
							while(i<results.size()&& !valid){
								if(results.get(i) instanceof WSSecurityEngineResult){
									WSSecurityEngineResult result = (WSSecurityEngineResult) results.get(i);
									X509Certificate certSoap = result.getCertificate();
									if(certSoap!=null){
										valid = CertificateUtils.isEqual(certAuth, certSoap);
									}
								}
								i++;
							}
						}
						if(!valid){
							LOGGER.error(Language.getFormatMessage(LanguageKeys.MSG_WS_024, new Object[] {applicationId} ));
							throw getFault(new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION));
						}
					}else{
						String msg = Language.getMessage(Language.getFormatMessage(LanguageKeys.MSG_WS_021, new Object[] {applicationId}));
						LOGGER.error(msg);
						throw new AxisFault(msg);
					}
				} catch (ConfigurationException e) {
					String msg = Language.getMessage(Language.getFormatMessage(LanguageKeys.MSG_WS_020, new Object[] {applicationId}));
					LOGGER.error(msg+ e.getMessage());
					throw new AxisFault(msg);
				}
			}
		} catch (WSSecurityException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.MSG_WS_018),e);
			throw getFault(e);
		} catch (Exception e){
			LOGGER.error(Language.getMessage(LanguageKeys.MSG_WS_018),e);
			throw new AxisFault(e.getMessage());
		}

	}

	/**
	 * Gets the application identifier included into SOAP request.
	 * @param messageContext	SOAP request.
	 * @return	Application identifier. Null if the identifier is not found.
	 * @throws AxisFault	If the request is not valid.
	 */
	private String getApplicationId(MessageContext messageContext) throws AxisFault {
		try {
			Node soapBody = messageContext.getCurrentMessage().getSOAPBody();
			Node paramNode = null;
			if (soapBody != null) {
				Node opName = soapBody.getFirstChild();
				if (opName != null) {
					paramNode = opName.getFirstChild();
				}
			}

			if (paramNode != null && paramNode.getFirstChild() != null) {
				String xml = paramNode.getFirstChild().getNodeValue();
				try {
					Document docRequest = XMLUtils.getDocument(xml.getBytes());
					String appId = XMLUtils.getNodeValue(docRequest.getDocumentElement(), GenerationReportTransformI.APPLICATIONID);
					if (appId == null) {
						appId = XMLUtils.getNodeValue(docRequest.getDocumentElement(), ValidationReportTransformI.APPLICATIONID);
					}
					return appId;
				} catch (ToolsException e) {
					String msg = Language.getMessage(LanguageKeys.MSG_WS_008);
					LOGGER.error(msg, e);
					throw new AxisFault(msg);
				}
			} else {
				throw new AxisFault(Language.getMessage(LanguageKeys.MSG_WS_008));
			}
		} catch (SOAPException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.MSG_WS_008), e);
			throw new AxisFault(e.getMessage());
		}
	}

	/**
	 * Gets the message SOAP as DOM document.
	 * @param messageContext	Message SOAP.
	 * @return	DOM document.
	 * @throws AxisFault	If an error occurs.
	 */
	private Document getSOAPAsDocument(MessageContext messageContext) throws AxisFault {
		try {
			return messageContext.getCurrentMessage().getSOAPEnvelope().getAsDocument();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.MSG_WS_008);
			LOGGER.error(msg, e);
			throw new AxisFault(msg, e);
		}
	}

	
	/**
	 * Gets a SOAP fault from the Web Security Exception.
	 * @param wsException	Web Security Exception.
	 * @return	SOAP  fault encapsulate into an {@link AxisFault} object. 
	 */
	private AxisFault getFault(WSSecurityException wsException) {
		if (wsException != null) {
			QName code = null;
			switch (wsException.getErrorCode()) {
				case WSSecurityException.FAILED_AUTHENTICATION:
					code = WSAuthorizationConstants.FAILED_AUTHENTICATION;
					break;
				case WSSecurityException.FAILED_CHECK:
					code = WSAuthorizationConstants.FAILED_CHECK;
					break;
				case WSSecurityException.INVALID_SECURITY:
					code = WSAuthorizationConstants.INVALID_SECURITY;
					break;
				case WSSecurityException.INVALID_SECURITY_TOKEN:
					code = WSAuthorizationConstants.INVALID_SECURITY_TOKEN;
					break;
				case WSSecurityException.SECURITY_TOKEN_UNAVAILABLE:
					code = WSAuthorizationConstants.SECURITY_TOKEN_UNAVAILABLE;
					break;
				case WSSecurityException.UNSUPPORTED_ALGORITHM:
					code = WSAuthorizationConstants.UNSUPPORTED_ALGORITHM;
					break;
				case WSSecurityException.UNSUPPORTED_SECURITY_TOKEN:
					code = WSAuthorizationConstants.UNSUPPORTED_SECURITY_TOKEN;
					break;
				default:
					code = WSAuthorizationConstants.FAILED_AUTHENTICATION;
					break;
			}
			return new AxisFault(code,wsException.getMessage(),null,null);
		} else {
			return new AxisFault(Language.getMessage(LanguageKeys.MSG_WS_015));
		}
	}
	
	/**
	 * Check if the SOAP request includes a 'BinarySecurityToken' element.
	 * @param soapDocument	SOAP request.
	 * @return	True if the element is included, otherwise false.
	 * @throws AxisFault If the request is not valid.
	 */
	private boolean isBinarySecurityToken(Document soapDocument) throws AxisFault{
		try {
			NodeList nodes = XMLUtils.getNodes(soapDocument.getDocumentElement(), BINARYSECURITYTOKEN_PATH);
			return (nodes!=null && nodes.getLength()==1);
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.MSG_WS_025);
			LOGGER.error(msg+e.getDescription());
			throw new AxisFault(msg);
		}
	}
}
