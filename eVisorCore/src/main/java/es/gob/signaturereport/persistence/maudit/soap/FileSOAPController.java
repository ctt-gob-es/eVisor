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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.FileSOAPController.java.</p>
 * <b>Description:</b><p>Class that that controls the storing in disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/11/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/11/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.tools.UtilsTime;


/** 
 * <p>Class that that controls the storing in disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/11/2011.
 */
public final class FileSOAPController {
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(FileSOAPController.class);
	/**
	 * Attribute that represents the key used to indicate the response pool size. 
	 */
	private static final String RESPONSE_POOL_SIZE = "signaturereport.audit.custody.response.disk.poolsize";
	/**
	 * Attribute that represents the key used to indicate the request pool size. 
	 */
	private static final String REQUEST_POOL_SIZE = "signaturereport.audit.custody.request.disk.poolsize";
	/**
	 * Attribute that represents the key used to indicate the response folder. 
	 */
	private static final String RESPONSE_FOLDER = "signaturereport.audit.custody.response.disk.folder";
	/**
	 * Attribute that represents the key used to indicate the request folder. 
	 */
	private static final String REQUEST_FOLDER = "signaturereport.audit.custody.request.disk.folder";

	/**
	 * Attribute that represents the key used to indicate maximum number of request for inclusion in the directory. 
	 */
	private static final String MAX_REQUEST_NUMBER = "signaturereport.audit.custody.request.disk.maxFileNumbers";

	/**
	 * Attribute that represents the key used to indicate maximum number of response for inclusion in the directory. 
	 */
	private static final String MAX_RESPONSE_NUMBER = "signaturereport.audit.custody.response.disk.maxFileNumbers";

	/**
	 * Attribute that represents the configuration parameters. 
	 */
	public static final String[ ] PARAMETERS = { RESPONSE_POOL_SIZE, REQUEST_POOL_SIZE, RESPONSE_FOLDER, REQUEST_FOLDER, MAX_REQUEST_NUMBER, MAX_RESPONSE_NUMBER };

	/**
	 * Attribute that represents the formatter for creating the directory. 
	 */
	private static final DecimalFormat FORMATTER = new DecimalFormat("0000000000");
	/**
	 * Attribute that represents the path to the folder used by includes the SOAP responses. 
	 */
	private String folderResponsePath = null;

	/**
	 * Attribute that represents the path to the folder used by includes the SOAP requests. 
	 */
	private String folderRequestPath = null;

	/**
	 * Attribute that represents the thread pool for storing the request message. 
	 */
	private ExecutorService serviceRequest = null;

	/**
	 * Attribute that represents the thread pool for storing the response message. 
	 */
	private ExecutorService serviceResponse = null;

	/**
	 * Attribute that represents the default maximum number of files. 
	 */
	private static final int DEFAULT_NUM_MAX_FILE = 100;
	/**
	 * Attribute that represents the maximum number of request for inclusion in the directory. 
	 */
	private int numMaxRequest = DEFAULT_NUM_MAX_FILE;

	/**
	 * Attribute that represents the maximum number of response for inclusion in the directory. 
	 */
	private int numMaxResponse = DEFAULT_NUM_MAX_FILE;

	/**
	 * Attribute that represents the number of request included into the directory. 
	 */
	private int currentRequestNumber = 0;

	/**
	 * Attribute that represents the number of request folder. 
	 */
	private int requestFolderNumber = 0;

	/**
	 * Attribute that represents the number of response folder. 
	 */
	private int responseFolderNumber = 1;

	/**
	 * Attribute that represents the number of responses included into the directory. 
	 */
	private int currentResponseNumber = 1;

	/**
	 * Attribute that represents the current day in the <b>yyyyMMdd</b> format. 
	 */
	private String currentDay = null;
	
	/**
	 * Attribute that represents a class instance. 
	 */
	private static FileSOAPController instance = null;
	
	/**
	 * Attribute that indicates if the request parameters are initialized. 
	 */
	private boolean requestInitialized=false;
	
	/**
	 * Attribute that indicates if the response parameters are initialized. 
	 */
	private boolean responseInitialized=false;
	
	/**
	 * Constructor method for the class FileSOAPController.java. 
	 */
	private FileSOAPController(){
		
	}
	
