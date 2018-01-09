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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.TemplateReader.java.</p>
 * <b>Description:</b><p>Class that provides the static files used to the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>31/03/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 31/03/2011.
 */
package es.gob.signaturereport.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/**
 * <p>Class that provides the static files used to the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 31/03/2011.
 */
public final class FileSystemReader {

	/**
	 * Attribute that represents the default buffer size. 
	 */
	private static final int DEFAULT_BUFFER_SIZE = 100000;
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(FileSystemReader.class);

	/**
	 * Attribute that represents an instance of class.
	 */
	private static FileSystemReader instance = null;

	/**
	 * Attribute that represents the path to template used to create XML request for DSS Validation Signature Service.
	 */
	private static final String DSS_VERIFY_TEMPLATE_PATH = "/transform/DSSVerifySignaturev5d5.xml";
	/**
	 * Attribute that represents the path to template used to create XML response for Generation Signature Report Service.
	 */
	private static final String GENERATION_TEMPLATE_PATH = "/transform/GenerationResponsev1d0.xml";

	/**
	 * Attribute that represents the path to template used to create XML response for Validation Signature Report Service.
	 */
	private static final String VALIDATION_TEMPLATE_PATH = "/transform/ValidationReportResponsev1d0.xml";

	/**
	 * Attribute that represents the template used to create XML request for DSS Validation Signature Service.
	 */
	private byte[ ] dssVerifyTemplate = null;

	/**
	 * Attribute that represents the template used to create XML response for Generation Signature Report Service.
	 */
	private byte[] generationTemplate = null;
	
	/**
	 * Attribute that represents the template used to create XML response for Validation Signature Report Service.
	 */
	private byte[] validationTemplate = null;
	


	/**
	 * Constructor method for the class FileSystemReader.java.
	 * @throws ToolsException If an error occurs.
	 */
	private FileSystemReader() throws ToolsException {
		load();
	}

	/**
	 * Method that provides an instance of the class.
	 * @return	FileSystemReader instance.
	 * @throws ToolsException	If an error occurs.
	 */
	public static FileSystemReader getInstance() throws ToolsException{
		if(instance == null){
			instance = new FileSystemReader();
		}
		return instance;
	}
	/**
	 * Load the content of files.
	 * @throws ToolsException If an error occurs.
	 */
	private void load() throws ToolsException {
		this.dssVerifyTemplate = getFile(DSS_VERIFY_TEMPLATE_PATH);
		this.generationTemplate = getFile(GENERATION_TEMPLATE_PATH);
		this.validationTemplate = getFile(VALIDATION_TEMPLATE_PATH);
	}

	/**
	 * Method that returns the array of bytes of supplied file.
	 * @param templatePath	Path of file to get.
	 * @return		Array of bytes of file.
	 * @throws ToolsException	If an error occurs while the system is reading the file.
	 */
	private byte[ ] getFile(String templatePath) throws ToolsException {
		InputStream in = FileSystemReader.class.getResourceAsStream(templatePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			if (in != null) {
				byte[ ] buff = new byte[DEFAULT_BUFFER_SIZE];
				int r = -1;
				while ((r = in.read(buff)) > 0) {
					baos.write(buff, 0, r);
				}
				return baos.toByteArray();
			} else {
				String msg = Language.getFormatMessage(LanguageKeys.UTIL_021, new Object[ ] { templatePath });
				LOGGER.error(msg);
				throw new ToolsException(ToolsException.ACCESS_FILE_ERROR, msg);
			}
		} catch (IOException e) {
			String msg = Language.getFormatMessage(LanguageKeys.UTIL_021, new Object[ ] { templatePath });
			LOGGER.error(msg,e);
			throw new ToolsException(ToolsException.ACCESS_FILE_ERROR, msg,e);
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					String msg = Language.getFormatMessage(LanguageKeys.UTIL_021, new Object[ ] { templatePath });
					LOGGER.error(msg,e);
				}
			}

			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					String msg = Language.getFormatMessage(LanguageKeys.UTIL_021, new Object[ ] { templatePath });
					LOGGER.error(msg,e);
				}
			}
		}


	}


	/**
	 * Gets the value of  the template used to create XML request for DSS Validation Signature Service.
	 * @return  The template used to create XML request for DSS Validation Signature Service.
	 */
	public byte[ ] getDssVerifyTemplate() {
		return dssVerifyTemplate;
	}


	/**
	 * Gets the value of  the template used to create XML response for Generation Signature Report Service.
	 * @return the value of  the template used to create XML response for Generation Signature Report Service.
	 */
	public byte[ ] getGenerationTemplate() {
		return generationTemplate;
	}
	
	/**
	 * Gets the value of  the template used to create XML response for Validation Signature Report Service.
	 * @return the value of  the template used to create XML response for Validation Signature Report Service.
	 */
	public byte[] getValidationTemplate() {
		return validationTemplate;
	}

}
