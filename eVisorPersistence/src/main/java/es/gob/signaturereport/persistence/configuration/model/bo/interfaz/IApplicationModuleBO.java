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
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IApplicationModuleBO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;

import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of applications.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IApplicationModuleBO extends Serializable {

	 /**
		 * Add an application to the configuration.
		 * @param applicationId		Application identifier.
		 * @param uoId				Unit organization identifier. This unit contain the application. Optional.
		 * @param appData			Application configuration.
		 * @throws DatabaseException	If an error occurs. The values might be:<br/>
		 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
		 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
		 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
		 */
	public void createApplication(String applicationId, ApplicationData appData, String uoId) throws DatabaseException;
	
	/**
	 * Update the values of an application
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void updateApplication(ApplicationPOJO applicationPOJO) throws DatabaseException;

	/**
	 * Removes an application from system configuration.
	 * @param applicationId			Application identifier.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist application with the supplied identifier.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one applications with the supplied identifier.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.
	 */
	public void removeApplication(String applicationId) throws DatabaseException;
	
	/**
	 * Gets an application by its identifier.
	 * @param applicationId			Application identifier.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist application with the supplied identifier.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one applications with the supplied identifier.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.
	 */
	public ApplicationPOJO getApplication(String applicationId) throws DatabaseException;
	
	/**
	 * Sets the application information.
	 * @param applicationId	Application identifier.
	 * @param appData		{@link ApplicationData} object that contains the information associated to application to modify.
	 * @throws DatabaseException If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the application is not found.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is more than one element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	public void modifyApplication(String applicationId, ApplicationData appData) throws DatabaseException;

}
