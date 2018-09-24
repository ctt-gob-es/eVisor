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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.FileSOAPPersistenceManager.java.</p>
 * <b>Description:</b><p>Class for storing the SOAP message in the disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/10/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/10/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.util.Map;

import javax.annotation.ManagedBean;

/** 
 * <p>Class for storing the SOAP message in the disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/10/2011.
 */
@ManagedBean
@FileSOAPPersistence
public class FileSOAPPersistenceManager implements SOAPPersistenceI {

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#getConfigurationParameters()
	 */
	public String[ ] getConfigurationParameters() {
		return FileSOAPController.PARAMETERS;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#init(java.util.Map)
	 */
	public void init(Map<String, String> configurationParams) {
		FileSOAPController.getInstance().init(configurationParams);
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#storeSOAP(byte[], long, int)
	 */
	public String storeSOAP(byte[ ] soap, long transactionId, int soapType) {
		return FileSOAPController.getInstance().storeSOAP(soap, transactionId, soapType);
	}

	
}
