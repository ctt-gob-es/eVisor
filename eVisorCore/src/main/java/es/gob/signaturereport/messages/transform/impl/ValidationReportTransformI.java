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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.impl.ValidationReportTransformI.java.</p>
 * <b>Description:</b><p> Interface that contains methods and constants for processing Web Service Message associated to Signature Report Validation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>26/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 26/05/2011.
 */
package es.gob.signaturereport.messages.transform.impl;

import es.gob.signaturereport.messages.transform.TransformI;


/** 
 * <p>Interface that contains methods and constants for processing Web Service Message associated to Signature Report Validation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 26/05/2011.
 */
public interface ValidationReportTransformI extends TransformI{

	/**
	 * Attribute that represents the path location to 'ApplicationId' element. 
	 */
	String APPLICATIONID = "/*[local-name()='ValidationReportRequest']/*[local-name()='ApplicationId']";
    
	/**
	 * Attribute that represents the path location to 'Report' element. 
	 */
	String REPORT = "/*[local-name()='ValidationReportRequest']/*[local-name()='Report']";
    
    /**
     * Attribute that represents the path location to 'Code' element.
     */
    String RESULTCODE = "/srsm:ValidationReportResponse/srsm:Result/srsm:Code";
    
    /**
     * Attribute that represents the path location to 'Message' element. 
     */
    String RESULTMESSAGE = "/srsm:ValidationReportResponse/srsm:Result/srsm:Message";
    
    /**
     * Attribute that represents the path location to 'Cause' element. 
     */
    String RESULTCAUSE = "/srsm:ValidationReportResponse/srsm:Result/srsm:Cause";
    
}
