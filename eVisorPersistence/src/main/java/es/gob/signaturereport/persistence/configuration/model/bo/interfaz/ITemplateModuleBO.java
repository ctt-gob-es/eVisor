package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;
import java.util.Map;

import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateContentPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateReportPOJO;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of Templates.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface ITemplateModuleBO extends Serializable {
	
	/**
	 * Gets the template report information registered into database.
	 * @param templateId	Template report identifier.
	 * @return	{@link TemplateReportPOJO} object that contains the template information.
	 * @throws DatabaseException	If an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the template doesn't exist.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	TemplateReportPOJO getTemplateReport(String templateId) throws DatabaseException;
	
	/**
	 * Gets the template content information registered into database.
	 * @param templateId	Template content identifier.
	 * @return	{@link TemplateContentPOJO} object that contains the template information.
	 * @throws DatabaseException	If an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the template doesn't exist.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	TemplateContentPOJO getTemplateContent(String templateId) throws DatabaseException;
	
	/**
	 * Add a template to the configuration of the system.
	 * @param templateReport	TemplateReportPOJO information.
	 * @param templateContent TemplateContentPOJO information.
	 * @throws DatabaseException	If an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void createTemplate(TemplateReportPOJO templateReport, TemplateContentPOJO templateContent) throws DatabaseException;
	
	/**
	 * Sets the template information. This method doesn't set the template content.
	 * @param template	TemplateReportPOJO information.
	 * @param newContent indicates if the template content is new in BD.
	 * @throws DatabaseException	If an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the template doesn't exist.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void modifyTemplate(TemplateReportPOJO template, boolean newContent) throws DatabaseException;
	
	/**
	 * Removes a template.
	 * @param templateId	Template identifier.
	 * @throws DatabaseException	If an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the template doesn't exist.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 *								{@link DatabaseException#ITEM_ASSOCIATED} This template is associated to other item.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void removeTemplate(String templateId) throws DatabaseException;
	
	/**
	 * Gets all template identifiers registered in the system.
	 * @return	Map that contains the identifiers. The structure is:
	 * 			Key: Template identifier. Value: Template type.
	 * @throws DatabaseException	If an error occurs.
	 */
	Map<String, Integer> getTemplateIds() throws DatabaseException;
	
	/**
	 * Method that checks if exists a template with the supplied identifier.
	 * @param templateId	Template identifier.
	 * @return	True if exist template, otherwise false.
	 * @throws DatabaseException if an error occurs. The errors are:
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	boolean existTemplate(String templateId) throws DatabaseException;
}
