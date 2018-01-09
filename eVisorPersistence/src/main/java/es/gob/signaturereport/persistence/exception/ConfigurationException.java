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
 * <b>File:</b><p>es.gob.signaturereport.persistence.configuration.model.bo.exception.ConfigurationException.java.</p>
 * <b>Description:</b><p> This class represents a record contained into the APPLICATION table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.persistence.exception;

/** 
 * <p>Class that contains information about an error in configuration management.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
public class ConfigurationException extends Exception {

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = -1822008200695011990L;

    /**
     * Attribute that represents that a unknown error has occurred.
     */
    public static final int UNKNOWN_ERROR = 0;

    /**
     * Attribute that represents that not found a configuration item.
     */
    public static final int ITEM_NO_FOUND = 1;
    
    
    /**
     * Attribute that represents that the input parameters of an operation are invalid. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 2;

    /**
     * Attribute that represents that a configuration operation is not valid because it associated to duplicate a configuration item. 
     */
    public static final int DUPLICATE_ITEM =3;
    
    /**
     * Attribute that represents an error to do configuration operation with a item configuration because this item is associated with other configuration item. 
     */
    public static final int ITEM_ASSOCIATED = 4;
    
    /**
     * Attribute that identifies the type of error occurred. 
     */
    private int code = UNKNOWN_ERROR;

    /**
     * Attribute that represents the description of error. 
     */
    private String description = null;

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
     * Constructor method for the class ConfigurationException.java.
     * @param errorCode	Identifier code that represents the type of error occurred.
     * @param errorDescription 	Description of error.
     */
    public ConfigurationException(int errorCode, String errorDescription) {
	super(errorDescription);
	this.code = errorCode;
	this.description = errorDescription;
    }
    
    /**
     * Constructor method for the class ConfigurationException.java.
     * @param errorCode	Identifier code that represents the type of error occurred.
     * @param errorDescription 	Description of error.
     * @param cause		Error cause.
     */
    public ConfigurationException(int errorCode, String errorDescription,Throwable cause) {
	super(errorDescription);
	this.code = errorCode;
	this.description = errorDescription;
    }

}
