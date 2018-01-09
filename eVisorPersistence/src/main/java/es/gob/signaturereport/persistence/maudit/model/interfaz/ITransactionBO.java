package es.gob.signaturereport.persistence.maudit.model.interfaz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.pojo.FieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO;

/**
 * <p>Interface that defines all the operations related with the management of transactions.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface ITransactionBO extends Serializable {
	
	/**
	 * Stores a transaction into database
	 * @param transaction The Transaction to be stored
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	void save(TransactionPOJO transaction) throws DatabaseException;

	/**
	 * Gets a transaction list (no transaction details) that matches with the supplied parameters. 
	 * @param beginningTime	Beginning time.
	 * @param endingDate	Ending time.
	 * @param service		Service identifier.
	 * @param application	Application identifier.
	 * @param firstResult	Index of first result.
	 * @param maxResults	Max number of the records.
	 * @param appField 		FieldPOJO filter
	 * @return	Transaction list.
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	List<TransactionPOJO> getTransactions(Date beginningTime, Date endingDate, Integer service, String application, Integer firstResult, Integer maxResults, FieldPOJO appField) throws DatabaseException;
	
	/**
	 * Gets transaction details associated to supplied identifier. 
	 * @param transactionId	Transaction identifier.
	 * @return	Audit transaction details.
	 * @throws DatabaseException	If an error occurs. The values might be:<br/>
	 * 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	 * 								{@link DatabaseException#DUPLICATE_ITEM} There is one application with the identifier supplied.<br/>
	 * 								{@link DatabaseException#UNKNOWN_ERROR} Other error.<br/>
	 */
	TransactionPOJO getTransaction(long transactionId) throws DatabaseException;
}
