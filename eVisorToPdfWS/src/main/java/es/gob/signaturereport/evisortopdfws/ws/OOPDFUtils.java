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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.ODFUtils.java.</p>
 * <b>Description:</b><p> Utility class for managing ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lib.uno.adapter.XOutputStreamToByteArrayAdapter;
import com.sun.star.uno.UnoRuntime;

import es.gob.signaturereport.evisortopdfws.util.StaticEVisorToPDFWSProperties;

/**
 * <p>Utility class for managing ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public final class OOPDFUtils {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(OOPDFUtils.class);
	
	/**
	 * Attribute that represents the ODT media type. 
	 */
	public static final String ODT_MEDIA_TYPE = "application/vnd.oasis.opendocument.text";

	/**
	 * Attribute that represents the ODG media type. 
	 */
	public static final String ODG_MEDIA_TYPE = "application/vnd.oasis.opendocument.graphics";

	/**
	 * Attribute that represents the ODS media type. 
	 */
	public static final String ODS_MEDIA_TYPE = "application/vnd.oasis.opendocument.spreadsheet";

	/**
	 * Attribute that represents the ODP media type. 
	 */
	public static final String ODP_MEDIA_TYPE = "application/vnd.oasis.opendocument.presentation";

	/**
	 * Attribute that represents the ODC media type. 
	 */
	public static final String ODC_MEDIA_TYPE = "application/vnd.oasis.opendocument.chart";

	/**
	 * Attribute that represents the BMP media type. 
	 */
	public static final String BMP_MEDIA_TYPE = "image/x-ms-bmp";

	/**
	 * Attribute that represents the GIF media type. 
	 */
	public static final String GIF_MEDIA_TYPE = "image/gif";

	/**
	 * Attribute that represents the JPEG media type. 
	 */
	public static final String JPEG_MEDIA_TYPE = "image/jpeg";

	/**
	 * Attribute that represents the PNG media type. 
	 */
	public static final String PNG_MEDIA_TYPE = "image/png";

	/**
	 * Attribute that represents the PNG media type. 
	 */
	public static final String TIFF_MEDIA_TYPE = "image/tiff";

	/**
	 * Attribute that represents the PDF media type. 
	 */
	public static final String PDF_MEDIA_TYPE = "application/pdf";

	/**
	 * Attribute that represents the microsoft word media type. 
	 */
	public static final String MSOFFICE_DOC_MEDIA_TYPE = "application/msword";

	/**
	 * Attribute that represents the microsoft word media type (in open xml format). 
	 */
	public static final String MSOFFICE_DOCX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	/**
	 * Attribute that represents the microsoft powerpoint media type. 
	 */
	public static final String MSOFFICE_PPT_MEDIA_TYPE = "application/vnd.ms-powerpoint";

	/**
	 * Attribute that represents the microsoft powerpoint media type (in open xml format). 
	 */
	public static final String MSOFFICE_PPTX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

	/**
	 * Attribute that represents the microsoft excel media type. 
	 */
	public static final String MSOFFICE_XLS_MEDIA_TYPE = "application/vnd.ms-excel";

	/**
	 * Attribute that represents the microsoft excel media type (in open xml format). 
	 */
	public static final String MSOFFICE_XLSX_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	/**
	 * Attribute that represents the text plain media type. 
	 */
	public static final String TXT_MEDIA_TYPE = "text/plain";

	/**
	 * Attribute that represents the RTF media type. 
	 */
	public static final String RTF_MEDIA_TYPE = "application/rtf";

	/**
	 * Attribute that represents the XML media type. 
	 */
	public static final String XML_MEDIA_TYPE = "application/xml";

	/**
	 * Attribute that represents the XSLT media type. 
	 */
	public static final String XSLT_MEDIA_TYPE = "application/xslt+xml";

	/**
	 * Attribute that represents the HTML media type. 
	 */
	public static final String HTML_MEDIA_TYPE = "text/html";

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
	 * Attribute that represents the key used to indicates if the OpenOficce server is active. 
	 */
	private static final String ACTIVE_OPENOFFICE_PROP = "openoffice.active";
	/**
	 * Attribute that represents the key used to indicates if the OpenOficce server host. 
	 */
	private static final String OPENOFFICE_HOST_PROP = "openoffice.serverhost";
	/**
	 * Attribute that represents the key used to indicates if the OpenOficce server port. 
	 */
	private static final String OPENOFFICE_PORT_PROP = "openoffice.serverport";

	
	/**
	 * Attribute that represents a flag that indicates if the exporting to PDF is active. 
	 */
	private  boolean exportToPDF = false;

	/**
	 * Attribute that represents the OpenOficce server host. 
	 */
	private String serverHost = null;

	/**
	 * Attribute that represents the OpenOficce server port.  
	 */
	private String serverPort = null;

	/**
	 * Attribute that represents a class instance. 
	 */
	private static OOPDFUtils instance = null;
	
	/**
	 * Attribute that represents the desktop service name. 
	 */
	public static final String SERVICE_DESKTOP = "com.sun.star.frame.Desktop";

	/**
	 * Constructor method for the class ODFUtils.java. 
	 */
	private OOPDFUtils() {
		String activeOO = StaticEVisorToPDFWSProperties.getProperty(ACTIVE_OPENOFFICE_PROP);
		exportToPDF = ((activeOO != null) && (activeOO.trim().toLowerCase().equals("true")));
		String host = StaticEVisorToPDFWSProperties.getProperty(OPENOFFICE_HOST_PROP);
		if (host != null) {
			serverHost = host.trim();
		}
		String port = StaticEVisorToPDFWSProperties.getProperty(OPENOFFICE_PORT_PROP);
		if (port != null) {
			serverPort = port.trim();
		}
	}

	/**
	 * Gets an instance of the class.
	 * @return	An instance of the class.
	 */
	public static OOPDFUtils getInstance() {
		if (instance == null) {
			instance = new OOPDFUtils();
		}
		return instance;
	}

	/**
	 * Exports the supplied ODF to PDF.
	 * @param document	file.
	 * @return	PDF file.
	 * @throws OOPDFException If an error occurs.
	 */
	public byte[ ] exportToPDF(byte[ ] document) throws OOPDFException {

		if (!exportToPDF) {
			String msg = LanguageWS.getMessage(LanguageWSKeys.UTIL_025);
			LOGGER.error(msg);
			throw new OOPDFException(OOPDFException.OPERATION_NOT_ALLOWED, msg);
		}
		byte[ ] pdf = null;

		Tika tika = new Tika();
		String mediaType = tika.detect(document);
		String filterName = getFilterName(mediaType);
		if (filterName == null) {
			String msg = LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_026, new Object[ ] { mediaType });
			LOGGER.error(msg);
			throw new OOPDFException(OOPDFException.INVALID_ODF_FILE, msg);
		} else {
			pdf = convertToPDF(document, filterName);
		}

		return pdf;
	}

	

	/**
	 * Converts the supplied ODF file to PDF.
	 * @param odf	ODF file.	
	 * @param filterName	Filter name.
	 * @return	PDF file.
	 * @throws OOPDFException	If an errors occurs
	 */
	private byte[ ] convertToPDF(byte[ ] odf, String filterName) throws OOPDFException {
		
		byte[ ] pdf = null;
		
		OOfficeConnection connection = new OOfficeConnection(serverHost,Integer.parseInt(serverPort));
		XOutputStreamToByteArrayAdapter xout = null;
		try {
			connection.connect();
			if(connection.isConnected()){
				XComponentLoader xComponentLoader = UnoRuntime.queryInterface(XComponentLoader.class,connection.getService(SERVICE_DESKTOP));
				PropertyValue propertyValues[] = new PropertyValue[2];
				propertyValues[0] = new PropertyValue();
				propertyValues[0].Name = "Hidden";
				propertyValues[0].Value = Boolean.TRUE;
				
				propertyValues[1] = new PropertyValue();
				propertyValues[1].Name = "InputStream";
				propertyValues[1].Value = new OOoInputStream(odf);
				XComponent documento = xComponentLoader.loadComponentFromURL("private:stream", "_blank", 0, propertyValues);
				
				PropertyValue[] propertyExport = new PropertyValue[2];
			  	   
			  	propertyExport[0] = new PropertyValue();
				propertyExport[0].Name = "FilterName";
				propertyExport[0].Value = filterName;
				  
				xout = new XOutputStreamToByteArrayAdapter(); 
				
				propertyExport[1] = new PropertyValue();
			  	propertyExport[1].Name = "OutputStream";
			  	propertyExport[1].Value = xout;
				  
				((XStorable) UnoRuntime.queryInterface(XStorable.class, documento)).storeToURL("private:stream", propertyExport);
				  
				xout.flush();
				pdf = xout.getBuffer();
			}else{
				throw new OOPDFException(OOPDFException.UNKNOWN_ERROR, LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_042, new Object[]{serverHost,serverPort}));
			}
		} catch (OOfficeConnectionException e) {
			throw new OOPDFException(OOPDFException.UNKNOWN_ERROR, LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_042, new Object[]{serverHost,serverPort}),e);
		} catch(Exception e){
			String msg = LanguageWS.getMessage(LanguageWSKeys.UTIL_024);
			LOGGER.error(msg, e);
			throw new OOPDFException(OOPDFException.UNKNOWN_ERROR, msg, e);
		}finally{
			if(connection.isConnected()){
				connection.disconnect();
			}
			
			if(xout!=null){
				try {
					xout.closeOutput();
				} catch (Exception e) {
					LOGGER.error(LanguageWS.getMessage(LanguageWSKeys.UTIL_003), e);
				} 
			}
		}
		return pdf;
	}

	/**
	 * Gets the filter name for the converting of the document to PDF.
	 * @param mimeType	MimeType.
	 * @return	Filter name.
	 */
	private String getFilterName(String mimeType) {
		String filterName = null;
		if (mimeType != null) {
			if (mimeType.equals(MSOFFICE_DOC_MEDIA_TYPE) || mimeType.equals(MSOFFICE_DOCX_MEDIA_TYPE) || mimeType.equals(ODT_MEDIA_TYPE)|| mimeType.equals(RTF_MEDIA_TYPE)) {
				filterName = TEXT_FILTER;
			} else if (mimeType.equals(ODG_MEDIA_TYPE) || mimeType.equals(ODC_MEDIA_TYPE)) {
				filterName = GRAPHICS_FILTER;
			} else if (mimeType.equals(ODS_MEDIA_TYPE) || mimeType.equals(MSOFFICE_XLS_MEDIA_TYPE) || mimeType.equals(MSOFFICE_XLSX_MEDIA_TYPE)) {
				filterName = SPREADSHEET_FILTER;
			} else if (mimeType.equals(ODP_MEDIA_TYPE) || mimeType.equals(MSOFFICE_PPT_MEDIA_TYPE) || mimeType.equals(MSOFFICE_PPTX_MEDIA_TYPE)) {
				filterName = PRESENTATION_FILTER;
			}
		}
		return filterName;

	}
	
	/**
	 * Indicates if the exporting to PDF is allowed.
	 * @return	True if the exporting to PDF is allowed. Otherwise false.
	 */
	public boolean isAllowedPDFExport() {
		return exportToPDF;
	}
	
}
