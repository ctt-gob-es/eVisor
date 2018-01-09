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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.Alarm.java.</p>
 * <b>Description:</b><p> Class that represents a system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>18/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 18/05/2011.
 */
package es.gob.signaturereport.configuration.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/** 
 * <p>Class that represents a system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 18/05/2011.
 */
public class Alarm implements Serializable {

	/**
	 * Attribute that represents the class version. 
	 */
	private static final long serialVersionUID = -6579884105797156873L;
	
	/**
	 * Attribute that represents the alarm identifier. 
	 */
	private AlarmIdentifier identifier = null;
	
	/**
	 * Attribute that represents list of receivers. 
	 */
	private List<String> receivers = new ArrayList<String>();
	
	/**
	 * Attribute that represents the period from which communicates an alarm until it can re-send. 
	 */
	private long standbyPeriod = 0;
	
	/**
	 * Attribute that represents if the alarm is locked. 
	 */
	private boolean lock  = false;

	/**
	 * Constructor method for the class Alarm.java.
	 * @param id Identifier of alarm.
	 */
	public Alarm(AlarmIdentifier id) {
		super();
		this.identifier = id;
	}

	
	/**
	 * Gets the value of the alarm identifier.
	 * @return the value of the alarm identifier.
	 */
	public AlarmIdentifier getIdentifier() {
		return identifier;
	}

	
	/**
	 * Sets the value of the alarm identifier.
	 * @param id The value for the alarm identifier.
	 */
	public void setIdentifier(AlarmIdentifier id) {
		this.identifier = id;
	}

	
	/**
	 * Gets the value of the alarm receivers.
	 * @return the value of the alarm receivers.
	 */
	public List<String> getReceivers() {
		return receivers;
	}

	
	/**
	 * Sets the value of the alarm receivers.
	 * @param recs The value for the alarm receivers.
	 */
	public void setReceivers(List<String> recs) {
		this.receivers = recs;
	}

	
	/**
	 * Gets the value of the attribute 'lock'.
	 * @return the value of the attribute 'lock'.
	 */
	public boolean isLock() {
		return lock;
	}

	
	/**
	 * Sets the value of the attribute 'lock'.
	 * @param islock The value for the attribute 'lock'.
	 */
	public void setLock(boolean islock) {
		this.lock = islock;
	}


	
	/**
	 * Gets the value of the attribute 'standbyPeriod'.
	 * @return the value of the attribute 'standbyPeriod'.
	 */
	public long getStandbyPeriod() {
		return standbyPeriod;
	}


	
	/**
	 * Sets the value of the attribute 'standbyPeriod'.
	 * @param period The value for the attribute 'standbyPeriod'.
	 */
	public void setStandbyPeriod(long period) {
		this.standbyPeriod = period;
	}
	

}
