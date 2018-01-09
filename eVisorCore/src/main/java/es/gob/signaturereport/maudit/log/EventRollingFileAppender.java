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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.EventRollingFileAppender.java.</p>
 * <b>Description:</b><p>Class that splits the daily log file in other log files to simplify their custody. This class
 * is an extension of the {@link FileAppender} to make the operations with a certain periodicity.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>05/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 05/07/2011.
 */
package es.gob.signaturereport.maudit.log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import es.gob.signaturereport.maudit.EventCollector;
import es.gob.signaturereport.tools.UniqueNumberGenerator;


/** 
 * <p>Class that splits the daily log file in other log files to simplify their custody. This class
 * is an extension of the {@link FileAppender} to make the operations with a certain periodicity.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 05/07/2011.
 */
public class EventRollingFileAppender extends FileAppender {

	/**
	 * Constant attribute that represents the number to identify the . 
	 */
	static final int TOP_OF_TROUBLE = -1;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: every minute. 
	 */
	static final int TOP_OF_MINUTE = 0;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: on top of every hour. 
	 */
	static final int TOP_OF_HOUR = 1;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: at midday and midnight.
	 */
	static final int HALF_DAY = 2;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: at midnight.
	 */
	static final int TOP_OF_DAY = 3;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: at start of week.
	 */
	static final int TOP_OF_WEEK = 4;

	/**
	 * Constant attribute that represents the number to identify the periodicity to roll-over the appender: at start of every month.
	 */
	static final int TOP_OF_MONTH = 5;

	/**
	 * Attribute that represents the sequence ID. 
	 */
	private String idSecuencia = null;

	/**
	 * Attribute that represents the date pattern. 
	 */
	private String datePattern = "'.'yyyy-MM-dd";

	/**
	 * Attribute that represents the file name of the scheduled log. 
	 */
	protected String scheduledFilename;

	/**
	 * Attribute that represents the next check for to roll-over the appender. 
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	/**
	 * Attribute that represents the current date. 
	 */
	Date now = new Date();

	/**
	 * Attribute that represents the object to format dates. 
	 */
	SimpleDateFormat sdf;

	/**
	 * Attribute that gives a periodicity type and the current time, 
	 * it computes the start of the next interval. 
	 */
	RollingCalendar rc = new RollingCalendar();

	/**
	 * Attribute that represents the period when to check for to roll-over the appender. 
	 */
	int checkPeriod = TOP_OF_TROUBLE;

	// CHECKSTYLE:OFF -- Checkstyle rule isn't applied to the constant's name
	// because it's a public constant and could be called by external
	// applications.
	/**
	 * Attribute that represents the time zone for the appender. 
	 */
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	// CHECKSTYLE:ON

	/**
	 * Constructor method for the class EventRollingFileAppender.java. 
	 */
	public EventRollingFileAppender() {
	}

	/**
	 * Constructor method for the class EventRollingFileAppender.java.
	 * @param layout Parameter that represents the layout.
	 * @param filename Parameter that represents the name of the log file. 
	 * @param datePatternParam Parameter that represents the date pattern. 
	 * @throws IOException If the method fails.
	 */
	public EventRollingFileAppender(Layout layout, String filename, String datePatternParam) throws IOException {
		super(layout, filename, true);
		this.datePattern = datePatternParam;
		activateOptions();
	}

	/**
	 * Sets the value of the attribute {@link #datePattern}.
	 * @param pattern The value for the attribute {@link #datePattern}.
	 */
	public void setDatePattern(String pattern) {
		datePattern = pattern;
	}

	/**
	 * Gets the value of the attribute {@link #datePattern}.
	 * @return the value of the attribute {@link #datePattern}.
	 */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * {@inheritDoc}
	 * @see org.apache.log4j.FileAppender#activateOptions()
	 */
	public void activateOptions() {

		// Por si se trata de un proceso de actualizaci�n de la plataforma,
		// buscamos el
		// nombre del fichero de diario anterior para proceder a darle la actual
		// nomenclatura.
		//buscarYactualizarFicheroDiarioVersionAnterior(fileName);

		// Obtenemos el nombre del fichero (si es que existe) que cumpla el
		// patr�n
		// idSecuencia.nombreFichero
		// Si existe, se obtiene su identificador de secuencia y se le asigna al
		// Productor.
		// Si no existe se crea.
		fileName = obtenerNombreFichero(fileName);
		super.activateOptions();
		if (datePattern != null && fileName != null) {

			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(datePattern);
			int type = computeCheckPeriod();
			rc.setType(type);
			File file = new File(fileName);
			scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));

		} 
	}

