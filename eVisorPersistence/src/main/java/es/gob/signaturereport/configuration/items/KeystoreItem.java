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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.KeystoreItem.java.</p>
 * <b>Description:</b><p> Class that represents a keystore.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/05/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;


/** 
 * <p>Class that represents a keystore.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/05/2011.
 */
public class KeystoreItem implements Serializable{
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -7147805639735837217L;

	/**
	 * Attribute that represents the keystore name. 
	 */
	private String name = null;
	
	/**
	 * Attribute that represents the version number. 
	 */
	private int version = 0;
	
	/**
	 * Attribute that represents the certificate list included into keystore. 
	 */
	private LinkedHashMap<String, CertificateItem> certificates = new LinkedHashMap<String, CertificateItem>();

	/**
	 * Constructor method for the class KeystoreItem.java.
	 * @param ksName		KeystoreItem name.
	 * @param ksVersion 	Version number.
	 */
	public KeystoreItem(String ksName, int ksVersion) {
		super();
		this.name = ksName;
		this.version = ksVersion;
	}

	
	/**
	 * Gets the value of the keystore name.
	 * @return the value of the keystore name.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the value of the keystore name.
	 * @param ksName The value for the keystore name.
	 */
	public void setName(String ksName) {
		this.name = ksName;
	}

	
	/**
	 * Gets the value of the version number.
	 * @return the value of the version number.
	 */
	public int getVersion() {
		return version;
	}

	
	/**
	 * Sets the value of the version number.
	 * @param ksVersion The value for the version number.
	 */
	public void setVersion(int ksVersion) {
		this.version = ksVersion;
	}

	
	/**
	 * Gets the value of the certificate list included into keystore.
	 * @return the value of the certificate list included into keystore.
	 */
	public LinkedHashMap<String, CertificateItem> getCertificates() {
		return certificates;
	}

	
	/**
	 * Sets the value of the certificate list included into keystore.
	 * @param certificateList The value for the certificate list included into keystore.
	 */
	public void setCertificates(LinkedHashMap<String, CertificateItem> certificateList) {
		this.certificates = certificateList;
	}


	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	public KeystoreItem clone() throws CloneNotSupportedException{
		KeystoreItem clone = new KeystoreItem(this.name, this.version);
		if(this.certificates !=null){
			Iterator<String> keys = this.certificates.keySet().iterator();
			while(keys.hasNext()){
				String key = (String) keys.next();
				CertificateItem cert = this.certificates.get(key);
				clone.getCertificates().put(key, cert.clone());
			} 
		}
		return clone;
	}	
	
}
