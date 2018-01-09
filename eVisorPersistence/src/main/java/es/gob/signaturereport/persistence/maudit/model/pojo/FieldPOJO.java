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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.db.model.Field.java.</p>
 * <b>Description:</b><p>Class that represent a record of the 'FIELDS' table.</p>
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
 * <p>Class that represent a record of the 'FIELDS' table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/07/2011.
 */
@Entity
@Table(name = "FIELDS")
public class FieldPOJO implements Serializable {

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -6142615357772352663L;
	
	/**
	 * Attribute that represents the value of FIELD_PK column.
	 */
	@Id
    @Column(name = "FIELD_PK", unique = true, nullable = false, length = NumberConstants.INT_7)
	private int fieldPk;
	
	/**
	 * Attribute that represents the value of LABEL column. 
	 */
	@Column(name = "LABEL", length = NumberConstants.INT_255)
	private String label = null;
	
	/**
	 * Attribute that represents the value of DESCRIPTION column. 
	 */
	@Column(name = "DESCRIPTION", length = NumberConstants.INT_50)
	private String description = null;
		
	/**
	 * Gets the value of the value of FIELD_PK column.
	 * @return the value of the value of FIELD_PK column.
	 */
	public int getFieldPk() {
		return fieldPk;
	}

	
	/**
	 * Sets the value of the value of FIELD_PK column.
	 * @param pk The value for the value of FIELD_PK column.
	 */
	public void setFieldPk(int pk) {
		this.fieldPk = pk;
	}

	
	/**
	 * Gets the value of the value of LABEL column.
	 * @return the value of the value of LABEL column.
	 */
	public String getLabel() {
		return label;
	}

	
	/**
	 * Sets the value of the value of LABEL column.
	 * @param fieldLabel The value for the value of LABEL column.
	 */
	public void setLabel(String fieldLabel) {
		this.label = fieldLabel;
	}

	
	/**
	 * Gets the value of the value of DESCRIPTION column. 
	 * @return the value of the value of DESCRIPTION column. 
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the value of DESCRIPTION column. 
	 * @param fieldDescription The value for the value of DESCRIPTION column. 
	 */
	public void setDescription(String fieldDescription) {
		this.description = fieldDescription;
	}
	
	
}
