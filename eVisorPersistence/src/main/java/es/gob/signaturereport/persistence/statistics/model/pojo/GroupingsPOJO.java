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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.persistence.db.model.Groupings.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.persistence.statistics.model.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>This class represents a record contained into the ST_GROUPINGS table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/07/2016.
 */
@Entity
@Table(name = "ST_GROUPINGS")
@NamedQueries({ @NamedQuery(name = "getGroupedStatistics", query = "SELECT uo FROM UoPOJO uo WHERE uo.uoId = :uoId AND uo.endingtime is null") })
@NamedNativeQueries({ @NamedNativeQuery(name = "computeStatistics", query = "select ST_SERVICE, ST_APPLICATION, ST_UO, ST_CODE, ST_ISSIGN, count(*) as TOTAL from (select TRAN, ST_SERVICE, ST_APPLICATION, ST_UO, ST_CODE from(select TRAN, ST_SERVICE, ST_APPLICATION, ST_UO from(select t.S_TRANSACTION_PK as TRAN, t.SERVICE as ST_SERVICE, f.F_VALUE as ST_APPLICATION from s_transactions t inner join t_traces tr on t.S_TRANSACTION_PK = tr.S_TRANSACTION inner join t_tracefields f on f.T_TRACE = tr.T_TRACE_PK where (t.CREATIONTIME>=:beginning and t.CREATIONTIME<:ending) and f.FIELD=0) left outer join ( select t.S_TRANSACTION_PK as TRAN_UO, f.F_VALUE as ST_UO from s_transactions t inner join t_traces tr on t.S_TRANSACTION_PK = tr.S_TRANSACTION inner join t_tracefields f on f.T_TRACE = tr.T_TRACE_PK where (t.CREATIONTIME>=:beginning and t.CREATIONTIME<:ending) and f.FIELD=12 ) on TRAN = TRAN_UO ) left outer join ( select t.S_TRANSACTION_PK as TRAN_CODE, f.F_VALUE as ST_CODE from s_transactions t inner join t_traces tr on t.S_TRANSACTION_PK = tr.S_TRANSACTION inner join t_tracefields f on f.T_TRACE = tr.T_TRACE_PK where (t.CREATIONTIME>=:beginning and t.CREATIONTIME<:ending) and f.FIELD=9 ) on TRAN = TRAN_CODE ) left outer join (select t.S_TRANSACTION_PK as TRAN_ISSIGN, f.F_VALUE as ST_ISSIGN from s_transactions t inner join t_traces tr on t.S_TRANSACTION_PK = tr.S_TRANSACTION inner join t_tracefields f on f.T_TRACE = tr.T_TRACE_PK where (t.CREATIONTIME>=:beginning and t.CREATIONTIME<:ending) and f.FIELD=4 ) on TRAN = TRAN_ISSIGN group by ST_SERVICE, ST_APPLICATION, ST_UO, ST_CODE, ST_ISSIGN", resultSetMapping = "computeWorkaround") })
@SqlResultSetMapping(name = "computeWorkaround", columns = { @ColumnResult(name = "ST_SERVICE"), @ColumnResult(name = "ST_APPLICATION"), @ColumnResult(name = "ST_UO"), @ColumnResult(name = "ST_CODE"), @ColumnResult(name = "ST_ISSIGN"), @ColumnResult(name = "TOTAL") })
public class GroupingsPOJO implements Serializable {

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 3336690419259718796L;

	/**
	 * Attribute that represents the primary key of the table 'ST_GROUPINGS'. 
	 */
	@Id
	@GeneratedValue(generator = "ST_GROUPING_SEQ_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ST_GROUPING_SEQ_GEN", sequenceName = "ST_GROUPING_SEQ", allocationSize = 0)
	@Column(name = "ST_GROUPING_PK", unique = true, nullable = false, precision = NumberConstants.INT_19)
	private Long groupingPk;

	/**
	 * Attribute that represents the 'ST_TIME' column value. 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ST_TIME", nullable = false)
	private StatisticTimesPOJO stTime;

	/**
	 * Attribute that represents the 'ST_SERVICE' column value. 
	 */
	@Column(name = "ST_SERVICE")
	private int service;

	/**
	 * Attribute that represents the 'ST_APPLICATION' column value. 
	 */
	@Column(name = "ST_APPLICATION")
	private String application;

	/**
	 * Attribute that represents the 'ST_UO' column value. 
	 */
	@Column(name = "ST_UO")
	private String uo;

	/**
	 * Attribute that represents the 'ST_CODE' column value. 
	 */
	@Column(name = "ST_CODE")
	private String code;

	/**
	 * Attribute that represents the 'ST_ISSIGN' column value. 
	 */
	@Column(name = "ST_ISSIGN")
	private String sign;

	/**
	 * Attribute that represents the 'CREATIONTIME' column value. 
	 */
	@Column(name = "CREATIONTIME")
	private Date creationTime;

	/**
	 * Attribute that represents the 'TOTAL' column value. 
	 */
	@Column(name = "TOTAL")
	private long total;

	/**
	 * Gets the value of the grouping identifier.
	 * @return the value of the grouping identifier.
	 */
	public Long getGroupingPk() {
		return groupingPk;
	}

	/**
	 * Sets the value of the grouping identifier.
	 * @param groupPk The value for the grouping identifier.
	 */
	public void setGroupingPk(Long groupPk) {
		this.groupingPk = groupPk;
	}

	/**
	 * Gets the value of the statistics time.
	 * @return the value of the statistics time.
	 */
	public StatisticTimesPOJO getStTime() {
		return stTime;
	}

	/**
	 * Sets the value of the statistics time.
	 * @param time The value for the statistics time.
	 */
	public void setStTime(StatisticTimesPOJO time) {
		this.stTime = time;
	}

	/**
	 * Gets the value of the service identifier.
	 * @return the value of the service identifier.
	 */
	public int getService() {
		return service;
	}

	/**
	 * Sets the value of the service identifier.
	 * @param serviceId The value for the service identifier.
	 */
	public void setService(int serviceId) {
		this.service = serviceId;
	}

	/**
	 * Gets the value of the application identifier.
	 * @return the value of the application identifier.
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * Sets the value of the application identifier.
	 * @param applicationId The value for the application identifier.
	 */
	public void setApplication(String applicationId) {
		this.application = applicationId;
	}

	/**
	 * Gets the value of the unit organization identifier.
	 * @return the value of the unit organization identifier.
	 */
	public String getUo() {
		return uo;
	}

	/**
	 * Sets the value of the unit organization identifier.
	 * @param uoId The value for the unit organization identifier.
	 */
	public void setUo(String uoId) {
		this.uo = uoId;
	}

	/**
	 * Gets the value of the result code.
	 * @return the value of the result code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the value of the result code.
	 * @param resultCode The value for the result code.
	 */
	public void setCode(String resultCode) {
		this.code = resultCode;
	}

	/**
	 * Gets if the report was signed.
	 * @return if the report was signed.
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * Sets if the report was signed.
	 * @param signed If the report was signed.
	 */
	public void setSign(String signed) {
		this.sign = signed;
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
	 * @param time The value for the creation time.
	 */
	public void setCreationTime(Date time) {
		this.creationTime = time;
	}

	/**
	 * Gets the value of number of matches.
	 * @return the value of number of matches.
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Sets the value of number of matches.
	 * @param numTotal The value for number of matches.
	 */
	public void setTotal(long numTotal) {
		this.total = numTotal;
	}

}
