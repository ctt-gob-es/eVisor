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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.OOfficeConnectionException.java.</p>
 * <b>Description:</b><p>Class that contains information about an connection error to Open Office Server..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;


/** 
 * <p>Class that contains information about an connection error to Open Office Server.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public class OOfficeConnectionException extends Exception {

	/**
	 * Attribute that represents the class serial version. 
	 */
	private static final long serialVersionUID = 361457271528264154L;
	
	/**
	 * Attribute that represents a description of error. 
	 */
	private String description = null;
	
	/**
	 * Gets the value of the description of error.
	 * @return the value of the description of error.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description of error.
	 * @param descriptionParam The value for the description of error.
	 */
	public final void setDescription(String descriptionParam) {
		this.description = descriptionParam;
	}

	/**
	 * Constructor method for the class OOfficeConnectionException.java.
	 * @param msg	Description of error.
	 * @param cause Cause of error.
	 */
	public OOfficeConnectionException(String msg, Throwable cause) {
		super(msg, cause);
		this.description = msg;
	}

	
}
