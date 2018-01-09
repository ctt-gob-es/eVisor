package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
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
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UsersPOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <p>Class manager for the Business Objects in user module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserModuleBO implements IUserModuleBO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2970966306453832357L;

	/**
     * Attribute that represents the class logger.
     */
    private static  Logger LOGGER = Logger.getLogger(UserModuleBO.class);

    /**
     * Attribute that allows to interact with the persistence configuration context.
     */
    @Inject
    private IConfigurationEntityManager em;
    
    
    /**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#getUser(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  UsersPOJO getUser( String login) throws DatabaseException {
				
		if (login == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_034));
		}

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_USER_LOGIN, login);
			List<UsersPOJO> userList = (List<UsersPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_USER_BY_LOGIN, parameters);

			if (userList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_037, new Object[ ] { login }));
			}
			
			UsersPOJO userPOJO = null;

			if (!userList.isEmpty()) {
				userPOJO = userList.get(NumberConstants.INT_0);
			}
		
			return userPOJO;

		} catch (PersistenceException pe) {

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, Language.getFormatMessage(LanguageKeys.DB_035, new Object[ ] { login }), pe);		

		} 
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#modifyUser(es.gob.signaturereport.persistence.configuration.model.pojo.UsersPOJO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  void modifyUser( UsersPOJO userPOJO) throws DatabaseException {
		
		if (userPOJO == null || userPOJO.getUserLogin() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_034));
		}
		
		PersonaldataPOJO pd = null;
		if (userPOJO.getPersonalData() != null && userPOJO.getPersonalData().getPdPk() != null) {
					
			pd = getPerson(userPOJO.getPersonalData().getPdPk());
			if (pd.getEndingtime() != null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_032, new Object[ ] { userPOJO.getPersonalData().getPdPk(), pd.getEndingtime().toString() }));
			}
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_USER_LOGIN, userPOJO.getUserLogin());
		List<UsersPOJO> userList = (List<UsersPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_USER_BY_LOGIN, parameters);
		
		if (userList.isEmpty()) {
			throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_036, new Object[ ] { userPOJO.getUserLogin() }));
		}
		if (userList.size() > 1) {
			throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_037, new Object[ ] { userPOJO.getUserLogin() }));
		}
		UsersPOJO userData = (UsersPOJO) userList.get(0);
		
		if (userPOJO.getUserPass() != null) {
			userData.setUserPass(userPOJO.getUserPass());
		}
		if (pd != null) {
			userData.setPersonalData(pd);
		}
		userData.setUserLocked(userPOJO.getUserLocked());
		
		em.merge(userData);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#getPerson(java.lang.Long)
	 */
	@Override
	public  PersonaldataPOJO getPerson( Long personPk) throws DatabaseException {
		
		if (personPk == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_034));
		}
		
		return (PersonaldataPOJO) em.load(PersonaldataPOJO.class, personPk);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#getAllPersons()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List<PersonaldataPOJO> getAllPersons() throws DatabaseException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		return (List<PersonaldataPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_ALL_PERSONS, parameters);
			
	}
		
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#getAllUsers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  List<UsersPOJO> getAllUsers() throws DatabaseException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		return (List<UsersPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_ALL_USERS, parameters);
			
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#modifyPerson(java.lang.Long, es.gob.signaturereport.configuration.items.Person)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public  void modifyPerson( PersonaldataPOJO person) throws DatabaseException {
		
		if (person == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_019));
		}
				
		if (person.getPdPk() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_022));
		}

		if (person.getPdName() == null || person.getPdSurname() == null || person.getPdPhone() == null || person.getPdEmail() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_019));
		}
		
		try {
						
			 PersonaldataPOJO personalData = (PersonaldataPOJO) em.load(PersonaldataPOJO.class, person.getPdPk());
			
			if (personalData == null) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_023, new Object[ ] { person.getPdPk() }));
			} else {
				// Verificamos que no se asigne datos de otro usuario.
								
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(IParametersQueriesConstants.PARAM_NAME, person.getPdName());
				parameters.put(IParametersQueriesConstants.PARAM_SURNAME, person.getPdSurname());
				parameters.put(IParametersQueriesConstants.PARAM_PHONE, person.getPdPhone());
				parameters.put(IParametersQueriesConstants.PARAM_EMAIL, person.getPdEmail());
				List<PersonaldataPOJO> personList = (List<PersonaldataPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PERSON, parameters);
				
				if (personList.isEmpty()) {
					person.setCreationtime(personalData.getCreationtime());
					em.merge(person);
					
				} else {
					if (personList.get(0).getPdPk() != person.getPdPk()) {
						throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getMessage(LanguageKeys.DB_021));
					}
				}
			}
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_024, new Object[ ] { person.getPdPk() });
			LOGGER.error(msg, pe);		
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#createPerson(es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Long createPerson(PersonaldataPOJO personalData) throws DatabaseException {
		
		if (personalData == null || personalData.getPdName() == null || personalData.getPdSurname() == null || personalData.getPdPhone() == null || personalData.getPdEmail() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_019));
		}
		
		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_NAME, personalData.getPdName());
			parameters.put(IParametersQueriesConstants.PARAM_SURNAME, personalData.getPdSurname());
			parameters.put(IParametersQueriesConstants.PARAM_PHONE, personalData.getPdPhone());
			parameters.put(IParametersQueriesConstants.PARAM_EMAIL, personalData.getPdEmail());
			List<PersonaldataPOJO> personList = (List<PersonaldataPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PERSON, parameters);
			
			if (personList.isEmpty()) {
								
				 PersonaldataPOJO personalDataPOJO = new PersonaldataPOJO();
				personalDataPOJO.setPdName(personalData.getPdName());
				personalDataPOJO.setPdSurname(personalData.getPdSurname());
				personalDataPOJO.setPdPhone(personalData.getPdPhone());
				personalDataPOJO.setPdEmail(personalData.getPdEmail());
				personalDataPOJO.setCreationtime(new Date());
				
				em.persist(personalDataPOJO);
				
				return personalDataPOJO.getPdPk();
				
			} else {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getMessage(LanguageKeys.DB_021));
			}
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_020, new Object[ ] { personalData.getPdName() + " " + personalData.getPdSurname() });
			LOGGER.error(msg, pe);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#deleteUser(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void deleteUser(String login) throws DatabaseException {
		if (login == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_034));
		}
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_USER_LOGIN, login);
			List<UsersPOJO> userList = (List<UsersPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_USER_BY_LOGIN, parameters);
			
			if (userList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_036, new Object[ ] { login }));
			}
			if (userList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_037, new Object[ ] { login }));
			}
			
			UsersPOJO userData = (UsersPOJO) userList.get(0);
			userData.setEndingtime(new Date());
			
			em.merge(userData);
						
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_040, new Object[ ] { login });
			LOGGER.error(msg, pe);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#createUser(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public  void createUser( UsersPOJO newUser) throws DatabaseException {

		if (newUser.getUserLogin() == null || newUser.getUserPass() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_029));
		}

		if (newUser.getPersonalData() != null && newUser.getPersonalData().getPdPk() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_030));
		}
		
		try {
					
			if (newUser.getPersonalData() == null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_023, new Object[ ] { newUser.getPersonalData().getPdPk() }));
			}else if (newUser.getPersonalData().getEndingtime() != null) {
				throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_032, new Object[ ] { newUser.getPersonalData().getPdPk(), newUser.getPersonalData().getEndingtime().toString() }));
			} else {
							
				if (getUser(newUser.getUserLogin()) == null) {
					
					em.persist(newUser);
					
				} else {
					throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_033, new Object[ ] { newUser.getUserLogin() }));
				}
			}
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_031, new Object[ ] { newUser.getUserLogin() });
			LOGGER.error(msg, pe);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUserModuleBO#deletePerson(java.lang.Long)
	 */
	@Override
	public void deletePerson(Long personId) throws DatabaseException {
		if (personId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_022));
		}
		
		try {
			
			PersonaldataPOJO personalData = getPerson(personId);
			if (personalData == null) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_023, new Object[ ] { personId }));
			} else {
				if (personalData.getApplications() != null && !personalData.getApplications().isEmpty()) {
					throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getMessage(LanguageKeys.DB_027));
				}
				if (personalData.getUsers() != null && !personalData.getUsers().isEmpty()) {
					throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getMessage(LanguageKeys.DB_028));
				}
				personalData.setEndingtime(new Date());
				
				em.merge(personalData);				
			}
		} catch (PersistenceException he) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_025, new Object[ ] { personId });
			LOGGER.error(msg, he);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,he);
		}
	}

}
