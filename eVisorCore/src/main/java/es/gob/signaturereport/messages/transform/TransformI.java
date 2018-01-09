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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.TransformI.java.</p>
 * <b>Description:</b><p>Interface that provides constants and methods for create and read a XML message.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/02/2011.
 */
package es.gob.signaturereport.messages.transform;

import java.util.LinkedHashMap;

import es.gob.signaturereport.messages.transform.exception.TransformException;


/** 
 * <p>Interface that provides constants and methods for create and read a XML message.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/02/2011.
 */
public interface TransformI {
    
    /**
     * Method that creates a XML from the information included into supplied map.
     * @param parameters	Information to serialize. The content is: <br/>
     * key: xpath to element, value: value of element.			
     * @return			XML accords to information supplied.
     * @throws	TransformException		Error in the creating process.
     */
    String marshal(LinkedHashMap<String, String> parameters) throws TransformException;
    
    /**
     * Gets the information contains a XML.
     * @param xml	XML.
     * @return		Map that contains the information of XML.The content is: <br/>
     * key: xpath to element, value: value of element.	
     * @throws	TransformException	Error in the reading process.
     */
    LinkedHashMap<String, Object> unmarshal(String xml) throws TransformException;
}
