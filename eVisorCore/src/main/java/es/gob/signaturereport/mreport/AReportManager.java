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
 * <b>File:</b><p>es.gob.signaturereport.mreport.AReportManager.java.</p>
 * <b>Description:</b><p>Abstract class management signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>21/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 21/02/2011.
 */
package es.gob.signaturereport.mreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.mreport.items.BarcodeImage;
import es.gob.signaturereport.mreport.items.PageDocumentImage;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.UtilsTime;

/** 
 * <p>Abstract class management signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/02/2011.
 */
public abstract class AReportManager implements ReportManagerI {

	/**
     * Attribute that represents the processing instruction. 
     */
    protected static final String PROC_INSTRUCTION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * Attribute that represents property that contain the number pages limit. 
     */
    protected static final  String NUM_MAX_PAGE_KEY = "signaturereport.global.imgpage.nummax";
    /**
     * Attribute that represents the namespaces used in the XML message. 
     */
    protected static final   String NS = "urn:es:gob:signaturereport:generation:inputparameters";

    /**
     * Attribute that represents the local name that identifies 'GenerationReport' element. 
     */
    protected static final  String GENERATIONREPORT = "GenerationReport";

    /**
     * Attribute that represents the local name that identifies 'ValidationResult' element. 
     */
    protected static final  String VALIDATIONRESULT = "ValidationResult";

    /**
     * Attribute that represents the local name that identifies 'Result' element. 
     */
    protected static final  String RESULT = "Result";

    /**
     * Attribute that represents the local name that identifies 'Major' element. 
     */
    protected static final  String MAJOR = "Major";

    /**
     * Attribute that represents the local name that identifies 'Minor' element. 
     */
    protected static final  String MINOR = "Minor";

    /**
     * Attribute that represents the local name that identifies 'Message' element. 
     */
    protected static final  String MESSAGE = "Message";

    /**
     * Attribute that represents the local name that identifies 'IndividualSignature' element. 
     */
    protected static final  String INDIVIDUALSIGNATURE = "IndividualSignature";
    
    /**
     * Attribute that represents the local name that identifies 'TimeStamp' element. 
     */
    protected static final  String TIMESTAMP = "TimeStamp";
    /**
     * Attribute that represents the local name that identifies 'CertificateInfo' element. 
     */
    protected static final  String CERTIFICATEINFO = "CertificateInfo";

    /**
     * Attribute that represents the local name that identifies 'Field' element. 
     */
    protected static final  String FIELD = "Field";
    
    /**
     * Attribute that represents the local name that identifies 'FieldId' element. 
     */
    protected static final  String FIELDID = "FieldId";
   
    /**
     * Attribute that represents the local name that identifies 'FieldValue' element. 
     */
    protected static final  String FIELDVALUE = "FieldValue";
    
    /**
     * Attribute that represents the local name that identifies 'ExternalParameters' element. 
     */
    protected static final  String EXTERNALPARAMETERS = "ExternalParameters";
    
    /**
     * Attribute that represents the local name that identifies 'Parameter' element. 
     */
    protected static final  String PARAMETER = "Parameter";
    
    /**
     * Attribute that represents the local name that identifies 'ParameterId' element. 
     */
    protected static final   String PARAMETERID = "ParameterId";
    
    /**
     * Attribute that represents the local name that identifies 'ParameterValue' element. 
     */
    protected static final   String PARAMETERVALUE = "ParameterValue";

    /**
     * Attribute that represents the local name that identifies 'DocumentInfo' element. 
     */
    protected static final   String DOCUMENTINFO = "DocumentInfo";
    
    /**
     * Attribute that represents the local name that identifies 'NumPages' element. 
     */
    protected static final  String NUMPAGES = "NumPages";
    
    /**
     * Attribute that represents the local name that identifies 'PagesOrientation' element. 
     */
    protected static final  String PAGESORIENTATION = "PagesOrientation";
    
    /**
     * Attribute that represents the local name that identifies 'PageInfo' element. 
     */
    protected static final  String PAGEINFO = "PageInfo";

    /**
     * Attribute that represents the local name that identifies 'PagesList' element. 
     */
    protected static final String PAGESLIST = "PagesList";
    
    /**
     * Attribute that represents the local name that identifies 'Page' element. 
     */
    protected static final  String PAGE = "Page";

    /**
     * Attribute that represents the local name that identifies 'Number' element. 
     */
    protected static final String NUMBER = "Number";
    
    /**
     * Attribute that represents the local name that identifies 'URL' element. 
     */
    protected static final  String URL = "URL";

    /**
     * Attribute that represents the local name that identifies 'Barcodes' element. 
     */
    protected static final  String BARCODES = "Barcodes";
    
