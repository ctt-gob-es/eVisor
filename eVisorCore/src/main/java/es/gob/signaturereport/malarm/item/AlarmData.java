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
 * <b>File:</b><p>es.gob.signaturereport.malarm.item.AlarmData.java.</p>
 * <b>Description:</b><p>Class that represent an system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm.item;

import java.util.Date;


/** 
 * <p>Class that represent an system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
public class AlarmData {
	
	/**
	 * Attribute that represents the alarm identifier. 
	 */
	private String alarmId = null;
	
	/**
	 * Attribute that represents the description of the alarm. 
	 */
	private String description = null;
	
	/**
	 * Attribute that represents mail subject associated to alarm. 
	 */
	private String subject = null;
	
	/**
	 * Attribute that represents mail message associated to alarm. 
	 */
	private String message = null;
	
	/**
	 * Attribute that indicates if the alarm is locked. 
	 */
	private boolean lock = false;
	
	/**
	 * Attribute that represents the time which the alarm is standby. Is specified in minutes. 
	 */
	private long standbyTime = 0;
	
	/**
	 * Attribute that represents the time which this alarm was communicated. 
	 */
	private Date lastCommunication = null;

	/**
	 * Attribute that represents the receivers of the alarm. 
	 */
	private String[] receivers = null;
	
	/**
	 * Constructor method for the class AlarmData.java. 
	 */
	public AlarmData() {
	}
	
	/**
	 * Gets the value of the receivers of the alarm.
	 * @return the value of the receivers of the alarm.
	 */
	public String[ ] getReceivers() {
		return receivers;
	}


	
	/**
	 * Sets the value of the receivers of the alarm.
	 * @param recs The value for the receivers of the alarm.
	 */
	public void setReceivers(String[ ] recs) {
		this.receivers = recs;
	}


	

	
	/**
	 * Gets the value of the alarm identifier.
	 * @return the value of the alarm identifier.
	 */
	public String getAlarmId() {
		return alarmId;
	}

	
	/**
	 * Sets the value of the alarm identifier.
	 * @param id The value for the alarm identifier.
	 */
	public void setAlarmId(String id) {
		this.alarmId = id;
	}

	
	/**
	 * Gets the value of the mail subject associated to the alarm.
	 * @return the value of the mail subject associated to the alarm.
	 */
	public String getSubject() {
		return subject;
	}

	
	/**
	 * Sets the value of the mail subject associated to the alarm.
	 * @param subj The value for the mail subject associated to the alarm.
	 */
	public void setSubject(String subj) {
		this.subject = subj;
	}

	
	/**
	 * Gets the value of the mail message associated to the alarm.
	 * @return the value of the mail message associated to the alarm.
	 */
	public String getMessage() {
		return message;
	}

	
	/**
	 * Sets the value of the mail message associated to the alarm.
	 * @param msg The value for the mail message associated to the alarm.
	 */
	public void setMessage(String msg) {
		this.message = msg;
	}

	
	/**
	 * Gets if the alarm is locked.
	 * @return if the alarm is locked.
	 */
	public boolean isLock() {
		return lock;
	}

	
	/**
	 * Sets if the alarm is locked.
	 * @param lockValue True if the alarm is locked. Otherwise false.
	 */
	public void setLock(boolean lockValue) {
		this.lock = lockValue;
	}

	
	/**
	 * Gets the value of the time which the alarm is standby.
	 * @return the value of the time which the alarm is standby.
	 */
	public long getStandbyTime() {
		return standbyTime;
	}

	
	/**
	 * Sets the value of the time which the alarm is standby.
	 * @param time The value for the time which the alarm is standby.
	 */
	public void setStandbyTime(long time) {
		this.standbyTime = time;
	}


	
	/**
	 * Gets the value of the time which this alarm was communicated.
	 * @return the value of the time which this alarm was communicated.
	 */
	public Date getLastCommunication() {
		return lastCommunication;
	}


	
	/**
	 * Sets the value of the time which this alarm was communicated.
	 * @param lastComm The value for the time which this alarm was communicated.
	 */
	public void setLastCommunication(Date lastComm) {
		this.lastCommunication = lastComm;
	}

	
	/**
	 * Gets the value of the alarm description.
	 * @return the value of the alarm description.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the alarm description.
	 * @param des The value for the alarm description.
	 */
	public void setDescription(String des) {
		this.description = des;
	}
	
	/**
	 * Retruns new copy instance.
	 * @return a new instance of alarm data.
	 */
	public AlarmData newInstance() {
		AlarmData aux = new AlarmData();
		aux.setAlarmId(this.getAlarmId());
		aux.setDescription(this.getDescription());
		aux.setLastCommunication(this.getLastCommunication());
		aux.setLock(this.isLock());
		aux.setMessage(this.getMessage());
		aux.setReceivers(this.getReceivers());
		aux.setStandbyTime(this.getStandbyTime());
		aux.setSubject(this.getSubject());
		return aux;
	}
	
	
	
}
