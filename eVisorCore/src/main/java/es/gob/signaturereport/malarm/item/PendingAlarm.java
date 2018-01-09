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
 * <b>File:</b><p>es.gob.signaturereport.malarm.item.PendingAlarm.java.</p>
 * <b>Description:</b><p>Class that represents an alarm pending communication.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/07/2011.
 */
package es.gob.signaturereport.malarm.item;

import java.util.Date;


/** 
 * <p>Class that represents an alarm pending communication.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/07/2011.
 */
public class PendingAlarm {
	
	/**
	 * Attribute that represents the alarm identifier. 
	 */
	private String alarmId = null; 
	
	/**
	 * Attribute that represents the log identifier. 
	 */
	private long logId = 0;
	
	/**
	 * Attribute that represents the creation time of alarm. 
	 */
	private Date creationTime = null;
	
	/**
	 * Attribute that represents additional information associated to the alarm. 
	 */
	private String moreInformation = null;

	/**
	 * Constructor method for the class PendingAlarm.java. 
	 */
	public PendingAlarm() {
	}

	/**
	 * Constructor method for the class PendingAlarm.java.
	 * @param id	Alarm identifier.
	 * @param logIdentifier		Log identifier.
	 * @param time 	Creation time.
	 */
	public PendingAlarm(String id, long logIdentifier, Date time) {
		super();
		this.alarmId = id;
		this.logId = logIdentifier;
		this.creationTime = time;
	}

	/**
	 * Constructor method for the class PendingAlarm.java.
	 * @param id	Alarm identifier.
	 * @param logIdentifier		Log identifier.
	 * @param time 	Creation time.
	 * @param addInformation Additional information. 
	 */
	public PendingAlarm(String id, long logIdentifier, Date time, String addInformation) {
		super();
		this.alarmId = id;
		this.logId = logIdentifier;
		this.creationTime = time;
		this.moreInformation = addInformation;
	}

	
	/**
	 * Gets the value of alarm identifier.
	 * @return the value of alarm identifier.
	 */
	public String getAlarmId() {
		return alarmId;
	}

	
	/**
	 * Sets the value of alarm identifier.
	 * @param id The value for alarm identifier.
	 */
	public void setAlarmId(String id) {
		this.alarmId = id;
	}

	
	/**
	 * Gets the value of log identifier.
	 * @return the value of log identifier.
	 */
	public long getLogId() {
		return logId;
	}

	
	/**
	 * Sets the value of log identifier.
	 * @param logIdentifier The value for log identifier.
	 */
	public void setLogId(long logIdentifier) {
		this.logId = logIdentifier;
	}

	
	/**
	 * Gets the value of creation time.
	 * @return the value of creation time.
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	
	/**
	 * Sets the value of creation time.
	 * @param time The value for creation time.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}

	
	/**
	 * Gets the value of additional information.
	 * @return the value of additional information.
	 */
	public String getMoreInformation() {
		return moreInformation;
	}

	
	/**
	 * Sets the value of additional information.
	 * @param addInformation The value for additional information.
	 */
	public void setMoreInformation(String addInformation) {
		this.moreInformation = addInformation;
	}
}
