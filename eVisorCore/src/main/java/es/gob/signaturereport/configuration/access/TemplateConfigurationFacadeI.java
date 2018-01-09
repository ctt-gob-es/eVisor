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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI.java.</p>
 * <b>Description:</b><p>  Interface that provides all methods to management of configuration of template.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.configuration.access;


import java.util.Map;

import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/** 
 * <p>Interface that provides all methods to management of configuration of template.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
public interface TemplateConfigurationFacadeI {
    
    /**
     * Constants that identifies a report of PDF type. 
     */
    int PDF_REPORT = 0;
          
    /**
     * Attribute that represents that the report does not include the signed document. 
     */
    int INC_SIGNED_DOC_NONE = 0;
    
    /**
     * Attribute that represents that the report includes the signed document as image. 
     */
    int INC_SIGNED_DOC_IMAGE = 1;
    
    /**
     * Attribute that represents that the report includes the signed document embed into the report. 
     */
    int INC_SIGNED_DOC_EMBED = 2;
    
    /**
     * Attribute that represents that the report and signed document will be concatenated. 
     */
    int INC_SIGNED_DOC_CONCAT = 3;
    
    /**
     * Attribute that represents the identifier the report into the concatenation rule. 
     */
    String REPORT_CONTAT_ID = "REP";
    
    /**
     * Attribute that represents the identifier the document into the concatenation rule. 
     */
    String DOCUMENT_CONCAT_ID = "DOC";
    
    /**
     * Attribute that represents the mask of page rule. 
     */
    String RANGE_MASK = "(([1-9][0-9]*)(\\-([1-9][0-9]*))?)((\\s)*,(\\s)*([1-9][0-9]*)(\\-([1-9][0-9]*))?)*";
    
    /**
     * Attribute that represents the mask of rule that  document and report will be concatenated. 
     */
    String CONCANT_MASK = "(REP|DOC)((\\()([1-9][0-9]*)((\\s)*\\-(\\s)*[1-9][0-9]*)?(\\)))?" +
	"((\\s)*\\+(\\s)*(REP|DOC)((\\()([1-9][0-9]*)((\\s)*\\-(\\s)*[1-9][0-9]*)?(\\)))?)*";
    
    /**
     * Gets the configuration information associates to template supplied.
     * @param templateId	Identifier of template.	
     * @return			{@link TemplateData} that contains the configuration information. Null if the template doesn't exist.
     * @throws ConfigurationException	If an error occurs while getting the information. The errors are:
     * 					{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the inputs parameters are invalid.<br/>
     * 					{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    TemplateData getTemplate(String templateId) throws ConfigurationException;
    
    /**
     * Add a template to configuration.
     * @param template	{@link TemplateData} that contains the configuration information.
     * @throws ConfigurationException	If an error occurs. The errors are:
     * 					{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the inputs parameters are invalid.<br/>
     * 					{@link ConfigurationException#DUPLICATE_ITEM} There is a template with the identifier supplied.<br/>
     * 					{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    void createTemplate(TemplateData template) throws ConfigurationException;
    
    
    /**
     * Sets the template information. This method doesn't set the template content.
     * @param template	{@link TemplateData} that contains the configuration information to set.
     * @throws ConfigurationException	If an error occurs. The errors are:
     * 					{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the inputs parameters are invalid.<br/>
     * 					{@link ConfigurationException#ITEM_NO_FOUND} If doesn't exist a template with the identifier supplied.<br/>
     * 					{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    void modifyTemplateData(TemplateData template) throws ConfigurationException;
    
    /**
     * Removes a template.
     * @param templateId	Template identifier.
     * @throws ConfigurationException If an error occurs. The errors are:
     * 					{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the inputs parameters are invalid.<br/>
     * 					{@link ConfigurationException#ITEM_NO_FOUND} If doesn't exist a template with the identifier supplied.<br/>
     * 					{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    void removeTemplate(String templateId) throws ConfigurationException;
    
    /**
     * Checks if exists a template identifier by supplied name.
     * @param templateId Template identifier.
     * @return	True if exist a template, otherwise false.
     * @throws ConfigurationException If an error occurs. The errors are:
     * 					{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the inputs parameters are invalid.<br/>
     * 					{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    boolean existTemplate(String templateId) throws ConfigurationException;
    
    /**
     * Gets all template identifiers registered in the system.
     * @return	Map that contains the identifiers. The structure is:
     * 			Key: Template identifier. Value: Template type.
     * @throws ConfigurationException If an error occurs.
     */
    Map<String, Integer> getTemplateIds() throws ConfigurationException;
}