//	/**
//	 * Method that only runs when a log file has been found with the name valid in reference to the nomenclature used previously.
//	 * This file is renamed and updated with the new nomenclature.
//	 * This process is useful in the update of the platform version so the last log written by the platform will not be out of date. 
//	 * @param rutaAbsoluta Parameter that represents the absolute path of the log file.
//	 */
//	private void buscarYactualizarFicheroDiarioVersionAnterior(String rutaAbsoluta) {
//
//		// Comprobamos si el fichero existe.
//		File ficheroRutaAbsoluta = new File(rutaAbsoluta);
//
//		// Si el fichero existe, lo renombramos al mismo pero añadi�ndole el
//		// identificador de secuencia por delante.
//		// Si no existe no hacemos nada.
//		if (ficheroRutaAbsoluta.exists()) {
//
//			// Obtenemos el nombre y la ruta al directorio al que pertenece.
//			String nombreFichero = ficheroRutaAbsoluta.getName();
//			String rutaDirectorioFichero = ficheroRutaAbsoluta.getParent();
//			// Generamos un nuevo identificador de secuencia.
//			String secuencia = String.valueOf(UniqueNumberGenerator.getInstance().getNumber());
//			// Generamos un nuevo File con la ruta hacia el nuevo fichero.
//			String rutaficheroDiarioActualizado = rutaDirectorioFichero + File.separator + secuencia + "." + nombreFichero;
//			File ficheroDiarioActualizado = new File(rutaficheroDiarioActualizado);
//			// Renombramos el fichero (la fecha de ultima modificaci�n se
//			// mantiene).
//			ficheroRutaAbsoluta.renameTo(ficheroDiarioActualizado);
//
//		}
//
//	}

	/**
	 * Method that obtains the name of the log file. For the name of the log file process actually the pattern used is 'sequenceID.fileName'.
	 * For the name of the log file processed in the past the pattern used is 'sequenceID.fileName.date'.
	 * @param rutaAbsoluta Parameter that represents the absolute path to the log file indicated in ther file <i>log4j.xml</i>.
	 * @return the name of the log file.
	 */
	private String obtenerNombreFichero(String rutaAbsoluta) {

		String result = rutaAbsoluta;

		// Primero hay que obtener el nombre del fichero (sin ruta).
		File ficheroRutaAbsoluta = new File(rutaAbsoluta);
		String ficheroNombre = ficheroRutaAbsoluta.getName();

		// Buscamos si existe ya un fichero en el que estamos escribiendo.
		File ficheroActual = buscarSiExistePatronFicheroEnRuta(ficheroNombre, ficheroRutaAbsoluta.getParentFile());

		if (ficheroActual != null) {

			// Si ya existe uno que corresponde, obtenemos el identificador.
			idSecuencia = obtenerIdentificadorDeCustodia(ficheroActual);
			// Y actualizamos el nombre de fichero a tratar.
			result = ficheroActual.getAbsolutePath();

		} else {
			
			idSecuencia = String.valueOf(UniqueNumberGenerator.getInstance().getNumber());
			result = ficheroRutaAbsoluta.getParent() + File.separator + idSecuencia + "." + ficheroNombre;

		}

		// Le indicamos al productor el identificador de secuencia.
		/*
JA
		try {
			Productor.getProductor().flush(idSecuencia);
		} catch (IOException e) {
			// Si se produjera alguna excepci�n al tratar de obtener el
			// productor,
			// se deberña asignar provisionalmente el identificador de secuencia
			// de forma est�tica, para que m�s adelante se realice de forma
			// independiente
			// el flush();
		}
*/
		EventCollector.getInstance().flush(idSecuencia);
		return result;

	}

	/**
	 * Method that searchs in a certain directory some file with the pattern 'sequenceID.fileName'.
	 * @param ficheroNombre Parameter that represents the name of the log file indicated in ther file <i>log4j.xml</i>.
	 * @param parentFile Parameter that represents the directory where to search the file.
	 * @return a file found with the searched pattern.
	 */
	private File buscarSiExistePatronFicheroEnRuta(String ficheroNombre, File parentFile) {

		File result = null;

		// Creamos un filtro para la b�squeda en el directorio.
		FiltroBusquedaDiario filtro = new FiltroBusquedaDiario(ficheroNombre);
		// Obtenemos la lista de ficheros que cumplen el filtro.
		File[ ] listaFicheros = parentFile.listFiles(filtro);

		// Si la lista es nula o vacña, no hacemos nada.
		if (listaFicheros != null && listaFicheros.length > 0) {

			// Se supone que solo encontrar� un fichero.
			result = listaFicheros[0];

		}

		return result;

	}

	/** 
	 * <p>Internal class that implements the interface {@link FilenameFilter}. This class represents a filter used in the search of files.
	 * The filter considers valid those files ending in '.dailyName' where 'dailyName' is a input param.</p>
	 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
	 * certificates and electronic signature.</p>
	 * @version 1.0, 31/01/2011.
	 */
	class FiltroBusquedaDiario implements FilenameFilter {

		/**
		 * Attribute that represents the name of the log file. 
		 */
		private String nombreDiario = null;

		/**
		 * Constructor method for the class EventRollingFileAppender.java.
		 * @param nombreDiarioParam Parameter that represents the name of the log file. 
		 */
		public FiltroBusquedaDiario(String nombreDiarioParam) {
			this.nombreDiario = nombreDiarioParam;
		}

		/**
		 * {@inheritDoc}
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			// TODO Mejorar el filtro para que los ficheros sean
			// idSecuencia.nombreDiario (se harña con String.matches).
			return name.endsWith("." + nombreDiario);

		}

	}

	/**
	 * Method that returns the name of the log file used to identify that in custody database.
	 * @param ficheroActual Parameter that represents the actual log file.
	 * @return the name of the log file used to identify that in custody database.
	 */
	private String obtenerIdentificadorDeCustodia(File ficheroActual) {

		String nombreFichero = ficheroActual.getName();
		String[ ] partes = nombreFichero.split("\\.");
		return partes[0];

	}

//	/**
//	 * Method that records in the log the periodicity used to roll the appender.
//	 * @param type Parameter that represents the periodicity type.
//	 */
//	void printPeriodicity(int type) {
//		switch (type) {
//			case TOP_OF_MINUTE:
//				LogLog.debug(Language.getFormatLog("log943", new Object[ ] { name }));
//				break;
//			case TOP_OF_HOUR:
//				LogLog.debug(Language.getFormatLog("log944", new Object[ ] { name }));
//				break;
//			case HALF_DAY:
//				LogLog.debug(Language.getFormatLog("log945", new Object[ ] { name }));
//				break;
//			case TOP_OF_DAY:
//				LogLog.debug(Language.getFormatLog("log946", new Object[ ] { name }));
//				break;
//			case TOP_OF_WEEK:
//				LogLog.debug(Language.getFormatLog("log947", new Object[ ] { name }));
//				break;
//			case TOP_OF_MONTH:
//				LogLog.debug(Language.getFormatLog("log948", new Object[ ] { name }));
//				break;
//			default:
//				LogLog.warn(Language.getFormatLog("log949", new Object[ ] { name }));
//		}
//	}

	/**
	 * Method that computes the roll over period by looping over the periods, starting with the shortest, and stopping when the r0 is
	 * ifferent from r1, where r0 is the epoch formatted according the datePattern (supplied by the user) and r1 is the 
	 * epoch+nextMillis(i) formatted according to datePattern. All date formatting is done in GMT and not local format because the test
	 * logic is based on comparisons relative to 1970-01-01 00:00:00 GMT (the epoch).
	 * @return the check period.
	 */
	int computeCheckPeriod() {
		RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.ENGLISH);
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null) {
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
				// formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);

				if (r0 != null && r1 != null && !r0.equals(r1)) {
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE;
	}

	/**
	 * Rollover the current file to a new file.
	 * @throws IOException If the method fails.
	 */
	void rollOver() throws IOException {

		/* Compute filename, but only if datePattern is specified */
		if (datePattern == null) {
			//JA-errorHandler.error(Language.resLog.getString("log950"));
			return;
		}

		String tokenFecha = sdf.format(now);
		String datedFilename = fileName + tokenFecha;
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledFilename.equals(datedFilename)) {
			return;
		}

		// close current file, and rename it to datedFilename
		this.closeFile();

		File target = new File(scheduledFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		boolean result = file.renameTo(target);
		if (result) {
			LogLog.debug(fileName + " -> " + scheduledFilename);
		} 

		try {
			// Se actualiza el nombre del fichero de diario.
			fileName = actualizarNombreFicheroDiario(fileName);
			// Actualizamos el identificador de secuencia del Productor.
	//JA-		Productor.getProductor().flush(idSecuencia);
			EventCollector.getInstance().flush(idSecuencia);
			this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
		//JA-	errorHandler.error(Language.getFormatLog("log952", new Object[ ] { fileName }));
		}

		// Actualizamos el nombre del fichero de diario para el scheduled.
		scheduledFilename = fileName + tokenFecha;

	}

	/**
	 * Method that updates the name of a log file with a new identifier.
	 * @param fileName Parameter that represents the name of the file tp update
	 * @return the name of the log file with the new sequence ID.
	 */
	private String actualizarNombreFicheroDiario(String fileName) {

		String result = null;

		File file = new File(fileName);
		String nameFile = file.getName();
		int dotPosition = nameFile.indexOf('.');
		idSecuencia = String.valueOf(UniqueNumberGenerator.getInstance().getNumber());
		nameFile = idSecuencia + nameFile.substring(dotPosition);
		result = file.getParent() + File.separator + nameFile;

		return result;

	}

	/**
	 * {@inheritDoc}
	 * @see org.apache.log4j.WriterAppender#subAppend(org.apache.log4j.spi.LoggingEvent)
	 */
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
			try {
				rollOver();
			} catch (IOException ioe) {
				//JA-LogLog.error(Language.resLog.getString("log953"), ioe);
			}
		}
		super.subAppend(event);
	}

	/**
	 * Gets the value of the attribute {@link #periodoEjecucion}.
	 * @return the value of the attribute {@link #periodoEjecucion}.
	 */
	public String getIdSecuencia() {
		return idSecuencia;
	}

	/**
	 * Sets the value of the attribute {@link #periodoEjecucion}.
	 * @param idSecuenciaParam The value for the attribute {@link #periodoEjecucion}.
	 */
	public void setIdSecuencia(String idSecuenciaParam) {
		this.idSecuencia = idSecuenciaParam;
	}
}

