package es.gob.signaturereport.persistence.malarm.model.bo.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.em.IAuditEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO;
import es.gob.signaturereport.persistence.malarm.model.pojo.AlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO;
import es.gob.signaturereport.persistence.malarm.model.pojo.ReceiversPOJO;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;

/**
 * <p>Class manager for the Business Objects in alarm module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AlarmModuleBO implements IAlarmModuleBO {

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -3218225694113988840L;

    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AlarmModuleBO.class);

	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private transient IAuditEntityManager em;

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#getAlarm(java.lang.String)
	 */
	@Override
	public AlarmPOJO getAlarm(final String alarmId) throws DatabaseException {

		AlarmPOJO alarm = null;
		
		try {
			alarm = (AlarmPOJO) em.load(AlarmPOJO.class, alarmId);
		} catch (PersistenceException pe) {
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.DB_117));
		}
		return alarm;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#getReceiversListEMail(java.lang.String[])
	 */
	@Override
	public List<ReceiversPOJO> getReceiversListEMail(final String[ ] receivers) {

		List<Predicate> predList = new LinkedList<Predicate>();
		Predicate[ ] predArray = new Predicate[predList.size()];

		// Preparo la sentencia dinámica
		CriteriaBuilder critBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ReceiversPOJO> criteria = critBuilder.createQuery(ReceiversPOJO.class);
		Root<ReceiversPOJO> root = criteria.from(ReceiversPOJO.class);
		
		Root<ReceiversPOJO> receiver = criteria.from(ReceiversPOJO.class);

		// Añado la primera restricción LIKE
		predList.add(critBuilder.like(receiver.<String> get(IParametersQueriesConstants.PARAM_MAIL), receivers[0]));

		for (int i = 1; i < receivers.length; i++) {
			// Por cada correo adicional, añado otra restricción LIKE disyuntiva (OR)
			predList.add(critBuilder.or(critBuilder.like(receiver.<String> get(IParametersQueriesConstants.PARAM_MAIL), receivers[i])));
		}

		predList.toArray(predArray);
		// Combino las restricciones para formar el WHERE
		criteria = criteria.where(predArray);
		criteria.select(root);

		return em.createQuery(criteria).getResultList();
	}

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#insertReceiver(es.gob.signaturereport.persistence.malarm.model.pojo.ReceiversPOJO)
     */
    @Override
    public boolean insertReceiver(final ReceiversPOJO receiverPojo) throws DatabaseException {

		boolean result = false;
		// Comprobamos los parámetros de entrada.
		if (receiverPojo == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_113));
		} else {
			try {
				// Se inserta en base de datos.
				em.persist(receiverPojo);
				result = true;
				//LOGGER.debug(Language.getFormatResPersistenceGeneral(IPersistenceGeneralKeys.SEMBO_LOG002, new Object[ ] { receiverPojo.getRecPk() }));
			} catch (Exception e) {

				LOGGER.debug(Language.getMessage(LanguageKeys.DB_113));
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.DB_113));
			}
		}
		return result;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#insertLogAlarm(es.gob.signaturereport.persistence.malarm.model.pojo.LogAlarmPOJO)
     */
    @Override
    public boolean insertLogAlarm(final LogAlarmPOJO logAlarmPojo) throws DatabaseException {

		boolean result = false;
		// Comprobamos los parámetros de entrada.
		if (logAlarmPojo == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_118));
		} else {
			try {
				// Se inserta en base de datos.
				em.persist(logAlarmPojo);
				result = true;
				//LOGGER.debug(Language.getFormatResPersistenceGeneral(IPersistenceGeneralKeys.SEMBO_LOG002, new Object[ ] { receiverPojo.getRecPk() }));
			} catch (Exception e) {

				LOGGER.debug(Language.getMessage(LanguageKeys.DB_118));
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.DB_118));
			}
		}
		return result;
    }


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#setCommunicationTime(java.util.Date, java.lang.Long[])
	 */
	@Override
	public void setCommunicationTime(final Date commtime, final Long[ ] logAlarmPKs) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_LIST_LOG_ALARM_PK, Arrays.asList(logAlarmPKs));
		parameters.put(IParametersQueriesConstants.PARAM_COMMUNICATION_TIME, commtime);
		em.executeNamedQuery(IQueriesNamesConstants.QUERYNAME_SET_COMMUNICATION_TIME_LOG_ALARM, parameters);

	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#setLastCommunication(java.lang.String, java.util.Date)
	 */
	@Override
	public void setLastCommunication(String alarmId, Date commTime) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_ALARM_ID, alarmId);
		parameters.put(IParametersQueriesConstants.PARAM_LASTCOM_ALARM, commTime);
		em.executeNamedQuery(IQueriesNamesConstants.QUERYNAME_SET_LAST_COMMUNICATION_ALARM, parameters);

	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#getPendingAlarms(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LogAlarmPOJO> getPendingAlarms(String alarmId) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_ALARM_ID, alarmId);
		
		return (List<LogAlarmPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_GET_PENDING_ALARMS, parameters);
	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#updateAlarm(es.gob.signaturereport.persistence.malarm.model.pojo.AlarmPOJO)
	 */
	@Override
	public void updateAlarm(final AlarmPOJO alarmPOJO) throws DatabaseException {

		if (alarmPOJO != null && alarmPOJO.getAlarmId() != null) {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_ALARM_ID, alarmPOJO.getAlarmId());
			AlarmPOJO alarmPOJOFinded = (AlarmPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_ALARM_BY_ID, parameters);

			if (alarmPOJOFinded != null && !alarmPOJOFinded.getAlarmId().equals(alarmPOJO.getAlarmId())) {

				String msg = Language.getFormatMessage(LanguageKeys.DB_116, new Object[ ] { alarmPOJO.getAlarmId() });
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, msg);
			}

			try {
				em.merge(alarmPOJO);
			} catch (PersistenceException pex) {

				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.DB_091));
			}
		} else {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_090));

		}
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.malarm.model.bo.interfaz.IAlarmModuleBO#findAllAlarms()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AlarmPOJO> findAllAlarms() throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();

		List<AlarmPOJO> listaAlarmas = null;

		try {
			listaAlarmas = (List<AlarmPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_ALL_ALARMS, parameters);
		} catch (PersistenceException e) {

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, e.getMessage());
		}

		return listaAlarmas;
	}

}
