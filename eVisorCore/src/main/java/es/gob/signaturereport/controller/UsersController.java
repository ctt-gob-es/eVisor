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
 * <b>File:</b><p>es.gob.signaturereport.controller.UsersController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.component.UIScrollableDataTable;

import es.gob.signaturereport.configuration.access.UserConfiguration;
import es.gob.signaturereport.configuration.access.UserConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.tools.UtilsBase64;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 13/10/2011.
 */
@Named("users")
@SessionScoped
public class UsersController extends AbstractController {

    /**
     * Attribute that represents . 
     */
    User userEdit;
    /**
     * Attribute that represents . 
     */
    Person personEdit;

    /**
     * Attribute that represents . 
     */
    String oldHashPassword;

    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection userSelected;
    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection personSelected;
    /**
     * Attribute that represents . 
     */
    UIScrollableDataTable personTable;

    /**
     * Attribute that represents . 
     */
    private int statusPerson = STATUS_NONE;
    
    @Inject @UserConfiguration
    private UserConfigurationFacadeI userconf;

    /**
     * 
     * @return
     */
    public String getOldHashPassword() {
	return oldHashPassword;
    }

    /**
     * 
     * @param oldHashPassword
     */
    public void setOldHashPassword(String oldHashPassword) {
	this.oldHashPassword = oldHashPassword;
    }

    /**
     * 
     * @return
     */
    public UIScrollableDataTable getPersonTable() {
	return personTable;
    }

    /**
     * 
     * @param personTable
     */
    public void setPersonTable(UIScrollableDataTable personTable) {
	this.personTable = personTable;
    }

    /**
     * 
     * @return
     */
    public int getStatusPerson() {
	return statusPerson;
    }

    /**
     * 
     * @param statusPerson
     */
    public void setStatusPerson(int statusPerson) {
	this.statusPerson = statusPerson;
    }

    /**
     * 
     * @return
     */
    public Person getPersonEdit() {
	return personEdit;
    }

    /**
     * 
     * @param personEdit
     */
    public void setPersonEdit(Person personEdit) {
	this.personEdit = personEdit;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getUserSelected() {
	return userSelected;
    }

    /**
     * 
     * @param userSelected
     */
    public void setUserSelected(org.richfaces.model.selection.Selection userSelected) {
	this.userSelected = userSelected;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getPersonSelected() {
	return personSelected;
    }

    /**
     * 
     * @param personSelected
     */
    public void setPersonSelected(org.richfaces.model.selection.Selection personSelected) {
	this.personSelected = personSelected;
    }

    /**
     * 
     * @return
     */
    public User getUserEdit() {
	return userEdit;
    }

    /**
     * 
     * @param userEdit
     */
    public void setUserEdit(User userEdit) {
	this.userEdit = userEdit;
    }

    /**
     * 
     * @return
     */
    public User[ ] getUsersList() {
	try {
	    return userconf.getAllUsers();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG108), e.getMessage());
	}
	return null;
    }

