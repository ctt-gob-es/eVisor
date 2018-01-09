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
 * <b>File:</b><p>es.gob.signaturereport.ws.handler.StartAuditTransactionHandler.java.</p>
 * <b>Description:</b><p> Class that start a audit transaction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.ws.handler;

import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;

import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.maudit.AuditManagerI;


/** 
 * <p>Class that start a audit transaction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class StartAuditTransactionHandler extends AuditTransactionHandler{

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = 1885772584191895407L;

	
	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		
		String encodedHash = getEncodedHash(messageContext);
			
		long sequenceId = AuditManager.getInstance().openTransaction(getServiceIdentifier(messageContext), encodedHash);
			
		AxisEngine.getCurrentMessageContext().setProperty(AuditManagerI.SEQUENCE_ID_CONTEXT, String.valueOf(sequenceId));
			
	}
	
	


}
