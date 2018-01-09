package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;
import java.util.List;

import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UsersPOJO;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of Users.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IUserModuleBO extends Serializable {
	
	
	/**
	 * Method that provides the information associated to an user registered in the system.
	 * @param login		User login.
	 * @return		{@link UsersPOJO} object that contains the user information.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist user with the supplied login.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one user with the supplied login.
	 */
	UsersPOJO getUser(String login) throws DatabaseException;
	
	/**
	 * Method that modifies the information associated to an user registered in the system.
	 * @param userPOJO		User data to modify.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist user with the supplied login.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one user with the supplied login.
	 */
	void modifyUser(UsersPOJO userPOJO) throws DatabaseException;
	
	/**
	 * Method that provides the personal data of a user registered in the system.
	 * @param personPk		Primary key of the personal data to get.
	 * @return {@link PersonaldataPOJO} object that contais the personal data associated to an user.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist user with the supplied login.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one user with the supplied login.
	 */
	PersonaldataPOJO getPerson(Long personPk) throws DatabaseException;
	
	/**
	 * Method that provides the list of all persons registered in the system.
	 * @return	List of {@link PersonaldataPOJO} object that contains the personal data.
	 * @throws DatabaseException	If an error occurs.
	 */
	List<PersonaldataPOJO> getAllPersons() throws DatabaseException;
	
	/**
	 * Method that provides the list of all users registered in the system.
	 * @return	List of {@link UsersPOJO} object that contains the personal data.
	 * @throws DatabaseException	If an error occurs.
	 */
	List<UsersPOJO> getAllUsers() throws DatabaseException;
	
	/**
	 * Method that modify the personal data of a person registered in the system.
	 * @param person	{@link PersonaldataPOJO} object that contains the new data.
	 * @throws DatabaseException If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the person is no found.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If exits a person with the supplied data.
	 */
	void modifyPerson(PersonaldataPOJO person) throws DatabaseException;
	
	/**
	 * Method that records a person in the system.
	 * @param personalData Personal data.
	 * @return	PersonaldataPOJO unique identifier.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is the element with the identifier supplied.<br/>
	 */
	Long createPerson(PersonaldataPOJO personalData) throws DatabaseException;
	
	/**
	 * Unregisters an user of the system.
	 * @param login		User login to drop.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist user with the supplied login.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one user with the supplied login.
	 */
	void deleteUser(String login) throws DatabaseException;
	
	/**
	 * This method registers an user in the system.
	 * @param newUser	Object with the information of the new user to be created.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If the login is used by other user.<br/>
	 */
	void createUser(UsersPOJO newUser) throws DatabaseException;
	
	/**
	 * Remove a person from configuration system.
	 * @param personId	Person identifier.
	 * @throws DatabaseException	If an errors occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the person is no found.<br/>
	 * 								{@link DatabaseException#ITEM_ASSOCIATED} The person is associated to an application or an user.
	 */
	void deletePerson(Long personId) throws DatabaseException;
}
