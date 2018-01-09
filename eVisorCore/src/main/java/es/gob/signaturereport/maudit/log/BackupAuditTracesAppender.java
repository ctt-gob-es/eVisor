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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.BackupAuditTracesAppender.java.</p>
 * <b>Description:</b><p> Class that represents the backup audit appender.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/08/2011.
 */
package es.gob.signaturereport.maudit.log;

import java.io.IOException;

import org.apache.log4j.FileAppender;


/** 
 * <p>Class that represents the backup audit appender.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/08/2011.
 */
public class BackupAuditTracesAppender  extends FileAppender{
	/**
	 * Attribute that represents the name of logger. 
	 */
	public static final String LOGGER_NAME = "BackupAuditTraces";
	
	/**
	 * Attribute that represents the appender name. 
	 */
	public static final String APPENDER_NAME ="BackupAuditTracesAppender";
	
	/**
	 * Close the file.
	 */
	public void closeBackupAuditFile(){
		this.closeFile();
	}

	
	/**
	 * Open the file.
	 * @throws IOException	If an errors occurs.
	 */
	public void openBackupAuditFile() throws IOException {
		this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
	}
}
