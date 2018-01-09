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
 * <b>File:</b><p>es.gob.signaturereport.malarm.mail.SMTPMailServer.java.</p>
 * <b>Description:</b><p>Class that contains the information associated to SMTP email server.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm.mail;


/** 
 * <p>Class that contains the information associated to SMTP email server.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
public class SMTPMailServer {
	
	/**
	 * Attribute that represents the server host. 
	 */
	private String host = null;
	
	/**
	 * Attribute that represents the server port. 
	 */
	private String port = null;
	
	/**
	 * Attribute that represents the user used to authentication. 
	 */
	private String user = null;
	
	/**
	 * Attribute that represents the password used to authentication. 
	 */
	private String password = null;

	/**
	 * Constructor method for the class SMTPMailServer.java. 
	 */
	public SMTPMailServer() {
	}

	/**
	 * Constructor method for the class SMTPMailServer.java.
	 * @param serverHost	Server host.
	 * @param serverPort 	Server port.
	 */
	public SMTPMailServer(String serverHost, String serverPort) {
		super();
		this.host = serverHost;
		this.port = serverPort;
	}

	/**
	 * Constructor method for the class SMTPMailServer.java.
	 * @param serverHost	Server host.
	 * @param serverPort	Server port.
	 * @param serverUser	Authentication user.
	 * @param serverPassword 	Authentication password.
	 */
	public SMTPMailServer(String serverHost, String serverPort, String serverUser, String serverPassword) {
		super();
		this.host = serverHost;
		this.port = serverPort;
		this.user = serverUser;
		this.password = serverPassword;
	}

	
	/**
	 * Gets the value of the server host.
	 * @return the value of the server host.
	 */
	public String getHost() {
		return host;
	}

	
	/**
	 * Sets the value of the server host.
	 * @param serverHost The value for the server host.
	 */
	public void setHost(String serverHost) {
		this.host = serverHost;
	}

	
	/**
	 * Gets the value of the server port.
	 * @return the value of the server port.
	 */
	public String getPort() {
		return port;
	}

	
	/**
	 * Sets the value of the server port.
	 * @param serverPort The value for the server port.
	 */
	public void setPort(String serverPort) {
		this.port = serverPort;
	}

	
	/**
	 * Gets the value of the authentication user.
	 * @return the value of the authentication user.
	 */
	public String getUser() {
		return user;
	}

	
	/**
	 * Sets the value of the authentication user.
	 * @param serverUser The value for the authentication user.
	 */
	public void setUser(String serverUser) {
		this.user = serverUser;
	}

	
	/**
	 * Gets the value of the authentication password.
	 * @return the value of the authentication password.
	 */
	public String getPassword() {
		return password;
	}

	
	/**
	 * Sets the value of the authentication password.
	 * @param serverPassword The value for the authentication password.
	 */
	public void setPassword(String serverPassword) {
		this.password = serverPassword;
	}
	
}
