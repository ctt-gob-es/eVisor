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
 * <b>File:</b><p>es.gob.signaturereport.ws.SignatureReportService.java.</p>
 * <b>Description:</b><p> Class that publishes services for generation and validation signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/04/2017.</p>
 * @author Spanish Government.
 * @version 1.1, 18/04/2017.
 */
package es.gob.signaturereport.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.axis.AxisEngine;
import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.log.traces.TraceFactory;
import es.gob.signaturereport.maudit.log.traces.TraceI;
import es.gob.signaturereport.messages.transform.TransformFactory;
import es.gob.signaturereport.messages.transform.TransformI;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.messages.transform.impl.GenerationReportTransformI;
import es.gob.signaturereport.messages.transform.impl.ValidationReportTransformI;
import es.gob.signaturereport.modes.SignatureReportGeneration;
import es.gob.signaturereport.modes.SignatureReportValidation;
import es.gob.signaturereport.modes.parameters.AfirmaResponse;
import es.gob.signaturereport.modes.parameters.Barcode;
import es.gob.signaturereport.modes.parameters.ReportGenerationRequest;
import es.gob.signaturereport.modes.parameters.RepositoryLocation;
import es.gob.signaturereport.modes.responses.ReportGenerationResponse;
import es.gob.signaturereport.modes.responses.ReportValidationResponse;
import es.gob.signaturereport.tools.UtilsBase64;

/**
 * <p>Class that publishes services for generation and validation signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.1, 18/04/2017.
 */
public class SignatureReportService {

	/**
	 * Attribute that represents the base 64 tool class. 
	 */
	private final UtilsBase64 base64 = new UtilsBase64();
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(SignatureReportService.class);

