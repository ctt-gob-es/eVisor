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
 * <b>File:</b><p>es.gob.signaturereport.maudit.item.AuditService.java.</p>
 * <b>Description:</b><p>Class that represents a service included into audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>12/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 12/09/2011.
 */
package es.gob.signaturereport.maudit.item;


/** 
 * <p>Class that represents a service included into audit module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 12/09/2011.
 */
public class AuditService {

	/**
	 * Attribute that represents the service identifier. 
	 */
	private int serviceId = 0;
	
	/**
	 * Attribute that represents service description. 
	 */
	private String description = null;

	
	/**
	 * Gets the value of the service identifier.
	 * @return the value of the service identifier.
	 */
	public int getServiceId() {
		return serviceId;
	}

	
	/**
	 * Sets the value of the service identifier.
	 * @param id The value for the service identifier.
	 */
	public void setServiceId(int id) {
		this.serviceId = id;
	}

	
	/**
	 * Gets the value of the service description.
	 * @return the value of the service description.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the service description.
	 * @param dsc The value for the service description.
	 */
	public void setDescription(String dsc) {
		this.description = dsc;
	}


	/**
	 * Constructor method for the class AuditService.java.
	 * @param id		Service identifier.
	 * @param dsc 	Service description.
	 */
	public AuditService(int id, String dsc) {
		super();
		this.serviceId = id;
		this.description = dsc;
	}
}
