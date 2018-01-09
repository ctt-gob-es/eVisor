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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.ApplicationConfigurationFacade.java.</p>
 * <b>Description:</b><p>This class provides all methods to management of configuration of applications and organizational units.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.UnitOrganization;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateReportPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UoPOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.ConversionUtils;

/**
 * <p>This class provides all methods to management of configuration of applications and organizational units.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
@Stateless
@ApplicationConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ApplicationConfigurationFacade implements ApplicationConfigurationFacadeI {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(ApplicationConfigurationFacade.class);

	/**
	 * Attribute that allows to operate with information about applications of the platform.
	 */
	@Inject
	private IApplicationModuleBO appBO;

	/**
	 * Attribute that allows to operate with information about the organizational units of the platform.
	 */
	@Inject
	private IUoModuleBO uoBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;

	/**
	 * Constructor method for the class ApplicationConfigurationFacade.java.
	 * 
	 */
	public ApplicationConfigurationFacade() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#getAplicationData(java.lang.String)
	 */
	@Override
	public ApplicationData getApplicationData(final String applicationId) throws ConfigurationException {

		if (applicationId == null) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_067));
		}

		final ApplicationPOJO appPOJO;
		try {
			appPOJO = appBO.getApplication(applicationId);

			final ApplicationData application = new ApplicationData();
			application.setName(appPOJO.getAppName());
			final Person manager = new Person();
			final PersonaldataPOJO pdPOJO = appPOJO.getPersonalData();
			ConversionUtils.personalData2Person(pdPOJO, manager);
			application.setManager(manager);
			application.setPlatformId(appPOJO.getPlatform().getPfId());
			application.setAuthenticationType(appPOJO.getAuthType());
			
			if (appPOJO.getAuthType().equals(ConfigurationFacade.CERTIFICATE_AUTHENTICATION)) {
				application.setAuthCertAlias(appPOJO.getCertificate().getCertId());
			} else if (appPOJO.getAuthType().equals(ConfigurationFacade.USER_PASS_AUTHENTICATION)) {
				application.setAuthUser(appPOJO.getAuthUser());
				application.setAuthPass(appPOJO.getAuthPass());
			}

			final Iterator<TemplateReportPOJO> templates = appPOJO.getTemplatereports().iterator();
			final ArrayList<String> templateList = new ArrayList<String>();

			while (templates.hasNext()) {
				final TemplateReportPOJO template = templates.next();
				templateList.add(template.getTrId());
			}

			application.setTemplates(templateList);

			return application;

		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_007, new Object[ ] { applicationId }), e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.DUPLICATE_ITEM) {
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.ITEM_NOT_FOUND){ 
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#createUO(java.lang.String, java.lang.String)
	 */
	@Override
	public void createUO(String unitId, String name) throws ConfigurationException {
		if (unitId == null || name == null || unitId.trim().equals("") || name.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_006));
		}
		String parentId = getParentId(unitId);
		try {

			uoBO.createUO(unitId, name, parentId);

		} catch (DatabaseException e) {

			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.DB_001, new Object[ ] { unitId });
			LOGGER.error(msg, e);
							
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_007, new Object[ ] { unitId }), e);
			
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.DUPLICATE_ITEM) {
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			}else if(e.getCode()== DatabaseException.ITEM_NOT_FOUND){ 
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#deleteUO(java.lang.String)
	 */
	@Override
	public void deleteUO(String unitId) throws ConfigurationException {
		if (unitId == null || unitId.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_008));
		}
		try {
			uoBO.deleteUO(unitId);

		} catch (DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
						
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_009, new Object[ ] { unitId }), e);
			
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#modifyUO(java.lang.String, java.lang.String)
	 */
	@Override
	public void modifyUO(String unitId, String name) throws ConfigurationException {
		if (unitId == null || name == null || unitId.trim().equals("") || name.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_010));
		}
		
		try {

			uoBO.modifyUOName(unitId, name);

		} catch (DatabaseException e) {

			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());

			String msg =Language.getFormatMessage(LanguageKeys.CONF_011, new Object[ ] { unitId });
			LOGGER.error(msg, e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR,msg,e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#modifyApplication(java.lang.String, es.gob.signaturereport.configuration.items.ApplicationData)
	 */
	@Override
	public void modifyApplication(String applicationId, ApplicationData appData) throws ConfigurationException {
		if (applicationId == null || applicationId.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_067));
		}
		validateApp(appData);
		try {
			appBO.modifyApplication(applicationId, appData);
			
		} catch (DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			
			String msg = Language.getFormatMessage(LanguageKeys.CONF_076, new Object[]{applicationId});
			LOGGER.error(msg, e);
			if(e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode() == DatabaseException.ITEM_NOT_FOUND){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#createApplication(java.lang.String, es.gob.signaturereport.configuration.items.ApplicationData)
	 */
	@Override
	public void createApplication(String applicationId, ApplicationData appData) throws ConfigurationException {
		if (applicationId == null || applicationId.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_067));
		}
		validateApp(appData);
		try {
			String uoId = getParentId(applicationId);
			
			appBO.createApplication(applicationId, appData, uoId);

		} catch (DatabaseException e) {

			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
						
			String msg = Language.getFormatMessage(LanguageKeys.CONF_077, new Object[]{applicationId});
			
			LOGGER.error(msg, e);

			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.DUPLICATE_ITEM) {
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#getUnitOrganization(java.lang.String, boolean)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UnitOrganization getUnitOrganization(final String unitId, final boolean recursive) throws ConfigurationException {

		if (unitId == null || unitId.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_010));
		}

		UnitOrganization unit = null;

		try {

			UoPOJO uoPOJO = (UoPOJO) uoBO.getUO(unitId, recursive);
						
			unit = new UnitOrganization(uoPOJO.getUoId(), uoPOJO.getUoName());

			if (recursive) {
				addUoAndAppsRec(uoPOJO, unit);
			} else {
				final Iterator<ApplicationPOJO> appIt = uoPOJO.getApplications().iterator();
				while (appIt.hasNext()) {
					final ApplicationPOJO app = appIt.next();
					if (app.getEndingtime() == null) {
						unit.addApplication(app.getAppId(), app.getAppName());
					}
				}
				final Iterator<UoPOJO> unitIt = uoPOJO.getUosForUo().iterator();
				while (unitIt.hasNext()) {
					UoPOJO unitChild = unitIt.next();
					if (unitChild.getEndingtime() == null) {
						UnitOrganization uoChild = new UnitOrganization(unitChild.getUoId(), unitChild.getUoName());
						unit.addSubUnit(uoChild.getUnitId(), uoChild);
					}
				}
			}

		} catch (NumberFormatException e) {

			e.printStackTrace();
			LOGGER.equals(e.getMessage());

		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_078, new Object[]{unitId});
			LOGGER.error(msg,e);
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else if (e.getCode() == DatabaseException.ITEM_NOT_FOUND) {
				return null;
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
			}
		} catch (Exception ex) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_078, new Object[]{unitId});
			LOGGER.error(msg,ex);
			
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,ex);
			
		}
		

		return unit;

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#removeApplication(java.lang.String)
	 */
	@Override
	public void removeApplication(String applicationId) throws ConfigurationException {

		if (applicationId == null || applicationId.trim().equals("")) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_067));
		}
		try {
			
			appBO.removeApplication(applicationId);

		} catch (DatabaseException e) {
			
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
				
			LOGGER.error(e.getDescription());
			int code = ConfigurationException.UNKNOWN_ERROR;
			if (e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS) {
				code = ConfigurationException.INVALID_INPUT_PARAMETERS;
			} else if (e.getCode()==DatabaseException.ITEM_NOT_FOUND) {
				code = ConfigurationException.ITEM_NO_FOUND;
			}
			throw new ConfigurationException(code, e.getDescription(), e);
		}
	}
	/**
	 * Method that extracts the parent identifier.
	 * @param unitId	Child identifier. Must be formatted: <br/>
	 * 					{parentUnitId}.{itemId}.
	 * @return	The parent identifier. Null, if the unit was created in the root.
	 */
	private String getParentId(final String unitId) {
		String parentId = null;
		int i = unitId.lastIndexOf('.');
		if (i > 0) {
			parentId = unitId.substring(0, i);
		}
		return parentId;
	}

	/**
	 * Checks the attribute of {@link ApplicationData} object.
	 * @param appData	Application information to check.
	 * @throws ConfigurationException	If the application information is invalid.
	 */
	private void validateApp(ApplicationData appData) throws ConfigurationException{
		
		if(appData==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_069));
		}else if(appData.getName()==null || appData.getName().trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_070));
		}else if(appData.getManager()== null || appData.getManager().getPersonId()==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_071));
		}else if(appData.getPlatformId()==null || appData.getPlatformId().trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_072));
		}
		
		if(appData.getAuthenticationType() == ConfigurationFacade.WITHOUT_AUTHENTICATION){
			appData.setAuthPass(null);
			appData.setAuthUser(null);
		}else if(appData.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION){
			if(appData.getAuthUser() == null){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_073));
			}
			if(appData.getAuthPass() == null){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_074));
			}
		}else if(appData.getAuthenticationType() == ConfigurationFacade.CERTIFICATE_AUTHENTICATION){
			if(appData.getAuthCertAlias() == null){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_073));
			}
			appData.setAuthPass(null);
			appData.setAuthUser(null);
		}else{
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_075, new Object[]{""+appData.getAuthenticationType()}));
		}
		
	}
	
	
	/**
	 * Include into an {@link UnitOrganization} object the subunits and applications registered in the supplied unit.
	 * @param unit	A {@link Uo} object that contains the information registered into database.
	 * @param uo	A {@link UnitOrganization} that will include the information.
	 */
	private void addUoAndAppsRec(final UoPOJO unit, final UnitOrganization uo) {
		if (unit != null && unit.getEndingtime() == null) {
			Iterator<ApplicationPOJO> appIt = unit.getApplications().iterator();
			while (appIt.hasNext()) {
				ApplicationPOJO app = appIt.next();
				if (app.getEndingtime() == null) {
					uo.addApplication(app.getAppId(), app.getAppName());
				}
			}
			Iterator<UoPOJO> unitIt = unit.getUosForUo().iterator();
			while (unitIt.hasNext()) {
				UoPOJO unitChild = unitIt.next();
				if (unitChild.getEndingtime() == null) {
					UnitOrganization uoChild = new UnitOrganization(unitChild.getUoId(), unitChild.getUoName());
					addUoAndAppsRec(unitChild, uoChild);
					uo.addSubUnit(uoChild.getUnitId(), uoChild);
				}
			}
		}
		return;
	}

}
