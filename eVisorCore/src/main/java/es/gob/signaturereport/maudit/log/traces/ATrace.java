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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.ATrace.java.</p>
 * <b>Description:</b><p> Abstract class that represents a system trace.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit.log.traces;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Abstract class that represents a system trace.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public abstract class ATrace implements TraceI {

	/**
	 * Attribute that represents the event transaction identifier. 
	 */
	private long sequenceId = -1;

	/**
	 * Attribute that represents the action identifier. 
	 */
	private int actionId = -1;

	/**
	 * Attribute that represents the service identifier. 
	 */
	private int serviceId = -1;

	/**
	 * Attribute that represents the trace creation time. 
	 */
	private Date creationTime = null;

	/**
	 * Attribute that represents the fields include into the trace. 
	 */
	private Map<String, List<TraceFieldValue>> fields = null;
	
	/**
	 * Attribute that represents message hash. 
	 */
	private String messageHash = null;

	/**
	 * Constructor method for the class ATrace.java.
	 * @param transactionId	Event transaction identifier.
	 * @param action 		Action identifier.
	 * @param service 	Service identifier.
	 * @param time 	Creation time.
	 * @param traces 		Trace fields.
	 * @param messageHashParam Message hash.
	 */
	public ATrace(long transactionId, int action, int service, Date time, Map<String, List<TraceFieldValue>> traces, String messageHashParam) {
		this.sequenceId = transactionId;
		this.actionId = action;
		if (traces != null) {
			this.fields = traces;
		} else {
			this.fields = new LinkedHashMap<String, List<TraceFieldValue>>();
		}
		this.serviceId = service;
		this.creationTime = time;
		this.messageHash = messageHashParam;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getMessage()
	 */
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append(TraceFactory.SEQUENCE_ID);
		sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
		sb.append(TraceFactory.START_VALUE_CHAR);
		sb.append(sequenceId);
		sb.append(TraceFactory.END_VALUE_CHAR);

		sb.append(TraceFactory.FIELD_DELIMITER_CHAR);

		sb.append(TraceFactory.ACTION_ID);
		sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
		sb.append(TraceFactory.START_VALUE_CHAR);
		sb.append(actionId);
		sb.append(TraceFactory.END_VALUE_CHAR);

		sb.append(TraceFactory.FIELD_DELIMITER_CHAR);

		sb.append(TraceFactory.SERVICE_ID);
		sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
		sb.append(TraceFactory.START_VALUE_CHAR);
		sb.append(serviceId);
		sb.append(TraceFactory.END_VALUE_CHAR);

		sb.append(TraceFactory.FIELD_DELIMITER_CHAR);
		
		if (messageHash != null && !messageHash.isEmpty()) {
    		sb.append(TraceFactory.HASH_MESSAGE);
    		sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
    		sb.append(TraceFactory.START_VALUE_CHAR);
    		sb.append(messageHash);
    		sb.append(TraceFactory.END_VALUE_CHAR);
    
    		sb.append(TraceFactory.FIELD_DELIMITER_CHAR);
		}

		sb.append(TraceFactory.CREATION_TIME);
		sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
		sb.append(TraceFactory.START_VALUE_CHAR);
		UtilsTime utilTime = new UtilsTime(creationTime);
		sb.append(utilTime.toString(TraceFactory.DATE_FORMAT));
		sb.append(TraceFactory.END_VALUE_CHAR);

		if (!fields.isEmpty()) {
			Iterator<String> keys = fields.keySet().iterator();
			while (keys.hasNext()) {
				String fieldId = keys.next();
				List<TraceFieldValue> fieldValue = fields.get(fieldId);
				if (!fieldValue.isEmpty()) {
					sb.append(TraceFactory.FIELD_DELIMITER_CHAR);
					sb.append(fieldId);
					sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
					sb.append(TraceFactory.START_VALUE_CHAR);
					TraceFieldValue fieldParam = fieldValue.get(0);
					if (fieldParam.getFieldValueType() == null && fieldValue.size() == 1) {
						sb.append(fieldParam.getFieldValue());
					} else {
						sb.append(TraceFactory.START_FIELD_VALUE_CHAR);			
						if (fieldParam.getFieldValueType() != null) {
							sb.append(fieldParam.getFieldValueType());
							sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
						}
						sb.append(fieldParam.getFieldValue());
						sb.append(TraceFactory.END_FIELD_VALUE_CHAR);
					}
					for (int i = 1; i < fieldValue.size(); i++) {
						TraceFieldValue param = fieldValue.get(i);
						sb.append(TraceFactory.FIELD_DELIMITER_CHAR);
						sb.append(TraceFactory.START_FIELD_VALUE_CHAR);
						if (param.getFieldValueType() != null) {
							sb.append(param.getFieldValueType());
							sb.append(TraceFactory.KEY_VALUE_DELIMITER_CHAR);
						}
						sb.append(param.getFieldValue());
						sb.append(TraceFactory.END_FIELD_VALUE_CHAR);
					}
					sb.append(TraceFactory.END_VALUE_CHAR);

				}
			}
		}
		return sb.toString().replace("\n", "");
	}

	/**
	* Add a message field.
	* @param fieldIdentifier Field identifier.
	* @param fieldValue Field value.
	*/
	protected void addMessageField(String fieldIdentifier, List<TraceFieldValue> fieldValue) {
		if (fieldIdentifier != null && fieldValue != null) {
			fields.put(fieldIdentifier, fieldValue);
		}
	}

	/**
	* Gets the value of the supplied field.
	* @param identifier Identifier of field to get.
	* @return Field value.
	*/
	protected List<TraceFieldValue> getMessageField(String identifier) {
		return fields.get(identifier);
	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getSecuenceId()
	 */
	public long getSecuenceId() {
		return this.sequenceId;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getActionId()
	 */
	public int getActionId() {
		return this.actionId;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getCreationTime()
	 */
	public Date getCreationTime() {
		return this.creationTime;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getServiceIdentifier()
	 */
	public int getServiceIdentifier() {
		return this.serviceId;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getFields()
	 */
	public Map<String, List<TraceFieldValue>> getFields() {
		return this.fields;
	}

}
