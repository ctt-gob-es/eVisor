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
 * <b>File:</b><p>es.gob.signaturereport.tools.FOUtils.java.</p>
 * <b>Description:</b><p>Utility class for processing XSL-FO files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/03/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/03/2011.
 */
package es.gob.signaturereport.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <p>Utility class for processing XSL-FO files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/03/2011.
 */
public final class FOUtils {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(FOUtils.class);

	/**
	 * Constructor method for the class FOUtils.java. 
	 */
	private FOUtils() {
	}

	/**
	 * Creates a PDF file from a XSL-FO document.
	 * @param foFile	XSL-FO document.
	 * @return	PDF File.
	 * @throws ToolsException If an error occurs.
	 */
	public static byte[ ] fo2pdf(byte[ ] foFile) throws ToolsException {
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent fopUserAgent = fopFactory.newFOUserAgent();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = null;
		byte[ ] pdf = null;
		try {
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, fopUserAgent, out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			in = new ByteArrayInputStream(foFile);
			Source src = new StreamSource(in);
			Result res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(src, res);
			pdf = out.toByteArray();
			if (pdf != null) {
				FormattingResults foResults = fop.getResults();
				LOGGER.debug(Language.getFormatMessage(LanguageKeys.UTIL_010, new Object[ ] { String.valueOf(foResults.getPageCount()) }));
			}
			return pdf;
		} catch (TransformerException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_034);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.INVALID_FO_FILE, msg,e);
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_011);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg,e);
		} finally {
			UtilsResources.safeCloseInputStream(in);
			UtilsResources.safeCloseOutputStream(out);
		}
	}
}
