package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;

import es.gob.signaturereport.persistence.configuration.model.pojo.KeystorePOJO;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of Unit Organization.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IKeystoreModuleBO extends Serializable {
	
	/**
	 * Add a certificate in a keystore.
	 * @param keystoreId	KeystoreItem identifier.
	 * @param alias			Alias of the new certificate.
	 * @param encoded		CertificateItem encoded.
	 * @param privateKey	Private key associated to the certificate.
	 * @param keyAlgorithm  Key algorithm.
	 * @return				Update keystore.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist store with the supplied identifier.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one certificate with the supplied alias.
	 */
	KeystorePOJO addCertificate(String keystoreId, String alias, byte[ ] encoded, byte[ ] privateKey, String keyAlgorithm) throws DatabaseException;
	
	/**
	 * Sets the certificate alias.
	 * @param keystoreId	Keystore identifier.
	 * @param alias			Original certificate alias.
	 * @param newAlias		Certificate alias to set.
	 * @return				Update keystore.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist store with the supplied identifier or doesn't exist certificate with the supplied alias.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one certificate with the supplied alias.
	 */
	KeystorePOJO modifyAliasCertificate(String keystoreId, String alias, String newAlias) throws DatabaseException;
	
	/**
	 * Removes a certificate from a store.
	 * @param keystoreId	KeystorePOJO identifier.
	 * @param alias			Certificate alias to remove.
	 * @return				Update keystore.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist store with the supplied identifier or doesn't exist certificate with the supplied alias.<br/>
	 * 								{@link DatabaseException#ITEM_ASSOCIATED} If this certificate is used by application or platform.<br/>
	 */
	KeystorePOJO removeCertificate(String keystoreId, String alias) throws DatabaseException;
	
	/**
	 * Method that obtains the content of a keystore.
	 * @param keystoreId	KeystoreItem identifier.
	 * @return	A {@link KeystorePOJO} object.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist keystore with supplied identifier.<br/>
	 */
	KeystorePOJO getKeystore(String keystoreId) throws DatabaseException;
	
	/**
	 * Gets the keystore version.
	 * @param keystoreId KeystoreItem identifier.
	 * @return	Number of version.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist keystore with supplied identifier.<br/>
	 */
	int getKeystoreVersion(String keystoreId) throws DatabaseException;
}
