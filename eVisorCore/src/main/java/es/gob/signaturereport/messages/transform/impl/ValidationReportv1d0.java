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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.ValidationReportv1d0.java.</p>
 * <b>Description:</b><p>Class to read a XML request and create a XML response as specified by the validation report service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>26/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 26/05/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.tools.FileSystemReader;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;


/** 
 * <p>Class to read a XML request and create a XML response as specified by the validation report service.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 26/05/2011.
 */
public class ValidationReportv1d0 implements ValidationReportTransformI {
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(ValidationReportv1d0.class);

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#marshal(java.util.LinkedHashMap)
	 */
	public String marshal(LinkedHashMap<String, String> parameters) throws TransformException {
		Document doc = getTemplate();

		try {
			String code = parameters.remove(RESULTCODE);
			if (code != null) {
				XMLUtils.includeElementValue(doc, RESULTCODE, code, Node.TEXT_NODE);
			}
			String message = parameters.remove(RESULTMESSAGE);
			if(message != null){
				XMLUtils.includeElementValue(doc, RESULTMESSAGE, message, Node.TEXT_NODE);
			}
			String cause = parameters.remove(RESULTCAUSE);
			if(cause != null){
				XMLUtils.includeElementValue(doc, RESULTMESSAGE, cause, Node.TEXT_NODE);
			}else{
				ArrayList<String> toRemove = new ArrayList<String>();
				toRemove.add(RESULTCAUSE);
				XMLUtils.removeNodes(doc, toRemove);
			}
			return new String(XMLUtils.getXMLBytes(doc),"UTF-8");
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_008);
			LOGGER.error(msg,e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.TransformI#unmarshal(java.lang.String)
	 */
	public LinkedHashMap<String, Object> unmarshal(String xml) throws TransformException {
		try {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			Document doc = XMLUtils.getDocument(xml.getBytes("UTF-8"));

			String appId = XMLUtils.getNodeValue(doc.getDocumentElement(), APPLICATIONID);
			if (appId != null) {
				response.put(APPLICATIONID, appId);
			}
			
			String encodedReport = XMLUtils.getNodeValue(doc.getDocumentElement(), REPORT);
			if(encodedReport != null){
				response.put(REPORT, encodedReport);
			}
			return response;
		} catch (ToolsException e) {
			LOGGER.error(e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, e.getDescription());
		} catch (UnsupportedEncodingException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_014);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}
	/**
	 * Gets a document element used as creation template.
	 * @return	Template as DOM document.
	 * @throws TransformException If an error occurs getting the template.
	 */
	protected Document getTemplate() throws TransformException {
		try {
			return XMLUtils.getDocument(FileSystemReader.getInstance().getValidationTemplate());
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_006);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.TEMPLATE_ERROR, msg);
		}
	}
}
