package es.gob.signaturereport.persistence.statistics.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.persistence.em.IAuditEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.statistics.model.bo.interfaz.IGroupingsBO;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;

/**
 * <p>Class manager for the Business Objects for groupings in statistics module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GroupingsBO implements IGroupingsBO {

	/**
	 * Attribute that represents . 
	 */
	private static final long serialVersionUID = 3810368113738690440L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(GroupingsBO.class);

	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    @Inject
    private transient IAuditEntityManager em;

	@Override
	public List<Object[ ]> getGroupedStatistics(String hqlGroupQuery, Date beginningTime, Date endingTime) throws DatabaseException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_BEGININGTIME, beginningTime);
		parameters.put(IParametersQueriesConstants.PARAM_ENDINGTIME, endingTime);
		
		return em.createHQLQuery(hqlGroupQuery, parameters);
	}

}
