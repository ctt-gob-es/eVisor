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
 * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.item.TabularStatistics.java.</p>
 * <b>Description:</b><p>Class that represents a tabular statistics result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>22/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 22/09/2011.
 */
package es.gob.signaturereport.maudit.statistics.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;



/** 
 * <p>Class that represents a tabular statistics result.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 22/09/2011.
 */
public class TabularStatistics {

	/**
	 * Attribute that represents the field identifier for the rows of the table. The allowed values are:<br/>
	 * {@link StatisticsManager#APPLICATION_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#RESULT_CODE_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#UO_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SIGN_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SERVICE_FIELD_TYPE}.<br/> 
	 */
	private int rowType = 0;
	/**
	 * Attribute that represents the field identifier for the columns of the table. The allowed values are:<br/>
	 * {@link StatisticsManager#APPLICATION_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#RESULT_CODE_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#UO_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SIGN_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SERVICE_FIELD_TYPE}.<br/> 
	 */
	private int columnType = 0;
	
	/**
	 * Attribute that represents the list of row names. 
	 */
	private ArrayList<String> rowNames = null;
	
	/**
	 * Attribute that represents the list of column names. 
	 */
	private ArrayList<String> columnNames = null;
	
	/**
	 * Attribute that represents the values of the table. 
	 */
	private LinkedHashMap<String,LinkedHashMap<String,Long>> matrix = null;

	/**
	 * Constructor method for the class TabularStatistics.java.
	 * @param rowId	The field identifier for the rows of the table. The allowed values are:<br/>
	 * {@link StatisticsManager#APPLICATION_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#RESULT_CODE_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#UO_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SIGN_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SERVICE_FIELD_TYPE}.<br/> 
	 * @param columnId The field identifier for the columns of the table. The allowed values are:<br/>
	 * {@link StatisticsManager#APPLICATION_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#RESULT_CODE_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#UO_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SIGN_FIELD_TYPE}.<br/>
	 * {@link StatisticsManager#SERVICE_FIELD_TYPE}.<br/> 
	 */
	public TabularStatistics(int rowId, int columnId) {
		super();
		this.rowType = rowId;
		this.columnType = columnId;
		rowNames = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		matrix = new LinkedHashMap<String, LinkedHashMap<String,Long>>();
	}
	
	/**
	 * Adds a value into the table.
	 * @param rowName		Row name where the value will be inserted. 
	 * @param columnName	Column name where the value will be inserted.
	 * @param value			Value.
	 */
	public void addValue(String rowName, String columnName, Long value){
		if(rowName!=null && columnName!=null && value!=null){
			if(!rowNames.contains(rowName)){
				rowNames.add(rowName);
			}
			if(!columnNames.contains(columnName)){
				columnNames.add(columnName);
			}
			LinkedHashMap<String,Long> rowValues = null;
			if(matrix.containsKey(rowName)){
				rowValues = matrix.get(rowName);
			}else{
				rowValues = new LinkedHashMap<String, Long>();
			}
			rowValues.put(columnName, value);
			matrix.put(rowName, rowValues);
		}
		
	}
	
	/**
	 * Gets the number of rows. 
	 * @return	Number of rows.
	 */
	public int getRowsNumber(){
		return rowNames.size();
	}
	
	/**
	 * Gets the number of columns.
	 * @return	Number of columns.
	 */
	public int getColumnsNumber(){
		return columnNames.size();
	}
	
	/**
	 * Gets the row names.
	 * @return	Row names.
	 */
	public String[] getRowNames(){
		if(!rowNames.isEmpty()){
			return rowNames.toArray(new String[rowNames.size()]);
		}else{
			return null;
		}
	}
	/**
	 * Gets the column names.
	 * @return	Column names.
	 */
	public String[] getColumnNames(){
		if(!columnNames.isEmpty()){
			return columnNames.toArray(new String[columnNames.size()]);
		}else{
			return null;
		}
	}
	
	/**
	 * Gets the value of a record included into the table by row name and column name.
	 * @param rowName		Row name.
	 * @param columnName	Column name.
	 * @return	Value.
	 */
	public Long getValueByNames(String rowName, String columnName){
		if(rowName!=null && columnName!=null){
			LinkedHashMap<String, Long> rowValues =  matrix.get(rowName);
			if(rowValues!=null){
				Long value = rowValues.get(columnName);
				//Si el valor forma parte de la matrix se devuelve 0 en caso contrario null
				if(value==null && columnNames.contains(columnName)){
					value =new Long(0);
				}
				return value;
			}
		}
		return null;
	}
	
	/**
	 * Gets the value of a record included into the table by row index and column index.
	 * @param rowIndex		Row position.
	 * @param columnIndex	Column position.
	 * @return	Value.
	 */
	public Long getValueByIndex(int rowIndex,int columnIndex){
		String rowName = null;
		String columnName = null;
		if(rowIndex>0 && rowIndex<rowNames.size()){
			rowName = rowNames.get(rowIndex);
		}else{
			return null;
		}
		if(columnIndex>0 && columnIndex < columnNames.size()){
			columnName = columnNames.get(columnIndex);
		}else{
			return null;
		}
		return getValueByNames(rowName, columnName);
	}

	
	/**
	 * Gets the value of the field identifier for the rows of the table.
	 * @return the value of the field identifier for the rows of the table.
	 */
	public int getRowType() {
		return rowType;
	}

	
	/**
	 * Sets the value of the field identifier for the rows of the table.
	 * @param rowId The value for the field identifier for the rows of the table.
	 */
	public void setRowType(int rowId) {
		this.rowType = rowId;
	}

	
	/**
	 * Gets the value of the field identifier for the columns of the table.
	 * @return the value of the field identifier for the columns of the table.
	 */
	public int getColumnType() {
		return columnType;
	}

	
	/**
	 * Sets the value of the field identifier for the columns of the table.
	 * @param columnId The value for the field identifier for the columns of the table.
	 */
	public void setColumnType(int columnId) {
		this.columnType = columnId;
	}
	
	public void replaceRowNames(ArrayList<String> newRowNames){
		if(rowNames.size() == newRowNames.size() ){
			for(int i =0;i<rowNames.size();i++){
				String oldName = rowNames.get(i);
				String newName = newRowNames.get(i);
				LinkedHashMap<String,Long> columnValues = matrix.remove(oldName);
				if(columnValues!=null){
					matrix.put(newName, columnValues);
				}
			}
			rowNames.clear();
			rowNames.addAll(newRowNames);
		}
	}
	
	/**
	 * Method for replacing the column names.
	 * @param newColumnNames	List of column names.
	 */
	public void replaceColumnNames(ArrayList<String> newColumnNames){
		if(columnNames.size()== newColumnNames.size()){
			String[] rowNames = matrix.keySet().toArray(new String[matrix.keySet().size()]);
			for(int i =0;i<rowNames.length;i++){
				LinkedHashMap<String,Long> columnValues = matrix.remove(rowNames[i]);
				LinkedHashMap<String,Long> newColumnValues = new LinkedHashMap<String, Long>();
				Iterator<String> columnIt =columnValues.keySet().iterator();
				while(columnIt.hasNext()){
					String originalColumnName = columnIt.next();
					int index = columnNames.indexOf(originalColumnName);
					if(index>=0){
						newColumnValues.put(newColumnNames.get(index), columnValues.get(originalColumnName));
					}
				}
				matrix.put(rowNames[i], newColumnValues);
			}
			columnNames.clear();
			columnNames.addAll(newColumnNames);
		}
	}
}
