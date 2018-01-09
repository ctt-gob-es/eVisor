// Copyright (C) 2017 MINHAP, Gobierno de España
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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.impl.CommonTrace.java.</p>
 * <b>Description:</b><p> Class that represents a trace where all parameters are key-value type.
 * <br/> Ej: param1=(value1),param2=(value2),param3=(value3).....</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit.log.traces.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import es.gob.signaturereport.maudit.log.traces.ATrace;
import es.gob.signaturereport.maudit.log.traces.TraceFieldValue;


/** 
 * <p>Class that represents a trace where all parameters are key-value type.
 * <br/> Ej: param1=(value1),param2=(value2),param3=(value3).....</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class CommonTrace extends ATrace {

	
	/**
	 * Constructor method for the class CommonTrace.java.
	 * @param sequenceId	Event transaction identifier.
	 * @param actionId 		Action identifier.
	 * @param serviceId 	Service identifier.
	 * @param creationDate 	Creation time.
	 * @param messageFields 		Trace fields.
	 * @param messageHashParam Message hash.
	 */
	public CommonTrace(long sequenceId, int actionId, int serviceId, Date creationDate, LinkedHashMap<String, List<TraceFieldValue>> messageFields, String messageHashParam) {
		super(sequenceId, actionId, serviceId, creationDate, messageFields, messageHashParam);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#addField(java.lang.String, java.lang.Object)
	 */
	public void addField(String fieldIdentifier, Object fieldValue) {
		TraceFieldValue param = new TraceFieldValue(fieldValue.toString(), null);
		ArrayList<TraceFieldValue> fieldValueParams = new ArrayList<TraceFieldValue>();
		fieldValueParams.add(param);
		addMessageField(fieldIdentifier, fieldValueParams);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue(String identifier) {
		String value = null;
		List<TraceFieldValue> params = getMessageField(identifier);
		if(params!=null && params.size()==1){
			value = params.get(0).getFieldValue();
		}
		return value;
	}

	

}
