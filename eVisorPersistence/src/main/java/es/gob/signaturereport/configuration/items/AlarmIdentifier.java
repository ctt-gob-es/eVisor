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
 * <b>File:</b><p>es.gob.signaturereport.malarm.item.AlarmIdentifier.java.</p>
 * <b>Description:</b><p> Class that represents an alarm identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/09/2011.
 */
package es.gob.signaturereport.configuration.items;


/** 
 * <p>Class that represents an alarm identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/09/2011.
 */
public class AlarmIdentifier {
	
	/**
	 * Attribute that represents the alarm identifier. 
	 */
	private String alarmId = null;
	
	/**
	 * Attribute that represents the description of the alarm. 
	 */
	private String description = null;

	/**
	 * Constructor method for the class AlarmIdentifier.java.
	 * @param id The alarm identifier.
	 */
	public AlarmIdentifier(String id) {
		super();
		this.alarmId = id;
	}

	/**
	 * Constructor method for the class AlarmIdentifier.java.
	 * @param id		The alarm identifier.
	 * @param des 	Alarm description.
	 */
	public AlarmIdentifier(String id, String des) {
		super();
		this.alarmId = id;
		this.description = des;
	}

	
	/**
	 * Gets the value of the alarm identifier.
	 * @return the value of the alarm identifier.
	 */
	public String getAlarmId() {
		return alarmId;
	}

	
	/**
	 * Sets the value of the alarm identifier.
	 * @param id The value for the alarm identifier.
	 */
	public void setAlarmId(String id) {
		this.alarmId = id;
	}

	
	/**
	 * Gets the value of the alarm description.
	 * @return the value of the alarm description.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Sets the value of the alarm description.
	 * @param alarmDescription The value for the alarm description.
	 */
	public void setDescription(String alarmDescription) {
		this.description = alarmDescription;
	}
}
