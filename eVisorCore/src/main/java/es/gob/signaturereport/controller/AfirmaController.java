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
 * <b>File:</b><p>es.gob.signaturereport.controller.AfirmaController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import es.gob.signaturereport.configuration.access.AfirmaConfiguration;
import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.access.KeystoreConfiguration;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.configuration.items.CertificateItem;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.configuration.items.WSServiceData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("afirma")
@SessionScoped
public class AfirmaController extends AbstractController {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8714482616127914904L;

	/**
     * Attribute that represents selected row in the platforms table. 
     */
    private org.richfaces.model.selection.Selection afirmaSelected;

    /**
     * Attribute that represents the selected or new platform. 
     */
    private AfirmaData afirmaEdit;
    /**
     * Attribute that represents the selected validation service data. 
     */
    private WSServiceData valserEdit;
    /**
     * Attribute that represents the selected platform ID. 
     */
    private String idAfirmaEdit;
    
    /**
     * Attribute that represents . 
     */
    @Inject @AfirmaConfiguration
    protected AfirmaConfigurationFacadeI confAfirma;
    
    /**
     * Attribute that represents . 
     */
    @Inject @KeystoreConfiguration
    protected KeystoreConfigurationFacadeI keystoreConf;

    /**
     * 
     * @return selected or new platform ID
     */
    public String getIdAfirmaEdit() {
	return idAfirmaEdit;
    }

    /**
     * 
     * @param idAfirmaEditIn selected or new platform
     */
    public void setIdAfirmaEdit(String idAfirmaEditIn) {
	this.idAfirmaEdit = idAfirmaEditIn;
    }

    /**
     * 
     * @return selected or new platform
     */
    public AfirmaData getAfirmaEdit() {
	if (afirmaEdit == null) {
	    afirmaEdit = new AfirmaData();
	}

	return afirmaEdit;
    }

    /**
     * 
     * @param afirmaEditIn selected or new platform
     */
    public void setAfirmaEdit(AfirmaData afirmaEditIn) {
	if (afirmaEditIn == null) {
	    this.afirmaEdit = new AfirmaData();
	} else {
	    this.afirmaEdit = afirmaEditIn;
	}
    }

