package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;

import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.persistence.configuration.model.pojo.PlatformPOJO;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of Platforms.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IPlatformModuleBO extends Serializable {
	
	/**
	 * Gets the platform information associated to supplied identifier.
	 * @param platformId	Platform identifier.
	 * @return				A {@link AfirmaData} object with the information associated.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist platform with the identifier supplied.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	PlatformPOJO getPlatform(String platformId) throws DatabaseException;
	
	/**
	 * Add a platform to the configuration of the system.
	 * @param platformId	Platform identifier.
	 * @param data			Platform information.
	 * @throws DatabaseException If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is already a platform with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void createPlatform(String platformId, AfirmaData data) throws DatabaseException;
	
	/**
	 * Gets all platform identifiers registered in the system.
	 * @return	Array of identifiers
	 * @throws DatabaseException	If an error occurs.
	 */
	String[ ] getPlatformIds() throws DatabaseException;
	
	/**
	 * Sets the platform information.
	 * @param platformId	Platform identifier.
	 * @param data			Platform information.
	 * @throws DatabaseException 	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist platform with the identifier supplied.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void modifyPlatform(String platformId, AfirmaData data) throws DatabaseException;
	
	/**
	 * Removes a platform registered in the system.
	 * @param platformId	Platform identifier.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist platform with the identifier supplied.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#ITEM_ASSOCIATED} This platform is associated to other item.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void removePlatform(String platformId) throws DatabaseException;

}
