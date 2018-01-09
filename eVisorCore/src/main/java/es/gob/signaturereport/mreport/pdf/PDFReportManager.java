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
 * <b>File:</b><p>es.gob.signaturereport.mreport.pdf.PDFReportManager.java.</p>
 * <b>Description:</b><p> Class for managing PDF reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>24/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 24/02/2011.
 */
package es.gob.signaturereport.mreport.pdf;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.barcode.BarcodeException;
import es.gob.signaturereport.barcode.BarcodeManager;
import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceProxy;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI;
import es.gob.signaturereport.mfirma.responses.SignedDocument;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.Barcode;
import es.gob.signaturereport.mreport.AReportManager;
import es.gob.signaturereport.mreport.ReportException;
import es.gob.signaturereport.mreport.items.BarcodeImage;
import es.gob.signaturereport.mreport.items.FileAttachment;
import es.gob.signaturereport.mreport.items.MatrixPagesInclude;
import es.gob.signaturereport.mreport.items.PageDocumentImage;
import es.gob.signaturereport.mreport.items.PageIncludeFormat;
import es.gob.signaturereport.tools.FOUtils;
import es.gob.signaturereport.tools.FileUtils;
import es.gob.signaturereport.tools.ImageUtils;
import es.gob.signaturereport.tools.ODFUtils;
import es.gob.signaturereport.tools.PDFUtils;
import es.gob.signaturereport.tools.SignatureUtils;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.UtilsBase64;
import es.gob.signaturereport.tools.XMLUtils;

/** 
 * <p>Class for managing PDF reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 24/02/2011.
 */
public class PDFReportManager extends AReportManager {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(PDFReportManager.class);

	/**
	 * Attribute that represents the attachment name used for the storing the signature into the report. 
	 */
	private static final String SIGNATURE_NAME = Language.getMessage(LanguageKeys.RPT_ATTCH_SIGN_NAME);
	/**
	 * Attribute that represents the attachment description used for the storing the signature into the report. 
	 */
	private static final String SIGNATURE_DESCRITION = Language.getMessage(LanguageKeys.RPT_ATTCH_SIGN_DES);;

	/**
	 * Attribute that represents the attachment name used for the storing the signed document into the report. 
	 */
	private static final String DOCUMENT_NAME = Language.getMessage(LanguageKeys.RPT_ATTCH_DOC_NAME);
	/**
	 * Attribute that represents the attachment description used for the storing the signed document into the report. 
	 */
	private static final String DOCUMENT_DESCRIPTION = Language.getMessage(LanguageKeys.RPT_ATTCH_DOC_DES);
	/**
	 * Attribute that represents the attachment name used for the storing the signer certificate into the report. 
	 */
	private static final String CERTIFICATE_NAME = Language.getMessage(LanguageKeys.RPT_ATTCH_CERT_NAME);

	/**
	 * Attribute that represents the attachment name used for the storing the signed document into the report. 
	 */
	private static final String DOCUMENT_SIG_NAME = Language.getMessage(LanguageKeys.RPT_ATTCH_DOCSIG_NAME);
	/**
	 * Attribute that represents the attachment description used for the storing the signed document into the report. 
	 */
	private static final String DOCUMENT_SIG_DESCRIPTION = Language.getMessage(LanguageKeys.RPT_ATTCH_DOCSIG_DES);

	/**
	 * Attribute that represents the attachment extension used for the storing the signer certificate into the report. 
	 */
	private static final String CERTIFICATE_EXTENSION = ".cer";
	/**
	 * Attribute that represents the attachment description used for the storing the signer certificate into the report. 
	 */
	private static final String CERTIFICATE_DESCRIPTION = Language.getMessage(LanguageKeys.RPT_ATTCH_CERT_DES);
	/**
	 * Attribute that represents the attachment name used for the storing the XML response into the report. 
	 */
	private static final String RESPONSE_NAME = Language.getMessage(LanguageKeys.RPT_ATTCH_RES_NAME);
	/**
	 * Attribute that represents the attachment description used for the storing the XML response into the report. 
	 */
	private static final String RESPONSE_DESCRIPTION = Language.getMessage(LanguageKeys.RPT_ATTCH_RES_DES);;