	/**
	 * Gets an instance of the class.
	 * @return	A class instance.
	 */
	public static FileSOAPController getInstance(){
		if(instance == null){
			instance = new FileSOAPController();
		}
		return instance;
	}

	
	/**
	 * Initializes the class.
	 * @param configurationParams Configuration parameters.
	 */
	public void init(Map<String, String> configurationParams){
		if (configurationParams.containsKey(REQUEST_FOLDER) && !requestInitialized) {
			String folder = configurationParams.get(REQUEST_FOLDER);
			File dir = new File(folder);
			if (!dir.exists()) {
				boolean created = dir.mkdir();
				if (created) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_067, new Object[ ] { folder }));
				}
			}
			folderRequestPath = dir.getAbsolutePath();
			if (configurationParams.containsKey(REQUEST_POOL_SIZE)) {
				String poolSizeStr = configurationParams.get(REQUEST_POOL_SIZE);
				try {
					int poolSize = Integer.parseInt(poolSizeStr);
					this.serviceRequest = Executors.newFixedThreadPool(poolSize);
				} catch (NumberFormatException nfe) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_069, new Object[ ] { poolSizeStr }), nfe);
				} catch (IllegalArgumentException iae) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_069, new Object[ ] { poolSizeStr }), iae);
				}
			}
			if (configurationParams.containsKey(MAX_REQUEST_NUMBER)) {
				String maxNumber = configurationParams.get(MAX_REQUEST_NUMBER);
				try {
					numMaxRequest = Integer.parseInt(maxNumber);
				} catch (NumberFormatException nfe) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_073, new Object[ ] { maxNumber }), nfe);
				}
			}
			requestInitialized = true;
		}

		if (configurationParams.containsKey(RESPONSE_FOLDER) && !responseInitialized) {
			String folder = configurationParams.get(RESPONSE_FOLDER);
			File dir = new File(folder);
			if (!dir.exists()) {
				boolean created = dir.mkdir();
				if (created) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_068, new Object[ ] { folder }));
				}
			}
			folderResponsePath = dir.getAbsolutePath();
			if (configurationParams.containsKey(RESPONSE_POOL_SIZE)) {
				String poolSizeStr = configurationParams.get(RESPONSE_POOL_SIZE);
				try {
					int poolSize = Integer.parseInt(poolSizeStr);
					this.serviceResponse = Executors.newFixedThreadPool(poolSize);
				} catch (NumberFormatException nfe) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_069, new Object[ ] { poolSizeStr }), nfe);
				} catch (IllegalArgumentException iae) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_069, new Object[ ] { poolSizeStr }), iae);
				}
			}
			if (configurationParams.containsKey(MAX_RESPONSE_NUMBER)) {
				String maxNumber = configurationParams.get(MAX_RESPONSE_NUMBER);
				try {
					numMaxResponse = Integer.parseInt(maxNumber);
				} catch (NumberFormatException nfe) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_073, new Object[ ] { maxNumber }), nfe);
				}
			}
			responseInitialized = true;
		}
		if(folderRequestPath!=null && folderResponsePath!=null && folderRequestPath.equals(folderResponsePath)){
			//Incluimos los directorios Request y Response para separar peticiones de respuestas
			folderRequestPath +="/Request";
			File dirRequest = new File(folderRequestPath);
			if(!dirRequest.exists()){
				dirRequest.mkdir();
			}
			folderResponsePath += "/Response";
			File dirResponse = new File(folderResponsePath);
			if(!dirResponse.exists()){
				dirResponse.mkdir();
			}
			initializeRequestDir();
			initializeResponseDir();
		}else{
			if(folderRequestPath!=null){
				initializeRequestDir();
			}
			if(folderResponsePath!=null){
				initializeResponseDir();
			}
		}
	}
	
	/**
	 * Initializes the requests directory.
	 */
	private void initializeRequestDir() {
		// Comprobamos si existe el directorio correpondiente al dña de hoy (en
		// caso de existir se crea)
		UtilsTime time = new UtilsTime();
		currentDay = time.toString(UtilsTime.FORMATO_FECHA_JUNTA_CORTA);
		File dayFolder = new File(this.folderRequestPath + "/" + currentDay);
		if (dayFolder.exists()) {
			// Existe el directorio
			String[ ] dirs = dayFolder.list();
			if (dirs != null) {
				requestFolderNumber = dirs.length;
				File currentFolder = new File(this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber));
				if (currentFolder.exists()) {
					String[ ] soaps = currentFolder.list();
					if (soaps != null) {
						currentRequestNumber = soaps.length;
					}
				} else {
					currentRequestNumber = 0;
					new File(this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber)).mkdir();
				}
			} else {
				requestFolderNumber = 1;
				currentRequestNumber = 0;
				new File(this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber)).mkdir();
			}
		} else {
			dayFolder.mkdir();
			currentRequestNumber = 0;
			requestFolderNumber = 1;
			new File(this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber)).mkdir();
		}
	}

	/**
	 * Gets the path where the SOAP message will be stored.
	 * @param soapType	SOAP type.
	 * @param transactionId	Audit transaction identifier.
	 * @return	Path where the SOAP message will be stored.
	 */
	private synchronized String getFilePath(int soapType, long transactionId) {
		UtilsTime time = new UtilsTime();
		String path = null;
		String day = time.toString(UtilsTime.FORMATO_FECHA_JUNTA_CORTA);
		if (!day.equals(currentDay)) {
			currentDay = day;
			if (folderRequestPath != null) {
				new File(this.folderRequestPath + "/" + currentDay).mkdir();
				currentRequestNumber = 0;
				requestFolderNumber = 1;
				new File(this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber)).mkdir();
			}
			if (folderResponsePath != null) {
				new File(this.folderResponsePath + "/" + currentDay).mkdir();
				currentResponseNumber = 0;
				responseFolderNumber = 1;
				new File(this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber)).mkdir();
			}		
		}
		if (soapType == SOAPPersistenceI.REQUEST_SOAP_TYPE) {
			if (currentRequestNumber >= numMaxRequest) {
				// Cambiamos el directorio donde se alojar�n las peticiones e
				// inicializamos el contador
				requestFolderNumber++;
				path = this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber);
				new File(path).mkdir();
				currentRequestNumber = 0;
			} else {
				path = this.folderRequestPath + "/" + currentDay + "/" + FORMATTER.format(requestFolderNumber);
			}
			path += "/" + "REQ-" + transactionId + ".xml";
			currentRequestNumber++;
		}else if (soapType == SOAPPersistenceI.RESPONSE_SOAP_TYPE) {
			if (currentResponseNumber >= numMaxResponse) {
				// Cambiamos el directorio donde se alojar�n las peticiones e
				// inicializamos el contador
				responseFolderNumber++;
				path = this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber);
				new File(path).mkdir();
				currentResponseNumber = 0;
			} else {
				path = this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber);
			}
			currentResponseNumber++;
			path += "/" + "RES-" + transactionId + ".xml";
		}
		return path;
	}

	/**
	 * Initializes the response directory.
	 */
	private void initializeResponseDir() {
		// Comprobamos si existe el directorio correpondiente al dña de hoy (en
		// caso de existir se crea)
		UtilsTime time = new UtilsTime();
		currentDay = time.toString(UtilsTime.FORMATO_FECHA_JUNTA_CORTA);
		File dayFolder = new File(this.folderResponsePath + "/" + currentDay);
		if (dayFolder.exists()) {
			// Existe el directorio
			String[ ] dirs = dayFolder.list();
			if (dirs != null) {
				responseFolderNumber = dirs.length;
				File currentFolder = new File(this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber));
				if (currentFolder.exists()) {
					String[ ] soaps = currentFolder.list();
					if (soaps != null) {
						currentResponseNumber = soaps.length;
					}
				} else {
					currentResponseNumber = 0;
					new File(this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber)).mkdir();
				}
			} else {
				responseFolderNumber = 1;
				currentResponseNumber = 0;
				new File(this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber)).mkdir();
			}
		} else {
			dayFolder.mkdir();
			currentResponseNumber = 0;
			responseFolderNumber = 1;
			new File(this.folderResponsePath + "/" + currentDay + "/" + FORMATTER.format(responseFolderNumber)).mkdir();
		}
	}
	/**
	 * Registers a SOAP message.
	 * @param soap	SOAP message as array of bytes.
	 * @param transactionId Audit transaction identifier.
	 * @param	soapType Soap type. The allowed values are:<br/>
	 * 	 {@link SOAPPersistenceI#REQUEST_SOAP_TYPE} if the SOAP is request.
	 * 	 {@link SOAPPersistenceI#RESPONSE_SOAP_TYPE} if the SOAP is response.
	 * @return	Unique identifier of record.
	 */
	public String storeSOAP(byte[ ] soap, long transactionId, int soapType) {

		String fileName = null;
		if (soap != null) {
			if (soapType == SOAPPersistenceI.REQUEST_SOAP_TYPE) {
				if (this.folderRequestPath != null && this.serviceRequest != null) {
					fileName = getFilePath(soapType, transactionId);
					FileSOAPThread t = new FileSOAPThread(soap,  fileName);
					this.serviceRequest.execute(t);
				} else {
					LOGGER.error(Language.getMessage(LanguageKeys.AUD_070));
				}

			} else if (soapType == SOAPPersistenceI.RESPONSE_SOAP_TYPE) {
				if (this.folderResponsePath != null && this.serviceResponse != null) {
					fileName = getFilePath(soapType, transactionId);
					FileSOAPThread t = new FileSOAPThread(soap,  fileName);
					this.serviceResponse.execute(t);
				} else {
					LOGGER.error(Language.getMessage(LanguageKeys.AUD_071));
				}
			}
		}
		return fileName;
	}
}
