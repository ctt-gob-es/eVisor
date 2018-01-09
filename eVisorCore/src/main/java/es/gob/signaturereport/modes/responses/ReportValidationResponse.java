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
 * <b>File:</b><p>es.gob.signaturereport.modes.responses.ReportValidationResponse.java.</p>
 * <b>Description:</b><p>Class that contains the information of the validation report result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>24/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 24/05/2011.
 */
package es.gob.signaturereport.modes.responses;


/** 
 * <p>Class that contains the information of the validation report result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 24/05/2011.
 */
public class ReportValidationResponse {
	
	  
    /**
     * Attribute that represents that the signature is valid. 
     */
    public static final int VALID_SIGNATURE = 100;
    
    /**
     * Attribute that represents the parameters included into request are not valid. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 101;
    
    /**
     * Attribute that represents that the signature is invalid. 
     */
    public static final int INVALID_SIGNATURE = 102;
    
    /**
     * Attribute that represents that the signature status is not completely valid. 
     */
    public static final int WARNING_SIGNATURE = 103;
    
   
    /**
     * Attribute that represents the result of validation is unknown. 
     */
    public static final int UNKNOWN_STATUS = 104;
    
    /**
	 * Attribute that represents that the "@firma" server is unavailable. 
	 */
	public static final int AFIRMA_UNAVAILABLE = 105;
    
    /**
     * Attribute that represents that a unknown error has occurred.
     */
    public static final int UNKNOWN_ERROR = -101;
     
    
	/**
	 * Attribute that represents the result code associated to validation report process. 
	 */
	private int  code = VALID_SIGNATURE ;
	
	  /**
     * Attribute that represents the description of process result. 
     */
    private String message = null;

    /**
     * Attribute that represents the cause of problem. 
     */
    private String cause = null;
	/**
	 * Constructor method for the class ReportValidationResponse.java. 
	 */
	public ReportValidationResponse() {
	}

	/**
	 * Constructor method for the class ReportValidationResponse.java.
	 * @param resultCode		Result code associated to validation report process. 
	 * @param resultMessage 	Description of process result.
	 */
	public ReportValidationResponse(int resultCode, String resultMessage) {
		super();
		this.code = resultCode;
		this.message = resultMessage;
	}
	
	/**
	 * Constructor method for the class ReportValidationResponse.java.
	 * @param resultCode		Result code associated to validation report process. 
	 * @param resultMessage 	Description of process result.
	 * @param resultCause		Cause of problem.
	 */
	public ReportValidationResponse(int resultCode, String resultMessage,String resultCause) {
		super();
		this.code = resultCode;
		this.message = resultMessage;
		this.cause = resultCause;
	}

	
	/**
	 * Gets the value of the result code associated to validation report process.
	 * @return the value of the result code associated to validation report process.
	 */
	public int getCode() {
		return code;
	}

	
	/**
	 * Sets the value of the result code associated to validation report process.
	 * @param resultCode The value for the result code associated to validation report process.
	 */
	public void setCode(int resultCode) {
		this.code = resultCode;
	}

	
	/**
	 * Gets the value of the description of process result.
	 * @return the value of the description of process result.
	 */
	public String getMessage() {
		return message;
	}

	
	/**
	 * Sets the value of the description of process result.
	 * @param resultMessage The value for the description of process result.
	 */
	public void setMessage(String resultMessage) {
		this.message = resultMessage;
	}

	
	/**
	 * Gets the value of the cause of problem.
	 * @return the value of the cause of problem.
	 */
	public String getCause() {
		return cause;
	}

	
	/**
	 * Sets the value of the cause of problem.
	 * @param resultCause The value for the cause of problem.
	 */
	public void setCause(String resultCause) {
		this.cause = resultCause;
	}
	
	
}
