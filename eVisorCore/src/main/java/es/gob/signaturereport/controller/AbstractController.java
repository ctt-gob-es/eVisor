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
 * <b>File:</b><p>es.gob.signaturereport.controller.AbstractController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacade;
import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.AlarmConfigurationFacade;
import es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.ApplicationConfigurationFacade;
import es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacade;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.UserConfigurationFacade;
import es.gob.signaturereport.configuration.access.UserConfigurationFacadeI;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManager;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.statistics.StatisticsManager;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */

public abstract class AbstractController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5114036874121894709L;
	/**
     * Attribute that represents . 
     */
    public static String PARAM1 = "param1";
    /**
     * Attribute that represents . 
     */
    public static String PARAM2 = "param2";

    /**
     * Attribute that represents . 
     */
    static public int STATUS_NONE = 0;

    /**
     * Attribute that represents . 
     */
    static public int STATUS_EDIT = 1;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_NEW = 2;

    // Status for templates
    /**
     * Attribute that represents . 
     */
    static public int STATUS_EDIT_PDF = 1;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_NEW_PDF = 2;


    // Status for applications/organization units
    /**
     * Attribute that represents . 
     */
    static public int STATUS_EDIT_UO = 1;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_NEW_UO = 2;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_EDIT_APP = 3;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_NEW_APP = 4;

    // Status for Stats
    /**
     * Attribute that represents . 
     */
    static public int STATUS_STATOPTIONS = 1;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_STATRESULT = 2;
    /**
     * Attribute that represents . 
     */
    static public int STATUS_STATTABULARRESULT = 3;

    /**
     * Attribute that represents . 
     */
    static public String KEYSTORE_TYPE_JKS = "JKS";
    /**
     * Attribute that represents . 
     */
    static public String KEYSTORE_TYPE_PKCS12 = "PKCS12";

    /**
     * Attribute that represents . 
     */
    static public String ID_REG_EXP = "^[a-zA-Z0-9\\-\\_]+$";

    /**
     * Attribute that represents . 
     */
    private int status = STATUS_NONE;

    /**
     * Attribute that represents . 
     */
    private static AfirmaConfigurationFacadeI confAfirma = null;

    /**
     * Attribute that represents . 
     */
    private static UserConfigurationFacadeI confUser = null;

    /**
     * Attribute that represents . 
     */
    private static KeystoreConfigurationFacadeI confKeystore = null;

    /**
     * Attribute that represents . 
     */
    private static AlarmConfigurationFacadeI confAlarm = null;

    /**
     * Attribute that represents . 
     */
    private static ApplicationConfigurationFacadeI confApplication = null;

    
    /**
     * Attribute that represents . 
     */
    private static AuditManagerI auditManager = null;

    /**
     * Attribute that represents . 
     */
    private static StatisticsManager statsManager = null;

    /**
     * Return the FacesContext instance for the request that is being processed by the current thread, if any.
     * @return FacesContext instance
     */
    public FacesContext getContext() {
	return FacesContext.getCurrentInstance();
    }

//    /**
//     * 
//     * @return
//     */
//    public static StatisticsManager getStatsManager() {
//	if (statsManager == null) {
//	    statsManager = StatisticsManager.getInstance();
//	}
//	return statsManager;
//    }
//
//    /**
//     * 
//     * @return
//     */
//    public static AuditManagerI getAuditManager() {
//	if (auditManager == null) {
//	    auditManager = AuditManager.getInstance();
//	}
//	return auditManager;
//    }
//
//    /**
//     * 
//     * @return
//     */
//    public static KeystoreConfigurationFacadeI getConfKeystore() {
//	if (confKeystore == null) {
//	    confKeystore = new KeystoreConfigurationFacade();
//	}
//	return confKeystore;
//    }
//
//    /**
//     * 
//     * @return
//     */
//    public static AlarmConfigurationFacadeI getConfAlarm() {
//	if (confAlarm == null) {
//	    confAlarm = new AlarmConfigurationFacade();
//	}
//	return confAlarm;
//    }
//
//    /**
//     * 
//     * @return
//     */
//    public static ApplicationConfigurationFacadeI getConfApplication() {
//	if (confApplication == null) {
//	    confApplication = new ApplicationConfigurationFacade();
//	}
//	return confApplication;
//    }

    /**
     * 
     * @return
     */
