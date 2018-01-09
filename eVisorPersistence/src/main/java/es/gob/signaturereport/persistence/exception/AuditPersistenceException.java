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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.AuditPersistenceException.java.</p>
 * <b>Description:</b><p> Class that represents an error occurred in an operation of persistence in the audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>20/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 20/07/2011.
 */
package es.gob.signaturereport.persistence.exception;


/** 
 * <p>Class that represents an error occurred in an operation of persistence in the audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 20/07/2011.
 */
public class AuditPersistenceException extends Exception {

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 8283728293960734144L;

	/**
	 * Attribute that represents a unknown error has occurred. 
	 */
	public static final int UNKNOWN_ERROR = -1;
	
	/**
	 * A required element was not found. 
	 */
	public static final int ITEM_NOT_FOUND = 1;
	
	/**
	 * The input parameters are invalid. 
	 */
	public static final int INVALID_INPUT_PARAMETERS = 2;
	
	
	/**
     * Attribute that identifies the type of error occurred. 
     */
    private int code = UNKNOWN_ERROR;

    /**
     * Attribute that represents the description of error. 
     */
    private String description = null;

	/**
	 * Constructor method for the class AuditPersistenceException.java.
	 * @param errorCode			Error code.
	 * @param errorDescription 	Error description.
	 */
	public AuditPersistenceException(int errorCode, String errorDescription) {
		super(errorDescription);
		this.code = errorCode;
		this.description = errorDescription;
	}
	
	/**
	 * Constructor method for the class AuditPersistenceException.java.
	 * @param errorCode			Error code.
	 * @param errorDescription 	Error description.
	 * @param cause				Error cause.
	 */
	public AuditPersistenceException(int errorCode, String errorDescription, Throwable cause) {
		super(errorDescription,cause);
		this.code = errorCode;
		this.description = errorDescription;
	}

	
	/**
	 * Gets the value of the error code.
	 * @return the value of the error code.
	 */
	public int getCode() {
		return code;
	}

	
	/**
	 * Sets the value of the error code.
	 * @param errorCode The value for the error code.
	 */
	public void setCode(int errorCode) {
		this.code = errorCode;
	}

	
	/**
	 * Gets the value of the error description.
	 * @return the value of the error description.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the error description.
	 * @param errorDescription The value for the error description.
	 */
	public void setDescription(String errorDescription) {
		this.description = errorDescription;
	}
    
}
