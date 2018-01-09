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
 * <b>Description:</b><p>Class that implemets access to WS for export to pdf.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisotopdfws.client;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.SocketFactoryFactory;

/** 
 * <p>Class that implemets access to WS for export to pdf.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 8/8/2016.
 */
public class ExportToPDFServiceProxy implements es.gob.signaturereport.evisotopdfws.client.ExportToPDFService {

	/**
	 * Attribute that represents endpoint of service. 
	 */
	private String endpoint = null;

	/**
	* Attribute that represents the instance of service. 
	*/
	private es.gob.signaturereport.evisotopdfws.client.ExportToPDFService exportToPDFService = null;

	/**
	* Constructor method for the class ExportToPDFServiceProxy.java. 
	*/
	public ExportToPDFServiceProxy() {
		initExportToPDFServiceProxy();
	}

	/**
	* Constructor method for the class ExportToPDFServiceProxy.java.
	* @param endpointParam endpoint of service
	*/
	public ExportToPDFServiceProxy(String endpointParam) {
		endpoint = endpointParam;
		initExportToPDFServiceProxy();
	}

	/**
	* Method that initializes the service.
	*/
	private void initExportToPDFServiceProxy() {
		try {
			exportToPDFService = (new es.gob.signaturereport.evisotopdfws.client.ExportToPDFServiceServiceLocator()).getExportToPDFService();
			if (exportToPDFService != null) {
				if (endpoint != null)
					((javax.xml.rpc.Stub) exportToPDFService)._setProperty("javax.xml.rpc.service.endpoint.address", endpoint);
				else
					endpoint = (String) ((javax.xml.rpc.Stub) exportToPDFService)._getProperty("javax.xml.rpc.service.endpoint.address");
			}

			if (endpoint != null && endpoint.toUpperCase().startsWith("HTTPS")) {

				Field field;
				try {
					field = SocketFactoryFactory.class.getDeclaredField("factories");
					field.setAccessible(true);
					Map factories = (Map) field.get(null);
					factories.put("https", new es.gob.signaturereport.evisotopdfws.client.CustomSSLSocketFactory(null));
					AxisProperties.setProperty("axis.socketSecureFactory", "es.gob.signaturereport.evisotopdfws.client.CustomSSLSocketFactory", true);
				} catch (Exception e) {
					// Vacío intencionadamente
				}
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {}
	}

	/**
	* Methos that gets the endpoint of service.
	* @return endpoint of service.
	*/
	public final String getEndpoint() {
		return endpoint;
	}

	/**
	* Methos that sets the endpoint of service.
	* @param endpointParam endpoint of service.
	*/
	public final void setEndpoint(String endpointParam) {
		endpoint = endpointParam;
		if (exportToPDFService != null)
			((javax.xml.rpc.Stub) exportToPDFService)._setProperty("javax.xml.rpc.service.endpoint.address", endpointParam);

	}

	/**
	 * Method that gets a instance of service.
	 * @return instance of service.
	 */
	public final es.gob.signaturereport.evisotopdfws.client.ExportToPDFService getExportToPDFService() {
		if (exportToPDFService == null)
			initExportToPDFServiceProxy();
		return exportToPDFService;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.evisotopdfws.client.ExportToPDFService#exportToPDF(byte[])
	 */
	public final byte[ ] exportToPDF(byte[ ] document) throws java.rmi.RemoteException {
		if (exportToPDFService == null)
			initExportToPDFServiceProxy();
		return exportToPDFService.exportToPDF(document);
	}

}