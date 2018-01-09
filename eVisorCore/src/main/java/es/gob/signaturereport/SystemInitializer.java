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
 * <b>File:</b><p>es.gob.signaturereport.malarm.AlarmInitialize.java.</p>
 * <b>Description:</b><p>Class that initializes the properties of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/07/2011.
 */
package es.gob.signaturereport;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.access.EVisorConfiguration;
import es.gob.signaturereport.configuration.access.keystore.KeystorePersistenceFacade;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.malarm.access.AlarmPersistenceFacade;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.access.AuditPersistenceFacade;
import es.gob.signaturereport.maudit.statistics.StatisticsManager;
import es.gob.signaturereport.persistence.ManagerBOs;
import es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceFacade;
import es.gob.signaturereport.persistence.utils.NumberConstants;
import es.gob.signaturereport.services.ServiceMBeanSupport;
import es.gob.signaturereport.services.exception.MBeanServiceException;
import es.gob.signaturereport.ws.engine.EngineConfigurationFactoryEVisor;


/** 
 * <p>Class that initializes the properties of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 2.0, 11/07/2016.
 */
@Startup
@Singleton
public class SystemInitializer extends ServiceMBeanSupport implements SystemInitializerMBean {
	
		
	/**
	 * Constant attribute that represents the log4j property that determines the path to the log4j configuration file.
	 */
	private static final String LOG4J_PROPERTY = "log4j.configuration";
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static Logger logger = null;
	
	/**
	 * Constant attribute that represents the name of system property key engine for the
	 * configuration factory in AXIS.
	 */
	private static final String AXIS_ENGINE_CONFIG_FACTORY = "axis.EngineConfigFactory";
	
	/**
     * Attribute that represents the alarms manager.
     */
    @Inject
    private AlarmManager alarmManager;
    
    /**
     * Attribute that represents the alarms manager.
     */
    @Inject
    private StatisticsManager statsManager;
    
    /**
	 * Attribute that represents the auditory manager.
	 * It is necessary to initialize by this way. 
	 */
	@Inject
	private AuditManagerI auditManager;
    
    /**
	 * Attribute that represents the manager of the BOs managers for the different data base schemes.
	 * It is necessary to initialize by this way.
	 */
	@Inject
	private ManagerBOs managerBOs;
	
	/**
	 * Attribute that represents the facade for all configuration modules in the platform.
	 * It is necessary to initialize by this way. 
	 */
	@Inject
	@EVisorConfiguration
	private ConfigurationFacade configurationFacade;
	
	/**
	 * Attribute that represents the facade for all configuration modules in the platform.
	 * It is necessary to initialize by this way. 
	 */
	@Inject
	private KeystorePersistenceFacade keystorePersistenceFacade;
	
	/**
	 * Attribute that represents the instance of the auditory persistence facade. 
	 */
	@Inject
	private AuditPersistenceFacade auditPersistenceFacade;
	
	/**
	 * Attribute that represents the facade for SOAP persistence facade.
	 * It is necessary to initialize by this way. 
	 */
	@Inject
	private SOAPPersistenceFacade soapPersistenceFacade;
	
	/**
	 * Attribute that represents the instance of the alarm persistence facade. 
	 */
	@Inject
	private AlarmPersistenceFacade alarmPersistenceFacade;
	
	
	
	

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.services.ServiceMBeanSupport#startService()
	 */
	@Override
	protected void startService() throws MBeanServiceException {
		
		// Se indica que el fichero de configuración de log4j se recargue cada
		// cierto periodo.
		org.apache.log4j.xml.DOMConfigurator.configureAndWatch(System.getProperty(LOG4J_PROPERTY), NumberConstants.INT_10000);
		// Despues de iniciar la configuración de log4j, iniciamos el logger.
		logger = Logger.getLogger(SystemInitializer.class);
		
		// Le indicamos a Axis cual es la factoría para las configuraciones
		// tanto de cliente como servidor. De este modo podemos tener el
		// "server-config.wsdd" fuera del war, concretamente en el directorio
		// de configuración del JBoss.
		System.setProperty(AXIS_ENGINE_CONFIG_FACTORY, EngineConfigurationFactoryEVisor.class.getName());

		// Inicializando el gestor de alarmas
		logger.info(Language.getMessage(LanguageKeys.UTIL_037));

		// Inicializamos el gestor de estadisticas
		logger.info(Language.getMessage(LanguageKeys.UTIL_038));	

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.services.ServiceMBeanSupport#stopService()
	 */
	@Override
	protected void stopService() throws MBeanServiceException {
				
	}
		
}
