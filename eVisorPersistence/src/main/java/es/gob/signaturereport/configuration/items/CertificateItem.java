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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.CertificateItem.java.</p>
 * <b>Description:</b><p> Class that represents a certificate.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/05/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.io.Serializable;
import java.util.Arrays;


/** 
 * <p>Class that represents a certificate.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/05/2011.
 */
public class CertificateItem implements Serializable{
	
	/**
	 * Constant that represents the 37 number. 
	 */
	private static final int XXXVII = 37;
	
	/**
	 * Constant that represents the 13 number. 
	 */
	private static final int XIII = 13;
	
	/**
	 * Constant that represents the 17 number. 
	 */
	private static final int XVII = 17;
	
	/**
	 * Constant that represents the 19 number. 
	 */
	private static final int XIX = 19;
	
	/**
	 * Constant that represents the 23 number. 
	 */
	private static final int XXIII = 23;
	
	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -2315759679107958776L;

	/**
	 * Attribute that represents the certificate alias. 
	 */
	private String alias = null;
	/**
	 * Attribute that represents the certificate encoded. 
	 */
	private byte[] content = null;

	/**
	 * Attribute that represents the private key associated to certificate. 
	 */
	private byte[] privateKey = null;
	

	/**
	 * Attribute that represents the key algorithm. 
	 */
	private String keyAlgorithm = null;
	
	/**
	 * Gets the value of the certificate encoded.
	 * @return the value of the certificate encoded.
	 */
	public byte[ ] getContent() {
		return content;
	}

	
	/**
	 * Sets the value of the certificate encoded.
	 * @param certContent The value for the certificate encoded.
	 */
	public void setContent(byte[ ] certContent) {
		if(certContent!=null){
			this.content = Arrays.copyOf(certContent,certContent.length);
		}
		
	}


	
	/**
	 * Gets the value of the private key associated to certificate. 
	 * @return the value of the private key associated to certificate. 
	 */
	public byte[ ] getPrivateKey() {
		return privateKey;
	}


	
	/**
	 * Sets the value of the private key associated to certificate. 
	 * @param privKey The value for the private key associated to certificate. 
	 */
	public void setPrivateKey(byte[ ] privKey) {
		if(privKey!=null){
			this.privateKey = Arrays.copyOf(privKey, privKey.length);
		}
	}


	/**
	 * Constructor method for the class CertificateItem.java.
	 * @param certAlias		Certificate alias.
	 * @param certContent CertificateItem encoded.
	 */
	public CertificateItem(String certAlias,byte[ ] certContent) {
		super();
		this.alias = certAlias;
		if(certContent!=null){
			this.content = Arrays.copyOf(certContent,certContent.length);
		}
	}



	
	/**
	 * Gets the value of the key algorithm.
	 * @return the value of the key algorithm.
	 */
	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}


	
	/**
	 * Sets the value of the key algorithm.
	 * @param keyAlg The value for the key algorithm.
	 */
	public void setKeyAlgorithm(String keyAlg) {
		this.keyAlgorithm = keyAlg;
	}


	
	/**
	 * Gets the value of the certificate alias.
	 * @return the value of the certificate alias.
	 */
	public String getAlias() {
		return alias;
	}


	
	/**
	 * Sets the value of the certificate alias.
	 * @param certAlias The value for the certificate alias.
	 */
	public void setAlias(String certAlias) {
		this.alias = certAlias;
	}

	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	public CertificateItem clone()throws CloneNotSupportedException{
		CertificateItem certItem = new CertificateItem(this.alias, this.content);
		certItem.setKeyAlgorithm(this.keyAlgorithm);
		certItem.setPrivateKey(this.privateKey);
		return certItem;
		
	}


	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int code = XXXVII;
		if(this.alias != null){
			code = code + XIII * this.alias.hashCode();
		}
		if(this.keyAlgorithm != null ){
			code = code + XVII * this.keyAlgorithm.hashCode();
		}
		if(this.content != null){
			code = code + XIX * this.content.hashCode();
		}
		if(this.privateKey != null){
			code = code + XXIII * this.privateKey.hashCode();
		}
		return code;
	}


	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CertificateItem){
			CertificateItem certItem = (CertificateItem) obj;
			return this.hashCode() == certItem.hashCode();
		}else{
			return false;
		}
	}
	
	
}
