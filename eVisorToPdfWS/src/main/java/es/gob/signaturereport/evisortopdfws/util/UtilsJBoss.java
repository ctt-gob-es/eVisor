/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-2013 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.tsa.utilidades.UtilsJBoss.java.</p>
 * <b>Description:</b><p>Utility class for JBoss properties and others.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * <b>Date:</b><p>02/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 02/05/2013.
 */
package es.gob.signaturereport.evisortopdfws.util;

import java.io.File;

/**
 * <p>Utility class for JBoss properties and others.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * @version 1.0, 02/05/2013.
 */
public final class UtilsJBoss {

    /**
     * Constant attribute that represents the property key jboss.server.config.dir.
     */
    public static final String PROP_JBOSS_SERVER_CONFIG_DIR = "jboss.server.config.dir";

    /**
     * Constructor method for the class UtilsJBoss.java.
     */
    private UtilsJBoss() {
    }

    /**
     * Method that returns the value of the system property jboss.server.config.dir.
     * @return Value of the system property jboss.server.config.dir. Null if not exist.
     */
    public static String getJBossServerConfigDir() {
	return System.getProperty(PROP_JBOSS_SERVER_CONFIG_DIR);
    }

    /**
     * Auxiliary method to create an absolute path to a file.
     * @param pathDir Directory absolute path that contains the file.
     * @param filename Name of the file.
     * @return Absolute path of the file.
     */
    public static String createAbsolutePath(String pathDir, String filename) {
	return pathDir + File.separator + filename;
    }

}
