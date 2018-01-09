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
 * <b>File:</b><p>es.gob.signaturereport.modes.SignatureReportGeneration.java.</p>
 * <b>Description:</b><p>Class that provides methods to generation of signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.modes;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.mfirma.SignatureManager;
import es.gob.signaturereport.mfirma.SignatureManagerException;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.AfirmaResponse;
import es.gob.signaturereport.modes.parameters.Barcode;
import es.gob.signaturereport.modes.parameters.ReportGenerationRequest;
import es.gob.signaturereport.modes.parameters.RepositoryLocation;
import es.gob.signaturereport.modes.responses.ReportGenerationResponse;
import es.gob.signaturereport.mreport.ReportException;
import es.gob.signaturereport.mreport.ReportManagerFactory;
import es.gob.signaturereport.mreport.ReportManagerI;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.properties.SignatureReportPropertiesI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/**
 * <p>Class that provides methods to generation of signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class SignatureReportGeneration {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(SignatureReportGeneration.class);

	/**
	 * Attribute that represents the property that contains the signature format for PDF files.
	 */
	private static final String PDF_FORMAT_PROPERTY = "signaturereport.global.pdfformat";
	
	@Inject
	private static SignatureReportPropertiesI signatureReportProperties;
	/**
	 * Attribute that represents the signature format for PDF files.
	 */
	private static final String PDF_FORMAT = StaticSignatureReportProperties.getProperty(PDF_FORMAT_PROPERTY);
	

	/**
	 * Constructor method for the class SignatureReportGeneration.java.
	 */
	public SignatureReportGeneration() {

	}

	
	
	/**
	 * This method creates a signature report.
	 * @param sequenceId	Sequence identifiers.
	 * @param parameters	Input parameters
	 * @return	ReportGenerationResponse that contains the process result.
	 */
	public ReportGenerationResponse createReport(long sequenceId, ReportGenerationRequest parameters){
		if(parameters==null){
			ReportGenerationResponse response = new ReportGenerationResponse();
			response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
			response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_001));
			return response;
		}
		return generateReport(sequenceId, parameters.getApplicationId(), parameters.getTemplateId(), parameters.getSignature(),parameters.getExternalSignature(), parameters.getAfirmaResponse(), parameters.getDocument(), parameters.isServerSign(), parameters.getExternalDocument(), parameters.getBarcodes(), parameters.getAdditionalParameters());
	}
	/**
	 * This method creates a signature report.
	 * @param sequenceId	Sequence identifier.
	 * @param applicationId	Identifier of application.
	 * @param templateId	Identifier of template.
	 * @param signature		Digital signature that to be used to generate report.
	 * @param externalSignature Location of a electronic signature into a external repository.
	 * @param afResponse 	Verify Signature Request returned by '@firma' platform.
	 * @param document		Document signed. Optional.
	 * @param serverSign	Optional property, if this property is true then the report will be signed.
	 * @param externalDocument 	Location of a signed document into a external repository.
	 * @param barcodes		Bar codes to include into signature report.
	 * @param additionalParameters	Additional parameters included in the request. Optional.
	 * @return	ReportGenerationResponse that contains the process result.
	 */
	private ReportGenerationResponse generateReport(long sequenceId,String applicationId, String templateId, byte[ ] signature, RepositoryLocation externalSignature, AfirmaResponse afResponse, byte[ ] document, boolean serverSign, RepositoryLocation externalDocument,ArrayList<Barcode> barcodes ,LinkedHashMap<String, String> additionalParameters) {

		ReportGenerationResponse response = new ReportGenerationResponse();

		// Validamos los parámetros de entrada
		if (applicationId == null || templateId == null || signature == null && externalSignature == null && afResponse == null) {
			response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
			if (applicationId == null) {
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_001));
			} else if (templateId == null) {
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_002));
			} else {
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_003));
			}
			return response;
		}


		// Obtenemos los datos de la aplicación
		ApplicationData appData = null;
		try {
			appData = ConfigurationFacade.getInstance().getApplicationData(applicationId);
		} catch (ConfigurationException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.FA_GEN_005, new Object[ ] { applicationId }), e);
			if (e.getCode() == ConfigurationException.ITEM_NO_FOUND) {
				response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
				response.setMessage(Language.getFormatMessage(LanguageKeys.FA_GEN_004, new Object[ ] { applicationId }));
			} else {
				response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
			}
			return response;
		}

		if (appData == null) {
			LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_007));
			response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
			response.setMessage(Language.getFormatMessage(LanguageKeys.FA_GEN_004, new Object[ ] { applicationId }));
			return response;
		}

		// Verificamos que la plantilla está soportada por la aplicación
		if (!appData.getTemplates().contains(templateId)) {
			response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
			response.setMessage(Language.getFormatMessage(LanguageKeys.FA_GEN_008, new Object[ ] { templateId }));
			return response;
		}


		ValidationSignatureResponse valResponse = null;
		if (afResponse != null) {
			//leemos la respuesta de validación incluida en la petición
			try {
				valResponse = readResponse(appData.getPlatformId(),afResponse);
			} catch (SignatureManagerException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_013),e);
				if(e.getCode()==SignatureManagerException.INVALID_SOAP_SIGNATURE){
					response.setCode(ReportGenerationResponse.INVALID_SOAP_SIGNATURE);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_014));
					return response;
				}else if(e.getCode()==SignatureManagerException.XML_RESPONSE_ERROR){
					response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_015));
					return response;
				} else {
					response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
					return response;
				}
			}
		} else {
			// Validamos la firma electrónica
			try {
				valResponse = validateSignature(appData.getPlatformId(), signature, externalSignature, document, externalDocument);
			} catch (SignatureManagerException e) {
				// Error al validar la firma electrónica
				LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_012) + e.getDescription());
				if (e.getCode() == SignatureManagerException.NOT_SUPPORTED) {
					response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_011));
					return response;
				}else if(e.getCode()== SignatureManagerException.AFIRMA_INVOKER_ERROR){
					response.setCode(ReportGenerationResponse.AFIRMA_UNAVAILABLE);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_029));
					return response;
				}else {
					response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
					response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
					return response;
				}
			}
		}
		if(valResponse == null){
			LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_016));
			response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
			response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
			return response;
		}

		//Proceso de Generación del Informe

		//1.Obtenemos la información de la plantilla
		TemplateData template = null;
		try {
			template = ConfigurationFacade.getInstance().getTemplate(templateId);
		} catch (ConfigurationException e) {
			LOGGER.error(Language.getFormatMessage(LanguageKeys.FA_GEN_017, new Object[ ] { templateId }), e);
			if (e.getCode() == ConfigurationException.ITEM_NO_FOUND) {
				response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
				response.setMessage(Language.getFormatMessage(LanguageKeys.FA_GEN_018, new Object[ ] { templateId }));
			} else {
				response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
			}
			return response;
		}
		if(template == null){
			LOGGER.error(Language.getFormatMessage(LanguageKeys.FA_GEN_019, new Object[ ] { templateId }));
			response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
			response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
			return response;
		}else{
			//Guardamos en auditorña la información de la plantilla
			TraceI traceOutputs = TraceFactory.getTraceElement(sequenceId, AuditManager.TEMPLATE_INFORMATION_ACT, AuditManager.GENERATION_REPORT_SRVC, new Date(), null);
			traceOutputs.addField(TraceI.TEMPLATE_TYPE, template.getReportType());
			AuditManager.getInstance().addTrace(traceOutputs);
		}
		//2. Una vez obtenida la plantilla procedemos a generar el informe.
		byte[] report = null;
		try {
			ReportManagerI reportManager = ReportManagerFactory.getReportManager(template.getReportType());
			report = reportManager.createReport(valResponse, template, signature, document,barcodes,additionalParameters);
		} catch (ReportException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_020),e);
			if(e.getCode()==ReportException.INVALID_SIGNATURE){
				response.setCode(ReportGenerationResponse.INVALID_SIGNATURE);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_021));
				return response;
			}else if(e.getCode()==ReportException.INVALID_PAGE_NUMBER || e.getCode()==ReportException.OVER_MAX_PAGE_NUMBER){
				response.setCode(ReportGenerationResponse.INVALID_PAGE_NUMBER);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_022));
				return response;
			}else if(e.getCode()==ReportException.INVALID_TEMPLATE){
				response.setCode(ReportGenerationResponse.INVALID_TEMPLATE);
				response.setMessage(Language.getFormatMessage(LanguageKeys.FA_GEN_025,new Object[]{templateId}));
				return response;
			}else if(e.getCode() == ReportException.INVALID_INPUT_PARAMETERS){
				response.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
				response.setMessage(e.getDescription());
				return response;
			}else{
				response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_006));
				return response;
			}
		}

		//3. En caso de indicarse firmante se procede a la firma del report
		if(report!=null && serverSign){
			SignatureManager signManager = new SignatureManager();
			try {
				String signatureFormat = null;
				if(template.getReportType()== TemplateConfigurationFacadeI.PDF_REPORT){
					signatureFormat = PDF_FORMAT;
				}
				
				report = signManager.serverSign( report,signatureFormat);
			} catch (SignatureManagerException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.FA_GEN_028),e);
				response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
				response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_009));
				return response;
			}
		}

		if(report != null){
			response.setCode(ReportGenerationResponse.PROCESS_OK);
			response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_027));
			response.setReport(report);
			return response;
		}else{
			response.setCode(ReportGenerationResponse.UNKNOWN_ERROR);
			response.setMessage(Language.getMessage(LanguageKeys.FA_GEN_026));
			return response;
		}

	}

	/**
	 * This method does the process of validating an electronic signature.
	 * @param platformId	Identifier that represents a "@firma configuration".
	 * @param signature		Digital signature that to be checked.
	 * @param externalSignature	Location of a electronic signature into a external repository.
	 * @param document		Signed document.
	 * @param externalDocument	Location of a signed document into a external repository.
	 * @return			Results of validation process.
	 * @throws SignatureManagerException	If an error occurs.
	 */
	private ValidationSignatureResponse validateSignature(String platformId, byte[ ] signature, RepositoryLocation externalSignature, byte[ ] document, RepositoryLocation externalDocument) throws SignatureManagerException {
		SignatureManager signManager = new SignatureManager();
		String uuidSig = null;
		String repositoryIdSig = null;
		String uuidDoc = null;
		String repositoryIdDoc = null;
		if(externalSignature!=null){
			uuidSig = externalSignature.getUuid();
			repositoryIdSig = externalSignature.getRepositoryId();
		}
		if(externalDocument !=null){
			uuidDoc = externalDocument.getUuid();
			repositoryIdDoc = externalDocument.getRepositoryId();
		}
		return signManager.validateSignature(platformId, signature, uuidSig, repositoryIdSig, document, uuidDoc, repositoryIdDoc);
	}

	/**
	 * This method processes a validation response of  '@firma' to extract the information contained in the message supplied.
	 * @param platformId	Identifier of '@firma' platform.
	 * @param response		Web service response returned by '@firma' platform.
	 * @return			{@link ValidationSignatureResponse} that contains the verification information.
	 * @throws SignatureManagerException	If an error occurs while reading response.
	 */
	private ValidationSignatureResponse readResponse(String platformId,AfirmaResponse response) throws SignatureManagerException{
		SignatureManager signManager = new SignatureManager();
		return signManager.readValidateResponse(platformId,response);
	}
}
