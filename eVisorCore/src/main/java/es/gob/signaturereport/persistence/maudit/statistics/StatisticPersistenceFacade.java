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
package es.gob.signaturereport.persistence.maudit.statistics;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.maudit.statistics.StatisticsManager;
import es.gob.signaturereport.maudit.statistics.item.GroupedStatistics;
import es.gob.signaturereport.maudit.statistics.item.RestrictionI;
import es.gob.signaturereport.maudit.statistics.item.TabularStatistics;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.statistics.model.bo.interfaz.IStatisticsBO;
import es.gob.signaturereport.tools.UtilsTime;

/**
 * <p>Persistence Facade Class for computing statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
@ManagedBean
public class StatisticPersistenceFacade {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StatisticPersistenceFacade.class);

	/**
	 * Attribute that represents a class instance.
	 */
	private static StatisticPersistenceFacade instance = null;
		
	/**
	 * Attribute that represents . 
	 */
	@Inject
	private IStatisticsBO statisticsBO;

	/**
	 * Constructor method for the class StatisticPersistenceFacade.java.
	 */
	private StatisticPersistenceFacade() {
	}

	/**
	 * Gets an instance of the class.
	 * @return	Instance of the class.
	 */
	public static StatisticPersistenceFacade getInstance() {
		if (instance == null) {
			instance = new StatisticPersistenceFacade();
		}
		return instance;
	}

	/**
	 * Compute the one-day usage statistics from stored audit information.
	 * @param stDate	Day in which you want to calculate statistics.
	 * @param update	If the previous values will be deleted.
	 * @param sendAlarm	If an error occurs then an alarm will be send.
	 * @throws StatisticPersistenceException	If an error occurs.
	 */
	public void computeStatistics(Date stDate, boolean update, boolean sendAlarm) throws StatisticPersistenceException {
		if (stDate == null) {
			throw new StatisticPersistenceException(StatisticPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_050));
		}
		String calTimeStr = new UtilsTime(stDate).toString(StatisticsManager.TIME_PATTERN);

		try {
			statisticsBO.computeStatistics(stDate, update, calTimeStr);
		} catch (Exception e) {
			String msg = Language.getFormatMessage(LanguageKeys.AUD_043, new Object[ ] { calTimeStr });
			LOGGER.error(msg, e);
			
			if (sendAlarm) {
				AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001, e.getMessage());
			}
			throw new StatisticPersistenceException(StatisticPersistenceException.UNKNOWN_ERROR, msg, e);
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
	 * @throws StatisticPersistenceException	If an error occurs.
	 */
	public TabularStatistics getTabulatedStatistics(Date beginningTime, Date endingTime, int rowField, int columnField, List<RestrictionI> restrictions) throws StatisticPersistenceException {

		if (beginningTime == null || endingTime == null) {
			throw new StatisticPersistenceException(StatisticPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_063));
		}
		String rowExp = getGroupingField(rowField);
		String columnExp = getGroupingField(columnField);
		String hql = "select " + rowExp + ", " + columnExp + ", sum (g.total) from GroupingsPOJO g, StatisticTimesPOJO t where g.stTime = t.stTimePk" + " and t.stDate >= :beginningTime and t.stDate <= :endingTime and " + rowExp + " is not null and " + columnExp + " is not null";
		
		try {
			
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
			List<Object[ ]> resultList = statisticsBO.getStatistics(hql, beginningTime, endingTime);
			TabularStatistics st = new TabularStatistics(rowField, columnField);
			if(resultList!=null && !resultList.isEmpty()){
				for(int i=0;i<resultList.size();i++){
					Object[] result = resultList.get(i);
					st.addValue(result[0].toString(), result[1].toString(), (Long)result[2]);
				}
			}
			return st;
		} catch (DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AUD_064, new Object[ ] { new UtilsTime(beginningTime).toString(StatisticsManager.TIME_PATTERN), new UtilsTime(endingTime).toString(StatisticsManager.TIME_PATTERN) });
			LOGGER.error(msg, e);
			throw new StatisticPersistenceException(StatisticPersistenceException.UNKNOWN_ERROR, msg,e);
		}
	}

	/**
	 * Method that calculates the grouping statistics by supplied field.
	 * @param beginningTime		Beginning time.
	 * @param endingTime		Ending time.
	 * @param field				Grouping field.
	 * @param restrictions		List of restrictions to add in the query.
	 * @return					{@link GroupedStatistics} object that contains the results.
	 * @throws StatisticPersistenceException	If an error occurs.
	 */
	public GroupedStatistics getGroupedStatistics(Date beginningTime, Date endingTime, int field, List<RestrictionI> restrictions) throws StatisticPersistenceException {
		
		if (beginningTime == null || endingTime == null) {
			throw new StatisticPersistenceException(StatisticPersistenceException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_063));
		}
		String groupingField = getGroupingField(field);
		String hql = "select " + groupingField + ", sum (g.total) from GroupingsPOJO g, StatisticTimesPOJO t where g.stTime = t.stTimePk" + " and t.stDate >= :beginningTime and t.stDate <= :endingTime and " + groupingField + " is not null";
		
		try {
			
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
						
			List<Object[]> groupList = statisticsBO.getStatistics(hql, beginningTime, endingTime);
					
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
		} catch (DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.AUD_064, new Object[ ] { new UtilsTime(beginningTime).toString(StatisticsManager.TIME_PATTERN), new UtilsTime(endingTime).toString(StatisticsManager.TIME_PATTERN) });
			LOGGER.error(msg, e);
			throw new StatisticPersistenceException(StatisticPersistenceException.UNKNOWN_ERROR, msg, e);
		}

	}

	/**
	 * Gets the field name associated to supplied field identifier.
	 * @param field	Field identifier.
	 * @return	Field name.
	 * @throws StatisticPersistenceException	If the identifier is not valid.
	 */
	private String getGroupingField(int field) throws StatisticPersistenceException {
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
				throw new StatisticPersistenceException(StatisticPersistenceException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AUD_065, new Object[ ] { String.valueOf(field) }));
		}
		return groupingField;
	}

	/**
	 * Gets the HQL expression associated to supplied restriction.
	 * @param r		Restriction.
	 * @return	HQL expression.
	 * @throws StatisticPersistenceException	If the restriction is not valid.
	 */
	private String getHQLRestriction(RestrictionI r) throws StatisticPersistenceException {
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
				throw new StatisticPersistenceException(StatisticPersistenceException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.AUD_066, new Object[ ] { r.getFieldOperation() }));
			}
			
		}
		return expresion;
	}

	
	
}
