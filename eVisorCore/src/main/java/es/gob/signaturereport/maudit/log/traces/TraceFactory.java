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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.TraceFactory.java.</p>
 * <b>Description:</b><p> Factory of trace elements.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit.log.traces;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.log.traces.impl.CommonTrace;
import es.gob.signaturereport.maudit.log.traces.impl.InitGenerationReportTrace;
import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Factory of trace elements.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public final class TraceFactory {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(TraceFactory.class);

	/*
	 * -- SPECIAL CHARACTERS --- 
	 */
	/**
	 * Attribute that represents the char used to separate key and value. 
	 */
	protected static final char KEY_VALUE_DELIMITER_CHAR = '=';

	/**
	 * Attribute that represents the char used to separate the fields. 
	 */
	protected static final char FIELD_DELIMITER_CHAR = ',';

	/**
	 * Attribute that represents the char used to start the field value. 
	 */
	protected static final char START_VALUE_CHAR = '(';

	/**
	 * Attribute that represents the char used to end the field value. 
	 */
	protected static final char END_VALUE_CHAR = ')';
	
	/**
	 * Constant that represents the character used to indicate the beginning of a key-value pair. 
	 */
	protected static final char START_FIELD_VALUE_CHAR='[';
	
	/**
	 * Constant that represents the character used to indicate the ending of a key-value pair. 
	 */
	protected static final char END_FIELD_VALUE_CHAR=']';

	/* 
	 * -- INTERNAL FIELDS
	 */

	/**
	 * Attribute that represents the sequence identifier field. 
	 */
	protected static final String SEQUENCE_ID = "SEQ_ID";

	/**
	 * Attribute that represents the action identifier. 
	 */
	protected static final String ACTION_ID = "ACT_ID";

	/**
	 * Attribute that represents the service identifier. 
	 */
	protected static final String SERVICE_ID = "SRV_ID";

	/**
	 * Attribute that represents the creation time. 
	 */
	protected static final String CREATION_TIME = "TIME";
	
	/**
	 * Attribute that represents the hash message of request or response. 
	 */
	protected static final String HASH_MESSAGE = "HM";

	/**
	 * Attribute that represents the format used to represent dates. 
	 */
	protected static final String DATE_FORMAT = UtilsTime.FORMATO_FECHA_ESTANDAR_ADICIONAL;

	/**
	 * Gets a trace object.
	 * @param sequenceId	Sequence identifier.
	 * @param actionId		Action identifier.
	 * @param serviceId		Service identifier.
	 * @param creationTime	Creation time.
	 * @param messageHash Message hash.
	 * @return				Trace object.
	 */
	public static TraceI getTraceElement(long sequenceId, int actionId, int serviceId, Date creationTime, String messageHash) {
		return getTraceImpl(sequenceId, actionId, serviceId, creationTime, new LinkedHashMap<String, List<TraceFieldValue>>(), messageHash);
	}

	/**
	 * Gets a trace object from a message includes into event log.
	 * @param message	Event message. The allowed format is:
	 * <br/>key_param_1=(param_value_1),key_param_2=(param_value_2),...,key_param_n=(param_value_n).
	 * @return	Trace object.
	 */
	public static TraceI getTraceElement(String message) {
		LinkedHashMap<String, List<TraceFieldValue>> fields = readMessage(message);
		String field = SEQUENCE_ID;
		List<TraceFieldValue> value = fields.remove(field);
		if (value == null || value.size()!=1) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { field }));
		} else {
			//LOGGER.debug(Language.getFormatMessage(LanguageKeys.AUD_005, new Object[ ] { field, value.get(0).getFieldValue() }));
			try {
				long seq = Long.parseLong(value.get(0).getFieldValue());
				field = ACTION_ID;
				value = fields.remove(field);
				if (value == null || value.size()!=1) {
					LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { field }));
				} else {
					int action = Integer.parseInt(value.get(0).getFieldValue());
					field = SERVICE_ID;
					value = fields.remove(field);
					if (value == null || value.size()!=1) {
						LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { field }));
					} else {
						int service = Integer.parseInt(value.get(0).getFieldValue());
						field = CREATION_TIME;
						value = fields.remove(field);
						if (value == null || value.size()!= 1) {
							LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { field }));
						} else {
							Date creationTime = UtilsTime.convierteFecha(value.get(0).getFieldValue(), DATE_FORMAT);
							return getTraceImpl(seq, action, service, creationTime, fields, null);
						}
					}

				}
			} catch (Exception e) {
				LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_004, new Object[ ] { field }), e);
			}
		}

		return null;
	}

	/**
	 * Gets the concrete trace implementation associated to supplied action. 
	 * @param sequenceId	Sequence identifier.
	 * @param actionId		Action identifier.
	 * @param serviceId		Service identifier.
	 * @param creationTime	Creation time.
	 * @param fields		Fields include in the event message.
	 * @param messageHashParam Message hash.
	 * @return	A {@link TraceI} element.
	 */
	private static TraceI getTraceImpl(long sequenceId, int actionId, int serviceId, Date creationTime, LinkedHashMap<String, List<TraceFieldValue>> fields, String messageHashParam) {
		TraceI traceImpl = null;
		switch (actionId) {
			case AuditManagerI.START_GENERATION_REPORT_ACT:
				traceImpl = new InitGenerationReportTrace(sequenceId, actionId, serviceId, creationTime, fields);
				break;
			default:
				traceImpl = new CommonTrace(sequenceId, actionId, serviceId, creationTime, fields, messageHashParam);
				break;
		}
		return traceImpl;
	}

	/**
	 * Constructor method for the class TraceFactory.java. 
	 */
	private TraceFactory() {
	}

	/**
	 * Read a message event. The format is:
	 * <br/> key_param_1=(param_value_1),key_param_2=(param_value_2),...,key_param_n=(param_value_n).
	 * <br/> The param_value may be:
	 * <br/> 1. Simple string. Ej (value)
	 * <br/> 2. List of String. Ej: ([value1],[value2],[value3]).
	 * <br/> 3. Map of String. Ej: ([key1=value1],[key2=value2]).
	 * <br/> 4. Combination of 2 and 3. ([value1],[key1=value1],[key2=value2]).
	 * @param message	Message included into event log.
	 * @return Map that contains the fields included into the supplied message.
	 */
	private static LinkedHashMap<String, List<TraceFieldValue>> readMessage(String message) {
		LinkedHashMap<String, List<TraceFieldValue>> params = new LinkedHashMap<String, List<TraceFieldValue>>();
		if (message != null) {
			String msg = message.trim();
			for (int i = 0; i < msg.length(); i++) {
				String key = "";
				String value = "";
				while (i < msg.length() && msg.charAt(i) != KEY_VALUE_DELIMITER_CHAR) {
					key = key + msg.charAt(i);
					i++;
				}
				i++;
				if (i < msg.length() && msg.charAt(i) == START_VALUE_CHAR) {
					i++;
					boolean foundEndValue = false;
					while (i < msg.length() && !foundEndValue) {
						foundEndValue = msg.charAt(i) == END_VALUE_CHAR;
						// Para ser el ultimo token debe continuar con ',' o ser
						// final de cadena
						foundEndValue = foundEndValue && ((i + 1) == msg.length() || msg.charAt(i + 1) == FIELD_DELIMITER_CHAR);
						if (!foundEndValue) {
							value = value + msg.charAt(i);
							i++;
						}
					}
				}
				i++;
				if (!key.equals("") && !value.equals("")) {
					List<TraceFieldValue>  traceParams = getTraceFieldValue(value);
					if(!traceParams.isEmpty()){
						params.put(key, traceParams);
					}
					
				}
			}
		}
		return params;
	}

	/**
	 * Gets all parameters include into the value of trace fields.
	 * @param fieldValue	Value of a field. The content may be:
	 * <br/> 1. A single string.
	 * <br/> 2. A list of string. Ej: [param1],[param2],[param3].
	 * <br/> 3. Map of string. Ej:[param1=param1_value],[param2=param2_value].
	 * <br/> 4. Combination of 2 and 3. ([value1],[key1=value1],[key2=value2]).
	 * @return List of parameters that make up the field value.
	 */
	private static List<TraceFieldValue> getTraceFieldValue(String fieldValue) {
		List<TraceFieldValue> value = new ArrayList<TraceFieldValue>();
		if (fieldValue.charAt(0) == START_FIELD_VALUE_CHAR) {
			for (int i = 1; i < fieldValue.length(); i++) {
				while (i < fieldValue.length() && fieldValue.charAt(i) != START_FIELD_VALUE_CHAR) {
					i++;
				}
				if (i < fieldValue.length() && fieldValue.charAt(i) == START_FIELD_VALUE_CHAR) {
					i++;
					String param = "";
					String paramValue = null;
					boolean endParam = false;

					while (i < fieldValue.length() && !endParam) {
						// El primer parametro termina con:
						// 1. '=' (Tipo Map)
						// 2. ']' (Tipo List). Para ser el ultimo token debe
						// continuar con ',' o ser final de cadena

						endParam = fieldValue.charAt(i) == KEY_VALUE_DELIMITER_CHAR;
						endParam = endParam || (fieldValue.charAt(i) == END_FIELD_VALUE_CHAR && ((i + 1) == fieldValue.length() || fieldValue.charAt(i + 1) == FIELD_DELIMITER_CHAR));
						if (!endParam) {
							param += fieldValue.charAt(i);
							i++;
						}
					}
					if (i < fieldValue.length() && fieldValue.charAt(i) == KEY_VALUE_DELIMITER_CHAR) {
						paramValue = "";
						boolean endValue = false;
						i++;
						while (i < fieldValue.length() && !endValue) {
							endValue = fieldValue.charAt(i) == END_FIELD_VALUE_CHAR;
							// Para ser el ultimo token debe continuar con ',' o
							// ser final de cadena
							endValue = endValue && ((i + 1) == fieldValue.length() || fieldValue.charAt(i + 1) == FIELD_DELIMITER_CHAR);
							if (!endValue) {
								paramValue += fieldValue.charAt(i);
								i++;
							}
						}
					}
					if (paramValue == null) {
						//Es del tipo [value]
						value.add(new TraceFieldValue(param,null));
					}else{
						//Es de tipo Map [paramType=paramValue]
						value.add(new TraceFieldValue(paramValue,param));
					}

				}

			}
		} else {
			value.add(new TraceFieldValue(fieldValue, null));
		}
		return value;
	}

}
