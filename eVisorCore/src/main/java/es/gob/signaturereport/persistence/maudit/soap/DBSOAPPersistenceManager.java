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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.DBSOAPPersistenceManager.java.</p>
 * <b>Description:</b><p>Class that manages the persistence operations in database of the SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>19/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 19/07/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ISoapsBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.SoapsPOJO;
import es.gob.signaturereport.tools.UniqueNumberGenerator;
import es.gob.signaturereport.tools.UtilsBase64;

/**
 * <p>Class that manages the persistence operations in database of the SOAP messages.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 19/07/2011.
 */
public class DBSOAPPersistenceManager implements SOAPPersistenceI {
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(DBSOAPPersistenceManager.class);
	
	/**
	 * Attribute that represents the hash algorithm used to digest the SOAP message. 
	 */
	private static final String HASH_ALGORITHM  = "SHA1" ;

	/**
	 * Attribute that represents the class tool for base 64 encoding. 
	 */
	private UtilsBase64 base64Encoder = new UtilsBase64();
	
	/**
	 *  Attribute that allows to operate with information of the SOAP messages of the platform.
	 */
	@Inject
	private ISoapsBO soapsBO;

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#init(java.util.Map)
	 */
	public void init(Map<String, String> configurationParams) {
		
	}
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#getConfigurationParameters()
	 */
	public String[ ] getConfigurationParameters() {
		return null;
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.maudit.soap.SOAPPersistenceI#storeSOAP(byte[], long, int)
	 */
	public String storeSOAP(byte[ ] soap, long transactionId, int soapType) {
		

		try {
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] hash = md.digest(soap);
			String encodedHash = base64Encoder.encodeBytes(hash);
			long soapPK = UniqueNumberGenerator.getInstance().getNumber();
						
			final SoapsPOJO soapPOJO = new SoapsPOJO();
			soapPOJO.setSoapPk(soapPK);
			soapPOJO.setSoapType(soapType);
			soapPOJO.setTransaction(transactionId);
			soapPOJO.setHashAlgorithm(HASH_ALGORITHM);
			soapPOJO.setHashValue(encodedHash);
			soapPOJO.setCreationTime(new Date());
			
			soapsBO.save(soapPOJO);
			
			return String.valueOf(soapPK);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.AUD_009),e);
		} catch (DatabaseException e){
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			LOGGER.error(Language.getMessage(LanguageKeys.AUD_008),e);
			
		}
		return null;
	}
}
