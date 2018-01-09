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
 * <b>File:</b><p>es.gob.signaturereport.maudit.log.EventFilenameFilter.java.</p>
 * <b>Description:</b><p> Class that represents a filename filter for the event file.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>02/08/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 02/08/2011.
 */
package es.gob.signaturereport.maudit.log;

import java.io.File;
import java.io.FilenameFilter;


/** 
 * <p>Class that represents a filename filter for the event file.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 02/08/2011.
 */
public class EventFilenameFilter implements FilenameFilter {
	
	/**
	 * Attribute that represents the end file name. 
	 */
	private String endFilename = null; 
	/**
	 * {@inheritDoc}
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		
		return name.endsWith(endFilename);
	}
	/**
	 * Constructor method for the class EventFilenameFilter.java.
	 * @param endName End file name.
	 */
	public EventFilenameFilter(String endName) {
		super();
		this.endFilename = endName;
	}

}
