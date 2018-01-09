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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.invoker.ServiceInvokerException.java.</p>
 * <b>Description:</b><p> Class that represents an error associated to invocation of "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 15/02/2011.
 */
package es.gob.signaturereport.mfirma.invoker;


/** 
 * <p>Class that represents an error associated to invocation of "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 15/02/2011.
 */
public class ServiceInvokerException extends Exception {

    /**
     * Attribute that represents . 
     */
    private static final long serialVersionUID = -4293744071637297049L;
    
    /**
     * Attribute that represents that a unknown error has occurred.
     */
    public static final int UNKNOWN_ERROR = 0;
    
    
    /**
     * Attribute that represents that the input parameters is not valid. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 1;
    
    /**
     * Attribute that identifies the type of error occurred. 
     */
    private int code = UNKNOWN_ERROR;

    /**
     * Attribute that represents the description of error. 
     */
    private String description = null;

    
    /**
     * Gets the value of the attribute 'code'.
     * @return the value of the attribute 'code'.
     */
    public int getCode() {
        return code;
    }

    
    /**
     * Sets the value of the attribute 'code'.
     * @param errorCode The value for the attribute 'code'.
     */
    public void setCode(int errorCode) {
        this.code = errorCode;
    }

    
    /**
     * Gets the value of the attribute 'description'.
     * @return the value of the attribute 'description'.
     */
    public String getDescription() {
        return description;
    }

    
    /**
     * Sets the value of the attribute 'description'.
     * @param message The value for the attribute 'description'.
     */
    public void setDescription(String message) {
        this.description = message;
    }


    /**
     * Constructor method for the class ServiceInvokerException.java.
     * @param errorCode	Identifier the type of error occurred.
     * @param message 	Description of error.
     */
    public ServiceInvokerException(int errorCode, String message) {
	super(message);
	this.code = errorCode;
	this.description = message;
    }

    /**
     * Constructor method for the class ServiceInvokerException.java.
     * @param errorCode	Identifier the type of error occurred.
     * @param message  	Description of error.
     * @param errorCause			Error cause.
     */
    public ServiceInvokerException(int errorCode, String message ,Throwable errorCause) {
	super(message ,errorCause);
	this.code = errorCode;
	this.description = message ;
    }

}
