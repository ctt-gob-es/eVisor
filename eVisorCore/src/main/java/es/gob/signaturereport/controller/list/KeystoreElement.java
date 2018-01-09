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
 * <b>File:</b><p>es.gob.signaturereport.controller.list.KeystoreElement.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller.list;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class KeystoreElement {
	
	/**
	 * Attribute that represents . 
	 */
	public static int TYPE_CERT = 0;
	/**
	 * Attribute that represents . 
	 */
	public static int TYPE_KEY = 1;
	
	/**
	 * Attribute that represents . 
	 */
	String alias;
	
	/**
	 * Attribute that represents . 
	 */
	int type = 0;
	
	/**
	 * Attribute that represents . 
	 */
	boolean add;
	
	/**
	 * 
	 * @return
	 */
	public boolean isAdd() {
	    return add;
	}

	
	/**
	 * 
	 * @param addin
	 */
	public void setAdd(boolean addin) {
	    this.add = addin;
	}

	/**
	 * Constructor method for the class KeystoreElement.java.
	 * @param aliasin
	 * @param type in
	 */
	public KeystoreElement(String aliasin, int typein) {
	    super();
	    this.alias = aliasin;
	    this.type = typein;
	    this.add = true;
	}

	/**
	 * 
	 * @return
	 */
	public String getAlias() {
	    return alias;
	}
	
	/**
	 * 
	 * @param aliasin
	 */
	public void setAlias(String aliasin) {
	    this.alias = aliasin;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getType() {
	    return type;
	}
	
	/**
	 * 
	 * @param typein
	 */
	public void setType(int typein) {
	    this.type = typein;
	}

}
