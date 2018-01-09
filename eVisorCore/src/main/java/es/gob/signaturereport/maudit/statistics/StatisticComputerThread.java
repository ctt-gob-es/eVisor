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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.StatisticComputerThread.java.</p>
 * <b>Description:</b><p>Class that represents a thread for computing statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>12/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 12/08/2011.
 */
package es.gob.signaturereport.maudit.statistics;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.EventCollector;
import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Class that represents a thread for computing statistics.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 12/08/2011.
 */
public class StatisticComputerThread extends Thread {

	/**
	 * Attribute that represents the start hour of this task. 
	 */
	private int hours = 0;

	/**
	 * Attribute that represents the start minute of this task. 
	 */
	private int minutes = 0;

	/**
	 * Attribute that represents the start seconds of this task. 
	 */
	private int seconds = 0;

	/**
	 * Attribute that represents a flag that indicates if the current values will be updated. 
	 */
	private boolean update = false;

	/**
	 * Attribute that represents a flag that indicates if this task must be stopped. 
	 */
	private boolean continueExecution = true;
	/**
	 * Attribute that represents the number of milliseconds that contains a day. 
	 */
	private static final long DAY_IN_MILLIS = 86400000;
	/**
	 * Attribute that represents the number of milliseconds that the task will be asleep while the current audit transactions will be registered. 
	 */
	private static final long FLUSH_TIME =600000;
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StatisticComputerThread.class);

	/**
	 * Constructor method for the class StatisticComputerThread.java.
	 * @param h		The start hours of this task.
	 * @param m	The start minutes of this task.
	 * @param s	The start seconds of this task.
	 * @param updateValues 	Indicates if the current values will be updated. 
	 */
	public StatisticComputerThread(int h, int m, int s, boolean updateValues) {
		super();
		this.hours = h;
		this.minutes = m;
		this.seconds = s;
		this.update = updateValues;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			sleep(getTimeToSleep());
			computeStatistics();
		} catch (InterruptedException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.AUD_058), e);
		}
	}

	/**
	 * Compute statistics.
	 * @throws InterruptedException	If an error occurs.
	 */
	private void computeStatistics() throws InterruptedException {
		while (continueExecution) {
			long time = System.currentTimeMillis() - DAY_IN_MILLIS;
			Date computeDate = new Date(time);
			UtilsTime utils = new UtilsTime(computeDate);
			LOGGER.info(Language.getFormatMessage(LanguageKeys.AUD_056, new Object[ ] { utils.toString(StatisticsManager.TIME_PATTERN) }));
			// Guardamos las transacciones pendiente de procesar por si existier�n del dña anterior
			EventCollector.getInstance().flush();
			sleep(FLUSH_TIME);
			//
			try {
				StatisticsManager.getInstance().computeStatistics(computeDate, update);
			} catch (StatisticsException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.AUD_055), e);
			}
			sleep(getTimeToSleep());
			LOGGER.info(Language.getMessage(LanguageKeys.AUD_057));
		}
	}

	/**
	 * Gets if this task must be stopped. 
	 * @return if this task must be stopped.
	 */
	public boolean isContinueExecution() {
		return continueExecution;
	}

	/**
	 * Sets if this task must be stopped. .
	 * @param continueRun if this task must be stopped. .
	 */
	public void setContinueExecution(boolean continueRun) {
		this.continueExecution = continueRun;
	}

	/**
	 * Gets the milliseconds that this task will be sleep.
	 * @return	Milliseconds.
	 */
	private long getTimeToSleep() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		long currentTime = System.currentTimeMillis();
		long executionTime = cal.getTimeInMillis();
		if (executionTime > currentTime) {
			return (executionTime - currentTime);
		} else {
			return ((executionTime + DAY_IN_MILLIS) - currentTime);
		}
	}
}