	/**
	 * Attribute that represents the tool class for management of file in base 64 encoded. 
	 */
	private UtilsBase64 base64Tool = new UtilsBase64();

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.mreport.ReportManagerI#createReport(es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse, es.gob.signaturereport.configuration.items.TemplateData, byte[], byte[], java.util.ArrayList, java.util.HashMap)
	 */
	public byte[ ] createReport(ValidationSignatureResponse validationResponse, TemplateData templateConf, byte[ ] signature, byte[ ] document, ArrayList<Barcode> barcodes, HashMap<String, String> additionalParameters) throws ReportException {
		// 1. verificamos que el resultado de validaci�n es 'firma v�lida'.
		if (validationResponse == null || validationResponse.getValidationInfo() == null) {
			String msg = Language.getMessage(LanguageKeys.RPT_003);
			LOGGER.error(msg);
			throw new ReportException(ReportException.INVALID_INPUT_PARAMETERS, msg);
		}
		String rm = (String) validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MAJOR_RES);
		if (rm == null) {
			String msg = Language.getMessage(LanguageKeys.RPT_003);
			LOGGER.error(msg);
			throw new ReportException(ReportException.INVALID_INPUT_PARAMETERS, msg);
		} else if (!rm.equals(DSSVerifyTransformI.VALID_SIGNATURE) && !templateConf.isForceGeneration()) {
			String msg = Language.getMessage(LanguageKeys.RPT_004);
			LOGGER.error(msg);
			throw new ReportException(ReportException.INVALID_SIGNATURE, msg);
		}
		// 2. Generamos el codigo de barra en caso de haberse solicitado
		BarcodeManager barManager = new BarcodeManager();
		ArrayList<BarcodeImage> bars = null;
		if (barcodes != null && !barcodes.isEmpty()) {
			try {
				bars = barManager.generateBarcode(barcodes, true, false);
			} catch (BarcodeException e1) {
				String msg = Language.getMessage(LanguageKeys.RPT_013);
				LOGGER.error(msg, e1);
				if (e1.getCode() == BarcodeException.INVALID_INPUT_PARAMETERS) {
					throw new ReportException(ReportException.INVALID_INPUT_PARAMETERS, e1.getDescription(), e1);
				} else {
					throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e1);
				}
			}
		}
		int numPages = 0;
		List<String> pagesOrientation = null;
		// 3. Obtenemos el documento firmado
		byte[ ] signedDocument = null;
		String mimeType = null;
		if (document != null) {
			// 3.1 Prevalece el documento incluido en la petici�n al que
			// pueda obtenerse de la plataforma de firma
			try {
				mimeType = FileUtils.getMediaType(document);
			} catch (ToolsException e) {
				throw new ReportException(ReportException.UNKNOWN_ERROR, e.getDescription(), e);
			}
			signedDocument = document;
		} else if (validationResponse.getSignedDocumentList() != null && validationResponse.getSignedDocumentList().size() > 0) {
			// 3.2 Nos quedamos con el primer documento recuperado de la
			// respuesta de la plataforma de firma
			signedDocument = validationResponse.getSignedDocumentList().get(0).getContent();
			mimeType = validationResponse.getSignedDocumentList().get(0).getMimeType();
		}

		// 4. Analizamos el modo de generaci�n del documento
		ArrayList<PageDocumentImage> images = null;
		byte[ ] signedPdf = null;
		if (templateConf.getModeDocInclude() == TemplateConfigurationFacadeI.INC_SIGNED_DOC_IMAGE) {
			// 4.1 Debemos obtener las images del documento
			try {
				if (signedDocument != null && mimeType != null) {
					if (FileUtils.isImage(mimeType)) {
						PageDocumentImage pageImg = ImageUtils.toPageDocumentImage(signedDocument, 1, mimeType, true, false);
						images = new ArrayList<PageDocumentImage>();
						images.add(pageImg);
						numPages = 1;
					} else if (FileUtils.PDF_MEDIA_TYPE.equals(mimeType)) {
						numPages = PDFUtils.getNumPages(signedDocument);
						checkNumberPage(templateConf.getPagesRange(), numPages);
						images = PDFUtils.getPDFPages(signedDocument, templateConf.getPagesRange(), true, false);
						pagesOrientation = PDFUtils.getPagesOrientation(signedDocument);
					} else if (FileUtils.isTextFile(mimeType)) {
						images = ImageUtils.textToImage(signedDocument, true, false);
						numPages = images.size();
					} else if (ODFUtils.getInstance().isExportableToPDF(mimeType)) {
						ExportToPDFServiceProxy service = new ExportToPDFServiceProxy();
						byte[ ] exportedPdf;
						try {
							exportedPdf = service.exportToPDF(signedDocument);
							if (exportedPdf != null) {
								numPages = PDFUtils.getNumPages(exportedPdf);
								checkNumberPage(templateConf.getPagesRange(), numPages);
								pagesOrientation = PDFUtils.getPagesOrientation(exportedPdf);
								images = PDFUtils.getPDFPages(exportedPdf, templateConf.getPagesRange(), true, false);
							}
						} catch (RemoteException e) {
							LOGGER.error(Language.getMessage(LanguageKeys.INVK_PDFWS_001), e);
						}

					}

				}
			} catch (ToolsException te) {
				LOGGER.error(Language.getMessage(LanguageKeys.RPT_010), te);
				if (te.getCode() == ToolsException.INVALID_PAGE_NUMBER) {
					throw new ReportException(ReportException.INVALID_PAGE_NUMBER, Language.getMessage(LanguageKeys.RPT_011), te);
				} else if (te.getCode() == ToolsException.INVALID_PDF_FILE) {
					throw new ReportException(ReportException.INVALID_PDF_FILE, Language.getMessage(LanguageKeys.RPT_012), te);
				} else {
					throw new ReportException(ReportException.UNKNOWN_ERROR, Language.getMessage(LanguageKeys.RPT_010), te);
				}
			}
		} else if (templateConf.getModeDocInclude() == TemplateConfigurationFacadeI.INC_SIGNED_DOC_EMBED || templateConf.getModeDocInclude() == TemplateConfigurationFacadeI.INC_SIGNED_DOC_CONCAT) {
			// 4.2 Transformamos el documento firmado a PDF
			try {
				if (FileUtils.isImage(mimeType)) {
					signedPdf = PDFUtils.imageToPDF(signedDocument);
				} else if (FileUtils.PDF_MEDIA_TYPE.equals(mimeType)) {
					signedPdf = signedDocument;
				} else if (FileUtils.isTextFile(mimeType)) {
					signedPdf = PDFUtils.textToPDF(signedDocument);
				} else if (ODFUtils.getInstance().isExportableToPDF(mimeType)) {
					ExportToPDFServiceProxy service = new ExportToPDFServiceProxy();
					try {
						signedPdf = service.exportToPDF(signedDocument);
					} catch (RemoteException e) {
						LOGGER.error(Language.getMessage(LanguageKeys.INVK_PDFWS_001), e);
					}
				}

				// Extreamos el n�mero de p�ginas
				if (signedPdf != null) {
					numPages = PDFUtils.getNumPages(signedPdf);
					pagesOrientation = PDFUtils.getPagesOrientation(signedPdf);
				}
			} catch (ToolsException e) {
				LOGGER.error(e.getDescription());
				if (e.getCode() == ToolsException.INVALID_PAGE_NUMBER) {
					throw new ReportException(ReportException.INVALID_PAGE_NUMBER, Language.getMessage(LanguageKeys.RPT_011), e);
				} else if (e.getCode() == ToolsException.INVALID_PDF_FILE) {
					throw new ReportException(ReportException.INVALID_PDF_FILE, Language.getMessage(LanguageKeys.RPT_012), e);
				} else {
					throw new ReportException(ReportException.UNKNOWN_ERROR, e.getDescription(), e);
				}
			}
		}

		// 4. generamos el XML de entrada.
		String xmlInput = createInputXML(validationResponse, additionalParameters, numPages, images, bars, pagesOrientation);

		// 5.Aplicamos la transformaci�n XSLT.
		byte[ ] foFile = null;
		try {
			foFile = XMLUtils.xslTransform(xmlInput.getBytes("UTF-8"), templateConf.getTemplate());
		} catch (UnsupportedEncodingException e) {
			String msg = Language.getMessage(LanguageKeys.RPT_005);
			LOGGER.error(msg, e);
			throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e);
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.RPT_005);
			LOGGER.error(msg, e);
			throw new ReportException(ReportException.INVALID_TEMPLATE, msg, e);
		}

		if (foFile == null) {
			String msg = Language.getMessage(LanguageKeys.RPT_014);
			LOGGER.error(msg);
			throw new ReportException(ReportException.UNKNOWN_ERROR, msg);
		}
		// 6.Analizamos si se incrusta el documento original
		MatrixPagesInclude pagesIncl = null;
		if (templateConf.getModeDocInclude() == TemplateConfigurationFacadeI.INC_SIGNED_DOC_EMBED) {

			try {
				Document doc = XMLUtils.getDocument(foFile);
				pagesIncl = getIncludePages(doc);
				foFile = XMLUtils.getXMLBytes(doc);
			} catch (ToolsException e) {
				String msg = Language.getMessage(LanguageKeys.RPT_015);
				LOGGER.error(msg, e);
				throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e);
			}

		}
		// 7. Realizamos el procesado XSL-FO
		byte[ ] pdf = null;
		try {
			pdf = FOUtils.fo2pdf(foFile);
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.RPT_019);
			LOGGER.error(msg, e);
			if (e.getCode() == ToolsException.INVALID_FO_FILE) {
				throw new ReportException(ReportException.INVALID_TEMPLATE, e.getDescription(), e);
			} else {
				throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e);
			}

		}
		// 8. Se realiza la incrustaci�n del documento original en el informe de
		// firma
		if (pagesIncl != null && !pagesIncl.isEmpty() && signedPdf != null) {
			try {
				pdf = PDFUtils.includePages(pdf, signedPdf, pagesIncl);
			} catch (ToolsException e) {
				String msg = Language.getMessage(LanguageKeys.RPT_022);
				LOGGER.error(msg, e);
				throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e);
			}
		}
		// 9. En caso de solicitar concatenaci�n, se concatena informe y
		// documento original.
		if (templateConf.getModeDocInclude() == TemplateConfigurationFacadeI.INC_SIGNED_DOC_CONCAT && signedPdf != null) {
			if (templateConf.getConcatenationRule() == null) {
				String msg = Language.getMessage(LanguageKeys.RPT_023);
				LOGGER.error(msg);
				throw new ReportException(ReportException.INVALID_TEMPLATE, msg);
			}
			try {
				pdf = PDFUtils.concatPDF(pdf, signedPdf, TemplateConfigurationFacadeI.REPORT_CONTAT_ID, TemplateConfigurationFacadeI.DOCUMENT_CONCAT_ID, templateConf.getConcatenationRule());
			} catch (ToolsException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.RPT_024), e);
				if (e.getCode() == ToolsException.INVALID_CONCATENATION_RULE) {
					throw new ReportException(ReportException.INVALID_TEMPLATE, Language.getMessage(LanguageKeys.RPT_024), e);
				} else {
					throw new ReportException(ReportException.INVALID_TEMPLATE, Language.getMessage(LanguageKeys.RPT_025), e);
				}
			}

		}

		// 10. Incluimos anexos en caso de ser requeridos
		try {
			ArrayList<FileAttachment> attachments = new ArrayList<FileAttachment>();
			// 10.1 Anexo de la firma
			if (templateConf.isAttachSignature() && signature != null) {
				String extension = SignatureUtils.getSignatureFileExtension(signature);
				FileAttachment att = new FileAttachment(SIGNATURE_NAME + "." + extension, SIGNATURE_DESCRITION);
				att.setContent(signature);
				attachments.add(att);
			}
			// 10.2 Anexo del documento
			if (templateConf.isAttachDocument() && document != null) {
				FileAttachment att = new FileAttachment(DOCUMENT_NAME + "." + FileUtils.getFileExtension(document), DOCUMENT_DESCRIPTION);
				att.setContent(document);
				attachments.add(att);
			}
			// 10.3 Anexo de la respuesta de verificaci�n de firma
			if (templateConf.isAttchResponse() && validationResponse.getXmlResponse() != null) {
				FileAttachment att = new FileAttachment(RESPONSE_NAME, RESPONSE_DESCRIPTION);
				att.setContent(validationResponse.getXmlResponse().getBytes("UTF-8"));
				attachments.add(att);
			}
			// 10.4 Anexo de certificado
			if (templateConf.isAttachCertificate() && validationResponse.getValidationInfo() != null && validationResponse.getValidationInfo().containsKey(DSSVerifyTransformI.IND_SIG_REPORT_RES)) {
				ArrayList<LinkedHashMap<String, Object>> signers = (ArrayList<LinkedHashMap<String, Object>>) validationResponse.getValidationInfo().get(DSSVerifyTransformI.IND_SIG_REPORT_RES);
				for (int i = 0; i < signers.size(); i++) {
					LinkedHashMap<String, Object> signer = signers.get(i);
					if (signer.containsKey(DSSVerifyTransformI.CERTIFICATE_VALUE_RES)) {
						FileAttachment att = new FileAttachment(CERTIFICATE_NAME + "_" + i + CERTIFICATE_EXTENSION, CERTIFICATE_DESCRIPTION);
						att.setContent(base64Tool.decode((String) signer.get(DSSVerifyTransformI.CERTIFICATE_VALUE_RES)));
						attachments.add(att);
					}
				}
			}
			// 10.5 Anexo de los documentos incluidos en la firma electr�nica
			if (templateConf.isAttchDocInSignature() && validationResponse.getSignedDocumentList() != null) {
				for (int i = 0; i < validationResponse.getSignedDocumentList().size(); i++) {
					SignedDocument sigDocument = validationResponse.getSignedDocumentList().get(i);
					if (sigDocument != null && sigDocument.getContent() != null && sigDocument.getMimeType() != null) {
						String extension = FileUtils.getFileExtension(sigDocument.getMimeType());
						FileAttachment att = new FileAttachment(DOCUMENT_SIG_NAME + "_" + i + "." + extension, DOCUMENT_SIG_DESCRIPTION);
						att.setContent(sigDocument.getContent());
						attachments.add(att);
					}
				}

			}
			if (!attachments.isEmpty()) {
				pdf = PDFUtils.addAttachment(pdf, attachments);
			}
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.RPT_026);
			LOGGER.error(msg);
			throw new ReportException(ReportException.UNKNOWN_ERROR, msg, e);
		}
		return pdf;
	}

	/**
	 * FO file may include various  "IncludePage" components in the following format:<br/>
	 // CHECKSTYLE:OFF -- The following describes a IncludePage element.
	 * "<IncludePage Ypos="String" Width="String" Height="String" Xpos="String" Layout="front/back" xmlns="urn:es:gob:signaturereport:generation:inputparameters">"<br/>
	 *  	"<DocumentPage>X</DocumentPage>"<br/>
	 *  	"<ReportPage>Y</ReportPage>"<br/>
	 * "</IncludePage>"<br/>
	 // CHECKSTYLE:ON
	 * In this component  is reported that the X page of signed document is included in the Y page of signature report.
	 * The position of the page is specified with the attributes Ypos, Xpos, Width and Height.
	 * @param doc Fo file.
	 * @return	A {@link MatrixPagesInclude} that contains information about the pages to include in a signature report
	 * @throws ReportException	If an error occurs.
	 */
	private MatrixPagesInclude getIncludePages(Document doc) throws ReportException {
		MatrixPagesInclude matrix = new MatrixPagesInclude();
		NodeList list = doc.getElementsByTagNameNS(NS, INCLUDEPAGE);
		while (list != null && list.getLength() > 0) {
			Node ipNode = list.item(0);
			String docPag = null;
			String rptPag = null;
			NodeList childPage = ipNode.getChildNodes();
			for (int i = 0; i < childPage.getLength(); i++) {
				if (childPage.item(i).getNodeType() == Node.ELEMENT_NODE && childPage.item(i).getLocalName().equals(DOCUMENTPAGE) && childPage.item(i).getFirstChild() != null) {
					docPag = childPage.item(i).getFirstChild().getNodeValue();
				}
				if (childPage.item(i).getNodeType() == Node.ELEMENT_NODE && childPage.item(i).getLocalName().equals(REPORTPAGE) && childPage.item(i).getFirstChild() != null) {
					rptPag = childPage.item(i).getFirstChild().getNodeValue();
				}
			}
			String msg = null;
			if (docPag == null) {
				msg = Language.getFormatMessage(LanguageKeys.RPT_016, new Object[ ] { INCLUDEPAGE, DOCUMENTPAGE });
			} else if (rptPag == null) {
				msg = Language.getFormatMessage(LanguageKeys.RPT_016, new Object[ ] { INCLUDEPAGE, REPORTPAGE });
			}
			try {
				PageIncludeFormat format = new PageIncludeFormat();
				if (msg != null) {
					LOGGER.error(msg);
					throw new ReportException(ReportException.INVALID_TEMPLATE, msg);
				}

				if (ipNode.getAttributes().getNamedItem(XPOS) != null && ipNode.getAttributes().getNamedItem(XPOS).getNodeValue() != null) {
					format.setXpos(Double.parseDouble(ipNode.getAttributes().getNamedItem(XPOS).getNodeValue()));
				}

				if (ipNode.getAttributes().getNamedItem(YPOS) != null && ipNode.getAttributes().getNamedItem(YPOS).getNodeValue() != null) {
					format.setYpos(Double.parseDouble(ipNode.getAttributes().getNamedItem(YPOS).getNodeValue()));
				}

				if (ipNode.getAttributes().getNamedItem(WIDTH) != null && ipNode.getAttributes().getNamedItem(WIDTH).getNodeValue() != null) {
					format.setWidth(Double.parseDouble(ipNode.getAttributes().getNamedItem(WIDTH).getNodeValue()));
				}

				if (ipNode.getAttributes().getNamedItem(HEIGHT) != null && ipNode.getAttributes().getNamedItem(HEIGHT).getNodeValue() != null) {
					format.setHeight(Double.parseDouble(ipNode.getAttributes().getNamedItem(HEIGHT).getNodeValue()));
				}

				if (ipNode.getAttributes().getNamedItem(LAYOUT) != null && ipNode.getAttributes().getNamedItem(LAYOUT).getNodeValue() != null) {
					format.setLayout(ipNode.getAttributes().getNamedItem(LAYOUT).getNodeValue());
				}

				matrix.addPage(Integer.parseInt(rptPag), Integer.parseInt(docPag), format);
				Node parent = ipNode.getParentNode();
				parent.removeChild(ipNode);
			} catch (NumberFormatException nfe) {
				msg = Language.getFormatMessage(LanguageKeys.RPT_017, new Object[ ] { INCLUDEPAGE });
				LOGGER.error(msg, nfe);
				throw new ReportException(ReportException.INVALID_TEMPLATE, msg, nfe);
			}

		}
		return matrix;
	}
}
