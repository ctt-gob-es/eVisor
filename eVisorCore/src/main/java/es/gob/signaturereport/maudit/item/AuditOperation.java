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
 * <b>File:</b><p>es.gob.signaturereport.maudit.item.AuditOperation.java.</p>
 * <b>Description:</b><p> Class that represent an operation of a audited service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>12/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 12/09/2011.
 */
package es.gob.signaturereport.maudit.item;


/** 
 * <p>Class that represent an operation of a audited service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 12/09/2011.
 */
public class AuditOperation {

	/**
	 * Attribute that represents the operation identifier. 
	 */
	private int operationId = 0;
	
	/**
	 * Attribute that represents description of the service. 
	 */
	private String description = null;

	
	/**
	 * Gets the value of the operation identifier.
	 * @return the value of the operation identifier.
	 */
	public int getOperationId() {
		return operationId;
	}

	
	/**
	 * Sets the value of the operation identifier.
	 * @param id The value for the operation identifier.
	 */
	public void setOperationId(int id) {
		this.operationId = id;
	}

	
	/**
	 * Gets the value of the operation description.
	 * @return the value of the operation description.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the operation description.
	 * @param opDescription The value for the operation description.
	 */
	public void setDescription(String opDescription) {
		this.description = opDescription;
	}


	/**
	 * Constructor method for the class AuditOperation.java.
	 * @param id	Operation identifier.
	 * @param opDescription 	Operation description.
	 */
	public AuditOperation(int id, String opDescription) {
		super();
		this.operationId = id;
		this.description = opDescription;
	}
	
	
}
