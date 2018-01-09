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
 * <b>File:</b><p>es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceProxy.java.</p>
 * <b>Description:</b><p>Class that implemets access to WS for export to pdf binding stub.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisotopdfws.client;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

/** 
 * <p>Class that implemets access to WS for export to pdf binding stub.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 8/8/2016.
 */
public class ExportToPDFServiceSoapBindingStub extends org.apache.axis.client.Stub implements es.gob.signaturereport.evisotopdfws.client.ExportToPDFService {

	/**
	 * Attribute that represents cachedSerClasses. 
	 */
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	
	/**
	 * Attribute that represents cachedSerQNames. 
	 */
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	
	/**
	 * Attribute that represents cachedSerFactories. 
	 */
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	
	/**
	 * Attribute that represents cachedDeserFactories. 
	 */
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	/**
	 * Attribute that represents operations. 
	 */
	private static OperationDesc[ ] operations;

	static {
		operations = new org.apache.axis.description.OperationDesc[1];
		initOperationDesc1();
	}

	/**
	 * Method that init operations.
	 */
	private static void initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("exportToPDF");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "document"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary"), byte[ ].class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "base64Binary"));
		oper.setReturnClass(byte[ ].class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "exportToPDFReturn"));
		oper.setStyle(org.apache.axis.constants.Style.RPC);
		oper.setUse(org.apache.axis.constants.Use.ENCODED);
		operations[0] = oper;

	}


	/**
	 * Constructor method for the class ExportToPDFServiceSoapBindingStub.java.
	 * @param endpointURL 
	 * @param service 
	 * @throws AxisFault 
	 */
	public ExportToPDFServiceSoapBindingStub(URL endpointURL, Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	/**
	 * Constructor method for the class ExportToPDFServiceSoapBindingStub.java.
	 * @param service 
	 * @throws AxisFault 
	 */
	public ExportToPDFServiceSoapBindingStub(Service service) throws AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
	}

	/**
	 * Creates call of WS.
	 * @return Call instance.
	 * @throws java.rmi.RemoteException 
	 */
	protected final Call createCall() throws RemoteException {
		try {
			Call call = super._createCall();
			if (super.maintainSessionSet) {
				call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				call.setPortName(super.cachedPortName);
			}
			Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				call.setProperty(key, super.cachedProperties.get(key));
			}
			return call;
		} catch (Throwable t) {
			throw new AxisFault("Failure trying to get the Call object", t);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.evisotopdfws.client.ExportToPDFService#exportToPDF(byte[])
	 */
	public final byte[ ] exportToPDF(byte[ ] document) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call call = createCall();
		call.setOperation(operations[0]);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI("");
		call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		call.setOperationName(new QName("http://ws.evisortopdfws.signaturereport.gob.es", "exportToPDF"));

		setRequestHeaders(call);
		setAttachments(call);
		try {
			Object resp = call.invoke(new Object[ ] { document });

			if (resp instanceof RemoteException) {
				throw (RemoteException) resp;
			} else {
				extractAttachments(call);
				try {
					return (byte[ ]) resp;
				} catch (Exception e) {
					return (byte[ ]) JavaUtils.convert(resp, byte[ ].class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}
