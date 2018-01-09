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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.db.model.Alarm.java.</p>
 * <b>Description:</b><p>Class that represent a record of the "ALARM" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.persistence.malarm.db.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/** 
 * <p>Class that represent a record of the "ALARM" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/10/2011.
 */
public class Alarm implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version. 
	 */
	private static final long serialVersionUID = -7551610237563403360L;
	
	/**
	 * Attribute that represents the value of "ALARM_ID" column. 
	 */
	private String alarmId;
	/**
	 * Attribute that represents the value of "SUBJECT" column. 
	 */
	private String subject;
	/**
	 * Attribute that represents the value of "MESSAGE" column. 
	 */
	private String message;
	/**
	 * Attribute that represents the value of "DESCRIPTION" column. 
	 */
	private String description;
	/**
	 * Attribute that represents the value of "ISLOCK" column. 
	 */
	private boolean islock;
	/**
	 * Attribute that represents the value of "STANDBYTIME" column. 
	 */
	private long standbytime;
	/**
	 * Attribute that represents the value of "LASTCOM" column. 
	 */
	private Date lastcom;
	/**
	 * Attribute that represents the association with the "LOG_ALARM" table. 
	 */
	private Set logAlarms = new HashSet(0);
	/**
	 * Attribute that represents the association with the "RECEIVERS" table. 
	 */
	private Set receiverses = new HashSet(0);

	/**
	 * Constructor method for the class Alarm.java. 
	 */
	public Alarm() {
	}



	/**
	 * Gets the value of "ALARM_ID" column.
	 * @return	The value of "ALARM_ID" column.
	 */
	public String getAlarmId() {
		return this.alarmId;
	}

	/**
	 * Sets the value of "ALARM_ID" column.
	 * @param id	Value of "ALARM_ID" column.
	 */
	public void setAlarmId(String id) {
		this.alarmId = id;
	}

	/**
	 * Gets the value of "SUBJECT" column.
	 * @return	Value of "SUBJECT" column.
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Sets the value of "SUBJECT" column.
	 * @param alarmSubject	Value of "SUBJECT" column.
	 */
	public void setSubject(String alarmSubject) {
		this.subject = alarmSubject;
	}

	/**
	 * Gets the value of "MESSAGE" column.
	 * @return The value of "MESSAGE" column.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the value of "MESSAGE" column.
	 * @param alarmMessage	Value of "MESSAGE" column.
	 */
	public void setMessage(String alarmMessage) {
		this.message = alarmMessage;
	}

	/**
	 * Gets the value of "ISLOCK" column.
	 * @return The value of "ISLOCK" column.
	 */
	public boolean getIslock() {
		return this.islock;
	}

	/**
	 * Sets the value of "ISLOCK" column.
	 * @param lock	The value of "ISLOCK" column.
	 */
	public void setIslock(boolean lock) {
		this.islock = lock;
	}

	/**
	 * Gets the value of "STANDBYTIME" column.
	 * @return	The value of "STANDBYTIME" column.
	 */
	public long getStandbytime() {
		return this.standbytime;
	}

	/**
	 * Sets the value of "STANDBYTIME" column.
	 * @param time	The value of "STANDBYTIME" column.
	 */
	public void setStandbytime(long time) {
		this.standbytime = time;
	}

	/**
	 * Gets the associations with the "LOGALARM" table.
	 * @return	The associations with the "LOGALARM" table.
	 */
	public Set getLogAlarms() {
		return this.logAlarms;
	}

	/**
	 * Sets the associations with the "LOGALARM" table.
	 * @param logAlarmAss	Associations with the "LOGALARM" table.
	 */
	public void setLogAlarms(Set logAlarmAss) {
		this.logAlarms = logAlarmAss;
	}

	/**
	 * Gets the associations with the "RECEIVERS" table.
	 * @return	The associations with the "RECEIVERS" table.
	 */
	public Set getReceiverses() {
		return this.receiverses;
	}

	/**
	 * Sets the associations with the "RECEIVERS" table.
	 * @param receiversesAss	Associations with the "RECEIVERS" table.
	 */
	public void setReceiverses(Set receiversesAss) {
		this.receiverses = receiversesAss;
	}

	
	/**
	 * Sets the value of the "LASCOM" column.
	 * @return	The value of the "LASCOM" column.
	 */
	public Date getLastcom() {
		return lastcom;
	}

	
	/**
	 * Sets the value of the "LASCOM" column.
	 * @param lastCommunication	The value of the "LASCOM" column.
	 */
	public void setLastcom(Date lastCommunication) {
		this.lastcom = lastCommunication;
	}

	
	/**
	 * Gets the value of the "DESCRIPTION" column.
	 * @return the value of the "DESCRIPTION" column.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the "DESCRIPTION" column.
	 * @param alarmDescription The value for the "DESCRIPTION" column.
	 */
	public void setDescription(String alarmDescription) {
		this.description = alarmDescription;
	}

}
