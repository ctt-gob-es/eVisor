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
 * <b>File:</b><p>es.gob.signaturereport.tools.ImageUtils.java.</p>
 * <b>Description:</b><p>Utility class for processing images.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 15/06/2011.
 */
package es.gob.signaturereport.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mreport.items.PageDocumentImage;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;


/** 
 * <p>Utility class for processing images.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 15/06/2011.
 */
public final class ImageUtils {
	
	/**
	 * Attribute that represents the three quarters of rotation. 
	 */
	private static final int THREE_QUARTERS_ROTATION = 270;
	
	/**
	 * Attribute that represents the half rotation. 
	 */
	private static final int HALF_ROTATION = 180;
	
	/**
	 * Attribute that represents a quarter of rotation. 
	 */
	private static final int QUARTER_ROTATION = 90;
	
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(ImageUtils.class);
	
	/**
	 * Attribute that represents the maximum number of lines. 
	 */
	private static final  int MAXLINENUMBERS = Integer.parseInt(StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.line.maxnumber").trim());
	
	/**
	 * Attribute that represents the font name. 
	 */
	private static final String FONTNAME = StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.font.name");
	
	/**
	 * Attribute that represents the maximum number of characters may be included into a text line. 
	 */
	private static final int MAXCHARACTERNUMBERS = Integer.parseInt(StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.line.character.maxnumber").trim());
	
	/**
	 * Attribute that represents the font size. 
	 */
	private static final int FONTSIZE= Integer.parseInt(StaticSignatureReportProperties.getProperty("signaturereport.global.textToImage.font.size").trim());
	
	/**
	 * Attribute that represents the content type of image used to extract the PDF page.
	 */
	private static final String CONTENT_TYPE_IMG = "image/png";

	/**
	 * Attribute that represents the image format used to extract the PDF page.
	 */
	private static final String IMG_FORMAT = "png";
	
	/**
	 * Attribute that represents the class used to encoded to Base64.
	 */
	private static UtilsBase64 base64Tools = new UtilsBase64();
	
	
	
	/**
	 * Method to rotate an image 90,180 or 270 degrees.
	 * @param image	Image that will be rotate.
	 * @param angle	Rotated angle. The allowed values are 90, 180 or 270.
	 * @param imageFormat Format of image.
	 * @return	Rotated image.
	 * @throws ToolsException 
	 */
	public static byte[] rotate(byte[] image, double angle, String imageFormat) throws ToolsException{
		if(angle==QUARTER_ROTATION || angle==HALF_ROTATION || angle == THREE_QUARTERS_ROTATION){
			InputStream in = null;
			ByteArrayOutputStream  out = null;
			try {
				in = new ByteArrayInputStream(image);
				BufferedImage buffImage = ImageIO.read(in);
				AffineTransform rotateTransform = AffineTransform.getRotateInstance(Math.toRadians(angle),buffImage.getWidth() / 2.0, buffImage.getHeight() / 2.0);
				if(angle==QUARTER_ROTATION || angle == THREE_QUARTERS_ROTATION){
					AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(QUARTER_ROTATION),buffImage.getWidth() / 2.0, buffImage.getHeight() / 2.0);
					Point2D p2din, p2dout;

				    p2din = new Point2D.Double(0.0, 0.0);
				    p2dout = at.transform(p2din, null);
				    double ytrans = p2dout.getY();

				    p2din = new Point2D.Double(0, buffImage.getHeight());
				    p2dout = at.transform(p2din, null);
				    double xtrans = p2dout.getX();

				    AffineTransform tat = new AffineTransform();
				    tat.translate(-xtrans, -ytrans);
				    rotateTransform.preConcatenate(tat);
				}
				BufferedImageOp bio = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
			    BufferedImage destinationBI = bio.filter(buffImage, null);
			    out = new ByteArrayOutputStream();
			    ImageIO.write(destinationBI, imageFormat, out);
			    return out.toByteArray();
			} catch (IOException e) {
				String msg = Language.getMessage(LanguageKeys.UTIL_036);
				LOGGER.error(msg,e);
				throw new ToolsException(ToolsException.UNKNOWN_ERROR, msg,e);
			}finally{
				UtilsResources.safeCloseInputStream(in);
				UtilsResources.safeCloseOutputStream(out);
			}
		}else{
			throw new ToolsException(ToolsException.INVALID_ROTATED_ANGLE, Language.getFormatMessage(LanguageKeys.UTIL_035, new Object[]{angle}));
		}
	}
	
	/**
	 * Converts a text file to one o more images.
	 * @param text	Text file.
	 * @param includeURL	Indicates if the RFC 2397 URL will be returned.
	 * @param includeContent	Indicates if the content image will be returned.
	 * @return	List of {@link PageDocumentImage}.
	 * @throws ToolsException	If an error occurs.
	 */
	public static ArrayList<PageDocumentImage> textToImage(byte[] text,boolean includeURL, boolean includeContent) throws ToolsException{
		ArrayList<PageDocumentImage> images = new ArrayList<PageDocumentImage>();
		ArrayList<String> lines = new ArrayList<String>();
		InputStream in = new ByteArrayInputStream(text);
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(inReader);
		try {
			String line = null;
			int imgNumber = 1; 
			while((line=reader.readLine())!=null){
				while(line.length()>MAXCHARACTERNUMBERS){
					lines.add(line.substring(0,MAXCHARACTERNUMBERS));
					line = line.substring(MAXCHARACTERNUMBERS);
				}
				lines.add(line);
				if(lines.size()>MAXLINENUMBERS){
					ArrayList<String> linesToImage = new ArrayList<String>(lines.subList(0, MAXLINENUMBERS));
					lines.subList(0, MAXLINENUMBERS).clear();
					byte[] image = textLinesToImage(linesToImage);
					PageDocumentImage pageImg = toPageDocumentImage(image,imgNumber, CONTENT_TYPE_IMG, includeURL, includeContent);
					images.add(pageImg);
					imgNumber++;
				}
			}
			if(!lines.isEmpty()){
				byte[] image = textLinesToImage(lines);
				PageDocumentImage pageImg = toPageDocumentImage(image,imgNumber, CONTENT_TYPE_IMG, includeURL, includeContent);
				images.add(pageImg);
			}
		} catch (IOException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_045);
			LOGGER.error(msg,e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR,msg,e);
		}finally{
			UtilsResources.safeCloseInputStream(in);
		}
		return images;
	}
	
