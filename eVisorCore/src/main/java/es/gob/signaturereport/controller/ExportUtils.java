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
 * <b>File:</b><p>es.gob.signaturereport.controller.ExportUtils.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import es.gob.signaturereport.controller.list.StatData;
import es.gob.signaturereport.controller.list.StatDataTabular;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class ExportUtils {

    /**
     * Attribute that represents . 
     */
    private static String TITLE = Language.getMessage(LanguageKeys.WMSG162);

    /**
     * 
     * @param statDataList
     * @param chart
     * @return
     * @throws IOException
     */
    public static synchronized byte[ ] generateReportToExcel(String groupedField,List<StatData> statDataList, JFreeChart chart) throws IOException {
	// Excel creation after your dataList has been generated

	// Give your file path may be to desktop so that you can see
	org.apache.poi.hssf.usermodel.HSSFWorkbook workBook = new HSSFWorkbook();
	Row row = null;
	Cell cell = null;

	Sheet sheetResult = workBook.createSheet(Language.getMessage(LanguageKeys.WMSG152));

	CreationHelper helper = workBook.getCreationHelper();
	Drawing drawing = sheetResult.createDrawingPatriarch();
	ClientAnchor anchor = helper.createClientAnchor();
	// set top-left corner of the picture,
	// subsequent call of Picture#resize() will operate relative to it
	anchor.setCol1(4);
	anchor.setRow1(3);

	Picture pict = drawing.createPicture(anchor, loadPicture(chart, workBook));
	pict.resize();

	// auto-size picture relative to its top-left corner

	HSSFCellStyle styleTitles = workBook.createCellStyle();
	styleTitles.setBorderTop(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderRight(HSSFCellStyle.BORDER_THIN);
	styleTitles.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
	styleTitles.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	HSSFFont font = workBook.createFont();
	font.setFontName(HSSFFont.FONT_ARIAL);
	font.setFontHeightInPoints((short) 10);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	font.setColor(HSSFColor.BLACK.index);
	styleTitles.setFont(font);

	HSSFFont fontTitle = workBook.createFont();
	fontTitle.setFontName(HSSFFont.FONT_ARIAL);
	fontTitle.setFontHeightInPoints((short) 14);
	fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	fontTitle.setColor(HSSFColor.BLACK.index);

	HSSFCellStyle styleField = workBook.createCellStyle();
	styleField.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
	styleField.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderRight(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderTop(HSSFCellStyle.BORDER_THIN);

	HSSFCellStyle styleTitleLeft = workBook.createCellStyle();
	styleTitleLeft.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setFont(fontTitle);

	HSSFCellStyle styleTitleCenter = workBook.createCellStyle();
	styleTitleCenter.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleCenter.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

	HSSFCellStyle styleTitleRight = workBook.createCellStyle();
	styleTitleRight.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	int i = 1;
	row = sheetResult.createRow(i);
	row.setHeight((short) 400);
	cell = row.createCell(1);
	cell.setCellValue(TITLE);
	cell.setCellStyle(styleTitleLeft);

	i++;
	while (i < 10) {
	    cell = row.createCell(i);
	    cell.setCellStyle(styleTitleCenter);
	    i++;
	}

	cell = row.createCell(i);
	cell.setCellValue(Language.getMessage(LanguageKeys.WMSG153) + new Date());

	cell.setCellStyle(styleTitleRight);

	row = sheetResult.createRow(3);
	cell = row.createCell(1);
	cell.setCellValue(groupedField);
	cell.setCellStyle(styleTitles);

	cell = row.createCell(2);
	cell.setCellValue(Language.getMessage(LanguageKeys.WMSG158));
	cell.setCellStyle(styleTitles);

	for (int rowNo = 4; rowNo < statDataList.size() + 4; rowNo++) {
	    row = sheetResult.createRow(rowNo);

	    cell = row.createCell(1);
	    cell.setCellValue(statDataList.get(rowNo - 4).getKey());
	    cell.setCellStyle(styleField);

	    cell = row.createCell(2);
	    cell.setCellValue(statDataList.get(rowNo - 4).getValue());
	    cell.setCellStyle(styleField);

	}

	sheetResult.setColumnWidth(0, 660);
	sheetResult.setColumnWidth(1, 7500);
	sheetResult.setColumnWidth(2, 3500);
	sheetResult.setColumnWidth(3, 660);
	sheetResult.setColumnWidth(10, 4000);

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	workBook.write(out);
	return out.toByteArray();

    }

    /**
     * 
     * @param statDataList List of data to export
     * @param chart Image to export
     * @return PDF file content
     * @throws IOException if an I/O error occurs
     * @throws DocumentException if an PDF error occurs
     */
    public static synchronized byte[ ] generateReportToPDF(String groupedField, List<StatData> statDataList, JFreeChart chart) throws IOException, DocumentException {
	// PDF creation after your statDataList has been generated
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Document document = new Document();
	PdfWriter docWriter = PdfWriter.getInstance(document, out);

	HeaderFooter header = new HeaderFooter(new Phrase(Language.getMessage(LanguageKeys.WMSG155), FontFactory.getFont("arial", 8, Font.NORMAL, Color.black)), false);
	header.setAlignment(Element.ALIGN_RIGHT);
	header.setBorderWidth(1);
	document.setHeader(header);

	HeaderFooter footer = new HeaderFooter(new Phrase(Language.getMessage(LanguageKeys.WMSG156) + new Date() + Language.getMessage(LanguageKeys.WMSG157), FontFactory.getFont("arial", 8, Font.NORMAL, Color.black)), true);
	footer.setAlignment(Element.ALIGN_RIGHT);
	footer.setBorderWidth(1);
	document.setFooter(footer);

	document.open();

	document.add(new Paragraph(" "));
	document.add(new Paragraph(TITLE, FontFactory.getFont("arial", 16, Font.NORMAL, Color.GRAY)));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));

	// add chart
	int width = 500;
	int height = 390;

	// get the direct pdf content
	PdfContentByte dc = docWriter.getDirectContent();

	// get a pdf template from the direct content
	PdfTemplate tp = dc.createTemplate(width, height);

	// create an AWT renderer from the pdf template
	Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());

	Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
	chart.draw(g2, r2D, null);
	g2.dispose();

	// add the rendered pdf template to the direct content
	// you will have to play around with this because the chart is
	// absolutely positioned.
	// 38 is just a typical left margin
	// docWriter.getVerticalPosition(true) will approximate the position
	// that the content above the chart ended
	dc.addTemplate(tp, 38, docWriter.getVerticalPosition(true) - height);

	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));

	// add table
	PdfPTable table = new PdfPTable(2);

	PdfPCell c1 = new PdfPCell(new Phrase(groupedField, FontFactory.getFont("arial", 10, Font.BOLD, Color.black)));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setGrayFill(0.6f);
	table.addCell(c1);

	c1 = new PdfPCell(new Phrase(Language.getMessage(LanguageKeys.WMSG158), FontFactory.getFont("arial", 10, Font.BOLD, Color.black)));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setGrayFill(0.6f);
	table.addCell(c1);

	table.setHeaderRows(1);

	for (int rowNo = 0; rowNo < statDataList.size(); rowNo++) {
	    table.addCell(new Phrase(statDataList.get(rowNo).getKey(), FontFactory.getFont("arial", 10, Font.NORMAL, Color.black)));
	    table.addCell(new Phrase(statDataList.get(rowNo).getValue().toString(), FontFactory.getFont("arial", 10, Font.NORMAL, Color.black)));
	}

	document.add(table);

	document.close();

	return out.toByteArray();

    }

    /**
     * 
     * @param statDataList List of data to export
     * @return PDF file content
     * @throws IOException if an I/O error occurs
     * @throws DocumentException if an PDF error occurs
     */
    public static synchronized byte[ ] generateReportTabularToPDF(String rowDescription, String columnDescription,List<StatDataTabular> statDataList) throws IOException, DocumentException {
	// PDF creation after your statDataList has been generated
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	Document document = new Document();
	PdfWriter.getInstance(document, out);

	HeaderFooter header = new HeaderFooter(new Phrase(Language.getMessage(LanguageKeys.WMSG155), FontFactory.getFont("arial", 8, Font.NORMAL, Color.black)), false);
	header.setAlignment(Element.ALIGN_RIGHT);
	header.setBorderWidth(1);
	document.setHeader(header);

	HeaderFooter footer = new HeaderFooter(new Phrase(Language.getMessage(LanguageKeys.WMSG156) + new Date() + Language.getMessage(LanguageKeys.WMSG157), FontFactory.getFont("arial", 8, Font.NORMAL, Color.black)), true);
	footer.setAlignment(Element.ALIGN_RIGHT);
	footer.setBorderWidth(1);
	document.setFooter(footer);

	document.open();

	document.add(new Paragraph(" "));
	document.add(new Paragraph(TITLE, FontFactory.getFont("arial", 16, Font.NORMAL, Color.GRAY)));
	document.add(new Paragraph(" "));
	document.add(new Paragraph(" "));

	// add table
	PdfPTable table = new PdfPTable(3);

	PdfPCell c1 = new PdfPCell(new Phrase(rowDescription, FontFactory.getFont("arial", 10, Font.BOLD, Color.black)));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setGrayFill(0.6f);
	table.addCell(c1);

	c1 = new PdfPCell(new Phrase(columnDescription, FontFactory.getFont("arial", 10, Font.BOLD, Color.black)));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setGrayFill(0.6f);
	table.addCell(c1);

	c1 = new PdfPCell(new Phrase(Language.getMessage(LanguageKeys.WMSG158), FontFactory.getFont("arial", 10, Font.BOLD, Color.black)));
	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	c1.setGrayFill(0.6f);
	table.addCell(c1);

	table.setHeaderRows(1);

	for (int rowNo = 0; rowNo < statDataList.size(); rowNo++) {
	    table.addCell(new Phrase(statDataList.get(rowNo).getRowName(), FontFactory.getFont("arial", 10, Font.NORMAL, Color.black)));
	    table.addCell(new Phrase(statDataList.get(rowNo).getColumnName(), FontFactory.getFont("arial", 10, Font.NORMAL, Color.black)));
	    table.addCell(new Phrase(statDataList.get(rowNo).getValue().toString(), FontFactory.getFont("arial", 10, Font.NORMAL, Color.black)));
	}

	document.add(table);

	document.close();

	return out.toByteArray();

    }

    /**
     * 
     * @param chart image to attach 	
     * @param wb Workbook
     * @return image index
     * @throws IOException if an I/O error occurs
     */
    private static synchronized int loadPicture(JFreeChart chart, HSSFWorkbook wb) throws IOException {
	int pictureIndex;
	FileInputStream fis = null;
	ByteArrayOutputStream bos = null;
	try {

	    BufferedImage buffImg = chart.createBufferedImage(500, 390, BufferedImage.TYPE_INT_RGB, null);

	    pictureIndex = wb.addPicture(EncoderUtil.encode(buffImg, ImageFormat.JPEG), HSSFPicture.PICTURE_TYPE_JPEG);

	} finally {
	    if (fis != null) {
		fis.close();
	    }
	    if (bos != null) {
		bos.close();
	    }
	}
	return pictureIndex;
    }

    /**
     * 
     * @param statDataList List of data for statistic
     * @return	Excel file content
     * @throws IOException if an I/O error occurs
     */
    public static byte[ ] generateReportTabularToExcel(String rowDescription, String columnDescription,List<StatDataTabular> statDataList) throws IOException {
	// Excel creation after your dataList has been generated

	// Give your file path may be to desktop so that you can see
	org.apache.poi.hssf.usermodel.HSSFWorkbook workBook = new HSSFWorkbook();
	Row row = null;
	Cell cell = null;

	Sheet sheetResult = workBook.createSheet(Language.getMessage(LanguageKeys.WMSG152));

	HSSFCellStyle styleTitles = workBook.createCellStyle();
	styleTitles.setBorderTop(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	styleTitles.setBorderRight(HSSFCellStyle.BORDER_THIN);
	styleTitles.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
	styleTitles.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	HSSFFont font = workBook.createFont();
	font.setFontName(HSSFFont.FONT_ARIAL);
	font.setFontHeightInPoints((short) 10);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	font.setColor(HSSFColor.BLACK.index);
	styleTitles.setFont(font);

	HSSFFont fontTitle = workBook.createFont();
	fontTitle.setFontName(HSSFFont.FONT_ARIAL);
	fontTitle.setFontHeightInPoints((short) 14);
	fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	fontTitle.setColor(HSSFColor.BLACK.index);

	HSSFCellStyle styleField = workBook.createCellStyle();
	styleField.setFillBackgroundColor(HSSFColor.DARK_BLUE.index);
	styleField.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderRight(HSSFCellStyle.BORDER_THIN);
	styleField.setBorderTop(HSSFCellStyle.BORDER_THIN);

	HSSFCellStyle styleTitleLeft = workBook.createCellStyle();
	styleTitleLeft.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleLeft.setFont(fontTitle);

	HSSFCellStyle styleTitleCenter = workBook.createCellStyle();
	styleTitleCenter.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleCenter.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

	HSSFCellStyle styleTitleRight = workBook.createCellStyle();
	styleTitleRight.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	styleTitleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

	int i = 1;
	row = sheetResult.createRow(i);
	row.setHeight((short) 400);
	cell = row.createCell(1);
	cell.setCellValue(TITLE);
	cell.setCellStyle(styleTitleLeft);

	i++;
	while (i < 10) {
	    cell = row.createCell(i);
	    cell.setCellStyle(styleTitleCenter);
	    i++;
	}

	cell = row.createCell(i);
	cell.setCellValue(Language.getMessage(LanguageKeys.WMSG153) + new Date());

	cell.setCellStyle(styleTitleRight);

	row = sheetResult.createRow(3);
	cell = row.createCell(1);
	cell.setCellValue(rowDescription);
	cell.setCellStyle(styleTitles);

	cell = row.createCell(2);
	cell.setCellValue(columnDescription);
	cell.setCellStyle(styleTitles);

	cell = row.createCell(3);
	cell.setCellValue("Coincidencias");
	cell.setCellStyle(styleTitles);

	for (int rowNo = 4; rowNo < statDataList.size() + 4; rowNo++) {
	    row = sheetResult.createRow(rowNo);

	    cell = row.createCell(1);
	    cell.setCellValue(statDataList.get(rowNo - 4).getRowName());
	    cell.setCellStyle(styleField);

	    cell = row.createCell(2);
	    cell.setCellValue(statDataList.get(rowNo - 4).getColumnName());
	    cell.setCellStyle(styleField);

	    cell = row.createCell(3);
	    cell.setCellValue(statDataList.get(rowNo - 4).getValue());
	    cell.setCellStyle(styleField);

	}

	sheetResult.setColumnWidth(0, 660);
	sheetResult.setColumnWidth(1, 7500);
	sheetResult.setColumnWidth(2, 7500);
	sheetResult.setColumnWidth(3, 5000);

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	workBook.write(out);
	return out.toByteArray();
    }

}
