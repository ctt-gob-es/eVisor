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
 * <b>File:</b><p>es.gob.signaturereport.controller.StatsController.java.</p>
 * <b>Description:</b><p>Controller class for generating the statistics reports</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.richfaces.component.UIScrollableDataTable;

import com.lowagie.text.DocumentException;

import es.gob.signaturereport.controller.list.ComparatorStatData;
import es.gob.signaturereport.controller.list.ComparatorStatDataTabular;
import es.gob.signaturereport.controller.list.RestrictionData;
import es.gob.signaturereport.controller.list.StatData;
import es.gob.signaturereport.controller.list.StatDataTabular;
import es.gob.signaturereport.controller.utils.CustomPieSectionLabelGenerator;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.statistics.StatisticsException;
import es.gob.signaturereport.maudit.statistics.StatisticsManager;
import es.gob.signaturereport.maudit.statistics.item.GroupedStatistics;
import es.gob.signaturereport.maudit.statistics.item.Restriction;
import es.gob.signaturereport.maudit.statistics.item.RestrictionI;
import es.gob.signaturereport.maudit.statistics.item.TabularStatistics;
import es.gob.signaturereport.modes.responses.ReportGenerationResponse;
import es.gob.signaturereport.modes.responses.ReportValidationResponse;

/** 
 * <p>Controller class for generating the statistics reports.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("stats")
@SessionScoped
public class StatsController extends AbstractController {

	/**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -7531476751631998803L;

	/**
	 * Constants that contains the services identifiers. 
	 */
	private static final LinkedHashMap<String, Integer> SERVICES = new LinkedHashMap<String, Integer>();

	/**
	 * Attribute that represents the signed report mode identifiers. 
	 */
	private static final LinkedHashMap<String, Boolean> REPORTSIGNATURE = new LinkedHashMap<String, Boolean>();

	/**
	 * Attribute that represents the result codes identifiers. 
	 */
	private static final LinkedHashMap<String, Integer> RESULTCODES = new LinkedHashMap<String, Integer>();

	/**
	 * Attribute that represents a select items for the services identifiers. 
	 */
	private static final List<SelectItem> SELECTSERVICES = new ArrayList<SelectItem>();

	/**
	 * Attribute that represents the select item for the signature identifiers. 
	 */
	private static final List<SelectItem> SELECTSIGNATURE = new ArrayList<SelectItem>();

	/**
	 * Attribute that represents the select items for the code results. 
	 */
	private static final List<SelectItem> SELECTCODE = new ArrayList<SelectItem>();

	/**
	 * Attribute that represents the codes descriptions. 
	 */
	private static final LinkedHashMap<Integer, String> RESULTCODEDESCRIPTION = new LinkedHashMap<Integer, String>();

	/**
	 * Attribute that represents the 500 number. 
	 */
	private static final int NUM_500 = 500;

	/**
	 * Attribute that represents the 390 number. 
	 */
	private static final int NUM_390 = 390;

	static {

		SERVICES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_SVC), AuditManagerI.GENERATION_REPORT_SRVC);
		SERVICES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_SVC), AuditManagerI.VALIDATION_REPORT_SRVC);

		REPORTSIGNATURE.put(Language.getMessage(LanguageKeys.WSTAT_SIGN), Boolean.TRUE);
		REPORTSIGNATURE.put(Language.getMessage(LanguageKeys.WSTAT_UNSIGN), Boolean.FALSE);

		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_0), ReportGenerationResponse.PROCESS_OK);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_1), ReportGenerationResponse.INVALID_INPUT_PARAMETERS);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_2), ReportGenerationResponse.INVALID_SIGNATURE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_3), ReportGenerationResponse.INVALID_SOAP_SIGNATURE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_4), ReportGenerationResponse.INVALID_SIGNED_DOCUMENT);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_5), ReportGenerationResponse.AFIRMA_UNAVAILABLE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_6), ReportGenerationResponse.INVALID_TEMPLATE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_7), ReportGenerationResponse.INVALID_PAGE_NUMBER);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_GEN_CODE_U), ReportGenerationResponse.UNKNOWN_ERROR);

		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_0), ReportValidationResponse.VALID_SIGNATURE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_1), ReportValidationResponse.INVALID_INPUT_PARAMETERS);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_2), ReportValidationResponse.INVALID_SIGNATURE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_3), ReportValidationResponse.WARNING_SIGNATURE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_4), ReportValidationResponse.UNKNOWN_STATUS);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_5), ReportValidationResponse.AFIRMA_UNAVAILABLE);
		RESULTCODES.put(Language.getMessage(LanguageKeys.WSTAT_VAL_CODE_U), ReportValidationResponse.UNKNOWN_ERROR);

		Iterator<String> messages = RESULTCODES.keySet().iterator();
		while(messages.hasNext()){
			String msg = messages.next();
			Integer value = RESULTCODES.get(msg);
			if(RESULTCODEDESCRIPTION.containsKey(value)){
				String des = RESULTCODEDESCRIPTION.get(value);
				des += " / "+msg;
				RESULTCODEDESCRIPTION.put(value, des);
			}else{
				RESULTCODEDESCRIPTION.put(value, msg);
			}
		}
		

		Iterator<String> itServices = SERVICES.keySet().iterator();
		while (itServices.hasNext()) {
			String serviceDesc = itServices.next();
			SELECTSERVICES.add(new SelectItem(serviceDesc, serviceDesc));
		}
		Iterator<String> itSignature = REPORTSIGNATURE.keySet().iterator();
		while (itSignature.hasNext()) {
			String signatureDesc = itSignature.next();
			SELECTSIGNATURE.add(new SelectItem(signatureDesc, signatureDesc));
		}

		Iterator<String> itCodes = RESULTCODES.keySet().iterator();
		while (itCodes.hasNext()) {
			String codeDesc = itCodes.next();
			SELECTCODE.add(new SelectItem(codeDesc, codeDesc));
		}
	}
	/**
	 * Attribute that represents initial date. 
	 */
	private Date iniDate = new Date();

	/**
	 * Attribute that represents end date. 
	 */
	private Date endDate = new Date();

	/**
	 * Attribute that represents list of restrictions. 
	 */
	private List<RestrictionI> restrictions = new ArrayList<RestrictionI>();

	/**
	 * Attribute that represents . 
	 */
	private String editRestrictionFieldOperation = StatisticsManager.EQUAL_OPERATION;
	/**
	 * Attribute that represents . 
	 */
	private String editRestrictionRestrictionUnion = StatisticsManager.AND_UNION;
	/**
	 * Attribute that represents . 
	 */
	private int editRestrictionFieldType = StatisticsManager.APPLICATION_FIELD_TYPE;
	/**
	 * Attribute that represents . 
	 */
	private String editRestrictionFieldValue = null;

	/**
	 * Attribute that represents . 
	 */
	private int fieldAgrup = StatisticsManager.APPLICATION_FIELD_TYPE;
	/**
	 * Attribute that represents . 
	 */
	private int fieldRow = StatisticsManager.APPLICATION_FIELD_TYPE;

	/**
	 * Attribute that represents . 
	 */
	private int fieldColumn = StatisticsManager.SERVICE_FIELD_TYPE;

	/**
	 * Attribute that represents . 
	 */
	private GroupedStatistics statsGrouped = null;
	/**
	 * Attribute that represents . 
	 */
	private TabularStatistics statsTabular = null;

	/**
	 * Attribute that represents . 
	 */
	org.richfaces.model.selection.Selection restrictionSelected;
	/**
	 * Attribute that represents . 
	 */
	UIScrollableDataTable restrictionTable;

	/**
	 * Attribute that represents the statistic type. 
	 */
	private int statType = 0;
	
	/**
     * Attribute that represents . 
     */
    @Inject @Singleton
    private StatisticsManager statsManager;

	/**
	 * Gets the field group.
	 * @return	Field group identifier.
	 */
	public int getFieldAgrup() {
		return fieldAgrup;
	}

	/**
	 * Sets the field group.
	 * @param fieldAgrupId Field group identifier.
	 */
	public void setFieldAgrup(int fieldAgrupId) {
		this.fieldAgrup = fieldAgrupId;
	}

	/**
	 * Gets the field row identifier.
	 * @return	Field row identifier.
	 */
	public int getFieldRow() {
		return fieldRow;
	}

	/**
	 * Sets the field row identifier.
	 * @param fieldRowId Field row identifier.
	 */
	public void setFieldRow(int fieldRowId) {
		this.fieldRow = fieldRowId;
	}

	/**
	 * Gets the field column identifier.
	 * @return	Field column identifier.
	 */
	public int getFieldColumn() {
		return fieldColumn;
	}

	/**
	 * Sets the field column identifier.
	 * @param fieldColumnId Field column identifier.
	 */
	public void setFieldColumn(int fieldColumnId) {
		this.fieldColumn = fieldColumnId;
	}

	/**
	 * Gets the statistics type.
	 * @return	Statistics type identifier.
	 */
	public int getStatType() {
		return statType;
	}

	/**
	 * Sets the statistics type identifier.
	 * @param statTypeId	The statistics type identifier.
	 */
	public void setStatType(int statTypeId) {
		this.statType = statTypeId;
	}

	/**
	 * Gets the select items for the services.
	 * @return	Select items for the services.
	 */
	public List<SelectItem> getSelectServices() {
		return SELECTSERVICES;
	}

	/**
	 * Gets the select items for the signature.
	 * @return	Select items for the signature.
	 */
	public List<SelectItem> getSelectSignature() {
		return SELECTSIGNATURE;
	}

	/**
	 * Gets the select items for the result codes.
	 * @return	Select items for the result codes.
	 */
	public List<SelectItem> getSelectErrorCode() {
		return SELECTCODE;
	}

	/**
	 * Gets the select items for the union operation.
	 * @return	Select items for the union operation.
	 */
	public List<SelectItem> getSelectUnion() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		SelectItem si = new SelectItem(StatisticsManager.AND_UNION, Language.getMessage(LanguageKeys.WSTAT_AND));
		items.add(si);

		si = new SelectItem(StatisticsManager.OR_UNION, Language.getMessage(LanguageKeys.WSTAT_OR));
		items.add(si);

		return items;
	}

	/**
	 * Gets the select items for the fields.
	 * @return	Select items for the fields.
	 */
	public List<SelectItem> getSelectField() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		SelectItem si = new SelectItem(StatisticsManager.APPLICATION_FIELD_TYPE, Language.getMessage(LanguageKeys.WSTAT_APP));
		items.add(si);

		si = new SelectItem(StatisticsManager.UO_FIELD_TYPE, Language.getMessage(LanguageKeys.WSTAT_UO));
		items.add(si);

		si = new SelectItem(StatisticsManager.SERVICE_FIELD_TYPE, Language.getMessage(LanguageKeys.WSTAT_SERVICEID));
		items.add(si);

		si = new SelectItem(StatisticsManager.SIGN_FIELD_TYPE, Language.getMessage(LanguageKeys.WSTAT_REPORT));
		items.add(si);

		si = new SelectItem(StatisticsManager.RESULT_CODE_FIELD_TYPE, Language.getMessage(LanguageKeys.WSTAT_CODRES));
		items.add(si);

		return items;
	}

	/**
	 * Gets the select items for the comparator operations.
	 * @return	Select items for the comparator operations.
	 */
	public List<SelectItem> getSelectOperation() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		SelectItem si = new SelectItem(StatisticsManager.EQUAL_OPERATION, Language.getMessage(LanguageKeys.WSTAT_EQUAL));
		items.add(si);
		si = new SelectItem(StatisticsManager.DISTINCT_OPERATION, Language.getMessage(LanguageKeys.WSTAT_DIST));
		items.add(si);
		si = new SelectItem(StatisticsManager.LIKE_OPERATION, Language.getMessage(LanguageKeys.WSTAT_LIKE));
		items.add(si);
		return items;
	}

	/**
	 * Gets the edit restriction field operation.
	 * @return The edit restriction field operation.
	 */
	public String getEditRestrictionFieldOperation() {
		return editRestrictionFieldOperation;
	}

	/**
	 * Sets the edit restriction field operation.
	 * @param fieldOperation	The edit restriction field operation.
	 */
	public void setEditRestrictionFieldOperation(String fieldOperation) {
		this.editRestrictionFieldOperation = fieldOperation;
	}

	/**
	 * Gets the edit restriction union operation.
	 * @return	Edit restriction union operation.
	 */
	public String getEditRestrictionRestrictionUnion() {
		return editRestrictionRestrictionUnion;
	}

	/**
	 * Sets the edit restriction union operation.
	 * @param union Edit restriction union operation.
	 */
	public void setEditRestrictionRestrictionUnion(String union) {
		this.editRestrictionRestrictionUnion = union;
	}

	/**
	 * Gets the edit restriction field type.
	 * @return Edit restriction field type.
	 */
	public int getEditRestrictionFieldType() {
		return editRestrictionFieldType;
	}

	/**
	 * Sets the edit restriction field type.
	 * @param fieldType	Edit restriction field type.
	 */
	public void setEditRestrictionFieldType(int fieldType) {
		this.editRestrictionFieldType = fieldType;
	}

	/**
	 * Gets the edit restriction field value.
	 * @return	Edit restriction field value.
	 */
	public String getEditRestrictionFieldValue() {
		return editRestrictionFieldValue;
	}

	/**
	 * Sets the edit restriction field value.
	 * @param fieldValue Edit restriction field value
	 */
	public void setEditRestrictionFieldValue(String fieldValue) {
		this.editRestrictionFieldValue = fieldValue;
	}

	/**
	 * Gets the select for the restriction fields.
	 * @return	Select for the restriction fields.
	 */
	public org.richfaces.model.selection.Selection getRestrictionSelected() {
		return restrictionSelected;
	}

	/**
	 * Sets the select for the restriction fields.
	 * @param resSelected	Select for the restriction fields.
	 */
	public void setRestrictionSelected(org.richfaces.model.selection.Selection resSelected) {
		this.restrictionSelected = resSelected;
	}

	/**
	 * Gets the restriction table.
	 * @return	Restriction table.
	 */
	public UIScrollableDataTable getRestrictionTable() {
		return restrictionTable;
	}

	/**
	 * Sets the restriction table.
	 * @param resTable	The restriction table.
	 */
	public void setRestrictionTable(UIScrollableDataTable resTable) {
		this.restrictionTable = resTable;
	}

	/**
	 * Gets the restriction list.
	 * @return	Restriction list.
	 */
	public List<RestrictionI> getRestrictions() {
		return restrictions;
	}

	/**
	 * Sets the restriction list.
	 * @param restrictionList	Restriction list.
	 */
	public void setRestrictions(List<RestrictionI> restrictionList) {
		this.restrictions = restrictionList;
	}

	/**
	 * Gets the starting time for the statistics query.
	 * @return	The starting time for the statistics query.
	 */
	public Date getIniDate() {
		return iniDate;
	}

	/**
	 * Sets the starting time for the statistics query.
	 * @param time Starting time for the statistics query.
	 */
	public void setIniDate(Date time) {
		this.iniDate = time;
	}

	/**
	 * Gets the ending time for the statistics query.
	 * @return	The ending time for the statistics query.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the ending time for the statistics query.
	 * @param time	Ending time for the statistics query.
	 */
	public void setEndDate(Date time) {
		this.endDate = time;
	}

	/**
	 * Method for generating of the statistics. 
	 * @return	Operation result.
	 */
	public String onGenerateStat() {
		if (statType == 0) {
			try {
				GroupedStatistics result = statsManager.getGroupedStatistics(iniDate, endDate, fieldAgrup, restrictions);
				// Modificamos los resultados para sustituir los c�digos por
				// cadenas descriptivas
				if (result.getField() == StatisticsManager.RESULT_CODE_FIELD_TYPE) {
					statsGrouped = new GroupedStatistics();
					statsGrouped.setField(result.getField());
					Iterator<String> it = result.getValues().keySet().iterator();
					while (it.hasNext()) {
						String value = it.next();
						int code = Integer.parseInt(value);
						String desc = RESULTCODEDESCRIPTION.get(code);
						if (desc != null) {
							statsGrouped.getValues().put(desc, result.getValues().get(value));
						} else {
							statsGrouped.getValues().put(value, result.getValues().get(value));
						}
					}
				} else if (result.getField() == StatisticsManager.SERVICE_FIELD_TYPE) {
					statsGrouped = new GroupedStatistics();
					statsGrouped.setField(result.getField());
					Iterator<String> it = result.getValues().keySet().iterator();
					while (it.hasNext()) {
						String value = it.next();
						if (value.equals(String.valueOf(AuditManagerI.GENERATION_REPORT_SRVC))) {
							statsGrouped.getValues().put(Language.getMessage(LanguageKeys.WSTAT_GEN_SVC), result.getValues().get(value));
						} else if (value.equals(String.valueOf(AuditManagerI.VALIDATION_REPORT_SRVC))) {
							statsGrouped.getValues().put(Language.getMessage(LanguageKeys.WSTAT_VAL_SVC), result.getValues().get(value));
						} else {
							statsGrouped.getValues().put(value, result.getValues().get(value));
						}
					}
				} else if (result.getField() == StatisticsManager.SIGN_FIELD_TYPE) {
					statsGrouped = new GroupedStatistics();
					statsGrouped.setField(result.getField());
					Iterator<String> it = result.getValues().keySet().iterator();
					while (it.hasNext()) {
						String value = it.next();
						if (value.equalsIgnoreCase("true")) {
							statsGrouped.getValues().put(Language.getMessage(LanguageKeys.WSTAT_SIGN), result.getValues().get(value));
						} else if (value.equalsIgnoreCase("false")) {
							statsGrouped.getValues().put(Language.getMessage(LanguageKeys.WSTAT_UNSIGN), result.getValues().get(value));
						} else {
							statsGrouped.getValues().put(value, result.getValues().get(value));
						}
					}
				} else {
					statsGrouped = result;
				}

				
				setStatus(STATUS_STATRESULT);
			} catch (StatisticsException e) {
				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG134), e.getMessage());
				return null;
			}

		} else {
			try {
				statsTabular = statsManager.getTabulatedStatistics(iniDate, endDate, fieldRow, fieldColumn, restrictions);
				// Modificamos los resultados para sustituir los c�digos por
				// cadenas descriptivas
				ArrayList<String> newRowNames = new ArrayList<String>();
				ArrayList<String> newColumnNames = new ArrayList<String>();
				if (statsTabular.getRowType() == StatisticsManager.SIGN_FIELD_TYPE) {
					String[ ] rowNames = statsTabular.getRowNames();
					if (rowNames != null) {
						for (int i = 0; i < rowNames.length; i++) {
							if (rowNames[i].equalsIgnoreCase("true")) {
								newRowNames.add(Language.getMessage(LanguageKeys.WSTAT_SIGN));
							} else if (rowNames[i].equalsIgnoreCase("false")) {
								newRowNames.add(Language.getMessage(LanguageKeys.WSTAT_UNSIGN));
							} else {
								newRowNames.add(rowNames[i]);
							}
						}
					}
				} else if (statsTabular.getRowType() == StatisticsManager.SERVICE_FIELD_TYPE) {
					String[ ] rowNames = statsTabular.getRowNames();
					if (rowNames != null) {
						for (int i = 0; i < rowNames.length; i++) {
							if (rowNames[i].equals(String.valueOf(AuditManagerI.GENERATION_REPORT_SRVC))) {
								newRowNames.add(Language.getMessage(LanguageKeys.WSTAT_GEN_SVC));
							} else if (rowNames[i].equals(String.valueOf(AuditManagerI.VALIDATION_REPORT_SRVC))) {
								newRowNames.add(Language.getMessage(LanguageKeys.WSTAT_VAL_SVC));
							} else {
								newRowNames.add(rowNames[i]);
							}
						}
					}
				} else if (statsTabular.getRowType() == StatisticsManager.RESULT_CODE_FIELD_TYPE) {
					String[ ] rowNames = statsTabular.getRowNames();
					if (rowNames != null) {
						for (int i = 0; i < rowNames.length; i++) {
							int code = Integer.parseInt(rowNames[i]);
							String codeDescription = RESULTCODEDESCRIPTION.get(code);
							if (codeDescription != null) {
								newRowNames.add(codeDescription);
							} else {
								newRowNames.add(rowNames[i]);
							}

						}
					}
				}

				if (statsTabular.getColumnType() == StatisticsManager.SIGN_FIELD_TYPE) {
					String[ ] columnNames = statsTabular.getColumnNames();
					if (columnNames != null) {
						for (int i = 0; i < columnNames.length; i++) {
							if (columnNames[i].equalsIgnoreCase("true")) {
								newColumnNames.add(Language.getMessage(LanguageKeys.WSTAT_SIGN));
							} else if (columnNames[i].equalsIgnoreCase("false")) {
								newColumnNames.add(Language.getMessage(LanguageKeys.WSTAT_UNSIGN));
							} else {
								newColumnNames.add(columnNames[i]);
							}
						}
					}
				} else if (statsTabular.getColumnType() == StatisticsManager.SERVICE_FIELD_TYPE) {
					String[ ] columnNames = statsTabular.getColumnNames();
					if (columnNames != null) {
						for (int i = 0; i < columnNames.length; i++) {
							if (columnNames[i].equals(String.valueOf(AuditManagerI.GENERATION_REPORT_SRVC))) {
								newColumnNames.add(Language.getMessage(LanguageKeys.WSTAT_GEN_SVC));
							} else if (columnNames[i].equals(String.valueOf(AuditManagerI.VALIDATION_REPORT_SRVC))) {
								newColumnNames.add(Language.getMessage(LanguageKeys.WSTAT_VAL_SVC));
							} else {
								newColumnNames.add(columnNames[i]);
							}
						}
					}
				} else if (statsTabular.getColumnType() == StatisticsManager.RESULT_CODE_FIELD_TYPE) {
					String[ ] columnNames = statsTabular.getColumnNames();
					if (columnNames != null) {
						for (int i = 0; i < columnNames.length; i++) {
							int code = Integer.parseInt(columnNames[i]);
							String codeDescription = RESULTCODEDESCRIPTION.get(code);
							if (codeDescription != null) {
								newColumnNames.add(codeDescription);
							} else {
								newColumnNames.add(columnNames[i]);
							}

						}
					}
				}
				if (!newRowNames.isEmpty()) {
					statsTabular.replaceRowNames(newRowNames);
				}
				if (!newColumnNames.isEmpty()) {
					statsTabular.replaceColumnNames(newColumnNames);
				}

				
				setStatus(STATUS_STATTABULARRESULT);
			} catch (StatisticsException e) {
				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG134), e.getMessage());
				return null;
			}
		}

		return ActionsWeb.APP_STATS;
	}

	/**
	 * Gets the grouping statistics data.
	 * @return Grouping statistics data.
	 */
	public List<StatData> getStatDataList() {
		List<StatData> l = new ArrayList<StatData>();
		if (statsGrouped == null) {
			return l;
		}
		if (statsGrouped.getValues() == null) {
			return l;
		}

		Iterator<String> itKeys = statsGrouped.getValues().keySet().iterator();
		while (itKeys.hasNext()) {
			String key = (String) itKeys.next();
			l.add(new StatData(key, statsGrouped.getValues().get(key)));
		}

		// Sort l
		java.util.Collections.sort(l, new ComparatorStatData());

		return l;

	}

	/**
	 * Gets the tabular statistics data.
	 * @return Tabular statistics data.
	 */
	public List<StatDataTabular> getStatDataTabularList() {
		List<StatDataTabular> l = new ArrayList<StatDataTabular>();
		if (statsTabular == null) {
			return l;
		}
		if (statsTabular.getColumnNames() == null) {
			return l;
		}
		if (statsTabular.getColumnNames() == null) {
			return l;
		}
		String[ ] columnNames = statsTabular.getColumnNames();
		String[ ] rowNames = statsTabular.getRowNames();
		for (int i = 0; i < rowNames.length; i++) {
			for (int j = 0; j < columnNames.length; j++) {
				Long value = statsTabular.getValueByNames(rowNames[i], columnNames[j]);
				if (value != null && value.longValue() > 0) {
					StatDataTabular data = new StatDataTabular(rowNames[i], columnNames[j], value);
					l.add(data);
				}

			}
		}

		// Sort l
		java.util.Collections.sort(l, new ComparatorStatDataTabular());

		return l;

	}

	/**
	 * Method for generating of the statistics pie data.
	 * @return Statistics pie data.
	 */
	private DefaultPieDataset getStatPieDataset() {

		DefaultPieDataset result = new DefaultPieDataset();

		if (statType == 0) {
			TreeMap<String, Long> mapSort = new TreeMap<String, Long>();
			mapSort.putAll(statsGrouped.getValues());
			Iterator<String> itKeys = mapSort.keySet().iterator();
			while (itKeys.hasNext()) {
				String key = (String) itKeys.next();
				result.setValue(key, statsGrouped.getValues().get(key));
			}
		}

		return result;
	}

	/**
	 * Method for generating the pie chart.
	 * @param out	{@link OutputStream} for writing.
	 * @param data	Object data.
	 */
	public void generatePieChart(OutputStream out, Object data) {

		try {
			JFreeChart chart = getChart();
			
			BufferedImage buffImg = chart.createBufferedImage(NUM_500, NUM_390, BufferedImage.TYPE_INT_RGB, null);

			ImageIO.write(buffImg, "jpeg", out);
		} catch (IOException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG135), e.getMessage());
		}

	}

	/**
	 * Method that generates a statistical graph.
	 * @return	A chart.
	 */
	private JFreeChart getChart() {
		JFreeChart chart = ChartFactory.createPieChart(null, getStatPieDataset(), true, false, false);

		chart.setBackgroundPaint(Color.WHITE);
		chart.setTextAntiAlias(true);
		chart.setAntiAlias(true);
		
		//Hides the labels
		Color transparent = new Color(0.0f,0.0f,0.0f,0.0f); 
		org.jfree.chart.plot.PiePlot plot =(org.jfree.chart.plot.PiePlot) chart.getPlot();
		plot.setLabelLinksVisible(Boolean.FALSE); 
		plot.setLabelOutlinePaint(transparent); 
		plot.setLabelBackgroundPaint(transparent); 
		plot.setLabelShadowPaint(transparent);
		PieSectionLabelGenerator generator = new CustomPieSectionLabelGenerator();
		plot.setLabelGenerator(generator);
		return chart;
	}

	/**
	 * Action to add a restriction to the list.
	 * @return	Process result.
	 */
	public String onAddRestriction() {

		if (restrictions == null) {
			restrictions = new ArrayList<RestrictionI>();
		}

		if (!isCanNext()) {
			editRestrictionRestrictionUnion = null;
		}

		if (editRestrictionFieldValue == null) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG136), Language.getMessage(LanguageKeys.WMSG137));
			return null;
		}
		editRestrictionFieldValue = editRestrictionFieldValue.trim();
		if (editRestrictionFieldValue.equals("")) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG136), Language.getMessage(LanguageKeys.WMSG137));
			return null;
		}

		if (editRestrictionFieldType == StatisticsManager.APPLICATION_FIELD_TYPE) {
			Restriction editRestriction = (Restriction) Restriction.createApplicationRestriction(editRestrictionFieldValue, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			RestrictionData data = new RestrictionData(editRestriction, editRestrictionFieldValue);
			restrictions.add(data);
		} else if (editRestrictionFieldType == StatisticsManager.UO_FIELD_TYPE) {
			Restriction editRestriction = (Restriction) Restriction.createUORestriction(editRestrictionFieldValue, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			RestrictionData data = new RestrictionData(editRestriction, editRestrictionFieldValue);
			restrictions.add(data);
		} else if (editRestrictionFieldType == StatisticsManager.SERVICE_FIELD_TYPE) {

			int num = SERVICES.get(editRestrictionFieldValue);
			Restriction editRestriction = (Restriction) Restriction.createServiceRestriction(num, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			RestrictionData data = new RestrictionData(editRestriction, editRestrictionFieldValue);
			restrictions.add(data);
		} else if (editRestrictionFieldType == StatisticsManager.RESULT_CODE_FIELD_TYPE) {

			int num = RESULTCODES.get(editRestrictionFieldValue);
			Restriction editRestriction = (Restriction) Restriction.createResultRestriction(num, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			RestrictionData data = new RestrictionData(editRestriction, editRestrictionFieldValue);
			restrictions.add(data);
		} else if (editRestrictionFieldType == StatisticsManager.SIGN_FIELD_TYPE) {
			Boolean signed = REPORTSIGNATURE.get(editRestrictionFieldValue);
			Restriction editRestriction = (Restriction) Restriction.createSignRestriction(signed, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			RestrictionData data = new RestrictionData(editRestriction, editRestrictionFieldValue);
			restrictions.add(data);
		}

		editRestrictionFieldValue = null;

		return null;

	}

	/**
	 * Indicates if the restriction list is empty.
	 * @return	false if the restriction list is null or empty. Otherwise true.
	 */
	public boolean isCanNext() {
		if (restrictions == null) {
			return false;
		}
		return (restrictions.size() > 0);
	}

	/**
	 * Indicates if the unable a union operation.
	 * @return True if the restriction list length greater than zero
	 */
	public boolean isEditUnion() {
		return (!((restrictions.size() == 0)));
	}

	/**
	 * Action of the selection the next restriction.
	 * @return	Process result.
	 */
	public String onNext() {
		if (restrictions == null) {
			restrictions = new ArrayList<RestrictionI>();
		}

		if (iniDate == null) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG140), Language.getMessage(LanguageKeys.WMSG141));
			return null;
		}

		if (endDate == null) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG140), Language.getMessage(LanguageKeys.WMSG142));
			return null;
		}

		if (iniDate.after(endDate)) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG140), Language.getMessage(LanguageKeys.WMSG143));
			return null;
		}

		// status = STATUS_STATOPTIONS;
		return null;
	}

	/**
	 * Action for creating a new restriction.
	 * @return	Process result.
	 */
	public String onNew() {
		restrictions = new ArrayList<RestrictionI>();

		editRestrictionFieldOperation = StatisticsManager.EQUAL_OPERATION;
		editRestrictionRestrictionUnion = StatisticsManager.AND_UNION;
		editRestrictionFieldType = StatisticsManager.APPLICATION_FIELD_TYPE;
		editRestrictionFieldValue = null;

		
		setStatus(STATUS_NONE);
		return null;
	}

	/**
	 * Action of the cancel the operation.
	 * @return	Status.
	 */
	public String onCancelOptions() {
		setStatus(STATUS_NONE);
		return null;
	}

	/**
	 * Exports to Excel the generated statistics.
	 * @return	Status.
	 */
	public String exportReportToExcel() {

		final FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		byte[ ] content = null;
		if (statType == 0) {
			JFreeChart chart = getChart();

			try {
				content = ExportUtils.generateReportToExcel(getFieldDescription(fieldAgrup), getStatDataList(), chart);
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG144), e.getMessage());
			}
		}

		if (statType == 1) {
			try {
				content = ExportUtils.generateReportTabularToExcel(getFieldDescription(fieldRow), getFieldDescription(fieldColumn), getStatDataTabularList());
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG144), e.getMessage());
			}
		}

		writeOutContent(response, content, Language.getMessage(LanguageKeys.WMSG145));
		facesContext.responseComplete();
		return null;

	}

	/**
	 * Exports to PDF the generated statistics.
	 * @return	Status.
	 */
	public String exportReportToPDF() {

		byte[ ] data = null;
		if (statType == 0) {
			JFreeChart chart = getChart();

			try {
				data = ExportUtils.generateReportToPDF(getFieldDescription(fieldAgrup), getStatDataList(), chart);
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG146), e.getMessage());
			} catch (DocumentException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG146), e.getMessage());
			}

		}

		if (statType == 1) {

			try {
				data = ExportUtils.generateReportTabularToPDF(getFieldDescription(fieldRow), getFieldDescription(fieldColumn), getStatDataTabularList());
			} catch (DocumentException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG146), e.getMessage());
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG146), e.getMessage());
			}

		}

		final FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		writeOutContent(response, data, Language.getMessage(LanguageKeys.WMSG147));

		facesContext.responseComplete();
		return null;

	}

	/**
	 * Action for modifying the current restriction.
	 * @return	Status.
	 */
	public String onModifyRestriction() {

		if (restrictionSelected == null) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG148), Language.getMessage(LanguageKeys.WMSG149));
			return null;
		}

		if (restrictionSelected.size() < 1) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG148), Language.getMessage(LanguageKeys.WMSG149));
			return null;
		}

		Iterator<Object> iterator = restrictionSelected.getKeys();
		RestrictionData resModify = null;
		while (iterator.hasNext()) {
			Object key = iterator.next();
			restrictionTable.setRowKey(key);
			if (restrictionTable.isRowAvailable()) {
				resModify = ((RestrictionData) restrictionTable.getRowData());
			}
		}

		int index = restrictions.indexOf(resModify);

		if (index >= 0) {

			RestrictionI editRestriction = null;
			if (index == 0) {
				editRestrictionRestrictionUnion = null;
			}

			if (editRestrictionFieldValue == null) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG150), Language.getMessage(LanguageKeys.WMSG137));
				return null;
			}
			editRestrictionFieldValue = editRestrictionFieldValue.trim();
			if (editRestrictionFieldValue.equals("")) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG150), Language.getMessage(LanguageKeys.WMSG137));
				return null;
			}
			Restriction res = null;
			if (editRestrictionFieldType == StatisticsManager.APPLICATION_FIELD_TYPE) {
				res = (Restriction) Restriction.createApplicationRestriction(editRestrictionFieldValue, editRestrictionFieldOperation, editRestrictionRestrictionUnion);

			} else if (editRestrictionFieldType == StatisticsManager.UO_FIELD_TYPE) {
				res = (Restriction) Restriction.createUORestriction(editRestrictionFieldValue, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			} else if (editRestrictionFieldType == StatisticsManager.SERVICE_FIELD_TYPE) {
				int num = SERVICES.get(editRestrictionFieldValue);
				res = (Restriction) Restriction.createServiceRestriction(num, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			} else if (editRestrictionFieldType == StatisticsManager.RESULT_CODE_FIELD_TYPE) {
				int num = RESULTCODES.get(editRestrictionFieldValue);
				res = (Restriction) Restriction.createResultRestriction(num, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			} else if (editRestrictionFieldType == StatisticsManager.SIGN_FIELD_TYPE) {
				Boolean value = REPORTSIGNATURE.get(editRestrictionFieldValue);
				res = (Restriction) Restriction.createSignRestriction(value, editRestrictionFieldOperation, editRestrictionRestrictionUnion);
			}
			// FIN DE crear la nueva restriccion

			restrictions.remove(index);

			if (res != null) {
				editRestriction = new RestrictionData(res, editRestrictionFieldValue);
				restrictions.add(index, editRestriction);
			}

		}

		return null;
	}

	/**
	 * Action for deleting the current restriction.
	 * @return Status.
	 */
	public String onDelRestriction() {

		if (restrictionSelected == null) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG151), Language.getMessage(LanguageKeys.WMSG149));
			return null;
		}

		if (restrictionSelected.size() < 1) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG151), Language.getMessage(LanguageKeys.WMSG149));
			return null;
		}

		Iterator<Object> iterator = restrictionSelected.getKeys();
		RestrictionData resModify = null;
		while (iterator.hasNext()) {
			Object key = iterator.next();
			restrictionTable.setRowKey(key);
			if (restrictionTable.isRowAvailable()) {
				resModify = ((RestrictionData) restrictionTable.getRowData());
			}

		}

		int index = restrictions.indexOf(resModify);

		if (index >= 0) {
			restrictions.remove(index);

			if ((index == 0) && (restrictions.size() > 0)) {
				// restrictions[0].rgetRestrictionUnion = null
				resModify = (RestrictionData) restrictions.get(0);
				Restriction newRes = null;
				editRestrictionFieldOperation = resModify.getFieldOperation();
				editRestrictionFieldType = resModify.getFieldType();
				editRestrictionFieldValue = resModify.getValueDescription();

				switch (editRestrictionFieldType) {
					case StatisticsManager.APPLICATION_FIELD_TYPE:
						newRes = (Restriction) Restriction.createApplicationRestriction(editRestrictionFieldValue, editRestrictionFieldOperation, null);
						break;
					case StatisticsManager.UO_FIELD_TYPE:
						newRes = (Restriction) Restriction.createUORestriction(editRestrictionFieldValue, editRestrictionFieldOperation, null);
						break;
					case StatisticsManager.SERVICE_FIELD_TYPE:
						newRes = (Restriction) Restriction.createServiceRestriction(SERVICES.get(editRestrictionFieldValue), editRestrictionFieldOperation, null);
						break;
					case StatisticsManager.RESULT_CODE_FIELD_TYPE:
						newRes = (Restriction) Restriction.createResultRestriction(RESULTCODES.get(editRestrictionFieldValue), editRestrictionFieldOperation, null);
						break;
					case StatisticsManager.SIGN_FIELD_TYPE:
						newRes = (Restriction) Restriction.createSignRestriction(REPORTSIGNATURE.get(editRestrictionFieldValue), editRestrictionFieldOperation, null);
						break;
					default:
						break;
				}

				restrictions.remove(0);
				restrictions.add(0, new RestrictionData(newRes, editRestrictionFieldValue));

			}

		}
		editRestrictionFieldOperation = StatisticsManager.EQUAL_OPERATION;
		editRestrictionRestrictionUnion = StatisticsManager.AND_UNION;
		editRestrictionFieldType = StatisticsManager.APPLICATION_FIELD_TYPE;
		editRestrictionFieldValue = null;

		return null;
	}

	/**
	 * Action for selecting the current restriction.
	 * @param event Event.
	 */
	public void onSelectRestriction(ActionEvent event) {

		if (restrictionSelected != null && restrictionSelected.size() > 0) {

			Iterator<Object> iterator = restrictionSelected.getKeys();
			RestrictionData resSelected = null;
			while (iterator.hasNext()) {
				Object key = iterator.next();
				restrictionTable.setRowKey(key);
				if (restrictionTable.isRowAvailable()) {
					resSelected = (RestrictionData) restrictionTable.getRowData();
				}

			}

			if (resSelected != null) {
				editRestrictionFieldOperation = resSelected.getFieldOperation();
				editRestrictionRestrictionUnion = resSelected.getRestrictionUnion();
				editRestrictionFieldType = resSelected.getFieldType();
				editRestrictionFieldValue = resSelected.getValueDescription();
			}

		}
	}

	/**
	 * Gets the field description.
	 * @param fieldId	Field identifier
	 * @return	Descriptive text for the field.
	 */
	private String getFieldDescription(int fieldId) {
		switch (fieldId) {
			case StatisticsManager.SERVICE_FIELD_TYPE:
				return Language.getMessage(LanguageKeys.WSTAT_SERVICEID);
			case StatisticsManager.APPLICATION_FIELD_TYPE:
				return Language.getMessage(LanguageKeys.WSTAT_APP);
			case StatisticsManager.RESULT_CODE_FIELD_TYPE:
				return Language.getMessage(LanguageKeys.WSTAT_CODRES);
			case StatisticsManager.SIGN_FIELD_TYPE:
				return Language.getMessage(LanguageKeys.WSTAT_REPORT);
			case StatisticsManager.UO_FIELD_TYPE:
				return Language.getMessage(LanguageKeys.WSTAT_UO);
			default:
				return "";
		}

	}

}
