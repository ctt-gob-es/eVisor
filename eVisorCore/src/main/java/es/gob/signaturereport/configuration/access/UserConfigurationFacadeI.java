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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.UserConfigurationFacadeI.java.</p>
 * <b>Description:</b><p> Interface that provides methods and constants used to manage system users.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>13/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 13/04/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.math.BigDecimal;

import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/**
 * <p>Interface that provides methods and constants used to manage system users.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 13/04/2011.
 */
public interface UserConfigurationFacadeI {


	/**
	 * Method that returns the user manager information is supplied credentials.
	 * @param login	User login.
	 * @param password	User password.
	 * @return		{@link User} if the user exists, otherwise null.
	 * @throws ConfigurationException If an error occurs.
	 */
	User getUserManager(String login, String password) throws ConfigurationException;


	/**
	 * Method that modifies the information associated with a registered user manager.
	 * @param login	User login.
	 * @param password	User password.
	 * @param personId	Unique identifier of the person registered in the system.
	 * @param locked	Lock status.
	 * @throws ConfigurationException	If the user does not exists or another error occurs.
	 */
	void modifyUserManager(String login, String password, Long personId, boolean locked) throws ConfigurationException;

	/**
	 * Add a new user manager in the system.
	 * @param login		User login.
	 * @param password	User password.
	 * @param personId	Unique identifier of the personal data associated to user.
	 * @throws ConfigurationException	If the user exists or another error occurs.
	 */
	void createUserManager(String login, String password, Long personId) throws ConfigurationException;
	/**
	 * Lock a user manager into the system.
	 * @param login	User login.
	 * @param isLocked  True if the user will be locked, false otherwise.
	 * @throws ConfigurationException	If the user does not exists or another error occurs.
	 */
	void lockUserManager(String login,boolean isLocked) throws ConfigurationException;

	/**
	 * Gets all persons registered in the system.
	 * @return	Array of {@link Person} that contain the personal data of persons register in the system.
	 * @throws ConfigurationException	If an error occurs.
	 */
	Person[ ] getAllPersons() throws ConfigurationException;
	
	/**
	 * Gets all users registered in the system.
	 * @return	Array of {@link User} that contain the personal data of users register in the system.
	 * @throws ConfigurationException	If an error occurs.
	 */
	User[ ] getAllUsers() throws ConfigurationException;
	
	/**
	 * Remove a person from configuration system.
	 * @param userId	Person identifier.
	 * @throws ConfigurationException If the user doesn't exists, the user is associated to other configuration item, or other error.
	 */
	void deleteUser(Long userId) throws ConfigurationException;

	/**
	 * Method that modify the personal data of a person registered in the system.
	 * @param userId	Unique identifier of the person which data will be modified.
	 * @param name		User name.
	 * @param surname	User surname.
	 * @param phoneNumber	User phone number.
	 * @param email		User e-mail.
	 * @throws ConfigurationException If the user does not exists or another error occurs. 
	 */
	void modifyUser(Long userId, String name, String surname, BigDecimal phoneNumber, String email) throws ConfigurationException;
	
	/**
	 * Method that records a user in the system.
	 ** @param name		User name.
	 * @param surname	User surname.
	 * @param phoneNumber	User phone number.
	 * @param email		User e-mail.
	 * @return	Personal unique identifier.
	 * @throws ConfigurationException	If an error occurs.
	 */
	Long addUser(String name, String surname, BigDecimal phoneNumber, String email) throws ConfigurationException;
	
	
	/**
	 * Delete an user manager
	 * @param login	User login.
	 * @throws ConfigurationException If an error occurs. 
	 */
	void deleteUserManager(String login)throws ConfigurationException;
	
	
}
