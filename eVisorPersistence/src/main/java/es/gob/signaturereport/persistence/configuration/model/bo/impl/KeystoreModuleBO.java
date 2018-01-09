package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.CertificatePOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.KeystorePOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;

/**
 * <p>Class manager for the Business Objects in keystore module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class KeystoreModuleBO implements IKeystoreModuleBO {
		
		/**
		* Attribute that represents . 
		*/
		private static final long serialVersionUID = -9064882580746733507L;

		/**
	     * Attribute that represents the class logger.
	     */
	    private static final Logger LOGGER = Logger.getLogger(KeystoreModuleBO.class);

		/**
	     * Attribute that allows to interact with the persistence configuration context.
	     */
	    @Inject
	    private IConfigurationEntityManager em;
	    
	    /**
	     * {@inheritDoc}
	     * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO#addCertificate(java.lang.String, java.lang.String, byte[], byte[], java.lang.String)
	     */
	    @Override
		public KeystorePOJO addCertificate(String keystoreId, String alias, byte[ ] encoded, byte[ ] privateKey, String keyAlgorithm) throws DatabaseException {
			if (keystoreId == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_041));
			}
			if (alias == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_044));
			}
			if (encoded == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_045));
			}
			
			try {
				
				KeystorePOJO dbKeystore = getDBKeystore(keystoreId);
				if (dbKeystore != null) {
					int ksPk = dbKeystore.getKsPk();
					int version = dbKeystore.getKsVersion();
					
					CertificatePOJO dbCert = new CertificatePOJO();
					dbCert.setCertId(alias);
					dbCert.setCertContent(encoded);
					dbCert.setCreationtime(new Date());
					dbCert.setKeyAlgorithm(keyAlgorithm);
					KeystorePOJO refKeystore = new KeystorePOJO();
					refKeystore.setKsPk(ksPk);
					refKeystore.setKsName(keystoreId);
					refKeystore.setKsVersion(version);
					dbCert.setKeystore(refKeystore);
					if (privateKey != null) {
						dbCert.setPrivateKey(privateKey);
					}
					
					em.persist(dbCert);
					
					increaseKeystoreVersion(keystoreId);
					
					return dbKeystore;

				} else {
					throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_047, new Object[ ] { keystoreId }));
				}
			} catch (PersistenceException pe) {
				
				String msg = Language.getFormatMessage(LanguageKeys.DB_046, new Object[ ] { alias, keystoreId });
				LOGGER.error(msg, pe);
				
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			} 
		}
	    
	   		
		/**
		 * Increases the keystore version.
		 * @param session	Database session.
		 * @param keystoreId	KeystoreItem identifier.
		 * @throws PersistenceException If an error occurs.
		 */
		//CHECKSTYLE:OFF -- this operation throw a "PersistenceException" and this must be controlled by the parent method.
		private void increaseKeystoreVersion(String keystoreId) throws PersistenceException {
			//CHECKSTYLE:ON
					
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_KEYSTORE_ID, keystoreId);
			em.executeNamedQuery(IQueriesNamesConstants.QUERYNAME_INC_VERSION, parameters);
		}
		
		/**
		 * Gets the KeystorePOJO identified by the supplied keystore identifier.
		 * @param keystoreId	KeystoreItem identifier.
		 * @throws PersistenceException If an error occurs.
		 * @return	KeystorePOJO.
		 */
		//CHECKSTYLE:OFF -- this operation throw a "PersistenceException" and this must be controlled by the parent method.
		@SuppressWarnings("unchecked")
		private KeystorePOJO getDBKeystore(String keystoreId) throws PersistenceException {
			//CHECKSTYLE:ON	
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_KEYSTORE_ID, keystoreId);
			final List<KeystorePOJO> keyStoreList = (List<KeystorePOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_BY_KEYSTORE_NAME, parameters);
			
			if (keyStoreList != null && keyStoreList.size() == 1) {
				return (KeystorePOJO) keyStoreList.get(0);
			} else {
				return null;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO#modifyAliasCertificate(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public KeystorePOJO modifyAliasCertificate(String keystoreId, String alias, String newAlias) throws DatabaseException {
			if (keystoreId == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_041));
			}
			if (alias == null || newAlias == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_044));
			}
			
			try {
				
				KeystorePOJO dbKeystore = getDBKeystore(keystoreId);
				
				if (dbKeystore == null) {
					throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_047, new Object[ ] { keystoreId }));
				} else {
					
					Iterator<CertificatePOJO> itCert = dbKeystore.getCertificates().iterator();
					CertificatePOJO cert = null;
					while (itCert.hasNext() && cert == null) {
						CertificatePOJO dbCert = itCert.next();
						if (dbCert.getCertId().equals(alias)) {
							cert = dbCert;
						}
					}
					if (cert == null) {
						throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { alias, keystoreId }));
					} else {
						cert.setCertId(newAlias);
						
						em.merge(cert);
												
						increaseKeystoreVersion(keystoreId);
										
					}
					return dbKeystore;
				}

			} catch (PersistenceException pe) {
				
				String msg = Language.getFormatMessage(LanguageKeys.DB_050, new Object[ ] { alias, keystoreId, newAlias });
				LOGGER.error(msg, pe);
				
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			}
		}

		/**
		 * {@inheritDoc}
		 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO#removeCertificate(java.lang.String, java.lang.String)
		 */
		@Override
		public KeystorePOJO removeCertificate(String keystoreId, String alias) throws DatabaseException {
			if (keystoreId == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_041));
			}
			if (alias == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_044));
			}
			
			try {
				
				KeystorePOJO dbKeystore = getDBKeystore(keystoreId);
				if (dbKeystore == null) {
					throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_047, new Object[ ] { keystoreId }));
				} else {
					
					Iterator<CertificatePOJO> itCert = dbKeystore.getCertificates().iterator();
					CertificatePOJO cert = null;
					while (itCert.hasNext() && cert == null) {
						CertificatePOJO dbCert = itCert.next();
						if (dbCert.getCertId().equals(alias)) {
							cert = dbCert;
						}
					}
					if (cert == null) {
						throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { alias, keystoreId }));
					} else {
						if (cert.getApplications() != null && !cert.getApplications().isEmpty()) {
							throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getFormatMessage(LanguageKeys.DB_053, new Object[ ] { alias, keystoreId }));
						}

						if (cert.getPlatformsForAuthCert() != null && !cert.getPlatformsForAuthCert().isEmpty() || cert.getPlatformsForSoaptrusted() != null && !cert.getPlatformsForSoaptrusted().isEmpty()) {
							throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getFormatMessage(LanguageKeys.DB_054, new Object[ ] { alias, keystoreId }));
						}
						cert.setEndingtime(new Date());
						
						em.merge(cert);
												
						increaseKeystoreVersion(keystoreId);
						
					}
					return dbKeystore;
				}

			} catch (PersistenceException pe) {
				
				String msg = Language.getFormatMessage(LanguageKeys.DB_052, new Object[ ] { alias, keystoreId });
				LOGGER.error(msg, pe);
				
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			} 
		}
		
		
		/**
		 * {@inheritDoc}
		 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO#getKeystore(java.lang.String)
		 */
		@Override
		public final KeystorePOJO getKeystore(final String keystoreId) throws DatabaseException {
			if (keystoreId == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_041));
			}
			
			KeystorePOJO dbKeystore = null;
			
			try {
				
				dbKeystore = getDBKeystore(keystoreId);				
				
			} catch (PersistenceException e) {
				
				String msg = Language.getFormatMessage(LanguageKeys.DB_042, new Object[ ] { keystoreId });
				LOGGER.error(msg, e);
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,e);
			} 
			
			return dbKeystore;
		}
		
		/**
		 * {@inheritDoc}
		 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IKeystoreModuleBO#getKeystoreVersion(java.lang.String)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public int getKeystoreVersion(String keystoreId) throws DatabaseException {
			
			if (keystoreId == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_041));
			}
			
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(IParametersQueriesConstants.PARAM_KEYSTORE_ID, keystoreId);
				List<Integer> listVs = (List<Integer>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_KEYSTORE_VERSION, parameters);
				
				if (listVs != null && listVs.size() == 1) {
					return ((Integer) listVs.get(0)).intValue();
				} else {
					throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_047, new Object[ ] { keystoreId }));
				}
			} catch (PersistenceException pe) {
				
				String msg = Language.getFormatMessage(LanguageKeys.DB_043, new Object[ ] { keystoreId });
				LOGGER.error(msg, pe);
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			}
		}
}
