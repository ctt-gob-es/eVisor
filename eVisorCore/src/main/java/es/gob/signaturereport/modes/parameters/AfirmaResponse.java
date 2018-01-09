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
 * <b>File:</b><p>es.gob.signaturereport.modes.parameters.AfirmaRequest.java.</p>
 * <b>Description:</b><p>Class that contains a web service response returned by '@firma' platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>22/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 22/02/2011.
 */
package es.gob.signaturereport.modes.parameters;

import java.util.Arrays;

/** 
 * <p>Class that contains a web service response returned by '@firma' platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 22/02/2011.
 */
public class AfirmaResponse {

	/**
	 * Attribute that represents the SOAP response returned by '@firma' platform. 
	 */
	private byte[ ] soapResponse = null;

	/**
	 * Attribute that represents the XML response returned by '@firma' platform. 
	 */
	private String xmlResponse = null;

	/**
	 * Constructor method for the class AfirmaResponse.java.
	 * @param soap The SOAP response returned by '@firma' platform. 
	 */
	public AfirmaResponse(byte[ ] soap) {
		super();
		if (soap != null) {
			setSoapResponse(soap);
		}

	}

	/**
	 * Constructor method for the class AfirmaResponse.java.
	 * @param xml 	The XML response returned by '@firma' platform.
	 */
	public AfirmaResponse(String xml) {
		super();
		this.xmlResponse = xml;
	}

	/**
	 * Gets the value of the SOAP response returned by '@firma' platform. 
	 * @return the value of the SOAP response returned by '@firma' platform. 
	 */
	public byte[ ] getSoapResponse() {
		return soapResponse;
	}

	/**
	 * Sets the value of the SOAP response returned by '@firma' platform. 
	 * @param soap The value for the SOAP response returned by '@firma' platform. 
	 */
	public void setSoapResponse(byte[ ] soap) {
		if(soap!=null){
			this.soapResponse = Arrays.copyOf(soap,soap.length);
		}else{
			this.soapResponse = null;
		}
	}

	/**
	 * Gets the value of the XML response returned by '@firma' platform.
	 * @return the value of the XML response returned by '@firma' platform.
	 */
	public String getXmlResponse() {
		return xmlResponse;
	}

	/**
	 * Sets the value of the XML response returned by '@firma' platform.
	 * @param xml The value for the XML response returned by '@firma' platform.
	 */
	public void setXmlResponse(String xml) {
		this.xmlResponse = xml;
	}
}