    /**
     * Attribute that represents the local name that identifies 'Barcode' element. 
     */
    protected static final  String BARCODE = "Barcode";

    /**
     * Attribute that represents the local name that identifies 'Code' element. 
     */
    protected static final  String CODE = "Code";

    /**
     * Attribute that represents the local name that identifies 'Type' element. 
     */
    protected static final  String TYPE = "Type";
    /**
     * Attribute that represents the local name that identifies 'IncludePage' element. 
     */
    protected static final  String INCLUDEPAGE= "IncludePage";
    
    /**
     * Attribute that represents the local name that identifies 'Ypos' attribute. 
     */
    protected static final  String YPOS= "Ypos";
    
    /**
     * Attribute that represents the local name that identifies ' Xpos' attribute. 
     */
    protected static final  String XPOS= "Xpos";
    
    /**
     * Attribute that represents the local name that identifies 'Width' attribute. 
     */
    protected static final  String WIDTH= "Width";
    
    /**
     * Attribute that represents the local name that identifies 'Height' attribute. 
     */
    protected static final  String HEIGHT= "Height";
    
    /**
     * Attribute that represents the local name that identifies 'Layout' attribute. 
     */
    protected static final  String LAYOUT = "Layout";
    
    /**
     * Attribute that represents the local name that identifies 'DocumentPage' element. 
     */
    protected static final  String DOCUMENTPAGE = "DocumentPage";
    
    /**
     * Attribute that represents the local name that identifies 'ReportPage' element. 
     */
    protected static final  String REPORTPAGE= "ReportPage";
    
    /**
     * Attribute that represents the local name that identifies 'GenerationReport' element. 
     */
    protected static final  String GENERATIONTIME ="GenerationTime";
    /**
     * Attribute that represents the object that manages the log of the class. 
     */
    private static final Logger LOGGER = Logger.getLogger(AReportManager.class);
    
   
    @SuppressWarnings("unchecked")
    protected String createInputXML(ValidationSignatureResponse validationResponse, HashMap<String, String> additionalParameters, int numPages, ArrayList<PageDocumentImage> images, ArrayList<BarcodeImage> barcodes, List<String> pagesOrientation) {
	StringBuffer sb = new StringBuffer();
	// <?xml version="1.0" encoding="UTF-8"?>
	sb.append(PROC_INSTRUCTION);
	// <GenerationReport
	// xmlns="urn:es:gob:signaturereport:generation:message">
	sb.append("<" + GENERATIONREPORT + " xmlns=\"" + NS + "\">");
	if (validationResponse != null && validationResponse.getValidationInfo() != null && !validationResponse.getValidationInfo().isEmpty()) {
	    // <ValidationResult>
	    sb.append("<" + VALIDATIONRESULT + ">");
	    // <Result>
	    sb.append("<" + RESULT + ">");
	    if (validationResponse.getValidationInfo().containsKey(DSSVerifyTransformI.RESULT_MAJOR_RES)) {
		// <Major/>
		sb.append("<" + MAJOR + ">" + validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MAJOR_RES) + "</" + MAJOR + ">");
	    }
	    if (validationResponse.getValidationInfo().containsKey(DSSVerifyTransformI.RESULT_MINOR_RES)) {
		// <Minor/>
		sb.append("<" + MINOR + ">" + validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MINOR_RES) + "</" + MINOR + ">");
	    }
	    if (validationResponse.getValidationInfo().containsKey(DSSVerifyTransformI.RESULT_MSG_RES)) {
		// <Message/>
		sb.append("<" + MESSAGE + ">" + validationResponse.getValidationInfo().get(DSSVerifyTransformI.RESULT_MSG_RES) + "</" + MESSAGE + ">");
	    }
	    // </Result>
	    sb.append("</" + RESULT + ">");

