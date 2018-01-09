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
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.bo.impl.ApplicationModuleBO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.CertificatePOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PersonaldataPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PlatformPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateReportPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UoPOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <p>Class manager for the Business Objects in configuration module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ApplicationModuleBO implements IApplicationModuleBO {

    /**
	 * Attribute that represents. 
	 */
	private static final long serialVersionUID = 3548851102090204045L;

	/**
	 * Constant indicating that the authorization of the web service request is made by user and password.
	 */
	public static final int USER_PASS_AUTHENTICATION = 2;
	/**
	 * Constant indicating that the authorization of the web service request is made by certificate.
	 */
	public static final int CERTIFICATE_AUTHENTICATION = 1;

	/**
	 * Constant that indicates that the web service request is without authentication.
	 */
	public static final int WITHOUT_AUTHENTICATION = 0;

    /**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private IConfigurationEntityManager em;

    /**
	 * Attribute that allows to operate with information about the organizational units of the platform.
	 */
	@Inject
	private transient IUoModuleBO uoBO;
	
	/**
	 * Attribute that allows to operate with information about the templates of application.
	 */
	@Inject
	private transient ITemplateModuleBO templateBO;

    // CHECKSTYLE:ON
    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationModuleBO.class);

    /**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationsModuleBO#createApplication(String.applicationId,es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO,String.uoId)
	 */
    @SuppressWarnings("unchecked")
	public final void createApplication(final String applicationId, final ApplicationData appData, final String uoId) throws DatabaseException {

		if (applicationId == null || applicationId.equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_094));
		}
		if (appData == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_108));
		}
		if (appData.getManager() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_109));
		}
		if (appData.getName() == null || appData.getName().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_110));
		}
		if (appData.getPlatformId() == null || appData.getPlatformId().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_111));
		}

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_APPLICATION_ID, applicationId);
			List<ApplicationPOJO> applicationList = (List<ApplicationPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_APPLICATION_BY_ID, parameters);

			if (applicationList.isEmpty()) {
				ApplicationPOJO application = new ApplicationPOJO();
				application.setAppId(applicationId);
				application.setAppName(appData.getName());
				//Unidad organizativa
				UoPOJO uo = null;
				if (uoId == null) {
					uo = new UoPOJO();
					uo.setUoPk(uoBO.getRootPk());
				} else {

					parameters = new HashMap<String, Object>();
					parameters.put(IParametersQueriesConstants.PARAM_UO_ID, uoId);
					List<UoPOJO> uoList = (List<UoPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_UO_BY_ID, parameters);

					if(uoList.size() != 1) {
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS,Language.getFormatMessage(LanguageKeys.DB_006, new Object[ ] { uoId }));
					} else {
						uo = uoList.get(0);
					}
				}

				application.setUo(uo);

				//Plataforma

				parameters = new HashMap<String, Object>();
				parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, appData.getPlatformId());
				List<PlatformPOJO> platforms = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);

				if (platforms.size() == 1) {
					application.setPlatform(platforms.get(0));
				} else {
					throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_057, new Object[ ] { appData.getPlatformId() }));
				}

				//Responsable

				PersonaldataPOJO person = (PersonaldataPOJO) em.load(PersonaldataPOJO.class,appData.getManager().getPersonId());

				if(person == null){
					throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_023));
				}
				application.setPersonalData(person);

				//Plantillas
				if(appData.getTemplates() != null && !appData.getTemplates().isEmpty()) {

					parameters = new HashMap<String, Object>();
					parameters.put(IParametersQueriesConstants.PARAM_TRPKS_ID_LIST, appData.getTemplates());
					List<Long> pkTemplateList = (List<Long>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TRPKS_BY_ID_LIST, parameters);

					if (pkTemplateList.size() != appData.getTemplates().size()) {
						String templateIds = appData.getTemplates().get(0);
						for (int i=1;i<appData.getTemplates().size();i++) {
							templateIds = templateIds + ", " + appData.getTemplates().get(i);
						}
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_124, new Object[]{templateIds}));
					} else {
						Iterator<Long> pksIt = pkTemplateList.iterator();
						while (pksIt.hasNext()) {
							TemplateReportPOJO tr = new TemplateReportPOJO();
							tr.setTrPk(pksIt.next());
							application.addTemplate(tr);
						}
					}
				}

				//Autorización WS
				application.setAuthType(appData.getAuthenticationType());
				if (appData.getAuthenticationType() == CERTIFICATE_AUTHENTICATION) {
					Long certPk = getCertificatePk(appData.getAuthCertAlias(), Constants.SOAP_AUTH_KEYSTORE );
					if (certPk==null) {
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS,Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { appData.getAuthCertAlias(),Constants.SOAP_AUTH_KEYSTORE }));
					} else {
						CertificatePOJO cert = new CertificatePOJO();
						cert.setCertPk(certPk);
						application.setCertificate(cert);
					}
				} else if (appData.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION) {
					application.setAuthUser(appData.getAuthUser());
					application.setAuthPass(appData.getAuthPass());
				}
				application.setCreationtime(new Date());

				try {
					em.persist(application);
				} catch (PersistenceException pex) {

					String msg = Language.getFormatMessage(LanguageKeys.DB_035, new Object[ ] { applicationId });
					
					LOGGER.error(msg, pex);

					throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pex);
				}

			} else {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_092, new Object[ ] { applicationId }));
			}

	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationsModuleBO#updateApplication(es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO)
	 */
	@Override
	public final void updateApplication(final ApplicationPOJO applicationPOJO) throws DatabaseException {

		// Comprobamos que la aplicación no sea nula y que no exista una
		// aplicación con el mismo identificador
		if (applicationPOJO != null && applicationPOJO.getAppId() != null) {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_APPLICATION_ID, applicationPOJO.getAppId());
			ApplicationPOJO applicationPOJOFinded = (ApplicationPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_APPLICATION_BY_ID, parameters);

			if (applicationPOJOFinded != null && !applicationPOJOFinded.getAppId().equals(applicationPOJO.getAppId())) {

				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM,Language.getMessage(LanguageKeys.DB_091));
			}

			LOGGER.debug(Language.getFormatMessage("", new Object[ ] { applicationPOJO.getAppId() }));

			try {
				em.merge(applicationPOJO);
			} catch (PersistenceException pex) {

				// TODO: ¿Gestinar la alarma?
			}
		} else {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_090));

		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationModuleBOI#modifyApplication(java.lang.String)
	 */
	@Override
	
	public final void removeApplication(final String applicationId) throws DatabaseException {

		try {
			
			final ApplicationPOJO appPOJO = getApplication(applicationId);
			
			if (appPOJO == null) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_091, new Object[ ] { applicationId }));
			} else{
				appPOJO.setEndingtime(new Date());
				em.merge(appPOJO);
			}

		} catch (PersistenceException pex) {

			String msg = Language.getFormatMessage(LanguageKeys.DB_035, new Object[ ] { applicationId });
			LOGGER.error(msg, pex);

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pex);

		} catch (DatabaseException dex) {

			String msg = Language.getFormatMessage(LanguageKeys.DB_035, new Object[ ] { applicationId });
			LOGGER.error(msg, dex);

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, dex);
		}

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationModuleBOI#modifyApplication(java.lang.String)
	 */
	@Override
	public final ApplicationPOJO getApplication(final String applicationId) throws DatabaseException {

		if (applicationId == null || applicationId.trim().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_090));
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_APPLICATION_ID, applicationId);
		ApplicationPOJO appPOJO = null;

		try {	
			appPOJO = (ApplicationPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_APPLICATION_BY_ID, parameters);
			
		} catch (PersistenceException pex) {

			final String msg = Language.getFormatMessage(LanguageKeys.DB_097, new Object[ ] { applicationId });

			LOGGER.error(msg);

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pex);
		}

		return appPOJO;
	}

	/**
	 * Gets the certificate primary key associated to certificate alias supplied.
	 * @param session	Database session.
	 * @param alias		Certificate alias.
	 * @param keystoreId	Keystore identifier.
	 * @return			Primary key. Null if the certificate doesn't exist or exist more tha one.
	 * @throws DatabaseException	Database error.
	 */
	//CHECKSTYLE:OFF -- this operation throw a "HibernateException" and this must be controlled by the parent method.
	@SuppressWarnings("unchecked")
	private final Long getCertificatePk(final String alias, final String keystoreId) throws DatabaseException{
		//CHECKSTYLE:ON
		Long pk = null;

		if (alias == null || alias.trim().equals("") || keystoreId == null || keystoreId.trim().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_090));
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_CERT_ALIAS, alias);
		parameters.put(IParametersQueriesConstants.PARAM_KEYSTORE_ID, keystoreId);
		List<CertificatePOJO> listCertsPOJO = (List<CertificatePOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_CERTIFICATE_BY_ID, parameters);

		if (listCertsPOJO != null && listCertsPOJO.size() == NumberConstants.INT_1)
		{
			pk = listCertsPOJO.get(NumberConstants.INT_0).getCertPk();
		}

		return pk;
	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationModuleBOI#modifyApplication(java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final void modifyApplication(final String applicationId, final ApplicationData appData) throws DatabaseException {

		if (applicationId == null || applicationId.equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_094));
		}
		if (appData == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_108));
		}
		if (appData.getManager() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_109));
		}
		if (appData.getName() == null || appData.getName().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_110));
		}
		if (appData.getPlatformId() == null || appData.getPlatformId().equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_111));
		}

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_APPLICATION_ID, applicationId);
			List<ApplicationPOJO> applicationList = (List<ApplicationPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_APPLICATION_BY_ID, parameters);
			if (applicationList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_036, new Object[ ] { applicationId }));
			} else if (applicationList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_092, new Object[ ] { applicationId }));
			} else {
				ApplicationPOJO application = applicationList.get(0);
				//Nombre de la aplicación
				application.setAppName(appData.getName());

				//Plataforma

				parameters = new HashMap<String, Object>();
				parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, appData.getPlatformId());
				List<PlatformPOJO> platforms = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);

				if (platforms.size() == 1) {
					application.setPlatform(platforms.get(0));
				} else {
					throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_057, new Object[ ] { appData.getPlatformId() }));
				}

				//Responsable

				PersonaldataPOJO person = (PersonaldataPOJO) em.load(PersonaldataPOJO.class,appData.getManager().getPersonId());

				if(person == null){
					throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_023));
				}
				application.setPersonalData(person);

				//Plantillas
				if(appData.getTemplates() != null && !appData.getTemplates().isEmpty()) {

					List<String> templates = appData.getTemplates();
					
						Iterator<String> idsIt = templates.iterator();
						while (idsIt.hasNext()) {
							String id = idsIt.next();
							boolean finded = false;
							
							if (application.getTemplatereports()!= null) {
								for (TemplateReportPOJO trp:application.getTemplatereports()) {
									if (trp.getTrId().equals(id)) {
										finded = true;
									}
								}
							}
							if (!finded) {
								TemplateReportPOJO tr = templateBO.getTemplateReport(id);
								Set<ApplicationPOJO> apps = tr.getApplications();
								apps.add(application);
								tr.setApplications(apps);
								em.merge(tr);
							}
							
						}
					
				}

				//Autorización WS
				application.setAuthType(appData.getAuthenticationType());
				if (appData.getAuthenticationType() == CERTIFICATE_AUTHENTICATION) {
					Long certPk = getCertificatePk(appData.getAuthCertAlias(), Constants.SOAP_AUTH_KEYSTORE );
					if (certPk == null) {
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS,Language.getFormatMessage(LanguageKeys.DB_051, new Object[ ] { appData.getAuthCertAlias(),Constants.SOAP_AUTH_KEYSTORE }));
					} else {
						CertificatePOJO cert = new CertificatePOJO();
						cert.setCertPk(certPk);
						application.setCertificate(cert);
					}
				} else if (appData.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION) {
					application.setAuthUser(appData.getAuthUser());
					application.setAuthPass(appData.getAuthPass());
				}

				em.merge(application);
			}

		} catch (PersistenceException pex) {

			String msg = Language.getFormatMessage(LanguageKeys.DB_096, new Object[ ] { applicationId });
			LOGGER.error(msg, pex);

			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,pex);
		} 
	}

}
