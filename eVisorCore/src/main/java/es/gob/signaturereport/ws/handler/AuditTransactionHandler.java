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
 * <b>File:</b><p>es.gob.signaturereport.ws.handler.AuditTransactionHandler.java.</p>
 * <b>Description:</b><p> Class that represents a web service handler for a audit operation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.ws.handler;


import java.security.MessageDigest;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.tools.UtilsBase64;
import es.gob.signaturereport.tools.XMLUtils;


/** 
 * <p>Class that represents a web service handler for a audit operation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
abstract class AuditTransactionHandler extends BasicHandler {


	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -1443415206748255947L;

	/**
	 * Attribute that represents the name of generate report operation. 
	 */
	private static final String GENERATE_REPORT = "generateReport";
	
	/**
	 * Attribute that represents the name of validate report operation. 
	 */
	private static final String VALIDATE_REPORT = "validateReport";
	
	/**
	 * Attribute that represents the hash algorithm used to digest the SOAP message. 
	 */
	protected static final String HASH_ALGORITHM  = "SHA256" ;
	
	
	/**
	 * Gets the service identifier associated to the request operation.
	 * @param messageContext	Message context.
	 * @return	Service identifier.
	 * @throws AxisFault	If an error occurs.
	 */
	public int getServiceIdentifier(MessageContext messageContext) throws AxisFault{
		String operation = messageContext.getOperation().getName();
		if(operation!=null){
				if(operation.equals(GENERATE_REPORT)){
				return AuditManager.GENERATION_REPORT_SRVC;
			}else if(operation.equals(VALIDATE_REPORT)){
				return AuditManager.VALIDATION_REPORT_SRVC;
			}else{
				throw new AxisFault(Language.getMessage(LanguageKeys.MSG_WS_029));
			}
		}else{
			throw new AxisFault(Language.getMessage(LanguageKeys.MSG_WS_029));
		}
	}

	/**
	 * Method that returns the base 64 encoded soap message.
	 * @param messageContext messsage context.
	 * @return the base 64 encoded soap message.
	 */
	public String getEncodedHash(MessageContext messageContext) {
		String encodedHash = null;
		try {
			byte[ ] soap = XMLUtils.getSOAP(messageContext.getMessage());
			
			UtilsBase64 base64Encoder = new UtilsBase64();
			
			MessageDigest md= MessageDigest.getInstance(HASH_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
			
			byte[] hash = md.digest(soap);
			encodedHash = base64Encoder.encodeBytes(hash);
		} catch (Exception e) {
			return null;
		}
		return encodedHash;
	}
}
