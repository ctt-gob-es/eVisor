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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.User.java.</p>
 * <b>Description:</b><p> Class that contains information about a user's system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>13/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 13/04/2011.
 */
package es.gob.signaturereport.configuration.items;

/** 
 * <p>Class that contains information about a user's system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 13/04/2011.
 */
public class User {

    /**
     * Attribute that represents the user login. 
     */
    private String login = null;

    /**
     * Attribute that represents the user password. 
     */
    private String password = null;
    /**
     * Attribute that indicates if the user is blocked.
     */
    private boolean isLocked = false;
    
    /**
     * Attribute that represents the personal data of user. 
     */
    private Person person = null;
    

    /**
     * Constructor method for the class User.java.
     * @param userLogin		The user login.
     * @param userPassword 	The user password.
     */
    public User(String userLogin, String userPassword) {
	super();
	this.login = userLogin;
	this.password = userPassword;
    }

    /**
     * Gets the value of the user login.
     * @return the value of the user login.
     */
    public String getLogin() {
	return login;
    }

    /**
     * Sets the value of the user login.
     * @param userLogin The value for the user login.
     */
    public void setLogin(String userLogin) {
	this.login = userLogin;
    }

    /**
     * Gets the value of the user password.
     * @return the value of the user password.
     */
    public String getPassword() {
	return password;
    }

    /**
     * Sets the value of the user password.
     * @param userPassword The value for the user password.
     */
    public void setPassword(String userPassword) {
	this.password = userPassword;
    }

    /**
     * Informs if the user is blocked.
     * @return true if the user is blocked, otherwise false.
     */
    public boolean isLocked() {
	return isLocked;
    }

    /**
     * Sets the status of user to locked or unlocked.
     * @param isUserLocked True if the user is blocked, otherwise false.
     */
    public void setLocked(boolean isUserLocked) {
	this.isLocked = isUserLocked;
    }

	
	/**
	 * Gets the value of the the personal data of the user.
	 * @return the value of the the personal data of the user.
	 */
	public Person getPerson() {
		return person;
	}

	
	/**
	 * Sets the value of the the personal data of the user.
	 * @param personalData The value for the the personal data of the user.
	 */
	public void setPerson(Person personalData) {
		this.person = personalData;
	}



}