    /**
     * 
     * @return List of existing platforms
     */
    public String[ ] getAfirmaList() {
	try {
	    return confAfirma.getAfirmaIds();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG008), e.getMessage());
	    return null;
	}

    }

    /**
     * 
     * @return List of certificates
     */
    public List<SelectItem> getCertsSOAP() {
	List<SelectItem> resultList = new ArrayList<SelectItem>();
	SelectItem item = new SelectItem("Ninguno", "Ninguno");
	resultList.add(item);

	KeystoreItem keystore;
	try {
	    keystore = keystoreConf.getKeystore("CONFIANZA SOAP");
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG009), e.getMessage());
	    return null;
	}
	LinkedHashMap<String, CertificateItem> certificatesId = keystore.getCertificates();

	Collection<String> c = certificatesId.keySet();

	// obtain an Iterator for Collection
	Iterator<String> itr = c.iterator();
	while (itr.hasNext()) {
	    String key = (String) itr.next();
	    item = new SelectItem(key, key);
	    resultList.add(item);
	}

	return resultList;
    }

    /**
     * 
     * @return List of certificates
     */
    public List<SelectItem> getCertsWS() {
	List<SelectItem> resultList = new ArrayList<SelectItem>();

	KeystoreItem keystore;
	try {
	    keystore = keystoreConf.getKeystore("KEYSTORE SOAP");
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG010), e.getMessage());
	    return null;
	}
	LinkedHashMap<String, CertificateItem> certificatesId = keystore.getCertificates();

	Collection<String> c = certificatesId.keySet();

	// obtain an Iterator for Collection
	Iterator<String> itr = c.iterator();
	while (itr.hasNext()) {
	    String key = (String) itr.next();
	    SelectItem item = new SelectItem(key, key);
	    resultList.add(item);
	}

	return resultList;
    }

    /**
     * 
     * @return selected row in platforms table
     */
    public org.richfaces.model.selection.Selection getAfirmaSelected() {
	return afirmaSelected;
    }

    /**
     * 
     * @param afirmaSelectedIn selected row in the platforms table
     */
    public void setAfirmaSelected(org.richfaces.model.selection.Selection afirmaSelectedIn) {
	this.afirmaSelected = afirmaSelectedIn;
    }

    /**
     * 
     * @param event action event
     */
    public void onClickList(ActionEvent event) {

	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	this.idAfirmaEdit = request.getParameter(PARAM1);

	try {
	    afirmaEdit = confAfirma.getAfirmaConfiguration(idAfirmaEdit);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG011), e.getMessage());
	}

	if (afirmaEdit.getServices() != null && !afirmaEdit.getServices().isEmpty()) {
	 
	    valserEdit = afirmaEdit.getServices().get(AfirmaConfigurationFacadeI.VALIDATE_SIGNATURE_SERVICE);
	}

	setStatus(STATUS_EDIT);

    }

    /**
     * 
     * @return selected or new validation service data
     */
    public WSServiceData getValserEdit() {
	if (valserEdit == null) {
	    valserEdit = new WSServiceData();
	}
	return valserEdit;
    }

    /**
     * 
     * @param valserEditIn selected or new validation service data
     */
    public void setValserEdit(WSServiceData valserEditIn) {
	if (valserEditIn == null) {
	    this.valserEdit = new WSServiceData();
	} else {
	    this.valserEdit = valserEditIn;
	}
    }

    /**
     * 
     * @return jsf action
     */
    public String onNewAfirma() {
	afirmaEdit = new AfirmaData();
	valserEdit = new WSServiceData();
	idAfirmaEdit = null;

	setStatus(STATUS_NEW);

	return null;
    }

    /**
     * 
     * @return jsf action
     */
    public String onNewAfirmaAcept() {

	
	afirmaEdit.getServices().put(AfirmaConfigurationFacadeI.VALIDATE_SIGNATURE_SERVICE, valserEdit);

	if (idAfirmaEdit.trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG013));

	    return null;
	}

	if (afirmaEdit.getApplicationId().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG014));

	    return null;
	}

	if ((afirmaEdit.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) && (afirmaEdit.getAuthenticationUser().trim().equals(""))) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG015));

	    return null;
	}

	if ((afirmaEdit.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) && (afirmaEdit.getAuthenticationPassword().trim().equals(""))) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG016));

	    return null;
	}

	// TODO Que ocurre si no hay certificados en el keystore??

	// Certificate
	if (afirmaEdit.getSoapTrusted().equals("Ninguno")) {
	    afirmaEdit.setSoapTrusted(null);
	}

	if (valserEdit.getServicesLocation().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG017));
	    return null;
	}

	if (valserEdit.getOperationName().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), Language.getMessage(LanguageKeys.WMSG018));
	    return null;
	}

	try {
	    confAfirma.createAfirmaConfiguration(idAfirmaEdit, afirmaEdit);
	    setStatus(STATUS_EDIT);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG019),Language.getMessage(LanguageKeys.WMSG020));

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG012), e.getMessage());

	    return null;
	}

	return null;
    }

    /**
     * 
     * @return jsf action
     */
    public String onModify() {

	if (afirmaEdit.getApplicationId().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), Language.getMessage(LanguageKeys.WMSG014));

	    return null;
	}

	if ((afirmaEdit.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) && (afirmaEdit.getAuthenticationUser().trim().equals(""))) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), Language.getMessage(LanguageKeys.WMSG015));

	    return null;
	}

	if ((afirmaEdit.getAuthenticationType() == ConfigurationFacade.USER_PASS_AUTHENTICATION) && (afirmaEdit.getAuthenticationPassword().trim().equals(""))) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), Language.getMessage(LanguageKeys.WMSG016));

	    return null;
	}

	// TODO Que ocurre si no hay certificados en el keystore??

	// Certificate
	if (afirmaEdit.getSoapTrusted().equals("Ninguno")) {
	    afirmaEdit.setSoapTrusted(null);
	}

	if (valserEdit.getServicesLocation().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), Language.getMessage(LanguageKeys.WMSG017));
	    return null;
	}

	if (valserEdit.getOperationName().trim().equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), Language.getMessage(LanguageKeys.WMSG018));
	    return null;
	}

	try {
	    confAfirma.modifyAfirmaConfiguration(idAfirmaEdit, afirmaEdit);
	    setStatus(STATUS_EDIT);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG022), Language.getMessage(LanguageKeys.WMSG023));
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG021), e.getMessage());
	    return null;
	}

	return null;
    }

    /**
     * 
     * @return jsf action
     */
    public String onDelete() {

	afirmaEdit.getServices().put(AfirmaConfigurationFacadeI.VALIDATE_SIGNATURE_SERVICE, valserEdit);

	try {
	    confAfirma.removeAfirmaConfiguration(idAfirmaEdit);
	    setStatus(STATUS_NONE);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG024), Language.getMessage(LanguageKeys.WMSG025));

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG026), e.getMessage());
	    return null;
	}

	return null;
    }

}
