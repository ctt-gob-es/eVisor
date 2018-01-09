package es.gob.signaturereport.persistence.maudit.model.interfaz;

import java.io.Serializable;
import java.util.List;

import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.pojo.ActionTypePOJO;

/**
 * <p>Interface that defines all the operations related with the management of actin types.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IActionTypeBO extends Serializable {

	/**
	 * Gets all operations registered in the audit module.
	 * @return	Audit operation list.
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	List<ActionTypePOJO> getAll() throws DatabaseException;
}
