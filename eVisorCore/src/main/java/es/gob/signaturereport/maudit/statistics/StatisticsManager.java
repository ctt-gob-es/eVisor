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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.StatisticsManager.java.</p>
 * <b>Description:</b><p> Manager for consulting services usage statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.maudit.statistics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.statistics.item.GroupedStatistics;
import es.gob.signaturereport.maudit.statistics.item.RestrictionI;
import es.gob.signaturereport.maudit.statistics.item.TabularStatistics;
import es.gob.signaturereport.persistence.maudit.statistics.StatisticPersistenceException;
import es.gob.signaturereport.persistence.maudit.statistics.StatisticPersistenceFacade;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.UtilsTime;


/** 
 * <p>Manager for consulting services usage statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
@Singleton
@ManagedBean
public final class StatisticsManager implements Serializable {
	
	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -1488593773805419213L;

	// Operaciones
	/**
	 * Constant that represents an equal operation.
	 */
	public static final String EQUAL_OPERATION = "=";

	/**
	 * Constant that represents a distinct operation.
	 */
	public static final String DISTINCT_OPERATION = "!=";
	
	/**
	 * Constant that represents a like operation.
	 */
	public static final String LIKE_OPERATION = "like";

	// Uniones

	/**
	 * Constant that represents the "AND" union operator.
	 */
	public static final String AND_UNION = "and";

	/**
	 * Constant that represents the "OR" union operator.
	 */
	public static final String OR_UNION = "or";

	// Campos de filtrado

	/**
	 * Constant that represents the filtering field 'Service identifier'.
	 */
	public static final int SERVICE_FIELD_TYPE = 0;

	/**
	 * Constant that represents the filtering field 'Application'.
	 */
	public static final int APPLICATION_FIELD_TYPE = 1;

	/**
	 * Constant that represents the filtering field 'Organization unit'.
	 */
	public static final int UO_FIELD_TYPE = 2;
	/**
	 * Constant that represents the filtering field 'Sign report'.
	 */
	public static final int SIGN_FIELD_TYPE = 3;

	/**
	 * Constant that represents the filtering field 'Result code'.
	 */
	public static final int RESULT_CODE_FIELD_TYPE = 4;
	
	/**
	 * Constant that represents the time pattern. 
	 */
	public static final String TIME_PATTERN = UtilsTime.FORMATO_FECHA_CORTO;
	
	/**
	 * Default active status of the 'calculate statistics' scheduler. 
	 */
	private static final boolean DEFAULT_TASK_ACTIVE_VALUE = false;
	
	/**
	 * Default update value mode of the 'calculate statistics' scheduler. 
	 */
	private static final boolean DEFAULT_TASK_UPDATE_VALUE = false;

	/**
	 * Constants that represents the key property that contains the update value mode of the 'calculate statistics' scheduler. 
	 */
	private static final String IS_UPDATE_TASK = "signaturereport.audit.statistics.task.update";

	/**
	 * Constants that represents the key property that contains the update value mode of the 'calculate statistics' scheduler. 
	 */
	private static final String IS_TASK_ACTIVE = "signaturereport.audit.statistics.task.active"; 
	
	/**
	 * Constants that represents the key property that contains the beginning time of the 'calculate statistics' scheduler. 
	 */
	private static final String TASK_TIME = "signaturereport.audit.statistics.task.time"; 
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StatisticsManager.class);

	/**
	 * Constant that represents the '3' number. 
	 */
	private static final int NUMBER_III = 3;
	
	/**
	 * Constant that represents the '24' number. 
	 */
	private static final int NUMBER_XXIV = 24;
	
	/**
	 * Constant that represents the '60' number. 
	 */
	private static final int NUMBER_LX = 60;
	/**
	 * Attribute that represents a class instance. 
	 */
	private static StatisticsManager instance = null;
	
	/**
     * Attribute that represents the alarms and summaries events scheme manager BOs.
     */
    @Inject
    private StatisticPersistenceFacade statisticsPersistenceFacade;
	
	
	/**
	 * Constructor method for the class StatisticsManager.java. 
	 */
