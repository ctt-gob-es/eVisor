package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.ArrayList;
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
import org.hibernate.HibernateException;

import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.configuration.items.WSServiceData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.CertificatePOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PlatformPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ServicePOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <p>Class manager for the Business Objects in platform module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PlatformModuleBO implements IPlatformModuleBO {
	
	/**
	 * Attribute that represents . 
	 */
	private static final long serialVersionUID = -2076568940281622175L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformModuleBO.class);
    
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private IConfigurationEntityManager em;
    
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO#getPlatform(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
	public PlatformPOJO getPlatform(String platformId) throws DatabaseException {
		if (platformId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_055));
		}
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, platformId);
			List<PlatformPOJO> platfList = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);
			
			if (platfList != null && platfList.size() > 0) {
				if (platfList.size() == 1) {
					return (PlatformPOJO) platfList.get(NumberConstants.INT_0);
				} else {
					throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_058, new Object[ ] { platformId }));
				}
			} else {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_057, new Object[ ] { platformId }));
			}
		} catch (PersistenceException e) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_056, new Object[ ] { platformId });
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,e);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO#createPlatform(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void createPlatform(String platformId, AfirmaData data) throws DatabaseException {
		if (platformId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_055));
		}
		if (data == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_059));
		}
		if (data.getApplicationId() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_060));
		}
		if (data.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION && (data.getAuthenticationUser() == null || data.getAuthenticationPassword() == null)) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_061));
		} else if (data.getAuthenticationType() == Constants.CERTIFICATE_AUTHENTICATION && data.getAuthenticationUser() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_061));
		}
		
		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, platformId);
			List<PlatformPOJO> platfList = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);
						
			if (platfList == null || platfList.isEmpty()) {
				PlatformPOJO platform = new PlatformPOJO();
				platform.setPfId(platformId);
				platform.setPfApp(data.getApplicationId());
				platform.setCreationtime(new Date());
				platform.setPfVersion(data.getVersion());
				platform.setAuthType(data.getAuthenticationType());
				if (data.getAuthenticationType() == Constants.CERTIFICATE_AUTHENTICATION) {
					Long authCertPk = getCertificatePk( data.getAuthenticationUser(), Constants.SOAP_SIGNER_KEYSTORE);
					if (authCertPk == null) {
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_064, new Object[ ] { data.getAuthenticationUser() }));
					} else {
						CertificatePOJO authCert = new CertificatePOJO();
						authCert.setCertPk(authCertPk);
						platform.setCertificateByAuthCert(authCert);
					}
					platform.setAuthUser(data.getAuthenticationUser());
				} else if (data.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION) {
					platform.setAuthUser(data.getAuthenticationUser());
					platform.setAuthPass(data.getAuthenticationPassword());
				}
				if (data.getSoapTrusted() != null) {
					Long soapCertPk = getCertificatePk(data.getSoapTrusted(), Constants.SOAP_TRUSTED_KEYSTORE);
					if (soapCertPk == null) {
						throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_064, new Object[ ] { data.getSoapTrusted() }));
					} else {
						CertificatePOJO soapCert = new CertificatePOJO();
						soapCert.setCertPk(soapCertPk);
						platform.setCertificateBySoaptrusted(soapCert);
					}
				}
								
				em.persist(platform);
				
				if (data.getServices() != null && !data.getServices().isEmpty()) {
					Iterator<String> servicesIds = data.getServices().keySet().iterator();
					while (servicesIds.hasNext()) {
						String serviceId = servicesIds.next();
						WSServiceData serviceData = data.getServices().get(serviceId);
						if (serviceData.getOperationName() != null && serviceData.getServicesLocation() != null) {
//							ServiceId servicePK = new ServiceId(serviceId, platform);
							ServicePOJO service = new ServicePOJO();
							service.setServiceId(serviceId);
							service.setPlatform(platform);
							service.setSLocation(serviceData.getServicesLocation());
							service.setSOperation(serviceData.getOperationName());
							service.setSTimeout(new Long(serviceData.getTimeOut()).longValue());
							
							em.persist(service);
						} else {
							throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_065, new Object[ ] { serviceId }));
						}
					}
				}
				
			} else {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getMessage(LanguageKeys.DB_063));
			}
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_062, new Object[ ] { platformId });
			LOGGER.error(msg, pe);
			
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO#getPlatformIds()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String[ ] getPlatformIds() throws DatabaseException {
		String[ ] ids = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			List<PlatformPOJO> platfList = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_ALL_ACTIVE_PLATFORMS, parameters);
					
			if (platfList != null && !platfList.isEmpty()) {
				ids = new String[platfList.size()];
				for (int i = 0; i < platfList.size(); i++) {
					ids[i] = ((PlatformPOJO) platfList.get(i)).getPfId();
				}
			}
		} catch (PersistenceException e) {
			
			String msg = Language.getMessage(LanguageKeys.DB_066);
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,e);
		}
		return ids;
	}
		
	/**
	 * Gets the certificate primary key associated to certificate alias supplied.
	 * @param session	Database session.
	 * @param alias		Certificate alias.
	 * @param keystoreId	Keystore identifier.
	 * @return			Primary key. Null if the certificate doesn't exist or exist more tha one.
	 * @throws HibernateException	Database error.
	 */
	//CHECKSTYLE:OFF -- this operation throw a "PersistenceException" and this must be controlled by the parent method.
	@SuppressWarnings("unchecked")
	private Long getCertificatePk(String alias, String keystoreId) throws PersistenceException {
		//CHECKSTYLE:ON
		Long pk = null;
		if (alias != null && keystoreId != null) {
						
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_CERT_ALIAS, alias);
			parameters.put(IParametersQueriesConstants.PARAM_KEYSTORE_ID, keystoreId);
			List<Long> ids = (List<Long>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_CERT_PK, parameters);
									
			if (ids != null && ids.size() == 1) {
				pk = (Long) ids.get(0);
			}
		}

		return pk;
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO#modifyPlatform(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void modifyPlatform(String platformId, AfirmaData data) throws DatabaseException {
		if (platformId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_055));
		}
		if (data == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_059));
		}
		if (data.getApplicationId() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_060));
		}
		if (data.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION && (data.getAuthenticationUser() == null || data.getAuthenticationPassword() == null)) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_061));
		} else if (data.getAuthenticationType() == Constants.CERTIFICATE_AUTHENTICATION && data.getAuthenticationUser() == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_061));
		}
				
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, platformId);
			List<PlatformPOJO> platfList = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);
			
			if (platfList == null || platfList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_057, new Object[ ] { platformId }));
			} else if (platfList.size() == 1) {
				PlatformPOJO platform = (PlatformPOJO) platfList.get(0);
				//platform.getServices().clear();
				platform.setPfApp(data.getApplicationId());
				platform.setAuthType(data.getAuthenticationType());
				platform.setPfVersion(data.getVersion());
				if (data.getAuthenticationType() == Constants.WITHOUT_AUTHENTICATION) {
					platform.setAuthUser(null);
					platform.setAuthPass(null);
					platform.setCertificateByAuthCert(null);
				} else if (data.getAuthenticationType() == Constants.USER_PASS_AUTHENTICATION) {
					platform.setAuthUser(data.getAuthenticationUser());
					platform.setAuthPass(data.getAuthenticationPassword());
					platform.setCertificateByAuthCert(null);
				} else if (data.getAuthenticationType() == Constants.CERTIFICATE_AUTHENTICATION) {
					CertificatePOJO cert = platform.getCertificateByAuthCert();
					if (cert == null || !cert.getCertId().equals(data.getAuthenticationUser())) {
						Long authCertPk = getCertificatePk(data.getAuthenticationUser(), Constants.SOAP_SIGNER_KEYSTORE);
						if (authCertPk == null) {
							throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_064, new Object[ ] { data.getAuthenticationUser() }));
						} else {
							CertificatePOJO authCert = new CertificatePOJO();
							authCert.setCertPk(authCertPk);
							platform.setCertificateByAuthCert(authCert);
						}
						platform.setAuthUser(data.getAuthenticationUser());
						platform.setAuthPass(null);
					}

				}

				if (data.getSoapTrusted() != null) {
					CertificatePOJO cert = platform.getCertificateBySoaptrusted();
					if (cert == null || !cert.getCertId().equals(data.getSoapTrusted())) {
						Long soapCertPk = getCertificatePk(data.getSoapTrusted(), Constants.SOAP_TRUSTED_KEYSTORE);
						if (soapCertPk == null) {
							throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.DB_064, new Object[ ] { data.getSoapTrusted() }));
						} else {
							CertificatePOJO soapCert = new CertificatePOJO();
							soapCert.setCertPk(soapCertPk);
							platform.setCertificateBySoaptrusted(soapCert);
						}
					}
				} else {
					platform.setCertificateBySoaptrusted(null);
				}
				
				// Compruebo si los servicios de la plataforma, son nuevos o hay que modifcar existentes
				
				if (data.getServices() != null && !data.getServices().isEmpty()) {
															
					Iterator <ServicePOJO> itServices = platform.getServices().iterator();
					ArrayList<String> servicesMod = new ArrayList<String>();
					
					// Itero sobre los servicios asociados en la plataforma en bd.
					
					while(itServices.hasNext()) {
						ServicePOJO service = itServices.next();
						
						// Si el servicio asociado a la plataforma en bd, se corresponde con el del formulario,
						// Lo modifico y lo añado a la lista de servicios modificados
						
						if(data.getServices().containsKey(service.getServiceId())){
							WSServiceData sd = data.getServices().get(service.getServiceId());
							service.setSLocation(sd.getServicesLocation());
							service.setSOperation(sd.getOperationName());
							service.setSTimeout(sd.getTimeOut());
							em.merge(service);
							servicesMod.add(service.getServiceId());
							
						// Si no se corresponde lo elimino de bd (como sevicio asociado a la plataforma)
							
						}else{
							em.remove(ServicePOJO.class, service);

						}
					}
					
					// Ahora itero sobre los servicios del formulario y si éste no está entre los modificados,
					// lo añado como servicio nuevo, asociado a la plataforma.
					
					Iterator<String> inputServices = data.getServices().keySet().iterator();
					while(inputServices.hasNext()) {
						String id = inputServices.next();
						if(!servicesMod.contains(id)){
							WSServiceData serviceData = data.getServices().get(id);			
							ServicePOJO service = new ServicePOJO();
							service.setServiceId(id);
							service.setPlatform(platform);
							service.setSLocation(serviceData.getServicesLocation());
							service.setSOperation(serviceData.getOperationName());
							service.setSTimeout(new Long(serviceData.getTimeOut()).longValue());
							
							em.persist(service);
						}
					}
				} else {
					Iterator <ServicePOJO> itServices = platform.getServices().iterator();
					while(itServices.hasNext()){
						ServicePOJO service = itServices.next();
						
						em.remove(ServicePOJO.class, service);
						
					}
					platform.getServices().clear();
				}
				em.merge(platform);
				
			} else {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_058, new Object[ ] { platformId }));
			}
		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_067, new Object[ ] { platformId });
			LOGGER.error(msg, pe);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,pe);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO#removePlatform(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void removePlatform(final String platformId) throws DatabaseException {
		if (platformId == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_055));
		}
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_PLATFORM_ID, platformId);
			List<PlatformPOJO> platfList = (List<PlatformPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_PLATFORM_BY_ID, parameters);
			
			if (platfList == null || platfList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_057, new Object[ ] { platformId }));
			} else if (platfList.size() == 1) {
				PlatformPOJO platform = (PlatformPOJO) platfList.get(0);
				if (platform.getApplications().isEmpty()) {
					platform.setEndingtime(new Date());
					
					em.merge(platform);
					
				} else {
					throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getFormatMessage(LanguageKeys.DB_069, new Object[ ] { platformId }));
				}
			} else {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_058, new Object[ ] { platformId }));
			}
		} catch (PersistenceException e) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_068, new Object[ ] { platformId });
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,e);
		} 
	}

}
