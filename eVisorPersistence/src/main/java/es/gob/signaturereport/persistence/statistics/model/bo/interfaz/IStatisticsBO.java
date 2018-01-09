package es.gob.signaturereport.persistence.statistics.model.bo.interfaz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.gob.signaturereport.persistence.exception.DatabaseException;

/**
 * <p>Interface that defines all the operations related with the management of statistics module.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
public interface IStatisticsBO extends Serializable {

	/**
	 * Method that calculates the statistics by supplied field.
	 * @param hqlGroupQuery		HQL query which obtains grouped statistics.
	 * @param beginningTime		Beginning time.
	 * @param endingTime		Ending time.
	 * @return					object that contains the results.
	 * @throws DatabaseException	If an error occurs. The most common errors are:<br/>
	* 								{@link DatabaseException#INVALID_INPUT_PARAMETERS} The input parameters are invalid.<br/>
	* 								{@link DatabaseException#ITEM_NOT_FOUND} If doesn't exist store with the supplied identifier.<br/>
	* 								{@link DatabaseException#DUPLICATE_ITEM} If there is more than one certificate with the supplied alias.
	 */
	List<Object[ ]> getStatistics(String hqlGroupQuery, Date beginningTime, Date endingTime) throws DatabaseException;
	
	void computeStatistics(Date stDate, boolean update, String calTimeStr);

}
