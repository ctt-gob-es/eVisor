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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.exception.TransformException.java.</p>
 * <b>Description:</b><p> Class that contains information about an error that occurred 
 * <br/>in the creation of an XML request or reading a XML response.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/02/2011.
 */
package es.gob.signaturereport.messages.transform.exception;


/** 
 * <p>Class that contains information about an error that occurred 
 * <br/>in the creation of an XML request or reading a XML response.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/02/2011.
 */
public class TransformException extends Exception{

    /**
     * Attribute that represents the serial version of class. 
     */
    private static final long serialVersionUID = 1921747593734614717L;

    /**
     * Attribute that represents the code associated to unknown error. 
     */
    public static final int UNKNOWN_ERROR = -1;

    /**
     * Attribute that represents the transform implementation is not found. 
     */
    public static final int CLASS_NOT_FOUND = 1;

    /**
     * Attribute that represents an error occurred in instancing process. 
     */
    public static final int INSTANCE_ERROR = 2;
    
    /**
     * Attribute that represents an error associated to XML template. 
     */
    public static final int TEMPLATE_ERROR = 3;
    
    /**
     * Attribute that represents an error associated to input parameters. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 4;
    
    /**
     * Attribute that specifies the error type. 
     */
    private int code = UNKNOWN_ERROR;
    
    /**
     * Attribute that represents a description of error. 
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
     * @param code The value for the attribute 'code'.
     */
    public void setCode(int code) {
        this.code = code;
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
     * @param description The value for the attribute 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Constructor method for the class TransformException.java.
     * @param code		Code of error.
     * @param description 	Description of error.
     */
    public TransformException(int code, String description) {
	super(description);
	this.code = code;
	this.description = description;
    }
    
}
