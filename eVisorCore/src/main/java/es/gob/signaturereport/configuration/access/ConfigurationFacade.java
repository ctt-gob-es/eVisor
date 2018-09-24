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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.ConfigurationFacade.java.</p>
 * <b>Description:</b><p> This class provides all methods to management of configuration.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Singleton;

import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.configuration.items.Alarm;
import es.gob.signaturereport.configuration.items.AlarmIdentifier;
import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.configuration.items.UnitOrganization;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/**
 * <p>This class provides all methods to management of configuration.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
@Singleton
@ManagedBean
@EVisorConfiguration
public class ConfigurationFacade implements ApplicationConfigurationFacadeI, AfirmaConfigurationFacadeI, KeystoreConfigurationFacadeI, TemplateConfigurationFacadeI, UserConfigurationFacadeI, AlarmConfigurationFacadeI {

	/**
	 * Attribute that represents instance of the class.
	 */
	private static ConfigurationFacade instance = null;
	/**
	 * Attribute that represents the Signature Report platform.
	 */
	public static final String SIGNATURE_REPORT_PLATFORM = "SignatureReport";

	/**
	 * Attribute that represents the Generation Report Service.
	 */
	public static final String GENERATION_REPORT_SERVICE = "GenerationReportService";
	
	/**
	 * Attribute that represents the Validation Report Service.
	 */
	public static final String VALIDATION_REPORT_SERVICE = "ValidationReportService";

	/**
	 * Attribute that represents the manager to configure the applications and organizational units.
	 */
	@Inject
	@ApplicationConfiguration
	private ApplicationConfigurationFacadeI appConf;

	/**
	 * Attribute that represents the manager to configure the "@firma platform".
	 */
	@Inject
	@AfirmaConfiguration
	private AfirmaConfigurationFacadeI afirmaConf;

	/**
	 * Attribute that represents the manager to configure the keystores of system.
	 */
	@Inject
	@KeystoreConfiguration
	private KeystoreConfigurationFacadeI keystoreConf;

	/**
	 * Attribute that represents the manager to configure the templates of system..
	 */
	@Inject
	@TemplateConfiguration
	private TemplateConfigurationFacadeI templateConf;

	/**
	 * Attribute that represents the manager to configure the information associated with system users..
	 */
	@Inject
	@UserConfiguration
	private UserConfigurationFacadeI userConf;

	/**
	 * Attribute that represents the manager configuration information associated with alarms.
	 */
	@Inject
	@AlarmConfiguration
	private AlarmConfigurationFacadeI alarmConf;
	
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

