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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.persistence.StatisticPersistenceException.java.</p>
 * <b>Description:</b><p>Class that represents an error in persistence operation occurred in the statistics module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 11/08/2011.
 */
package es.gob.signaturereport.persistence.maudit.statistics;


/** 
 * <p>Class that represents an error in persistence operation occurred in the statistics module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/08/2011.
 */
public class StatisticPersistenceException extends Exception {

	/**
	 * Attribute that represents the class serial version. 
	 */
	private static final long serialVersionUID = -8629983858771167137L;
	
	/**
	 * Attribute that represents a unknown error has occurred. 
	 */
	public static final int UNKNOWN_ERROR = -1;
	
	/**
	 * Attribute that represents an error has occurred in the interaction with the database. 
	 */
	public static final int DATABASE_ERROR = 0;
	
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
	 * Constructor method for the class StatisticPersistenceException.java.
	 * @param errorCode Error code.
	 * @param message 	Error description.
	 */
	public StatisticPersistenceException(int errorCode, String message) {
		super(message);
		this.code = errorCode;
		this.description = message;
	}

	/**
	 * Constructor method for the class StatisticPersistenceException.java.
	 * @param errorCode Error code.
	 * @param message 	Error description.
	 * @param cause		Error cause.
	 */
	public StatisticPersistenceException(int errorCode, String message,Throwable cause) {
		super(message,cause);
		this.code = errorCode;
		this.description = message;
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
	 * @param message The value for the error description.
	 */
	public void setDescription(String message) {
		this.description = message;
	}
}
