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
 * <b>File:</b><p>es.gob.signaturereport.malarm.persistence.file.AlarmAppender.java.</p>
 * <b>Description:</b><p>Class that extends the {@link FileAppender} for the logging of the system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>05/07/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 05/07/2011.
 */
package es.gob.signaturereport.malarm.file;

import java.io.IOException;

import org.apache.log4j.FileAppender;


/** 
 * <p>Class that extends the {@link FileAppender} for the logging of the system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 05/07/2011.
 */
public class AlarmAppender extends FileAppender {
	
	/**
	 * Attribute that represents the name of logger. 
	 */
	public static final String LOGGER_NAME = "BackupAlarm";
	
	/**
	 * Attribute that represents the appender name. 
	 */
	public static final String APPENDER_NAME ="BackupAlarmAppender";
	
	/**
	 * Close the file.
	 */
	public void closeAlarmFile(){
		this.closeFile();
	}

	
	/**
	 * Open the file.
	 * @throws IOException	If an errors occurs.
	 */
	public void openAlarmFile() throws IOException {
		this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
	}
}
