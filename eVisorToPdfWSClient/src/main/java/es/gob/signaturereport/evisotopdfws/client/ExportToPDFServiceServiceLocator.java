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
 * <b>File:</b><p>es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceServiceLocator.java.</p>
 * <b>Description:</b><p>Class that instances the service access.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisotopdfws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/** 
 * <p>Class that instances the service access.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 8/8/2016.
 */
public class ExportToPDFServiceServiceLocator extends org.apache.axis.client.Service implements es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceService {

	/**
	 * Attribute that represents . 
	 */
	private static final long serialVersionUID = -1978179794359909875L;

	/**
	 * Constructor method for the class ExportToPDFServiceServiceLocator.java. 
	 */
	public ExportToPDFServiceServiceLocator() {
	}

	/**
	 * Use to get a proxy class for ExportToPDFService. 
	 */
	private String exportToPDFServiceAddress = StaticSignatureReportProperties.getProperty("signaturereport.topdfws.endpoint");

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceService#getExportToPDFServiceAddress()
	 */
	public final String getExportToPDFServiceAddress() {
		return exportToPDFServiceAddress;
	}

	/**
	 * The WSDD service name defaults to the port name. 
	 */
	private String exportToPDFServiceWSDDServiceName = "ExportToPDFService";

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceService#getExportToPDFService()
	 */
	public final ExportToPDFService getExportToPDFService() throws ServiceException {
		URL endpoint;
		try {
			endpoint = new URL(exportToPDFServiceAddress);
		} catch (MalformedURLException e) {
			throw new ServiceException(e);
		}
		return getExportToPDFService(endpoint);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceService#getExportToPDFService(java.net.URL)
	 */
	public final ExportToPDFService getExportToPDFService(URL portAddress) throws ServiceException {
		try {
			ExportToPDFServiceSoapBindingStub stub = new ExportToPDFServiceSoapBindingStub(portAddress, this);
			stub.setPortName(exportToPDFServiceWSDDServiceName);
			return stub;
		} catch (AxisFault e) {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.client.Service#getPort(java.lang.Class)
	 */
	public final Remote getPort(Class serviceEndpointInterface) throws ServiceException {
		try {
			if (ExportToPDFService.class.isAssignableFrom(serviceEndpointInterface)) {
				ExportToPDFServiceSoapBindingStub stub = new ExportToPDFServiceSoapBindingStub(new URL(exportToPDFServiceAddress), this);
				stub.setPortName(exportToPDFServiceWSDDServiceName);
				return stub;
			}
		} catch (java.lang.Throwable t) {
			throw new ServiceException(t);
		}
		throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * {@inheritDoc}
	 * @see org.apache.axis.client.Service#getServiceName()
	 */
	public final QName getServiceName() {
		return new QName(exportToPDFServiceAddress, "ExportToPDFServiceService");
	}

}
