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
 * <b>File:</b><p>es.gob.signaturereport.modes.SignatureReportValidation.java.</p>
 * <b>Description:</b><p> Class that provides method to validate a signed report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>24/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 24/05/2011.
 */
package es.gob.signaturereport.modes;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI;
import es.gob.signaturereport.mfirma.SignatureManager;
import es.gob.signaturereport.mfirma.SignatureManagerException;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.RepositoryLocation;
import es.gob.signaturereport.modes.responses.ReportValidationResponse;
import es.gob.signaturereport.persistence.exception.ConfigurationException;


/** 
 * <p>Class that provides method to validate a signed report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 24/05/2011.
 */
public class SignatureReportValidation {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(SignatureReportValidation.class);

	/**
	 * Constructor method for the class SignatureReportValidation.java. 
	 */
	public SignatureReportValidation() {
	}
	
	/**
	 * Method that validates a signed report.
	 * @param applicationId	Application identifier.
	 * @param report	Report. Must be signed.
	 * @param externalReport Location of a report into a external repository.
	 * @return	A {@link ReportValidationResponse} object that contains the result o process.
	 */
	public ReportValidationResponse validateReport(String applicationId, byte[] report, RepositoryLocation externalReport){
		if(applicationId == null){
			return new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.FA_VAL_001));
		}else if((report == null && externalReport == null) || (report == null && externalReport!=null && (externalReport.getRepositoryId()==null || externalReport.getUuid()== null)) ){
			return new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.FA_VAL_002));
		}else{
			// Obtenemos los datos de la aplicaci�n
			ApplicationData appData = null;
			try {
				appData = ConfigurationFacade.getInstance().getApplicationData(applicationId);
			} catch (ConfigurationException e) {
				LOGGER.error(Language.getFormatMessage(LanguageKeys.FA_VAL_003, new Object[ ] { applicationId }), e);
				if (e.getCode() == ConfigurationException.ITEM_NO_FOUND) {
					return new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.FA_VAL_004, new Object[ ] { applicationId }));
				} else {
					return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.FA_VAL_005));
				}		
			}
			if (appData == null) {
				return new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.FA_VAL_004, new Object[ ] { applicationId }));
			}
			//Validaci�n de la firma electr�nica
			SignatureManager signManager = new SignatureManager();
			String uuidSig = null;
			String repositoryIdSig = null;

			if(externalReport!=null){
				uuidSig = externalReport.getUuid();
				repositoryIdSig = externalReport.getRepositoryId();
			}
			try {
				ValidationSignatureResponse valResult = signManager.validateSignature(appData.getPlatformId(), report, uuidSig, repositoryIdSig, null, null, null);
				return readResponse(valResult);
			} catch (SignatureManagerException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.FA_VAL_006),e);
				if (e.getCode() == SignatureManagerException.NOT_SUPPORTED) {
					return new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.FA_VAL_007));
				} else if(e.getCode()==SignatureManagerException.AFIRMA_INVOKER_ERROR){
					return new ReportValidationResponse(ReportValidationResponse.AFIRMA_UNAVAILABLE, Language.getMessage(LanguageKeys.FA_VAL_014));
				}else {
					return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.FA_VAL_005));
				}
			}
		}
	}
	
	/**
	 * Method that extract the validation information returned from "@firma".
	 * @param validationResponse	Validation information returned from "@firma".
	 * @return	Validation result.
	 */
	private ReportValidationResponse readResponse(ValidationSignatureResponse validationResponse){
		if(validationResponse != null && validationResponse.getValidationInfo()!=null && !validationResponse.getValidationInfo().isEmpty() ){
			String rm = (String) validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MAJOR_RES);
			if(rm==null){
				return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.FA_VAL_009));
			}else if(rm.equals(DSSVerifyTransformI.VALID_SIGNATURE)){
					return new ReportValidationResponse(ReportValidationResponse.VALID_SIGNATURE, Language.getMessage(LanguageKeys.FA_VAL_010));
			}else if(rm.equals(DSSVerifyTransformI.WARNING_SIGNATURE)){
				String cause = (String) validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MSG_RES);
				if(cause==null){
					return new ReportValidationResponse(ReportValidationResponse.WARNING_SIGNATURE, Language.getMessage(LanguageKeys.FA_VAL_011));
				}else{
					return new ReportValidationResponse(ReportValidationResponse.WARNING_SIGNATURE, Language.getMessage(LanguageKeys.FA_VAL_011),cause);
				}	
			}else if(rm.equals(DSSVerifyTransformI.INVALID_SIGNATURE)){
				String cause = (String) validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MSG_RES);
				if(cause==null){
					return new ReportValidationResponse(ReportValidationResponse.INVALID_SIGNATURE, Language.getMessage(LanguageKeys.FA_VAL_012));
				}else{
					return new ReportValidationResponse(ReportValidationResponse.INVALID_SIGNATURE, Language.getMessage(LanguageKeys.FA_VAL_012),cause);
				}
			}else{
				String cause = (String) validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MSG_RES);
				if(cause==null){
					return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_STATUS, Language.getMessage(LanguageKeys.FA_VAL_013));
				}else{
					return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_STATUS, Language.getMessage(LanguageKeys.FA_VAL_013),cause);
				}
			}
		}else{
			return new ReportValidationResponse(ReportValidationResponse.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.FA_VAL_008));
		}
	}
}
