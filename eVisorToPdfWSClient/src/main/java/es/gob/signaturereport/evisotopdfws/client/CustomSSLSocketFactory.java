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
 * <b>File:</b><p>es.gob.signaturereport.evisotopdfws.client.ExportToPDFService.java.</p>
 * <b>Description:</b><p>Interface that defines exportToPDF utils from WS.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisotopdfws.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Hashtable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.axis.components.net.JSSESocketFactory;
import org.apache.axis.components.net.SecureSocketFactory;

import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/** 
 * <p>Custom SSL socket factory to use our integrated keystore. Based loosely on org.apache.axis.components.net.SunJSSESocketFactory.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 8/8/2016.
 */
public class CustomSSLSocketFactory extends JSSESocketFactory implements SecureSocketFactory {

	/**
	 * Constructor CustomSSLSocketFactory.
	 * 
	 * @param attributes 
	 * @throws Exception 
	 */
	public CustomSSLSocketFactory(Hashtable<?, ?> attributes) throws Exception {
		super(attributes);
	}

	/**
	 * Read the keystore, init the SSL socket factory
	 * 
	 * This overrides the parent class to provide our SocketFactory
	 * implementation.
	 * 
	 * @throws IOException If any error.
	 */
	@Override
	protected final void initFactory() throws IOException {

		try {
			SSLContext context = getContext();
			sslFactory = context.getSocketFactory();
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Gets a custom SSL Context. This is the main working of this class. The
	 * following are the steps that make up our custom configuration:
	 * 
	 * 1. Open our keystore file using the password provided 2. Create a
	 * KeyManagerFactory and TrustManagerFactory using this file 3. Initialise a
	 * SSLContext using these factories
	 * 
	 * @return SSLContext
	 * @throws WebServiceClientConfigException
	 * @throws Exception Error creating context for SSLSocket
	 */
	protected final SSLContext getContext() throws Exception {
		
		String path = StaticSignatureReportProperties.getProperty("signaturereport.keystores.SSLTrusted.path");
		
		String password = StaticSignatureReportProperties.getProperty("signaturereport.keystores.SSLTrusted.password");
		
		String type = StaticSignatureReportProperties.getProperty("signaturereport.keystores.SSLTrusted.type");

		char[] keystorepass = password.toCharArray();

		String pass = new String(keystorepass);
		if (pass == null || pass.isEmpty())
			throw new Exception("Could not read password for configured keystore!");

		InputStream keystoreFile = new FileInputStream(path);
		if (keystoreFile == null)
			throw new Exception("Could not read the configured keystore file at "
					+ path);

		try {
			// create required keystores and their corresponding manager objects
			KeyStore keyStore = KeyStore.getInstance(type);

			keyStore.load(keystoreFile, keystorepass);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
			kmf.init(keyStore, keystorepass);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
					.getDefaultAlgorithm());
			tmf.init(keyStore);

			// congifure a local SSLContext to use created keystores
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

			return sslContext;
		} catch (Exception e) {
			throw new Exception("Error creating context for SSLSocket!", e);
		}
	}


}