//	/**
//	 * Constructor method for the class ConfigurationFacade.java.
//	 */
//	private ConfigurationFacade() {
//		appConf = new ApplicationConfigurationFacade();
//		afirmaConf = new AfirmaConfigurationFacade();
//		keystoreConf = new KeystoreConfigurationFacade();
//		templateConf = new TemplateConfigurationFacade();
//		userConf = new UserConfigurationFacade();
//		alarmConf = new AlarmConfigurationFacade();
//	}

	/**
	 * Gets a instance of the class.
	 * @return A instance of class.
	 */
	public static ConfigurationFacade getInstance() {

		if (instance == null) {
			instance = new ConfigurationFacade();
		}
		return instance;
	}
	
	/**
	* Method that initializes the configuration facade.
	*/
	@PostConstruct
	public final void init() {
		instance = this;
	}

	/**
	 * Method that destroy the singleton of this class.
	 */
	@PreDestroy
	public final void destroy() {
		instance = null;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystorePath(java.lang.String)
	 */
	public String getKeystorePath(String keystoreId) throws ConfigurationException {
		return keystoreConf.getKeystorePath(keystoreId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystorePassword(java.lang.String)
	 */
	public String getKeystorePassword(String keystoreId) throws ConfigurationException {
		return keystoreConf.getKeystorePassword(keystoreId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystoreType(java.lang.String)
	 */
	public String getKeystoreType(String keystoreId) throws ConfigurationException {
		return keystoreConf.getKeystoreType(keystoreId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getCertificate(java.lang.String, java.lang.String)
	 */
	public X509Certificate getCertificate(String keystoreId, String alias) throws ConfigurationException {
		return keystoreConf.getCertificate(keystoreId, alias);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#getSystemAlarms()
	 */
	public AlarmIdentifier[ ] getSystemAlarms() throws ConfigurationException {

		return alarmConf.getSystemAlarms();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#getAlarm(java.lang.String)
	 */
	public Alarm getAlarm(String alarmId) throws ConfigurationException {
		return alarmConf.getAlarm(alarmId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI#setAlarmConfiguration(java.lang.String, java.util.List, boolean, int)
	 */
	public void setAlarmConfiguration(String alarmId, List<String> receivers, boolean lock, int standByTimePeriod) throws ConfigurationException {
		alarmConf.setAlarmConfiguration(alarmId, receivers, lock, standByTimePeriod);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#removeCertificate(java.lang.String, java.lang.String)
	 */
	public void removeCertificate(String keystoreId, String alias) throws ConfigurationException {
		keystoreConf.removeCertificate(keystoreId, alias);

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#addCertificate(java.lang.String, java.lang.String, java.security.cert.X509Certificate, java.security.PrivateKey)
	 */
	public void addCertificate(String keystoreId, String alias, X509Certificate certificate, PrivateKey key) throws ConfigurationException {
		keystoreConf.addCertificate(keystoreId, alias, certificate, key);

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#modifyAlias(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void modifyAlias(String keystoreId, String alias, String newAlias) throws ConfigurationException {
		keystoreConf.modifyAlias(keystoreId, alias, newAlias);

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI#getKeystore(java.lang.String)
	 */
	public KeystoreItem getKeystore(String keystoreId) throws ConfigurationException {
		return keystoreConf.getKeystore(keystoreId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getUserManager(java.lang.String, java.lang.String)
	 */
	public User getUserManager(String login, String password) throws ConfigurationException {	
		return userConf.getUserManager(login, password);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#modifyUserManager(java.lang.String, java.lang.String, java.lang.Long, boolean)
	 */
	public void modifyUserManager(String login, String password, Long personId, boolean locked) throws ConfigurationException {
		userConf.modifyUserManager(login, password, personId, locked);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#createUserManager(java.lang.String, java.lang.String, java.lang.Long)
	 */
	public void createUserManager(String login, String password, Long personId) throws ConfigurationException {
		userConf.createUserManager(login, password, personId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#lockUserManager(java.lang.String, boolean)
	 */
	public void lockUserManager(String login, boolean isLocked) throws ConfigurationException {
		userConf.lockUserManager(login, isLocked);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getAllUsers()
	 */
	public User[ ] getAllUsers() throws ConfigurationException {
		return userConf.getAllUsers();
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#getAllPersons()
	 */
	public Person[ ] getAllPersons() throws ConfigurationException {
		return userConf.getAllPersons();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#deleteUser(java.lang.Long)
	 */
	public void deleteUser(Long userId) throws ConfigurationException {
		userConf.deleteUser(userId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#modifyUser(java.lang.Long, java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.String)
	 */
	public void modifyUser(Long userId, String name, String surname, BigDecimal phoneNumber, String email) throws ConfigurationException {
		userConf.modifyUser(userId, name, surname, phoneNumber, email);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#addUser(java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.String)
	 */
	public Long addUser(String name, String surname, BigDecimal phoneNumber, String email) throws ConfigurationException {
		return userConf.addUser(name, surname, phoneNumber, email);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#getAfirmaConfiguration(java.lang.String)
	 */
	public AfirmaData getAfirmaConfiguration(String afirmaId) throws ConfigurationException {
		return afirmaConf.getAfirmaConfiguration(afirmaId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#createAfirmaConfiguration(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	public void createAfirmaConfiguration(String afirmaId, AfirmaData afirmaData) throws ConfigurationException {
		afirmaConf.createAfirmaConfiguration(afirmaId, afirmaData);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#getAfirmaIds()
	 */
	public String[ ] getAfirmaIds() throws ConfigurationException {
		return afirmaConf.getAfirmaIds();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#modifyAfirmaConfiguration(java.lang.String, es.gob.signaturereport.configuration.items.AfirmaData)
	 */
	public void modifyAfirmaConfiguration(String afirmaId, AfirmaData afirmaData) throws ConfigurationException {
		afirmaConf.modifyAfirmaConfiguration(afirmaId, afirmaData);	
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI#removeAfirmaConfiguration(java.lang.String)
	 */
	public void removeAfirmaConfiguration(String afirmaId) throws ConfigurationException {
		afirmaConf.removeAfirmaConfiguration(afirmaId);	
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#getTemplate(java.lang.String)
	 */
	public TemplateData getTemplate(String templateId) throws ConfigurationException {
		return templateConf.getTemplate(templateId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#createTemplate(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	public void createTemplate(TemplateData template) throws ConfigurationException {
		templateConf.createTemplate(template);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#modifyTemplateData(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	public void modifyTemplateData(TemplateData template) throws ConfigurationException {
		templateConf.modifyTemplateData(template);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#removeTemplate(java.lang.String)
	 */
	public void removeTemplate(String templateId) throws ConfigurationException {
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#existTemplate(java.lang.String)
	 */
	public boolean existTemplate(String templateId) throws ConfigurationException {
		return templateConf.existTemplate(templateId);
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#getTemplateIds()
	 */
	public Map<String, Integer> getTemplateIds() throws ConfigurationException {
		return templateConf.getTemplateIds();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#getApplicationData(java.lang.String)
	 */
	public ApplicationData getApplicationData(String applicationId) throws ConfigurationException {
		return appConf.getApplicationData(applicationId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#createUO(java.lang.String, java.lang.String)
	 */
	public void createUO(String unitId, String name) throws ConfigurationException {
		appConf.createUO(unitId, name);		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#deleteUO(java.lang.String)
	 */
	public void deleteUO(String unitId) throws ConfigurationException {
		appConf.deleteUO(unitId);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#createApplication(java.lang.String, es.gob.signaturereport.configuration.items.ApplicationData)
	 */
	public void createApplication(String applicationId, ApplicationData appData) throws ConfigurationException {
		appConf.createApplication(applicationId, appData);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#modifyUO(java.lang.String, java.lang.String)
	 */
	public void modifyUO(String unitId, String name) throws ConfigurationException {
		appConf.modifyUO(unitId, name);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#modifyApplication(java.lang.String, es.gob.signaturereport.configuration.items.ApplicationData)
	 */
	public void modifyApplication(String applicationId, ApplicationData appData) throws ConfigurationException {
		appConf.modifyApplication(applicationId, appData);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#getUnitOrganization(java.lang.String, boolean)
	 */
	public UnitOrganization getUnitOrganization(String unitId, boolean recursive) throws ConfigurationException {
		return appConf.getUnitOrganization(unitId, recursive);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI#removeApplication(java.lang.String)
	 */
	public void removeApplication(String applicationId) throws ConfigurationException {
		appConf.removeApplication(applicationId);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.UserConfigurationFacadeI#deleteUserManager(java.lang.String)
	 */
	public void deleteUserManager(String login) throws ConfigurationException {
		userConf.deleteUserManager(login);
	}

	
	public ApplicationConfigurationFacadeI getAppConf() {
		return appConf;
	}

	
	public void setAppConf(ApplicationConfigurationFacadeI appConf) {
		this.appConf = appConf;
	}

	
	public AfirmaConfigurationFacadeI getAfirmaConf() {
		return afirmaConf;
	}

	
	public void setAfirmaConf(AfirmaConfigurationFacadeI afirmaConf) {
		this.afirmaConf = afirmaConf;
	}

	
	public KeystoreConfigurationFacadeI getKeystoreConf() {
		return new KeystoreConfigurationFacade();
	}

	
	public void setKeystoreConf(KeystoreConfigurationFacadeI keystoreConf) {
		this.keystoreConf = keystoreConf;
	}

	
	public TemplateConfigurationFacadeI getTemplateConf() {
		return templateConf;
	}

	
	public void setTemplateConf(TemplateConfigurationFacadeI templateConf) {
		this.templateConf = templateConf;
	}

	
	public UserConfigurationFacadeI getUserConf() {
		return userConf;
	}

	
	public void setUserConf(UserConfigurationFacadeI userConf) {
		this.userConf = userConf;
	}

	
	public AlarmConfigurationFacadeI getAlarmConf() {
		return alarmConf;
	}

	
	public void setAlarmConf(AlarmConfigurationFacadeI alarmConf) {
		this.alarmConf = alarmConf;
	}
	
	
}
