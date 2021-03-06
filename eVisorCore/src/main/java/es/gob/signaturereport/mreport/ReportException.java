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
 * <b>File:</b><p>es.gob.signaturereport.mreport.ReportException.java.</p>
 * <b>Description:</b><p> Class that contains information about an error that occurred into  generation report module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>21/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 21/02/2011.
 */
package es.gob.signaturereport.mreport;


/** 
 * <p>Class that contains information about an error that occurred into  generation report module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/02/2011.
 */
public class ReportException extends Exception {

    /**
     * Attribute that represents the serial version of class. 
     */
    private static final long serialVersionUID = -9072074778671069296L;
    
    /**
     * Attribute that represents the code associated to unknown error. 
     */
    public static final int UNKNOWN_ERROR = -1;
    
    /**
     * Attribute that represents the code that indicates that input parameters are invalid. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 1;
    
    /**
     * Attribute that represents the code that indicates that the signature is invalid. 
     */
    public static final int INVALID_SIGNATURE = 2;
    
    /**
     * Attribute that represents that has exceeded the maximum number of pages allowed. 
     */
    public static final int OVER_MAX_PAGE_NUMBER = 3;

    /**
     * Attribute that represents the page number is invalid. 
     */
    public static final int INVALID_PAGE_NUMBER = 5;

    /**
     * Attribute that represents the document isn't a valid PDF. 
     */
    public static final int INVALID_PDF_FILE = 6;
    
    /**
     * Attribute that represents the template is invalid. 
     */
    public static final int INVALID_TEMPLATE = 7;
    
    
    /**
     * Attribute that identifies the error type occurred. 
     */
    private int code = UNKNOWN_ERROR;
    
    /**
     * Attribute that represents a error description. 
     */
    private String description = null;

    /**
     * Constructor method for the class ReportException.java.
     * @param codeId	Identifier of error.
     * @param message 	Error description.
     */
    public ReportException(int codeId, String message) {
	super(message);
	this.code = codeId;
	this.description = message;
    }

    
    /**
     * Constructor method for the class ReportException.java.
     * @param codeId	Identifier of error.
     * @param message 	Error description.
     * @param cause		Error cause.
     */
    public ReportException(int codeId, String message,Throwable cause) {
	super(message,cause);
	this.code = codeId;
	this.description = message;
	
    }
    
    /**
     * Gets the value of the attribute that identifies the error type occurred.
     * @return the value of the attribute that identifies the error type occurred.
     */
    public int getCode() {
        return code;
    }

    
    /**
     * Sets the value of the attribute that identifies the error type occurred.
     * @param codeId The value for the attribute that identifies the error type occurred.
     */
    public void setCode(int codeId) {
        this.code = codeId;
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
