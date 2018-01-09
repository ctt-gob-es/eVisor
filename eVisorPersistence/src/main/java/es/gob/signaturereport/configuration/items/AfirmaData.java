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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.AfirmaData.java.</p>
 * <b>Description:</b><p>Class that contains all information necessary for invocation of a "@firma platform".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.io.Serializable;
import java.util.LinkedHashMap;

import es.gob.signaturereport.persistence.utils.Constants;

/** 
 * <p>Class that contains all information necessary for invocation of a "@firma platform".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/02/2011.
 */
public class AfirmaData implements Serializable{

    /**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -8699932634780290993L;

	/**
     * Attribute that represents the version of "@firma". 
     */
    private String version = null;

    /**
     * Attribute that represents the application for invoking the "@firma". 
     */
    private String applicationId = null;

    /**
     * Attribute that indicates the authorization type of the web service request. 
     */
    private int authenticationType = Constants.WITHOUT_AUTHENTICATION;

    /**
     * Attribute that represents the authorization user or certificate of the web service request. 
     */
    private String authenticationUser = null;

    /**
     * Attribute that represents the authorization password of the web service request.
     */
    private String authenticationPassword = null;

    /**
     * Attribute that represents the alias of certificate used to sign the SOAP request makes by '@firma'. 
     */
    private String soapTrusted = null;
    
    /**
     * Web services that can be invoked for this platform. 
     */
    private LinkedHashMap<String, WSServiceData> services = new LinkedHashMap<String, WSServiceData>();

    /**
     * Gets the version of "@firma".
     * @return the value of the version of "@firma".
     */
    public String getVersion() {
	return version;
    }

    /**
     * Sets the value of the version of "@firma".
     * @param afirmaVersion The value for the version of "@firma".
     */
    public void setVersion(String afirmaVersion) {
	this.version = afirmaVersion;
    }

    /**
     * Gets the value of the application for invoking the "@firma".
     * @return the value of the application for invoking the "@firma".
     */
    public String getApplicationId() {
	return applicationId;
    }

    /**
     * Sets the value of the application for invoking the "@firma".
     * @param appId The value for the application for invoking the "@firma".
     */
    public void setApplicationId(String appId) {
	this.applicationId = appId;
    }

    /**
     * Gets the value of the authorization type of the web service request.
     * @return the value of the authorization type of the web service request.
     */
    public int getAuthenticationType() {
	return authenticationType;
    }

    /**
     * Sets the value of the authorization type of the web service request.
     * @param type The value for the authorization type of the web service request.
     */
    public void setAuthenticationType(int type) {
	this.authenticationType = type;
    }

    /**
     * Gets the value of the authorization user or certificate of the web service request.
     * @return the value of the authorization user or certificate of the web service request.
     */
    public String getAuthenticationUser() {
	return authenticationUser;
    }

    /**
     * Sets the value of the authorization user or certificate of the web service request.
     * @param user The value for the authorization user or certificate of the web service request.
     */
    public void setAuthenticationUser(String user) {
	this.authenticationUser = user;
    }

    /**
     * Gets the value of the authorization password of the web service request.
     * @return the value of the authorization password of the web service request.
     */
    public String getAuthenticationPassword() {
	return authenticationPassword;
    }

    /**
     * Sets the value of the authorization password of the web service request.
     * @param password The value for the authorization password of the web service request.
     */
    public void setAuthenticationPassword(String password) {
	this.authenticationPassword = password;
    }

    /**
     * Gets the value of the alias of certificate used to sign the SOAP request.
     * @return the value of the alias of certificate used to sign the SOAP request.
     */
    public String getSoapTrusted() {
	return soapTrusted;
    }

    /**
     * Sets the value of the alias of certificate used to sign the SOAP request.
     * @param aliasCert The value for the alias of certificate used to sign the SOAP request.
     */
    public void setSoapTrusted(String aliasCert) {
	this.soapTrusted = aliasCert;
    }

    
    /**
     * Gets the value of the web services that can be invoked for this platform.
     * @return the value of the attribute 'services'.
     */
    public LinkedHashMap<String, WSServiceData> getServices() {
        return services;
    }

    
    /**
     * Sets the value of the attribute 'services'.
     * @param servicesList The value for the attribute 'services'.
     */
    public void setServices(LinkedHashMap<String, WSServiceData> servicesList) {
        this.services = servicesList;
    }
    
}