    /**
     * 
     * @return
     */
    public Person[ ] getPersonsList() {
	try {
	    return userconf.getAllPersons();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG109), e.getMessage());
	}
	return null;
    }

    /**
     * 
     * @return
     */
    public String onStartNewUser() {
	userEdit = new User("", "");
	setStatus(STATUS_NEW);
	return null;
    }

    /**
     * 
     * @return
     */
    public String onNewUserAcept() {
	try {

	    if (userEdit.getPerson() == null) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG110), Language.getMessage(LanguageKeys.WMSG111));
		return null;
	    }

	    MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    byte[ ] hash = digest.digest(userEdit.getPassword().getBytes());

	    UtilsBase64 b64 = new UtilsBase64();

	    oldHashPassword = b64.encodeBytes(hash);

	    userconf.createUserManager(userEdit.getLogin(), oldHashPassword, userEdit.getPerson().getPersonId());
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG112), Language.getMessage(LanguageKeys.WMSG113) + userEdit.getLogin());
	    setStatus(STATUS_EDIT);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG114), e.getDescription());
	    return null;
	} catch (NoSuchAlgorithmException e1) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG114), e1.getMessage());
	    return null;
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptModifyPassword() {
	try {

	    if (userEdit.getPassword().trim().equals("")) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG115), Language.getMessage(LanguageKeys.WMSG116));
		return null;
	    }

	    MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    byte[ ] hash = digest.digest(userEdit.getPassword().getBytes());

	    UtilsBase64 b64 = new UtilsBase64();

	    oldHashPassword = b64.encodeBytes(hash);

	    if (userEdit.getPerson() == null) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG115), Language.getMessage(LanguageKeys.WMSG111));
		return null;
	    }

	    userconf.modifyUserManager(userEdit.getLogin(), oldHashPassword, userEdit.getPerson().getPersonId(), userEdit.isLocked());
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG117), Language.getMessage(LanguageKeys.WMSG118));
	    setStatus(STATUS_EDIT);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG119), e.getDescription());
	    return null;
	} catch (NoSuchAlgorithmException e1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG119), e1.getMessage());
	    return null;
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public String onModifyUserAcept() {
	try {

	    if (userEdit.getPerson() == null) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG115), Language.getMessage(LanguageKeys.WMSG111));
		return null;
	    }

	    userconf.modifyUserManager(userEdit.getLogin(), oldHashPassword, userEdit.getPerson().getPersonId(), userEdit.isLocked());
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG117), Language.getMessage(LanguageKeys.WMSG120) + userEdit.getLogin());
	    setStatus(STATUS_EDIT);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG115), e.getDescription());
	    return null;
	}

	return null;
    }

    public String onStartSelectPerson() {
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onClickUsersList() {

	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String login = request.getParameter(PARAM1);

	User[ ] uList = getUsersList();
	boolean end = false;
	int i = 0;

	while ((i < uList.length) && (!end)) {

	    if (uList[i].getLogin().equals(login)) {

		userEdit = uList[i];
		oldHashPassword = userEdit.getPassword();
		setStatus(STATUS_EDIT);
		end = true;
	    }
	    i++;
	}
	return null;

    }

    /**
     * 
     * @return
     */
    public String onStartNewPerson() {
	personEdit = new Person();
	statusPerson = STATUS_NEW;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onStartModifyPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG121), Language.getMessage(LanguageKeys.WMSG122));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG121), Language.getMessage(LanguageKeys.WMSG122));
	    return null;
	}

	Long personId = new Long(0);
	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		personId = ((Person) personTable.getRowData()).getPersonId();
	    }
	}

	Person[ ] pList = getPersonsList();
	boolean end = false;
	int i = 0;
	Person pd = null;

	while ((i < pList.length) && (!end)) {

	    if (pList[i].getPersonId().intValue() == personId.intValue()) {

		pd = pList[i];
		end = true;
	    }
	    i++;
	}
	personEdit = pd;

	statusPerson = STATUS_EDIT;

	return null;
    }

    /**
     * 
     * @return
     */
    public String onAssignPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG123), Language.getMessage(LanguageKeys.WMSG122));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG123), Language.getMessage(LanguageKeys.WMSG122));
	    return null;
	}

	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		userEdit.setPerson((Person) personTable.getRowData());
	    }
	}
	return null;
    }
    
    /**
     * 
     * @return
     */
    public String onDelPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG163), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG163), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		try {
		    userconf.deleteUser(((Person) personTable.getRowData()).getPersonId());
		    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG164), "");
		} catch (ConfigurationException e) {
		    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG165), e.getDescription());
		}
		
	    }
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptModifyPersonAndAssign() {

	// TODO Validaciones y conversiones
	try {
	    userconf.modifyUser(personEdit.getPersonId(), personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG124), e.getDescription());
	    return null;
	}
	userEdit.setPerson(personEdit);
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptModifyPerson() {
	// TODO Validaciones y conversiones
	try {
	    userconf.modifyUser(personEdit.getPersonId(), personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG124), e.getDescription());
	    return null;
	}

	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptNewPerson() {
	// TODO Validaciones y conversiones

	try {
	    userconf.addUser(personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG125), e.getDescription());
	    return null;
	}
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptNewPersonAndAssign() {
	// TODO Validaciones y conversiones
	try {
		personEdit.setPersonId(userconf.addUser(personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail()));
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG125), e.getDescription());
	    return null;
	}
	userEdit.setPerson(personEdit);
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onCancelActionPerson() {
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onDeleteUser() {
	try {
		userconf.deleteUserManager(userEdit.getLogin());
		statusPerson = STATUS_NONE;
		setStatus(STATUS_NONE);
		return null;
	} catch (ConfigurationException e) {
		addMessage(FacesMessage.SEVERITY_ERROR, "No se pudo dar de baja el usuario.", e.getDescription());
		return null;
	}

	}

}
