/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.updateddbb.i18n.Language.java.</p>
 * <b>Description:</b><p>Class that manages the access to the properties files used for generating messages.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/07/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/07/2014.
 */
package es.gob.signaturereport.standalone.maudit.i18n;

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

/**
 * <p>Class that manages the access to the properties files used for generating messages.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 04/07/2014.
 */
public final class LanguageStandalone {

	/**
	 * Constant attribute that represents the locale es_ES.
	 */
	public static final Locale LOCALE_ES_ES = new Locale("es", "ES");

	/**
	 * Constant attribute that represents the locale en_US.
	 */
	public static final Locale LOCALE_EN_US = new Locale("en", "US");

	/**
	 * Constant attribute that represents the name of messages directory inside configuration directory.
	 */
	private static final String MESSAGES_DIRECTORY = "configuration\\message-content";

	/**
	 * Attribute that represents the URL class loader for the messages files.
	 */
	private static URLClassLoader urlClassLoaderMessages = null;

	/**
	 * Constant attribute that represents the key for the configured locale for the module.
	 */
	private static final String LANGUAGE = "LANGUAGE";

	/**
	 * Constant attribute that represents the string to identify the the bundle name for the file with the application language.
	 */
	private static final String BUNDLENAME_LANGUAGE = "message-language";

	/**
	 * Constant attribute that represents the string to identify the bundle name to the file related with logs.
	 */
	private static final String BUNDLENAME_STANDALONE_AUDIT = "MessagesReports";

	/**
	 * Attribute that represents the locale specified in the configuration.
	 */
	private static Locale currentLocale = null;

	/**
	 * Attribute that represents the properties for the locale messages.
	 */
	private static ResourceBundle resStandaloneAudit = null;

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(LanguageStandalone.class);

	/**
	 * Constructor method for the class Language.java.
	 */
	private LanguageStandalone() {
		super();
	}

	/**
	 * Method that initiates the resources for accessing to the properties files.
	 */
	static {
		final File configDirFile = new File(System.getProperty("user.dir"), MESSAGES_DIRECTORY);
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
		reloadMessagesConfiguration();
	}

	/**
	 * Method that loads the configured locale and reload the text messages.
	 */
	public static void reloadMessagesConfiguration() {
		if (urlClassLoaderMessages != null) {
			boolean takeDefaultLocale = false;
			String propLocale = null;

			// Cargamos el recurso que determina el locale.
			ResourceBundle resLocale = ResourceBundle.getBundle(BUNDLENAME_LANGUAGE, Locale.getDefault(), urlClassLoaderMessages);
						
			if (resLocale == null) {
				takeDefaultLocale = true;
			} else {
				propLocale = resLocale.getString(LANGUAGE);
			}
			// Tratamos de inicializar el Locale.
			if (propLocale == null) {
				takeDefaultLocale = true;
			} else {
				propLocale = propLocale.trim();
				String[ ] localeSplit = propLocale.split("_");
				if (localeSplit == null || localeSplit.length != 2) {
					takeDefaultLocale = true;
				} else {
					currentLocale = new Locale(localeSplit[0], localeSplit[1]);
				}
			}
			// Si hay que tomar el locale por defecto...
			if (takeDefaultLocale) {
				LOGGER.error("No property was obtained correctly determining the Locale for log messages. Will take the default locale.");
				currentLocale = Locale.getDefault();
			}
			// Se informa en el log del Locale selecccionado.
			LOGGER.info("Take the next locale for messages logs: " + currentLocale.toString());
			// Se cargan los mensajes
			resStandaloneAudit = ResourceBundle.getBundle(BUNDLENAME_STANDALONE_AUDIT, currentLocale, urlClassLoaderMessages);
		}
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
