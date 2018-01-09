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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.ApplicationData.java.</p>
 * <b>Description:</b><p>Class that contains the configuration information of a application.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>03/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 03/02/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.util.ArrayList;

import es.gob.signaturereport.persistence.utils.Constants;


/** 
 * <p>Class that contains the configuration information of a application.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 03/02/2011.
 */
public class ApplicationData {
    
	/**
	 * Attribute that represents the name of application. 
	 */
	private String name = null; 
    /**
     * Attribute that represents the template identifier list. 
     */
    private ArrayList<String> templates = new ArrayList<String>();
      
    /**
     * Attribute that represents the "@firma platform" used by application. 
     */
    private String platformId = null;
    
    /**
     * Attribute that represents the person that manages the application. 
     */
    private Person manager = null;
    
    /**
     * Attribute that represents the authentication type used to authorize soap request. 
     */
    private int authenticationType = Constants.WITHOUT_AUTHENTICATION;
    
    /**
     * Attribute that represents the user used to authorize soap request.  
     */
    private String authUser = null;
    
    /**
     * Attribute that represents the password used to authorize soap request. 
     */
    private String authPass = null;
    
    /**
     * Attribute that represents the alias certificate used to authorize soap request.  
     */
    private String authCertAlias = null;
    
    
    /**
     * Gets the value of the alias certificate used to authorize soap request.
     * @return the value of the alias certificate used to authorize soap request.
     */
    public String getAuthCertAlias() {
        return authCertAlias;
    }

    
    /**
     * Sets the value of the alias certificate used to authorize soap request.
     * @param alias The value for the alias certificate used to authorize soap request.
     */
    public void setAuthCertAlias(String alias) {
        this.authCertAlias = alias;
    }

    /**
     * Sets the value of the template identifier list.
     * @param templatesList The value for the template identifier list.
     */
    public void setTemplates(ArrayList<String> templatesList) {
	this.templates = templatesList;
    }

    /**
     * Gets the value of the template identifier list.
     * @return the value of the template identifier list.
     */
    public ArrayList<String> getTemplates() {
	return templates;
    }


    
    /**
     * Gets the value of the "@firma platform" used by application.
     * @return the value of the "@firma platform" used by application.
     */
    public String getPlatformId() {
        return platformId;
    }

    
    /**
     * Sets the value of the "@firma platform" used by application.
     * @param plaformIdentifier The value for the "@firma platform" used by application.
     */
    public void setPlatformId(String plaformIdentifier) {
        this.platformId = plaformIdentifier;
    }

	
	/**
	 * Gets the value of the name of application.
	 * @return the value of the name of application.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the value of the name of application.
	 * @param appName The value for the name of application.
	 */
	public void setName(String appName) {
		this.name = appName;
	}

	
	/**
	 * Gets the value of the person that manages the application.
	 * @return the value of the person that manages the application.
	 */
	public Person getManager() {
		return manager;
	}

	
	/**
	 * Sets the value of the person that manages the application.
	 * @param appManager The value for the person that manages the application.
	 */
	public void setManager(Person appManager) {
		this.manager = appManager;
	}

	
	/**
	 * Gets the value of the authentication type used to authorize soap request.
	 * @return the value of the authentication type used to authorize soap request.
	 */
	public int getAuthenticationType() {
		return authenticationType;
	}

	
	/**
	 * Sets the value of the authentication type used to authorize soap request.
	 * @param authType The value for the authentication type used to authorize soap request.
	 */
	public void setAuthenticationType(int authType) {
		this.authenticationType = authType;
	}

	
	/**
	 * Gets the value of the user or alias certificate used to authorize soap request.
	 * @return the value of the user or alias certificate used to authorize soap request.
	 */
	public String getAuthUser() {
		return authUser;
	}

	
	/**
	 * Sets the value of the user or alias certificate used to authorize soap request.
	 * @param wsUser The value for the user or alias certificate used to authorize soap request.
	 */
	public void setAuthUser(String wsUser) {
		this.authUser = wsUser;
	}

	
	/**
	 * Gets the value of the password used to authorize soap request.
	 * @return the value of the password used to authorize soap request.
	 */
	public String getAuthPass() {
		return authPass;
	}

	
	/**
	 * Sets the value of the password used to authorize soap request.
	 * @param wsPassword The value for the password used to authorize soap request.
	 */
	public void setAuthPass(String wsPassword) {
		this.authPass = wsPassword;
	}

	


}
