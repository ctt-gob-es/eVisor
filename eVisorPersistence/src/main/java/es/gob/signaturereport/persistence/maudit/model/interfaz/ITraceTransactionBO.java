package es.gob.signaturereport.persistence.maudit.model.interfaz;

import java.io.Serializable;

import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO;

/**
 * <p>Interface that defines all the operations related with the management of trace fields.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface ITraceTransactionBO extends Serializable {
	
	/**
	 * Stores a TraceTransaction into database
	 * @param traceTransaction The trace transaction to persist
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void save(TraceTransactionPOJO traceTransaction) throws DatabaseException;

}
