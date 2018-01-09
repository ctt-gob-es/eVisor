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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.keystore.KeystorePersistenceFacade.java.</p>
 * <b>Description:</b><p>Facade to persistence operations associated with keystore.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>23/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 23/02/2011.
 */
package es.gob.signaturereport.configuration.access.keystore;

import java.lang.reflect.Constructor;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/** 
 * <p>Facade to persistence operations associated with keystore.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 23/02/2011.
 */
@Singleton
@ManagedBean
public class KeystorePersistenceFacade {
	
	/**
	 * Attribute that represents instance of the class.
	 */
	private static KeystorePersistenceFacade instance = null;

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(KeystorePersistenceFacade.class);
	
	/**
	 * Attribute that represents the property used to contain the information for the keystore used to sign SOAP messages. 
	 */
	private static final String SOAPSIGNER_PROPERTY = "signaturereport.keystores.SOAPSigner";

	/**
	 * Attribute that represents the property used to contain the information for the keystore used to verify SOAP messages. 
	 */
	private static final String TRUST_SOAP_PROPERTY = "signaturereport.keystores.SOAPTrusted";

	/**
	 * Attribute that represents the property used to contain the information for the keystore used to verify SSL connection. 
	 */
	private static final String TRUST_SSL_PROPERTY = "signaturereport.keystores.SSLTrusted";

	/**
	 * Attribute that represents the property used to contain the information for the keystore used to verify SOAP request makes by clients. 
	 */
	private static final String SOAP_AUTH_PROPERTY = "signaturereport.keystores.SOAPAuthentication";

	/**
	 * Attribute that represents the end of property used to contain the class implementation for a keystore. 
	 */
	private static final String IMPL_PROPERTY = ".impl";

	/**
	 * Attribute that represents the end of property used to contain the path to a keystore. 
	 */
	private static final String PATH_PROPERTY = ".path";

	/**
	 * Attribute that represents the end of property used to contain the keystore type. 
	 */
	private static final String TYPE_PROPERTY = ".type";

	/**
	 * Attribute that represents the end of property used to contain the keystore password. 
	 */
	private static final String PASS_PROPERTY = ".password";

	/**
	 * Attribute that represents the class that manages the keystore used to sign SOAP message. 
	 */
	//private KeystoreAccess soapSigner = null;
	/**
	 * Attribute that represents the class that manages the keystore used to verify SOAP message. 
	 */
	//private KeystoreAccess trustSoap = null;
	/**
	 * Attribute that represents the class that manages the keystore used to verify SSL connection. 
	 */
	//private KeystoreAccess trustSsl = null;

	/**
	 * Attribute that represents the class that manages the keystore used to verify SOAP request makes by clients. 
	 */
	//private KeystoreAccess soapAut = null;
	
	@Inject
    private KeystoreAccess soapSigner;
	@Inject
    private KeystoreAccess trustSoap;
	@Inject
    private KeystoreAccess trustSsl;
	@Inject
    private KeystoreAccess soapAut;
	
	/**
	 * Gets a instance of the class.
	 * @return A instance of class.
	 */
	public static KeystorePersistenceFacade getInstance() {

		if (instance == null) {
			instance = new KeystorePersistenceFacade();
		}
		return instance;
	}
	
	/**
     * Method that initializes the keystores.
     */
    @PostConstruct
    public final void init() {
    	
    	instance = this;
    	    	    	
    	setKeystoreProperties(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE, soapSigner);
    	setKeystoreProperties(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE, trustSoap);
    	setKeystoreProperties(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE, trustSsl);
    	setKeystoreProperties(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE, soapAut);
    		
    }


	/**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	
    	instance = null;
    	
    	soapSigner = null;
		trustSoap = null;
		trustSsl = null;
		soapAut = null;
    }
	
	/**
	 * Method that provides the password of the keystore provided.
	 * @param keystoreId	KeystoreItem identifier. Allowed values:
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
	 * @return	Password of the keystore provided. Null if is not found.
	 * @throws ConfigurationException If an error occurs.
	 */
	public String getKeystorePassword(String keystoreId) throws ConfigurationException {
		String password = null;

		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				password = soapSigner.getKeystorePassword();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				password = trustSoap.getKeystorePassword();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				password = trustSsl.getKeystorePassword();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				password = soapAut.getKeystorePassword();
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}

