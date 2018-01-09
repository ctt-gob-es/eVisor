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
 * <b>File:</b><p>es.gob.signaturereport.controller.authentication.UserManager.java.</p>
 * <b>Description:</b><p>Class that manages the authenticated user.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller.authentication;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import es.gob.signaturereport.configuration.access.UserConfiguration;
import es.gob.signaturereport.configuration.access.UserConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.User;
import es.gob.signaturereport.controller.ActionsWeb;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.tools.UtilsBase64;

/** 
 * <p>Class that manages the authenticated user.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("userManager")
@SessionScoped
public class UserManager implements Serializable {

    /**
     * Attribute that represents the version identifier for a Serializable class. 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Attribute that represents the user session key. 
     */
    public static final String USER_SESSION_KEY = "user_signature_report";
    
    @Inject @UserConfiguration
    private UserConfigurationFacadeI userconf;

    /**
     * Attribute that represents the user name. 
     */
    private String username;

    /**
     * Attribute that represents the user password. 
     */
    private String password;

    // Descripcion del problema de validacion
    /**
     * Attribute that represents the validation message. 
     */
    private String validateMsg = "";

    /**
     * Sets the value of the validation message.
     * @return validation message
     */
    public String getValidateMsg() {
	return validateMsg;
    }

    /**
     * Sets the value of validation message.
     * @param validateMsgin validation message
     */
    public void setValidateMsg(String validateMsgin) {
	this.validateMsg = validateMsgin;
    }

    /**
     * Constructor method for the class UserManager.java. 
     */
    public UserManager() {
	super();
    }

    /**
     * Gets the value of the user name. 
     * @return user name
     */
    public String getUsername() {
	return username;
    }

    /**
     * Sets the value of user name.
     * @param usernamein user name
     */
    public void setUsername(String usernamein) {
	this.username = usernamein;
    }

    /**
     * Gets the value of user password. 
     * @return user password
     */
    public String getPassword() {
	return password;
    }

    /**
     * Sets the value of user password. 
     * @param passwordin user password
     */
    public void setPassword(String passwordin) {
	this.password = passwordin;
    }

    /**
     * Sets the value of password hash.
     * @param passwordin user password
     * @return password hash
     * @throws NoSuchAlgorithmException if SHA-1 not supported
     */
    public String getHash(String passwordin) throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance("SHA-1");
	byte[ ] hash = digest.digest(passwordin.getBytes());

	UtilsBase64 b64 = new UtilsBase64();

	return b64.encodeBytes(hash);
    }

	/**
	 * Validate the actual user name and password.
	 * @return JSF action
	 * @throws NoSuchAlgorithmException 
	 * @throws ConfigurationException 
	 */
	public String validateUser() throws NoSuchAlgorithmException, ConfigurationException {

		// UserConfigurationFacadeI userconf = new UserConfigurationFacade();
		FacesContext context = FacesContext.getCurrentInstance();

		// Comprobar que se han introducido datos
		if (username.trim().equals("")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG001), Language.getMessage(LanguageKeys.WMSG002));
			context.addMessage(null, message);
			return null;
		}
		if (password.trim().equals("")) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG001), Language.getMessage(LanguageKeys.WMSG003));
			context.addMessage(null, message);
			return null;
		}

		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[ ] hash = digest.digest(password.trim().getBytes());
		UtilsBase64 coder = new UtilsBase64();
		String hashString = coder.encodeBytes(hash);

		User user = userconf.getUserManager(username.trim(), hashString);

		if (user != null) {

			if (user.isLocked()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG001), Language.getMessage(LanguageKeys.WMSG004));
				context.addMessage(null, message);
				return null;
			} else {

				context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
				return ActionsWeb.START;
			}
		} else {

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG001), Language.getMessage(LanguageKeys.WMSG005));
			context.addMessage(null, message);
			return null;

		}
	}

    /**
     * Log out.
     * @return JSF login action
     */
    public String logout() {
	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	if (session != null) {
	    session.invalidate();
	}
	return ActionsWeb.LOGIN;

    }

    /**
     * 
     * @return 
     */
    public boolean getValidateMsgExist() {
	if (validateMsg == null) {
	    return false;
	}

	return validateMsg.trim().equals("");

    }

    /**
     * 
     * @return
     */
    public boolean isloginOk() {
	FacesContext context = FacesContext.getCurrentInstance();
	Object userObject = context.getExternalContext().getSessionMap().get(USER_SESSION_KEY);

	if (userObject != null) {
	    return (userObject instanceof User);
	} else {
	    return false;
	}
    }

    /**
     * Gets the value of user description. 
     * @return User description
     */
    public String getNameLogin() {
	FacesContext context = FacesContext.getCurrentInstance();
	Object userObject = context.getExternalContext().getSessionMap().get(USER_SESSION_KEY);

	if (userObject != null) {
	    if (userObject instanceof User) {
		User user = (User) userObject;
		return user.getLogin();
	    }
	}
	return null;
    }

}
