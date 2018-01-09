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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.EventAppender.java.</p>
 * <b>Description:</b><p>Class that represents the appender for event log.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>05/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 05/07/2011.
 */
package es.gob.signaturereport.maudit.log;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.EventCollector;
import es.gob.signaturereport.persistence.exception.AuditPersistenceException;
import es.gob.signaturereport.maudit.access.AuditPersistenceFacade;
import es.gob.signaturereport.tools.FileUtils;
import es.gob.signaturereport.tools.ToolsException;

/**
 * <p>Class that represents the appender for event log.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 05/07/2011.
 */
public class EventAppender extends EventRollingFileAppender {

	/**
	 * Attribute that represents the object to format dates.
	 */
	private SimpleDateFormat sdf = null;

	/**
	 * <p>Internal class that manages the custody of the log daily.</p>
	 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
	 * certificates and electronic signature.</p>
	 * @version 1.0, 28/01/2011.
	 */
	private class CustodiaDiario implements Runnable {

		/**
		 * Attribute that represents the object that manages the log of the class.
		 */
		private final Logger logger = Logger.getLogger(CustodiaDiario.class);

		/**
		 * Attribute that represents the full path of the log file.
		 */
		private String fullPathLogFile = null;

		
		/**
		 * Attribute that represents the sequence ID.
		 */
		private String idSecuencia = null;

		// /**
		// * Attribute that represents the object that manages the log of the
		// internal class.
		// */
		// private Logger loggerCustodiado = Logger.getLogger("CustodiaDiario");

		/**
		 * Constructor method for the class EventAppender.java.
		 * @param fullPathLogFileParam Parameter that represents the full path of the log file.
		 * @param idSecuenciaParam Parameter that represents the sequence ID.
		 */
		public CustodiaDiario(String fullPathLogFileParam, String idSecuenciaParam) {
			this.fullPathLogFile = fullPathLogFileParam;
			this.idSecuencia = idSecuenciaParam;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				if (EventCollector.getInstance().getMode() != EventCollector.UNCOUPLED_MODE && EventCollector.getInstance().isCustodyEventFile()) {
					byte[ ] eventFile = FileUtils.getFile(this.fullPathLogFile);
					if (eventFile != null) {
						logger.info(Language.getFormatMessage(LanguageKeys.AUD_025, new Object[ ] { this.fullPathLogFile }));
						String custodyType = EventCollector.getInstance().getEventCustodyType();
						byte[ ] hash = null;
						if (AuditManagerI.HASH_CUSTODY_TYPE.equals(custodyType)) {
							try {
								MessageDigest md = MessageDigest.getInstance("SHA1");
								hash = md.digest(eventFile);
							} catch (NoSuchAlgorithmException e) {
								logger.error(Language.getFormatMessage(LanguageKeys.AUD_027, new Object[ ] { this.fullPathLogFile }));
								hash = eventFile;
							}
						}
						if (hash != null) {
							AuditPersistenceFacade.getInstance().updateEventContent(new Long(this.idSecuencia), hash, custodyType);
						} else {
							AuditPersistenceFacade.getInstance().updateEventContent(new Long(this.idSecuencia), eventFile, custodyType);
						}
						logger.info(Language.getFormatMessage(LanguageKeys.AUD_026, new Object[ ] { this.fullPathLogFile }));

					}
				}
			} catch (ToolsException e) {
				logger.error(Language.getFormatMessage(LanguageKeys.AUD_024, new Object[ ] { this.fullPathLogFile }) + e.getMessage());
			} catch (NumberFormatException e) {
				logger.error(Language.getFormatMessage(LanguageKeys.AUD_019, new Object[ ] { this.idSecuencia }));
			} catch (AuditPersistenceException e) {
				logger.error(Language.getFormatMessage(LanguageKeys.AUD_022, new Object[ ] { this.fullPathLogFile }) + e.getDescription());
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.log.EventRollingFileAppender#rollOver()
	 */
	protected void rollOver() throws IOException {
		// logger.info("Iniciando el tratamiento del fichero de eventos");
		// logger.info("Memoria total: " + Runtime.getRuntime().totalMemory());
		// logger.info("Memoria libre: " + Runtime.getRuntime().freeMemory());
		// logger.info("Memoria m�xima: " + Runtime.getRuntime().maxMemory());

		// Se comprueba si ha llegado el momento de realizar un cambio
		// de fichero.
		String datedFilename = fileName + sdf.format(now);
		if (scheduledFilename.equals(datedFilename)) {
			// Todavia no se va a efectuar el cambio de fichero.
			super.rollOver();

			return;
		}
		// JA LOGGER.info(Language.resLog.getString("log926b"));

		// Antes de que se ejecute la l�gica del padre y cambie el nombre al
		// fichero
		// de log actual, se recupera la fecha en que fue modificado por �ltima
		// vez
		// para poder reconstruir posteriormente el nombre del fichero y leer su
		// contenido.
		String nombreFichero = this.getFile();
		File f = new File(nombreFichero);
		Date fecha = new Date(f.lastModified());
		String fullPathLogFile = nombreFichero + sdf.format(fecha);
		// Tambien se recupera el identificador de secuencia que se utilizar�
		// para firmar el fichero.
		String idSecuencia = getIdSecuencia();
		// Se limpia el Productor.
		// JA Productor.getProductor().flush(idSecuencia);

		// Se ejecuta la logica del padre
		super.rollOver();

		// Se lanza el hilo que se encarga de Firmar y Custodia el fichero
		// de diario.
		// JA LOGGER.info(Language.resLog.getString("log926c"));
		Thread t = new Thread(new CustodiaDiario(fullPathLogFile, idSecuencia));
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	/**
	 * Constructor method for the class EventAppender.java.
	 */
	public EventAppender() {
		super();

		String pattern = this.getDatePattern();
		sdf = new SimpleDateFormat(pattern);

	}

}
