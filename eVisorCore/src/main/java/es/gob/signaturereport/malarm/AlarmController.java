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
 * <b>File:</b><p>es.gob.signaturereport.malarm.AlarmController.java.</p>
 * <b>Description:</b><p> Class that manages the mail sending about system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>06/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 06/06/2011.
 */
package es.gob.signaturereport.malarm;

import java.util.LinkedHashMap;

import es.gob.signaturereport.malarm.item.AlarmData;

/** 
 * <p>Class that manages the mail sending about system alarm.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 06/06/2011.
 */
public class AlarmController {

	/**
	 * Attribute that represents the threads used to communicate the alarms. 
	 */
	private LinkedHashMap<String, AlarmThread> alarmsThreads = new LinkedHashMap<String, AlarmThread>();


	/**
	 * Attribute that represents the number of milliseconds contained into a minute. 
	 */
	private static final int MINUTE_IN_MILLIS = 60000;
	
	/**
	 * Constructor method for the class AlarmController.java. 
	 */
	protected AlarmController() {
	}

	/**
	 * Communicates an error occurs in the system. 
	 * @param alarm	Alarm.
	 */
	public void communicateAlarm(AlarmData alarm) {
		AlarmThread thread = alarmsThreads.get(alarm.getAlarmId());
		long currentTime = System.currentTimeMillis();
		long nextComm = alarm.getLastCommunication().getTime();

		nextComm = nextComm + MINUTE_IN_MILLIS * alarm.getStandbyTime();

		if (thread == null || !thread.isAlive()) {
			thread = new AlarmThread();
			alarmsThreads.put(alarm.getAlarmId(), thread);
			if (currentTime < nextComm) {
				thread.sendAlarm(nextComm - currentTime, alarm);
			} else {
				thread.sendAlarm(0, alarm);
			}
		}
	}

}
