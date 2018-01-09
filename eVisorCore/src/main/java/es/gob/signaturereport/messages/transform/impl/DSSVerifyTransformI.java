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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI.java.</p>
 * <b>Description:</b><p> Interface that provides constants for create and read a message accords to web services of DSS validation signature public by "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/02/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import es.gob.signaturereport.messages.transform.TransformI;

/** 
 * <p>Interface that provides constants for create and read a message accords to web services of DSS validation signature public by "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/02/2011.
 */
public interface DSSVerifyTransformI extends TransformI {
    
    /**
     * Attribute that represents the URI that indicate the signature is valid. 
     */
    String VALID_SIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:ValidSignature";
    
    /**
     * Attribute that represents the URI that indicate the signature is invalid. 
     */
    String INVALID_SIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:InvalidSignature";
    
    /**
     * Attribute that represents the URI that indicate the result of validation process is warning. 
     */
    String WARNING_SIGNATURE = "urn:oasis:names:tc:dss:1.0:resultmajor:Warning";
    
    /**
     * Attribute that represents the constant used to identify the XPATH to objectId element included into InputDocuments element. 
     */
    String DOCUMENT_UUID_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Other/*[local-name()='getContentStream']/*[local-name()='cmism:objectId']";

    /**
     * Attribute that represents the constant used to identify the XPATH to repositoryId element included into InputDocuments element. 
     */
    String DOCUMENT_REPOSITORYID_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Other/*[local-name()='getContentStream']/*[local-name()='cmism:repositoryId']";
    /**
     * Attribute that represents the constant used to identify the XPATH to Other element included into VerifyRequest element. 
     */
    String DOCUMENT_OTHER_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Other";
    /**
     * Attribute that represents the constant used to identify the XPATH to Base64XML element included into VerifyRequest. 
     */
    String BASE64XML_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Document[not(@ID)]/dss:Base64XML";

    /**
     * Attribute that represents the XPATH to Document element (included into VerifyRequest) that contains the ID attribute. 
     */
    String DOCUMENT_WITH_ID_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Document[@ID]";

    /**
     * Attribute that represents the constant used to identify the XPATH to Base64XML element included into Document that contains ID attribute. 
     */
    String BASE64XML_WITH_ID_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Document[@ID]/dss:Base64XML";

    /**
     * Attribute that represents the XPATH to ID attribute contains into Document element (included into VerifyRequest). 
     */
    String DOCUMENT_ATT_ID_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Document/@ID";

    /**
     * Attribute that represents the constant used to identify the XPATH to Base64XML element included into VerifyRequest element. 
     */
    String BASE64DATA_REQ = "/dss:VerifyRequest/dss:InputDocuments/dss:Document/dss:Base64Data";

    /**
     * Attribute that represents the constant used to identify the XPATH to Base64Signature element included into SignatureObject element. 
     */
    String B64SIGNATURE_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:Base64Signature";

    /**
     * Attribute that represents the constant used to identify the XPATH to WhichDocument attribute included into SignaturePtr element. 
     */
    String SIGNATUREPTR_ATT_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:SignaturePtr/@WhichDocument";

    /**
     * Attribute that represents the constant used to identify the XPATH to SignaturePtr element included into SignatureObject element. 
     */
    String SIGNATUREPTR_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:SignaturePtr";

    /**
     * Attribute that represents the constant used to identify the XPATH to Other element included into InputDocuments element. 
     */
    String SIG_OTHER_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:Other";

    /**
     * Attribute that represents the constant used to identify the XPATH to repositoryId element included into InputDocuments element. 
     */
    String SIG_REPOSITORYID_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:Other/*[local-name()='getContentStream']/*[local-name()='repositoryId']";

    /**
     * Attribute that represents the constant used to identify the XPATH to objectId element included into InputDocuments element. 
     */
    String SIG_OBJECTID_REQ = "/dss:VerifyRequest/dss:SignatureObject/dss:Other/*[local-name()='getContentStream']/*[local-name()='objectId']";

    /**
     * Attribute that represents the constant used to identify the XPATH to Name element included into ClaimedIdentity element.
     */
    String APP_NAME_REQ = "/dss:VerifyRequest/dss:OptionalInputs/dss:ClaimedIdentity/dss:Name";

    /**
     * Attribute that represents the constant used to identify the XPATH to Signature element included into SignatureObject element. 
     */
    String SIG_XMLDSIG_REQ = "/dss:VerifyRequest/dss:SignatureObject/*[local-name()='Signature']";

    /**
     * Attribute that represents the constant used to identify the XPATH to SignatureObject element included into VerifyRequest element. 
     */
    String SIGNATURE_OBJECT_REQ = "/dss:VerifyRequest/dss:SignatureObject";

    /**
     * Attribute that represents the constant used to identify the XPATH to ReturnSignedDataInfo element included into VerifyRequest element. 
     */
    String RETURNSIGNEDDATAINFO_REQ = "/dss:VerifyRequest/dss:OptionalInputs/*[local-name()='ReturnSignedDataInfo']";

