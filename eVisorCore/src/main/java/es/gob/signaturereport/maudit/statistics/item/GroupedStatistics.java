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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.item.GroupedStatistics.java.</p>
 * <b>Description:</b><p>Class that contains the result of calculating the statistics by grouping..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>21/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 21/09/2011.
 */
package es.gob.signaturereport.maudit.statistics.item;

import java.util.LinkedHashMap;
import java.util.Map;


/** 
 * <p>Class that contains the result of calculating the statistics by grouping.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 21/09/2011.
 */
public class GroupedStatistics {

	/**
	 * Attribute that represents the field used to calculate the statistics by grouping. 
	 */
	private int field = 0;
	
	/**
	 * Attribute that represents the result. 
	 */
	private Map<String,Long> values = new LinkedHashMap<String, Long>();

	/**
	 * Constructor method for the class GroupedStatistics.java. 
	 */
	public GroupedStatistics() {
	}

	
	/**
	 * Gets the value of the field used to calculate the statistics by grouping.
	 * @return the value of the field used to calculate the statistics by grouping.
	 */
	public int getField() {
		return field;
	}

	
	/**
	 * Sets the value of the field used to calculate the statistics by grouping.
	 * @param groupedField The value for the field used to calculate the statistics by grouping.
	 */
	public void setField(int groupedField) {
		this.field = groupedField;
	}

	
	/**
	 * Gets the value of the grouped result.
	 * @return the value of the grouped result.
	 */
	public Map<String, Long> getValues() {
		return values;
	}

	
	/**
	 * Sets the value of the grouped result.
	 * @param result The value for the grouped result.
	 */
	public void setValues(Map<String, Long> result) {
		this.values = result;
	}
}
