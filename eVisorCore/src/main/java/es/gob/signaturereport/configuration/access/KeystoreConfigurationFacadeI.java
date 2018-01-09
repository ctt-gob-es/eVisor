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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI.java.</p>
 * <b>Description:</b><p>Interface that provides methods for managing the system keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>22/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 22/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/** 
 * <p>Interface that provides methods for managing the system keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 22/02/2011.
 */
public interface KeystoreConfigurationFacadeI {
    
    /**
     * A constant representing the keystore used to sign SOAP message. 
     */
    String SOAP_SIGNER_KEYSTORE = "KEYSTORE SOAP";
    
    /**
     * A constant representing the keystore used to verify SOAP message. 
     */
    String SOAP_TRUSTED_KEYSTORE = "CONFIANZA SOAP";
   
    /**
     * A constant representing the keystore used to verify SSL connection. 
     */
    String SSL_TRUSTED_KEYSTORE = "CONFIANZA SSL";
    
    /**
     * Constant that identifies the keystore used to authorize the SOAP requests.
     */
    String SOAP_AUTH_KEYSTORE = "AUTORIZACION WS";
    
    /**
     * Attribute that represents the constant used to identify a key of certificate type. 
     */
    String X509_TYPE = "X.509";
    /**
     * Attribute that represents the constant used to identify a key of PKCS12 type. 
     */
    String PKCS12_TYPE = "PKCS12"; 
    
    /**
     * Attribute that represents the constant used to identify a key of JKS type. 
     */
    String JKS_TYPE = "JKS";
    
    /**
     * Method that provides the location of the keystore provided.
     * @param keystoreId	KeystoreItem identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @return	Location of the keystore provided.
     * @throws ConfigurationException If an error occurs.
     */
    String getKeystorePath(String keystoreId)throws ConfigurationException;
    
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
    X509Certificate getCertificate(String keystoreId, String alias) throws ConfigurationException;
    
    /**
     * Method that provides the password of the keystore provided.
     * @param keystoreId	KeystoreItem identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @return	Password of the keystore.
     * @throws ConfigurationException	If an error occurs.
     */
    String getKeystorePassword(String keystoreId)throws ConfigurationException;
    
    /**
     * Method that provides the type of the keystore provided.
     * @param keystoreId	KeystoreItem identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @return	Type of keystore.
     * @throws ConfigurationException	If an error occurs.
     */
    String getKeystoreType(String keystoreId)throws ConfigurationException;
    
    /**
	 * Remove a certificate from a keystore.
	 * @param keystoreId	Keystore identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
	 * @param alias			Certificate alias.
	 * @throws ConfigurationException	If an error occurs.
	 */
	void removeCertificate(String keystoreId, String alias) throws ConfigurationException;
	
	 /**
     * Method that includes a certificate into the keystore.
     * @param keystoreId Keystore identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @param alias		Certificate alias.
     * @param certificate	A {@link X509Certificate} object.
     * @param key	Private key. May be null.
     * @throws ConfigurationException	If an error occurs.
     */
    void addCertificate(String keystoreId, String alias, X509Certificate certificate, PrivateKey key)throws ConfigurationException;
    
    
    /**
     * Method that modifies the alias associated to a certificate included into the keystore. 
     * @param keystoreId	Keystore identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @param alias		CertificateItem alias to change.
     * @param newAlias	New certificate alias.
     * @throws ConfigurationException	If an errors occurs.
     */
    void modifyAlias(String keystoreId, String alias, String newAlias) throws ConfigurationException;
    
    /**
     * Gets the store associated to keystore identifier supplied.
     * @param keystoreId	Keystore identifier. Allowed values:
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_SIGNER_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SSL_TRUSTED_KEYSTORE}.
     * <br/> {@link KeystoreConfigurationFacadeI#SOAP_AUTH_KEYSTORE}.
     * @return A {@link KeystoreItem} object that contains all certificates and private keys associated to keystore.
     * @throws ConfigurationException	If an error occurs.
     */
    KeystoreItem getKeystore(String keystoreId) throws ConfigurationException;
}