/**
 *  RollingCalendar is a helper class to DailyRollingFileAppender.
 *  Given a periodicity type and the current time, it computes the
 *  start of the next interval.
 * */
class RollingCalendar extends GregorianCalendar {

	/**
	 * Constant attribute that represents the number 12.
	 */
	private static final int NUM12 = 12;

	/**
	 * Class serial version.
	 */
	private static final long serialVersionUID = 5887525651974696143L;

	/**
	 * Attribute that represents the type of periodicity to use. 
	 */
	int type = EventRollingFileAppender.TOP_OF_TROUBLE;

	/**
	 * Constructor method for the class EventRollingFileAppender.java. 
	 */
	RollingCalendar() {
		super();
	}

	/**
	 * Constructor method for the class EventRollingFileAppender.java.
	 * @param tz Parameter that represents the time zone.
	 * @param locale  Parameter that represents the locale.
	 */
	RollingCalendar(TimeZone tz, Locale locale) {
		super(tz, locale);
	}

	/**
	 * Sets the value of the attribute {@link #periodoEjecucion}.
	 * @param typeParam The value for the attribute {@link #periodoEjecucion}.
	 */
	void setType(int typeParam) {
		this.type = typeParam;
	}

	/**
	 * Method that obtains the next date check in milliseconds.
	 * @param now Parameter that represents the current date.
	 * @return the next date check in milliseconds.
	 */
	public long getNextCheckMillis(Date now) {
		return getNextCheckDate(now).getTime();
	}

