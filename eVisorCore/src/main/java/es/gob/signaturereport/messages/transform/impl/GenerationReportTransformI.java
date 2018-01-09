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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.GenerationReportTransformI.java.</p>
 * <b>Description:</b><p> Interface that contains methods and constants for processing Web Service Message associated to Signature Report Generation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>31/03/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 31/03/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import es.gob.signaturereport.messages.transform.TransformI;


/** 
 * <p>Interface that contains methods and constants for processing Web Service Message associated to Signature Report Generation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 31/03/2011.
 */
public interface GenerationReportTransformI extends TransformI {

    String APPLICATIONID = "/*[local-name()='GenerationRequest']/*[local-name()='ApplicationId']";
    
    String TEMPLATEID = "/*[local-name()='GenerationRequest']/*[local-name()='TemplateId']";
    
    String SIGNATURE = "/*[local-name()='GenerationRequest']/*[local-name()='Signature']";
    
    String ENCODEDSIGNATURE = "*[local-name()='EncodedSignature']";
    
    String REPOSITORYLOCATION = "*[local-name()='RepositoryLocation']";
    
    String REPOSITORYID = "*[local-name()='RepositoryId']";
    
    String OBJECTID = "*[local-name()='ObjectId']";
    
    String VALIDATIONRESPONSE = "*[local-name()='ValidationResponse']";
    
    String ENCODEDDOCUMENT = "*[local-name()='EncodedDocument']";
    
    String DOCUMENT = "/*[local-name()='GenerationRequest']/*[local-name()='Document']";
    
    String INCLUDESIGNATURE = "/*[local-name()='GenerationRequest']/*[local-name()='IncludeSignature']";
    
    String BARCODE = "/*[local-name()='GenerationRequest']/*[local-name()='Barcodes']/*[local-name()='Barcode']";
    
    String TYPE = "*[local-name()='Type']";
    
    String MESSAGE = "*[local-name()='Message']";
    
    String CONFIGURATIONPARAMS = "*[local-name()='Configuration']/*[local-name()='Parameter']";
    
    String EXTERNALPARAMS = "/*[local-name()='GenerationRequest']/*[local-name()='ExternalParameters']/*[local-name()='Parameter']";
    
    String PARAMETERID = "*[local-name()='ParameterId']";
    
    String PARAMETERVALUE = "*[local-name()='ParameterValue']";
    
    String RESULTCODE = "/srsm:GenerationResponse/srsm:Result/srsm:Code";
    
    String RESULTMESSAGE = "/srsm:GenerationResponse/srsm:Result/srsm:Message";
    
    String REPORT = "/srsm:GenerationResponse/srsm:Report";
    
    
}