	    if (validationResponse.getValidationInfo().containsKey(DSSVerifyTransformI.IND_SIG_REPORT_RES)) {
		ArrayList<LinkedHashMap<String, Object>> signatures = (ArrayList<LinkedHashMap<String, Object>>) validationResponse.getValidationInfo().get(DSSVerifyTransformI.IND_SIG_REPORT_RES);
		for (int i = 0; i < signatures.size(); i++) {
		    // <IndividualSignature>
		    sb.append("<" + INDIVIDUALSIGNATURE + ">");
		    LinkedHashMap<String, Object> signer = (LinkedHashMap<String, Object>) signatures.get(i);
		    // <Result>
		    sb.append("<" + RESULT + ">");
		    if (signer.containsKey(DSSVerifyTransformI.RESULT_MAJOR_RES)) {
			// <Major/>
			sb.append("<" + MAJOR + ">" + signer.get(DSSVerifyTransformI.RESULT_MAJOR_RES) + "</" + MAJOR + ">");
		    }
		    if (signer.containsKey(DSSVerifyTransformI.RESULT_MINOR_RES)) {
			// <Minor/>
			sb.append("<" + MINOR + ">" + signer.get(DSSVerifyTransformI.RESULT_MINOR_RES) + "</" + MINOR + ">");
		    }
		    if (signer.containsKey(DSSVerifyTransformI.RESULT_MSG_RES)) {
			// <Message/>
			sb.append("<" + MESSAGE + ">" + signer.get(DSSVerifyTransformI.RESULT_MSG_RES) + "</" + MESSAGE + ">");
		    }
		    // </Result>
		    sb.append("</" + RESULT + ">");
		    if (signer.containsKey(DSSVerifyTransformI.CREATION_TIME_RES)) {
			// <TimeStamp/>
			sb.append("<" + TIMESTAMP + ">" + signer.get(DSSVerifyTransformI.CREATION_TIME_RES) + "</" + TIMESTAMP + ">");
		    }
		    if (signer.containsKey(DSSVerifyTransformI.CERT_INFO_RES)) {
			// <CertificateInfo>
			sb.append("<" + CERTIFICATEINFO + ">");
			LinkedHashMap<String, String> certInfo = (LinkedHashMap<String, String>) signer.get(DSSVerifyTransformI.CERT_INFO_RES);
			Iterator<String> it = certInfo.keySet().iterator();
			while (it.hasNext()) {
			    // <Field>
			    sb.append("<" + FIELD + ">");
			    String id = it.next();
			    // <FieldId/>
			    sb.append("<" + FIELDID + ">" + id + "</" + FIELDID + ">");
			    String value = certInfo.get(id);
			    // <FieldValue/>
			    sb.append("<" + FIELDVALUE + ">" + value + "</" + FIELDVALUE + ">");
			    // </Field>
			    sb.append("</" + FIELD + ">");
			}

			// </CertificateInfo>
			sb.append("</" + CERTIFICATEINFO + ">");
		    }

		    // </IndividualSignature>
		    sb.append("</" + INDIVIDUALSIGNATURE + ">");
		}
	    }

