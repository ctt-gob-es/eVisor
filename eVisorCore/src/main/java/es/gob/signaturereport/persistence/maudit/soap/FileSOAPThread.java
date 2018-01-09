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
 * <b>File:</b><p>es.gob.signaturereport.maudit.persistence.soap.FileSOAPThread.java.</p>
 * <b>Description:</b><p> Thread for write a soap message in the disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/10/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/10/2011.
 */
package es.gob.signaturereport.persistence.maudit.soap;

import java.io.FileOutputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.tools.UtilsResources;

/**
 * <p>Thread for write a soap message in the disk.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/10/2011.
 */
public class FileSOAPThread implements Runnable {
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(FileSOAPThread.class);

	/**
	 * Attribute that represents the SOAP message.
	 */
	private byte[] soap = null;

	/**
	 * Attribute that represents the filename.
	 */
	private String fileName = null;

	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if(this.soap!=null && this.fileName!=null){
			FileOutputStream file = null;
			try {
				file = new FileOutputStream(fileName);
				file.write(soap);
			} catch (Exception e) {
				LOGGER.error(Language.getFormatMessage(LanguageKeys.AUD_072, new Object[]{fileName}),e);
			} finally{
				UtilsResources.safeCloseOutputStream(file);
			}
		}
	}

	/**
	 * Constructor method for the class FileSOAPThread.java.
	 * @param message		SOAP message.
	 * @param path 	String filename.
	 */
	public FileSOAPThread(byte[ ] message, String path) {
		super();
		if(message!=null){
			this.soap = Arrays.copyOf(message, message.length);
		}
		this.fileName = path;
	}

}
