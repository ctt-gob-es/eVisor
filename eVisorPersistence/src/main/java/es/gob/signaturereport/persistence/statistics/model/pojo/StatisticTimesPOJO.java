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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.persistence.db.model.StatisticTimes.java.</p>
 * <b>Description:</b><p>Class that represents a record of the ST_TIMES table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.persistence.statistics.model.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;


/** 
 * <p>Class that represents a record of the ST_TIMES table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/07/2016.
 */
@Entity
@Table(name = "ST_TIMES")
public class StatisticTimesPOJO implements Serializable {
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 4615322366497557854L;

	/**
	 * Attribute that represents the primary key of the table. 
	 */
	@Id
	@GeneratedValue(generator = "ST_TIME_SEQ_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ST_TIME_SEQ_GEN", sequenceName = "ST_TIME_SEQ",allocationSize=0)
	@Column(name = "ST_TIME_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long stTimePk;

	/**
	 * Attribute that represents the calculation time. 
	 */
	@Column(name = "ST_DATE")
	private Date stDate = null;

	/**
	 * Attribute that represents the creation time of the record. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime = null;

	
	/**
	 * Gets the value of the primary key.
	 * @return the value of the primary key.
	 */
	public Long getStTimePk() {
		return stTimePk;
	}

	
	/**
	 * Sets the value of the primary key.
	 * @param stTimePk The value for the primary key.
	 */
	public void setStTimePk(Long stTimePk) {
		this.stTimePk = stTimePk;
	}

	
	/**
	 * Gets the value of the calculation time.
	 * @return the value of the calculation time.
	 */
	public Date getStDate() {
		return stDate;
	}

	
	/**
	 * Sets the value of the calculation time.
	 * @param stDate The value for the calculation time.
	 */
	public void setStDate(Date stDate) {
		this.stDate = stDate;
	}

	
	/**
	 * Gets the value of the creation time.
	 * @return the value of the creation time.
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	
	/**
	 * Sets the value of the creation time.
	 * @param creationTime The value for the creation time.
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}
