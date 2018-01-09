/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2013-,2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.tsa.utilities.StaticTSAConfig.java.</p>
 * <b>Description:</b><p>Class contains static properties of TS@. This properties are immutable and they can be modified only restarting the server context.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * <b>Date:</b><p>06/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 06/05/2013.
 */
package es.gob.signaturereport.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.signaturereport.utils.UtilsJBoss;
import es.gob.signaturereport.utils.UtilsResources;

/**
 * <p>Class contains static properties of Evisor. This properties are immutable and they can be modified only restarting the server context.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 06/05/2013.
 */
public final class StaticSignatureReportProperties {

	/**
	 * Attribute that represents set of properties of the platform.
	 */
	private static Properties staticProperties;

	/**
	 * Attribute that represents the class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(StaticSignatureReportProperties.class);

	/**
	 * Attribute that represents name of properties file.
	 */
	public static final String STATIC_EVISOR_FILENAME = "signaturereport.properties";
	
	/**
	 * Constructor method for the class StaticTSAConfig.java.
	 */
	private StaticSignatureReportProperties() {
	}

	/**
	 * Gets all properties from original file.
	 * @return all properties
	 */
	private static Properties getProperties() {
		if (staticProperties == null) {
			synchronized (StaticSignatureReportProperties.class) {
				if (staticProperties == null) {
					staticProperties = new Properties();
					FileInputStream configStream = null;
					try {
						//LOGGER.trace(Language.getFormatResCommonsUtilGeneral(ICommonsUtilGeneralKeys.STC_LOG001, new Object[ ] { STATIC_TSA_FILENAME }));
						configStream = new FileInputStream(UtilsJBoss.createAbsolutePath(UtilsJBoss.getJBossServerConfigDir(), STATIC_EVISOR_FILENAME));
						staticProperties.load(configStream);
						//LOGGER.trace(Language.getFormatResCommonsUtilGeneral(ICommonsUtilGeneralKeys.STC_LOG002, new Object[ ] { staticProperties }));
					} catch (IOException e) {
						//LOGGER.error(Language.getFormatResCommonsUtilGeneral(ICommonsUtilGeneralKeys.STC_LOG003, new Object[ ] { STATIC_TSA_FILENAME, e.getMessage() }), e);
					} finally {
						UtilsResources.safeCloseInputStream(configStream);
					}
				}
			}
		}
		return staticProperties;
	}

	/**
	 * Returns the value of property given.
	 * @param propertyName name of @Firma property.
	 * @return the value of property given.
	 */
	public static String getProperty(String propertyName) {
		return (String) getProperties().get(propertyName);
	}

}
