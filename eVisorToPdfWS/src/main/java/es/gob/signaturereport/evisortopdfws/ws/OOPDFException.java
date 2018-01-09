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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.ToolsException.java.</p>
 * <b>Description:</b><p> Class that contains information about an error that occurred in one of the utility classes.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

/** 
 * <p>Class that contains information about an error that occurred in one of the utility classes.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public class OOPDFException extends Exception {

	/**
	 * Attribute that represents serial version of class. 
	 */
	private static final long serialVersionUID = -8115731516478580213L;

	/**
	 * Attribute that represents the code associated to unknown error. 
	 */
	public static final int UNKNOWN_ERROR = -1;

	/**
	 * Attribute that represents the code associated to XML parser error. 
	 */
	public static final int XML_PARSER_ERROR = 1;

	/**
	 * Attribute that represents the code associated to an error while search by XPATH. 
	 */
	public static final int XPATH_ERROR = 2;

	/**
	 * Attribute that represents the code associated to a invalid signature. 
	 */
	public static final int INVALID_SIGNATURE = 3;

	/**
	 * Attribute that represents the code associated to an error while transform by XSLT. 
	 */
	public static final int XSL_TRANSFORM_ERROR = 4;

	/**
	 * Attribute that represents the error code associated to invalid number of page. 
	 */
	public static final int INVALID_PAGE_NUMBER = 5;

	/**
	 * Attribute that represents the file supplied is not valid PDF. 
	 */
	public static final int INVALID_PDF_FILE = 6;

	/**
	 * Attribute that represents the concatenation rule isn't valid. 
	 */
	public static final int INVALID_CONCATENATION_RULE = 7;

	/**
	 * Attribute that represents that an error occurs while the system access to a file. 
	 */
	public static final int ACCESS_FILE_ERROR = 8;
	
	/**
	 * Attribute that represents the document isn't a valid ODF. 
	 */
	public static final int INVALID_ODF_FILE = 9;

	/**
	 * Attribute that represents the operation is not allowed. 
	 */
	public static final int OPERATION_NOT_ALLOWED = 10;

	/**
	 * Attribute that represents the document is not a valid XSL-FO file. 
	 */
	public static final int INVALID_FO_FILE = 11;

	/**
	 * Attribute that represents the rotated angle is not allow. 
	 */
	public static final int INVALID_ROTATED_ANGLE = 12;

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
	public final int getCode() {
		return code;
	}

	/**
	 * Sets the value of the attribute 'code'.
	 * @param codeParam The value for the attribute 'code'.
	 */
	public final void setCode(int codeParam) {
		this.code = codeParam;
	}

	/**
	 * Gets the value of the attribute 'description'.
	 * @return the value of the attribute 'description'.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the attribute 'description'.
	 * @param descriptionParam The value for the attribute 'description'.
	 */
	public final void setDescription(String descriptionParam) {
		this.description = descriptionParam;
	}

	/**
	 * Constructor method for the class TransformException.java.
	 * @param codeParam		Code of error.
	 * @param descriptionParam 	Description of error.
	 */
	public OOPDFException(int codeParam, String descriptionParam) {
		super(descriptionParam);
		this.code = codeParam;
		this.description = descriptionParam;
	}
	
	/**
     * Constructor method for the class TransformException.java.
     * @param codeParam Code of error.
     * @param descriptionParam Description of error.
     * @param cause Error cause.
     */
    public OOPDFException(int codeParam, String descriptionParam,Throwable cause) {
	super(descriptionParam,cause);
	this.code = codeParam;
	this.description = descriptionParam;
    }
}
