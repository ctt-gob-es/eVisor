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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.traces.impl.InitGenerationReportTrace.java.</p>
 * <b>Description:</b><p>Class that represent a trace of start generation report process.The content of trace is:
 * <br/> SEQ_ID=($SEQUENCEID),ACT_ID=({@link AuditManagerI#START_GENERATION_REPORT_ACT}),SRV_ID=({@link AuditManagerI#GENERATION_REPORT_SRVC}),TIME=($CREATIONTIME),$LIST_FIELDS.
 * <br/> The LISTFIELDS are:
 * <br/> APP_ID=($APLICATIONID),TEMPL_ID=($TEMPLATEID),IS_SIGN=(true/false),EXT_SIGN=([$REPOSITORY=$UUID]),EXT_DOC=([$REPOSITORY=$UUID]),BARCODES=([$BARTYPE1=$MESSAGE1],[$BARTYPE2=$MESSAGE2]),EXT_PARAMS=([$PARAM1=$VALUE1],[$PARAM2=$VALUE2])</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.maudit.log.traces.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.log.traces.ATrace;
import es.gob.signaturereport.maudit.log.traces.TraceFieldValue;
import es.gob.signaturereport.modes.parameters.Barcode;
import es.gob.signaturereport.modes.parameters.RepositoryLocation;

/** 
 * <p>Class that represent a trace of start generation report process. The content of trace is:
 * <br/> SEQ_ID=($SEQUENCEID),ACT_ID=({@link AuditManagerI#START_GENERATION_REPORT_ACT}),SRV_ID=({@link AuditManagerI#GENERATION_REPORT_SRVC}),TIME=($CREATIONTIME),$LIST_FIELDS.
 * <br/> The LISTFIELDS are:
 * <br/> APP_ID=($APLICATIONID),TEMPL_ID=($TEMPLATEID),IS_SIGN=(true/false),EXT_SIGN=([$REPOSITORY=$UUID]),EXT_DOC=([$REPOSITORY=$UUID]),BARCODES=([$BARTYPE1=$MESSAGE1],[$BARTYPE2=$MESSAGE2]),EXT_PARAMS=([$PARAM1=$VALUE1],[$PARAM2=$VALUE2])</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class InitGenerationReportTrace extends ATrace {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(InitGenerationReportTrace.class);

	/**
	 * Constructor method for the class InitGenerationReportTrace.java.
	 * @param sequenceId	Event transaction identifier.
	 * @param actionId 		Action identifier.
	 * @param serviceId 	Service identifier.
	 * @param creationDate 	Creation time.
	 * @param messageFields 		Trace fields.
	 */
	public InitGenerationReportTrace(long sequenceId, int actionId, int serviceId, Date creationDate, LinkedHashMap<String, List<TraceFieldValue>> messageFields) {
		super(sequenceId, actionId, serviceId, creationDate, messageFields, null);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#addField(java.lang.String, java.lang.Object)
	 */
	public void addField(String fieldIdentifier, Object fieldValue) {
		try {
			ArrayList<TraceFieldValue> fieldValueParams = new ArrayList<TraceFieldValue>();

			if ((fieldIdentifier.equals(EXT_SIGN) || fieldIdentifier.equals(EXT_DOC))) {
				RepositoryLocation location = (RepositoryLocation) fieldValue;
				TraceFieldValue param = new TraceFieldValue(location.getUuid(), location.getRepositoryId());
				fieldValueParams.add(param);
			} else if (fieldIdentifier.equals(BARCODES)) {
				ArrayList<Barcode> barcodes = (ArrayList<Barcode>) fieldValue;
				for (int i = 0; i < barcodes.size(); i++) {
					Barcode barcode = (Barcode) barcodes.get(i);
					TraceFieldValue param = new TraceFieldValue(barcode.getMessage(), barcode.getType());
					fieldValueParams.add(param);
				}
			} else if (fieldIdentifier.equals(EXT_PARAMS)) {
				LinkedHashMap<String, String> extParams = (LinkedHashMap<String, String>) fieldValue;
				Iterator<String> keys = extParams.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = extParams.get(key);
					TraceFieldValue param = new TraceFieldValue(value, key);
					fieldValueParams.add(param);
				}
			} else {
				TraceFieldValue param = new TraceFieldValue(fieldValue.toString(), null);
				fieldValueParams.add(param);
			}
			addMessageField(fieldIdentifier, fieldValueParams);
		} catch (ClassCastException ce) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_012, new Object[ ] { fieldIdentifier }), ce);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.traces.TraceI#getFieldValue(java.lang.String)
	 */
	public Object getFieldValue(String identifier) {
		List<TraceFieldValue> params = getMessageField(identifier);
		if (params != null && params.size() > 0) {
			if (identifier.equals(EXT_SIGN) || identifier.equals(EXT_DOC)) {
				return new RepositoryLocation(params.get(0).getFieldValue(), params.get(0).getFieldValueType());
			} else if (identifier.equals(BARCODES)) {
				ArrayList<Barcode> barcodes = new ArrayList<Barcode>();
				for (int i = 0; i < params.size(); i++) {
					Barcode barcode = new Barcode(params.get(i).getFieldValueType(), params.get(i).getFieldValue());
					barcodes.add(barcode);
				}
				return barcodes;
			} else {
				return params.get(0).getFieldValue();
			}
		}
		return null;
	}

}
