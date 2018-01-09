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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.SOAPPersistenceI.java.</p>
 * <b>Description:</b><p>This class provides all methods and constants for persistence operation associated to soap messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>19/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 19/07/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.util.Map;

/** 
 * <p>This class provides all methods and constants for persistence operation associated to soap messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 19/07/2011.
 */
public interface SOAPPersistenceI {
	
	/**
	 * Constants that represents the request soap type. 
	 */
	int REQUEST_SOAP_TYPE = 0;
	/**
	 * Constants that represents the response soap type. 
	 */
	int RESPONSE_SOAP_TYPE = 1;
	
	/**
	 * Gets the list of parameters used for configuring the class to provide the 'init' method.
	 * @return	list of parameters.
	 */
	String[] getConfigurationParameters();
	/**
	 * Initialize the class.
	 * @param configurationParams	Configuration parameters.
	 */
	void init (Map<String, String> configurationParams);
	
	/**
	 * Registers a SOAP message.
	 * @param soap	SOAP message as array of bytes.
	 * @param transactionId Audit transaction identifier.
	 * @param	soapType Soap type. The allowed values are:<br/>
	 * 	 {@link SOAPPersistenceI#REQUEST_SOAP_TYPE} if the SOAP is request.
	 * 	 {@link SOAPPersistenceI#RESPONSE_SOAP_TYPE} if the SOAP is response.
	 * @return	Unique identifier of record.
	 */
	String storeSOAP(byte[] soap,long transactionId, int soapType);
	
}