    /**
     * Attribute that represents the constant used to identify the XPATH to Result element included into VerifyResponse element. 
     */
    String ABS_RESULT_RES = "/*[local-name()='VerifyResponse']/*[local-name()='Result']";

    /**
     * Attribute that represents the constant used to identify the XPATH to Result element included into unknown element. 
     */
    String PART_RESULT_RES = "*[local-name()='Result']";

    /**
     * Attribute that represents the constant used to identify the XPATH to ResultMajor element included into VerifyResponse element. 
     */
    String RESULT_MAJOR_RES = "*[local-name()='ResultMajor']";

    /**
     * Attribute that represents the constant used to identify the XPATH to ResultMinor element included into VerifyResponse element. 
     */
    String RESULT_MINOR_RES = "*[local-name()='ResultMinor']";

    /**
     * Attribute that represents the constant used to identify the XPATH to ResultMessage element included into VerifyResponse element. 
     */
    String RESULT_MSG_RES = "*[local-name()='ResultMessage']";

    /**
     * Attribute that represents the constant used to identify the XPATH to ReadableCertificateInfo element included into IndividualSignatureReport element. 
     */
    String CERT_INFO_RES = "*[local-name()='Details']/*[local-name()='ReadableCertificateInfo']";

    /**
     * Attribute that represents the constant used to identify the XPATH to ResultMessage element included into IndividualSignatureReport element. 
     */
    String IND_SIG_REPORT_RES = "/*[local-name()='VerifyResponse']/*[local-name()='OptionalOutputs']/*[local-name()='VerificationReport']/*[local-name()='IndividualSignatureReport']";

    /**
     * Attribute that represents the local name of FieldIdentity element. 
     */
    String FIELDIDENTITY_NAME = "FieldIdentity";

    /**
     * Attribute that represents the local name of FieldValue element.
     */
    String FIELDVALUE_NAME = "FieldValue";

    /**
     * Attribute that represents the constant used to identify the XPATH to CreationTime element included into IndividualSignatureReport element. 
     */
    String CREATION_TIME_RES = "*[local-name()='Details']/*[local-name()='DetailedReport']/*[local-name()='Properties']/*[local-name()='UnsignedProperties']/*[local-name()='UnsignedSignatureProperties']/*[local-name()='SignatureTimeStamp']/*[local-name()='TimeStampContent']/*[local-name()='CreationTime']";

    /**
     * Attribute that represents the constant used to identify the XPATH to X509SerialNumber element included into IndividualSignatureReport element. 
     */
    String SIGNER_CERT_SN_RES = "*[local-name()='Details']/*[local-name()='DetailedReport']/*[local-name()='CertificatePathValidity']/*[local-name()='CertificateIdentifier']/*[local-name()='X509SerialNumber']";

    /**
     * Attribute that represents the constant used to identify the XPATH to PathValidityDetail element included into IndividualSignatureReport element. 
     */
    String PATHVALIDITYDETAIL_RES = "*[local-name()='Details']/*[local-name()='DetailedReport']/*[local-name()='CertificatePathValidity']/*[local-name()='PathValidityDetail']";

    /**
     * Attribute that represents the constant used to identify the XPATH to CertificateValidity element included into PathValidityDetail element. 
     */
    String CERTIFICATEVALIDITY_RES = "*[local-name()='CertificateValidity']";
    /**
     * Attribute that represents the constant used to identify the XPATH to X509SerialNumber element included into CertificateValidity element. 
     */
    String X509SERIALNUMBER_RES = "*[local-name()='CertificateIdentifier']/*[local-name()='X509SerialNumber']";
    /**
     * Attribute that represents the constant used to identify the XPATH to CertificateValue element included into CertificateValidity element. 
     */
    String CERTIFICATE_VALUE_RES = "*[local-name()='CertificateValue']";
    /**
     * Attribute that represents the constant used to identify the XPATH to DataInfo element included into VerifyResponse element. 
     */
    String DATAINFO_RES = "/*[local-name()='VerifyResponse']/*[local-name()='OptionalOutputs']/*[local-name()='SignedDataInfo']/*[local-name()='DataInfo']";
    /**
     * Attribute that represents the constant used to identify the XPATH to ContentData element included into DataInfo element. 
     */
    String CONTENT_DATA_RES = "*[local-name()='ContentData']/*[local-name()='BinaryValue']";
    
    /**
     * Attribute that represents the constant used to identify the XPATH to "afxp:XPath" element included into DataInfo element. 
     */
    String REFS_XPATH_RES ="*[local-name()='SignedDataRefs']/*/*[local-name()='XPath']";
    /**
     * Attribute that represents the constant used to identify the {@link Map} that contains the document included into the XML signature. 
     */
	String REFS_XPATH_RES_AND_CONTENT = "*[local-name()='SignedDataRefs']/*/*[local-name()='XPath']_And_Contents";
}
