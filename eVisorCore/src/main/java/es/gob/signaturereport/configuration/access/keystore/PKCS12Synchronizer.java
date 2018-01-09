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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.keystore.PKCS12Synchronizer.java.</p>
 * <b>Description:</b><p>Class that manages the persistence of keystore of  pkcs12 type and controls the synchronization with the database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/05/2011.
 */
package es.gob.signaturereport.configuration.access.keystore;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.CertificateItem;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.CertificatePOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.KeystorePOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.tools.UtilsResources;

/** 
 * <p>Class that manages the persistence of keystore of pkcs12 type and controls the synchronization with the database.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/05/2011.
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PKCS12Synchronizer implements KeystoreAccess {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(PKCS12Synchronizer.class);

	/**
	 * Attribute that represents the keystore information. 
	 */
	private KeystoreItem keystore = null;

	/**
	 * Attribute that represents the class that defines the functionality of a certificate factory. 
	 */
	private CertificateFactory cf = null;
	
	/**
	 * 
	 */
	private Properties keystoreProp;
	
	/**
	 * Attribute that allows to operate with information about users of the platform.
	 */
	@Inject
	private IKeystoreModuleBO keystoreBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;

	/**
	 * Constructor method for the class PKCS12Synchronizer.java.
	 */
//	public PKCS12Synchronizer() {
//					
//		try {
//			cf = CertificateFactory.getInstance(KeystoreConfigurationFacadeI.X509_TYPE);
//		} catch (CertificateException e) {
//			LOGGER.error(Language.getMessage(LanguageKeys.CONF_019), e);
//		}
//		loadKeystore();
//	}
	
	@PostConstruct
	public void init()
	{
		try {
			cf = CertificateFactory.getInstance(KeystoreConfigurationFacadeI.X509_TYPE);
		} catch (CertificateException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.CONF_019), e);
		}
		//loadKeystore();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#getCertificate(java.lang.String)
	 */
	@Override
	public X509Certificate getCertificate(String alias) throws ConfigurationException {
		if(alias==null || alias.trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_044));
		}
		if (!isUpdated()) {
			loadKeystore();
		}
		CertificateItem cert = keystore.getCertificates().get(alias);
		if (cert != null && cert.getContent() != null) {
			ByteArrayInputStream bi = null;
						
			bi = new ByteArrayInputStream(cert.getContent());
			
			try {
				return (X509Certificate) cf.generateCertificate(bi);
			} catch (CertificateException e) {
				String msg = Language.getFormatMessage(LanguageKeys.CONF_020, new Object[ ] { alias, keystoreProp.getProperty(KEYSTORE_ID) });
				LOGGER.error(msg, e);
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, Language.getFormatMessage(LanguageKeys.CONF_021, new Object[ ] { alias, keystoreProp.getProperty(KEYSTORE_ID) }),e);
			}
		} else {
			throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, Language.getFormatMessage(LanguageKeys.CONF_020, new Object[ ] { alias,keystoreProp.getProperty(KEYSTORE_ID) }));
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#getKeystorePassword()
	 */
	@Override
	public String getKeystorePassword() {
		return keystoreProp.getProperty(KEYSTORE_PASS);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#getKeystoreType()
	 */
	@Override
	public String getKeystoreType() {
		return keystoreProp.getProperty(KEYSTORE_TYPE);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#getKeystorePath()
	 */
	@Override
	public String getKeystorePath() {
		return keystoreProp.getProperty(KEYSTORE_PATH);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#addCertificate(java.lang.String, java.security.cert.X509Certificate, java.security.PrivateKey)
	 */
	@Override
	public void addCertificate(String alias, X509Certificate certificate, PrivateKey key) throws ConfigurationException {
		byte[] privateKey = null;
		String keyAlgorithm = null;
		if(key!=null){
			privateKey = key.getEncoded();
			keyAlgorithm = key.getAlgorithm();
		}
		byte[] certEncoded = null;
		if(certificate!=null){
			try {
				certEncoded = certificate.getEncoded();
			} catch (CertificateEncodingException e) {
				String msg =Language.getFormatMessage(LanguageKeys.CONF_023, new Object[]{alias,keystoreProp.getProperty(KEYSTORE_ID)});
				LOGGER.error(msg,e);
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, msg,e);
			}
		}
		try {
			
			KeystorePOJO ksPOJO = keystoreBO.addCertificate(keystoreProp.getProperty(KEYSTORE_ID), alias, certEncoded, privateKey, keyAlgorithm);
			
			KeystoreItem keystoreItem = new KeystoreItem(keystoreProp.getProperty(KEYSTORE_ID), ksPOJO.getKsVersion());
			dbKeystore2Keystore(ksPOJO, keystoreItem);
			
			if (keystoreItem.getCertificates().containsKey(alias)) {
				throw new es.gob.signaturereport.persistence.exception.DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_049, new Object[ ] { alias, ksPOJO.getKsName() }));
			} else {
				CertificateItem cert = new CertificateItem(alias, certEncoded);
				cert.setPrivateKey(privateKey);
				cert.setKeyAlgorithm(keyAlgorithm);
				keystoreItem.getCertificates().put(alias, cert);
				keystoreItem.setVersion(ksPOJO.getKsVersion() + 1);
			}
			
			this.keystore = keystoreItem;
						
			loadKeystore();
		}  catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_023, new Object[]{alias,keystoreProp.getProperty(KEYSTORE_ID)}),e);
			if(e.getCode()== DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.ITEM_NOT_FOUND ){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.DUPLICATE_ITEM){
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#modifyAlias(java.lang.String, java.lang.String)
	 */
	@Override
	public void modifyAlias(String alias, String newAlias) throws ConfigurationException {
		try {
			
			final KeystorePOJO dbKeystore = keystoreBO.modifyAliasCertificate(keystoreProp.getProperty(KEYSTORE_ID), alias, newAlias);
			
			int version = dbKeystore.getKsVersion();
			KeystoreItem keystoreItem = new KeystoreItem(keystoreProp.getProperty(KEYSTORE_ID), version);
			dbKeystore2Keystore(dbKeystore, keystoreItem);
			
			if (keystoreItem.getCertificates().containsKey(newAlias)) {
				throw new es.gob.signaturereport.persistence.exception.DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_049, new Object[ ] { newAlias, keystoreProp.getProperty(KEYSTORE_ID) }));
			}
			if (!keystoreItem.getCertificates().containsKey(alias)) {
				throw new es.gob.signaturereport.persistence.exception.DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { alias, keystoreProp.getProperty(KEYSTORE_ID) }));
			}
			
			CertificateItem certItem = keystoreItem.getCertificates().remove(alias);
			certItem.setAlias(newAlias);
			keystoreItem.getCertificates().put(newAlias, certItem);
			keystoreItem.setVersion(version + 1);
			
			this.keystore = keystoreItem;
			loadKeystore();
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_024, new Object[] {newAlias,alias,keystoreProp.getProperty(KEYSTORE_ID)}),e);
			if(e.getCode()==DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.ITEM_NOT_FOUND ){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.DUPLICATE_ITEM){
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.keystore.AKeystoreAccess#removeCertificate(java.lang.String)
	 */
	@Override
	public void removeCertificate(String alias) throws ConfigurationException {
		try {
			final KeystorePOJO dbKeystore = keystoreBO.removeCertificate(keystoreProp.getProperty(KEYSTORE_ID), alias);
			
			int version = dbKeystore.getKsVersion();
			KeystoreItem keystoreItem = new KeystoreItem(keystoreProp.getProperty(KEYSTORE_ID), version);
			dbKeystore2Keystore(dbKeystore, keystoreItem);
			if (!keystoreItem.getCertificates().containsKey(alias)) {
				throw new es.gob.signaturereport.persistence.exception.DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { alias, keystoreProp.getProperty(KEYSTORE_ID) }));
			}
			
			keystoreItem.getCertificates().remove(alias);
			keystoreItem.setVersion(version + 1);
						
			loadKeystore();
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_025, new Object[] {alias,keystoreProp.getProperty(KEYSTORE_ID)}),e);
			if(e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.ITEM_NOT_FOUND ){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else if(e.getCode() == DatabaseException.ITEM_ASSOCIATED){
				throw new ConfigurationException(ConfigurationException.ITEM_ASSOCIATED, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	@Override
	public KeystoreItem getKeystore() throws ConfigurationException {
		if (!isUpdated()) {
			loadKeystore();
		}
		try{
			return this.keystore.clone();
		}catch(CloneNotSupportedException e){
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getMessage(),e);
		}
	}
	
	@Override
	public void initKeystore() {
			loadKeystore();
	}
	
	/**
	 * Method to get a keystore from database and register it in this class.
	 */
	protected void loadKeystore() {
		ByteArrayInputStream bi = null;
		FileOutputStream fo = null;
		try {
			
			KeystoreItem keystoreItem = new KeystoreItem(keystoreProp.getProperty(KEYSTORE_ID), 0);
			
			final KeystorePOJO dbKeystore = keystoreBO.getKeystore(keystoreProp.getProperty(KEYSTORE_ID));
			
			this.keystore = keystoreItem;
			
			if (dbKeystore != null) {
				dbKeystore2Keystore(dbKeystore, keystore);
			} else {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_047, new Object[ ] { keystoreProp.getProperty(KEYSTORE_ID) }));
			}

			if(this.keystore != null && keystoreProp.getProperty(KEYSTORE_PATH)!=null){
				KeyStore keystoreFile = KeyStore.getInstance(keystoreProp.getProperty(KEYSTORE_TYPE));
				keystoreFile.load(null, keystoreProp.getProperty(KEYSTORE_PASS).toCharArray());
				if (!this.keystore.getCertificates().isEmpty()) {
					Iterator<String> aliases = this.keystore.getCertificates().keySet().iterator();
					while (aliases.hasNext()) {
						String alias = aliases.next();
						CertificateItem certConf = this.keystore.getCertificates().get(alias);
						bi = new ByteArrayInputStream(certConf.getContent());
						java.security.cert.Certificate cert = cf.generateCertificate(bi);
						if (certConf.getPrivateKey() != null) {
							PKCS8EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(certConf.getPrivateKey());
							PrivateKey key = KeyFactory.getInstance(certConf.getKeyAlgorithm()).generatePrivate(encodedKey);
							keystoreFile.setKeyEntry(alias, key,keystoreProp.getProperty(KEYSTORE_PASS).toCharArray(), new java.security.cert.Certificate[ ] { cert });
						} else {
							keystoreFile.setCertificateEntry(alias, cert);
						}
					}
				}
				fo = new FileOutputStream(new File(keystoreProp.getProperty(KEYSTORE_PATH)));
				keystoreFile.store(fo, keystoreProp.getProperty(KEYSTORE_PASS).toCharArray());
			}
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {	
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
		} catch (Exception e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_021, new Object[] {keystoreProp.getProperty(KEYSTORE_ID)}),e);
		} finally {
			if (bi != null) {
				UtilsResources.safeCloseInputStream(bi);
			}

			if (fo != null) {
				UtilsResources.safeCloseOutputStream(fo);
			}
		}
	}

	/**
	 * Method that checks if the keystore is the newest.
	 * @return True if the keystore is updated. Otherwise false.
	 */
	private boolean isUpdated() {
		boolean updated = true;
		try {
			int versionDb = keystoreBO.getKeystoreVersion(keystoreProp.getProperty(KEYSTORE_ID));		
			
			updated = (this.keystore.getVersion() == versionDb);
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_026, new Object[] {keystoreProp.getProperty(KEYSTORE_ID)}),e);
		}
		return updated;
	}
	
	/**
	 * Method that includes the information included into {@link KeystorePOJO} object to {@link KeystoreItem} object.
	 * @param dbKeystore	{@link KeystorePOJO} object that contains the information to extract.
	 * @param keystoreItem		{@link KeystoreItem} object in which will be included the information.
	 */
	private void dbKeystore2Keystore(final KeystorePOJO dbKeystore, final KeystoreItem keystoreItem) {
		if (dbKeystore != null && keystore != null) {
			keystoreItem.setVersion(dbKeystore.getKsVersion());
			LinkedHashMap<String, CertificateItem> certificates = new LinkedHashMap<String, CertificateItem>();
			Iterator<CertificatePOJO> itCert = dbKeystore.getCertificates().iterator();
			while (itCert.hasNext()) {
				CertificatePOJO dbCert = itCert.next();
				CertificateItem cert = new CertificateItem(dbCert.getCertId(), dbCert.getCertContent());
				if (dbCert.getPrivateKey() != null) {
					cert.setPrivateKey(dbCert.getPrivateKey());
					cert.setKeyAlgorithm(dbCert.getKeyAlgorithm());
				}
				certificates.put(dbCert.getCertId(), cert);
			}
			keystoreItem.setCertificates(certificates);
		}
		return;
	}

	
	
	/**
	 * 
	 * @return
	 */
	@Override
	public Properties getKeystoreProp() {
		return keystoreProp;
	}

	
	/**
	 * 
	 * @param keystoreProp
	 */
	@Override
	public void setKeystoreProp(Properties keystoreProp) {
		this.keystoreProp = keystoreProp;
	}

	
}
