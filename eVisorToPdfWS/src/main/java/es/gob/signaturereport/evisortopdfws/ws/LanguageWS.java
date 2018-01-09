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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.LanguageWS.java.</p>
 * <b>Description:</b><p> Class responsible for managing the access to language
 * resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/** 
 * <p>Class responsible for managing the access to language
 * resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public final class LanguageWS {

	/**
	 * Constructor method for the class Language.java. 
	 */
	private LanguageWS() {
	}

	/**
	 * Attribute that represents the list of messages.
	 */
	private static ResourceBundle msgReports;

	/**
	 * Attribute that represents the locale specified in the configuration.
	 */
	private static Locale currentLocale;

	/**
	 * Attribute that represents the name of property file for language configuration. 
	 */
	private static final String FILE_PROP_NAME = "message-language";

	/**
	 * Attribute that represents the property that indicates the language used by the system. 
	 */
	private static final String LANGUAGE_ATT = "LANGUAGE";

	/**
	 * Attribute that represents the location of file that contains the messages of system. 
	 */
	private static final String CONTENT_LANGUAGE_PATH = "message-content.MessagesReports";

	static {
		currentLocale = new Locale(ResourceBundle.getBundle(FILE_PROP_NAME).getString(LANGUAGE_ATT));

		msgReports = ResourceBundle.getBundle(CONTENT_LANGUAGE_PATH, currentLocale);
	}

	/**
	 * Gets the message with the key and values indicated as input parameters.
	 * @param key Key for obtain the message.
	 * @param values Values for insert in the message.
	 * @return String with the message well-formed.
	 */
	public static String getFormatMessage(String key, Object[ ] values) {
		return new MessageFormat(msgReports.getString(key), currentLocale).format(values);
	}

	/**
	 * Gets the message with the key indicated as input parameters.
	 * @param key Key for obtain the message.
	 * @return String with the message.
	 */
	public static String getMessage(String key) {
		return msgReports.getString(key);
	}
}
