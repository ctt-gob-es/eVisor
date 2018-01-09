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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.Service.java.</p>
 * <b>Description:</b><p>Class that represent a record of the 'SERVICES' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/07/2011.
 */
package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import es.gob.signaturereport.persistence.utils.NumberConstants;

/** 
 * <p>Class that represent a record of the 'SERVICES' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/07/2011.
 */
@Entity
@Table(name = "SERVICES")
public class ServicesPOJO implements Serializable {
	
	/**
	 * Serial version of the class.
	 */
	private static final long serialVersionUID = 1922042293939785614L;

	/**
	 * Attribute that represents the SERVICE_PK column value. 
	 */
	@Id
    @Column(name = "SERVICE_PK", unique = true, nullable = false, length = NumberConstants.INT_3)
	private int servicePk = 0;
	
	/**
	 * Attribute that represents the DESCRIPTION column value. 
	 */
	@Column(name = "DESCRIPTION", length = NumberConstants.INT_50)
	private String description = null;
	
	
	/**
	 * Gets the value of the SERVICE_PK column value.
	 * @return the value of the SERVICE_PK column value.
	 */
	public int getServicePk() {
		return servicePk;
	}

	
	/**
	 * Sets the value of the SERVICE_PK column value.
	 * @param servicePk The value for the SERVICE_PK column value.
	 */
	public void setServicePk(int servicePk) {
		this.servicePk = servicePk;
	}

	
	/**
	 * Gets the value of the DESCRIPTION column value.
	 * @return the value of the DESCRIPTION column value.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the DESCRIPTION column value.
	 * @param description The value for the DESCRIPTION column value.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
}
