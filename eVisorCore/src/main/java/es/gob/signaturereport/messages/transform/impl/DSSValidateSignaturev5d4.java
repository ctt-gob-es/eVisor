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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d4.java.</p>
 * <b>Description:</b><p> Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v.5.4.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>17/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 17/02/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;

/** 
 * <p>Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v.5.4.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 17/02/2011.
 */
public class DSSValidateSignaturev5d4 extends DSSValidateSignaturev5d3r1 {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(DSSValidateSignaturev5d4.class);

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d5#marshal(java.util.LinkedHashMap)
	 */
	@Override
	public String marshal(LinkedHashMap<String, String> parameters) throws TransformException {

		Document xml = getTemplate();

		ArrayList<String> nodeToRemove = new ArrayList<String>();

		super.fillDocument(xml, parameters, nodeToRemove);

		super.fillSignature(xml, parameters, nodeToRemove);

		this.fillOptionalOutputs(xml, parameters, nodeToRemove);

		return super.getStringXML(xml, nodeToRemove);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d3r1#fillOptionalOutputs(org.w3c.dom.Document, java.util.LinkedHashMap, java.util.ArrayList)
	 */
	@Override
	protected void fillOptionalOutputs(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		super.fillOptionalOutputs(xml, parameters, nodeToRemove);
		if (nodeToRemove.contains(RETURNSIGNEDDATAINFO_REQ)) {
			nodeToRemove.remove(RETURNSIGNEDDATAINFO_REQ);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d3r1#unmarshal(java.lang.String)
	 */
	@Override
	public LinkedHashMap<String, Object> unmarshal(String xml) throws TransformException {

		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();

		Document doc = null;
		try {
			doc = XMLUtils.getDocument(xml.getBytes("UTF-8"));
		} catch (ToolsException e) {
			LOGGER.error(e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, e.getDescription());
		} catch (UnsupportedEncodingException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_014);
			LOGGER.error(msg, e);
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}

		super.readGlobalResult(doc, response);

		super.readIndividualSignatureReport(doc, response);

		readSignedDocument(doc, response);

		return response;

	}

	/**
	 * This method includes the signed document into Map supplied.
	 * @param doc	XML document to search the signed document.
	 * @param outputs	Map to include signed document.
	 * @throws TransformException	There was an error processing the XML.
	 */
	protected void readSignedDocument(Document doc, LinkedHashMap<String, Object> outputs) throws TransformException {
		try {
			NodeList dataInfo = XMLUtils.getNodes(doc.getDocumentElement(), DATAINFO_RES);
			if (dataInfo != null && dataInfo.getLength() == 1) {
				String content = XMLUtils.getNodeValue(dataInfo.item(0), CONTENT_DATA_RES);
				if (content != null) {
					outputs.put(CONTENT_DATA_RES, content);
				} else {
					NodeList xpathNodeList = XMLUtils.getNodes(dataInfo.item(0), REFS_XPATH_RES);
					if(xpathNodeList!=null && xpathNodeList.getLength()>0){
						ArrayList<String> xpathList = new ArrayList<String>();
						for(int i=0;i<xpathNodeList.getLength();i++){
							if(xpathNodeList.item(i).getFirstChild()!=null){
								xpathList.add(xpathNodeList.item(i).getFirstChild().getNodeValue());
							}
						}
						outputs.put(REFS_XPATH_RES, xpathList);
					}
				}
			}
		} catch (ToolsException e) {
			LOGGER.error(e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, e.getDescription());

		}
		return;
	}

}
