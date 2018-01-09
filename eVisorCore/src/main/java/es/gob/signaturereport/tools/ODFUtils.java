// Copyright (C) 2012-13 MINHAP, Gobierno de Espa�a
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
 * <b>File:</b><p>es.gob.signaturereport.tools.ODFUtils.java.</p>
 * <b>Description:</b><p> Utility class for managing ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/04/2011.
 */
package es.gob.signaturereport.tools;

//import com.sun.star.beans.PropertyValue;
//import com.sun.star.frame.XComponentLoader;
//import com.sun.star.frame.XStorable;
//import com.sun.star.lang.XComponent;
//import com.sun.star.lib.uno.adapter.XOutputStreamToByteArrayAdapter;
//import com.sun.star.uno.UnoRuntime;


/**
 * <p>Utility class for managing ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/04/2011.
 */
public final class ODFUtils {

	/**
	 * Attribute that represents the text filter. 
	 */
	private static final String TEXT_FILTER = "writer_pdf_Export";

	/**
	 * Attribute that represents the graphics filter. 
	 */
	private static final String GRAPHICS_FILTER = "draw_pdf_Export";
	/**
	* Attribute that represents the presentation filter. 
	*/
	private static final String PRESENTATION_FILTER = "impress_pdf_Export";
	/**
	* Attribute that represents the spreadshee filter. 
	*/
	private static final String SPREADSHEET_FILTER = "calc_pdf_Export";

	/**
	 * Attribute that represents a class instance. 
	 */
	private static ODFUtils instance = null;

	/**
	 * Constructor method for the class ODFUtils.java. 
	 */
	private ODFUtils() {
	
	}

	/**
	 * Gets an instance of the class.
	 * @return	An instance of the class.
	 */
	public static ODFUtils getInstance() {
		if (instance == null) {
			instance = new ODFUtils();
		}
		return instance;
	}

	/**
	 * Check if the supplied MimeType matches with any format that is exportable to PDF. 
	 * @param mimeType	MimeType.
	 * @return	True if the format is convertable to PDF.
	 */
	public boolean isExportableToPDF(String mimeType) {
		return getFilterName(mimeType) != null;
	}

	/**
	 * Gets the filter name for the converting of the document to PDF.
	 * @param mimeType	MimeType.
	 * @return	Filter name.
	 */
	private String getFilterName(String mimeType) {
		String filterName = null;
		if (mimeType != null) {
			if (mimeType.equals(FileUtils.MSOFFICE_DOC_MEDIA_TYPE) || mimeType.equals(FileUtils.MSOFFICE_DOCX_MEDIA_TYPE) || mimeType.equals(FileUtils.ODT_MEDIA_TYPE)|| mimeType.equals(FileUtils.RTF_MEDIA_TYPE)) {
				filterName = TEXT_FILTER;
			} else if (mimeType.equals(FileUtils.ODG_MEDIA_TYPE) || mimeType.equals(FileUtils.ODC_MEDIA_TYPE)) {
				filterName = GRAPHICS_FILTER;
			} else if (mimeType.equals(FileUtils.ODS_MEDIA_TYPE) || mimeType.equals(FileUtils.MSOFFICE_XLS_MEDIA_TYPE) || mimeType.equals(FileUtils.MSOFFICE_XLSX_MEDIA_TYPE)) {
				filterName = SPREADSHEET_FILTER;
			} else if (mimeType.equals(FileUtils.ODP_MEDIA_TYPE) || mimeType.equals(FileUtils.MSOFFICE_PPT_MEDIA_TYPE) || mimeType.equals(FileUtils.MSOFFICE_PPTX_MEDIA_TYPE)) {
				filterName = PRESENTATION_FILTER;
			}
		}
		return filterName;

	}
	
}
