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
 * <b>File:</b><p>es.gob.signaturereport.mreport.ReportManagerI.java.</p>
 * <b>Description:</b><p>Interface that contains methods and constants used for the management signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>21/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 21/02/2011.
 */
package es.gob.signaturereport.mreport;

import java.util.ArrayList;
import java.util.HashMap;

import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.Barcode;


/** 
 * <p>Interface that contains methods and constants used for the management signature reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/03/2011.
 */
public interface ReportManagerI {
    
    
    
    /**
     * Create a signature report from the information supplied.
     * @param validationResponse	{@link ValidationSignatureResponse} that contains the validation results.
     * @param templateConf		{@link TemplateData} that contains the information about of template of generation.
     * @param signature			Electronic signature.
     * @param document			Signed document.
     * @param barcodes			Bar code to include into the signature report.
     * @param additionalParameters	Additional parameters included in the request.
     * @return				Report.
     * @throws ReportException		If an error occurs generating the report.
     */
   byte[ ] createReport(ValidationSignatureResponse validationResponse, TemplateData templateConf, byte[ ] signature, byte[ ] document, ArrayList<Barcode> barcodes, HashMap<String, String> additionalParameters) throws ReportException;


}
