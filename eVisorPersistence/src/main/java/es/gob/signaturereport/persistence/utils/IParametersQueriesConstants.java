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
 * <b>File:</b><p>es.gob.signaturereport.persistence.utils.IParametersQueriesConstants.java.</p>
 * <b>Description:</b><p>Interface that defines the parameters used for SQL queries.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format..</p>
 * <b>Date:</b><p>07/05/2013.</p>
 * @author Gobierno de España.
 * @version 1.0, 07/05/2013.
 */
package es.gob.signaturereport.persistence.utils;


/**
 * <p>Interface that defines the parameters used for SQL queries.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/05/2013.
 */
public interface IParametersQueriesConstants {

    // *************************************************
    // Constantes relativas al esquema de configuración.
    // *************************************************

    /**
     * Constant attribute that represents the string <i>"appId"</i>.
     */
	public static final String PARAM_APPLICATION_ID = "appId";

    /**
     * Constant attribute that represents the string <i>"appId"</i>.
     */
	public static final String PARAM_UO_ID = "uoId";

    /**
     * Constant attribute that represents the string <i>"appId"</i>.
     */
	public static final String PARAM_PLATFORM_ID = "pfId";

    /**
     * Constant attribute that represents the string <i>"appId"</i>.
     */
	public static final String PARAM_TRPKS_ID_LIST = "trIdList";

    /**
	 * Attribute that represent the property associated to query CERT_PK_QUERY of attribute certificate id.
	 */
	public static final String PARAM_CERT_ALIAS = "cert_alias";

	/**
	 * Attribute that represent the property associated to id of keyStore.
	 */
	public static final String PARAM_KEYSTORE_ID = "keystoreId";

	/**
	 * Attribute that represent the property associated to the login of the user.
	 */
	public static final String PARAM_USER_LOGIN = "login";

	/**
	 * Attribute that represents the property used to identify the MAIL column. 
	 */
	public static final String PARAM_MAIL = "mail";
	
	/**
	 * Attribute that represents the property used to identify the COMTIME column. 
	 */
	public static final String PARAM_COMMUNICATION_TIME = "comtime";

	/**
	 * Attribute that represents a list of LOGALARM PKs. 
	 */
	public static final String PARAM_LIST_LOG_ALARM_PK = "listLogPk";

	/**
	 * Attribute that represents the property used to identify the ALARMID column.
	 */
	public static final String PARAM_ALARM_ID = "alarmId";
	
	/**
	 * Attribute that represents the property used to identify the LASTCOM column.
	 */
	public static final String PARAM_LASTCOM_ALARM = "lastCom";
	
	/**
	 * Attribute that represents the property used to identify the NAME column.
	 */
	public static final String PARAM_NAME = "name";
	
	/**
	 * Attribute that represents the property used to identify the SURNAME column.
	 */
	public static final String PARAM_SURNAME = "surname";
	
	/**
	 * Attribute that represents the property used to identify the PHONE column.
	 */
	public static final String PARAM_PHONE = "phone";
	
	/**
	 * Attribute that represents the property used to identify the EMAIL column.
	 */
	public static final String PARAM_EMAIL = "email";
	
	/**
	 * Attribute that represents the property used to identify the KSNAME column.
	 */
	public static final String PARAM_KEYSTORE_NAME = "ksName";
	
	/**
     * Constant attribute that represents the string <i>"trId"</i>.
     */
	public static final String PARAM_TR_ID = "trId";
	
	/**
     * Constant attribute that represents the string <i>"contentId"</i>.
     */
	public static final String PARAM_TEMPLATECONTENT_ID = "contentId";
	
	/**
     * Constant attribute that represents the string <i>"contentId"</i>.
     */
	public static final String PARAM_CREATIONTIME = "creationTime";
	
	/**
     * Constant attribute that represents the string <i>"contentId"</i>.
     */
	public static final String PARAM_BEGININGTIME = "beginningTime";
	
	/**
     * Constant attribute that represents the string <i>"contentId"</i>.
     */
	public static final String PARAM_ENDINGTIME = "endingTime";
	
}