//    public static TemplateConfigurationFacadeI getConfTemplate() {
//	if (confTemplate == null) {
//	    confTemplate = new TemplateConfigurationFacade();
//	}
//	return confTemplate;
//    }

    /**
     * 
     * @return page status
     */
    public int getStatus() {
	return status;
    }

    /**
     * 
     * @param status page status
     */
    public void setStatus(int status) {
	this.status = status;
    }

//    /**
//     * 
//     * @return
//     */
//    public static AfirmaConfigurationFacadeI getConfAfirma() {
//	if (confAfirma == null) {
//	    confAfirma = new AfirmaConfigurationFacade();
//	}
//
//	return confAfirma;
//    }
//
//    /**
//     * 
//     * @return
//     */
//    public static UserConfigurationFacadeI getConfUser() {
//	if (confUser == null) {
//	    confUser = new UserConfigurationFacade();
//	}
//
//	return confUser;
//    }

    /**
     * Append a FacesMessage to the set of messages.
     * @param severity
     * @param summary
     * @param detail
     */
    public void addMessage(Severity severity, String summary, String detail) {
	FacesMessage message = new FacesMessage(severity, summary, detail);
	getContext().addMessage(null, message);
    }

    /**
     * 
     * @param res
     * @param content
     * @param theFilename
     */
    protected void writeOutContent(final HttpServletResponse res, final byte[ ] content, final String theFilename) {
	if (content == null) {
	    return;
	}
		writeOutContent(res, content, theFilename, "application/octet-stream");
    }

    
    /**
     * 
     * @param res
     * @param content
     * @param theFilename
     * @param mediaType 
     */
    protected void writeOutContent(final HttpServletResponse res, final byte[ ] content, final String theFilename, String mediaType) {
	if (content == null) {
	    return;
	}
	try {
	    res.setContentLength(content.length);
	    res.setDateHeader("Expires", 0);
	    res.setContentType(mediaType);
	    res.setHeader("Content-disposition", "attachment; filename=\"" + theFilename + "\"");
	    fastChannelCopy(Channels.newChannel(new ByteArrayInputStream(content)), Channels.newChannel(res.getOutputStream()));
	} catch (final IOException e) {
	    // produce a error message
	}
    }
    /**
     * 
     * @param src
     * @param dest
     * @throws IOException
     */
    private void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
	final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
	while (src.read(buffer) != -1) {
	    buffer.flip();
	    dest.write(buffer);
	    buffer.compact();
	}
	buffer.flip();
	while (buffer.hasRemaining()) {
	    dest.write(buffer);
	}
    }

    /**
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[ ] getBytesFromFile(File file) throws IOException {
	InputStream is = new FileInputStream(file);

	// Get the size of the file
	long length = file.length();

	// You cannot create an array using a long type.
	// It needs to be an int type.
	// Before converting to an int type, check
	// to ensure that file is not larger than Integer.MAX_VALUE.
	if (length > Integer.MAX_VALUE) {
	    throw new IOException(Language.getMessage(LanguageKeys.WMSG006));// File is
								     // too
								     // large
	}

	// Create the byte array to hold the data
	byte[ ] bytes = new byte[(int) length];

	// Read in the bytes
	int offset = 0;
	int numRead = 0;
	while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
	    offset += numRead;
	}

	// Ensure all the bytes have been read in
	if (offset < bytes.length) {
	    throw new IOException(Language.getMessage(LanguageKeys.WMSG007) + file.getName());
	}

	// Close the input stream and return bytes
	is.close();
	return bytes;
    }

}
