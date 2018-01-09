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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.AlarmConfigurationFacade.java.</p>
 * <b>Description:</b><p>Class that manages the configuration information associated with alarms.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>14/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 14/04/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.items.Alarm;
import es.gob.signaturereport.configuration.items.AlarmIdentifier;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmException;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.malarm.item.AlarmData;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/** 
 * <p>Class that manages the configuration information associated with alarms.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 14/04/2011.
 */
@Stateless
@AlarmConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AlarmConfigurationFacade implements AlarmConfigurationFacadeI {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(AlarmConfigurationFacade.class);

	/**
	 * Attribute that represents the system alarm identifiers. 
	 */
	private static AlarmIdentifier[] alarmIdentifiers = null;
	
	
	
	/**
	 * Constant that represents the alarm number. 
	 */
	private static final int NUM_ALARMS = 3;
		
	static {
		alarmIdentifiers = new AlarmIdentifier[NUM_ALARMS];
		try {
			alarmIdentifiers[0] = new AlarmIdentifier(AlarmManager.ALARM_001,AlarmManager.getInstance().getAlarmDescription(AlarmManager.ALARM_001));
			alarmIdentifiers[1] = new AlarmIdentifier(AlarmManager.ALARM_002,AlarmManager.getInstance().getAlarmDescription(AlarmManager.ALARM_002));	
			alarmIdentifiers[2] = new AlarmIdentifier(AlarmManager.ALARM_003,AlarmManager.getInstance().getAlarmDescription(AlarmManager.ALARM_003));
		} catch (AlarmException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.CONF_079),e);
		}
	}
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#getSystemAlarms()
     */
    public final AlarmIdentifier[ ] getSystemAlarms() throws ConfigurationException {
    	return alarmIdentifiers;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#getAlarm(java.lang.String)
     */
    public final Alarm getAlarm(final String alarmId) throws ConfigurationException {
    	Alarm alarm = null;
    	if (alarmId == null || alarmId.trim().length() == 0) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_080));
    	}
    	try {
			AlarmData ad =  AlarmManager.getInstance().getAlarm(alarmId);
			if (ad != null) {
				AlarmIdentifier alarmIdentifier = new AlarmIdentifier(ad.getAlarmId(),ad.getDescription());
				alarm = new Alarm(alarmIdentifier);
				alarm.setLock(ad.isLock());
				alarm.setReceivers(Arrays.asList(ad.getReceivers()));
				alarm.setStandbyPeriod(ad.getStandbyTime());
			}
			return alarm;
		} catch (AlarmException e) {
			String message = Language.getFormatMessage(LanguageKeys.CONF_081, new Object[]{alarmId});
			LOGGER.error(message + e.getDescription());
			if (e.getCode() == AlarmException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, message,e);
			}
		}
    }

   

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#setAlarmConfiguration(java.lang.String, java.lang.String[], boolean, int)
	 */
	public final void setAlarmConfiguration(final String alarmId, final List<String> receivers, final boolean lock, final int standByTime) throws ConfigurationException {
		if (alarmId == null|| alarmId.trim().length()==0) {
    		throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_080));
    	}
		
		if (receivers == null || receivers.isEmpty()) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_082));
		}
		
		if (standByTime < 0) {
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS,Language.getMessage(LanguageKeys.CONF_083));			
		}
		
		try {
			AlarmManager.getInstance().setAlarmInformation(alarmId, lock, standByTime, receivers.toArray(new String[receivers.size()]));
		} catch (AlarmException e) {
			
			String message = Language.getFormatMessage(LanguageKeys.CONF_084, new Object[]{alarmId});
			LOGGER.error(message + e.getDescription());
			if (e.getCode() == AlarmException.INVALID_INPUT_PARAMETERS) {
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			} else {
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, message,e);
			}
		}
	}


}
