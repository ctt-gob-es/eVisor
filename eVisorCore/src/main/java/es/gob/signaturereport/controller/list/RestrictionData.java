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
 * <b>File:</b><p>es.gob.signaturereport.controller.list.RestrictionData.java.</p>
 * <b>Description:</b><p>Class that represents a filter for adding to a statistics query.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>16/12/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 16/12/2011.
 */
package es.gob.signaturereport.controller.list;

import es.gob.signaturereport.maudit.statistics.item.Restriction;
import es.gob.signaturereport.maudit.statistics.item.RestrictionI;


/** 
 * <p>Class that represents a filter for adding to a statistics query.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 16/12/2011.
 */
public class RestrictionData implements RestrictionI {
	
	/**
	 * Attribute that represents a filter for adding to a statistics query. 
	 */
	private Restriction statsRestriction = null;
	
	/**
	 * Attribute that represents the value description. 
	 */
	private String valueDescription = null;

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldOperation()
	 */
	public String getFieldOperation() {
		if(statsRestriction!=null){
			return statsRestriction.getFieldOperation();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getRestrictionUnion()
	 */
	public String getRestrictionUnion() {
		if(statsRestriction!=null){
			return statsRestriction.getRestrictionUnion();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldType()
	 */
	public int getFieldType() {
		if(statsRestriction!=null){
			return statsRestriction.getFieldType();
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldValue()
	 */
	public String getFieldValue() {
		if(statsRestriction!=null){
			return statsRestriction.getFieldValue();
		}
		return null;
	}

	/**
	 * Constructor method for the class RestrictionData.java.
	 * @param statsRestriction
	 * @param valueDescription 
	 */
	public RestrictionData(Restriction statsRestriction, String valueDescription) {
		this.statsRestriction = statsRestriction;
		this.valueDescription = valueDescription;
	}

	
	/**
	 * Gets the value of the attribute 'statsRestriction'.
	 * @return the value of the attribute 'statsRestriction'.
	 */
	public Restriction getStatsRestriction() {
		return statsRestriction;
	}

	
	/**
	 * Sets the value of the attribute 'statsRestriction'.
	 * @param statsRestriction The value for the attribute 'statsRestriction'.
	 */
	public void setStatsRestriction(Restriction statsRestriction) {
		this.statsRestriction = statsRestriction;
	}

	
	/**
	 * Gets the value of the attribute 'valueDescription'.
	 * @return the value of the attribute 'valueDescription'.
	 */
	public String getValueDescription() {
		return valueDescription;
	}

	
	/**
	 * Sets the value of the attribute 'valueDescription'.
	 * @param valueDescription The value for the attribute 'valueDescription'.
	 */
	public void setValueDescription(String valueDescription) {
		this.valueDescription = valueDescription;
	}

}
