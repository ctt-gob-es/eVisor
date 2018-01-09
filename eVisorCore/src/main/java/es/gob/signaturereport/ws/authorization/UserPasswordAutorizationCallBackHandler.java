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
 * <b>File:</b><p>es.gob.signaturereport.ws.authorization.UserPasswordAutorizationCallBackHandler.java.</p>
 * <b>Description:</b><p>Class that represents a handler for checking the user information and determines whether the user is valid
 * for certain application of the platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/06/2011.
 */
package es.gob.signaturereport.ws.authorization;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;


/**
 * <p>Class that represents a handler for checking the user information and determines whether the user is valid
 * for certain application of the platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/06/2011.
 */
public class UserPasswordAutorizationCallBackHandler implements CallbackHandler{


	/**
	 * Attribute that represents the user associated to application.
	 */
	private String user = null;

	/**
	 * Attribute that represents the password associated to application user.
	 */
	private String password = null;

	/**
	 * Attribute that represents if the user and password are valid.
	 */
	private boolean valid = false;
	/**
	 * {@inheritDoc}
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	public void handle(Callback[ ] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc;
		for (int i = 0; (i < callbacks.length && !this.valid); i++) {
			pc = (WSPasswordCallback) callbacks[i];
			if (pc instanceof WSPasswordCallback) {
				checkUserPassword(pc);
			} 
		}

	}


	/**
	 * Constructor method for the class UserPasswordAutorizationCallBackHandler.java.
	 * @param user		User.
	 * @param password 	Password.
	 */
	public UserPasswordAutorizationCallBackHandler(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	/**
	 * Gets if the user and password are valid.
	 * @return True if the user and password are valid. Otherwise false.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Checks the user and password included into SOAP request.
	 * @param pc 	{@link WSPasswordCallback} Class to provide a password 'callback' mechanism.
	 */
	private void checkUserPassword(WSPasswordCallback pc){
			if(pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN){
				//Si no es correcto lanza una excepci�n al procesar la cabecera de seguridad
				pc.setPassword(this.password);
				this.valid = pc.getIdentifer().equals(this.user);
			}else if(pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN_UNKNOWN && pc.getPasswordType().equals(WSConstants.PASSWORD_TEXT)){
				this.valid = pc.getPassword().equals(this.password) && pc.getIdentifer().equals(this.user);
			}
		
	}
}


