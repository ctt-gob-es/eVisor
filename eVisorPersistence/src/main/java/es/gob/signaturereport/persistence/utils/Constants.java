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
 * <b>File:</b><p>es.gob.signaturereport.tools.NumberConstants.java.</p>
 * <b>Description:</b><p>Interface that defines numeric constants for generalized usage..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Spanish Government.
 * @version 1.0.
 */
package es.gob.signaturereport.persistence.utils;

/**
 * <p>Interface that defines numeric constants for generalized usage.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/05/2013.
 */
public interface Constants {

	 /**
     * Constant attribute that represents the string <i>"yes_no"</i>.
     */
	public static String CONS_YES_NO = "yes_no";
	
	 /**
     * A constant representing the keystore used to sign SOAP message. 
     */
    String SOAP_SIGNER_KEYSTORE = "KEYSTORE SOAP";
    
    /**
     * A constant representing the keystore used to verify SOAP message. 
     */
    String SOAP_TRUSTED_KEYSTORE = "CONFIANZA SOAP";
   
    /**
     * A constant representing the keystore used to verify SSL connection. 
     */
    String SSL_TRUSTED_KEYSTORE = "CONFIANZA SSL";
    
    /**
     * Constant that identifies the keystore used to authorize the SOAP requests.
     */
    String SOAP_AUTH_KEYSTORE = "AUTORIZACION WS";
    
    /**
     * Attribute that represents the constant used to identify a key of certificate type. 
     */
    String X509_TYPE = "X.509";
    /**
     * Attribute that represents the constant used to identify a key of PKCS12 type. 
     */
    String PKCS12_TYPE = "PKCS12"; 
    
    /**
     * Attribute that represents the constant used to identify a key of JKS type. 
     */
    String JKS_TYPE = "JKS";
    
    /**
	 * Attribute that represents the Signature Report platform.
	 */
	public static final String SIGNATURE_REPORT_PLATFORM = "SignatureReport";

	/**
	 * Attribute that represents the Generation Report Service.
	 */
	public static final String GENERATION_REPORT_SERVICE = "GenerationReportService";
	
	/**
	 * Attribute that represents the Validation Report Service.
	 */
	public static final String VALIDATION_REPORT_SERVICE = "ValidationReportService";
	
	/**
	 * Constant indicating that the authorization of the web service request is made by user and password.
	 */
	public static final int USER_PASS_AUTHENTICATION = 2;
	/**
	 * Constant indicating that the authorization of the web service request is made by certificate.
	 */
	public static final int CERTIFICATE_AUTHENTICATION = 1;

	/**
	 * Constant that indicates that the web service request is without authentication.
	 */
	public static final int WITHOUT_AUTHENTICATION = 0;
	
	/**
    * Constants that identifies a report of PDF type. 
    */
   int PDF_REPORT = 0;
         
   /**
    * Attribute that represents that the report does not include the signed document. 
    */
   int INC_SIGNED_DOC_NONE = 0;
   
   /**
    * Attribute that represents that the report includes the signed document as image. 
    */
   int INC_SIGNED_DOC_IMAGE = 1;
   
   /**
    * Attribute that represents that the report includes the signed document embed into the report. 
    */
   int INC_SIGNED_DOC_EMBED = 2;
   
   /**
    * Attribute that represents that the report and signed document will be concatenated. 
    */
   int INC_SIGNED_DOC_CONCAT = 3;
   
   /**
    * Attribute that represents the identifier the report into the concatenation rule. 
    */
   String REPORT_CONTAT_ID = "REP";
   
   /**
    * Attribute that represents the identifier the document into the concatenation rule. 
    */
   String DOCUMENT_CONCAT_ID = "DOC";
	
   /**
    * Constant attribute that represents a delimiter for XPath.
    */
   public static final String PATH_DELIMITER = "/";

}