	/**
	 * Includes the image into a {@link PageDocumentImage} object.
	 * @param image	Image file.
	 * @param pageNumber	Page number.
	 * @param mimeType	MimeType.
	 * @param includeURL	Indicates if the RFC 2397 URL will be returned.	
	 * @param includeContent	Indicates if the content image will be returned.
	 * @return	A {@link PageDocumentImage} object that contains the image.
	 */
	public static PageDocumentImage toPageDocumentImage(byte[] image,int pageNumber, String mimeType,boolean includeURL, boolean includeContent){
		PageDocumentImage pageImg = new PageDocumentImage(pageNumber);
		pageImg.setContentType(mimeType);
		if (includeContent) {
			pageImg.setContent(image);
		}
		if (includeURL) {
			String encodedImg = base64Tools.encodeBytes(image);
			String url = URLUtils.createRFC2397URL(mimeType, encodedImg);
			pageImg.setLocation(url);
		}
		return pageImg;
	}
	
	/**
	 * Creates a image that contains the supplied text lines.
	 * @param lines	Test lines.
	 * @return	Image.	
	 * @throws ToolsException	If an error occurs.
	 */
	private static byte[] textLinesToImage(ArrayList<String> lines) throws ToolsException{
		Font font = new Font(FONTNAME, Font.PLAIN, FONTSIZE);
		ArrayList<Rectangle2D> boundList = new ArrayList<Rectangle2D>();
		FontRenderContext frc = new FontRenderContext(null, true, true);
		double height = 0;
		double width = 0;
		for(int j=0;j<lines.size();j++){
			Rectangle2D bounds = font.getStringBounds(lines.get(j), frc);
            if(bounds.getWidth()>width){
            	width=bounds.getWidth();
            }
            height = height + bounds.getHeight();
            boundList.add(bounds);
		}
        BufferedImage image = new BufferedImage((int)width+1,(int) height+1,   BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int)width+1,(int) height+1);
        g.setColor(Color.BLACK);
        g.setFont(font);
        float yPos = 0;
        for(int j=0;j<lines.size();j++){
        	g.drawString(lines.get(j), (float) boundList.get(j).getX(), (float) (yPos - boundList.get(j).getY()));
        	yPos = (float) (yPos + boundList.get(j).getHeight());
        }
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			ImageIO.write(image, IMG_FORMAT, out);
			return out.toByteArray();
		} catch (IOException e) {
			String msg = Language.getMessage(LanguageKeys.UTIL_045);
			LOGGER.error(msg,e);
			throw new ToolsException(ToolsException.UNKNOWN_ERROR,msg,e);
		}finally{
			UtilsResources.safeCloseOutputStream(out);
		}
	}

	/**
	 * Constructor method for the class ImageUtils.java. 
	 */
	private ImageUtils() {
		
	}
	
		
}
