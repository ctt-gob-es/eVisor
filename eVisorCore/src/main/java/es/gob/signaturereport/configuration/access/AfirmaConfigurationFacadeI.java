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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI.java.</p>
 * <b>Description:</b><p>Interfaces that provides all method to management of configurations of "@firma platform".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Interfaces that provides all method to management of configurations of "@firma platform".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/02/2011.
 */
public interface AfirmaConfigurationFacadeI {

    /**
     * Attribute that represents the constant that identifies the 5.3.1 version of "@firma platform". 
     */
    String VERSION_5_3_1 = "v5d3r1";

    /**
     * Attribute that represents the constant that identifies the 5.4 version of "@firma platform". 
     */
    String VERSION_5_4 = "v5d4";

    /**
     * Attribute that represents the constant that identifies the 5.5 version of "@firma platform". 
     */
    String VERSION_5_5 = "v5d5";
    
    /**
     * Attribute that represents the constant that identifies the 5.6 version of "@firma platform". 
     */
    String VERSION_5_6 = "v5d6";

    /**
     * Attribute that represents the constant that identifies to Web Service of Validation Signature. 
     */
    String VALIDATE_SIGNATURE_SERVICE = "validateSignature";
   

    /**
     * Attribute that represents the "@firma" platform. 
     */
     String AFIRMA_PLATFORM = "@firma";

     /**
 	 * Gets the platform information associated to supplied identifier.
 	 * @param afirmaId	Platform identifier.
 	 * @return				A {@link AfirmaData} object with the information associated. Null if the platform doesn't exist.
 	 * @throws ConfigurationException	If an error occurs. The most common errors are:<br/>
 	 * 								{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
 	 * 								{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
 	 */
    AfirmaData getAfirmaConfiguration(String afirmaId) throws ConfigurationException;
    
    /**
     * Add a new configuration to invoke "@firma" in the system.
      * @param afirmaId	Platform identifier.
	 * @param  afirmaData			Platform information.
	 * @throws ConfigurationException If an error occurs. The most common errors are:<br/>
	 * 								{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link ConfigurationException#DUPLICATE_ITEM} If there is a platform with the identifier supplied.<br/>
	 * 								{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
	 */
    void createAfirmaConfiguration(String afirmaId, AfirmaData afirmaData) throws ConfigurationException;
    
    
    /**
     * Gets all platform identifiers registered in the system.
     * @return	List of platform identifiers.
     * @throws ConfigurationException	if an error occurs.
     */
    String[] getAfirmaIds() throws ConfigurationException;
    
    /**
     * Sets the platform configuration to the configuration supplied.
     * @param afirmaId		Platform identifier.
     * @param afirmaData	Platform configuration.
     * @throws ConfigurationException	If an error occurs. The most common errors are:<br/>
	 * 								{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link ConfigurationException#ITEM_NO_FOUND} If doesn't exist platform with the identifier supplied.<br/>
	 * 								{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
     */
    void modifyAfirmaConfiguration(String afirmaId, AfirmaData afirmaData) throws ConfigurationException;
    
    /**
	 * Removes a platform registered in the system.
	 * @param afirmaId	Platform identifier.
	 * @throws ConfigurationException	If an error occurs. The most common errors are:<br/>
	 * 								{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link ConfigurationException#ITEM_NOT_FOUND} If doesn't exist platform with the identifier supplied.<br/>
	 * 								{@link ConfigurationException#ITEM_ASSOCIATED} This platform is associated to an application.<br/>
	 * 								{@link ConfigurationException#UNKNOWN_ERROR} Other error.<br/>
	 */
    void removeAfirmaConfiguration(String afirmaId) throws ConfigurationException;
    
}
