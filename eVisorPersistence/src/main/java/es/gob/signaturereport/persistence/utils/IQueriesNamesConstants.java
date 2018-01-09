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
 * <b>File:</b><p>es.gob.signaturereport.persistence.utils.IQueriesNamesConstants.java.</p>
 * <b>Description:</b><p>Interface that defines the names used for SQL queries.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * <b>Date:</b><p>07/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 07/05/2013.
 */
package es.gob.signaturereport.persistence.utils;

/**
 * <p>Interface that defines the names used for SQL queries.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * @version 1.0, 07/05/2013.
 */
public interface IQueriesNamesConstants {

    // *************************************************
    // Constantes relativas al esquema de configuración.
    // *************************************************


    /**
     * Constant attribute that represents the query <i>"findAppById"</i>.
     */
    public static final String QUERYNAME_FIND_APPLICATION_BY_ID = "findAppById";

    /**
     * Constant attribute that represents the query <i>"findUosById"</i>.
     */
    public static final String QUERYNAME_FIND_UO_BY_ID = "findUosById";

    /**
     * Constant attribute that represents the query <i>"findPlatformById"</i>.
     */
    public static final String QUERYNAME_FIND_PLATFORM_BY_ID = "findPlatformById";

    /**
     * Constant attribute that represents the query <i>"findTRPKsByIdList"</i>.
     */
    public static final String QUERYNAME_FIND_TRPKS_BY_ID_LIST = "findTRPKsByIdList";

    /**
     * Constant attribute that represents the query <i>"getPKCertificate"</i>.
     */
	public static final String QUERYNAME_CERT_PK = "findCertyId";
	
	/**
     * Constant attribute that represents the query <i>"findCertById"</i>.
     */
    public static final String QUERYNAME_FIND_CERTIFICATE_BY_ID = "findCertById";

    /**
     * Constant attribute that represents the query <i>"findUserByLogin"</i>.
     */
    public static final String QUERYNAME_FIND_USER_BY_LOGIN = "findUserByLogin";

    /**
     * Constant attribute that represents the query <i>"setCommunicationTime"</i>.
     */
    public static final String QUERYNAME_SET_COMMUNICATION_TIME_LOG_ALARM = "setCommunicationTime";

    /**
     * Constant attribute that represents the query <i>"setLastCommunication"</i>.
     */
    public static final String QUERYNAME_SET_LAST_COMMUNICATION_ALARM = "setLastCommunication";

    /**
     * Constant attribute that represents the query <i>"getPendingAlarms"</i>.
     */
    public static final String QUERYNAME_GET_PENDING_ALARMS = "getPendingAlarms";

    /**
     * Constant attribute that represents the query <i>"findAlarmById"</i>.
     */
    public static final String QUERYNAME_FIND_ALARM_BY_ID = "findAlarmById";

    /**
     * Constant attribute that represents the query <i>"findAllAlarms"</i>.
     */
    public static final String QUERYNAME_FIND_ALL_ALARMS = "findAllAlarms";
    
    /**
     * Constant attribute that represents the query <i>"findAllPersons"</i>.
     */
    public static final String QUERYNAME_FIND_ALL_PERSONS = "findAllPersons";
    
    /**
     * Constant attribute that represents the query <i>"findAllPersons"</i>.
     */
    public static final String QUERYNAME_FIND_ALL_USERS = "findAllUsers";
    
    /**
     * Constant attribute that represents the query <i>"findPerson"</i>.
     */
    public static final String QUERYNAME_FIND_PERSON = "findPerson";
    
    /**
	 * Attribute that represent one query that increment version of keystore that name passed for parameter.
	 */
	public static final String QUERYNAME_INC_VERSION = "increaseVersion";
	
	/**
	 * Constant attribute that represents the query <i>"findKSByName"</i>.
	 */
	public static final String QUERYNAME_FIND_BY_KEYSTORE_NAME = "findKSByName";
	
	/**
	 * Constant attribute that represents the query <i>"getVersion"</i>.
	 */
	public static final String QUERYNAME_KEYSTORE_VERSION = "getVersion";
	
	/**
	 * Constant attribute that represents the query <i>"findAllActivePlatforms"</i>.
	 */
	public static final String QUERYNAME_FIND_ALL_ACTIVE_PLATFORMS = "findAllActivePlatforms";
	
	/**
	 * Constant attribute that represents the query <i>"findTRPById"</i>.
	 */
	public static final String QUERYNAME_FIND_TEMPLATEREPORT_BY_ID = "findTRPById";
	
	/**
	 * Constant attribute that represents the query <i>"findTCById"</i>.
	 */
	public static final String QUERYNAME_FIND_TEMPLATECONTENT_BY_ID = "findTCById";
	
	/**
	 * Attribute that represent one query that return id and pk of template.
	 */
	public static final String QUERYNAME_FIND_ALL_ACTIVE_TEMPLATEREPORT = "findAllActiveTR";
		
}
