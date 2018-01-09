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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.UserConfigurationFacade.java.</p>
 * <b>Description:</b><p> Class that manages the configuration information associated with system users.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>13/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 13/04/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UsersPOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.ConversionUtils;


/** 
 * <p>Class that manages the configuration information associated with system users.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 13/04/2011.
 */
@Stateless
@UserConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UserConfigurationFacade implements UserConfigurationFacadeI {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static  Logger LOGGER = Logger.getLogger(UserConfigurationFacade.class);

	/**
	 * Attribute that allows to operate with information about users of the platform.
	 */
	@Inject
	private IUserModuleBO userBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getUserManager(java.lang.String, java.lang.String)
	 */
	public User getUserManager(String login, String password) throws ConfigurationException {

		if (login == null) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_030));
		}
		if (password == null) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_031));
		}

		UsersPOJO userPOJO = null;
		User user = null;

		try {
			userPOJO = userBO.getUser(login);

			if (userPOJO == null) {
				// throw new
				// ConfigurationException(ConfigurationException.ITEM_NO_FOUND,
				// Language.getFormatMessage(LanguageKeys.DB_036, new Object[ ]
				// { login }));

				user = null;
			} else {

				user = new User(userPOJO.getUserLogin(), userPOJO.getUserPass());
				user.setLocked(userPOJO.getUserLocked());
				PersonaldataPOJO pd = userPOJO.getPersonalData();
				Person person = new Person();
				ConversionUtils.personalData2Person(pd, person);
				user.setPerson(person);
			}
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {

			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());

			LOGGER.error(Language.getFormatMessage(LanguageKeys.DB_035, new Object[ ] { login }), e);

			if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				return null;
			} else if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else {
				String msg = Language.getFormatMessage(LanguageKeys.CONF_029, new Object[ ] { login });
				LOGGER.error(msg, e);
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
			}
		}

		if (user != null && user.getPassword().equals(password)) {
			return user;
		} else {
			return null;
		}
	}

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#modifyUserManager(es.gob.signaturereport.configuration.items.User)
     */
    public  void modifyUserManager( String login,  String password,  Long personId,  boolean locked) throws ConfigurationException {
    	if (login == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_030));
    	}
    	if (password == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_031));
    	}
    	if (personId == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_032));
    	}
    	try {
    		
    		UsersPOJO userPOJO = new UsersPOJO();
    		PersonaldataPOJO personalDataPOJO = new PersonaldataPOJO();
    		userPOJO.setUserLogin(login);
    		userPOJO.setUserLocked(locked);
    		userPOJO.setUserPass(password);
    		personalDataPOJO.setPdPk(personId);
    		userPOJO.setPersonalData(personalDataPOJO);
			userBO.modifyUser(userPOJO);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_033, new Object[] {login});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(), e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
			}
		}
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#createUserManager(es.gob.signaturereport.configuration.items.User)
     */
    public  void createUserManager( String login,  String password,  Long personId) throws ConfigurationException {
    	if (personId == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_032));
    	}
    	if (login == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_030));
    	}
    	if (password == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_031));
    	}
    	try {
    		
    		PersonaldataPOJO personalData = userBO.getPerson(personId);
    		UsersPOJO newUser = new UsersPOJO(); 
    		newUser.setPersonalData(personalData);
    		newUser.setUserLogin(login);
    		newUser.setUserPass(password);
    		newUser.setCreationtime(new Date());
    		newUser.setUserLocked(Boolean.FALSE);
    		
			userBO.createUser(newUser);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_034, new Object[] {login});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.DUPLICATE_ITEM) {
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(), e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
			}
		}
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#lockUser(java.lang.String)
     */
    public  void lockUserManager( String login,  boolean isLocked) throws ConfigurationException {
    	if(login == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_030));
    	}
    	try {
    		
    		UsersPOJO userPOJO = new UsersPOJO();
    		PersonaldataPOJO personalDataPOJO = new PersonaldataPOJO();
    		userPOJO.setUserLogin(login);
    		userPOJO.setPersonalData(personalDataPOJO);
			userBO.modifyUser(userPOJO);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_035, new Object[] {login});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(), e);
			}else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
			}
		}
    }

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getAllPersons()
	 */
	public  Person[ ] getAllPersons() throws ConfigurationException {
		try {
			
			List<PersonaldataPOJO> listaPersonalData = userBO.getAllPersons();
			Person[ ] listaPersonas = null;
			
			if (listaPersonalData != null) {
				
				listaPersonas = new Person[listaPersonalData.size()];
				Iterator<PersonaldataPOJO> pdIt = listaPersonalData.iterator();
				int i = 0;
				while (pdIt.hasNext()) {
					PersonaldataPOJO pd = pdIt.next();
					listaPersonas[i] = new Person();
					personalData2Person(pd, listaPersonas[i]);
					i++;
				}
				
			}
			
			return listaPersonas;
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.CONF_036PERSON);
			LOGGER.error(msg, e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getAllUsers()
	 */
	public  User[ ] getAllUsers() throws ConfigurationException {
		try {
			
			 List<UsersPOJO> listaUsersPOJO = userBO.getAllUsers();
			User[ ] users = null;
			
			if (listaUsersPOJO != null) {
				
				users = new User[listaUsersPOJO.size()];
				Iterator<UsersPOJO> udIt = listaUsersPOJO.iterator();
				int i = 0;
				while (udIt.hasNext()) {
					UsersPOJO u = udIt.next();
					users[i] = new User(u.getUserLogin(), u.getUserPass());
					users[i].setLocked(u.getUserLocked());
					Person p = new Person();
					personalData2Person(u.getPersonalData(), p);
					users[i].setPerson(p);
					i++;
				}
			}

			return users;
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.CONF_036USER);
			LOGGER.error(msg, e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#deleteUser(java.lang.Long)
	 */
	public  void deleteUser( Long userId) throws ConfigurationException {
		if(userId ==null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_038));
    	}
		
		try {
			
			userBO.deletePerson(userId);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_037,new Object[] {userId});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.ITEM_ASSOCIATED) {
				throw new ConfigurationException(ConfigurationException.ITEM_ASSOCIATED, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#modifyUser(java.lang.Long, es.gob.signaturereport.configuration.items.Person)
	 */
	public  void modifyUser( Long userId,  String name,  String surname,  BigDecimal phoneNumber,  String email) throws ConfigurationException {
		
		if (userId == null){
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_038));
    	}
		if (name == null || name.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_040));
		}
		if (surname == null || surname.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_041));
		}
		if (phoneNumber == null) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_042));
		}
		if (email == null || email.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_043));
		}
				
		PersonaldataPOJO person = new PersonaldataPOJO();
		person.setPdPk(userId);
		person.setPdName(name);
		person.setPdSurname(surname);
		person.setPdPhone(phoneNumber);
		person.setPdEmail(email);
		
		try {
			
			userBO.modifyPerson(person);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
						
			String msg = Language.getFormatMessage(LanguageKeys.CONF_039, new Object[] {userId});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg ,e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#addUser(java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.String)
	 */
	public Long addUser( String name,  String surname,  BigDecimal phoneNumber,  String email) throws ConfigurationException {
		if (name == null || name.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_040));
		}
		if (surname == null || surname.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_041));
		}
		if (phoneNumber == null) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_042));
		}
		if (email == null || email.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_043));
		}
			
		PersonaldataPOJO person = new PersonaldataPOJO();
		person.setPdName(name);
		person.setPdSurname(surname);
		person.setPdPhone(phoneNumber);
		person.setPdEmail(email);
		
		try {
					
			return userBO.createPerson(person);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			
			String msg = Language.getFormatMessage(LanguageKeys.CONF_044, new Object[] {name + " " + surname + " " + phoneNumber + " " + email});
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.DUPLICATE_ITEM) {
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription() ,e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg, e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#deleteUserManager(java.lang.String)
	 */
	public  void deleteUserManager( String login) throws ConfigurationException {
		if (login == null) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_030));
    	}
		try {
						
			userBO.deleteUser(login);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(), e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(), e);
			}
		}
	}
	
	/**
	 * Method that includes the information included into {@link PersonaldataPOJO} object to {@link Person} object.
	 * @param pd	{@link PersonaldataPOJO} object that contains the information to extract.
	 * @param person	{@link Person} object in which will be included the information.
	 */
	private void personalData2Person(PersonaldataPOJO pd, Person person) {
		person.setName(pd.getPdName());
		person.setSurname(pd.getPdSurname());
		person.setPhone(pd.getPdPhone());
		person.setEmail(pd.getPdEmail());
		person.setPersonId(pd.getPdPk());
		return;
	}

}
