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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.db.model.LogAlarm.java.</p>
 * <b>Description:</b><p>Class that represent a record of the "LOG_ALARM" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.persistence.malarm.db.model;


import java.util.Date;


/** 
 * <p>Class that represent a record of the "LOG_ALARM" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/10/2011.
 */
public class LogAlarm implements java.io.Serializable {

	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -8696275311703556091L;
	
	/**
	 * Attribute that represents the "LOG_PK" column. 
	 */
	private Long logPk;
	
	/**
	 * Attribute that represents the association with the "ALARM" table. 
	 */
	private Alarm alarm;
	
	/**
	 * Attribute that represents the value of the "CREATIONTIME" column. 
	 */
	private Date creationtime;
	/**
	 * Attribute that represents the value of the "ISLOCK" column. 
	 */
	private boolean islock;
	
	/**
	 * Attribute that represents the value of the "COMTIME" column. 
	 */
	private Date comtime;
	/**
	 * Attribute that represents the value of the "REPORTED" column. 
	 */
	private String reported ;
	/**
	 * Attribute that represents the value of the "MOREINFO" column. 
	 */
	private String moreInfo;

	/**
	 * Constructor method for the class LogAlarm.java. 
	 */
	public LogAlarm() {
	}

	
	/**
	 * Gets the value of the value of the "REPORTED" column.
	 * @return the value of the value of the "REPORTED" column.
	 */
	public String getReported() {
		return reported;
	}

	
	/**
	 * Sets the value of the value of the "REPORTED" column.
	 * @param emailReported The value for the value of the "REPORTED" column.
	 */
	public void setReported(String emailReported) {
		this.reported = emailReported;
	}

	/**
	 * Gets the value of the "LOG_PK" column.
	 * @return	The value of the "LOG_PK" column.
	 */
	public Long getLogPk() {
		return this.logPk;
	}

	/**
	 * Sets the value of the "LOG_PK" column.
	 * @param pk	Value of the "LOG_PK" column.
	 */
	public void setLogPk(Long pk) {
		this.logPk = pk;
	}

	/**
	 * Gets the association with the "ALARM" table.
	 * @return	The association with the "ALARM" table.
	 */
	public Alarm getAlarm() {
		return this.alarm;
	}

	/**
	 * Sets the association with the "ALARM" table. 
	 * @param alarmAss	Association with the "ALARM" table.
	 */
	public void setAlarm(Alarm alarmAss) {
		this.alarm = alarmAss;
	}

	/**
	 * Gets the value of the "CREATIONTIME" column.
	 * @return	Value of the "CREATIONTIME" column.
	 */
	public Date getCreationtime() {
		return this.creationtime;
	}

	/**
	 * Sets the value of the "CREATIONTIME" column.
	 * @param time	Value of the "CREATIONTIME" column.
	 */
	public void setCreationtime(Date time) {
		this.creationtime = time;
	}

	/**
	 * Gets the value of the "ISLOCK" column.
	 * @return	Value of the "ISLOCK" column.
	 */
	public boolean getIslock() {
		return this.islock;
	}

	/**
	 * Sets the value of the "ISLOCK" column.
	 * @param lock	Value of the "ISLOCK" column.
	 */
	public void setIslock(boolean lock) {
		this.islock = lock;
	}

	/**
	 * Gets the value of the "COMTIME" column.
	 * @return	Value of the "COMTIME" column.
	 */
	public Date getComtime() {
		return this.comtime;
	}

	/**
	 * Sets the value of the "COMTIME" column.
	 * @param time	Value of the "COMTIME" column.
	 */
	public void setComtime(Date time) {
		this.comtime = time;
	}

	
	/**
	 * Gets the value of the "MOREINFO" column.
	 * @return the value of the "MOREINFO" column.
	 */
	public String getMoreInfo() {
		return moreInfo;
	}

	
	/**
	 * Sets the value of the "MOREINFO" column.
	 * @param info The value for the "MOREINFO" column.
	 */
	public void setMoreInfo(String info) {
		this.moreInfo = info;
	}

	

}
