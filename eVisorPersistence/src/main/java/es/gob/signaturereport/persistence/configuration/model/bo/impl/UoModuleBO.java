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
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.bo.impl.UoModuleBO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UoPOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;

/**
 * <p>Class manager for the Business Objects in configuration module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UoModuleBO implements IUoModuleBO {

	 /**
	 * Attribute that represents . 
	 */
	private static final long serialVersionUID = -4255556263695404814L;

	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(UoModuleBO.class);

	 /**
	  * Attribute that represents the identifier used to identify the root unit. 
	  */
	public static final String ROOT_UNIT_ID = "ROOT";

	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
	@Inject
    private IConfigurationEntityManager em;

	@Override
	public UoPOJO getUO(String unitId, boolean recursive) throws DatabaseException {

		if (unitId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_004));
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_UO_ID, unitId);
		UoPOJO uoPOJO = (UoPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_UO_BY_ID, parameters);

		return uoPOJO;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO#createUO(java.lang.String, java.lang.String)
	 */
	@Override
	public void createUO(final String unitId, final String name, final String parentId) throws DatabaseException {

		if (unitId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_002));
		}

		UoPOJO uoPOJO = getUO(unitId, Boolean.FALSE);

		if (uoPOJO == null) {
			if (parentId == null) {

				uoPOJO = new UoPOJO();
				uoPOJO.setCreationtime(new Date());
				uoPOJO.setUoPk(null);
				uoPOJO.setUoId(unitId);
				uoPOJO.setUoName(name);
				UoPOJO uoParent = new UoPOJO();
				uoParent.setUoPk(getRootPk());
				uoPOJO.getUosForParentUo().add(uoParent);

				em.persist(uoPOJO);

			} else {

				final UoPOJO uoParent = getUO(parentId, Boolean.FALSE);

				if (uoParent == null) {
					throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_006, new Object[ ] { parentId }));
				} else {
					uoPOJO = new UoPOJO();
					uoPOJO.setCreationtime(new Date());
					uoPOJO.setUoPk(null);
					uoPOJO.setUoId(unitId);
					uoPOJO.setUoName(name);
					uoPOJO.getUosForParentUo().add(uoParent);

					em.persist(uoPOJO);
				}
			}
		} else {
			throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_005, new Object[ ] { unitId }));
		}

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO#getRootPk()
	 */
	@Override
	public Long getRootPk() {

//		try {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_UO_ID, ROOT_UNIT_ID);
		UoPOJO uoPOJO = (UoPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_UO_BY_ID, parameters);

		return uoPOJO.getUoPk();

//		} catch (HibernateException e) {
//			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
//			logger.error(Language.getMessage(LanguageKeys.DB_089), e);
//		} finally {
//			UtilsResources.safeCloseSession(session);
//		}
		//return null;
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO#updateUO(es.gob.signaturereport.persistence.configuration.model.pojo.UoPOJO)
	 */
	@Override
	public void updateUO(final UoPOJO uoPOJO) throws DatabaseException {

		// Comprobamos que la aplicaci�n no sea nula y que no exista una
		// aplicación con el mismo identificador
		if (uoPOJO != null && uoPOJO.getUoId() != null) {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_APPLICATION_ID, uoPOJO.getUoId());
			ApplicationPOJO applicationPOJOFinded = (ApplicationPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_APPLICATION_BY_ID, parameters);

			if (applicationPOJOFinded != null && !applicationPOJOFinded.getAppId().equals(uoPOJO.getUoId())) {

				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM,Language.getMessage(LanguageKeys.DB_091));
			}

			//LOGGER.debug(Language.getFormatMessage("", new Object[ ] { uoPOJO.getUoId() }));

			em.merge(uoPOJO);
		} else {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_090));

		}
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO#deleteUO(java.lang.String)
	 */
	@Override
	public void deleteUO(final String unitId) throws DatabaseException {

		UoPOJO uoPOJO = getUO(unitId, Boolean.FALSE);

		if (uoPOJO != null) {
			deleteUoRec(uoPOJO);

			try {
				updateUO(uoPOJO);
			} catch (PersistenceException pe) {

				String msg = Language.getFormatMessage(LanguageKeys.DB_007, new Object[ ] { unitId });
				throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
			}

		} else {
			throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_006, new Object[ ] { unitId }));
		}

	}

	/**
	 * Recursive method for deleting units.
	 * @param unit	Unit to delete.
	 */
	private void deleteUoRec(final UoPOJO unit) {
		if (unit != null && unit.getEndingtime() == null) {
			unit.setEndingtime(new Date());
			Iterator<ApplicationPOJO> appIt = unit.getApplications().iterator();
			while (appIt.hasNext()) {
				final ApplicationPOJO appPOJO = appIt.next();
				if (appPOJO.getEndingtime() == null) {
					appPOJO.setEndingtime(new Date());
				}
			}
			Iterator<UoPOJO> unitIt = unit.getUosForUo().iterator();
			while (unitIt.hasNext()) {
				final UoPOJO unitChild = unitIt.next();
				deleteUoRec(unitChild);
			}
		}
		return;
	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO#modifyUOName(java.lang.String, java.lang.String)
	 */
	@Override
	public void modifyUOName(final String unitId, final String name) throws DatabaseException {

		// 1. Comprobamos los par�metros de entrada
		if (unitId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_002));
		}
		if (name == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_004));
		}

		// 2. Procedemos a modificar el elemento

		try {

			UoPOJO uoPOJO = getUO(unitId, Boolean.FALSE);

			if (uoPOJO != null) {
				uoPOJO.setUoName(name);

				em.merge(uoPOJO);

			} else {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_006, new Object[ ] { unitId }));
			}
		} catch (PersistenceException pex) {

			String msg = Language.getFormatMessage(LanguageKeys.DB_010, new Object[ ] { name, unitId });

			LOGGER.error(msg, pex);

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pex);
		} 
	}

}
