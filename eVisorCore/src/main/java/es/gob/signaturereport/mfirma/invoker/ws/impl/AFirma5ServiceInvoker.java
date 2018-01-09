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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.invoker.ws.impl.AFirma5ServiceInvoker.java.</p>
 * <b>Description:</b><p> Class responsible for making a web service request issued by the platform "@firma v5".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 15/02/2011.
 */
package es.gob.signaturereport.mfirma.invoker.ws.impl;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.components.net.SocketFactoryFactory;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.soap.SOAPConstants;
import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.configuration.items.WSServiceData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.mfirma.invoker.AServiceInvoker;
import es.gob.signaturereport.mfirma.invoker.ServiceInvokerException;
import es.gob.signaturereport.mfirma.invoker.ws.ClientHandler;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Class responsible for making a web service request issued by the platform "@firma v5".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 15/02/2011.
 */
public class AFirma5ServiceInvoker extends AServiceInvoker {

	/**
	 * Constructor method for the class AFirma5ServiceInvoker.java.
	 * @param data Information for invocation  to '@firma' platform.
	 */
	public AFirma5ServiceInvoker(AfirmaData data) {
		super(data);
	}

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(AFirma5ServiceInvoker.class);

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.mfirma.invoker.ServiceInvokerI#invokeService(java.lang.String, java.lang.Object[])
	 */
	public Object invokeService(String serviceIdentifier, Object[ ] parameters) throws ServiceInvokerException {
		WSServiceData serviceData = serviceInvokerData.getServices().get(serviceIdentifier);
		if (serviceData == null) {
			String msg = Language.getFormatMessage(LanguageKeys.INVK_001, new Object[ ] { serviceIdentifier });
			LOGGER.error(msg);
			throw new ServiceInvokerException(ServiceInvokerException.INVALID_INPUT_PARAMETERS, msg);
		}

		if (parameters == null || (parameters.length != 1 || !(parameters[0] instanceof String))) {
			String msg = Language.getMessage(LanguageKeys.INVK_002);
			LOGGER.error(msg);
			throw new ServiceInvokerException(ServiceInvokerException.INVALID_INPUT_PARAMETERS, msg);
		}
		return invoke(serviceData.getServicesLocation(), serviceData.getOperationName(), (String) parameters[0], serviceInvokerData.getAuthenticationType(), serviceInvokerData.getAuthenticationUser(), serviceInvokerData.getAuthenticationPassword(), serviceData.getTimeOut());
	}

	/**
	 * Method that performs a web service request to the specified URL. 
	 * <br/>The input and output parameters are XML. The request may be securized by user-password or certificate.
	 * @param serviceLocation	Represents the URL location of web service.
	 * @param method		Represents the name of operation associated to web service.
	 * @param xmlRequest	XML request. This XML is the input parameter of web service.
	 * @param authMode		Indicates the authorization type of the web service request.
	 * @param user		Represents the authorization user of the web service request or the alias of certificate used to sign the SOAP request.
	 * @param password		Represents the authorization user password of the web service request.
	  * @param timeout		Timeout of service invocation.
	 * @return			XML that contains the result of invocation.
	 * @throws ServiceInvokerException	If an error occurs while invoking to the web service.
	 */
	private String invoke(String serviceLocation, String method, String xmlRequest, int authMode, String user, String password, Integer timeout) throws ServiceInvokerException {
		String response = null;
		ClientHandler handler = new ClientHandler(authMode);
		if (authMode == ConfigurationFacade.USER_PASS_AUTHENTICATION) {
			handler.setUser(user);
			handler.setPassword(password);
		} else if (authMode == ConfigurationFacade.CERTIFICATE_AUTHENTICATION) {
			handler.setKeystoreAlias(user);
			try {
				handler.setAliasPassword(ConfigurationFacade.getInstance().getKeystorePassword(ConfigurationFacade.SOAP_SIGNER_KEYSTORE));
				handler.setKeystorePath(ConfigurationFacade.getInstance().getKeystorePath(ConfigurationFacade.SOAP_SIGNER_KEYSTORE));
				handler.setKeystorePassword(handler.getAliasPassword());
				handler.setKeystoreType(ConfigurationFacade.getInstance().getKeystoreType(ConfigurationFacade.SOAP_SIGNER_KEYSTORE));
			} catch (ConfigurationException e) {
				String msg = Language.getMessage(LanguageKeys.INVK_008);
				LOGGER.error(msg, e);
				throw new ServiceInvokerException(ServiceInvokerException.UNKNOWN_ERROR, msg, e);
			}
		}
		Service service = new Service();

		try {
			if (serviceLocation != null && serviceLocation.toUpperCase().startsWith("HTTPS")) {

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
			Call call = (Call) service.createCall();
			call.setOperation(method);
			call.setOperationStyle(Style.RPC_STR);
			call.setOperationUse(Use.LITERAL_STR);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("");
			call.setEncodingStyle(null);
			call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
			call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
			call.setTargetEndpointAddress(serviceLocation);
			call.setOperationName(new QName("", method));
			call.setTimeout(timeout);
			call.setClientHandlers(handler, null);
			response = (String) call.invoke(new Object[ ] { xmlRequest });
		} catch (ServiceException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_002, e.getMessage());
			String msg = Language.getMessage(LanguageKeys.INVK_004);
			LOGGER.error(msg, e);
			throw new ServiceInvokerException(ServiceInvokerException.UNKNOWN_ERROR, msg, e);
		} catch (RemoteException e) {
			String msg = Language.getFormatMessage(LanguageKeys.INVK_005, new Object[ ] { method, serviceLocation });
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_002, msg + e.getMessage());
			LOGGER.error(msg, e);
			throw new ServiceInvokerException(ServiceInvokerException.UNKNOWN_ERROR, msg, e);
		}
		return response;
	}
}
