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
 * <b>File:</b><p>es.gob.signaturereport.tools.PDFUtils.java.</p>
 * <b>Description:</b><p> Class that contains tools to manage of PDF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>04/03/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 04/03/2011.
 */
package es.gob.signaturereport.tools;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.krysalis.barcode4j.tools.UnitConv;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mreport.items.FileAttachment;
import es.gob.signaturereport.mreport.items.MatrixPagesInclude;
import es.gob.signaturereport.mreport.items.PageDocumentImage;
import es.gob.signaturereport.mreport.items.PageIncludeFormat;
import es.gob.signaturereport.properties.SignatureReportPropertiesI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/**
 * <p>Class that contains tools to manage of PDF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 04/03/2011.
 */
public final class PDFUtils {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PDFUtils.class);

	/**
	 * Attribute that represents the class used to encoded to Base64.
	 */
	private static UtilsBase64 base64Tools = new UtilsBase64();

	/**
	 * Attribute that represents the last index to finding the "Linearized" string. 
	 */
	private static final int LINEAR_LAST_INDEX = 1024;

	/**
	 * Attribute that represents the number 12. 
	 */
	private static final int XII = 12;
	/**
	 * Attribute that represents the content type of image used to extract the PDF page.
	 */
	private static final String CONTENT_TYPE_IMG = "image/png";

	/**
	 * Attribute that represents the image format used to extract the PDF page.
	 */
	private static final String IMG_FORMAT = "png";
	/**
	 * Attribute that represents the default resolution (dpi) used to create PDF file.
	 */
	private static final int RESOLUTION_DEFAULT = 72;

	/**
	 * Attribute that represents the 3 number. 
	 */
	private static final int III = 3;
	

	/**
	 * Attribute that represents the 90 number. 
	 */
	private static final float XC = 90;
	
	/**
	 * Attribute that represents the 270 number. 
	 */
	private static final float CCLXX = 270;
	
	/**
	 * Attribute that represents the 180 number. 
	 */
	private static final float CLXXX = 180;
	
	@Inject
	private static SignatureReportPropertiesI signatureReportProperties;

	/**
	 * Attribute that represents the font name. 
	 */
	 private static final String FONTNAME = StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.font.name");

	/**
	 * Attribute that represents the font size. 
	 */
	private static final int FONTSIZE=Integer.parseInt(StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.font.size").trim());
	
	
	/**
	 * Constructor method for the class PDFUtils.java.
	 */
	private PDFUtils() {
	}

	/**
	 * Adds the supplied attachment list to supplied PDF.
	 * @param pdf	PDF file.
	 * @param attachments	 Attachment files.
	 * @return	Modified file.
	 * @throws ToolsException	If an error occurs.
	 */
	public static byte[ ] addAttachment(byte[ ] pdf, ArrayList<FileAttachment> attachments) throws ToolsException {
		byte[ ] doc = null;
		ByteArrayOutputStream out = null;
		try {
			PdfReader readerPdf = new PdfReader(pdf);
			out = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(readerPdf, out);
			for (int i = 0; i < attachments.size(); i++) {
				FileAttachment attachment = attachments.get(i);
				stamper.addFileAttachment(attachment.getDescription(), attachment.getContent(), null, attachment.getName());
			}
			stamper.close();
			doc = out.toByteArray();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_020);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);

		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}
		return doc;
	}

	/**
	 * Concatenates the supplied PDF files.
	 * @param pdf1	PDF file.
	 * @param pdf2	PDF file.
	 * @param identifier1	Token used to identifies the first input document.
	 * @param identifier2	Token used to identifies the second input document.
	 * @param rule	Concatenation rule.Ej: $identifier1(1)+ $identifier2(4-6). Concat the page 1 of the first document with the pages 4 to 6 of the second document. 
	 * @return	Concatenated PDF file.
	 * @throws ToolsException	If an error occurs.
	 */
	public static byte[ ] concatPDF(byte[ ] pdf1, byte[ ] pdf2, String identifier1, String identifier2, String rule) throws ToolsException {
		String[ ] p = rule.split("\\+");
		byte[ ] pdf = null;
		ByteArrayOutputStream out = null;
		try {
			PdfReader readerPdf1 = new PdfReader(pdf1);
			PdfReader readerPdf2 = new PdfReader(pdf2);
			com.lowagie.text.Rectangle psize = readerPdf1.getPageSize(1);
			Document document = new Document(new com.lowagie.text.Rectangle(psize.getWidth(), psize.getHeight()));
			out = new ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(document, out);
			document.open();
			for (int i = 0; i < p.length; i++) {
				String docId = null;
				int iniPage = -1;
				int endPage = -1;
				p[i] = p[i].trim();
				int pos = p[i].indexOf('(');
				if (pos > 0) {
					docId = p[i].substring(0, pos);
					String intval = p[i].substring(pos + 1, p[i].length() - 1).trim();
					String[ ] intvalSp = intval.split("-");
					iniPage = Integer.parseInt(intvalSp[0].trim());
					if (intvalSp.length == 2) {
						endPage = Integer.parseInt(intvalSp[1].trim());
					} else {
						// Una unica pagina
						endPage = iniPage;
					}
				} else {
					docId = p[i];
				}
				if (docId != null) {
					if (docId.equals(identifier1)) {
						if (iniPage < 0) {
							// Todo el documento
							iniPage = 1;
							endPage = readerPdf1.getNumberOfPages();
						} else if (iniPage <= readerPdf1.getNumberOfPages()) {
							if (endPage == -1) {
								// El documento hasta el final
								endPage = readerPdf1.getNumberOfPages();
							} else if (endPage > readerPdf1.getNumberOfPages()) {
								String msg = Language.getFormatMessage(LanguageKeys.UTIL_019, new Object[ ] { String.valueOf(endPage), docId });
								LOGGER.error(msg);
								throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);
							}
						} else {
							String msg = Language.getFormatMessage(LanguageKeys.UTIL_019, new Object[ ] { String.valueOf(iniPage), docId });
							LOGGER.error(msg);
							throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);

						}
						while (iniPage <= endPage) {
							copy.addPage(copy.getImportedPage(readerPdf1, iniPage));
							iniPage++;
						}
						copy.freeReader(readerPdf1);
					} else if (docId.equals(identifier2)) {
						if (iniPage < 0) {
							// Todo el documento
							iniPage = 1;
							endPage = readerPdf2.getNumberOfPages();
						} else if (iniPage <= readerPdf2.getNumberOfPages()) {
							if (endPage == -1) {
								// El documento hasta el final
								endPage = readerPdf2.getNumberOfPages();
							} else if (endPage > readerPdf2.getNumberOfPages()) {
								String msg = Language.getFormatMessage(LanguageKeys.UTIL_019, new Object[ ] { String.valueOf(endPage), docId });
								LOGGER.error(msg);
								throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);
							}
						} else {
							String msg = Language.getFormatMessage(LanguageKeys.UTIL_019, new Object[ ] { String.valueOf(iniPage), docId });
							LOGGER.error(msg);
							throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);

						}
						while (iniPage <= endPage) {
							copy.addPage(copy.getImportedPage(readerPdf2, iniPage));
							iniPage++;
						}
						copy.freeReader(readerPdf2);
					} else {
						String msg = Language.getFormatMessage(LanguageKeys.UTIL_018, new Object[ ] { docId });
						LOGGER.error(msg);
						throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);
					}
				} else {
					String msg = Language.getMessage(LanguageKeys.UTIL_017);
					LOGGER.error(msg);
					throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg);
				}
			}
			document.close();
			pdf = out.toByteArray();
		} catch (NumberFormatException nfe) {
			String msg = Language.getMessage(LanguageKeys.UTIL_015);
			LOGGER.error(msg, nfe);
			throw new ToolsException(ToolsException.INVALID_CONCATENATION_RULE, msg, nfe);
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_016);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}
		return pdf;
	}

	/**
	 * Checks if the supplied document is a PDF document. 
	 * @param file	Document to check.
	 * @return	True if the document is a PDF file. Otherwise false
	 */
	public static boolean isPDFFile(byte[ ] file) {
		boolean pdf = false;
		if (file != null) {
			int i = 0;
			boolean end = false;
			while (i < file.length && !end) {
				if (file[i] != ' ' && file[i] != '\n' && file[i] != '\t' && file[i] != '\r') {
					end = true;
					pdf = i + III < file.length && file[i] == '%' && file[i + 1] == 'P' && file[i + 2] == 'D' && file[i + III] == 'F';
				}
				i++;
			}
		}
		return pdf;
	}

	/**
	 * Method that creates a PDF file by including a document in another with the specified rules.
	 * @param targetPdf	Target document.
	 * @param originPdf	Original document.
	 * @param pagesIncl	Rules.
	 * @return	A PDF document.
	 * @throws ToolsException	If an error occurs.
	 */
	public static byte[ ] includePages(byte[ ] targetPdf, byte[ ] originPdf, MatrixPagesInclude pagesIncl) throws ToolsException {
		byte[ ] pdf = null;
		ByteArrayOutputStream out = null;
		try {
			PdfReader readerTarget = new PdfReader(targetPdf);
			PdfReader readerOrigin = new PdfReader(originPdf);
			Document document = new Document();
			out = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			PdfContentByte directContent = writer.getDirectContent();
			PdfContentByte under = writer.getDirectContentUnder();
			for (int i = 1; i <= readerTarget.getNumberOfPages(); i++) {
				com.lowagie.text.Rectangle rectangle = readerTarget.getPageSize(i);
				document.setPageSize(rectangle);
				document.newPage();
				PdfImportedPage pageTarget = writer.getImportedPage(readerTarget, i);

				directContent.addTemplate(pageTarget, 0, 0);
				int[ ] originPages = pagesIncl.getPageToInclude(i);
				if (originPages != null) {
					for (int j = 0; j < originPages.length; j++) {
						PageIncludeFormat[ ] format = pagesIncl.getPagesFormat(i, originPages[j]);
						PdfImportedPage pageOrigen = writer.getImportedPage(readerOrigin, (originPages[j]));
						float rotate = 0;
						if(readerOrigin.getPageN((originPages[j])).getAsNumber(PdfName.ROTATE)!=null){
							rotate = readerOrigin.getPageN((originPages[j])).getAsNumber(PdfName.ROTATE).floatValue();
						}
						for (int k = 0; k < format.length; k++) {
							float a = 0;
							float b = 0;
							float c = 0;
							float d = 0;
							float e = 0;
							float f = 0;
							float width = 0;
							if (format[k].getWidth() > 0) {
								width = UnitConv.mm2px(format[k].getWidth(), RESOLUTION_DEFAULT);
							}
							float height = 0;
							if (format[k].getHeight() > 0) {
								height = UnitConv.mm2px(format[k].getHeight(), RESOLUTION_DEFAULT);
							}
							float xpos = UnitConv.mm2px(format[k].getXpos(), RESOLUTION_DEFAULT);
							float ypos = UnitConv.mm2px(format[k].getYpos(), RESOLUTION_DEFAULT);
							float xFactor = 1;
							if (width > 0) {
								xFactor = width / pageOrigen.getWidth();
							}

							float yFactor = 1;
							if (height > 0) {
								yFactor = height / pageOrigen.getHeight();
							}
							if(rotate == 0){
								a = xFactor;
								b = 0;
								c = 0;
								d = yFactor;
								e = xpos;
								f = ypos;
							}else if(rotate == XC){
								xFactor = width/pageOrigen.getHeight();
								yFactor = height/pageOrigen.getWidth();
								a = 0;
								b = -yFactor;
								c = xFactor;
								d=0;
								e=xpos;
								f=ypos+height;
							}else if(rotate == CCLXX){
								xFactor = width/pageOrigen.getHeight();
								yFactor = height/pageOrigen.getWidth();
								a = 0;
								b = yFactor;
								c = -xFactor;
								d=0;
								e=xpos+width;
								f=ypos;
							}else{
								float angle = (float) (-rotate * (Math.PI / CLXXX));
								float  rotWidth = (float) ( (pageOrigen.getHeight() * Math.sin(angle)) + (pageOrigen.getWidth() * Math.cos(angle))) ;
								xFactor = (float) (width / rotWidth);
								float  rotHeight = (float) ( (pageOrigen.getWidth() * Math.sin(angle)) + (pageOrigen.getHeight() * Math.cos(angle)) );
								yFactor = height / rotHeight;
								a = (float) (xFactor * Math.cos(angle));
								b = (float) (yFactor * Math.sin(angle));
								c = (float) (xFactor * - Math.sin(angle));
								d = (float) (yFactor * Math.cos(angle));
								e = (float) (xpos + (width * Math.sin(angle)));
								f = (float) (ypos + (height * Math.cos(angle)));
							}	
							if (format[k].getLayout().equals(PageIncludeFormat.BACK_LAYOUT)) {
								under.addTemplate(pageOrigen, a,b,c,d,e,f);
							} else {	
								directContent.addTemplate(pageOrigen, a,b,c,d,e,f);
							}

						}
					}
				}
			}
			document.close();
			readerOrigin.close();
			readerTarget.close();
			pdf = out.toByteArray();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_014);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}
		return pdf;

	}

	/**
	 * Gets the page number of a PDF document.
	 * @param document	PDF document.
	 * @return	Page number.
	 * @throws ToolsException	If an error occurs.
	 */
	public static int getNumPages(byte[ ] document) throws ToolsException {
		try {
			PdfReader reader = new PdfReader(document);

			return reader.getNumberOfPages();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_009);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.INVALID_PDF_FILE, msg, e);
		}
	}
	
	/**
	 * Gets a List of String that represents the orientation of each page.
	 * @param document	PDF document.
	 * @return a List of String that represents the orientation of each page.
	 * @throws ToolsException If an error occurs.
	 */
	public static List<String> getPagesOrientation(byte[ ] document) throws ToolsException {
		
		List<String> pages = new ArrayList<String>();
		try {
		
    		PdfReader reader = new PdfReader(document);
    		
    		int numPages = reader.getNumberOfPages();
    		
    		for (int i = 1; i <= numPages; i++) {
    			com.lowagie.text.Rectangle page = reader.getPageSize(i);
    			if (page.getWidth() > page.getHeight()) {
    				pages.add("H");
    			} else {
    				pages.add("V");
    			}
    		}
		
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_009);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.INVALID_PDF_FILE, msg, e);
		}

		return pages;
		
	}

	/**
	 * Gets the images of selected pages of the PDF document.
	 * @param document	PDF document.
	 * @param range	Page range. Ej1: 1-3 (prints the pages 1 to 3). Ej2: 3,4,5-11 (prints the pages 3 , 4 and 5 to 11). If the value is 'null' prints all document pages.
	 * @param includeURL	Indicates if the RFC 2397 URL will be returned.
	 * @param includeContent	Indicates if the content image will be returned.
	 * @return	List of {@link PageDocumentImage}.
	 * @throws ToolsException	If an error occurs.
	 */
	public static ArrayList<PageDocumentImage> getPDFPages(byte[ ] document, String range, boolean includeURL, boolean includeContent) throws ToolsException {
		ArrayList<PageDocumentImage> pages = new ArrayList<PageDocumentImage>();

		PDFFile pdf = getPDFViewFile(document);
		if (range == null) {
			for (int i = 0; i < pdf.getNumPages(); i++) {
				PDFPage page = pdf.getPage(i + 1);
				PageDocumentImage pageImg = new PageDocumentImage(i + 1);
				pageImg.setContentType(CONTENT_TYPE_IMG);
				byte[ ] content = getImagePageContent(page);
				if (includeContent) {
					pageImg.setContent(content);
				}

				if (includeURL) {
					String encodedImg = base64Tools.encodeBytes(content);
					String url = URLUtils.createRFC2397URL(CONTENT_TYPE_IMG, encodedImg);
					pageImg.setLocation(url);
				}
				pages.add(pageImg);
			}
		} else {
			String[ ] p = range.trim().split(",");
			try {
				for (int i = 0; i < p.length; i++) {
					if (p[i].indexOf('-') > 0) {
						String[ ] intval = p[i].split("-");
						int pos = Integer.parseInt(intval[0]);
						int end = Integer.parseInt(intval[1]);
						if (pos < end && end <= pdf.getNumPages()) {
							while (pos <= end) {
								PDFPage page = pdf.getPage((pos));
								PageDocumentImage pageImg = new PageDocumentImage(pos);
								pageImg.setContentType(CONTENT_TYPE_IMG);
								byte[ ] content = getImagePageContent(page);
								if (includeContent) {
									pageImg.setContent(content);
								}

								if (includeURL) {
									String encodedImg = base64Tools.encodeBytes(content);
									String url = URLUtils.createRFC2397URL(CONTENT_TYPE_IMG, encodedImg);
									pageImg.setLocation(url);
								}
								pages.add(pageImg);
								pos++;
							}
						}
					} else {
						int number = Integer.parseInt(p[i]);
						if (number <= pdf.getNumPages() && number > 0) {
							PDFPage page = pdf.getPage(number);
							PageDocumentImage pageImg = new PageDocumentImage(number);
							pageImg.setContentType(CONTENT_TYPE_IMG);
							byte[ ] content = getImagePageContent(page);
							if (includeContent) {
								pageImg.setContent(content);
							}

							if (includeURL) {
								String encodedImg = base64Tools.encodeBytes(content);
								String url = URLUtils.createRFC2397URL(CONTENT_TYPE_IMG, encodedImg);
								pageImg.setLocation(url);
							}
							pages.add(pageImg);
						}
					}
				}
			} catch (NumberFormatException nfe) {
				String msg = Language.getMessage(LanguageKeys.UTIL_012);
				LOGGER.error(msg, nfe);
				throw new ToolsException(ToolsException.INVALID_PAGE_NUMBER, msg, nfe);
			}
		}

		return pages;
	}

	/**
	 * Gets the {@link PDFFile} object associated to the supplied PDF.
	 * @param document	PDF document.
	 * @return	A {@link PDFFile} object.
	 * @throws ToolsException	If the PDF document is not valid.
	 */
	private static PDFFile getPDFViewFile(byte[ ] document) throws ToolsException {
		boolean isLinearized = false;

		int i = 0;
		while (i < LINEAR_LAST_INDEX && !isLinearized) {
			if ((document[i] == '<') && i + XII < LINEAR_LAST_INDEX) {
				isLinearized = (document[++i] == '<') && (document[++i] == '/') && (document[++i] == 'L') && (document[++i] == 'i') && (document[++i] == 'n') && (document[++i] == 'e') && (document[++i] == 'a') && (document[++i] == 'r') && (document[++i] == 'i') && (document[++i] == 'z') && (document[++i] == 'e') && (document[++i] == 'd');
			}
			i++;
		}
		byte[ ] pdf = null;
		if (!isLinearized) {
			pdf = document;
		} else {
			try {
				PdfReader reader = new PdfReader(document);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				com.lowagie.text.Rectangle psize = reader.getPageSize(1);
				Document pdfDocument = new Document(new com.lowagie.text.Rectangle(psize.getWidth(), psize.getHeight()));

				PdfWriter writer = PdfWriter.getInstance(pdfDocument, out);
				pdfDocument.open();
				PdfContentByte directContent = writer.getDirectContent();
				for (int j = 1; j <= reader.getNumberOfPages(); j++) {
					pdfDocument.newPage();
					PdfImportedPage pageTarget = writer.getImportedPage(reader, j);

					directContent.addTemplate(pageTarget, 0, 0);
				}
				pdfDocument.close();
				pdf = out.toByteArray();
				out.close();
			} catch (Exception e) {
				String msg = Language.getMessage(LanguageKeys.UTIL_009);
				LOGGER.error(msg, e);
				throw new ToolsException(ToolsException.INVALID_PDF_FILE, msg, e);
			}
		}

		try {
			ByteBuffer buf = ByteBuffer.wrap(pdf);
			return new PDFFile(buf);
		} catch (IOException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_009);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.INVALID_PDF_FILE, msg, e);
		}
	}

	/**
	 * Gets the image of a PDF page. 
	 * @param page	PDF page.
	 * @return	Image of the page.
	 * @throws ToolsException	If an error occurs.
	 */
	private static byte[ ] getImagePageContent(PDFPage page) throws ToolsException {
		ByteArrayOutputStream out = null;
		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());

		// generate the image
		Image img = page.getImage(rect.width, rect.height, // width
		// &
		// height
		rect, // clip rect
		null, // null for the ImageObserver
		true, // fill background with white
		true // block until drawing is done
		);
		BufferedImage bimage = toBufferedImage(img);
		out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bimage, IMG_FORMAT, out);
			return out.toByteArray();

		} catch (IOException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_009);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}

	}

	/**
	 * Convert a {@link Image} to {@link BufferedImage}. 
	 * @param image	{@link Image}.
	 * @return	{@link BufferedImage}.
	 */
	private static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		// CHECKSTYLE:OFF -- No existe el m�todo clone en la clase Image
		image = new ImageIcon(image).getImage();
		// CHECKSTYLE:ON

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;

			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	/**
	 * Method that converts a text file to PDF file.
	 * @param text	Text file.
	 * @return	PDF file.
	 * @throws ToolsException If an error occurs.
	 */
	public static byte[ ] textToPDF(byte[ ] text) throws ToolsException {
		Document pdfDoc = new Document(com.lowagie.text.PageSize.A4);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(pdfDoc, out);
			pdfDoc.open();
			Font font = FontFactory.getFont(FONTNAME, FONTSIZE);
			pdfDoc.add(new Paragraph(new String(text), font));
			pdfDoc.close();
			return out.toByteArray();
		} catch (DocumentException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_046);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}

	}

	/**
	 * Methods that concatenates the supplied PDF files to a PDF file.
	 * @param files	List of PDF files.
	 * @return	Concatenated PDF file.
	 * @throws ToolsException	If an error occurs.
	 */
	public static byte[ ] concatPDFs(List<byte[ ]> files) throws ToolsException {
		Document pdfDoc = new Document(com.lowagie.text.PageSize.A4);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfCopy copy = new PdfCopy(pdfDoc, out);
			pdfDoc.open();
			for (int i = 0; i < files.size(); i++) {
				PdfReader reader = new PdfReader(files.get(i));
				int numPages = reader.getNumberOfPages();
				for (int j = 1; j <= numPages; j++) {
					copy.addPage(copy.getImportedPage(reader, j));
				}
				copy.freeReader(reader);
			}
			pdfDoc.close();
			return out.toByteArray();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_016);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}

	}

	/**
	 * Method that converts a image file to PDF file.
	 * @param image	Image file.
	 * @return	PDF file.
	 * @throws ToolsException If an error occurs.
	 */
	public static byte[ ] imageToPDF(byte[ ] image) throws ToolsException {
		Document pdfDoc = new Document(com.lowagie.text.PageSize.A4);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(pdfDoc, out);
			pdfDoc.open();
			com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(image);
			pdfDoc.add(img);
			pdfDoc.close();
			return out.toByteArray();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_047);
			LOGGER.error(msg, e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg, e);
		} finally {
			UtilsResources.safeCloseOutputStream(out);
		}

	}

}
