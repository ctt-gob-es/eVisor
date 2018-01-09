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
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.bo.interfaz.IUoModuleBO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence.configuration.model.bo.interfaz;

import java.io.Serializable;

import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.UoPOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of Unit Organization.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IUoModuleBO extends Serializable {

	/**
	 * Gets the unit information.
	 * @param unitId	Unit identifier.
	 * @param recursive	Indicates if  you want to get the applications and units registered in the subunits of the supplied unit.
	 * @return	A {@link uoPOJO} with the information.
	 * @throws ConfigurationException	If an error occurs. :<br/>
	 */
	UoPOJO getUO(String unitId, boolean recursive) throws DatabaseException;

	/**
	 * Method that allows to register an  organizational unit in the system.
	 * @param unitId	Unit identifier.
	 * @param name		Unit name.
	 * @param parentId	Unit parent identifier. Optional.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} If there is the element with the identifier supplied.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If the parent item is no found.
	 */
	void createUO(String unitId, String name, String parentId) throws DatabaseException;

	/**
	 * Update the values of an UO
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void updateUO(UoPOJO uoPOJO) throws DatabaseException;
	
	/**
	 * Method that allows to delete an unit organization.
	 * @param unitId	Unit identifier.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If item is no found.
	 */
	public void deleteUO(String unitId) throws DatabaseException;
	
	/**
	 * Change the name of an unit organization.
	 * @param unitId	Unit organization identifier.
	 * @param name		Name to set.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#ITEM_NOT_FOUND} If item is no found.
	 */
	public void modifyUOName(String unitId, String name) throws DatabaseException;
	
	/**
	 * Gets the key of root unit.
	 * @return	Value of UO_PK for the record.
	 */
	Long getRootPk();

}
