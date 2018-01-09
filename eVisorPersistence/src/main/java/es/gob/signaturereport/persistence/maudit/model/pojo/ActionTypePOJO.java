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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.ActionType.java.</p>
 * <b>Description:</b><p>Class that represent a record of the 'ACTIONTYPES' table.</p>
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
 * <p>Class that represent a record of the 'ACTIONTYPES' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/07/2011.
 */
@Entity
@Table(name = "ACTIONTYPES")
public class ActionTypePOJO implements Serializable {
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 5717463159253155842L;

	/**
	 * Attribute that represents the 'ACTIONTYPE_PK' column value. 
	 */
	@Id
    @Column(name = "ACTIONTYPE_PK", unique = true, nullable = false, length = NumberConstants.INT_7)
	private int actionType;
	
	/**
	 * Attribute that represents the 'DESCRIPTION' column value. 
	 */
	@Column(name = "DESCRIPTION")
	private String description;
		
	/**
	 * Gets the value of the 'ACTIONTYPE_PK' column value.
	 * @return the value of the 'ACTIONTYPE_PK' column value.
	 */
	public int getActionType() {
		return actionType;
	}
	
	/**
	 * Sets the value of the 'ACTIONTYPE_PK' column value.
	 * @param type The value for the 'ACTIONTYPE_PK' column value.
	 */
	public void setActionType(int type) {
		this.actionType = type;
	}

	
	/**
	 * Gets the value of the 'DESCRIPTION' column value.
	 * @return the value of the 'DESCRIPTION' column value.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the 'DESCRIPTION' column value.
	 * @param dsc The value for the 'DESCRIPTION' column value.
	 */
	public void setDescription(String dsc) {
		this.description = dsc;
	}
}
