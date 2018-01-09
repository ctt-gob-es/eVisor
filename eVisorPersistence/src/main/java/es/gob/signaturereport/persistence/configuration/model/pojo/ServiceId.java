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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.db.model.ServiceId.java.</p>
 * <b>Description:</b><p>This class represents the primary key of the SERVICE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.model.pojo;

import java.io.Serializable;

/** 
 * <p>This class represents the primary key of the SERVICE table.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/04/2011.
 */
public class ServiceId implements Serializable {

	/**
	 * Attribute that represents the 17 number. 
	 */
	private static final int XVII = 17;
	
	/**
	 * Attribute that represents the 37 number. 
	 */
	private static final int XXXVII = 37;
	/**
	 * Attribute that represents the serial version of the class. 
	 */
	private static final long serialVersionUID = -6404495975258636350L;

	/**
	 * Attribute that represents the SERVICE_ID column. 
	 */
	private String serviceId;
	
	/**
	 * Attribute that represents the association to PLATFORM table. 
	 */
	private Long platform;

	/**
	 * Constructor method for the class ServiceId.java. 
	 */
	public ServiceId() {
	}

	/**
	 * Constructor method for the class ServiceId.java.
	 * @param serviceIdentifier	Service identifier.
	 * @param servicePlatform 	Platform that publishes the service.
	 */
	public ServiceId(String serviceIdentifier, Long servicePlatform) {
		this.serviceId = serviceIdentifier;
		this.platform = servicePlatform;
	}

	/**
	 * Gets the service identifier.
	 * @return	Service identifier.
	 */
	public String getServiceId() {
		return this.serviceId;
	}

	/**
	 * Sets the service identifier.
	 * @param serviceIdentifier	Service identifier.
	 */
	public void setServiceId(String serviceIdentifier) {
		this.serviceId = serviceIdentifier;
	}

	/**
	 * Gets the platform that publishes the service.
	 * @return	Platform that publishes the service.
	 */
	public Long getPlatform() {
		return this.platform;
	}

	/**
	 * Sets the platform that publishes the service.
	 * @param servicePlatform	Platform that publishes the service.
	 */
	public void setPlatform(Long servicePlatform) {
		this.platform = servicePlatform;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if ((other == null) || (!(other instanceof ServiceId))){
			return false;
		}	
		ServiceId castOther = (ServiceId) other;
		if((castOther.getServiceId() == null && this.getServiceId()!=null) ||(castOther.getServiceId() != null && this.getServiceId()==null) 
				||(castOther.getServiceId() != null && this.getServiceId()!=null && !castOther.getServiceId().equals(this.getServiceId())) ){
			return false;
		}
		
		if((castOther.getPlatform() == null && this.getPlatform()!=null) ||(castOther.getPlatform() != null && this.getPlatform()==null)){
			return false;
		}else if(castOther.getPlatform() != null && this.getPlatform()!=null){
			PlatformPOJO p1 = new PlatformPOJO();
			p1.setPfPk(castOther.getPlatform());
			PlatformPOJO p2 = new PlatformPOJO();
			p2.setPfPk(this.getPlatform());
			if((p1.getPfPk()!=null && p2.getPfPk()==null)||(p1.getPfPk()==null && p2.getPfPk()!=null)||(p1.getPfPk()!=null && p2.getPfPk()!=null && !p1.getPfPk().equals(p2) )){
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = XVII;

		result = XXXVII * result + (getServiceId() == null ? 0 : this.getServiceId().hashCode());
		result = XXXVII * result + (getPlatform() == null ? 0 : this.getPlatform().intValue());
		return result;
	}

}
