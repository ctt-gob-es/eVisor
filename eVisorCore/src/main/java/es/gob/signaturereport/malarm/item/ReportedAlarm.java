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
 * <b>File:</b><p>es.gob.signaturereport.malarm.item.ReportedAlarm.java.</p>
 * <b>Description:</b><p>Class that contains information about a reported alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/07/2011.
 */
package es.gob.signaturereport.malarm.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


/** 
 * <p>Class that contains information about a reported alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/07/2011.
 */
public class ReportedAlarm {
	
	/**
	 * Attribute that represents the alarm identifier. 
	 */
	private String alarmId = null; 

	/**
	 * Attribute that represents the log identifiers. 
	 */
	private Long[] logIdentifiers = null;
	
	/**
	 * Attribute that represents the communication time. 
	 */
	private Date communicationTime = null;

	/**
	 * Constructor method for the class ReportedAlarm.java.
	 * @param id	Alarm identifier.
	 * @param logIds	Log identifier list.
	 * @param time Communication time.
	 */
	public ReportedAlarm(String id, Long[ ] logIds, Date time) {
		super();
		this.alarmId = id;
		if(logIds!=null){
			this.logIdentifiers = logIds.clone();
		}
		
		this.communicationTime = time;
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
	 * Gets the value of the log identifiers.
	 * @return the value of the log identifiers.
	 */
	public Long[ ] getLogIdentifiers() {
		return logIdentifiers;
	}

	
	/**
	 * Sets the value of the log identifiers.
	 * @param logIds The value for the log identifiers.
	 */
	public void setLogIdentifiers(Long[ ] logIds) {
		if(logIds!=null){
			this.logIdentifiers = logIds.clone();
		}
	}

	
	/**
	 * Gets the value of the communication time.
	 * @return the value of the communication time.
	 */
	public Date getCommunicationTime() {
		return communicationTime;
	}

	
	/**
	 * Sets the value of the communication time.
	 * @param time The value for the communication time.
	 */
	public void setCommunicationTime(Date time) {
		this.communicationTime = time;
	}
	
}
