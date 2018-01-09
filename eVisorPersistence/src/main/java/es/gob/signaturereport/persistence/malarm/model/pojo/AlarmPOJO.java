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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.Alarm.java.</p>
 * <b>Description:</b><p> Class that represents a system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 18/05/2011.
 */
package es.gob.signaturereport.persistence.malarm.model.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import es.gob.signaturereport.persistence.utils.Constants;
import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the ALARM table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
@Entity
@Table(name = "ALARM")
@NamedQueries({
	  @NamedQuery(name="findAlarmById",
	              query="SELECT al FROM AlarmPOJO al WHERE al.alarmId = :alarmId"),
	  @NamedQuery(name="findAllAlarms",
	              query="SELECT al FROM AlarmPOJO al"),
	  @NamedQuery(name="setLastCommunication",
	  			  query="UPDATE AlarmPOJO SET lastcom = :lastCom WHERE alarmId LIKE :alarmId")
})
public class AlarmPOJO implements Serializable {

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -5357072984269730654L;

	/**
	 * Attribute that represents the alarm identifier primary key. 
	 */
	@Id
    @Column(name = "ALARM_ID", unique = true, nullable = false, length = NumberConstants.INT_50)
	private String alarmId;

	/**
	 * Attribute that represents the alarm description. 
	 */
	@Column(name = "DESCRIPTION", length = NumberConstants.INT_50)
	private String description;

	/**
	 * Attribute that represents the alarm subject. 
	 */
	@Column(name = "SUBJECT", length = NumberConstants.INT_50)
	private String subject;

	/**
	 * Attribute that represents the alarm message. 
	 */
	@Column(name = "MESSAGE")
	private String message;

	/**
	 * Attribute that represents the alarm lock. 
	 */
	@Column(name = "ISLOCK")
	@Type(type = Constants.CONS_YES_NO)
	private boolean isLock;

	/**
	 * Attribute that represents the alarm standby time 
	 */
	@Column(name = "STANDBYTIME", precision =  NumberConstants.INT_10, scale = NumberConstants.INT_0, nullable = false)
	private Long standbytime;

	/**
     * Attribute that represents the alarm last communication.
     */
    @Column(name = "LASTCOM")
    private Date lastcom;

    /**
	 * Attribute that represents the association to applications contained into this organizational unit. 
	 */
    @OneToMany(mappedBy = "alarm" , fetch = FetchType.EAGER)
	private Set<LogAlarmPOJO> logAlarms = new HashSet<LogAlarmPOJO>(0);

    /**
     * 
     */
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ALARM_REC", joinColumns = { @JoinColumn(name = "ALARM", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "REC", nullable = false) })
    private Set<ReceiversPOJO> receiverses = new HashSet<ReceiversPOJO>(0);


	/**
	 * 
	 * @return
	 */
	public String getAlarmId() {
		return alarmId;
	}


	/**
	 * 
	 * @param alarmId
	 */
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}


	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 
	 * @return
	 */
	public boolean isLock() {
		return isLock;
	}


	/**
	 * 
	 * @param isLock
	 */
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}


	/**
	 * 
	 * @return
	 */
	public Long getStandbytime() {
		return standbytime;
	}


	/**
	 * 
	 * @param standbytime
	 */
	public void setStandbytime(Long standbytime) {
		this.standbytime = standbytime;
	}


	/**
	 * 
	 * @return
	 */
	public Date getLastcom() {
		return lastcom;
	}


	/**
	 * 
	 * @param lastcom
	 */
	public void setLastcom(Date lastcom) {
		this.lastcom = lastcom;
	}


	/**
	 * 
	 * @return
	 */
	public Set<LogAlarmPOJO> getLogAlarms() {
		return logAlarms;
	}


	/**
	 * 
	 * @param logAlarms
	 */
	public void setLogAlarms(Set<LogAlarmPOJO> logAlarms) {
		this.logAlarms = logAlarms;
	}

	/**
	 * 
	 * @return
	 */
	public Set<ReceiversPOJO> getReceiverses() {
		return receiverses;
	}

	/**
	 * 
	 * @param receiverses
	 */
	public void setReceiverses(Set<ReceiversPOJO> receiverses) {
		this.receiverses = receiverses;
	}

}
