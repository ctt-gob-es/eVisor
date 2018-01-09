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
 * <b>File:</b><p>es.gob.signaturereport.modes.responses.ReportGenerationResponse.java.</p>
 * <b>Description:</b><p> Class that contains the information of the generation report result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.modes.responses;

import java.util.Arrays;

/** 
 * <p>Class that contains the information of the generation report result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
public class ReportGenerationResponse {
    
    /**
     * Attribute that represents that the process has been satisfactory. 
     */
    public static final int PROCESS_OK = 0;

    /**
     * Attribute that represents that a unknown error has occurred.
     */
    public static final int UNKNOWN_ERROR = -1;
    
    /**
     * Attribute that represents the parameters included into request are not valid. 
     */
    public static final int INVALID_INPUT_PARAMETERS = 1;

    
    /**
     * Attribute that represents the signature supplied is invalid. 
     */
    public static final int INVALID_SIGNATURE= 2;

    /**
     * Attribute that represents the result is invalid for the SOAP signature is not correct. 
     */
    public static final int INVALID_SOAP_SIGNATURE = 3;
    
    /**
     * Attribute that represents the original document is not valid. 
     */
    public static final int INVALID_SIGNED_DOCUMENT = 4;
    
    /**
	 * Attribute that represents that the "@firma" server is unavailable. 
	 */
	public static final int AFIRMA_UNAVAILABLE = 5;
    
  
    /**
     * Attribute that represents the template is not valid. 
     */
    public static final int INVALID_TEMPLATE = 6;
    
    /**
     * Attribute that represents the number of pages of document is not valid from the number of pages to extract as image. 
     */
    public static final int INVALID_PAGE_NUMBER = 7;

	
    
    /**
    * Attribute that represents the result code of generation report process . 
    */
    private int code = UNKNOWN_ERROR;

    /**
     * Attribute that represents the description of process result. 
     */
    private String message = null;
    
    /**
     * Attribute that represents a signature report. 
     */
    private byte[] report = null;
    
    /**
     * Gets the value of the message result.
     * @return the value of the message result.
     */
    public String getMessage() {
        return message;
    }


    
    /**
     * Sets the value of the message result.
     * @param resultMessage The value for the message result.
     */
    public void setMessage(String resultMessage) {
        this.message = resultMessage;
    }


    /**
     * Gets the value of the result code.
     * @return the value of the result code.
     */
    public int getCode() {
        return code;
    }

    
    /**
     * Sets the value of the result code.
     * @param resultCode The value for the result code.
     */
    public void setCode(int resultCode) {
        this.code = resultCode;
    }

    /**
     * Constructor method for the class ReportGenerationResponse.java.
     */
    public ReportGenerationResponse() {

    }



    
    /**
     * Gets the value of a signature report.
     * @return the value a signature report.
     */
    public byte[ ] getReport() {
        return report;
    }



    
    /**
     * Sets the value of a signature report.
     * @param signatureReport The value for a signature report.
     */
    public void setReport(byte[ ] signatureReport) {
    	if(signatureReport!=null){
    		this.report = Arrays.copyOf(signatureReport,signatureReport.length);
    	}else{
    		this.report =null;
    	}
    }

}
