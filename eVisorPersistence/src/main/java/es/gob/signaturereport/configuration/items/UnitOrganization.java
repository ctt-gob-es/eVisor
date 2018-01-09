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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.UnitOrganization.java.</p>
 * <b>Description:</b><p> Class that represents an unit organization.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>23/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 23/05/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.util.LinkedHashMap;

/** 
 * <p>Class that represents an unit organization.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 23/05/2011.
 */
public class UnitOrganization {

	/**
	 * Attribute that represents the unit identifier. If the unit is root this is null. 
	 */
	private String unitId = null;

	/**
	 * Attribute that represents the unit name. If the unit is root this is null. 
	 */
	private String unitName = null;

	/**
	 * Attribute that represents the subunits registered in this unit. 
	 */
	private LinkedHashMap<String, UnitOrganization> subunits = new LinkedHashMap<String, UnitOrganization>();

	/**
	 * Attribute that represents the applications registered in this unit. The format is:<br/>
	 * Key: Application identifier, Value: Application Name 
	 */
	private LinkedHashMap<String, String> applications = new LinkedHashMap<String, String>();

	/**
	 * Gets the value of the unit identifier.
	 * @return the value of the unit identifier.
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * Sets the value of the unit identifier.
	 * @param id The value for the unit identifier.
	 */
	public void setUnitId(String id) {
		this.unitId = id;
	}

	/**
	 * Gets the value of the unit name.
	 * @return the value of the unit name.
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * Sets the value of the unit name.
	 * @param name The value for the unit name.
	 */
	public void setUnitName(String name) {
		this.unitName = name;
	}

	/**
	 * Gets the value of the subunits registered in this unit.
	 * @return the value of the subunits registered in this unit.
	 */
	public LinkedHashMap<String, UnitOrganization> getSubunits() {
		return subunits;
	}

	/**
	 * Sets the value of the subunits registered in this unit.
	 * @param subUnitList The value for the subunits registered in this unit.
	 */
	public void setSubunits(LinkedHashMap<String, UnitOrganization> subUnitList) {
		this.subunits = subUnitList;
	}

	/**
	 * Gets the value of the applications registered in this unit.
	 * @return the value of the applications registered in this unit. A map with the follow structure:<br/>
	 * Key: Application identifier Value: Application Name.
	 */
	public LinkedHashMap<String, String> getApplications() {
		return applications;
	}

	/**
	 * Sets the value of the applications registered in this unit.
	 * @param apps The value for the applications registered in this unit. A map with the follow structure:<br/>
	 * Key: Application identifier Value: Application Name.
	 */
	public void setApplications(LinkedHashMap<String, String> apps) {
		this.applications = apps;
	}

	/**
	 * Add an application. 
	 * @param applicationId		Application identifier.	
	 * @param applicationName	Application name.
	 */
	public void addApplication(String applicationId, String applicationName) {
		if (applicationId != null && applicationName != null) {
			this.applications.put(applicationId, applicationName);
		}
	}
	
	/**
	 * Add an unit organization to subunit list.
	 * @param id	Unit identifier.
	 * @param uo	Unit to add.
	 */
	public void addSubUnit(String id, UnitOrganization uo){
		if(id!=null && uo!=null){
			this.subunits.put(id, uo);
		}
	}

	/**
	 * Constructor method for the class UnitOrganization.java. 
	 */
	public UnitOrganization() {
	}

	/**
	 * Constructor method for the class UnitOrganization.java.
	 * @param id	The unit identifier. If the unit is root this is null. 
	 * @param name 	Unit name. If the unit is root this is null. 
	 */
	public UnitOrganization(String id, String name) {
		super();
		this.unitId = id;
		this.unitName = name;
	}
}
