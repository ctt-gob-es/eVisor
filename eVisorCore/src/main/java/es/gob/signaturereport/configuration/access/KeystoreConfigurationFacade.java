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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.KeystoreConfigurationFacade.java.</p>
 * <b>Description:</b><p> Class that manages the configuration information associated with keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>22/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 22/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.keystore.KeystorePersistenceFacade;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/**
 * <p>Class that manages the configuration information associated with keystores.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 22/02/2011.
 */
@Stateless
@KeystoreConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class KeystoreConfigurationFacade implements KeystoreConfigurationFacadeI {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(KeystoreConfigurationFacade.class);
	
	@Inject
	private KeystorePersistenceFacade keystorePersistenceFacade;

	/**
	 * Constructor method for the class KeystoreConfigurationFacade.java.
	 */
	public KeystoreConfigurationFacade() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystorePath(java.lang.String)
	 */
	public String getKeystorePath(String keystoreId) throws ConfigurationException{
		String path = keystorePersistenceFacade.getKeystorePath(keystoreId);
		if(path==null){
			String msg = Language.getFormatMessage(LanguageKeys.CONF_001, new String[]{keystoreId});
			LOGGER.error(msg);
			throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, msg);
		}
		return path;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getCertificate(int, java.lang.String)
	 */
	public X509Certificate getCertificate(String keystoreId, String alias) throws ConfigurationException {
		X509Certificate cert =  keystorePersistenceFacade.getCertificate(keystoreId, alias);
		if(cert==null){
			String msg = Language.getFormatMessage(LanguageKeys.CONF_005, new String[]{alias,keystoreId});
			LOGGER.error(msg);
			throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, msg);
		}
		return cert;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystorePassword(java.lang.String)
	 */
	public String getKeystorePassword(String keystoreId) throws ConfigurationException{
		String password = keystorePersistenceFacade.getKeystorePassword(keystoreId);
		if(password==null){
			String msg = Language.getFormatMessage(LanguageKeys.CONF_003, new String[]{keystoreId});
			LOGGER.error(msg);
			throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, msg);
		}
		return password;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystoreType(java.lang.String)
	 */
	public String getKeystoreType(String keystoreId) throws ConfigurationException{
		String keystoreType = keystorePersistenceFacade.getKeystoreType(keystoreId);
		if(keystoreType==null){
			String msg = Language.getFormatMessage(LanguageKeys.CONF_004, new String[]{keystoreId});
			LOGGER.error(msg);
			throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, msg);
		}
		return keystoreType;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#removeCertificate(java.lang.String, java.lang.String)
	 */
	public void removeCertificate(String keystoreId, String alias) throws ConfigurationException {
		keystorePersistenceFacade.removeCertificate(keystoreId, alias);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#addCertificate(java.lang.String, java.lang.String, java.security.cert.X509Certificate, java.security.PrivateKey)
	 */
	public void addCertificate(String keystoreId, String alias, X509Certificate certificate, PrivateKey key) throws ConfigurationException {
		keystorePersistenceFacade.addCertificate(keystoreId, alias, certificate, key);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#modifyAlias(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void modifyAlias(String keystoreId, String alias, String newAlias) throws ConfigurationException {
		keystorePersistenceFacade.modifyAlias(keystoreId, alias, newAlias);

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystore(java.lang.String)
	 */
	public KeystoreItem getKeystore(String keystoreId) throws ConfigurationException {
		// TODO Auto-generated method stub
		return keystorePersistenceFacade.getKeystore(keystoreId);
	}

}