	/**
	 * Method that provides to generate signature report.
	 * @param xmlRequest String that represents a XML request of report generation.
	 * @return	XML that contains the result of the process.
	 */
	public String generateReport(String xmlRequest) {

		LOGGER.info(Language.getMessage(LanguageKeys.MSG_WS_001));
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.MSG_WS_003, new Object[ ] { xmlRequest }));
		ReportGenerationResponse reportResponse = null;

		String sequenceId = (String) AxisEngine.getCurrentMessageContext().getProperty(AuditManagerI.SEQUENCE_ID_CONTEXT);
		if (sequenceId == null) {
			long seqId = AuditManager.getInstance().openTransaction(AuditManager.GENERATION_REPORT_SRVC, null);
			sequenceId = String.valueOf(seqId);
			AxisEngine.getCurrentMessageContext().setProperty(AuditManagerI.SEQUENCE_ID_CONTEXT, sequenceId);
		}
		// Obtenemos el objeto para registrar la información de auditoria
		// asociada a la petición
		TraceI traceInputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManager.START_GENERATION_REPORT_ACT, AuditManager.GENERATION_REPORT_SRVC, new Date(), null);

		// 1.Obtenemos la información contenida en la petición
		try {
			TransformI transform = TransformFactory.getTransform(ConfigurationFacade.GENERATION_REPORT_SERVICE, null, ConfigurationFacade.SIGNATURE_REPORT_PLATFORM);
			LinkedHashMap<String, Object> parameters = null;
			try {
				parameters = transform.unmarshal(xmlRequest);
			} catch (TransformException e) {
				// Petición no válida
				String msg = Language.getMessage(LanguageKeys.MSG_WS_009);
				LOGGER.error(msg);
				reportResponse = new ReportGenerationResponse();
				reportResponse.setCode(ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
				reportResponse.setMessage(msg);

			}
			if (reportResponse == null) {
				// 1.1 Parametros a recoger
				ReportGenerationRequest rgr = new ReportGenerationRequest();

				// // 1.2 Extraemos del Map
				if (parameters != null && !parameters.isEmpty()) {
					// 1.2.1 Identificador de aplicación
					rgr.setApplicationId((String) parameters.remove(GenerationReportTransformI.APPLICATIONID));
					// Registramos en auditoría el identificador de aplicación
					traceInputs.addField(TraceI.APPLICATION_ID, rgr.getApplicationId());
					int uoIndex = rgr.getApplicationId().lastIndexOf('.');
					if (uoIndex > 0) {
						traceInputs.addField(TraceI.UO_ID, rgr.getApplicationId().substring(0, uoIndex));
					}
					// 1.2.2 Identificador de la plantilla
					rgr.setTemplateId((String) parameters.remove(GenerationReportTransformI.TEMPLATEID));
					// Registramos en auditoría el identificador de plantilla
					traceInputs.addField(TraceI.TEMPLATE_ID, rgr.getTemplateId());
					// 1.2.3 Firma electrónica
					LinkedHashMap<String, String> signatureMap = (LinkedHashMap<String, String>) parameters.remove(GenerationReportTransformI.SIGNATURE);
					if (signatureMap != null && !signatureMap.isEmpty()) {
						if (signatureMap.containsKey(GenerationReportTransformI.ENCODEDSIGNATURE)) {
							rgr.setSignature(base64.decode(signatureMap.remove(GenerationReportTransformI.ENCODEDSIGNATURE)));
						} else if (signatureMap.containsKey(GenerationReportTransformI.VALIDATIONRESPONSE)) {
							byte[ ] soapResponse = base64.decode(signatureMap.remove(GenerationReportTransformI.VALIDATIONRESPONSE));
							rgr.setAfirmaResponse(new AfirmaResponse(soapResponse));
						} else if (signatureMap.containsKey(GenerationReportTransformI.REPOSITORYID) && signatureMap.containsKey(GenerationReportTransformI.OBJECTID)) {
							rgr.setExternalSignature(new RepositoryLocation(signatureMap.remove(GenerationReportTransformI.OBJECTID), signatureMap.remove(GenerationReportTransformI.REPOSITORYID)));
							traceInputs.addField(TraceI.EXT_SIGN, rgr.getExternalSignature());
						}
					}
					// 1.2.4 Firma del Report
					rgr.setServerSign(parameters.containsKey(GenerationReportTransformI.INCLUDESIGNATURE) && Boolean.parseBoolean((String) parameters.remove(GenerationReportTransformI.INCLUDESIGNATURE)));
					// Registramos en auditoria si se realiza la firma del
					// report
					traceInputs.addField(TraceI.IS_SIGN, rgr.isServerSign());
					// 1.2.5 Documento
					LinkedHashMap<String, String> documentMap = (LinkedHashMap<String, String>) parameters.remove(GenerationReportTransformI.DOCUMENT);
					if (documentMap != null && !documentMap.isEmpty()) {
						if (documentMap.containsKey(GenerationReportTransformI.ENCODEDDOCUMENT)) {
							rgr.setDocument(base64.decode(documentMap.remove(GenerationReportTransformI.ENCODEDDOCUMENT)));
						} else if (documentMap.containsKey(GenerationReportTransformI.REPOSITORYID) && documentMap.containsKey(GenerationReportTransformI.OBJECTID)) {
							rgr.setExternalDocument(new RepositoryLocation(signatureMap.remove(GenerationReportTransformI.OBJECTID), signatureMap.remove(GenerationReportTransformI.REPOSITORYID)));
							traceInputs.addField(TraceI.EXT_DOC, rgr.getExternalDocument());
						}
					}
					// 1.2.6 Barcodes
					ArrayList<LinkedHashMap<String, Object>> barcodesList = (ArrayList<LinkedHashMap<String, Object>>) parameters.remove(GenerationReportTransformI.BARCODE);
					if (barcodesList != null && !barcodesList.isEmpty()) {
						ArrayList<Barcode> barcodes = new ArrayList<Barcode>();
						Iterator<LinkedHashMap<String, Object>> it = barcodesList.iterator();
						while (it.hasNext()) {
							LinkedHashMap<String, Object> barInfo = it.next();
							Barcode barcode = new Barcode((String) barInfo.remove(GenerationReportTransformI.TYPE), (String) barInfo.remove(GenerationReportTransformI.MESSAGE));
							barcode.setConfiguration((LinkedHashMap<String, String>) barInfo.remove(GenerationReportTransformI.CONFIGURATIONPARAMS));
							barcodes.add(barcode);
						}
						traceInputs.addField(TraceI.BARCODES, barcodes);
						rgr.setBarcodes(barcodes);
					}
					// 1.2.7 ExternalParameters
					LinkedHashMap<String, String> additionalParameters = (LinkedHashMap<String, String>) parameters.remove(GenerationReportTransformI.EXTERNALPARAMS);
					if (additionalParameters != null && !additionalParameters.isEmpty()) {
						traceInputs.addField(TraceI.EXT_PARAMS, additionalParameters);
						rgr.setAdditionalParameters(additionalParameters);
					}
				}

				// Registramos en auditoria la información de petición
				AuditManager.getInstance().addTrace(traceInputs);
				SignatureReportGeneration reportGenerator = new SignatureReportGeneration();
				reportResponse = reportGenerator.createReport(Long.parseLong(sequenceId), rgr);
			}

			if (reportResponse == null || reportResponse.getMessage() == null) {
				String msg = Language.getMessage(LanguageKeys.MSG_WS_005);
				LOGGER.error(msg);
				// Obtenemos el objeto para registrar el resultado del proceso
				TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManager.END_GENERATION_REPORT_ACT, AuditManager.GENERATION_REPORT_SRVC, new Date(), null);
				traceOutputs.addField(TraceI.RES_CODE, ReportGenerationResponse.UNKNOWN_ERROR);
				traceOutputs.addField(TraceI.RES_MESSAGE, msg);
				// Registramos en auditoria el resultado del proceso
				AuditManager.getInstance().addTrace(traceOutputs);
				return getUnknownErrorResponse(msg, ConfigurationFacade.GENERATION_REPORT_SERVICE);
			} else {
				LOGGER.info(Language.getFormatMessage(LanguageKeys.MSG_WS_006, new Object[ ] { "" + reportResponse.getCode(), reportResponse.getMessage() }));
				LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();
				results.put(GenerationReportTransformI.RESULTCODE, "" + reportResponse.getCode());
				results.put(GenerationReportTransformI.RESULTMESSAGE, reportResponse.getMessage());
				if (reportResponse.getReport() != null) {
					String encodedReport = base64.encodeBytes(reportResponse.getReport());
					results.put(GenerationReportTransformI.REPORT, encodedReport);
				}
				String xmlResponse = transform.marshal(results);
				// Obtenemos el objeto para registrar el resultado del proceso
				TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManager.END_GENERATION_REPORT_ACT, AuditManager.GENERATION_REPORT_SRVC, new Date(), null);
				traceOutputs.addField(TraceI.RES_CODE, reportResponse.getCode());
				traceOutputs.addField(TraceI.RES_MESSAGE, reportResponse.getMessage());
				// Registramos en auditoria el resultado del proceso
				AuditManager.getInstance().addTrace(traceOutputs);
				return xmlResponse;
			}
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.MSG_WS_004);
			LOGGER.error(msg, e);
			// Obtenemos el objeto para registrar el resultado del proceso
			TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManager.END_GENERATION_REPORT_ACT, AuditManager.GENERATION_REPORT_SRVC, new Date(), null);
			traceOutputs.addField(TraceI.RES_CODE, ReportGenerationResponse.UNKNOWN_ERROR);
			traceOutputs.addField(TraceI.RES_MESSAGE, msg);
			// Registramos en auditoria el resultado del proceso
			AuditManager.getInstance().addTrace(traceOutputs);
			return getUnknownErrorResponse(msg, ConfigurationFacade.GENERATION_REPORT_SERVICE);
		}
	}

	/**
	 * Validate a signed report.
	 * @param xmlRequest String that represents a XML request of report validation.
	 * @return XML that contains the result of the process.
	 */
	public String validateReport(String xmlRequest) {
		ReportValidationResponse valResp = null;
		LOGGER.info(Language.getMessage(LanguageKeys.MSG_WS_002));
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.MSG_WS_003, new Object[ ] { xmlRequest }));
		String sequenceId = (String) AxisEngine.getCurrentMessageContext().getProperty(AuditManagerI.SEQUENCE_ID_CONTEXT);
		if (sequenceId == null) {
			long seqId = AuditManager.getInstance().openTransaction(AuditManager.VALIDATION_REPORT_SRVC, null);
			sequenceId = String.valueOf(seqId);
			AxisEngine.getCurrentMessageContext().setProperty(AuditManagerI.SEQUENCE_ID_CONTEXT, sequenceId);
		}
		// Obtenemos el objeto para registrar la informaci�n de auditoria
		// asociada a la petici�n
		TraceI traceInputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManager.START_VALIDATION_REPORT_ACT, AuditManager.VALIDATION_REPORT_SRVC, new Date(), null);
		try {
			TransformI transform = TransformFactory.getTransform(ConfigurationFacade.VALIDATION_REPORT_SERVICE, null, ConfigurationFacade.SIGNATURE_REPORT_PLATFORM);
			LinkedHashMap<String, Object> parameters = null;
			try {
				parameters = transform.unmarshal(xmlRequest);
			} catch (TransformException e) {
				// Petici�n no v�lida
				String msg = Language.getMessage(LanguageKeys.MSG_WS_009);
				LOGGER.error(msg);
				valResp = new ReportValidationResponse(ReportValidationResponse.INVALID_INPUT_PARAMETERS, msg);
			}
			if (valResp == null) {
				String appId = (String) parameters.get(ValidationReportTransformI.APPLICATIONID);
				// Registramos en auditoria el identificador de aplicaci�n
				traceInputs.addField(TraceI.APPLICATION_ID, appId);
				int uoIndex = appId.lastIndexOf('.');
				if (uoIndex > 0) {
					traceInputs.addField(TraceI.UO_ID, appId.substring(0, uoIndex));
				}
				AuditManager.getInstance().addTrace(traceInputs);
				String reportEncoded = (String) parameters.get(ValidationReportTransformI.REPORT);
				byte[ ] report = null;
				if (reportEncoded != null) {
					report = base64.decode(reportEncoded);
				}
				SignatureReportValidation srm = new SignatureReportValidation();
				valResp = srm.validateReport(appId, report, null);
				if (valResp == null) {
					String msg = Language.getMessage(LanguageKeys.MSG_WS_007);
					LOGGER.error(msg);
					TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManagerI.END_VALIDATION_REPORT_ACT, AuditManagerI.VALIDATION_REPORT_SRVC, new Date(), null);
					traceOutputs.addField(TraceI.RES_CODE, ReportValidationResponse.UNKNOWN_ERROR);
					traceOutputs.addField(TraceI.RES_MESSAGE, msg);
					AuditManager.getInstance().addTrace(traceOutputs);
					return getUnknownErrorResponse(msg, ConfigurationFacade.VALIDATION_REPORT_SERVICE);
				}
			}
			LinkedHashMap<String, String> results = new LinkedHashMap<String, String>();
			results.put(ValidationReportTransformI.RESULTCODE, "" + valResp.getCode());
			results.put(ValidationReportTransformI.RESULTMESSAGE, valResp.getMessage());
			if (valResp.getCause() != null) {
				results.put(ValidationReportTransformI.RESULTCAUSE, valResp.getCause());
			}
			String xmlResponse = transform.marshal(results);
			// Obtenemos el objeto para registrar el resultado del proceso
			TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManagerI.END_VALIDATION_REPORT_ACT, AuditManagerI.VALIDATION_REPORT_SRVC, new Date(), null);
			traceOutputs.addField(TraceI.RES_CODE, valResp.getCode());
			traceOutputs.addField(TraceI.RES_MESSAGE, valResp.getMessage());
			if (valResp.getCause() != null) {
				traceOutputs.addField(TraceI.RES_CAUSE, valResp.getCause());
			}
			AuditManager.getInstance().addTrace(traceOutputs);
			return xmlResponse;
			
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.MSG_WS_004);
			LOGGER.error(msg, e);
			TraceI traceOutputs = TraceFactory.getTraceElement(Long.parseLong(sequenceId), AuditManagerI.END_VALIDATION_REPORT_ACT, AuditManagerI.VALIDATION_REPORT_SRVC, new Date(), null);
			traceOutputs.addField(TraceI.RES_CODE, ReportValidationResponse.UNKNOWN_ERROR);
			traceOutputs.addField(TraceI.RES_MESSAGE, msg);
			AuditManager.getInstance().addTrace(traceOutputs);
			return getUnknownErrorResponse(msg, ConfigurationFacade.VALIDATION_REPORT_SERVICE);
		}
	}

	/**
	 * Method that returns an unknown error response. <br/>
	 * ONLY should use this method if an exception occurs in this class.
	 * @param message	Description of error.
	 * @param serviceId Invoked service identifier.
	 * @return		Generation Signature Report Response.
	 */
	private String getUnknownErrorResponse(String message, String serviceId) {
		StringBuffer sb = new StringBuffer();
		String tagService = null;
		if (serviceId.equals(ConfigurationFacade.GENERATION_REPORT_SERVICE)) {
			tagService = "GenerationResponse";
		} else if (serviceId.equals(ConfigurationFacade.VALIDATION_REPORT_SERVICE)) {
			tagService = "ValidationReportResponse";
		}
		if (tagService != null) {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<srsm:" + tagService + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:srsm=\"urn:es:gob:signaturereport:services:messages\">");
			sb.append("<srsm:Result>");
			if (serviceId.equals(ConfigurationFacade.GENERATION_REPORT_SERVICE)) {
				sb.append("<srsm:Code>" + ReportGenerationResponse.UNKNOWN_ERROR + "</srsm:Code>");
			} else if (serviceId.equals(ConfigurationFacade.VALIDATION_REPORT_SERVICE)) {
				sb.append("<srsm:Code>" + ReportValidationResponse.UNKNOWN_ERROR + "</srsm:Code>");
			}
			sb.append("<srsm:Message>" + message + "</srsm:Message>");
			sb.append("</srsm:Result>");
			sb.append("</srsm:" + tagService + ">");
		}
		return sb.toString();
	}

}