		return password;
	}

	/**
	 * Method that provides the type of the keystore provided.
	 * @param keystoreId	KeystoreItem identifier. Allowed values:
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
	 * @return	Type of the keystore provided. Null if is not found.
	 * @throws ConfigurationException If an error occurs.
	 */
	public String getKeystoreType(String keystoreId) throws ConfigurationException {
		String keystoreType = null;
		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				keystoreType = soapSigner.getKeystoreType();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				keystoreType = trustSoap.getKeystoreType();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				keystoreType = trustSsl.getKeystoreType();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				keystoreType = soapAut.getKeystoreType();
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
		return keystoreType;
	}

	/**
	 * Method that provides the location of the keystore provided.
	 * @param keystoreId	KeystoreItem identifier. Allowed values:
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
	 * @return	Location of the keystore provided. Null if is not found.
	 * @throws ConfigurationException If an error occurs.
	 */
	public String getKeystorePath(String keystoreId) throws ConfigurationException {
		String path = null;
		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				path = soapSigner.getKeystorePath();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				path = trustSoap.getKeystorePath();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				path = trustSsl.getKeystorePath();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				path = soapAut.getKeystorePath();
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
		return path;
	}

	/**
	 *  Load the class that manages the keystore.
	 * @param keystoreId 	KeystoreItem identifier.
	 * @return		{@link KeystoreAccess} - class that manages the keystore.
	 *
	 */
	@SuppressWarnings("unchecked")
	private KeystoreAccess loadKeystoreImpl(String keystoreId) {
		String keyProp = null;
		if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
			keyProp = SOAPSIGNER_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
			keyProp = TRUST_SOAP_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
			keyProp = TRUST_SSL_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
			keyProp = SOAP_AUTH_PROPERTY;
		}
		String className = "";
		try {
			//String path = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.KEYSTORE_SECTION_ID, keyProp+PATH_PROPERTY);
			String path = StaticSignatureReportProperties.getProperty(keyProp+PATH_PROPERTY);
			//String password = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.KEYSTORE_SECTION_ID, keyProp+PASS_PROPERTY);
			String password = StaticSignatureReportProperties.getProperty(keyProp+PASS_PROPERTY);
			//String type = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.KEYSTORE_SECTION_ID, keyProp+TYPE_PROPERTY);
			String type = StaticSignatureReportProperties.getProperty(keyProp+TYPE_PROPERTY);
			Properties prop = new Properties();
			prop.setProperty(KeystoreAccess.KEYSTORE_ID, keystoreId);
			if(path!=null){
				prop.setProperty(KeystoreAccess.KEYSTORE_PATH, path);
			}
			prop.setProperty(KeystoreAccess.KEYSTORE_TYPE, type);
			if(password!=null){
				prop.setProperty(KeystoreAccess.KEYSTORE_PASS, password);
			}
			//className = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.KEYSTORE_SECTION_ID, keyProp+IMPL_PROPERTY);
			className = StaticSignatureReportProperties.getProperty(keyProp+IMPL_PROPERTY);
			Constructor<KeystoreAccess> constructor = (Constructor<KeystoreAccess>) Class.forName(className).getConstructor(Properties.class);
											
			return constructor.newInstance(new Object[ ] { prop });
		} catch (Exception e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_002, new Object[ ] { className }), e);
			return null;
		}
	}
		

	/**
	 * Gets the certificate associated with the alias supplied and which is contained into the keystore supplied.
	 * @param keystoreId	KeystoreItem identifier. Allowed values:
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
	 * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
	 * @param alias	Alias of certificate.
	 * @return		CertificateItem.	
	 * @throws ConfigurationException	If an error occurs accessing the keystore.
	 */
	public X509Certificate getCertificate(String keystoreId, String alias) throws ConfigurationException {
		X509Certificate cert = null;
		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				cert = soapSigner.getCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				cert = trustSoap.getCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				cert = trustSsl.getCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				cert = soapAut.getCertificate(alias);
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
		return cert;
	}
	
	/**
	 * Remove a certificate from a keystore.
	 * @param keystoreId	Keystore identifier.
	 * @param alias			Certificate alias.
	 * @throws ConfigurationException	If an error occurs.
	 */
	public void removeCertificate(String keystoreId, String alias) throws ConfigurationException{
		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				soapSigner.removeCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				trustSoap.removeCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				trustSsl.removeCertificate(alias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				soapAut.removeCertificate(alias);
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
	}
	
	
	 /**
     * Method that includes a certificate into the keystore.
     * @param keystoreId Keystore identifier.
     * @param alias		Certificate alias.
     * @param certificate	A {@link X509Certificate} object.
     * @param key	Private key. May be null.
     * @throws ConfigurationException	If an error occurs.
     */
    public void addCertificate(String keystoreId, String alias, X509Certificate certificate, PrivateKey key)throws ConfigurationException{
    	if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				soapSigner.addCertificate(alias, certificate, key);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				trustSoap.addCertificate(alias, certificate, key);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				trustSsl.addCertificate(alias, certificate, key);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				soapAut.addCertificate(alias, certificate, key);
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
    }
    
    /**
     * Method that modifies the alias associated to a certificate included into the keystore. 
     * @param keystoreId	Keystore identifier.
     * @param alias		CertificateItem alias to change.
     * @param newAlias	New certificate alias.
     * @throws ConfigurationException	If an errors occurs.
     */
    public void modifyAlias(String keystoreId, String alias, String newAlias) throws ConfigurationException{
    	if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				soapSigner.modifyAlias(alias, newAlias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				trustSoap.modifyAlias(alias, newAlias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				trustSsl.modifyAlias(alias, newAlias);
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				soapAut.modifyAlias(alias, newAlias);
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
    }
    
    /**
	 * Gets the store associated to keystore identifier supplied.
	 * @param keystoreId Keystore identifier.
     * @return A {@link KeystoreItem} object that contains all certificates and private keys associated to keystore.
     * @throws ConfigurationException	If an error occurs.
	 */
	public KeystoreItem getKeystore(String keystoreId) throws ConfigurationException{
		if (keystoreId != null) {
			if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
				return soapSigner.getKeystore();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
				return trustSoap.getKeystore();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
				return trustSsl.getKeystore();
			} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
				return soapAut.getKeystore();
			}else{
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_027, new Object[]{keystoreId}));
			}
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_028));
		}
	}
	
	private void setKeystoreProperties(String keystoreId, KeystoreAccess keystoreAccess) {
    	String keyProp = null;
		if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_SIGNER_KEYSTORE)) {
			keyProp = SOAPSIGNER_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_TRUSTED_KEYSTORE)) {
			keyProp = TRUST_SOAP_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SSL_TRUSTED_KEYSTORE)) {
			keyProp = TRUST_SSL_PROPERTY;
		} else if (keystoreId.equals(KeystoreConfigurationFacadeI.SOAP_AUTH_KEYSTORE)) {
			keyProp = SOAP_AUTH_PROPERTY;
		}
		String className = "";
		
		try {
			
			String path = StaticSignatureReportProperties.getProperty(keyProp+PATH_PROPERTY);
			String password = StaticSignatureReportProperties.getProperty(keyProp+PASS_PROPERTY);
			String type = StaticSignatureReportProperties.getProperty(keyProp+TYPE_PROPERTY);
			Properties prop = new Properties();
			prop.setProperty(KeystoreAccess.KEYSTORE_ID, keystoreId);
			if(path!=null){
				prop.setProperty(KeystoreAccess.KEYSTORE_PATH, path);
			}
			prop.setProperty(KeystoreAccess.KEYSTORE_TYPE, type);
			if(password!=null){
				prop.setProperty(KeystoreAccess.KEYSTORE_PASS, password);
			}
						
			keystoreAccess.setKeystoreProp(prop);
			keystoreAccess.initKeystore();
						
			
		} catch (Exception e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_002, new Object[ ] { className }), e);
			
		}
		
	}

}
