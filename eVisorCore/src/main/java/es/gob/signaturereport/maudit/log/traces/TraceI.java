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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.TraceI.java.</p>
 * <b>Description:</b><p> Interface that provide the methods and constants for reading and writing the event message.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>14/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 14/07/2011.
 */
package es.gob.signaturereport.maudit.log.traces;

import java.util.Date;
import java.util.List;
import java.util.Map;


/** 
 * <p>Interface that provide the methods and constants for reading and writing the event message.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 14/07/2011.
 */
public interface TraceI {

	/* 
	 * ----------------- FIELDS OF TRACES ------------------------
	 */
	
	/**
	 * Constant that represents the application identifier field. 
	 */
	String APPLICATION_ID = "APP_ID";
	
	/**
	 * Constant that represents the soap request store identifier. 
	 */
	String REQUEST_ID="REQ_ID";
	/**
	 * Constant that represents the soap response store identifier. 
	 */
	String RESPONSE_ID = "RES_ID";
	
	/**
	 * Constant that represents the template identifier. 
	 */
	String TEMPLATE_ID= "TEMPL_ID";
	/**
	 * Constant that indicates whether the report should be signed.
	 */
	String IS_SIGN = "IS_SIGN";
	
	/**
	 * Constant that represents the signature location into external repository. 
	 */
	String EXT_SIGN = "EXT_SIGN";
	
	/**
	 * Constant that represents the document location into external repository. 
	 */
	String EXT_DOC = "EXT_DOC";

	/**
	 * Constant that represents the bar codes. 
	 */
	String BARCODES = "BARCODES";

	/**
	 * Constant that represents the external parameters include into the generation report request. 
	 */
	String EXT_PARAMS = "EXT_PARAMS";

	/**
	 * Constants that represents the result code include into the web service response. 
	 */
	String RES_CODE = "RES_CODE";
	/**
	 * Constants that represents the result message include into the web service response. 
	 */
	String RES_MESSAGE = "RES_MSG";
	/**
	 * Constants that represents the result cause include into the web service response. 
	 */
	String RES_CAUSE = "RES_CAUSE";
	
	/**
	 * Constant that represents the organization unit identifier field. 
	 */
	String UO_ID = "UO_ID";
	
	/**
	 * Attribute that represents the template type. 
	 */
	String TEMPLATE_TYPE = "TEMP_TYPE";
	 
	
	/**
	 * Gets the string that represents a system trace.
	 * @return	Event message.
	 */
	String getMessage();
	
	/**
	 * Adds a field to the trace.
	 * @param fieldIdentifier	Field identifier.
	 * @param fieldValue	Field value.
	 */
	void addField(String fieldIdentifier, Object fieldValue);
	
	/**
	 * Gets the value of supplied field.
	 * @param identifier	Field identifier.
	 * @return	Field value. Null if the field doesn't exist.
	 */
	Object getFieldValue (String identifier);
	
	/**
	 * Gets all fields included into the trace.
	 * @return Trace fields map. The content may be:
	 * <br/> 1. If the audit field is "ID_FIELD=FIELD_VALUE" the map record content:
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record key = ID_FIELD.
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record content = List of a {@link TraceFieldValue} object that {@link TraceFieldValue#getFieldValue()}=FIELD_VALUE and {@link TraceFieldValue#getFieldValueType()}=null. 
	 * <br/> 2. If the audit field is "ID_FIELD=[FIELD_VALUE_1],[FIELD_VALUE_2],..,[FIELD_VALUE_N]" the map record content:
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record key = ID_FIELD.
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record content = List of N {@link TraceFieldValue} object that {@link TraceFieldValue#getFieldValue()}=FIELD_VALUE_X and {@link TraceFieldValue#getFieldValueType()}=null. 
     * <br/> 3. If the audit field is "ID_FIELD=[FIELD_TYPE_1=FIELD_VALUE_1],[FIELD_TYPE_2=FIELD_VALUE_2],...,[FIELD_TYPE_N=FIELD_VALUE_N]" the map record content:
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record key = ID_FIELD.
	 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Map record content = List of N {@link TraceFieldValue} object that {@link TraceFieldValue#getFieldValue()}=FIELD_VALUE_X and {@link TraceFieldValue#getFieldValueType()}=FIELD_TYPE_N. 
	 */
	Map<String,List<TraceFieldValue>> getFields();
	
	/**
	 * Gets the sequence identifier associated to the trace. 
	 * @return	Sequence identifier.
	 */
	long getSecuenceId();
	
	/**
	 * Gets the action identifier associated to the trace. 
	 * @return	action identifier.
	 */
	int getActionId();
	
	/**
	 * Gets the creation time of the trace.
	 * @return Creation time.
	 */
	Date getCreationTime();
	
	/**
	 * Gets the service identifiers.
	 * @return	Service identifier.
	 */
	int getServiceIdentifier();
}
