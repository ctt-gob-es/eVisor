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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.db.model.Receivers.java.</p>
 * <b>Description:</b><p>Class that represent a record of the "RECEIVERS" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.persistence.malarm.model.pojo;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <p>Class that represent a record of the "RECEIVERS" table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Entity
@Table(name = "RECEIVERS")
public class ReceiversPOJO implements java.io.Serializable {

	/**
	 * Attribute that represents the class serial version. 
	 */
	private static final long serialVersionUID = -6841518219762366592L;

	/**
	 * Attribute that represents the value of the "REC_PK" column. 
	 */
	@Id
	@GeneratedValue(generator = "SQ_RECEIVERS_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SQ_RECEIVERS_GEN", sequenceName = "SQ_RECEIVERS",allocationSize=0)
	@Column(name = "REC_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long recPk;

	/**
	 * Attribute that represents the value of the "MAIL" column. 
	 */
	@Column(name = "MAIL")
	private String mail;

	/**
	 * Attribute that represents the association with the "ALARM" table. 
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ALARM_REC", joinColumns = { @JoinColumn(name = "REC", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ALARM", nullable = false) })
	private Set<AlarmPOJO> alarms = new HashSet<AlarmPOJO>(0);

	/**
	 * Gets the value of the "REC_PK" column.
	 * @return	Value of the "REC_PK" column.
	 */
	public Long getRecPk() {
		return this.recPk;
	}

	/**
	 * Sets the value of the "REC_PK" column.
	 * @param pk	The value of the "REC_PK" column.
	 */
	public void setRecPk(Long pk) {
		this.recPk = pk;
	}

	/**
	 * Gets the value of the "MAIL" column.
	 * @return	Value of the "MAIL" column.
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * Gets the value of the "MAIL" column.
	 * @param email	The value of the "MAIL" column.
	 */
	public void setMail(String email) {
		this.mail = email;
	}

	/**
	 * Gets the association of the "ALARM" column.
	 * @return	Association of the "ALARM" column.
	 */
	public Set<AlarmPOJO> getAlarms() {
		return this.alarms;
	}

	/**
	 * Sets the association of the "ALARM" column.
	 * @param alarmsAss	Association of the "ALARM" column.
	 */
	public void setAlarms(Set<AlarmPOJO> alarmsAss) {
		this.alarms = alarmsAss;
	}

}
