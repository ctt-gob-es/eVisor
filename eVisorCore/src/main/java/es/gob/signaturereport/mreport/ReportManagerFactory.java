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
 * <b>File:</b><p>es.gob.signaturereport.mreport.ReportManagerFactory.java.</p>
 * <b>Description:</b><p> Class that implements a factory to obtain instances AReportManager.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>21/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 21/02/2011.
 */
package es.gob.signaturereport.mreport;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mreport.pdf.PDFReportManager;


/**
 * <p>Class that implements a factory to obtain instances AReportManager.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/02/2011.
 */
public class ReportManagerFactory {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(ReportManagerFactory.class);

	/**
	 * Gets a AReportManager instance to create signature report.
	 * @param reportType	Report type. Allowed values:
	 * <br/> {@link TemplateConfigurationFacadeI#PDF_REPORT} - Manager to get is PDF manager.
	 * @return	{@link AReportManager} that manages the report operations.
	 * @throws ReportException	If an error occurs into creation process.
	 */
	public static ReportManagerI getReportManager(int reportType) throws ReportException{
		
			if(reportType == TemplateConfigurationFacadeI.PDF_REPORT){
				return new PDFReportManager();
			}else{
				String msg = Language.getFormatMessage(LanguageKeys.RPT_002,new Object[]{reportType});
				LOGGER.error(msg);
				throw new ReportException(ReportException.INVALID_INPUT_PARAMETERS, msg);
			}
		

	}

	/**
	 * Constructor method for the class ReportManagerFactory.java.
	 */
	private ReportManagerFactory() {
	}
}
