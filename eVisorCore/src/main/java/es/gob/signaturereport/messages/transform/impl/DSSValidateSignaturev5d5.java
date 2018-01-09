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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d5.java.</p>
 * <b>Description:</b><p> Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v.5.5.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/02/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.exception.TransformException;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;

/** 
 * <p>Class that manages the creating and reading of XML associated to web service of DSS validation signature public by "@firma" v5.5.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/02/2011.
 */
public class DSSValidateSignaturev5d5 extends DSSValidateSignaturev5d4 {

	/**
	* Attribute that represents the object that manages the log of the class. 
	*/
	private static final Logger LOGGER = Logger.getLogger(DSSValidateSignaturev5d5.class);

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d4#marshal(java.util.LinkedHashMap)
	 */
	@Override
	public String marshal(LinkedHashMap<String, String> parameters) throws TransformException {

		Document xml = getTemplate();

		ArrayList<String> nodeToRemove = new ArrayList<String>();

		this.fillDocument(xml, parameters, nodeToRemove);

		this.fillSignature(xml, parameters, nodeToRemove);

		super.fillOptionalOutputs(xml, parameters, nodeToRemove);

		return super.getStringXML(xml, nodeToRemove);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d3r1#fillSignature(org.w3c.dom.Document, java.util.LinkedHashMap, java.util.ArrayList)
	 */
	@Override
	protected void fillSignature(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		try {

			if (parameters.containsKey(SIG_OBJECTID_REQ) && parameters.containsKey(SIG_REPOSITORYID_REQ)) {
				String uuid = parameters.get(SIG_OBJECTID_REQ);
				String repository = parameters.get(SIG_REPOSITORYID_REQ);
				XMLUtils.includeElementValue(xml, SIG_OBJECTID_REQ, uuid, Node.TEXT_NODE);
				XMLUtils.includeElementValue(xml, SIG_REPOSITORYID_REQ, repository, Node.TEXT_NODE);
				nodeToRemove.add(B64SIGNATURE_REQ);
				nodeToRemove.add(DOCUMENT_WITH_ID_REQ);
				nodeToRemove.add(SIG_XMLDSIG_REQ);
				nodeToRemove.add(SIGNATUREPTR_REQ);
			} else {
				super.fillSignature(xml, parameters, nodeToRemove);
			}

		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_010);
			LOGGER.error(msg + e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
		return;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.messages.transform.impl.DSSValidateSignaturev5d3r1#fillDocument(org.w3c.dom.Document, java.util.LinkedHashMap, java.util.ArrayList)
	 */
	@Override
	protected void fillDocument(Document xml, LinkedHashMap<String, String> parameters, ArrayList<String> nodeToRemove) throws TransformException {
		try {
			if (parameters.containsKey(DOCUMENT_REPOSITORYID_REQ) && parameters.containsKey(DOCUMENT_UUID_REQ)) {
				String uuid = parameters.get(DOCUMENT_UUID_REQ);
				String repository = parameters.get(DOCUMENT_REPOSITORYID_REQ);
				XMLUtils.includeElementValue(xml, SIG_OBJECTID_REQ, uuid, Node.TEXT_NODE);
				XMLUtils.includeElementValue(xml, SIG_REPOSITORYID_REQ, repository, Node.TEXT_NODE);
				nodeToRemove.add(BASE64DATA_REQ);
				nodeToRemove.add(BASE64XML_REQ);
			} else {
				super.fillDocument(xml, parameters, nodeToRemove);
			}
		} catch (ToolsException e) {
			String msg = Language.getMessage(LanguageKeys.TRAN_010);
			LOGGER.error(msg + e.getDescription());
			throw new TransformException(TransformException.UNKNOWN_ERROR, msg);
		}
	}

}
