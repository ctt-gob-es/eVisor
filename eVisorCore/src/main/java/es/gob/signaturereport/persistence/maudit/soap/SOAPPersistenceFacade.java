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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.SOAPPersistenceFacade.java.</p>
 * <b>Description:</b><p>Class that represents a facade of persistence for storing SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>02/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 02/08/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.util.LinkedHashMap;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.AuditPersistenceException;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;


/** 
 * <p>Class that represents a facade of persistence for storing SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 02/08/2011.
 */
@Singleton
@ManagedBean
public final class SOAPPersistenceFacade {
	
	/**
	 * Attribute that represents instance of the class.
	 */
	private static SOAPPersistenceFacade instance = null;
	
	/**
	 * Attribute that represents the SOAP request type of persistence. 
	 */
	private SOAPPersistenceType soapRequestType;
	
	/**
	 * Attribute that represents the SOAP response type of persistence. 
	 */
	private SOAPPersistenceType soapResponseType;
	
	/**
	 * Producer method to instance the selected persistence type for SOAP requests
	 * @param db Instance of DBSOAPPersistenceManager
	 * @param file Instance of FileSOAPPersistenceManager
	 * @return The instance selected depending soapRequestType
	 */
	@Produces
	@SOAPRequestPersistence
	public SOAPPersistenceI getSoapRequestManager(@New FileSOAPPersistenceManager file,
           @New DBSOAPPersistenceManager db) {
		
		
		if (soapRequestType == null) {
			soapRequestType = SOAPPersistenceType.DB;
		}
		
        switch (soapRequestType) {
            case DB:
                return db;
            case FILE:
                return file;
            default:
                return db;
        }
    }
	
	/**
	 * Producer method to instance the selected persistence type for SOAP requests
	 * @param db Instance of DBSOAPPersistenceManager
	 * @param file Instance of FileSOAPPersistenceManager
	 * @return The instance selected depending soapResponseType
	 */
	@Produces
	@SOAPResponsePersistence
    public SOAPPersistenceI getSoapResponseManager(@New FileSOAPPersistenceManager file,
            @New DBSOAPPersistenceManager db) {
		
		if (soapResponseType == null) {
			soapResponseType = SOAPPersistenceType.DB;
		}

        switch (soapResponseType) {
            case DB:
                return db;
            case FILE:
                return file;
            default:
                return db;
        }
    }

	/**
	 * Attribute that represents the class that manages the persistence for SOAP requests. 
	 */
	@Inject
	@SOAPRequestPersistence
	private SOAPPersistenceI soapRequestManager;

	/**
	 * Attribute that represents the class that manages the persistence for SOAP responses. 
	 */
	@Inject
	@SOAPResponsePersistence
	private SOAPPersistenceI soapResponseManager;
	
	@Inject
	public SOAPPersistenceFacade() {
		
		final String requestType = StaticSignatureReportProperties.getProperty(REQUEST_TYPE_KEY);
		final String responseType = StaticSignatureReportProperties.getProperty(RESPONSE_TYPE_KEY);
		
		if (DB_TYPE_VALUE.equals(requestType))
		{
			soapRequestType = SOAPPersistenceType.DB;
		}
		
		if (FILE_TYPE_VALUE.equals(requestType))
		{
			soapRequestType = SOAPPersistenceType.FILE;
		}
		
		if (DB_TYPE_VALUE.equals(responseType))
		{
			soapResponseType = SOAPPersistenceType.DB;
		}
		
		if (FILE_TYPE_VALUE.equals(responseType))
		{
			soapResponseType = SOAPPersistenceType.FILE;
		}
	}
			
	/**
	 * Attribute that represents the key used to indicate the class implementation for managing SOAP request. 
	 */
	private static final String REQUEST_IMPL_KEY = "signaturereport.audit.custody.request.impl";
	
		
	/**
	 * Attribute that represents the key used to indicate the class implementation for managing SOAP request. 
	 */
	private static final String RESPONSE_IMPL_KEY = "signaturereport.audit.custody.response.impl";
	
	/**
	 * Attribute that represents the key used to indicate the class implementation for managing SOAP request. 
	 */
	private static final String REQUEST_TYPE_KEY = "signaturereport.audit.custody.response.type";
	
	/**
	 * Attribute that represents the value used to indicate the type of persistence DATABASE in SOAP request. 
	 */
	private static final String DB_TYPE_VALUE = "db";
	
	/**
	 * Attribute that represents the value used to indicate the type of persistence FILE in SOAP request. 
	 */
	private static final String FILE_TYPE_VALUE = "file";
	
	/**
	 * Attribute that represents the key used to indicate the class implementation for managing SOAP request. 
	 */
	private static final String RESPONSE_TYPE_KEY = "signaturereport.audit.custody.response.type";

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(SOAPPersistenceFacade.class);
	
