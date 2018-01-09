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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.SignatureReportProperties.java.</p>
 * <b>Description:</b><p>Class that manages the properties of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/** 
 * <p>Class that manages the properties of system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public final class OOProperties {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(OOProperties.class);
	/**
	 * Attribute that represents the location of system property file. 
	 */
	private static final String PROPERTIES_FILE = "/openoffice.properties";

	/**
	 * Attribute that represents a instance of SignatureReportProperties. 
	 */
	private static OOProperties instance = null;

	/**
	 * Attribute that represents the Map that contains all properties of System. 
	 * <p>The Keys are the section identifier.</p>
	 * <p>The Values are LinkedHashMap object that contains the properties of section.</p>
	 */
	private Properties properties;

	/**
	 * Constructor method for the class SignatureReportProperties.java. 
	 */
	private OOProperties() {
		load();
	}

	/**
	 * Gets a instance of SignatureReportProperties.
	 * @return	Instance of SignatureReportProperties.
	 */
	public static OOProperties getInstance() {
		if (instance == null) {
			instance = new OOProperties();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.properties.SignatureReportPropertiesI#getPropertyValue(java.lang.String, java.lang.String)
	 */
	public String getPropertyValue(String propertyId) {

		return properties.getProperty(propertyId);
	}

	/**
	 * Load of properties.
	 */
	private void load() {

		InputStream is = OOProperties.class.getResourceAsStream(PROPERTIES_FILE);
		properties = new Properties();
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
				String msg = LanguageWS.getMessage(LanguageWSKeys.LP_001);
				LOGGER.error(msg, e);
			}

		}
	}
}