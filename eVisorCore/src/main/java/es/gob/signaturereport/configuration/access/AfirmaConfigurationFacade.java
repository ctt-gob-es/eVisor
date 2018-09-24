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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.AfirmaConfigurationFacade.java.</p>
 * <b>Description:</b><p>Class that manages the configuration of the "@firma platforms"</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.configuration.items.WSServiceData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IPlatformModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.CertificatePOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.PlatformPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ServicePOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/** 
 * <p>Class that manages the configuration of the "@firma platforms"</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/02/2011.
 */
@Stateless
@AfirmaConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AfirmaConfigurationFacade implements AfirmaConfigurationFacadeI {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(AfirmaConfigurationFacade.class);
	
	/**
	 * Attribute that allows to operate with information about applications of the platform.
	 */
	@Inject
	private IPlatformModuleBO platformBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#getAfirmaConfiguration(java.lang.String)
	 */
	public AfirmaData getAfirmaConfiguration(final String afirmaId) throws ConfigurationException {
		if(afirmaId==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_046));
		}
		try {
			
			final PlatformPOJO dbPlatform = platformBO.getPlatform(afirmaId);
			AfirmaData afirmaData = new AfirmaData();
			platform2afirmadata(dbPlatform, afirmaData);
			return afirmaData;
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_045, new Object[]{afirmaId});
			LOGGER.error(msg,e);
			if(e.getCode()==DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(), e);
			}else if(e.getCode()==DatabaseException.ITEM_NOT_FOUND){
				return null;
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#createAfirmaConfiguration(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	public void createAfirmaConfiguration(final String afirmaId, final AfirmaData afirmaData) throws ConfigurationException {
		if(afirmaId==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_046));
		}
		if(afirmaData==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_047));
		}
		try {
			
			platformBO.createPlatform(afirmaId, afirmaData);
						
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_048, new Object[]{afirmaId});
			LOGGER.error(msg,e);
			if(e.getCode()==DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()==DatabaseException.DUPLICATE_ITEM){
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#getAfirmaIds()
	 */
	@Override
	public String[ ] getAfirmaIds() throws ConfigurationException {
		try {
						
			return platformBO.getPlatformIds();
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getMessage(LanguageKeys.CONF_049);
			LOGGER.error(msg,e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#modifyAfirmaConfiguration(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	public void modifyAfirmaConfiguration(final String afirmaId, final AfirmaData afirmaData) throws ConfigurationException {
		if(afirmaId==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_046));
		}
		if(afirmaData==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_047));
		}
		try {
					
			platformBO.modifyPlatform(afirmaId, afirmaData);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_050, new Object[]{afirmaId});
			LOGGER.error(msg,e);
			if(e.getCode()==DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()==DatabaseException.ITEM_NOT_FOUND){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#removeAfirmaConfiguration(java.lang.String)
	 */
	public void removeAfirmaConfiguration(final String afirmaId) throws ConfigurationException {
		if(afirmaId==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_046));
		}
		try {
			
			platformBO.removePlatform(afirmaId);
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_051, new Object[]{afirmaId});
			LOGGER.error(msg,e);
			if(e.getCode()==DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()==DatabaseException.ITEM_NOT_FOUND){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, e.getDescription(),e);
			}else if(e.getCode()==DatabaseException.ITEM_ASSOCIATED){
				throw new ConfigurationException(ConfigurationException.ITEM_ASSOCIATED, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}
	
	/**
	 * Copy the information included into a {@link PlatformPOJO} object to an {@link AfirmaData} object.
	 * @param dbPlatform		{@link PlatformPOJO} object that contains the original information.
	 * @param afirmaData	{@link AfirmaData} object where the information will be included.
	 */
	private void platform2afirmadata(final PlatformPOJO dbPlatform, final AfirmaData afirmaData) {
		if (dbPlatform != null && afirmaData != null) {
			afirmaData.setVersion(dbPlatform.getPfVersion());
			afirmaData.setApplicationId(dbPlatform.getPfApp());
			afirmaData.setAuthenticationType(dbPlatform.getAuthType());
			if (afirmaData.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) {
				afirmaData.setAuthenticationUser(dbPlatform.getAuthUser());
				afirmaData.setAuthenticationPassword(dbPlatform.getAuthPass());
			} else if (afirmaData.getAuthenticationType() == ConfigurationFacade.CERTIFICATE_AUTHENTICATION) {
				CertificatePOJO certAuth = dbPlatform.getCertificateByAuthCert();
				if (certAuth != null && certAuth.getEndingtime() == null) {
					afirmaData.setAuthenticationUser(certAuth.getCertId());
				}
			}
			CertificatePOJO certSoapTrusted = dbPlatform.getCertificateBySoaptrusted();
			if (certSoapTrusted != null && certSoapTrusted.getEndingtime() == null) {
				afirmaData.setSoapTrusted(certSoapTrusted.getCertId());
			}
			LinkedHashMap<String, WSServiceData> services = new LinkedHashMap<String, WSServiceData>();
			Iterator<ServicePOJO> servIt = dbPlatform.getServices().iterator();
			while (servIt.hasNext()) {
				ServicePOJO sv = servIt.next();
				WSServiceData serviceData = new WSServiceData();
				serviceData.setOperationName(sv.getSOperation());
				serviceData.setServicesLocation(sv.getSLocation());
				serviceData.setTimeOut(new Long(sv.getSTimeout()).intValue());
				services.put(sv.getServiceId(), serviceData);
			}
			afirmaData.setServices(services);

		}
		return;
	}

}