	/**
	 * Constructor method for the class SOAPPersistenceFacade.java. 
	 */
	@PostConstruct
	public final void init() {
		
		instance = this;
		
		loadSOAPMAnagerImpl();
	}
	
		
	/**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	instance = null;
    }
	
	/**
	 * Gets an instance of the class.
	 * @return	An instance of the class.
	 */
	public static SOAPPersistenceFacade getInstance(){
		
		if(instance==null){
			instance = new SOAPPersistenceFacade();
		}
		
		return instance;
	}
	
	/**
	 * Store a SOAP request message.
	 * @param message	SOAP message.
	 * @param transactionId Audit transaction identifier.
	  * @return	Store SOAP identifier.
	 * @throws AuditPersistenceException	If an error occurs
	 */
	public String storeSOAPRequest(byte[ ] message,long transactionId) throws AuditPersistenceException {
		if (message == null) {
			throw new AuditPersistenceException(AuditPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_007));
		}
		if (this.soapRequestManager != null) {
			return this.soapRequestManager.storeSOAP(message,transactionId,SOAPPersistenceI.REQUEST_SOAP_TYPE);
		} else {
			String msg = Language.getMessage(LanguageKeys.AUD_002);
			LOGGER.fatal(msg);
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg);
		}
	}

	/**
	 * Store a SOAP response message.
	 * @param message	SOAP message.
	 * @param transactionId Audit transaction identifier.
	 * @return	Store SOAP identifier.
	 * @throws AuditPersistenceException	If an error occurs
	 */
	public String storeSOAPResponse(byte[ ] message,long transactionId) throws AuditPersistenceException {
		if (message == null) {
			throw new AuditPersistenceException(AuditPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_007));
		}
		if (this.soapResponseManager != null) {
			return this.soapResponseManager.storeSOAP(message, transactionId,SOAPPersistenceI.RESPONSE_SOAP_TYPE);
		} else {
			String msg = Language.getMessage(LanguageKeys.AUD_006);
			LOGGER.fatal(msg);
			throw new AuditPersistenceException(AuditPersistenceException.UNKNOWN_ERROR, msg);
		}
	}
	
	

	
	/**
	 * Loads the managers for processing the SOAP message. 
	 */
	private void loadSOAPMAnagerImpl() {
		
		// Se carga la clase para la persistencia de las peticiones SOAP
		
		//String className = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportProperties.AUDIT_SECTION_ID, REQUEST_IMPL_KEY);
		//if (className != null) {
			try {
				//this.soapRequestManager = (SOAPPersistenceI) Class.forName(className).newInstance();
				String[ ] properties = this.soapRequestManager.getConfigurationParameters();
				if (properties != null) {
					LinkedHashMap<String, String> confParams = new LinkedHashMap<String, String>();
					for (int i = 0; i < properties.length; i++) {
						String value = StaticSignatureReportProperties.getProperty(properties[i]);
						if (value != null) {
							confParams.put(properties[i], value);
						}
					}
					this.soapRequestManager.init(confParams);
				}
			} catch (Exception e) {
				LOGGER.fatal(Language.getFormatMessage(LanguageKeys.AUD_003, new Object[ ] { StaticSignatureReportProperties.getProperty(REQUEST_IMPL_KEY) }));
			}
//		} 
//		else {
//			LOGGER.fatal(Language.getMessage(LanguageKeys.AUD_002));
//		}
		
		// Se carga la clase para la persistencia de las respuestas SOAP

//		className = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportProperties.AUDIT_SECTION_ID, RESPONSE_IMPL_KEY);
//		if (className != null) {
			try {
				//this.soapResponseManager = (SOAPPersistenceI) Class.forName(className).newInstance();
				String[ ] properties = this.soapResponseManager.getConfigurationParameters();
				if (properties != null) {
					LinkedHashMap<String, String> confParams = new LinkedHashMap<String, String>();
					for (int i = 0; i < properties.length; i++) {
						String value = StaticSignatureReportProperties.getProperty(properties[i]);
						if (value != null) {
							confParams.put(properties[i], value);
						}
					}
					this.soapResponseManager.init(confParams);
				}
			} catch (Exception e) {
				LOGGER.fatal(Language.getFormatMessage(LanguageKeys.AUD_003, new Object[ ] { StaticSignatureReportProperties.getProperty(RESPONSE_IMPL_KEY) }));
			}
//		} else {
//			LOGGER.fatal(Language.getMessage(LanguageKeys.AUD_006));
//		}
	}

	
	public SOAPPersistenceType getSoapRequestType() {
		return soapRequestType;
	}

	
	public void setSoapRequestType(SOAPPersistenceType soapRequestType) {
		this.soapRequestType = soapRequestType;
	}

	
	public SOAPPersistenceType getSoapResponseType() {
		return soapResponseType;
	}

	
	public void setSoapResponseType(SOAPPersistenceType soapResponseType) {
		this.soapResponseType = soapResponseType;
	}
	
	
}
