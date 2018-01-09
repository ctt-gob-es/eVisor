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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI.java.</p>
 * <b>Description:</b><p> Interface that provides all methods to management of configuration of applications and organizational units.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.configuration.items.UnitOrganization;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
	   

/** 
 * <p>Interface that provides all methods to management of configuration of applications and organizational units.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
public interface ApplicationConfigurationFacadeI {

    /**
     * Gets the configuration data of the supplied application.
     * @param applicationId 	Application identifier.
     * @return		ApplicationData that contains the configuration data. Null if the application doesn't exist.
     * @throws ConfigurationException  If an error occurs. The values might be:<br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     * 
     */
    ApplicationData getApplicationData(final String applicationId) throws ConfigurationException;

    /**
     * Creates an unit organizational in the system.
     * @param unitId	Unit identifier.
     * @param name	Unit name.	
     * @throws ConfigurationException	If an error occurs. The values might be: <br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#ITEM_NO_FOUND} If the parent unit doesn't exist. <br/>
     * 	{@link ConfigurationException#DUPLICATE_ITEM} The identifier is associated to other unit organization.<br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    void createUO(String unitId, String name) throws ConfigurationException;

    /**
     * Delete an unit system.
     * @param unitId	Unit identifier.
     * @throws ConfigurationException	If an error occurs. The values might be: <br/>
     *  {@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#ITEM_NO_FOUND} If the unit doesn't exist. <br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    void deleteUO(String unitId) throws ConfigurationException;

    /**
     * Register an application in the system.
     * @param applicationId		Application identifier.
     * @param appData			Application information.
     * @throws ConfigurationException	If an error occurs. The values might be: <br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#DUPLICATE_ITEM} If supplied identifier is used by other application or unit organization. <br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    void createApplication(String applicationId, ApplicationData appData) throws ConfigurationException; 

    /**
     * Modify the name of an Organizational Unit.
     * @param unitId	Organizational Unit identifier.
     * @param name		Name to set.
     * @throws ConfigurationException	If an error occurs. The values might be: <br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#ITEM_NO_FOUND} If unit is not found. <br/>
     * 		{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    void modifyUO (String unitId, String name) throws ConfigurationException;

    /**
     * Modify the information of an application registered in the system.
     * @param applicationId		Application identifier.
     * @param appData			Application information.
     * @throws ConfigurationException	If an error occurs. The values might be: <br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid. <br/>
     * 	{@link ConfigurationException#ITEM_NO_FOUND} If application is not found. <br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    void modifyApplication(String applicationId,ApplicationData appData) throws ConfigurationException;

    /**
     * Gets the unit organization information.
     * @param unitId	Unit identifier.
     * @param recursive	Indicates if  you want to get the applications and units registered in the subunits of the supplied unit.
     * @return A {@link UnitOrganization} with the information. Null if the unit doesn't exist.
     * @throws ConfigurationException	If an error occurs. The values are:<br/>
     * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} If the input parameters are invalid.<br/>
     * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
     */
    UnitOrganization getUnitOrganization(final String unitId, final boolean recursive) throws ConfigurationException;

    /**
	 * Removes an application from system configuration.
	 * @param applicationId			Application identifier.
	 * @throws ConfigurationException	If an error occurs. The most common errors are:<br/>
	 * 	{@link ConfigurationException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 *	{@link ConfigurationException#ITEM_NO_FOUND}If doesn't exist application with the supplied identifier.<br/>
	 * 	{@link ConfigurationException#UNKNOWN_ERROR} Other error.
	 */
	void removeApplication(String applicationId) throws ConfigurationException;
    /**
	 * Attribute that represents the identifier used to identify the root unit. 
	 */
	 String ROOT_UNIT_ID = "ROOT";
}
