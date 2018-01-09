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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.Person.java.</p>
 * <b>Description:</b><p>Class that contains information about a person registered in the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.io.Serializable;
import java.math.BigDecimal;


/** 
 * <p>Class that contains information about a person registered in the system.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/04/2011.
 */
public class Person implements Serializable{

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -1069005154582152029L;

	/**
	 * Attribute that represents the person identifier. 
	 */
	private Long personId = null;
	
	/**
	 * Attribute that represents the person name. 
	 */
	private String name = null;
	
	/**
	 * Attribute that represents the person surname. 
	 */
	private String surname = null;
	
	/**
	 * Attribute that represents the person phone number. 
	 */
	private BigDecimal phone = null;
	
	/**
	 * Attribute that represents the person email address. 
	 */
	private String email = null;

	/**
	 * Constructor method for the class Person.java. 
	 */
	public Person() {
	}

	
	/**
	 * Gets the value of the person name.
	 * @return the value of the person name.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Sets the value of the person name.
	 * @param pname The value for the person name.
	 */
	public void setName(String pname) {
		this.name = pname;
	}

	
	/**
	 * Gets the value of the person surname.
	 * @return the value of the person surname.
	 */
	public String getSurname() {
		return surname;
	}

	
	/**
	 * Sets the value of the person surname.
	 * @param psurname The value for the person surname.
	 */
	public void setSurname(String psurname) {
		this.surname = psurname;
	}

	
	/**
	 * Gets the value of the person phone number.
	 * @return the value of the person phone number.
	 */
	public BigDecimal getPhone() {
		return phone;
	}

	
	/**
	 * Sets the value of the person phone number.
	 * @param phoneNumber The value for the person phone number.
	 */
	public void setPhone(BigDecimal phoneNumber) {
		this.phone = phoneNumber;
	}

	
	/**
	 * Gets the value of the person email address.
	 * @return the value of the person email address.
	 */
	public String getEmail() {
		return email;
	}

	
	/**
	 * Sets the value of the person email address.
	 * @param emailAddres The value for the person email address.
	 */
	public void setEmail(String emailAddres) {
		this.email = emailAddres;
	}


	
	/**
	 * Gets the value of the person identifier.
	 * @return the value of the person identifier.
	 */
	public Long getPersonId() {
		return personId;
	}


	
	/**
	 * Sets the value of the person identifier.
	 * @param pId The value for the person identifier.
	 */
	public void setPersonId(Long pId) {
		this.personId = pId;
	}
	
	
}