//	private StatisticsManager() {
//		boolean active = DEFAULT_TASK_ACTIVE_VALUE;
//		String activeTask = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, IS_TASK_ACTIVE);
//		if(activeTask != null){
//			active = Boolean.parseBoolean(activeTask.trim());		
//		}else{
//			LOGGER.warn(Language.getFormatMessage(LanguageKeys.AUD_053, new Object[]{DEFAULT_TASK_ACTIVE_VALUE}));
//		}
//		if(active){
//			String updateValuesStr = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, IS_UPDATE_TASK);
//			boolean updateValue = DEFAULT_TASK_UPDATE_VALUE;
//			if(updateValuesStr!=null){
//				updateValue = Boolean.parseBoolean(updateValuesStr);
//			}
//			String timeStr = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, TASK_TIME);
//			int[] timeTokens = validateTaskTime(timeStr);
//			if(timeTokens==null){
//				LOGGER.error(Language.getMessage(LanguageKeys.AUD_054));
//			}else{
//				StatisticComputerThread computerThread = new StatisticComputerThread(timeTokens[0],timeTokens[1], timeTokens[2], updateValue);
//				computerThread.start();
//			}
//		}
//	}

	/**
	 * Gets an instance of the class.
	 * @return	A {@link StatisticsManager} instance.
	 */
	public static StatisticsManager getInstance(){
		if(instance==null){
			instance = new StatisticsManager();
		}
		return instance;
	}
	
	 /**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {
    	instance = this;
    	
    	boolean active = DEFAULT_TASK_ACTIVE_VALUE;
		//String activeTask = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, IS_TASK_ACTIVE);
    	String activeTask = StaticSignatureReportProperties.getProperty(IS_TASK_ACTIVE);
		if(activeTask != null){
			active = Boolean.parseBoolean(activeTask.trim());		
		}else{
			LOGGER.warn(Language.getFormatMessage(LanguageKeys.AUD_053, new Object[]{DEFAULT_TASK_ACTIVE_VALUE}));
		}
		if(active){
			//String updateValuesStr = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, IS_UPDATE_TASK);
			String updateValuesStr = StaticSignatureReportProperties.getProperty(IS_UPDATE_TASK);
			boolean updateValue = DEFAULT_TASK_UPDATE_VALUE;
			if(updateValuesStr!=null){
				updateValue = Boolean.parseBoolean(updateValuesStr);
			}
			//String timeStr = SignatureReportProperties.getInstance().getPropertyValue(SignatureReportPropertiesI.AUDIT_SECTION_ID, TASK_TIME);
			String timeStr = StaticSignatureReportProperties.getProperty(TASK_TIME);
			int[] timeTokens = validateTaskTime(timeStr);
			if(timeTokens==null){
				LOGGER.error(Language.getMessage(LanguageKeys.AUD_054));
			}else{
				StatisticComputerThread computerThread = new StatisticComputerThread(timeTokens[0],timeTokens[1], timeTokens[2], updateValue);
				computerThread.start();
			}
		}
    }

    /**
     * Method that destroys the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
    	instance = null;
    }
	
	/**
	 * Method that calculates the grouping statistics by supplied field.
	 * @param beginningTime		Beginning time.
	 * @param endingTime		Ending time.
	 * @param field				Grouping field.
	 * @param restrictions		List of restrictions to add in the query.
	 * @return					{@link GroupedStatistics} object that contains the results.
	 * @throws StatisticsException	If an error occurs.
	 */
	public GroupedStatistics getGroupedStatistics(Date beginningTime, Date endingTime, int field, List<RestrictionI> restrictions) throws StatisticsException {
		try {
			return statisticsPersistenceFacade.getGroupedStatistics(beginningTime, endingTime, field, restrictions);
		} catch (StatisticPersistenceException e) {
			LOGGER.error(e.getDescription());
			if(e.getCode()==StatisticPersistenceException.INVALID_INPUT_PARAMETERS){
				throw new StatisticsException(StatisticsException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else{
				throw new StatisticsException(StatisticsException.UNKNOWN_ERROR, e.getDescription(),e);
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
	 * @throws StatisticsException	If an error occurs.
	 */
	public TabularStatistics getTabulatedStatistics(Date beginningTime, Date endingTime, int rowField, int columnField, List<RestrictionI> restrictions) throws StatisticsException {
		try {
			return statisticsPersistenceFacade.getTabulatedStatistics(beginningTime, endingTime, rowField, columnField, restrictions);
		} catch (StatisticPersistenceException e) {
			LOGGER.error(e.getDescription());
			if(e.getCode()==StatisticPersistenceException.INVALID_INPUT_PARAMETERS){
				throw new StatisticsException(StatisticsException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else{
				throw new StatisticsException(StatisticsException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
	}
	
	/**
	 * Compute the one-day usage statistics from stored audit information.
	 * @param computeDate	Day in which you want to calculate statistics.
	 * @param update		If the previous values will be deleted.
	 * @throws StatisticsException	If an error occurs.
	 */
	public void computeStatistics(Date computeDate, boolean update) throws StatisticsException{
		if(computeDate==null){
			throw new StatisticsException(StatisticsException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.AUD_050));
		}else{
			UtilsTime utils = new UtilsTime(computeDate);
			try {
				statisticsPersistenceFacade.computeStatistics(computeDate, update,true);
			} catch (StatisticPersistenceException e) {
				String msg = Language.getFormatMessage(LanguageKeys.AUD_059, new Object[]{utils.toString(TIME_PATTERN)});
				LOGGER.error(msg+e.getMessage());
				throw new StatisticsException(StatisticsException.UNKNOWN_ERROR, msg,e);
			}
		}
	}
	
	/**
	 * Extracts hours, minutes and seconds from the supplied string.
	 * @param time	String time. The allowed value are:
	 * <br/> HH:mm:ss where 'HH'(0 to 23), 'mm' (0 to 59) and 'ss' (0 to 59).
	 * @return	Array of the integer [HH,mm,ss] if the format is valid. Otherwise null.
	 */
	private int[] validateTaskTime(String time){
		if(time!=null){
			String[] tokens = time.split(":");
			if(tokens.length==NUMBER_III){
				try{
					int h = Integer.parseInt(tokens[0]);
					if(h>=0 && h<NUMBER_XXIV){
						int m = Integer.parseInt(tokens[1]);
						if(m>=0 && m<NUMBER_LX){
							int s = Integer.parseInt(tokens[2]);
							if(s>=0 && s<NUMBER_LX){
								return new int[]{h,m,s};
							}
						}
					}
				}catch(NumberFormatException nfe){
					return null;
				}
			}
		}	
		return null;
	}
	
}