	    // </ValidationResult>
	    sb.append("</" + VALIDATIONRESULT + ">");
	}
	sb.append("<"+GENERATIONTIME+">");
	sb.append(UtilsTime.getFechaSistema(UtilsTime.FORMATO_FECHA_ESTANDAR));
	sb.append("</"+GENERATIONTIME+">");
	if (additionalParameters != null && !additionalParameters.isEmpty()) {
	    // <ExternalParameters>
	    sb.append("<" + EXTERNALPARAMETERS + ">");
	    Iterator<String> it = additionalParameters.keySet().iterator();
	    while (it.hasNext()) {
		// <Parameter>
		sb.append("<" + PARAMETER + ">");
		String id = it.next();
		// <ParameterId/>
		sb.append("<" + PARAMETERID + ">" + id + "</" + PARAMETERID + ">");
		String value = additionalParameters.get(id);
		// <ParameterValue/>
		sb.append("<" + PARAMETERVALUE + ">" + value + "</" + PARAMETERVALUE + ">");
		// </Parameter>
		sb.append("</" + PARAMETER + ">");
	    }
	    // </ExternalParameters>
	    sb.append("</" + EXTERNALPARAMETERS + ">");
	}
	// <DocumentInfo>
	if (numPages > 0) {
	    // <DocumentInfo>
	    sb.append("<" + DOCUMENTINFO + ">");
	    // <NumPages> </NumPages>
	    sb.append("<" + NUMPAGES + ">" + numPages + "</" + NUMPAGES + ">");
	    
	    if (images != null && !images.isEmpty()) {
		// <PagesList>
		sb.append("<" + PAGESLIST + ">");
		for (int i = 0; i < images.size(); i++) {
		    PageDocumentImage pageImage = images.get(i);
		    // <Page>
		    sb.append("<" + PAGE + ">");
		    // <Number> </Number>
		    sb.append("<" + NUMBER + ">" + pageImage.getNumPage() + "</" + NUMBER + ">");
		    // <URL> </URL>
		    sb.append("<" + URL + ">" + pageImage.getLocation() + "</" + URL + ">");
		    // </Page>
		    sb.append("</" + PAGE + ">");
		}
		// </PagesList>
		sb.append("</" + PAGESLIST + ">");
	    }
	    
	    if (pagesOrientation != null) {
	    	//<PagesOrientation> 
	    	sb.append("<" + PAGESORIENTATION + ">");
	    	// <PageInfo/>
	    	for (int i = 1 ; i <= pagesOrientation.size(); i++) {
	    		sb.append("<" + PAGEINFO + " orientation=\""+pagesOrientation.get(i-1)+"\"/>");
	    	}	  
	    	sb.append("</" + PAGESORIENTATION + ">");
	    	//</PagesOrientation>
	    } else {
	    	//<PagesOrientation> 
	    	sb.append("<" + PAGESORIENTATION + ">");
	    	// <PageInfo/>
	    	for (int i = 1 ; i <= numPages; i++) {
	    		sb.append("<" + PAGEINFO + " orientation=\"V\"/>");
	    	}	
	    	sb.append("</" + PAGESORIENTATION + ">");
	    	//</PagesOrientation>
	    }

	    // </DocumentInfo>
	    sb.append("</" + DOCUMENTINFO + ">");
	}
	if (barcodes != null && !barcodes.isEmpty()) {
	    // <Barcodes>
	    sb.append("<" + BARCODES + ">");
	    for (int i = 0; i < barcodes.size(); i++) {
		BarcodeImage barImg = barcodes.get(i);
		// <Barcode>
		sb.append("<" + BARCODE + ">");
		if (barImg.getBarcodeType() != null) {
		    // <Type> </Type>
		    sb.append("<" + TYPE + ">" + barImg.getBarcodeType() + "</" + TYPE + ">");
		}
		if (barImg.getMessage() != null) {
		    // <Code> </Code>
		    sb.append("<" + CODE + ">" + barImg.getMessage() + "</" + CODE + ">");
		}
		if (barImg.getLocation() != null) {
		    // <URL> </URL>
		    sb.append("<" + URL + ">" + barImg.getLocation() + "</" + URL + ">");
		}
		// </Barcode>
		sb.append("</" + BARCODE + ">");
	    }
	    // </Barcodes>
	    sb.append("</" + BARCODES + ">");
	}

	// </GenerationReport>
	sb.append("</" + GENERATIONREPORT + ">");
	return sb.toString();
    }

    protected void checkNumberPage(String range, int numPages) throws ReportException {
	String numMaxStr = StaticSignatureReportProperties.getProperty(NUM_MAX_PAGE_KEY);
	if (numMaxStr == null || numMaxStr.trim().length() == 0) {
	    String msg = Language.getFormatMessage(LanguageKeys.RPT_006, new Object[ ] { NUM_MAX_PAGE_KEY });
	    LOGGER.error(msg);
	    throw new ReportException(ReportException.UNKNOWN_ERROR, msg);
	}

	int max = 0;
	try {
	    max = Integer.parseInt(numMaxStr.trim());
	} catch (NumberFormatException nfe) {
	    String msg = Language.getFormatMessage(LanguageKeys.RPT_007, new Object[ ] { numMaxStr.trim(), NUM_MAX_PAGE_KEY });
	    LOGGER.error(msg, nfe);
	    throw new ReportException(ReportException.UNKNOWN_ERROR, msg,nfe);
	}
	int pages = 0;
	if (range == null) {
	    pages = numPages;
	} else {
	    try {
		String[ ] p = range.trim().split(",");
		for (int i = 0; i < p.length; i++) {
		    if (p[i].indexOf('-') > 0) {
			String[ ] intval = p[i].split("-");
			int interval = (Integer.parseInt(intval[1]) - Integer.parseInt(intval[0]));
			if(interval < 0){
			    String msg = Language.getMessage(LanguageKeys.RPT_020);
			    LOGGER.error(msg);
			    throw new ReportException(ReportException.INVALID_TEMPLATE, msg);
			}else if(Integer.parseInt(intval[1])>numPages){
			    String msg = Language.getFormatMessage(LanguageKeys.RPT_021,new Object[]{String.valueOf(numPages)});
			    LOGGER.error(msg);
			    throw new ReportException(ReportException.INVALID_TEMPLATE, msg);
			}
			pages = pages + interval + 1;
		    } else {
			if(Integer.parseInt(p[i])>numPages){
			    String msg = Language.getFormatMessage(LanguageKeys.RPT_021,new Object[]{String.valueOf(numPages)});
			    LOGGER.error(msg);
			    throw new ReportException(ReportException.INVALID_TEMPLATE, msg);
			}
			pages++;
		    }
		}
	    } catch (NumberFormatException nfe) {
		String msg = Language.getMessage(LanguageKeys.RPT_020);
		LOGGER.error(msg,nfe);
		throw new ReportException(ReportException.INVALID_TEMPLATE, msg,nfe);
	    }
	}
	if (max < pages) {
	    String msg = Language.getMessage(LanguageKeys.RPT_008);
	    LOGGER.error(msg);
	    throw new ReportException(ReportException.OVER_MAX_PAGE_NUMBER, msg);
	}

    }
}
