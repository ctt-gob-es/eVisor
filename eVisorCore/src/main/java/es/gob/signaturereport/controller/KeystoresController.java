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
 * <b>File:</b><p>es.gob.signaturereport.controller.KeystoreController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.component.UIScrollableDataTable;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import es.gob.signaturereport.configuration.access.KeystoreConfiguration;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.CertificateItem;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.controller.list.KeystoreElement;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 10/10/2011.
 */
@Named("keystores")
@SessionScoped
public class KeystoresController extends AbstractController {

    /**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = 6420190254853374118L;

	/** The keystore edit. */
    private KeystoreItem keystoreEdit;

    /** The cert edit. */
    private X509Certificate certEdit;

    /**
     * Attribute that represents the current keystore. 
     */
    private String currentKeystore;

    /** The current alias. */
    private String currentAlias;

    /** The old alias. */
    private String oldAlias;

    /** The ik_keystore file name. */
    private String ik_keystoreFileName;
    /**
     * Attribute that represents . 
     */
    private String ik_keystore_type = KEYSTORE_TYPE_PKCS12;
    /**
     * Attribute that represents . 
     */
    private KeyStore ik_keystore = null;
    /**
     * Attribute that represents . 
     */
    private org.richfaces.model.selection.Selection ik_keystoreSelection;
    /**
     * Attribute that represents . 
     */
    private UIScrollableDataTable ik_table;

    /**
     * Attribute that represents . 
     */
    File ik_keystoreFile;
    
    /**
     * Attribute that represents . 
     */
    @Inject @KeystoreConfiguration
    protected KeystoreConfigurationFacadeI keystoreConf;

    /**
    * Gets the ik_keystore_type.
    *
    * @return the ik_keystore_type
    */
    public String getIk_keystore_type() {
	return ik_keystore_type;
    }

    /**
     * Sets the ik_keystore_type.
     *
     * @param ik_keystore_type the new ik_keystore_type
     */
    public void setIk_keystore_type(String ik_keystore_type) {
	this.ik_keystore_type = ik_keystore_type;
    }

    /**
     * Attribute that represents . 
     */
    String ik_keystorePassword;

    /**
     * Attribute that represents . 
     */
    List<KeystoreElement> ik_keystoreContent = new ArrayList<KeystoreElement>();

    /**
     * Gets the ik_keystore file.
     *
     * @return the ik_keystore file
     */
    public File getIk_keystoreFile() {
	return ik_keystoreFile;
    }

    /**
     * Sets the ik_keystore file.
     *
     * @param ik_keystoreFile the new ik_keystore file
     */
    public void setIk_keystoreFile(File ik_keystoreFile) {
	this.ik_keystoreFile = ik_keystoreFile;
    }

    /**
     * On start import key.
     *
     * @return the string
     */
    public String onStartImportKey() {
	ik_keystoreContent = null;
	ik_keystoreFileName = null;
	ik_keystoreFile = null;
	ik_keystore = null;
	ik_keystorePassword = null;
	return null;
    }

    /**
     * On upload key complete.
     *
     * @param event the event
     */
    public void onUploadKeyComplete(UploadEvent event) {
	UploadItem item = event.getUploadItem();
	ik_keystoreFile = item.getFile();
	ik_keystoreFileName = item.getFileName();
    }

    /**
     * On next import key.
     *
     * @return the string
     */
    public String onNextImportKey() {
	try {
	    ik_keystore = KeyStore.getInstance(ik_keystore_type);

	    if (ik_keystoreFile == null) {
		addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), Language.getMessage(LanguageKeys.WMSG073));
		return null;
	    }

	    ik_keystore.load(new FileInputStream(ik_keystoreFile), ik_keystorePassword.toCharArray());

	    Enumeration<String> aliases = ik_keystore.aliases();
	    ik_keystoreContent = new ArrayList<KeystoreElement>();

