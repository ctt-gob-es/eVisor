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
 * <b>File:</b><p>es.gob.signaturereport.ws.handler.CustodyResponseHandler.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.ws.handler;

import java.util.Date;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditException;
import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class CustodyResponseHandler extends AuditTransactionHandler{

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 669075474938060243L;

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(CustodyResponseHandler.class);
	
	
	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		if(AuditManager.getInstance().isCustodyResponse()){
			try {
				byte[] soap = XMLUtils.getSOAP(messageContext.getMessage());
				String sequenceId = (String) AxisEngine.getCurrentMessageContext().getProperty(AuditManagerI.SEQUENCE_ID_CONTEXT);
				int service = getServiceIdentifier(messageContext);
				if(sequenceId==null){
					long seqId = AuditManager.getInstance().openTransaction(service, null);
					sequenceId = String.valueOf(seqId);
					AxisEngine.getCurrentMessageContext().setProperty(AuditManagerI.SEQUENCE_ID_CONTEXT, sequenceId);
				}
				String storeId = AuditManager.getInstance().storeResponseMessage(soap,Long.parseLong(sequenceId));
				TraceI trace = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManagerI.STORE_RESPONSE_ACT, service, new Date(), null);
				trace.addField(TraceI.RESPONSE_ID, storeId);
				AuditManager.getInstance().addTrace(trace);
			} catch (ToolsException e) {
				String msg = Language.getMessage(LanguageKeys.MSG_WS_031);
				LOGGER.error(msg + e.getDescription());
				throw new AxisFault(msg,e);
			} catch (AuditException e) {
				String msg = Language.getMessage(LanguageKeys.MSG_WS_032);
				LOGGER.error(msg + e.getDescription());
				throw new AxisFault(msg,e);
			}
		}
		
	}

}
