package es.gob.signaturereport.persistence.statistics.model.bo.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.em.IAuditEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.statistics.model.bo.interfaz.IStatisticsBO;
import es.gob.signaturereport.persistence.statistics.model.pojo.GroupingsPOJO;
import es.gob.signaturereport.persistence.statistics.model.pojo.StatisticTimesPOJO;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;

/**
 * <p>Class manager for the Business Objects for statistics module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class StatisticsBO implements IStatisticsBO {

	
	/**
	 * Attribute that represents serialVersionUID. 
	 */
	private static final long serialVersionUID = 7595836213476355178L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StatisticsBO.class);
    
    /**
	 * Constant that represents the attribute name associated to "ST_DATE" column.
	 */
	private static final String ST_DATE = "stDate";

	/**
	 * Constant that represents the index of column "Service".
	 */
	private static final int SERVICE_INDEX = 0;

	/**
	 * Constant that represents the index of column "Application".
	 */
	private static final int APPLICATION_INDEX = 1;

	/**
	 * Constant that represents the index of column "UO".
	 */
	private static final int UO_INDEX = 2;

	/**
	 * Constant that represents the index of column "Code".
	 */
	private static final int CODE_INDEX = 3;

	/**
	 * Constant that represents the index of column "Issign".
	 */
	private static final int ISSIGN_INDEX = 4;

	/**
	 * Constant that represents the index of column "Total".
	 */
	private static final int TOTAL_INDEX = 5;
	
	/**
	 * Constant that represents the 'compute statistics' query name.
	 */
	private static final String COMPUTE_ST_QUERY = "computeStatistics";
	/**
	 * Constant that represents the beginning time field.
	 */
	private static final String BEGINNING_TIME_PROP = "beginning";
	/**
	 * Constant that represents the ending time field.
	 */
	private static final String ENDING_TIME_PROP = "ending";
	
	/**
	 * Attribute that represents the name of the application field when this is unknown. 
	 */
	private static final String UNKNOWN_APP_NAME = Language.getMessage(LanguageKeys.ST_001);
	
	/**
	 * Attribute that represents the name of the UO field when this is unknown. 
	 */
	private static final String UNKNOWN_OU_NAME = Language.getMessage(LanguageKeys.ST_002);

	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    @Inject
    private transient IAuditEntityManager em;


	@Override
	public List<Object[ ]> getStatistics(String hqlGroupQuery, Date beginningTime, Date endingTime) throws DatabaseException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_BEGININGTIME, beginningTime);
		parameters.put(IParametersQueriesConstants.PARAM_ENDINGTIME, endingTime);
		
		return em.createHQLQuery(hqlGroupQuery, parameters);
	}


	@Override
	public void computeStatistics(Date stDate, boolean update, String calTimeStr) {
		
		LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_044, new Object[ ] { calTimeStr, String.valueOf(update) }));
		
			Calendar cal = Calendar.getInstance();
			cal.setTime(stDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date beginningTime = cal.getTime();
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);
			Date endingTime = cal.getTime();
			
			final CriteriaBuilder critBuilder = em.getCriteriaBuilder();
			final CriteriaQuery<StatisticTimesPOJO> criteria = critBuilder.createQuery(StatisticTimesPOJO.class);
			Root<StatisticTimesPOJO> root = criteria.from(StatisticTimesPOJO.class);
			criteria.where(critBuilder.equal(root.<Date> get(ST_DATE), beginningTime));
					
			List<StatisticTimesPOJO> list = em.createQuery(criteria).getResultList();
			if (list != null && !list.isEmpty()) {
				if (update) {
					LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_046, new Object[ ] { calTimeStr }));
					em.remove(StatisticTimesPOJO.class, list.get(0).getStTimePk());
				} else {
					LOGGER.warn(Language.getFormatMessage(LanguageKeys.AUD_047, new Object[ ] { calTimeStr }));
					return;
				}
			}
			StatisticTimesPOJO stTime = new StatisticTimesPOJO();
			stTime.setCreationTime(new Date());
			stTime.setStDate(beginningTime);
			
			em.persist(stTime);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(BEGINNING_TIME_PROP, beginningTime);
			parameters.put(ENDING_TIME_PROP, endingTime);
			
			List<Object[ ]> results = (List<Object[ ]>) em.namedQuery(COMPUTE_ST_QUERY, parameters);

			if (results != null) {
				Iterator<Object[ ]> resultIt = results.iterator();
				while (resultIt.hasNext()) {
					Object[ ] result = resultIt.next();
					if(result[SERVICE_INDEX] != null && result[TOTAL_INDEX] != null){
						GroupingsPOJO g = new GroupingsPOJO();
						g.setService(((BigDecimal) result[0]).intValue());
						g.setTotal(((BigDecimal) result[TOTAL_INDEX]).longValue());
						if (result[APPLICATION_INDEX] != null) {
							g.setApplication((String) result[APPLICATION_INDEX]);
						}else{
							g.setApplication(UNKNOWN_APP_NAME);
						}
						if (result[UO_INDEX] != null) {
							g.setUo((String) result[UO_INDEX]);
						}else{
							g.setUo(UNKNOWN_OU_NAME);
						}
						if (result[CODE_INDEX] != null) {
							g.setCode((String) result[CODE_INDEX]);
						}
						if (result[ISSIGN_INDEX] != null) {
							g.setSign((String) result[ISSIGN_INDEX]);
						}
						g.setStTime(stTime);
						g.setCreationTime(new Date());
						em.persist(g);
					}				
				}
			}
			LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_045, new Object[ ] { calTimeStr }));
		
	}

}
