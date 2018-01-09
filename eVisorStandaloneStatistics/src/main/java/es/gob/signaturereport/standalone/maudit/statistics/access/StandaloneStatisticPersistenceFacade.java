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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.persistence.StatisticPersistenceFacade.java.</p>
 * <b>Description:</b><p>Persistence Facade Class for computing statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.standalone.maudit.statistics.access;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.maudit.statistics.StatisticsManager;
import es.gob.signaturereport.maudit.statistics.item.GroupedStatistics;
import es.gob.signaturereport.maudit.statistics.item.RestrictionI;
import es.gob.signaturereport.maudit.statistics.item.TabularStatistics;
import es.gob.signaturereport.persistence.statistics.model.pojo.GroupingsPOJO;
import es.gob.signaturereport.persistence.statistics.model.pojo.StatisticTimesPOJO;
import es.gob.signaturereport.standalone.maudit.statistics.i18n.LanguageStandalone;
import es.gob.signaturereport.standalone.persistence.em.StandaloneAuditEntityManager;
import es.gob.signaturereport.tools.UtilsTime;

/**
 * <p>Persistence Facade Class for computing statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
public final class StandaloneStatisticPersistenceFacade {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StandaloneStatisticPersistenceFacade.class);

	/**
	 * Attribute that represents a class instance.
	 */
	private static StandaloneStatisticPersistenceFacade instance = null;
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
	 * Attribute that represents the name of the application field when this is unknown. 
	 */
	private static final String UNKNOWN_APP_NAME = LanguageStandalone.getResStandaloneStatistics(LanguageKeys.ST_001);
	
	/**
	 * Attribute that represents the name of the UO field when this is unknown. 
	 */
	private static final String UNKNOWN_OU_NAME = LanguageStandalone.getResStandaloneStatistics(LanguageKeys.ST_002);
	
	/**
	 * Constructor method for the class StatisticPersistenceFacade.java.
	 */
	private StandaloneStatisticPersistenceFacade() {
	}

	/**
	 * Gets an instance of the class.
	 * @return	Instance of the class.
	 */
	public static StandaloneStatisticPersistenceFacade getInstance() {
		if (instance == null) {
			instance = new StandaloneStatisticPersistenceFacade();
		}
		return instance;
	}

	/**
	 * Compute the one-day usage statistics from stored audit information.
	 * @param stDate	Day in which you want to calculate statistics.
	 * @param update	If the previous values will be deleted.
	 * @param sendAlarm	If an error occurs then an alarm will be send.
	 * @throws StandaloneStatisticPersistenceException	If an error occurs.
	 */
	public void computeStatistics(Date stDate, boolean update, boolean sendAlarm) throws StandaloneStatisticPersistenceException {
		if (stDate == null) {
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.INVALID_INPUT_PARAMETERS, LanguageStandalone.getResStandaloneStatistics(LanguageKeys.AUD_050));
		}
		EntityManager em = null;
		EntityTransaction tx = null;
		String calTimeStr = new UtilsTime(stDate).toString(StatisticsManager.TIME_PATTERN);
		LOGGER.info(LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_044, new Object[ ] { calTimeStr, String.valueOf(update) }));
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(stDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date beginningTime = cal.getTime();
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);
			Date endingTime = cal.getTime();
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			tx = em.getTransaction();
			tx.begin();
			
			final CriteriaBuilder critBuilder = em.getCriteriaBuilder();
			final CriteriaQuery<StatisticTimesPOJO> criteria = critBuilder.createQuery(StatisticTimesPOJO.class);
			Root<StatisticTimesPOJO> root = criteria.from(StatisticTimesPOJO.class);
			criteria.where(critBuilder.equal(root.<Date> get(ST_DATE), beginningTime));
					
			List<StatisticTimesPOJO> list = em.createQuery(criteria).getResultList();
			if (list != null && !list.isEmpty()) {
				if (update) {
					LOGGER.info(LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_046, new Object[ ] { calTimeStr }));
					em.remove(list.get(0));
				} else {
					LOGGER.warn(LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_047, new Object[ ] { calTimeStr }));
					return;
				}
			}
			StatisticTimesPOJO stTime = new StatisticTimesPOJO();
			stTime.setCreationTime(new Date());
			stTime.setStDate(beginningTime);
			
			em.persist(stTime);
			
			Query query = em.createNamedQuery(COMPUTE_ST_QUERY);
			query.setParameter(BEGINNING_TIME_PROP, beginningTime);
			query.setParameter(ENDING_TIME_PROP, endingTime);
			
			@SuppressWarnings("unchecked")
			List<Object[ ]> results = query.getResultList();
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
			tx.commit();
			LOGGER.info(LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_045, new Object[ ] { calTimeStr }));
		} catch (Exception e) {
			String msg = LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_043, new Object[ ] { calTimeStr });
			LOGGER.error(msg, e);
			
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.UNKNOWN_ERROR, msg, e);
		} finally {
			
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Gets the tabulated statistics from the supplied parameters.
	 * @param beginningTime	Beginning time.
	 * @param endingTime	Ending time.
	 * @param rowField		Row result field.
	 * @param columnField	Column result field.
	 * @param restrictions	List of restrictions to add in the query.
	 * @return		A {@link TabularStatistics} object that will contain the results.
	 * @throws StandaloneStatisticPersistenceException	If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public TabularStatistics getTabulatedStatistics(Date beginningTime, Date endingTime, int rowField, int columnField, List<RestrictionI> restrictions) throws StandaloneStatisticPersistenceException {
				
		if (beginningTime == null || endingTime == null) {
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.INVALID_INPUT_PARAMETERS, LanguageStandalone.getResStandaloneStatistics(LanguageKeys.AUD_063));
		}
		String rowExp = getGroupingField(rowField);
		String columnExp = getGroupingField(columnField);
		String hql = "select " + rowExp + ", " + columnExp + ", sum (g.total) from Groupings g, StatisticTimes t where g.stTime = t.stTimePk" + " and t.stDate >= :beginningTime and t.stDate <= :endingTime and " + rowExp + " is not null and " + columnExp + " is not null";
		EntityManager em = null;
		
		try {
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			
			if (restrictions != null && !restrictions.isEmpty()) {
				String allRes = "";
				for (int i = 0; i < restrictions.size(); i++) {
					RestrictionI r = restrictions.get(i);
					String expresion = getHQLRestriction(r);
					if (expresion != null) {
						if (!allRes.equals("")) {
							allRes += " " + r.getRestrictionUnion() + " " + expresion;
						} else {
							allRes += expresion;
						}
					}
				}
				if (!allRes.equals("")) {
					hql += " and (" + allRes + ")";
				}
			}
			hql += " group by " + rowExp + ", " + columnExp + " order by " + rowExp;
			
			Query query = em.createQuery(hql);
			query.setParameter("beginningTime", beginningTime);
			query.setParameter("endingTime", endingTime);
						
			List<Object[ ]> resultList = query.getResultList();
			TabularStatistics st = new TabularStatistics(rowField, columnField);
			if(resultList!=null && !resultList.isEmpty()){
				for(int i=0;i<resultList.size();i++){
					Object[] result = resultList.get(i);
					st.addValue(result[0].toString(), result[1].toString(), (Long)result[2]);
				}
			}
			return st;
		} catch (Exception e) {
			String msg = LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_064, new Object[ ] { new UtilsTime(beginningTime).toString(StatisticsManager.TIME_PATTERN), new UtilsTime(endingTime).toString(StatisticsManager.TIME_PATTERN) });
			LOGGER.error(msg, e);
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.UNKNOWN_ERROR, msg, e);
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
		
	}

	/**
	 * Method that calculates the grouping statistics by supplied field.
	 * @param beginningTime		Beginning time.
	 * @param endingTime		Ending time.
	 * @param field				Grouping field.
	 * @param restrictions		List of restrictions to add in the query.
	 * @return					{@link GroupedStatistics} object that contains the results.
	 * @throws StandaloneStatisticPersistenceException	If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public GroupedStatistics getGroupedStatistics(Date beginningTime, Date endingTime, int field, List<RestrictionI> restrictions) throws StandaloneStatisticPersistenceException {
		
		if (beginningTime == null || endingTime == null) {
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.INVALID_INPUT_PARAMETERS, LanguageStandalone.getResStandaloneStatistics(LanguageKeys.AUD_063));
		}
		String groupingField = getGroupingField(field);
		String hql = "select " + groupingField + ", sum (g.total) from GroupingsPOJO g, StatisticTimesPOJO t where g.stTime = t.stTimePk" + " and t.stDate >= :beginningTime and t.stDate <= :endingTime and " + groupingField + " is not null";
		EntityManager em = null;
		
		try {
			
			em = StandaloneAuditEntityManager.getInstance().getEntityManager();
			
			if (restrictions != null && !restrictions.isEmpty()) {
				String allRes = "";
				for (int i = 0; i < restrictions.size(); i++) {
					RestrictionI r = restrictions.get(i);
					String expresion = getHQLRestriction(r);
					if (expresion != null) {
						if (!allRes.equals("")) {
							allRes += " " + r.getRestrictionUnion() + " " + expresion;
						} else {
							allRes += expresion;
						}
					}
				}
				if (!allRes.equals("")) {
					hql += " and (" + allRes + ")";
				}
			}
			hql += " group by " + groupingField + " order by " + groupingField;
			
			Query query = em.createQuery(hql);
			query.setParameter("beginningTime", beginningTime);
			query.setParameter("endingTime", endingTime);
						
			List<Object[]> groupList = query.getResultList();
					
			GroupedStatistics gr = new GroupedStatistics();
			gr.setField(field);
			if (groupList != null && !groupList.isEmpty()) {

				LinkedHashMap<String, Long> values = new LinkedHashMap<String, Long>();
				if (field == StatisticsManager.SERVICE_FIELD_TYPE) {
					for (int i = 0; i < groupList.size(); i++) {
						Object[ ] res = groupList.get(i);
						values.put(String.valueOf(res[0]), (Long) res[1]);
					}
				} else {
					for (int i = 0; i < groupList.size(); i++) {
						Object[ ] res = groupList.get(i);
						values.put((String) res[0], (Long) res[1]);
					}
				}
				gr.setValues(values);

			}
			return gr;
		} catch (Exception e) {
			String msg = LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_064, new Object[ ] { new UtilsTime(beginningTime).toString(StatisticsManager.TIME_PATTERN), new UtilsTime(endingTime).toString(StatisticsManager.TIME_PATTERN) });
			LOGGER.error(msg, e);
			throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.UNKNOWN_ERROR, msg, e);
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}

	}

	/**
	 * Gets the field name associated to supplied field identifier.
	 * @param field	Field identifier.
	 * @return	Field name.
	 * @throws StandaloneStatisticPersistenceException	If the identifier is not valid.
	 */
	private String getGroupingField(int field) throws StandaloneStatisticPersistenceException {
		String groupingField = null;
		switch (field) {
			case StatisticsManager.SERVICE_FIELD_TYPE:
				groupingField = "g.service";
				break;
			case StatisticsManager.APPLICATION_FIELD_TYPE:
				groupingField = "g.application";
				break;
			case StatisticsManager.UO_FIELD_TYPE:
				groupingField = "g.uo";
				break;
			case StatisticsManager.RESULT_CODE_FIELD_TYPE:
				groupingField = "g.code";
				break;
			case StatisticsManager.SIGN_FIELD_TYPE:
				groupingField = "g.sign";
				break;
			default:
				throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.INVALID_INPUT_PARAMETERS, LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_065, new Object[ ] { String.valueOf(field) }));
		}
		return groupingField;
	}

	/**
	 * Gets the HQL expression associated to supplied restriction.
	 * @param r		Restriction.
	 * @return	HQL expression.
	 * @throws StandaloneStatisticPersistenceException	If the restriction is not valid.
	 */
	private String getHQLRestriction(RestrictionI r) throws StandaloneStatisticPersistenceException {
		String expresion = null;
		if (r != null && r.getFieldOperation() != null && r.getFieldValue() != null) {
			expresion = getGroupingField(r.getFieldType());
			if (r.getFieldOperation().equals(StatisticsManager.EQUAL_OPERATION)) {
				expresion += " = '" + r.getFieldValue() + "'";
			} else if (r.getFieldOperation().equals(StatisticsManager.DISTINCT_OPERATION)) {
				expresion += " <> '" + r.getFieldValue() + "'";
			}else if (r.getFieldOperation().equals(StatisticsManager.LIKE_OPERATION)) {
				if(r.getFieldType()== StatisticsManager.UO_FIELD_TYPE ||r.getFieldType()== StatisticsManager.APPLICATION_FIELD_TYPE){
					expresion += " like '" + r.getFieldValue() + "%'";
				}else{
					expresion += " = '" + r.getFieldValue() + "'";
				}
			}else {
				throw new StandaloneStatisticPersistenceException(StandaloneStatisticPersistenceException.INVALID_INPUT_PARAMETERS, LanguageStandalone.getFormatResStandaloneStatistics(LanguageKeys.AUD_066, new Object[ ] { r.getFieldOperation() }));
			}
			
		}
		return expresion;
	}

	
	
}
