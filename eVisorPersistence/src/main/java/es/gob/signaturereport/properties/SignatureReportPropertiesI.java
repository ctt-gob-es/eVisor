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
 * <b>File:</b><p>es.gob.signaturereport.properties.SignatureReportPropertiesI.java.</p>
 * <b>Description:</b><p>Interface that provides methods and constants to manage the properties of the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/02/2011.
 */
package es.gob.signaturereport.properties;

/** 
 * <p>Interface that provides methods and constants to manage the properties of the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/02/2011.
 */
public interface SignatureReportPropertiesI {

    /**
     * Attribute that represents identifier of "XML_TRANSFORM" section. 
     */
    String TRANSFORM_SECTION_ID = "XML_TRANSFORM";
    
    /**
     * Attribute that represents identifier of "AFIRMA_INVOKER" section. 
     */
    String INVOKER_SECTION_ID = "AFIRMA_INVOKER";
    
   
    /**
     * Attribute that represents identifier of "KEYSTORES" section.
     */
    String KEYSTORE_SECTION_ID ="KEYSTORES";
    
    /**
     * Attribute that represents the identifier used to represents the general properties. 
     */
    String GLOBAL_SECTION_ID = "GLOBAL";

    /**
     * Attribute that represents identifier of "CACHE" section.
     */
    String CACHE_SECTION_ID = "CACHE";
    
    /**
     * Attribute that represents identifier of "ALARM" section.
     */
    String ALARM_SECTION_ID = "ALARM";
    
    /**
     * Attribute that represents identifier of "AUDIT" section. 
     */
    String AUDIT_SECTION_ID= "AUDIT";
    
    /**
     * Gets the value of property supplied. This property must be locate into section supplied.
     * @param sectionId		Identifier of section that contains the property.
     * @param propertyId	Identifier of property.
     * @return			Value of property. Null if the property is not found.
     */
    String getPropertyValue(String sectionId, String propertyId);
    
 
}