	    while (aliases.hasMoreElements()) {
		String alias = aliases.nextElement();

		// java.security.cert.Certificate cert =
		// keystoreFile.getCertificate(alias);

		KeystoreElement ke = new KeystoreElement(alias, KeystoreElement.TYPE_CERT);
		ik_keystoreContent.add(ke);

		if (ik_keystore.getKey(alias, ik_keystorePassword.toCharArray()) != null) {
		    ke = new KeystoreElement(alias, KeystoreElement.TYPE_KEY);
		    ik_keystoreContent.add(ke);
		}

	    }
	} catch (UnrecoverableKeyException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	} catch (KeyStoreException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	} catch (NoSuchAlgorithmException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	} catch (CertificateException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	} catch (FileNotFoundException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	} catch (IOException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	}

	return null;

    }

    /**
     * Change add.
     *
     * @param event the event
     */
    public void changeAdd(ValueChangeEvent event) {

	boolean add = (Boolean) event.getNewValue();

	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String alias = request.getParameter(PARAM1);
	int type = Integer.parseInt(request.getParameter(PARAM2));

	int i = 0;

	for (i = 0; i < ik_keystoreContent.size(); i++) {
	    KeystoreElement ke = ik_keystoreContent.get(i);

	    if (ke.getAlias().equals(alias) && (ke.getType() == type)) {
		ke.setAdd(add);
		ik_keystoreContent.remove(i);
		ik_keystoreContent.add(i, ke);
		i = ik_keystoreContent.size();
	    }
	}

    }

    /**
     * On accept import key.
     *
     * @return the string
     */
    public String onAceptImportKey() {

	List<KeystoreElement> selectedKeys = new ArrayList<KeystoreElement>();

	Iterator<Object> iterator = getIk_keystoreSelection().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    ik_table.setRowKey(key);
	    if (ik_table.isRowAvailable()) {
		selectedKeys.add((KeystoreElement) ik_table.getRowData());
	    }
	}

	Iterator<KeystoreElement> it = selectedKeys.iterator();
	LinkedHashMap<String, X509Certificate> certMap = new LinkedHashMap<String, X509Certificate>();
	LinkedHashMap<String,PrivateKey> keyMap = new LinkedHashMap<String, PrivateKey>();
	  
	while (it.hasNext()) {

	    KeystoreElement ke = (KeystoreElement) it.next();
	     try {
    		if (ke.getType() == KeystoreElement.TYPE_CERT) {
    			certMap.put(ke.getAlias(), (X509Certificate) ik_keystore.getCertificate(ke.getAlias()));
    			} else {
    			keyMap.put(ke.getAlias(), (PrivateKey) ik_keystore.getKey(ke.getAlias(), ik_keystorePassword.toCharArray()));
    		   }
		
	    } catch (KeyStoreException e) {
	    	addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG074), e.getMessage());
	    } catch (UnrecoverableKeyException e) {
	    	addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG074), e.getMessage());
	    } catch (NoSuchAlgorithmException e) {
	    	addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG074), e.getMessage());
	    }
	}
	try {
		Iterator<Entry<String, X509Certificate>> certEntries = certMap.entrySet().iterator();
    	while(certEntries.hasNext()){
    		Entry<String, X509Certificate> certEntry  = certEntries.next();
    		String alias = certEntry.getKey();
    		keystoreConf.addCertificate(currentKeystore, alias, certEntry.getValue(), keyMap.get(alias));
    	}
    	keystoreEdit = keystoreConf.getKeystore(currentKeystore);
	 }  catch (ConfigurationException e) {
	   	addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG074), e.getMessage());
	 }
	 return ActionsWeb.ADMIN_KEYSTORES;
    }

    /**
     * Gets the ik_keystore selection.
     *
     * @return the ik_keystore selection
     */
    public org.richfaces.model.selection.Selection getIk_keystoreSelection() {
	return ik_keystoreSelection;
    }

    /**
     * Sets the ik_keystore selection.
     *
     * @param ik_keystoreSelection the new ik_keystore selection
     */
    public void setIk_keystoreSelection(org.richfaces.model.selection.Selection ik_keystoreSelection) {
	this.ik_keystoreSelection = ik_keystoreSelection;
    }

    /**
     * Gets the ik_table.
     *
     * @return the ik_table
     */
    public UIScrollableDataTable getIk_table() {
	return ik_table;
    }

    /**
     * Sets the ik_table.
     *
     * @param ik_table the new ik_table
     */
    public void setIk_table(UIScrollableDataTable ik_table) {
	this.ik_table = ik_table;
    }

    /**
     * Gets the keystore edit.
     *
     * @return the keystore edit
     */
    public KeystoreItem getKeystoreEdit() {
	return keystoreEdit;
    }

    /**
     * Sets the keystore edit.
     *
     * @param keystoreEdit the new keystore edit
     */
    public void setKeystoreEdit(KeystoreItem keystoreEdit) {
	this.keystoreEdit = keystoreEdit;
    }

    /**
     * Gets the old alias.
     *
     * @return the old alias
     */
    public String getOldAlias() {
	return oldAlias;
    }

    /**
     * Sets the old alias.
     *
     * @param oldAlias the new old alias
     */
    public void setOldAlias(String oldAlias) {
	this.oldAlias = oldAlias;
    }

    /**
     * Gets the ik_keystore file name.
     *
     * @return the ik_keystore file name
     */
    public String getIk_keystoreFileName() {
	return ik_keystoreFileName;
    }

    /**
     * Sets the ik_keystore file name.
     *
     * @param ik_keystoreFileName the new ik_keystore file name
     */
    public void setIk_keystoreFileName(String ik_keystoreFileName) {
	this.ik_keystoreFileName = ik_keystoreFileName;
    }

    /**
     * Gets the ik_keystore password.
     *
     * @return the ik_keystore password
     */
    public String getIk_keystorePassword() {
	return ik_keystorePassword;
    }

    /**
     * Sets the ik_keystore password.
     *
     * @param ik_keystorePassword the new ik_keystore password
     */
    public void setIk_keystorePassword(String ik_keystorePassword) {
	this.ik_keystorePassword = ik_keystorePassword;
    }

    /**
     * Gets the ik_keystore content.
     *
     * @return the ik_keystore content
     */
    public List<KeystoreElement> getIk_keystoreContent() {
	return ik_keystoreContent;
    }

    /**
     * Sets the ik_keystore content.
     *
     * @param ik_keystoreContent the new ik_keystore content
     */
    public void setIk_keystoreContent(List<KeystoreElement> ik_keystoreContent) {
	this.ik_keystoreContent = ik_keystoreContent;
    }

    /**
     * Gets the current keystore.
     *
     * @return the current keystore
     */
    public String getCurrentKeystore() {
	return currentKeystore;
    }

    /**
     * Sets the current keystore.
     *
     * @param currentKeystore the new current keystore
     */
    public void setCurrentKeystore(String currentKeystore) {
	this.currentKeystore = currentKeystore;
    }

    /**
     * Gets the current alias.
     *
     * @return the current alias
     */
    public String getCurrentAlias() {
	return currentAlias;
    }

    /**
     * Sets the current alias.
     *
     * @param currentAlias the new current alias
     */
    public void setCurrentAlias(String currentAlias) {
	this.currentAlias = currentAlias;
    }

    /**
     * Gets the cert edit.
     *
     * @return the cert edit
     */
    public X509Certificate getCertEdit() {
	return certEdit;

    }

    /**
     * Sets the cert edit.
     *
     * @param certEdit the new cert edit
     */
    public void setCertEdit(X509Certificate certEdit) {
	this.certEdit = certEdit;

    }

    /**
     * On view keystore.
     *
     * @return the string
     */
    public String onViewKeystore() {

	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String keystoreId = request.getParameter(PARAM1);

	try {
	    keystoreEdit = keystoreConf.getKeystore(keystoreId);
	    
	    setStatus(STATUS_EDIT);
	    currentKeystore = keystoreId;
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG072), e.getMessage());
	}

	return null;
    }

    /**
     * Gets the list keystore.
     *
     * @return the list keystore
     */
    public List<KeystoreElement> getListKeystore() {

	List<KeystoreElement> l = new ArrayList<KeystoreElement>();

	LinkedHashMap<String, CertificateItem> certificatesId = keystoreEdit.getCertificates();

	Collection<String> c = certificatesId.keySet();

	// obtain an Iterator for Collection
	Iterator<String> itr = c.iterator();

	// iterate through LinkedHashMap values iterator
	while (itr.hasNext()) {
	    String key = (String) itr.next();
	    CertificateItem cer = certificatesId.get(key);

	    if (cer.getContent() != null) {
		l.add(new KeystoreElement(cer.getAlias(), KeystoreElement.TYPE_CERT));
	    }
	    if (cer.getPrivateKey() != null) {
		l.add(new KeystoreElement(cer.getAlias(), KeystoreElement.TYPE_KEY));
	    }

	}

	return l;
    }

    /**
     * On select certificate.
     *
     * @param event the event
     */
    public void onSelectCertificate(ActionEvent event) {

	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String alias = request.getParameter(PARAM1);

	try {
	    certEdit = keystoreConf.getCertificate(currentKeystore, alias);
	    currentAlias = alias;
	    oldAlias = alias;
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG075), e.getMessage());
	}

    }

    /**
     * On upload cert complete.
     *
     * @param event the event
     */
    public void onUploadCertComplete(UploadEvent event) {

	UploadItem item = event.getUploadItem();

	try {
	    File file = item.getFile();
	    CertificateFactory cf = CertificateFactory.getInstance("X.509");
	    X509Certificate cert = (X509Certificate) cf.generateCertificate(new FileInputStream(file));

	    keystoreConf.addCertificate(getCurrentKeystore(), item.getFileName(), cert, null);
	    keystoreEdit = keystoreConf.getKeystore(currentKeystore);

	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG076), Language.getMessage(LanguageKeys.WMSG077) + currentAlias);

	} catch (CertificateException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG078), e.getMessage());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG078), e.getMessage());
	} catch (FileNotFoundException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG078), e.getMessage());
	}

    }

    /**
     * On modify alias.
     *
     * @return the string
     */
    public String onModifyAlias() {
	try {
	    keystoreConf.modifyAlias(currentKeystore, oldAlias, currentAlias);
	    keystoreEdit = keystoreConf.getKeystore(currentKeystore);
	    oldAlias = currentAlias;

	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG079), Language.getMessage(LanguageKeys.WMSG080) + currentAlias);
	    return ActionsWeb.ADMIN_KEYSTORES;
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG081), e.getMessage());
	}

	return ActionsWeb.ADMIN_KEYSTORES;
    }

    /**
     * On delete cert.
     *
     * @return the string
     */
    public String onDeleteCert() {
	try {
	    keystoreConf.removeCertificate(currentKeystore, oldAlias);
	    keystoreEdit = keystoreConf.getKeystore(currentKeystore);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG082), Language.getMessage(LanguageKeys.WMSG083) + oldAlias);
	    return ActionsWeb.ADMIN_KEYSTORES;
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG084), e.getMessage());
	}

	return ActionsWeb.ADMIN_KEYSTORES;
    }

}
