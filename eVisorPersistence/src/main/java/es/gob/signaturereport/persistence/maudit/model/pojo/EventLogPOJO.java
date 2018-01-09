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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.EventLog.java.</p>
 * <b>Description:</b><p>Class that represent a record of the 'EVENTLOGS' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/07/2011.
 */

package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <p>Class that represent a record of the 'EVENTLOGS' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/07/2011.
 */
@Entity
@Table(name = "EVENTLOGS")
public class EventLogPOJO implements Serializable {

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = 4106183576452108289L;

	/**
	 * Attribute that represents the value of EVENTLOG_PK column.. 
	 */
	@Id
    @Column(name = "EVENTLOG_PK", unique = true, nullable = false, length = NumberConstants.INT_50)
	private Long eventLogPK = null;
	
	/**
	 * Attribute that represents the value of LOG_CONTENT column. 
	 */
	@Column(name = "LOG_CONTENT")
	@Lob
	private byte[] content = null;
	
	/**
	 * Attribute that represents the value of STORE_TYPE column. 
	 */
	@Column(name = "STORE_TYPE")
	private String storeType = null;
	
	/**
	 * Attribute that represents the value of CREATIONTIME column. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime = null;
	
	/**
	 * Attribute that represents the value of STORETIME column. 
	 */
	@Column(name = "STORETIME")
	private Date storeTime = null;
		
	/**
	 * Gets the value of the 'EVENTLOG_PK' column value.
	 * @return the value of the 'EVENTLOG_PK' column value.
	 */
	public Long getEventLogPK() {
		return eventLogPK;
	}

	
	/**
	 * Sets the value of the 'EVENTLOG_PK' column value.
	 * @param pk The value for the 'EVENTLOG_PK' column value.
	 */
	public void setEventLogPK(Long pk) {
		this.eventLogPK = pk;
	}

	
	/**
	 * Gets the value of the 'LOG_CONTENT' column value.
	 * @return the value of the 'LOG_CONTENT' column value.
	 */
	public byte[] getContent() {
		return content;
	}

	
	/**
	 * Sets the value of the 'LOG_CONTENT' column value.
	 * @param logContent The value for the 'LOG_CONTENT' column value.
	 */
	public void setContent(byte[] logContent) {
		this.content = logContent;
	}

	
	/**
	 * Gets the value of the 'STORE_TYPE' column value.
	 * @return the value of the 'STORE_TYPE' column value.
	 */
	public String getStoreType() {
		return storeType;
	}

	
	/**
	 * Sets the value of the 'STORE_TYPE' column value.
	 * @param type The value for the 'STORE_TYPE' column value.
	 */
	public void setStoreType(String type) {
		this.storeType = type;
	}

	
	/**
	 * Gets the value of the 'CREATIONTIME' column value.
	 * @return the value of the 'CREATIONTIME' column value.
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	
	/**
	 * Sets the value of the 'CREATIONTIME' column value.
	 * @param time The value for the 'CREATIONTIME' column value.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}

	
	/**
	 * Gets the value of the 'STORETIME' column value.
	 * @return the value of the 'STORETIME' column value.
	 */
	public Date getStoreTime() {
		return storeTime;
	}

	
	/**
	 * Sets the value of the 'STORETIME' column value.
	 * @param time The value for the 'STORETIME' column value.
	 */
	public void setStoreTime(Date time) {
		this.storeTime = time;
	}
}
