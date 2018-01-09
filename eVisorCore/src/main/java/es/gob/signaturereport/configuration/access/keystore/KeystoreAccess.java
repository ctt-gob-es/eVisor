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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.keystore.AKeystoreAccess.java.</p>
 * <b>Description:</b><p> Abstract class that must implement the classes that manage the keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>23/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 23/02/2011.
 */
package es.gob.signaturereport.configuration.access.keystore;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;

import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/** 
 * <p>Abstract class that must implement the classes that manage the keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 23/02/2011.
 */
public interface KeystoreAccess {

	/**
	 * Attribute that represents the property associated to keystore identifier. 
	 */
	static final String KEYSTORE_ID = "Id";

	/**
	 * Attribute that represents to keystore file. 
	 */
	static final  String KEYSTORE_PATH = "Path"; 

	/**
	 * Attribute that represents the keystore type. 
	 */
	static final String KEYSTORE_TYPE = "Type";

	/**
	 * Attribute that represents the keystore password. 
	 */
	static final String KEYSTORE_PASS = "Password";

	
	/**
     * Gets the certificate associated with the alias supplied.
     * @param alias	Alias of certificate.
     * @return		CertificateItem.	
     * @throws ConfigurationException	If an error occurs accessing the keystore.
     */
    X509Certificate getCertificate(String alias) throws ConfigurationException;

    /**
     * Returns the password of keystore.
     * @return	Password.
     */
    String getKeystorePassword();

    /**
     * Gets the type of keystore.
     * @return	Type of keystore.
     */
    String getKeystoreType();

    /**
     * Gets the location of keystore.
     * @return	Path to keystore.
     */
    String getKeystorePath();
    
    /**
     * Gets a keystore from database and register it in this class.
     */
    void initKeystore();

    
    /**
     * Method that includes a certificate into the keystore.
     * @param alias		CertificateItem alias.
     * @param certificate	CertificateItem object.
     * @param key	Private key. May be null.
     * @throws ConfigurationException	If an error occurs.
     */
    void addCertificate(String alias, X509Certificate certificate, PrivateKey key)throws ConfigurationException;

    /**
     * Method that modifies the alias associated to a certificate included into the keystore. 
     * @param alias		CertificateItem alias to change.
     * @param newAlias	New certificate alias.
     * @throws ConfigurationException	If an errors occurs.
     */
    void modifyAlias(String alias, String newAlias) throws ConfigurationException;

    /**
     * Method that removes a certificate included into the keystore.
     * @param alias	CertificateItem alias to remove.
     * @throws ConfigurationException	If an error occurs.
     */
    void removeCertificate(String alias) throws ConfigurationException;

	/**
	 * Gets the store associated to keystore identifier supplied.
     * @return A {@link KeystoreItem} object that contains all certificates and private keys associated to keystore.
     * @throws ConfigurationException	If an error occurs.
	 */
	KeystoreItem getKeystore() throws ConfigurationException;

	Properties getKeystoreProp();
	
	void setKeystoreProp(Properties keystoreProp);
}