	/**
	 * Method that obtains the next date check.
	 * @param now Parameter that represents the current date.
	 * @return the next date check.
	 */
	public Date getNextCheckDate(Date now) {
		this.setTime(now);

		switch (type) {
			case EventRollingFileAppender.TOP_OF_MINUTE:
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				this.add(Calendar.MINUTE, 1);
				break;
			case EventRollingFileAppender.TOP_OF_HOUR:
				this.set(Calendar.MINUTE, 0);
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				this.add(Calendar.HOUR_OF_DAY, 1);
				break;
			case EventRollingFileAppender.HALF_DAY:
				this.set(Calendar.MINUTE, 0);
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				int hour = get(Calendar.HOUR_OF_DAY);
				if (hour < NUM12) {
					this.set(Calendar.HOUR_OF_DAY, NUM12);
				} else {
					this.set(Calendar.HOUR_OF_DAY, 0);
					this.add(Calendar.DAY_OF_MONTH, 1);
				}
				break;
			case EventRollingFileAppender.TOP_OF_DAY:
				this.set(Calendar.HOUR_OF_DAY, 0);
				this.set(Calendar.MINUTE, 0);
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				this.add(Calendar.DATE, 1);
				break;
			case EventRollingFileAppender.TOP_OF_WEEK:
				this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
				this.set(Calendar.HOUR_OF_DAY, 0);
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				this.add(Calendar.WEEK_OF_YEAR, 1);
				break;
			case EventRollingFileAppender.TOP_OF_MONTH:
				this.set(Calendar.DATE, 1);
				this.set(Calendar.HOUR_OF_DAY, 0);
				this.set(Calendar.SECOND, 0);
				this.set(Calendar.MILLISECOND, 0);
				this.add(Calendar.MONTH, 1);
				break;
			default:
				break;		
//	JA-			throw new IllegalStateException(Language.resLog.getString("log954"));
		}

		return getTime();
	}
}
