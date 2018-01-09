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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.TraceFieldValue.java.</p>
 * <b>Description:</b><p> Class that represents a field value include into an audit trace.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>26/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 26/07/2011.
 */
package es.gob.signaturereport.maudit.log.traces;

/** 
 * <p>Class that represents the field value include into an audit trace.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 26/07/2011.
 */
public class TraceFieldValue {

	/**
	 * Attribute that represents the value of the field. 
	 */
	private String fieldValue = null;

	/**
	 * Attribute that represents the field value identifier. 
	 */
	private String fieldValueType = null;

	/**
	 * Constructor method for the class TraceFieldValue.java.
	 * @param value 	Value of the field.
	 * @param type 	Additional key value identifier. May be null.
	 */
	public TraceFieldValue(String value, String type) {
		super();
		this.fieldValue = value;
		this.fieldValueType = type;
	}

	
	/**
	 * Gets the value of the field value.
	 * @return the value of the field value.
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	
	/**
	 * Sets the value of the field value.
	 * @param value The value for the field value.
	 */
	public void setFieldValue(String value) {
		this.fieldValue = value;
	}


	
	/**
	 * Gets the value of the field value type.
	 * @return the value of the field value type.
	 */
	public String getFieldValueType() {
		return fieldValueType;
	}


	
	/**
	 * Sets the value of the field value type.
	 * @param type The value for the field value type.
	 */
	public void setFieldValueType(String type) {
		this.fieldValueType = type;
	}

	
	
	
	

	

}
