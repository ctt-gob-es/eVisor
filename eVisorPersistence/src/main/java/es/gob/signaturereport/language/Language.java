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
 * <b>File:</b><p>es.gob.signaturereport.language.Language.java.</p>
 * <b>Description:</b><p> Class responsible for managing the access to language
 * resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/02/2011.
 */
package es.gob.signaturereport.language;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import es.gob.signaturereport.utils.UtilsJBoss;

/** 
 * <p>Class responsible for managing the access to language
 * resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/02/2011.
 */
public class Language {

	/**
	 * Constructor method for the class Language.java. 
	 */
	private Language() {
	}

	/**
	 * Attribute that represents the list of messages.
	 */
	private static ResourceBundle msgReports;

	/**
	 * Attribute that represents the properties for the locale for the standalone audit messages.
	 */
	private static ResourceBundle resStandaloneAudit = null;

	/**
	 * Attribute that represents the locale specified in the configuration.
	 */
	private static Locale currentLocale;

	/**
	 * Constant attribute that represents the name of messages directory inside configuration directory.
	 */
	private static final String MESSAGES_DIRECTORY = "message-content";

	/**
	 * Attribute that represents the URL class loader for the messages files.
	 */
	private static URLClassLoader urlClassLoaderMessages = null;

	/**
	 * Attribute that represents the name of property file for language configuration. 
	 */
	private static final String FILE_PROP_NAME = "message-language";

	/**
	 * Attribute that represents the property that indicates the language used by the system. 
	 */
	private static final String LANGUAGE_ATT = "LANGUAGE";

	/**
	 * Attribute that represents the location of file that contains the messages of system. 
	 */
	private static final String CONTENT_LANGUAGE_PATH = "MessagesReports";
	
	/**
	 * Constant attribute that represents the string to identify the bundle name to the file related with standalone audit logs.
	 */
	private static final String BUNDLENAME_STANDALONE_AUDIT = "standalone.audit";
	
	/**
	 * Constant attribute that represents the string to identify the bundle name to the file related with standalone audit logs.
	 */
	private static final String BUNDLENAME_STANDALONE_STATISTICS = "standalone.audit";

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(Language.class);

	static {

		// Preparamos el URLClassLoader que hará referencia
		// al directorio de los mensajes de logs dentro de la configuración.
		final File configDirFile = new File(UtilsJBoss.createAbsolutePath(UtilsJBoss.getJBossServerConfigDir(), MESSAGES_DIRECTORY));
		urlClassLoaderMessages = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

			public URLClassLoader run() {
				try {
					return new URLClassLoader(new URL[ ] { configDirFile.toURI().toURL() });
				} catch (MalformedURLException e) {
					LOGGER.error(e);
					return null;
				}
			}
		});

		currentLocale = new Locale(ResourceBundle.getBundle(FILE_PROP_NAME, Locale.getDefault(), urlClassLoaderMessages).getString(LANGUAGE_ATT));

		msgReports = ResourceBundle.getBundle(CONTENT_LANGUAGE_PATH, currentLocale, urlClassLoaderMessages);
		
		// Se cargan los mensajes del procesador de logs externo
		resStandaloneAudit = ResourceBundle.getBundle(CONTENT_LANGUAGE_PATH, currentLocale, urlClassLoaderMessages);

	}

	/**
	 * Gets the message with the key and values indicated as input parameters.
	 * @param key Key for obtain the message.
	 * @param values Values for insert in the message.
	 * @return String with the message well-formed.
	 */
	public static String getFormatMessage(String key, Object[ ] values) {

		return new MessageFormat(msgReports.getString(key), currentLocale).format(values);
	}

	/**
	 * Gets the message with the key indicated as input parameters.
	 * @param key Key for obtain the message.
	 * @return String with the message.
	 */
	public static String getMessage(String key) {
		return msgReports.getString(key);
	}

	/**
	 * Method that gets the bundle message of the standalone audit module for a certain key.
	 * @param key Parameter that represents the key for obtain the message.
	 * @return The bundle message of the standalone audit module for certain key.
	 */
	public static String getResStandaloneAudit(String key) {
		return resStandaloneAudit.getString(key);

	}
	
	/**
	 * Method that gets the bundle message of the standalone audit module for a certain key and values indicated as input parameters.
	 * @param key Parameter that represents the key for obtain the message.
	 * @param values Parameter that represents the list of values for insert in the message.
	 * @return the bundle message of the standalone audit module for certain key and values indicated as input parameters.
	 */
	public static String getFormatResStandaloneAudit(String key, Object[ ] values) {
		return new MessageFormat(resStandaloneAudit.getString(key), currentLocale).format(values);
	}

}
